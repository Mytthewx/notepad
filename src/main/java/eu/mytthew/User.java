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

	public void getNotes() {
		if (notes.isEmpty()) {
			System.out.println("There is no notes.");
		} else {
			notes.stream().map(Note::toString).forEach(System.out::println);
		}
	}

	public void removeNotes(int ID) {
		if (notes.isEmpty()) {
			System.out.println("There is no notes.");
		} else {
			for (int i = 0; i < notes.size(); i++) {
				if (notes.get(i).getID() == ID) {
					notes.remove(notes.get(i));
				} else {
					System.out.println("No note with this ID.");
				}
			}
		}
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
