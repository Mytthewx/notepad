package eu.mytthew.notepad.auth;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileAuthServiceTest {
	public JSONObject openTestFile(String filename) {
		try {
			return new JSONObject(new JSONTokener(new FileInputStream(Paths.get("src", "test", "resources", "users", filename + ".json").toFile())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	void addUserTrueTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		ArgumentCaptor<String> nicknameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<JSONObject> passwordCaptor = ArgumentCaptor.forClass(JSONObject.class);
		when(fileOperation.createFile(nicknameCaptor.capture(), passwordCaptor.capture())).thenReturn(true);

		// when
		authService.addUser("UserTest", "123");

		// then
		ArgumentCaptor<String> nicknameCaptorVerify = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<JSONObject> passwordCaptorVerify = ArgumentCaptor.forClass(JSONObject.class);
		verify(fileOperation, times(1)).createFile(nicknameCaptorVerify.capture(), passwordCaptorVerify.capture());
		assertEquals("UserTest", nicknameCaptorVerify.getValue());
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", passwordCaptorVerify.getValue().getString("pass"));
	}

	@Test
	void containsNicknameTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		when(fileOperation.fileExist(eq("UserTest"))).thenReturn(true);

		// when
		boolean result = authService.containsNickname("UserTest");

		// then
		assertTrue(result);
	}

	@Test
	void loginUserWithTrueTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(eq("UserTest"))).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("nick"))).thenReturn("UserTest");
		when(jsonObject.getString(eq("pass"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");

		// when
		boolean result = authService.login("UserTest", "123");

		// then
		assertTrue(result);
	}

	@Test
	void loginUserWithFalseTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(eq("UserTest"))).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("nick"))).thenReturn("UserTest");
		when(jsonObject.getString(eq("pass"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");

		// when
		boolean result = authService.login("UserTest", "badpassword");

		// then
		assertFalse(result);
	}

	@Test
	void changeNicknameToNewUniqueNickname() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(eq("UserTest"))).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("nick"))).thenReturn("UserTest");
		when(jsonObject.getString(eq("pass"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		authService.login("UserTest", "123");

		// when
		boolean result = authService.changeNickname("NewNickname");

		// then
		assertTrue(result);
	}

	@Test
	void changeNicknameToNewNotUniqueNickname() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(eq("UserTest"))).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("nick"))).thenReturn("UserTest");
		when(jsonObject.getString(eq("pass"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		authService.login("UserTest", "123");
		when(authService.containsNickname(eq("SecondUser"))).thenReturn(true);

		// when
		boolean result = authService.changeNickname("SecondUser");

		// then
		assertFalse(result);
	}

	@Test
	void changePasswordCorrectly() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(eq("UserTest"))).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("nick"))).thenReturn("UserTest");
		when(jsonObject.getString(eq("pass"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		authService.login("UserTest", "123");

		// when
		boolean result = authService.changePassword("123", "12345");

		// then
		assertTrue(result);
		assertEquals("5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", authService.getLoggedUser().getPassword());
	}

	@Test
	void changePasswordWrong() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(eq("UserTest"))).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("nick"))).thenReturn("UserTest");
		when(jsonObject.getString(eq("pass"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		authService.login("UserTest", "123");

		// when
		boolean result = authService.changePassword("12345", "12345");

		// then
		assertFalse(result);
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", authService.getLoggedUser().getPassword());
	}

	@Test
	void correctlyLogoutTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(eq("UserTest"))).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("nick"))).thenReturn("UserTest");
		when(jsonObject.getString(eq("pass"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		authService.login("UserTest", "123");

		// when
		boolean result = authService.logout();

		// then
		assertTrue(result);
		assertNull(authService.getLoggedUser());
	}

	@Test
	void nullUserLogoutTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		IAuthService authService = new FileAuthService(fileOperation, config);

		// when
		boolean result = authService.logout();

		// then
		assertFalse(result);
		assertNull(authService.getLoggedUser());
	}
}
