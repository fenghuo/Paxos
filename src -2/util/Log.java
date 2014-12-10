package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import paxos.BallotNumber;

public class Log {

	public ArrayList<Integer> values;
	public ArrayList<BallotNumber> numbers;
	private String path = null;
	private BufferedWriter writer = null;

	public Log() {
		values = new ArrayList<Integer>();
		numbers = new ArrayList<BallotNumber>();
	}

	public Log(String path) {
		this();
		this.path = path;
		try {
			Scanner in = new Scanner(new File(path));
			String line = "";
			while ((line = in.nextLine()) != null) {
				String msg = line.substring(line.indexOf(":") + 1);
				numbers.add(new BallotNumber(msg.split("_")[0]));
				values.add(Integer.parseInt(msg.split("_")[1]));
			}
		} catch (Exception e) {
		}
	}

	public synchronized void Write(BallotNumber bal, Integer val, int logIndex) {
		while (numbers.size() <= logIndex) {
			values.add(val);
			numbers.add(bal);
		}
		values.set(logIndex, val);
		numbers.set(logIndex, bal);

		try {
			writer = new BufferedWriter(new FileWriter(path, false));
			for (int i = 0; i < numbers.size(); i++) {
				writer.append(i + " : " + numbers.get(i).toMsg() + "_"
						+ values.get(i));
				writer.newLine();
				writer.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int Size() {
		return values.size();
	}

	public void SetPath(String path) {
		this.path = path;
	}

	public Integer GetValue() {
		return values.get(values.size() - 1);
	}
}