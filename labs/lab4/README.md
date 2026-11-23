# Lab 4 - HDLC over a Multipoint Network (Le HDLC et le réseau multipoint)

## Objective

Work with a multipoint network of one primary and two secondary stations,
study HDLC as a concrete data link protocol, and use its sliding
(anticipation) window.

The physical layer is simulated over sockets (`PhysicalLayer`,
`PhysicalLayerServer`), the data link layer is implemented by
`PrimaryHDLCDataLink` and `SecondaryHDLCDataLink`, and `PrimaryStation` and
`SecondaryStation` are the applications exchanging a message.

## Requirements

The task is to complete `dlDataRequest()` in `SecondaryHDLCDataLink`, which
sends a message from a secondary station to the primary as a series of HDLC
I-frames:

- Wait for a poll (an RR frame with the P/F bit set) before transmitting.
- Split the message into I-frames of at most 32 data bytes
  (`HdlcDefs.MAX_DATA_SIZE_BYTES`).
- Number frames with the send sequence variable `vs`, wrapping modulo the
  sequence space.
- Use a sliding window of size 4, with its right edge at `rhsWindow`, and
  advance it as RR frames acknowledge sent frames.
- Error recovery is out of scope: no FCS, REJ or SREJ.

## Running

```bash
javac *.java
java PhysicalLayerServer
java SecondaryStation 1
java SecondaryStation 2
java PrimaryStation
```

Run each command in its own terminal, in that order.

## Status

Incomplete. `checkNr()` is implemented, but `dlDataRequest()` still contains
the course's "Completer" placeholders, and the lab does not compile as
committed: `frame` is used before it is assigned in `dlDataRequest()`.
