package eu.mytthew.notepad;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Reminder {
	private int id;
	private String name;
	private LocalDate date;

	public Reminder(int id, String name, LocalDate date) {
		this(name, date);
		this.id = id;
	}

	public Reminder(String name, LocalDate date) {
		this.name = name;
		this.date = date;
	}
}
