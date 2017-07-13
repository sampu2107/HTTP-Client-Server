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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

/**
 * ServerProcess class constructs the response header based on the HTTP request.
 * Server sends the requested object if the request is a GET or saves the files
 * locally if the request is a PUT.
 * 
 * @author Sampath Kumar
 * @version 1.0
 * @since 2017-06-04
 */

public class ServerProcess implements Runnable {

	private Socket client;
	PrintStream printStream;
	DataInputStream dataInputStream;
	String request = "";
	String temp = ".";
	String requestHeader = "";
	String response = "";
	String path = "SERVER_FOLDER";
	int i = 0;
	byte[] b = new byte[2048];

	public ServerProcess(Socket client) {
		this.client = client;

	}

	public void run() {
		try {
			System.out.println("Following thread has been started: "
					+ Thread.currentThread().getName());
			processRequest();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to process the request based on the type of request and sends the
	 * response.
	 */

	private void processRequest() throws IOException, InterruptedException {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
			dataInputStream = new DataInputStream(client.getInputStream());
			request = dataInputStream.readLine();
			dataInputStream.readLine();
			dataInputStream.readLine();
			StringBuilder sb = new StringBuilder();
			String fileName = request.split("\n")[0].split(" ")[1].split("/")[1];
			path = path + "/" + fileName; // Server Database
			if (request.split("\n")[0].contains("GET")) {
				if (checkFile(path)) {
					constructResponseHeader(200, sb);
					response = sb.toString();
					FileInputStream fis = new FileInputStream(path);
					while ((i = fis.read(b)) != -1) {
						response += new String(b, 0, i) + "\n";
					}
					sb.setLength(0);
					fis.close();
				} else {
					constructResponseHeader(404, sb);
					response = sb.toString();
				}
			} else if (request.split("\n")[0].contains("PUT")) {
				File f = new File(path);
				if (!f.exists()) {
					f.createNewFile();
				}
				int size = Integer.parseInt(dataInputStream.readLine());
				FileOutputStream fos = new FileOutputStream(path);
				while (size > 0) {
					i = dataInputStream.read(b);
					fos.write(b, 0, i);
					size--;
				}
				fos.close();
				constructResponseHeader(200, sb);
				response = sb.toString();
			} else {
				constructResponseHeader(301, sb);
				response = sb.toString();
			}
			printStream = new PrintStream(client.getOutputStream());
			printStream.println(response);
			Thread.sleep(1000);
			System.out.println("Client Connection: "
					+ client.getInetAddress().getCanonicalHostName()
					+ " closed");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				printStream.close();
				dataInputStream.close();
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method to construct the response header based on the status code.
	 */

	private void constructResponseHeader(int statusCode, StringBuilder sb) {
		if (statusCode == 200) {
			sb.append("HTTP/1.1 200 OK\r\n");
			sb.append("Date:" + new Date() + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("\r\n");
		} else if (statusCode == 404) {
			sb.append("HTTP/1.1 404 Not Found\r\n");
			sb.append("Date:" + new Date() + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("\r\n");
		} else if (statusCode == 301) {
			sb.append("HTTP/1.1 301 Bad Request\r\n");
			sb.append("Date:" + new Date() + "\r\n");
			sb.append("Server:localhost\r\n");
			sb.append("\r\n");
		}
	}

	/**
	 * Method to check if the requested file exists on the server database.
	 */
	private boolean checkFile(String file) {
		File serverFile = new File(file);
		return serverFile.exists() && serverFile.isFile();
	}
}
