# CEG3585 - Introduction to Data Communications and Networking

Coursework for **CEG3585** at the University of Ottawa: labs and assignments spanning the physical, data link, and network layers of the OSI reference model.

## Course scope

- **Physical layer** - Fourier analysis of signals, analog and digital transmission, transmission impairments, channel capacity, and signal encoding schemes
- **Transmission media** - guided (cable) and wireless propagation
- **Data link layer** - framing, error detection and correction, flow control, HDLC
- **Multiplexing** - FDM, synchronous TDM, statistical TDM, xDSL/ADSL
- **Switching** - circuit switching and packet switching
- **LANs** - topologies, bridges, switches, Ethernet, token ring, fibre channel
- **Routing** - routing algorithms and protocols (RIP, OSPF, BGP), Internet architecture

## Repository structure

```
.
├── assignments/
│   ├── dev1/
│   ├── dev2/
│   ├── dev3/
│   └── dev4/
└── labs/
    ├── lab1/          # TCP/IP chat application (C)
    ├── lab2/          # Fourier series + network transport (Java)
    ├── lab3/          # B8ZS line coding over sockets
    └── lab4/          # HDLC sliding window over a multipoint link (Java)
```

## Labs

### Lab 1 — Chat application (C)
A connection-oriented (TCP/IP) client/server chat built on sockets. Clients register with the server; a message addressed to a username is routed to that recipient through the server, which reports if the recipient is offline and closes the socket on disconnect.

Files: `server.c`, `client.c`.

```bash
gcc -o server server.c -lpthread
gcc -o client client.c -lpthread
./server 4444        # then, per client:
./client  4444
```

### Lab 2 — Fourier series and network transport (Java)
Computes the Fourier coefficients (a₀, aₙ, bₙ) of the six standard waveforms in Table 1 (A = 10, T = 2) up to the 100th harmonic and plots the reconstructed curve locally. A client/server pair then sends the curve data over the network to be displayed on a remote machine.

Files: `FourierSeries.java`, `FourierF1.java`–`FourierF6.java` (the six waveforms — square, rectangular pulse train, sawtooth, triangular, half-wave rectified sine, full-wave rectified sine), `Client.java`, `Server.java`.

```bash
javac *.java
java Server          # then, in a second terminal:
java Client
```

### Lab 3 — B8ZS line coding over sockets (Java)
An encoder (client) and decoder (server) communicating over a socket. The client reads a binary string from the keyboard and encodes it to B8ZS — represented as `+`, `-`, and `0` for positive pulse, negative pulse, and no signal, with the first `1` taken as positive polarity. After a request-to-send / ready-to-receive handshake, the encoded stream is transmitted; the server acknowledges, decodes it back to the original bitstream, and prints it.

Example: `1100000000110000010` → `+-000-+0+-+-00000+0` → decoded back to `1100000000110000010`.

### Lab 4 — HDLC sliding window over a multipoint link (Java)
Simulates a multipoint network with one primary and two secondary stations. The physical layer is emulated over sockets (`PhysicalLayer` / `PhysicalLayerServer`), HDLC entities handle the data link layer (`PrimaryHDLCDataLink` / `SecondaryHDLCDataLink`), and `PrimaryStation` / `SecondaryStation` are the applications exchanging a message.

The implemented work completes `dlDataRequest()` in `SecondaryHDLCDataLink`: segmenting the message into I-frames of ≤ 32 data bytes, advancing send sequence numbers modulo the sequence space, and driving a size-4 anticipation (sliding) window acknowledged by RR frames. Error recovery is out of scope — no FCS, REJ, or SREJ.

Run each command in its own terminal, in order:
```bash
java PhysicalLayerServer
java SecondaryStation 1
java SecondaryStation 2
java PrimaryStation
```

## Assignments

Assignment folders (`dev1`–`dev4`) are reserved and will be populated as work is added.

## Languages

C, Java.
