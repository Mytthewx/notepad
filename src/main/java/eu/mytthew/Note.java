package eu.mytthew;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note {
	private static int globalID = 0;
	private final String title;
	private final String content;
	private final int id = globalID++;
	private final LocalDateTime noteTime = LocalDateTime.now();

	public Note(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public int getId() {
		return id;
	}
	

	@Override
	public String toString() {
		return "ID: " + id +
				"\nDate: " + noteTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
				"\nTitle: " + title +
				"\nContent: '" + content + '\'' + "\n";
	}
}
