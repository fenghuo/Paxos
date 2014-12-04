package paxos;

import java.util.HashMap;
import java.util.HashSet;

import util.CommService;

public class Learner {

	private int id;
	private Paxos paxos;
	private CommService commService;
	private HashMap<BallotNumber, Integer> countAccept;
	private HashMap<BallotNumber, Integer> countDecide;

	public Learner(int numberOfMajority, int id, Paxos paxos,
			CommService commService) {
		this.id = id;
		this.paxos = paxos;
		this.commService = commService;
		this.countAccept = new HashMap<BallotNumber, Integer>();
		this.countDecide = new HashMap<BallotNumber, Integer>();
	}

	public void ReceiveAccept(BallotNumber bal, Integer val) {
		countAccept.put(bal,
				(countAccept.get(bal) == null ? 0 : countAccept.get(bal)) + 1);
		if (countAccept.get(bal) >= paxos.numberOfMajority) {
			// decide v
			paxos.acceptVal = null;
			commService.SendDecide(bal, val);
		}
	}

	public void ReceiveDecide(BallotNumber bal, Integer val) {
		// decide v
		paxos.acceptVal = null;
	}
}
