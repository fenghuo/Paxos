package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.print.attribute.standard.DateTimeAtCompleted;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int port1 = 11111;
		int n = 5;
		int[] port = new int[n];
		Endpoint[] e = new Endpoint[n];
		ArrayList<Endpoint> servers = new ArrayList<Endpoint>();
		for (int i = 0; i < n; i++) {
			port[i] = port1 + i;
			e[i] = new Endpoint("127.0.0.1", port[i]);
			servers.add(e[i]);
		}
		final Server[] server = new Server[n];
		for (int i = 0; i < n; i++) {
			server[i] = new Server(i + 1, 3, servers, port[i]);
		}

		server[0].SetValue(new Date().getSeconds());

		if (false)
			return;
		final Scanner in = new Scanner(System.in);
		String line = "";
		do {
			System.out.println("Deposit: 1");
			System.out.println("Withdraw: 2");
			System.out.println("Balance: 3");
			System.out.println("Fail: 4");
			System.out.println("Unfail: 5");
			System.out.println("Print: 6");
			System.out.println("Concurrent: 7");
			int opt = in.nextInt();
			switch (opt) {
			case 1:
				System.out.println(server[in.nextInt()].SetValue(in.nextInt()));
				break;
			case 4:
				server[in.nextInt()].commService.running = false;
				break;
			case 5:
				server[in.nextInt()].commService.running = true;
				break;
			case 6:
				int k = in.nextInt();
				for (int i = 0; i < server[k].log.Size(); i++) {
					System.out.println(i + "\t"
							+ server[k].log.numbers.get(i).toMsg() + "\t"
							+ server[k].log.values.get(i));
				}
				break;
			case 7:
				new Thread() {
					public void run() {
						System.out.println(server[1].SetValue(109));
					}
				}.start();
				new Thread() {
					public void run() {
						System.out.println(server[0].SetValue(201));
					}
				}.start();
				break;
			}
		} while (in.hasNextInt());

	}

	private static class T1 extends Thread {

		@Override
		public void run() {
			// new CommService().SendTo(new Endpoint("169.231.53.15", 6666),
			// "test\n");
		}

	}

	private static class T2 extends Thread {

		@Override
		public void run() {
			// new CommService().Receive(6666);
		}

	}
}
