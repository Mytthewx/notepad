package eu.mytthew;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class User {
	private final List<Note> notes = new ArrayList<>();
	private String nickname;
	private String password;

	public User(String nickname, String password) {
		this.nickname = nickname;
		this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
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
		this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
