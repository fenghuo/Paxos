package util;

import java.util.ArrayList;

import paxos.Acceptor;
import paxos.Learner;
import paxos.Paxos;
import paxos.Proposer;

public class Server {
	Proposer proposer;
	Acceptor acceptor;
	Learner learner;

	public Server(int id, int numberOfMajority, ArrayList<Endpoint> servers,
			int port) {
		Paxos paxos = new Paxos(numberOfMajority);
		CommService commService = new CommService(servers);
		this.proposer = new Proposer(numberOfMajority, id, paxos, commService);
		this.acceptor = new Acceptor(numberOfMajority, id, paxos, commService);
		this.learner = new Learner(numberOfMajority, id, paxos, commService);
		commService.Init(proposer, acceptor, learner, port);
		commService.start();
	}
}
