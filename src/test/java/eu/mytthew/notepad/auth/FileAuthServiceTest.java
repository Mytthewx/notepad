package eu.mytthew.notepad.auth;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileAuthServiceTest {
	public JSONObject openTestFile(String filename) {
		try {
			return new JSONObject(new JSONTokener(new FileInputStream(Paths.get("src", "test", "resources", filename.toLowerCase() + ".json").toFile())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

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
		when(fileOperation.fileExist(eq("Mytthew"))).thenReturn(true);

		// when
		boolean result = authService.containsNickname("Mytthew");

		// then
		assertTrue(result);
	}

	@Test
	public void loginUserWithTrueTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject outerObject = openTestFile("UserTest");
		when(fileOperation.openFile(any())).thenReturn(outerObject);

		// when
		boolean result = authService.login("UserTest", "123");

		// then
		assertTrue(result);
	}

	@Test
	public void loginUserWithoutNotesTrueTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject outerObject = openTestFile("usertest-without-notes");
		when(fileOperation.openFile(any())).thenReturn(outerObject);

		// when
		boolean result = authService.login("usertest-without-notes", "123");

		// then
		assertTrue(result);
	}

	@Test
	public void loginUserWithFalseTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject jsonObject = openTestFile("UserTest");
		when(fileOperation.openFile(any())).thenReturn(jsonObject);

		// when
		boolean result = authService.login("UserTest", "12345");

		// then
		assertFalse(result);
	}

	@Test
	public void changeNicknameToNewUniqueNickname() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject jsonObject = openTestFile("UserTest");
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		authService.login("UserTest", "123");

		// when
		boolean result = authService.changeNickname("Mateusz");

		// then
		assertTrue(result);
	}

	@Test
	public void changeNicknameToNewNotUniqueNickname() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject jsonObject = openTestFile("UserTest");
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		authService.login("UserTest", "123");
		when(fileOperation.fileExist("UserTest")).thenReturn(true);

		// when
		boolean result = authService.changeNickname("UserTest");

		// then
		assertFalse(result);
	}

	@Test
	public void changePasswordCorrectly() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject jsonObject = openTestFile("UserTest");
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		authService.login("UserTest", "123");

		// when
		boolean result = authService.changePassword("123", "12345");

		// then
		assertTrue(result);
	}

	@Test
	public void changePasswordWrong() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject jsonObject = openTestFile("UserTest");
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		authService.login("UserTest", "123");

		// when
		boolean result = authService.changePassword("1234", "12345");

		// then
		assertFalse(result);
	}

	@Test
	public void correctlyLogoutTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);
		JSONObject outerObject = openTestFile("UserTest");
		when(fileOperation.openFile(any())).thenReturn(outerObject);
		authService.login("UserTest", "123");
		when(fileOperation.createFile(any(), any())).thenReturn(true);

		// when
		boolean result = authService.logout();

		// then
		assertTrue(result);
	}

	@Test
	public void nullUserLogoutTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation);

		// when
		boolean result = authService.logout();

		// then
		assertFalse(result);
	}
}
