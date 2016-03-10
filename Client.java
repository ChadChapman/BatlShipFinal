import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
  public static final int LISTENING_PORT = 32007;
  private DataInputStream incoming;
  private DataOutputStream outgoing;

  public boolean connect() throws IOException {

    System.out.print("Enter host name or IP address: ");
    Scanner stdin = new Scanner(System.in);
    String hostName = stdin.nextLine();

    Socket socket = new Socket(hostName, LISTENING_PORT);
    incoming = new DataInputStream(socket.getInputStream());
    outgoing = new DataOutputStream(socket.getOutputStream());

    System.out.println("Connected, waiting for other player.");
    boolean goingFirst = incoming.readBoolean();

    return goingFirst;
  }

  public int sendShot(int shotRow, int shotCol) throws IOException {
    outgoing.writeInt(shotRow);
    outgoing.writeInt(shotCol);
    int response = incoming.readInt();
    return response;
  }

  public int[] receiveShot() throws IOException {
    int[] shot = new int[2];
    shot[0] = incoming.readInt();
    shot[1] = incoming.readInt();
    return shot;
  }

  public void sendShotResult(int result) throws IOException {
    outgoing.writeInt(result);
  }
}
