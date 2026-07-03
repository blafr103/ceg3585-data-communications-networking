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

void *thread_recv_msg(void *sock)
{
    int their_sock = *((int *)sock);
    char msg[500];
    int len;
    while ((len = recv(their_sock, msg, sizeof(msg) - 1, 0)) > 0) {
        msg[len] = '\0';
        fputs(msg, stdout);
        memset(msg, '\0', sizeof(msg));
    }
    return NULL;
}

int main(int argc, char *argv[])
{
    struct sockaddr_in their_addr;
    int client_socket;
    int portno;
    pthread_t thread_recv_ID;
    char msg[500];
    char res[650];
    char ip[INET_ADDRSTRLEN];
    int len;
    char target[100];

    if (argc != 4) {
        fprintf(stderr, "usage: %s <target> <username> <port>\n", argv[0]);
        exit(1);
    }
    strcpy(target, argv[1]);
    strcpy(username, argv[2]);
    portno = atoi(argv[3]);

    client_socket = socket(AF_INET, SOCK_STREAM, 0);
    memset(their_addr.sin_zero, '\0', sizeof(their_addr.sin_zero));
    their_addr.sin_family = AF_INET;
    their_addr.sin_port = htons(portno);
    their_addr.sin_addr.s_addr = inet_addr("127.0.0.1");

    if (connect(client_socket, (struct sockaddr *)&their_addr,
                sizeof(their_addr)) < 0) {
        perror("connection not established");
        exit(1);
    }
    inet_ntop(AF_INET, &their_addr.sin_addr, ip, INET_ADDRSTRLEN);
    printf("connected to %s, start chatting\n", ip);

    // Registration: first thing we send is username.
    send(client_socket, username, strlen(username), 0);

    pthread_create(&thread_recv_ID, NULL, thread_recv_msg, &client_socket);

    // Each line goes out as "target:message", server prepends registered name before delivering it.
    while (fgets(msg, 500, stdin) != NULL) {
        strcpy(res, target);
        strcat(res, ":");
        strcat(res, msg);
        len = write(client_socket, res, strlen(res));
        if (len < 0) {
            perror("message not sent");
            exit(1);
        }
        memset(msg, '\0', sizeof(msg));
        memset(res, '\0', sizeof(res));
    }
    pthread_join(thread_recv_ID, NULL);
    close(client_socket);
    return 0;
}
