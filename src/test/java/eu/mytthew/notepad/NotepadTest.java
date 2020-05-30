package eu.mytthew.notepad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NotepadTest {
	@Test
	public void isUserLoggedWithNullTest() {
		// given
		LoginSystem loginSystem = new LoginSystem();

		// when
		User loggedUser = loginSystem.getLoggedUser();

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
