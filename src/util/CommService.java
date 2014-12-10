package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import paxos.*;

public class CommService extends Thread {

	ArrayList<Endpoint> servers;
	HashMap<Integer, Proposer> proposer;
	HashMap<Integer, Acceptor> acceptor;
	HashMap<Integer, Learner> learner;
	private int port;
	private Log log;
	private int id;
	private int numberOfMajority;
	public boolean running = true;

	public CommService(ArrayList<Endpoint> servers, int id, int numberOfMajority) {
		this.servers = servers;
		this.id = id;
		this.numberOfMajority = numberOfMajority;
	}

	public void Init(HashMap<Integer, Proposer> proposer,
			HashMap<Integer, Acceptor> acceptor,
			HashMap<Integer, Learner> learner, int port, Log log) {
		this.proposer = proposer;
		this.acceptor = acceptor;
		this.learner = learner;
		this.port = port;
		this.log = log;
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
		boolean s = false;
		try {
			Socket echoSocket = new Socket(server, port);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(),
					true);
			out.print(msg);
			out.println();
			echoSocket.close();
			s = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SendPrepare(BallotNumber bal, int logIndex) {
		SendToAll(new Message.Prepare(bal, logIndex).toMsg());
	}

	public void SendACK(BallotNumber bal, BallotNumber accpetNum,
			Integer acceptVal, String ip, int port, int logIndex) {
		SendTo(new Endpoint(ip, port), new Message.ACK(bal, accpetNum,
				acceptVal, logIndex).toMsg());
	}

	public void SendAccept(BallotNumber bal, Integer acceptVal, int logIndex) {
		SendToAll(new Message.Accept(bal, acceptVal, logIndex).toMsg());
	}

	public void SendDecide(BallotNumber bal, Integer acceptVal, int logIndex) {
		SendToAll(new Message.Decide(bal, acceptVal, logIndex).toMsg());
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
				if (!running)
					continue;
				new Thread() {
					public void run() {
						Call(message, address.getHostName(),
								clientSocket.getLocalPort());
					}
				}.start();
				clientSocket.close();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return "";
	}

	private void Call(String readLine, String ip, int port) {
		if (readLine != null) {
			// System.out.println("Port: " + port + " Receiving: " + readLine);
			if (readLine.contains(":")) {
				String type = readLine.split(":")[0];
				String msg = readLine.split(":")[1];
				int index = Integer.parseInt(readLine.substring(readLine
						.lastIndexOf(";") + 1));
				Create(index);
				if (index < log.Size()) {
					if (type.equals("Decide")) {
						Message.Decide message = new Message.Decide(msg);
						learner.get(index).ReceiveDecide(message.accpetNum,
								message.acceptVal);
					}
					return;
				}
				// System.out.println(log.Size());
				if (type.equals("Prepare")) {
					Message.Prepare message = new Message.Prepare(msg);
					port = 11111;
					acceptor.get(index).ReceivePrepare(message.bal, ip, port);
				} else if (type.equals("ACK")) {
					Message.ACK message = new Message.ACK(msg);
					proposer.get(index).ReceiveACK(message.bal,
							message.accpetNum, message.acceptVal);
				} else if (type.equals("Accept")) {
					Message.Accept message = new Message.Accept(msg);
					acceptor.get(index).ReceiveAccept(message.accpetNum,
							message.acceptVal);
					learner.get(index).ReceiveAccept(message.accpetNum,
							message.acceptVal);
				} else if (type.equals("Decide")) {
					Message.Decide message = new Message.Decide(msg);
					learner.get(index).ReceiveDecide(message.accpetNum,
							message.acceptVal);
				}
			}
		}
	}

	public void Create(int logIndex) {
		if (!proposer.containsKey(logIndex)) {
			Paxos paxos = new Paxos(numberOfMajority, logIndex);
			proposer.put(logIndex, new Proposer(numberOfMajority, id, paxos,
					this, log));
			acceptor.put(logIndex, new Acceptor(numberOfMajority, id, paxos,
					this));
			learner.put(logIndex, new Learner(numberOfMajority, id, paxos,
					this, log));
		}
	}
}
