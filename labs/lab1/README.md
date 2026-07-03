# Lab 1 — Chat Application (Application de bavardage)

## Objective

Design and implement a connection-oriented (TCP/IP) chat client and server
using socket programming. TCP/IP is specified for its reliability: the
transport layer itself performs the checksum, retransmits lost message
segments, discards duplicates, and adapts the rate.

## Requirements

- May be implemented in C/C++, Java, or Python, using socket programming.
- A client that wants to communicate with another client must first register
  with the server.
- The client then sends its message to the intended recipient by passing
  through the server. If that recipient is online, the server relays the
  message, otherwise the server returns "the client is not online."
- When a client disconnects, the server closes the socket.

## Provided starter and environment

- A starter application is provided, demonstrated on an Ubuntu-based Linux
  distribution.
- The system consists of one server program and three client programs.
- Launch order: start the server on port **4444** first, then start the
  clients. Compile both programs before executing.
- In the provided starter, a message sent by one client is received by **all**
  clients (broadcast). The task is to instead deliver each message to a chosen
  recipient, identified by a username (e.g. `mohamed`).

Build and run (per the manual's demonstration):

```bash
gcc -o server server.c -lpthread
./server 4444

gcc -o client client.c -lpthread
./client mohamed 4444
./client jean 4444
./client alain 4444
```

## Deliverables

a. Brief description of the purpose and theory of the problem.
b. Brief explanation of the solution algorithm.
c. Design document using UML diagrams.
d. Screenshots demonstrating the application.
e. Discussion.
f. Conclusion.

## Evaluation

Application functionality 50%, report 50%.

---

## Note on commit history
This lab was retroactively fixed, post-course submission.
