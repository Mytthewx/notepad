package eu.mytthew;


import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.NONE;

@Data
@NoArgsConstructor(access = NONE)
@Getter
@EqualsAndHashCode
@ToString
public class Note {
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
