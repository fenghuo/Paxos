package util;

import paxos.BallotNumber;

public class Message {

	public static class Prepare {
		public BallotNumber bal;

		public Prepare(BallotNumber bal) {
			this.bal = bal;
		}

		public Prepare(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 2) {
				bal = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
			}
		}

		public String toMsg() {
			return "Prepare:" + bal.toMsg();
		}
	}

	public static class ACK {
		public BallotNumber bal;
		public BallotNumber accpetNum;
		public Integer acceptVal;

		public ACK(BallotNumber bal, BallotNumber accpetNum, Integer acceptVal) {
			this.bal = bal;
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
		}

		public ACK(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 5) {
				bal = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				accpetNum = new BallotNumber(Integer.parseInt(res[2]),
						Integer.parseInt(res[3]));
				try {
					acceptVal = Integer.parseInt(res[4]);
				} catch (Exception e) {
					acceptVal = null;
				}
			}
		}

		public String toMsg() {
			return "ACK:" + bal.toMsg() + accpetNum.toMsg() + acceptVal;
		}
	}

	public static class Accept {
		public BallotNumber accpetNum;
		public Integer acceptVal;

		public Accept(BallotNumber accpetNum, Integer acceptVal) {
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
		}

		public Accept(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 3) {
				accpetNum = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				try {
					acceptVal = Integer.parseInt(res[2]);
				} catch (Exception e) {
					acceptVal = null;
				}
			}
		}

		public String toMsg() {
			return "Accept:" + accpetNum.toMsg() + acceptVal.toString();
		}
	}

	public static class Decide {
		public BallotNumber accpetNum;
		public Integer acceptVal;

		public Decide(BallotNumber accpetNum, Integer acceptVal) {
			this.accpetNum = accpetNum;
			this.acceptVal = acceptVal;
		}

		public Decide(String msg) {
			String[] res = msg.split(";");
			if (res.length >= 3) {
				accpetNum = new BallotNumber(Integer.parseInt(res[0]),
						Integer.parseInt(res[1]));
				try {
					acceptVal = Integer.parseInt(res[2]);
				} catch (Exception e) {
					acceptVal = null;
				}
			}
		}

		public String toMsg() {
			return "Decide:" + accpetNum.toMsg() + acceptVal.toString();
		}
	}

	public String type;
}
