import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

	public static void main(String args[]) {
		Socket socket = null;
		BufferedReader bufferedReader = null;
		PrintStream printStream = null;
		BufferedReader clientMessageBufferedReader = null;
		try {
			socket = new Socket("127.0.0.1", 5000);
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printStream = new PrintStream(socket.getOutputStream());
			clientMessageBufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String clientMessage;
			String serverMessage;
			
			while (true) {
				System.out.print("Client : ");
				clientMessage = clientMessageBufferedReader.readLine();
				printStream.println(clientMessage);
				if (clientMessage.equalsIgnoreCase("BYE")) {
					System.out.println("Connection ended by client");
					break;
				}
				serverMessage = bufferedReader.readLine();
				System.out.print("Server : " + serverMessage + "\n");

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				printStream.close();
				socket.close();
				bufferedReader.close();
				clientMessageBufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
