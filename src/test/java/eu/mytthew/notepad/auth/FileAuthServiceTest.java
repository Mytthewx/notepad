package eu.mytthew.notepad.auth;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
	public void addUserTrueWithVerifyTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		when(fileOperation.createFile(any(), any())).thenReturn(true);

		// when
		boolean result = authService.addUser("UserTest", "123");

		// then
		assertTrue(result);
		verify(fileOperation, times(1)).createFile(any(), any());
	}

	@Test
	public void addUserTrueTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("nick", "usertest");
		jsonObject2.put("pass", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		when(fileOperation.createFile(eq("usertest"), eq(jsonObject2))).thenReturn(true);

		// when
		boolean result = authService.addUser("usertest", "123");

		// then
		verify(fileOperation, times(1)).createFile(eq("usertest"), eq(jsonObject2));
	}
}
