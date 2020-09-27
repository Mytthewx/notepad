package eu.mytthew.notepad.entity;

import eu.mytthew.notepad.Reminder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Note {
	private int id;
	private final List<Reminder> reminders = new ArrayList<>();
	private String title;
	private String content;
	private LocalDate noteDate;
	private int userId;

	public Note(int id, String title, String content, LocalDate noteDate, int userId) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.noteDate = noteDate;
		this.userId = userId;
	}

	public Note(String title, String content, LocalDate noteDate) {
		this.title = title;
		this.content = content;
		this.noteDate = noteDate;
	}

	public Note(String title, String content, LocalDate noteDate, int userId) {
		this.title = title;
		this.content = content;
		this.noteDate = noteDate;
		this.userId = userId;
	}
}
