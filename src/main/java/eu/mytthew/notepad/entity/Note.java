package eu.mytthew.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Note {
	private String title;
	private String content;
	private LocalDate noteDate;
	private final UUID uuid = UUID.randomUUID();

	public Note(String title, String content, String date) {
		this.title = title;
		this.content = content;
		noteDate = LocalDate.parse(date);
	}
}
