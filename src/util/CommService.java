package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import paxos.*;

public class CommService extends Thread {

	ArrayList<Endpoint> servers;
	private Proposer proposer;
	private Acceptor acceptor;
	private Learner learner;
	private int port;

	public CommService(ArrayList<Endpoint> servers) {
		this.servers = servers;
	}

	public void Init(Proposer proposer, Acceptor acceptor, Learner learner,
			int port) {
		this.proposer = proposer;
		this.acceptor = acceptor;
		this.learner = learner;
		this.port = port;
	}

	public void run() {
		Receive(port);
	}

	public void addServer(String ip, int port) {
		servers.add(new Endpoint(ip, port));
	}

	public void SendTo(Endpoint endpoint, String msg) {
		System.out.println("Sending : " + msg + " --  " + port + "->"
				+ endpoint.ip + ":" + endpoint.port);
		String server = endpoint.ip;
		int port = endpoint.port;
		try {
			Socket echoSocket = new Socket(server, port);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(),
					true);
			out.print(msg);
			out.println();
			echoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SendPrepare(BallotNumber bal) {
		SendToAll(new Message.Prepare(bal).toMsg());
	}

	public void SendACK(BallotNumber bal, BallotNumber accpetNum,
			Integer acceptVal, String ip, int port) {
		SendTo(new Endpoint(ip, port), new Message.ACK(bal, accpetNum,
				acceptVal).toMsg());
	}

	public void SendAccept(BallotNumber bal, Integer acceptVal) {
		SendToAll(new Message.Accept(bal, acceptVal).toMsg());
	}

	public void SendDecide(BallotNumber bal, Integer acceptVal) {
		SendToAll(new Message.Decide(bal, acceptVal).toMsg());
	}

	public void SendToAll(String msg) {
		for (Endpoint p : servers)
			SendTo(p, msg);
	}

	public String Receive(int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				final Socket clientSocket = serverSocket.accept();
				final BufferedReader in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				final InetSocketAddress address = (InetSocketAddress) clientSocket
						.getRemoteSocketAddress();
				final String message = in.readLine();
				new Thread() {
					public void run() {
						Call(message, address.getHostName(),
								clientSocket.getLocalPort());
					}
				}.start();
				clientSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private void Call(String readLine, String ip, int port) {
		if (readLine != null) {
			System.out.println("Port: " + port + " Receiving: " + readLine);
			if (readLine.contains(":")) {
				String type = readLine.split(":")[0];
				String msg = readLine.split(":")[1];
				if (type.equals("Prepare")) {
					Message.Prepare message = new Message.Prepare(msg);
					port = 11111;
					acceptor.ReceivePrepare(message.bal, ip, port);
				} else if (type.equals("ACK")) {
					Message.ACK message = new Message.ACK(msg);
					proposer.ReceiveACK(message.bal, message.accpetNum,
							message.acceptVal);
				} else if (type.equals("Accept")) {
					Message.Accept message = new Message.Accept(msg);
					acceptor.ReceiveAccept(message.accpetNum, message.acceptVal);
					learner.ReceiveAccept(message.accpetNum, message.acceptVal);
				} else if (type.equals("Decide")) {
					Message.Decide message = new Message.Decide(msg);
					learner.ReceiveDecide(message.accpetNum, message.acceptVal);
				}
			}
		}
	}
}
