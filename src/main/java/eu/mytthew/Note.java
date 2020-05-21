package eu.mytthew;


import com.google.common.base.Objects;

import java.time.LocalDateTime;

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

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getNoteTime() {
		return noteTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Note note = (Note) o;
		return id == note.id &&
				Objects.equal(title, note.title) &&
				Objects.equal(content, note.content) &&
				Objects.equal(noteTime, note.noteTime);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(title, content, id, noteTime);
	}

	@Override
	public String toString() {
		return "Note{" +
				"title='" + title + '\'' +
				", content='" + content + '\'' +
				", id=" + id +
				", noteTime=" + noteTime +
				'}';
	}
}
