package eu.mytthew.notepad;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Reminder {
	private int id;
	private String name;
	private LocalDate date;
	private int noteId;

	public Reminder(int id, String name, LocalDate date, int noteId) {
		this(name, date, noteId);
		this.id = id;
	}

	public Reminder(String name, LocalDate date) {
		this.name = name;
		this.date = date;
	}

	public Reminder(String name, LocalDate date, int noteId) {
		this.name = name;
		this.date = date;
		this.noteId = noteId;
	}
}
