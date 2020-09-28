package eu.mytthew.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Note {
	private int id;
	private String title;
	private String content;
	private LocalDate noteDate;
	private int userId;

	public Note(String title, String content, LocalDate noteDate) {
		this(0, title, content, noteDate, 0);
		this.title = title;
		this.content = content;
		this.noteDate = noteDate;
	}
}
