package eu.mytthew.notepad.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileAuthServiceTest {
	@Test
	public void addUserFalseTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		when(fileOperation.createFile(any(), any())).thenReturn(false);

		// when
		boolean result = authService.addUser("UserTest", "123");

		// then
		assertFalse(result);
	}

	@Test
	public void addUserTrueTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		when(fileOperation.createFile(any(), any())).thenReturn(false);

		// when
		boolean result = authService.addUser("UserTest", "123");

		// then
		assertFalse(result);
	}
}
