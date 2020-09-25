package eu.mytthew.notepad.auth;

public class IdProvider {
	public static final IdProvider instance = new IdProvider();

	private int id = 0;

	private IdProvider() {
	}

	public static IdProvider getInstance() {
		return instance;
	}

	public int getNextSequence() {
		return id++;
	}
}
