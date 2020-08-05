package eu.mytthew.notepad.auth;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
	public void addUserTrueTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nick", "usertest");
		jsonObject.put("pass", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		ArgumentCaptor<String> nicknameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<JSONObject> passwordCaptor = ArgumentCaptor.forClass(JSONObject.class);
		when(fileOperation.createFile(nicknameCaptor.capture(), passwordCaptor.capture())).thenReturn(true);

		// when
		boolean result = authService.addUser("usertest", "123");

		// then
		ArgumentCaptor<String> nicknameCaptorVerify = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<JSONObject> passwordCaptorVerify = ArgumentCaptor.forClass(JSONObject.class);
		verify(fileOperation, times(1)).createFile(nicknameCaptorVerify.capture(), passwordCaptorVerify.capture());
		assertTrue(result);
		assertEquals("usertest", nicknameCaptorVerify.getValue());
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", passwordCaptorVerify.getValue().getString("pass"));
	}

	@Test
	public void containsNicknameTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		authService.addUser("Mytthew", "123");

		// when
		boolean result = authService.containsNickname("Mytthew");

		// then
		assertTrue(result);
	}

	@Test
	public void changeNicknameTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nick", "Mytthew");
		jsonObject.put("pass", "123");

		// when


		// then
		assertTrue(authService.containsNickname("Mateusz"));
	}
}
