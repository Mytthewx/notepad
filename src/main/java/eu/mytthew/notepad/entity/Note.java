package eu.mytthew.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Note {
	private String title;
	private String content;
	private final UUID uuid = UUID.randomUUID();
	private final LocalDateTime noteTime = LocalDateTime.now();
}
