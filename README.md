# Follow Me
 ● When a key is pressed in server (instruction), the client receives it; and has a timer
counting up to X seconds in which the same key has to be pressed.
● The server verifies if the keypress sent by client matched “the instruction” and assign +1
point on correct, -1 on a wrong key and 0 for timeouts.
● The game is over when the score reaches either +10 points or -3 points.
● The game is also over if the client does not respond for 3 continuous instructions.
● Each score update should send the score back to the connected client.
● The client should also know the timeout value for each instruction received.


### Installation

**Server**
```sh
$ git clone https://github.com/balu-sk93/fm-server.git
$ cd fm-server
$ mvn clean install
$ java -jar target/fm-server-0.0.1-SNAPSHOT.jar
```
Server will start on port 8090

**Client**
https://fm-server-1.herokuapp.com/

**Steps** 
1. Connect to server on ws://localhost:8090/fm.
2. Enter the instruction in server terminal.
3. Client gets the notification on server key press.
4. Enter the value on client terminal.
5. Server compare the values and responds with the score.

