package eu.mytthew.notepad.auth;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HashPasswordTest {
	@Test
	void hashPasswordTest() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertTrue(result);
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", authService.getLoggedUser().getPassword());
	}
}
