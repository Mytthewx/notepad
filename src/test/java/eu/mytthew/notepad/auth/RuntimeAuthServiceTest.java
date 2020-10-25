package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuntimeAuthServiceTest {
	@Test
	void loginWithCorrectNicknameAndPassword() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertTrue(result);
	}

	@Test
	void loginWithWrongNickname() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.login("Mateusz", "123");

		// then
		assertFalse(result);
	}

	@Test
	void loginWithWrongPassword() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.login("Mytthew", "12345");

		// then
		assertFalse(result);
	}

	@Test
	void isUserLoggedWithNullTest() {
		// given
		IAuthService authService = new RuntimeAuthService();

		// when
		User loggedUser = authService.getLoggedUser();

		// then
		assertNull(loggedUser);
	}

	@Test
	void getUserNickname() {
		// given
		User user = new User("Mytthew", "123");

		// when
		String nickname = user.getNickname();

		// then
		assertEquals("Mytthew", nickname);
	}

	@Test
	void containsNicknameTest() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mateusz", "123");

		// when
		boolean result = authService.containsNickname("Mateusz");

		// then
		assertTrue(result);
	}

	@Test
	void addUserWithUniqueNickname() {
		// given
		IAuthService authService = new RuntimeAuthService();

		// when
		User user = authService.addUser("Mytthew", "123");

		// then
		assertNotNull(user);
	}

	@Test
	void addUserWithNonUniqueNickname() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		User user = authService.addUser("Mytthew", "123");

		//then
		assertNull(user);
	}

	@Test
	void changePasswordWithWrongCurrentPassword() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = authService.changePassword("1234", "12345");

		// then
		assertFalse(result);
	}

	@Test
	void changePasswordWithCorrectCurrentPassword() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = authService.changePassword("123", "12345");

		// then
		assertTrue(result);
	}

	@Test
	void changeNicknameThatNotExist() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = authService.changeNickname("Mateusz");

		// result
		assertTrue(result);
		assertEquals("Mateusz", authService.getLoggedUser().getNickname());
	}

	@Test
	void changeNicknameThatExist() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.addUser("Mateusz", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = authService.changeNickname("Mateusz");

		// result
		assertFalse(result);
		assertEquals("Mytthew", authService.getLoggedUser().getNickname());
	}

	@Test
	void logoutUser() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = authService.logout();

		// result
		assertTrue(result);
		assertNull(authService.getLoggedUser());
	}
}
