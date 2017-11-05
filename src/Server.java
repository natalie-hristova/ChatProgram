import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private int port;
	private ServerSocket serverSocket = null;
	private Socket client = null;
	private ExecutorService pool = null;
	private int clientCount = 0;
	private static int MAX_CLIENTS = 10;

	public static void main(String[] args) throws IOException {
		Server server = new Server(5000);
		server.startServer();
	}

	Server(int port) {
		this.port = port;
		pool = Executors.newFixedThreadPool(MAX_CLIENTS);
	}

	public void startServer() throws IOException {
		serverSocket = new ServerSocket(5000);
		System.out.println("Server started...");
		while (true) {
			client = serverSocket.accept();
			clientCount++;
			ServerThread runnable = new ServerThread(client, clientCount, this);
			pool.execute(runnable);
		}

	}

	private static class ServerThread implements Runnable {
		
		Server server = null;
		Socket client = null;
		BufferedReader bufferedReader;
		PrintStream printStream;
		Scanner sc = new Scanner(System.in);
		int id;
		String serverMessage;
		String clientMessage;

		ServerThread(Socket client, int count, Server server) {

			this.client = client;
			this.server = server;
			this.id = count;
			System.out.println("Connection " + id + " with client " + client);

			try {
				bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				printStream = new PrintStream(client.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			boolean work = true;
			try {
				while (true) {
					clientMessage = bufferedReader.readLine();

					System.out.print("Client(" + id + ") :" + clientMessage + "\n");
					System.out.print("Server : ");
					serverMessage = sc.nextLine();
					if (serverMessage.equalsIgnoreCase("bye")) {
						printStream.println("BYE");
						work = false;
						System.out.println("Connection ended by server");
						break;
					}
					printStream.println(serverMessage);
				}
				if (!work) {
					System.out.println("Server cleaning up done.");
					System.exit(0);
				}
			} catch (IOException ex) {
				System.out.println("Error : " + ex);
			}
			finally{
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				printStream.close();
			}
		}
	}
}
