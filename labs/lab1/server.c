#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

#define MAX 100

// user mapping: username -> socket
// required for private message routing
typedef struct {
    char name[100];
    int sock;
} user_t;

user_t users[MAX];
int user_count = 0;

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

// find socket using username
int find_socket(char *name)
{
    for (int i = 0; i < user_count; i++) {
        if (strcmp(users[i].name, name) == 0)
            return users[i].sock;
    }
    return -1;
}

// thread handling one client
void *handle_client(void *arg)
{
    user_t user = *(user_t *)arg;
    char msg[500];
    int len;

    while ((len = recv(user.sock, msg, sizeof(msg) - 1, 0)) > 0) {
        msg[len] = '\0';

        // expected format: target:message
        char target[100], text[400];

        char *sep = strchr(msg, ':');
        if (!sep) continue;
        
        *sep = '\0';
        
        strncpy(target, msg, sizeof(target) - 1);
        target[sizeof(target) - 1] = '\0';
        snprintf(text, sizeof(text), "%s", sep + 1);
        
        text[strcspn(text, "\r\n")] = 0;

        pthread_mutex_lock(&mutex);

        // lookup destination socket
        int dest_sock = find_socket(target);

        if (dest_sock != -1) {
            char out[600];

            // format: sender:message
            snprintf(out, sizeof(out), "%s: %s\n", user.name, text);
            if (send(dest_sock, out, strlen(out), 0) < 0) {
                perror("send failed");
            }
        } else {
            char err[] = "User not online\n";
            send(user.sock, err, strlen(err), 0);
        }

        pthread_mutex_unlock(&mutex);
    }

    // remove user on disconnect
    pthread_mutex_lock(&mutex);

    for (int i = 0; i < user_count; i++) {
        if (users[i].sock == user.sock) {
            for (int j = i; j < user_count - 1; j++) {
                users[j] = users[j + 1];
            }
            user_count--;
            break;
        }
    }

    pthread_mutex_unlock(&mutex);

    close(user.sock);
    return NULL;
}

int main(int argc, char *argv[])
{
    if (argc != 2) {
        printf("Usage: ./server <port>\n");
        exit(1);
    }

    int port = atoi(argv[1]);

    // create TCP socket
    int server_sock = socket(AF_INET, SOCK_STREAM, 0);

    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    server_addr.sin_addr.s_addr = INADDR_ANY;

    // bind socket
    if (bind(server_sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("bind failed");
        exit(1);
    }

    // listen for clients
    listen(server_sock, 10);

    printf("Server running on port %d\n", port);

    while (1) {
        struct sockaddr_in client_addr;
        socklen_t len = sizeof(client_addr);

        // accept new client
        int client_sock = accept(server_sock,
                                  (struct sockaddr *)&client_addr,
                                  &len);

        // first message = username
        char name[100];
        memset(name, 0, sizeof(name));
        memset(name, 0, sizeof(name));
        recv(client_sock, name, sizeof(name) - 1, 0);
        name[sizeof(name) - 1] = '\0';
        name[strcspn(name, "\r\n")] = 0;

        printf("User connected: %s\n", name);

        pthread_mutex_lock(&mutex);

        // store user in table
        if (user_count >= MAX) {
            close(client_sock);
            pthread_mutex_unlock(&mutex);
            continue;
        }
        
        if (strlen(name) == 0) {
            close(client_sock);
            pthread_mutex_unlock(&mutex);
            continue;
        }
        
        for (int i = 0; i < user_count; i++) {
            if (strcmp(users[i].name, name) == 0) {
                close(client_sock);
                pthread_mutex_unlock(&mutex);
                continue;
            }
        }

        strcpy(users[user_count].name, name);
        users[user_count].sock = client_sock;
        user_count++;

        pthread_mutex_unlock(&mutex);

        // allocate per-thread user copy
        user_t *u = malloc(sizeof(user_t));
        strcpy(u->name, name);
        u->sock = client_sock;

        pthread_t tid;
        pthread_create(&tid, NULL, handle_client, u);
        pthread_detach(tid);
    }

    return 0;
}
