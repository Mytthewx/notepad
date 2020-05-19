package eu.mytthew;

import java.util.ArrayList;
import java.util.List;

public class User {
	List<Note> notes = new ArrayList<>();
	private String nickname;
	private String password;

	public User(String nickname, String password) {
		this.nickname = nickname;
		this.password = password;
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public List<Note> getNotes() {
		return notes;
	}

	public boolean removeNote(int ID) {
		for (int i = 0; i < notes.size(); i++) {
			if (notes.get(i).getId() == ID) {
				notes.remove(notes.get(i));
				return true;
			}
		}
		return false;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
