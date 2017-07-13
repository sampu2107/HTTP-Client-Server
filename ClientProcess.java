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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * ClientProcess class takes Server name, Port, and the path of the file as
 * arguments. It constructs a request header based on the HTTP command, submits
 * a request to the server and displays the response.
 * 
 * @author Sampath Kumar
 * @version 1.0
 * @since 2017-06-04
 */

public class ClientProcess {

	/**
	 * Opens a connection based on the host and port number, declares a write to
	 * this URL, builds the HTTP GET request header, sends the request and
	 * displays the response. I/P, O/P streams and socket connections are closed
	 * upon successful response.
	 */
	public static void getMethod(String hostName, int port, String filePath) {

		Socket clientSocket = null;
		PrintStream request = null;
		DataInputStream response = null;
		try {
			System.out.println("Trying to connect: " + hostName
					+ " on port number: " + port);
			clientSocket = new Socket(hostName, port);
			String data = "";
			System.out.println("************************************");
			System.out.println("Connected to the server");
			System.out.println("************************************");
			request = new PrintStream(clientSocket.getOutputStream());
			System.out
					.println("Creating a GET request... Trying to get the file: "
							+ filePath);
			request.print("GET /" + filePath + "/ HTTP/1.1\r\n");
			request.print("Host: " + hostName + "\r\n");
			request.print("\r\n");
			request.flush();
			System.out.println("GET Request sent!");
			System.out.println("************************************");
			response = new DataInputStream(clientSocket.getInputStream());
			System.out.println("Response from the Server:");
			while ((data = response.readLine()) != null) {
				System.out.println(data);
			}
			System.out.println("Response Received!");
			System.out.println("************************************");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Exception Occured with the connection: "
					+ e.getMessage());
		} finally {
			try {
				request.close();
				response.close();
				clientSocket.close();
			} catch (IOException e) {
				System.out.println("IO Exception: " + e.getMessage());
			}
		}
	}

	/**
	 * Opens a connection based on the host and port number, declares a write to
	 * this URL, builds the HTTP POST request header and the contents needed to
	 * be uploaded, sends the request, and displays the response. I/P, O/P
	 * streams and socket connections are closed upon successful response.
	 */
	public static void putMethod(String hostName, int port, String filePath) {
		Socket clientSocket = null;
		PrintStream request = null;
		DataInputStream response = null;
		FileInputStream fileInputStream = null;
		String data = "";
		String serverResponse = "";
		byte[] bs = new byte[2048];
		int i = 0;
		try {
			clientSocket = new Socket(hostName, port);
			System.out.println("************************************");
			System.out.println("Connected to the server");
			System.out.println("************************************");
			request = new PrintStream(clientSocket.getOutputStream());
			System.out
					.println("Creating a PUT request... Trying to upload the file: "
							+ filePath);
			request.print("PUT /" + filePath + "/ HTTP/1.1\r\n");
			request.print("Host: " + hostName + "\r\n");
			request.print("\r\n");
			System.out.println("PUT Request Header sent!");
			System.out.println("************************************");
			File file = new File(filePath);
			int size = (int) Math.ceil((double) file.length() / bs.length);
			if (!file.exists() || !file.isFile()) {
				System.out.println("File doesn't exist in the given path: "
						+ filePath);
				return;
			}
			fileInputStream = new FileInputStream(filePath);
			request.println(size);
			while ((i = fileInputStream.read(bs)) != -1) {
				request.write(bs, 0, i);
			}
			response = new DataInputStream(clientSocket.getInputStream());
			System.out.println("Response from the Server:");
			while ((data = response.readLine()) != null) {
				serverResponse += data + "\n";
			}
			System.out.println("Response: " + serverResponse);
			System.out.println("************************************");
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Exception Occured with the connection: "
					+ e.getMessage());
		} finally {
			try {
				request.close();
				response.close();
				clientSocket.close();
			} catch (IOException e) {
				System.out.println("IO Exception: " + e.getMessage());
			}
		}
	}
}