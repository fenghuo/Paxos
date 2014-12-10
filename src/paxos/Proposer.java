package paxos;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import util.CommService;
import util.Log;

public class Proposer {

	private int id;
	private Paxos paxos;
	private CommService commService;
	protected HashMap<BallotNumber, Integer> countMajority = new HashMap<BallotNumber, Integer>();
	private Integer myVal;
	private Integer proposedVal;
	private BallotNumber bal;
	private Log log;
	public boolean succeed = false;

	public Proposer(int numberOfMajority, int id, Paxos paxos,
			CommService commService, Log log) {
		this.id = id;
		this.myVal = null;
		this.paxos = paxos;
		this.commService = commService;
		this.log = log;
	}

	public void SetLeader(Integer proposedVal) {
		this.proposedVal = proposedVal;
		new Thread() {
			public void run() {
				while (log.Size() <= paxos.logIndex) {
					Prepare();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void Prepare() {
		paxos.Increase(id);
		this.countMajority.put(paxos.ballotNumber, 0);
		commService.SendPrepare(paxos.ballotNumber, paxos.logIndex);
	}

	public synchronized void ReceiveACK(BallotNumber bal,
			BallotNumber accpetNum, Integer acceptVal) {
		if (!countMajority.containsKey(bal))
			countMajority.put(bal, 0);
		if (countMajority.get(bal) >= 0) {
			countMajority.put(bal, countMajority.get(bal) + 1);

			if (acceptVal != null) {
				if (myVal == null || accpetNum.compareTo(this.bal) > 0) {
					this.myVal = acceptVal;
					this.bal = accpetNum;
				}
			}

			if (this.countMajority.get(bal) >= this.paxos.numberOfMajority) {
				if (myVal == null)
					myVal = proposedVal;
				commService.SendAccept(paxos.ballotNumber, myVal,
						paxos.logIndex);
				succeed = true;
				this.countMajority.put(bal, -1);
			}
		}
		// from majority
		// send back to all;
	}

	public boolean Finished(int logIndex) {
		return log.Size() > logIndex;
	}

	public boolean Succeed() {
		return succeed;
	}
}
