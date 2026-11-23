# Lab 3 - B8ZS Line Coding over Sockets (Programmation socket et B8ZS)

## Objective

Implement two programs that use sockets to carry a B8ZS-encoded stream from an
encoder (client) to a decoder (server), which decodes it back to the original
bits.

## Requirements

- The client reads a string of 0s and 1s from the keyboard and encodes it to
  B8ZS, assuming the first 1 has positive polarity.
- The encoded stream is written with three characters: `+` (positive pulse),
  `-` (negative pulse) and `0` (no signal).
- Before sending, the client sends a "request to send" and waits for the
  server's "ready to receive".
- The server acknowledges the stream, decodes it to the original bits and
  prints the result.

## Running

```bash
javac *.java
java Server          # then, in a second terminal:
java Client
```

Type a binary string, for example `1100000000110000010`.

## Status

Partly complete.

- **Encoder: working.** `1100000000110000010` encodes to
  `+-000-+0+-+-00000+0`, with the eight zeros replaced by `000-+0+-`.
- **Decoder: not implemented.** `decodageB8ZS()` in `Server.java` is a stub
  that returns its input unchanged, so the encoded stream is echoed back
  instead of being decoded.
- **Handshake: not implemented.** The client sends the encoded stream
  immediately, without the request to send / ready to receive exchange.
