package paxos;

import java.util.HashMap;
import java.util.HashSet;

import util.CommService;
import util.Log;

public class Acceptor {
	private int id;
	private Paxos paxos;
	private CommService commService;
	private HashSet<BallotNumber> first;
	private Log log;

	public Acceptor(int numberOfMajority, int id, Paxos paxos,
			CommService commService, Log log) {
		this.id = id;
		this.paxos = paxos;
		this.commService = commService;
		this.first = new HashSet<BallotNumber>();
		this.log = log;
	}

	public synchronized void ReceivePrepare(BallotNumber bal, String ip,
			int port) {
		if (paxos.logIndex < log.Size())
			return;
		if (bal.compareTo(paxos.ballotNumber) >= 0) {
			paxos.ballotNumber = bal.copy();
			// send ack to serverId
			commService.SendACK(paxos.ballotNumber, paxos.acceptNumber,
					paxos.acceptVal, ip, port, paxos.logIndex);
		}
	}

	public synchronized void ReceiveAccept(BallotNumber bal, Integer val) {
		if (paxos.logIndex < log.Size())
			return;
		if (bal.compareTo(paxos.ballotNumber) >= 0) {
			paxos.ballotNumber = bal.copy();
			paxos.acceptVal = val;

			if (!first.contains(bal)) {
				commService.SendAccept(bal, val, paxos.logIndex);
				first.add(bal);
			}
			// send accept to all
		}
	}
}
