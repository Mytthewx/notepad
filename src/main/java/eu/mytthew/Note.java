package eu.mytthew;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note {
	private static int globalID = 0;
	private final String title;
	private final String content;
	private final int ID = globalID++;
	private LocalDateTime localDateTime = LocalDateTime.now();

	public Note(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public int getID() {
		return ID;
	}

	@Override
	public String toString() {
		return "ID: " + ID +
				"\nDate: " + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
				"\nTitle: " + title +
				"\nContent: '" + content + '\'' + "\n";
	}
}
