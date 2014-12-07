package util;

public class Bank {

	Server server;

	public Bank(Server server) {
		this.server = server;
	}

	public boolean deposit(Integer value) {
		int bal = balance();
		return server.SetValue(value + bal);
	}

	public boolean withdraw(Integer value) {
		int bal = balance();
		if (bal < value)
			return false;
		return server.SetValue(bal - value);
	}

	public Integer balance() {
		return 0;
	}

	public void fail() {
		server.commService.running = false;
	}

	public void unfail() {
		server.commService.running = true;
	}
}
