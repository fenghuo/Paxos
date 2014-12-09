package paxos;

import java.util.HashMap;
import java.util.HashSet;

import util.CommService;
import util.Log;

public class Learner {

	private int id;
	private Paxos paxos;
	private CommService commService;
	private HashMap<BallotNumber, Integer> countAccept;
	private HashMap<BallotNumber, Integer> countDecide;
	private Log log;
	private boolean sending = false;

	public Learner(int numberOfMajority, int id, Paxos paxos,
			CommService commService, Log log) {
		this.id = id;
		this.paxos = paxos;
		this.commService = commService;
		this.countAccept = new HashMap<BallotNumber, Integer>();
		this.countDecide = new HashMap<BallotNumber, Integer>();
		this.log = log;
	}

	public void ReceiveAccept(BallotNumber bal, Integer val) {
		countAccept.put(bal,
				(countAccept.get(bal) == null ? 0 : countAccept.get(bal)) + 1);
		if (countAccept.get(bal) >= paxos.numberOfMajority) {
			// decide v
			commService.SendDecide(bal, val, paxos.logIndex);
		}
	}

	public void ReceiveDecide(final BallotNumber bal, final Integer val) {
		// decide v
		paxos.acceptVal = null;
		log.Write(bal, val, paxos.logIndex);

		countDecide.put(bal,
				(countDecide.get(bal) == null ? 0 : countDecide.get(bal)) + 1);
		if (!sending) {
			sending = true;
			new Thread() {
				public void run() {
					while (true) {
						paxos.acceptVal = null;
						commService.SendDecide(bal, val, paxos.logIndex);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}
}
