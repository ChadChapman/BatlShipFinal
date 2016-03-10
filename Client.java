// Imports the necessary packages.
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
  public static final int LISTENING_PORT = 32007;
  private DataInputStream incoming;
  private DataOutputStream outgoing;

  // This function will open a socket on the listening port
  // to the address entered by the user. It will return 
  // true if the player is selected to go first, false if not.
  public boolean connect() throws IOException {
    System.out.print("Enter host name or IP address: ");
    Scanner stdin = new Scanner(System.in);
    String hostName = stdin.nextLine();
    Socket socket = new Socket(hostName, LISTENING_PORT);
    // Opens input and output streams on the socket connection.
    incoming = new DataInputStream(socket.getInputStream());
    outgoing = new DataOutputStream(socket.getOutputStream());
    System.out.println("Connected, waiting for other player.");
    // Receives which player goes first from the server.
    boolean goingFirst = incoming.readBoolean();
    return goingFirst;
  }

  // This function will accept a row and column as parameters
  // and send them to the server. It will return the result of the 
  // shot (hit, miss, etc) as an int.
  public int sendShot(int shotRow, int shotCol) throws IOException {
    outgoing.writeInt(shotRow);
    outgoing.writeInt(shotCol);
    int response = incoming.readInt();
    return response;
  }
  
  // Function will receive a shot through the server. 
  // It will return the shot to the caller in an int array.
  public int[] receiveShot() throws IOException {
    int[] shot = new int[2];
    shot[0] = incoming.readInt();
    shot[1] = incoming.readInt();
    return shot;
  }
  
  // This fuction is used to send the result of a 
  // received shot to the server.
  public void sendShotResult(int result) throws IOException {
    outgoing.writeInt(result);
  }
}
