package eu.mytthew.notepad;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Reminder {
	private int id;
	private String name;
	private LocalDate date;
	private int noteId;

	public Reminder(String name, LocalDate date) {
		this(0, name, date, 0);
	}
}
