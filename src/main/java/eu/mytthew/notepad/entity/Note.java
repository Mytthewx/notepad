package eu.mytthew.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Note {
	private final String title;
	private final String content;
	private final UUID uuid = UUID.randomUUID();
	private final LocalDateTime noteTime = LocalDateTime.now();
}
