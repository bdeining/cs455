CS455 Assignment 1

To build with Make :
From the root directory :
make clean
make

To Run Make JARs :
java cs455.scaling.server.Server "50000" "10"
java cs455.scaling.client.Client "localhost" "50000" "1"

TODO:
Pass state with bytebuffer - look for race condition - could be on client
Performance - 100 clients
CS machines - working so far