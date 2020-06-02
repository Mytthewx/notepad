package eu.mytthew.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Note {
	@Setter
	private String title;
	@Setter
	private String content;
	private final UUID uuid = UUID.randomUUID();
	private final LocalDateTime noteTime = LocalDateTime.now();
}
