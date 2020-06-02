package eu.mytthew.notepad.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class User {
	private final List<Note> notes = new ArrayList<>();
	@Getter
	@Setter
	private String nickname;
	@Getter
	@Setter
	private String password;

	public User(String nickname, String password) {
		this.nickname = nickname;
		this.password = password;
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public List<Note> getNotes() {
		return Collections.unmodifiableList(notes);
	}

	public boolean removeNote(Note note) {
		if (notes.isEmpty()) {
			return false;
		}
		notes.remove(note);
		return true;
	}

	public boolean removeNote(UUID uuid) {
		Optional<Note> optionalNote = notes.stream()
				.filter(note -> note.getUuid().equals(uuid))
				.findAny();
		if (optionalNote.isPresent()) {
			notes.remove(optionalNote.get());
			return true;
		}
		return false;
	}
}
