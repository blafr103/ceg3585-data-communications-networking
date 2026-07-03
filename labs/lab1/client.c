#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>

char username[100];

// Receive thread: continuously receives messages from server
void *thread_recv_msg(void *sock)
{
    int their_sock = *((int *)sock);
    char msg[500];
    int len;

    while ((len = recv(their_sock, msg, 500, 0)) > 0) {
        msg[len] = '\0';
        fputs(msg, stdout);
        memset(msg, 0, sizeof(msg));
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    // argv[1] = server IP
    // argv[2] = username
    // argv[3] = port

    if (argc != 4) {
        printf("Usage: ./client <server_ip> <username> <port>\n");
        exit(1);
    }

    char *server_ip = argv[1];
    strcpy(username, argv[2]);
    int port = atoi(argv[3]);

    // socket setup
    int client_socket = socket(AF_INET, SOCK_STREAM, 0);

    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    server_addr.sin_addr.s_addr = inet_addr(server_ip);

    // CONNECT
    if (connect(client_socket,
                (struct sockaddr *)&server_addr,
                sizeof(server_addr)) < 0) {
        perror("connection not established");
        exit(1);
    }

    printf("Connected to server\n");

    // register username
    send(client_socket, username, strlen(username), 0);

    // receiver thread
    pthread_t thread_recv_ID;
    pthread_create(&thread_recv_ID, NULL, thread_recv_msg, &client_socket);

    char msg[500];
    char out[600];
    int len;

    // message loop
    while (fgets(msg, sizeof(msg), stdin) != NULL) {

        msg[strcspn(msg, "\n")] = 0;

        snprintf(out, sizeof(out), "%s:%s", username, msg);

        len = send(client_socket, out, strlen(out), 0);

        if (len < 0) {
            perror("send failed");
            break;
        }

        memset(msg, 0, sizeof(msg));
        memset(out, 0, sizeof(out));
    }

    pthread_join(thread_recv_ID, NULL);
    close(client_socket);

    return 0;
}
