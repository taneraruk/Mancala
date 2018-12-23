
# Game
This is a web-based game where multiple users can play at the same time. 
To start the game, two client should be connected. 

## Board Setup
Each of the two players has his six pits in front of him. To the right of the six pits,
each player has a larger pit. In each of the six round pits are put six stones when
the game starts.

## Rules
### Game Play
The player who begins with the first move picks up all the stones in anyone of his
own six pits, and sows the stones on to the right, one in each of the following
pits, including his own big pit. No stones are put in the opponents&#39; big pit. If the
player&#39;s last stone lands in his own big pit, he gets another turn. This can be
repeated several times before it&#39;s the other player&#39;s turn.

### Capturing Stones
During the game the pits are emptied on both sides. Always when the last stone
lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other players&#39; pit) and puts them in his own pit.

### The Game Ends
The game is over as soon as one of the sides run out of stones. The player who
still has stones in his pits keeps them and puts them in his/hers big pit. Winner of
the game is the player who has the most stones in his big pit.

### Restart game
In order to restart game, both clients should click 'Restart' button.

## Setup & Run
### Prerequisites
- Maven
- Java JDK

Build: 

```
mvn clean package
```

Run:

```
java -jar game.jar
```

Play:

```
http://localhost:8080
```
"# Mancala" 
