package eu.mytthew.notepad;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class User {
	private final List<Note> notes = new ArrayList<>();
	@Getter
	@Setter
	private String nickname;
	@Getter
	private String password;

	public User(String nickname, String password) {
		this.nickname = nickname;
		this.password = HashPassword.hashPassword(password);
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public List<Note> getNotes() {
		return Collections.unmodifiableList(notes);
	}

	public boolean removeNote(int id) {
		Optional<Note> optionalNote = notes.stream()
				.filter(note -> note.getId() == id)
				.findAny();
		if (optionalNote.isPresent()) {
			notes.remove(optionalNote.get());
			return true;
		}
		return false;
	}

	public void setPassword(String password) {
		this.password = HashPassword.hashPassword(password);
	}
}
