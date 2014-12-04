package util;

public class Bank {

	Server server;

	public Bank(Server server) {
		this.server = server;
	}

	public boolean deposit(double value) {
		return false;
	}

	public boolean withdraw(double value) {
		return false;
	}

	public double balance(double value) {
		return 0;
	}

	public boolean fail() {
		return false;
	}

	public boolean unfail() {
		return false;
	}
}
