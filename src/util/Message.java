package util;

import paxos.BallotNumber;

public class Message {

	public static class Prepare {
		public BallotNumber bal;
		public int logIndex;

		public Prepare(BallotNumber bal, int logIndex) {
			this.bal = bal;
			this.logIndex = logIndex;
		}

		public Prepare(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 2) {
				bal = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				logIndex = Integer.parseInt(res[2]);
			}
		}

		public String toMsg() {
			return "Prepare:" + bal.toMsg() + logIndex;
		}
	}

	public static class ACK {
		public BallotNumber bal;
		public BallotNumber accpetNum;
		public Integer acceptVal;
		public int logIndex;

		public ACK(BallotNumber bal, BallotNumber accpetNum, Integer acceptVal,
				int logIndex) {
			this.bal = bal;
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
			this.logIndex = logIndex;
		}

		public ACK(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 6) {
				bal = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				accpetNum = new BallotNumber(Integer.parseInt(res[2]),
						Integer.parseInt(res[3]));
				try {
					acceptVal = Integer.parseInt(res[4]);
				} catch (Exception e) {
					acceptVal = null;
				}
				logIndex = Integer.parseInt(res[5]);
			}
		}

		public String toMsg() {
			return "ACK:" + bal.toMsg() + accpetNum.toMsg() + acceptVal + ";"
					+ logIndex;
		}
	}

	public static class Accept {
		public BallotNumber accpetNum;
		public Integer acceptVal;
		public int logIndex;

		public Accept(BallotNumber accpetNum, Integer acceptVal, int logIndex) {
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
			this.logIndex = logIndex;
		}

		public Accept(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 4) {
				accpetNum = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				try {
					acceptVal = Integer.parseInt(res[2]);
				} catch (Exception e) {
					acceptVal = null;
				}
				logIndex = Integer.parseInt(res[3]);
			}
		}

		public String toMsg() {
			return "Accept:" + accpetNum.toMsg() + acceptVal.toString() + ";"
					+ logIndex;
		}
	}

	public static class Decide {
		public BallotNumber accpetNum;
		public Integer acceptVal;
		public int logIndex;

		public Decide(BallotNumber accpetNum, Integer acceptVal, int logIndex) {
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
			this.logIndex = logIndex;
		}

		public Decide(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 4) {
				accpetNum = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				try {
					acceptVal = Integer.parseInt(res[2]);
				} catch (Exception e) {
					acceptVal = null;
				}
				logIndex = Integer.parseInt(res[3]);
			}
		}

		public String toMsg() {
			return "Decide:" + accpetNum.toMsg() + acceptVal.toString() + ";"
					+ logIndex;
		}
	}

	public String type;
}
