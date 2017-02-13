CS455 Assignment 1

To build with Maven :
There are two separate profiles that will send the Main Class to either MessagingNode or Registry.
mvn compile assembly:single -Pregistry
mvn compile assembly:single -PmessagingNode

To Run Maven JARs :
cd target/
java -jar registry-jar-with-dependencies.jar "50000"
java -jar messagingNode-jar-with-dependencies.jar "localhost" "50000"

To build with Make :
From the root directory :
make clean
make

To Run Make JARs :
java cs455.overlay.node.MessagingNode "localhost" "50000"
java cs455.overlay.node.Registry "50000"