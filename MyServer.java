/*=============================================================================
|      Project:  HTTP Client and Server
|       Author:  Sampath Kumar Gunasekaran(sgunase2@uncc.edu)
|
|       Course:  ITCS 6166
|   Instructor:  Dewan Ahmed 
|     Due Date:  Jun 9 at 11:59PM
|
|     Language:  Java 
|	  Version :  1.8.0_101
|                
| Deficiencies:  No logical errors.
 *===========================================================================*/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * MyServer class is a multi-threaded server which takes Port as command line argument and sends the response
 * over the established connection for all the accepted clients.
 * 
 * @author Sampath Kumar
 * @version 1.0
 * @since 2017-06-04
 */

public class MyServer {

	public static void main(String[] args) {

		int portNumber = 0;
		ServerSocket serverSocket = null;
		Socket client = null;

		if (args.length == 1) {
			portNumber = Integer.parseInt(args[0]);
		} else {
			System.out
					.println("Invalid number of arguments. Expecting one argument");
			return;
		}
		try {
			System.out.println("Starting the server at port: " + portNumber);
			serverSocket = new ServerSocket(portNumber);
			stopResources(serverSocket);
			System.out.println("********Server is Ready********");
			while (true) {
				System.out.println("Waiting for clients...");
				client = serverSocket.accept();
				System.out.println("Following Client has been connected: "
						+ client.getInetAddress().getCanonicalHostName());
				Thread thread = new Thread(new ServerProcess(client));
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to clean up the resources and shut down the server gracefully on
	 * termination using ctrl+c command in the terminal.
	 */
	private static void stopResources(ServerSocket serverSocket) {
		final ServerSocket serverSock = serverSocket;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					System.out.println("Starting to shut down");
					if (!serverSock.isClosed()) {
						serverSock.close();
					}
					System.out.println("Cleaned up all the resources");
					System.out.println("The server is shut down!!!");
				} catch (IOException e) {
					System.out.println("Exception while trying to shut down");
				}
			}
		});
	}
}
