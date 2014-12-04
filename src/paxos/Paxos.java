package paxos;

public class Paxos {
	public int numberOfMajority;
	public BallotNumber ballotNumber;
	public BallotNumber acceptNumber;
	public Integer acceptVal;

	public Paxos(int numberOfMajority) {
		this.numberOfMajority = numberOfMajority;
		this.ballotNumber = new BallotNumber(0, 0);
		this.acceptNumber = new BallotNumber(0, 0);
		this.acceptVal = null;
	}

	public void Increase(int processerId) {
		this.ballotNumber.num++;
		this.ballotNumber.processId = processerId;
	}

}
