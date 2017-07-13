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

/**
 * MyClient class takes Server name, Port, HTTP command and the path of the file
 * as command line arguments and submits a request to the server.
 * 
 * @author Sampath Kumar
 * @version 1.0
 * @since 2017-06-04
 */

public class MyClient {

	public static void main(String[] args) throws IOException {

		String hostName, command, filePath = "";
		int port = 0;
		if (args.length == 4) {
			hostName = args[0];
			port = Integer.parseInt(args[1]);
			command = args[2];
			filePath = args[3];
		} else {
			System.out
					.println("Invalid number of arguments. Expecting four arguments");
			return;
		}
		if (command.equals("GET")) {
			ClientProcess.getMethod(hostName, port, filePath);
		} else if (command.equals("PUT")) {
			ClientProcess.putMethod(hostName, port, filePath);
		} else {
			System.out
					.println("HTTP Command is not valid! System accepts only GET or PUT as commands.");
			return;
		}
	}
}