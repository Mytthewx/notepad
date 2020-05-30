package eu.mytthew.notepad;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import static lombok.AccessLevel.NONE;

@Getter
@EqualsAndHashCode
@ToString
public class Note {
	@Getter(NONE)
	private static int globalID = 0;
	private final String title;
	private final String content;
	private final int id = globalID++;
	private final LocalDateTime noteTime = LocalDateTime.now();

	public Note(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
