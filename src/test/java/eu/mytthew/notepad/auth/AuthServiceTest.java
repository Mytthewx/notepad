package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthServiceTest {
	@Test
	public void loginWithCorrectNicknameAndPassword() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertTrue(result);
	}

	@Test
	public void loginWithWrongNickname() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.login("Mateusz", "123");

		// then
		assertFalse(result);
	}

	@Test
	public void loginWithWrongPassword() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.login("Mytthew", "12345");

		// then
		assertFalse(result);
	}

	@Test
	public void isUserLoggedWithNullTest() {
		// given
		IAuthService authService = new RuntimeAuthService();

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

	@Test
	public void containsNicknameTest() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mateusz", "123");

		// when
		boolean result = authService.containsNickname("Mateusz");

		// then
		assertTrue(result);
	}

	@Test
	public void addUserWithUniqueNickname() {
		// given
		IAuthService authService = new RuntimeAuthService();

		// when
		boolean result = authService.addUser("Mytthew", "123");

		// then
		assertTrue(result);
	}

	@Test
	public void addUserWithNonUniqueNickname() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.addUser("Mytthew", "123");

		//then
		assertFalse(result);
	}

	@Test
	public void changePasswordWithWrongCurrentPassword() {
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
	public void changePasswordWithCorrectCurrentPassword() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = authService.changePassword("123", "12345");

		// then
		assertTrue(result);
	}
}
