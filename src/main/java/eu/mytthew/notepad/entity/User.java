package eu.mytthew.notepad.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class User {
	private final List<Note> notes = new ArrayList<>();
	@Getter
	@Setter
	private String nickname;
	@Getter
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
		if (notes.isEmpty()) {
			return false;
		}
		for (int i = 0; i < notes.size(); i++) {
			if (notes.get(i).getUuid().equals(uuid)) {
				notes.remove(notes.get(i));
				return true;
			}
		}
		return false;
	}


//	public boolean removeNote(int id) {
//		Optional<Note> optionalNote = notes.stream()
//				.filter(note -> note.getId())
//				.findAny();
//		if (optionalNote.isPresent()) {
//			notes.remove(optionalNote.get());
//			return true;
//		}
//		return false;
//	}

	public void setPassword(String password) {
		this.password = password;
	}
}
