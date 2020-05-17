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

	public String getNotes() {
		return "Notes for this user:\n" + notes.toString();
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
