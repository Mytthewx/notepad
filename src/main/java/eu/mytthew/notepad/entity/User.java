package eu.mytthew.notepad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
	private final int id;
	private String nickname;
	private String password;

	public User(String nickname, String password) {
		this(0, nickname, password);
	}
}
