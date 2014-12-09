package util;

import java.util.ArrayList;
import java.util.Scanner;

public class Bank {

	static Server server;

	public Bank(Server server) {
		this.server = server;
	}

	public Bank(int id, int numberOfMajority, ArrayList<Endpoint> servers,
			int port) {
		server = new Server(id, numberOfMajority, servers, port);
	}

	public static boolean Deposit(Integer value) {
		int bal = Balance();
		return server.SetValue(value + bal);
	}

	public static boolean Withdraw(Integer value) {
		int bal = Balance();
		if (bal < value)
			return false;
		return server.SetValue(bal - value);
	}

	public static Integer Balance() {
		return server.log.GetValue();
	}

	public static void Fail() {
		server.commService.running = false;
	}

	public static void Unfail() {
		server.commService.running = true;
	}

	private static void Print() {
		for (int i = 0; i < server.log.Size(); i++) {
			System.out.println(i + "\t" + server.log.numbers.get(i).toString()
					+ "\t" + server.log.values.get(i));
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ArrayList<Endpoint> servers = new ArrayList<Endpoint>();
		Bank bank = new Bank(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]), servers, Integer.parseInt(args[2]));
		Scanner in = new Scanner(System.in);
		String line = "";
		do {
			System.out.println("Deposit: 1");
			System.out.println("Withdraw: 2");
			System.out.println("Balance: 3");
			System.out.println("Fail: 4");
			System.out.println("Unfail: 5");
			System.out.println("Print: 6");
			int opt = in.nextInt();
			switch (opt) {
			case 1:
				Deposit(in.nextInt());
				break;
			case 2:
				Withdraw(in.nextInt());
				break;
			case 3:
				Balance();
				break;
			case 4:
				Fail();
				break;
			case 5:
				Unfail();
				break;
			case 6:
				Print();
				break;
			}
		} while (in.hasNextInt());

	}
}
