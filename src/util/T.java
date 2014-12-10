package util;

import java.io.PrintWriter;
import java.net.Socket;

public class T {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String server = "54.169.187.65";
		int port = 11111;
		try {
			Socket echoSocket = new Socket(server, port);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(),
					true);
			out.print("Prepare:1;1;0");
			out.println();
			echoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
