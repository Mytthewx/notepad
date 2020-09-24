package eu.mytthew.notepad.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class User {
	private final List<Note> notes = new ArrayList<>();
	@Getter
	private int id;
	@Getter
	@Setter
	private String nickname;
	@Getter
	@Setter
	private String password;

	public User(int id, String nickname, String password) {
		this(nickname, password);
		this.id = id;
	}

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
}
