package eu.mytthew.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Note {
	private final UUID uuid = UUID.randomUUID();
	private String title;
	private String content;
	private LocalDate noteDate;
}
