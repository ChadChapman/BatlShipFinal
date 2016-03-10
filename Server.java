import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
  public static final int LISTENING_PORT = 32007;
  Random random = new Random();

  public static void main(String[] args) {
    while(true) {
      Server server = new Server();
      try {
        server.run();
      } catch(IOException e) {
        System.err.println("Error: IOException, server restarting.");
      }
    }
  }

  public void run() throws IOException { 
	  ServerSocket listener; 
    Socket connection1 = null;
    Socket connection2 = null; 
    listener = new ServerSocket(LISTENING_PORT);

    boolean playing = true;
    while(playing) {
      try {
        System.out.println("Listening on port " + LISTENING_PORT);

       // listener listens for two connections
        while (connection1 == null || connection2 == null){
          // Connects the second player if player one has already connected
        if (connection1 != null) {
            connection2 = listener.accept();
            System.out.println("Player2 connected");
         // Else connects the first player
        } else {
            connection1 = listener.accept();
            System.out.println("Player1 connected");
          }
        }
      } catch (Exception e) {
        System.out.println("Sorry, the server has shut down.");
        System.out.println("Error: " + e);
        return;
      }
     // After two players are connected server launches playGame
      try {
        playGame(connection1, connection2);
      } catch(IOException e) {
        System.err.println("Connection error with game, starting new game.");
        connection1 = null;
        connection2 = null;
      }
    }
  }

 //randoms between one and zero to pick who goes first
  private int pickFirstPlayer() {
    return random.nextInt(2);
  }

  private void playGame(Socket player1, Socket player2) throws IOException {

    // Assigns two input streams one for each player
	DataInputStream in1 = new DataInputStream(player1.getInputStream());
    DataInputStream in2 = new DataInputStream(player2.getInputStream());

    //Assigns two output streams one for each player
    DataOutputStream out1 = new DataOutputStream(player1.getOutputStream());
    DataOutputStream out2 = new DataOutputStream(player2.getOutputStream());

    int firstPlayer = pickFirstPlayer();
    // if  a zero is rolled firstPlayer goes first 
    if(firstPlayer == 0) {
      System.out.println("Player 1 goes first.");
      out1.writeBoolean(true);
      out2.writeBoolean(false);
   // If a one is rolled player 2 goes first
    } else {
      System.out.println("Player 2 goes first.");
      out1.writeBoolean(false);
      out2.writeBoolean(true);
    }

    boolean gameIsOver = false;
    boolean firstPlayerTurn = (firstPlayer == 0);
    // While the game isn't over
    while(!gameIsOver) {
      // Data inputs and out puts to go back and forth between players
      DataInputStream activeIn;
      DataInputStream otherIn;
      DataOutputStream activeOut;
      DataOutputStream otherOut;

      // If it's first players turn
      if(firstPlayerTurn) {
        activeIn = in1;
        activeOut = out1;
        otherIn = in2;
        otherOut = out2;
     // if it's second players turn
      } else {
        activeIn = in2;
        activeOut = out2;
        otherIn = in1;
        otherOut = out1;
      }

      System.out.println("Receiving shot from player.");
      // Reads in a shot going to a row,col coordinate
      int shotRow = activeIn.readInt();
      int shotCol = activeIn.readInt();

      System.out.println("Got shot: " + shotRow + ", " + shotCol + ". Sending to other.");
      //
      otherOut.writeInt(shotRow);
      otherOut.writeInt(shotCol);

      System.out.println("Reading result from other.");
      int result = otherIn.readInt();

      System.out.println("Got " + result + ". Sending to player.");
      activeOut.writeInt(result);
      // Switches players to the next players turn after they Send a Shot
      firstPlayerTurn = !firstPlayerTurn;
    }
    
  }
}


