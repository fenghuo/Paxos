package paxos;

import java.util.concurrent.atomic.AtomicInteger;

import util.CommService;

public class Proposer {

	private int id;
	private Paxos paxos;
	private CommService commService;
	private AtomicInteger countMajority = new AtomicInteger(0);
	private Integer myVal;
	private Integer proposedVal;
	private BallotNumber bal;

	public Proposer(int numberOfMajority, int id, Paxos paxos,
			CommService commService) {
		this.id = id;
		this.myVal = null;
		this.paxos = paxos;
		this.commService = commService;
	}

	public void Prepare(Integer proposedVal) {
		paxos.Increase(id);
		this.countMajority = new AtomicInteger(0);
		this.proposedVal = proposedVal;
		commService.SendPrepare(paxos.ballotNumber);
	}

	public synchronized void ReceiveACK(BallotNumber bal,
			BallotNumber accpetNum, Integer acceptVal) {
		if (bal.equals(paxos.ballotNumber) && this.countMajority.get() >= 0) {
			this.countMajority.getAndIncrement();
			System.out.println(this);

			if (acceptVal != null) {
				if (myVal == null || accpetNum.compareTo(this.bal) > 0) {
					this.myVal = acceptVal;
					this.bal = accpetNum;
				}
			}

			if (this.countMajority.get() >= this.paxos.numberOfMajority) {
				if (myVal == null)
					myVal = proposedVal;
				commService.SendAccept(paxos.ballotNumber, myVal);
				this.countMajority.set(-1);
			}
		}
		// from majority
		// send back to all;
	}
}
