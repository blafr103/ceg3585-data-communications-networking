#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

struct client_info {
    int  sockno;
    char ip[INET_ADDRSTRLEN];
    char name[100];
};

struct client_info clients[100];   // table of structs, not raw sockets
int n = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

// Forward msg to whoever registered as target_name, else tell the sender.
void send_to_target(char *msg, int sender, char *target_name)
{
    pthread_mutex_lock(&mutex);
    for (int i = 0; i < n; i++) {
        if (strcmp(clients[i].name, target_name) == 0) {
            send(clients[i].sockno, msg, strlen(msg), 0);
            pthread_mutex_unlock(&mutex);
            return;
        }
    }
    send(sender, "client not online\n", 18, 0);
    pthread_mutex_unlock(&mutex);
}

void *thread_recv_msg(void *arg)
{
    pthread_detach(pthread_self());
    struct client_info *cl = arg;
    char msg[500];
    int len;

    // Each message is "target:body". Forward "sendername:body".
    while ((len = recv(cl->sockno, msg, sizeof(msg) - 1, 0)) > 0) {
        msg[len] = '\0';

        char *sep = strchr(msg, ':');
        if (sep == NULL)              // no target -> malformed, ignore
            continue;
        *sep = '\0';                  // split in place
        char *target = msg;
        char *body   = sep + 1;

        char out[600];
        snprintf(out, sizeof(out), "%s:%s", cl->name, body);
        send_to_target(out, cl->sockno, target);
    }

    // recv <= 0: disconnected. Remove from table.
    pthread_mutex_lock(&mutex);
    printf("%s disconnected\n", cl->ip);
    for (int i = 0; i < n; i++) {
        if (clients[i].sockno == cl->sockno) {
            for (int j = i; j < n - 1; j++)
                clients[j] = clients[j + 1];
            n--;
            break;                    // found and removed; stop
        }
    }
    pthread_mutex_unlock(&mutex);

    close(cl->sockno);
    free(cl);
    return NULL;
}

int main(int argc, char *argv[])
{
    struct sockaddr_in my_addr, their_addr;
    int server_socket, client_socket;
    socklen_t their_addr_size;
    int portno;
    pthread_t thread_recv_ID;
    char ip[INET_ADDRSTRLEN];
    char name[100];

    if (argc != 2) {
        fprintf(stderr, "usage: %s <port>\n", argv[0]);
        exit(1);
    }
    portno = atoi(argv[1]);

    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    memset(my_addr.sin_zero, '\0', sizeof(my_addr.sin_zero));
    my_addr.sin_family = AF_INET;
    my_addr.sin_port = htons(portno);
    my_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    their_addr_size = sizeof(their_addr);

    if (bind(server_socket, (struct sockaddr *)&my_addr, sizeof(my_addr)) != 0) {
        perror("binding unsuccessful");
        exit(1);
    }
    if (listen(server_socket, 5) != 0) {
        perror("listening unsuccessful");
        exit(1);
    }

    while (1) {
        if ((client_socket = accept(server_socket,
                (struct sockaddr *)&their_addr, &their_addr_size)) < 0) {
            perror("accept unsuccessful");
            exit(1);
        }

        // Registration: the client's first message is its username.
        int rlen = recv(client_socket, name, sizeof(name) - 1, 0);
        if (rlen <= 0) { close(client_socket); continue; }
        name[rlen] = '\0';
        name[strcspn(name, "\r\n")] = '\0';   // strip any trailing newline

        inet_ntop(AF_INET, &their_addr.sin_addr, ip, INET_ADDRSTRLEN);
        printf("%s connected as %s\n", ip, name);
        printf("New Client connected from port# %d and IP %s\n",
               ntohs(their_addr.sin_port), inet_ntoa(their_addr.sin_addr));

        struct client_info *cl = malloc(sizeof *cl);
        cl->sockno = client_socket;
        strcpy(cl->name, name);
        strcpy(cl->ip, ip);

        pthread_mutex_lock(&mutex);
        clients[n] = *cl;             // value copy into the table
        n++;
        pthread_mutex_unlock(&mutex);

        pthread_create(&thread_recv_ID, NULL, thread_recv_msg, cl);
    }
    return 0;
}
