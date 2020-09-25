package eu.mytthew.notepad.entity;

import lombok.Getter;
import lombok.Setter;


public class User {
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
}
