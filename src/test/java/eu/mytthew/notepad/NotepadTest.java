package eu.mytthew.notepad;

import eu.mytthew.notepad.auth.AuthService;
import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NotepadTest {
	@Test
	public void isUserLoggedWithNullTest() {
		// given
		AuthService authService = new AuthService();

		// when
		User loggedUser = authService.getLoggedUser();

		// then
		assertNull(loggedUser);
	}

	@Test
	public void getUserNickname() {
		// given
		User user = new User("Mytthew", "123");

		// when
		String nickname = user.getNickname();

		// then
		assertEquals("Mytthew", nickname);
	}
}
