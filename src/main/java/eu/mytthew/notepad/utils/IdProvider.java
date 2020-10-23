package eu.mytthew.notepad.utils;

public class IdProvider {
	private int counter;

	public IdProvider(int counter) {
		this.counter = counter;
	}

	public IdProvider() {
		this(0);
	}

	public int next() {
		return counter++;
	}
}
