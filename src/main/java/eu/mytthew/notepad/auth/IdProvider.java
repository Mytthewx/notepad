package eu.mytthew.notepad.auth;

public class IdProvider {
	public static final IdProvider noteInstance = new IdProvider();
	public static final IdProvider userInstance = new IdProvider();
	public static final IdProvider reminderInstance = new IdProvider();

	private int noteId = 0;
	private int userId = 0;
	private int reminderId = 0;

	private IdProvider() {
	}

	public static IdProvider getNoteInstance() {
		return noteInstance;
	}

	public static IdProvider getUserInstance() {
		return userInstance;
	}

	public static IdProvider getReminderInstance() {
		return reminderInstance;
	}

	public int getNextUserSequence() {
		return userId++;
	}

	public int getNextNoteSequence() {
		return noteId++;
	}

	public int getNextReminderSequence() {
		return reminderId++;
	}
}
