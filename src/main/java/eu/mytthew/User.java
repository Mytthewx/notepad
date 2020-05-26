package eu.mytthew;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static eu.mytthew.HashPassword.hashPassword;
import static lombok.AccessLevel.NONE;

@Data
@NoArgsConstructor(access = NONE)
@Getter
@Setter
public class User {
	private final List<Note> notes = new ArrayList<>();
	private String nickname;
	private String password;

	public User(String nickname, String password) {
		this.nickname = nickname;
		this.password = hashPassword(password);
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
		this.password = hashPassword(password);
	}
}
