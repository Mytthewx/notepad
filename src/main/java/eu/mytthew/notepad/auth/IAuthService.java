package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;

public interface IAuthService {
	boolean containsNickname(String nickname);

	boolean login(String nickname, String password);

	boolean changePassword(String oldPassword, String newPassword);

	boolean addUser(String nickname, String password);

	boolean logout();

	User getLoggedUser();
}
