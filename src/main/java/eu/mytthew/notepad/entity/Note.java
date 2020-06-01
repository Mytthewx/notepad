package eu.mytthew.notepad.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
public class Note {
	//	@Getter(NONE)
	private final String title;
	private final String content;
	private final UUID uuid = UUID.randomUUID();
	private final LocalDateTime noteTime = LocalDateTime.now();

	public Note(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
