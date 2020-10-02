package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DatabaseAuthServiceTest {
	void login(Connection connection, IAuthService authService) throws SQLException {
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement userPasswordSQL = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(userPasswordSQL);
		ResultSet rs2 = mock(ResultSet.class);
		when(userPasswordSQL.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);
		when(rs2.getString("password")).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		PreparedStatement loginStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM users WHERE login = ?"))).thenReturn(loginStatement);
		ResultSet rs3 = mock(ResultSet.class);
		when(loginStatement.getResultSet()).thenReturn(rs3);
		when(rs3.next()).thenReturn(true);
		authService.login("Mytthew", "123");
	}

	@Test
	void addUserTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		PreparedStatement addUserStatement = mock(PreparedStatement.class);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);
		when(connection.prepareStatement(eq("INSERT INTO users(login, password) VALUES (?, ?);"))).thenReturn(addUserStatement);
		IAuthService authService = new DatabaseAuthService(connection);

		// when
		User result = authService.addUser("Mytthew", "123");

		// then
		assertEquals("Mytthew", result.getNickname());
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", result.getPassword());
		verify(connection).prepareStatement("INSERT INTO users(login, password) VALUES (?, ?);");
	}

	@Test
	void addUserTestWithException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);
		when(connection.prepareStatement(eq("INSERT INTO users(login, password) VALUES (?, ?);"))).thenThrow(new SQLException());
		IAuthService authService = new DatabaseAuthService(connection);

		// when
		User user = authService.addUser("Mytthew", "123");

		// then
		assertNull(user);
	}

	@Test
	void addUserTestWithNullReturn() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		PreparedStatement addUserStatement = mock(PreparedStatement.class);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(connection.prepareStatement(eq("INSERT INTO users(login, password) VALUES (?, ?);"))).thenReturn(addUserStatement);
		IAuthService authService = new DatabaseAuthService(connection);

		// when
		User result = authService.addUser("Mytthew", "123");

		// then
		assertNull(result);
	}

	@Test
	void containsNicknameWithException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenThrow(new SQLException());
		IAuthService authService = new DatabaseAuthService(connection);

		// when
		boolean result = authService.containsNickname("Mytthew");

		// then
		assertFalse(result);
	}

	@Test
	void loginTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement userPasswordSQL = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(userPasswordSQL);
		ResultSet rs2 = mock(ResultSet.class);
		when(userPasswordSQL.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);
		when(rs2.getString("password")).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		PreparedStatement loginStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM users WHERE login = ?"))).thenReturn(loginStatement);
		ResultSet rs3 = mock(ResultSet.class);
		when(loginStatement.getResultSet()).thenReturn(rs3);
		when(rs3.next()).thenReturn(true);

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertTrue(result);
	}

	@Test
	void loginFalseNoContainsUser() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertFalse(result);
	}

	@Test
	void loginFalseWrongPassword() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement userPasswordSQL = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(userPasswordSQL);
		ResultSet rs2 = mock(ResultSet.class);
		when(userPasswordSQL.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);
		when(rs2.getString("password")).thenReturn("wrongpassword");

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertFalse(result);
	}

	@Test
	void loginTrueWithoutCreatingNewUser() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement userPasswordSQL = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(userPasswordSQL);
		ResultSet rs2 = mock(ResultSet.class);
		when(userPasswordSQL.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);
		when(rs2.getString("password")).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		PreparedStatement loginStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM users WHERE login = ?"))).thenReturn(loginStatement);
		ResultSet rs3 = mock(ResultSet.class);
		when(loginStatement.getResultSet()).thenReturn(rs3);
		when(rs3.next()).thenReturn(false);

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertTrue(result);
	}

	@Test
	void loginFalseUserWithoutPassword() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement userPasswordSQL = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(userPasswordSQL);
		ResultSet rs2 = mock(ResultSet.class);
		when(userPasswordSQL.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(false);

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertFalse(result);
	}

	@Test
	void loginFalseWithException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserStatement);
		ResultSet rs = mock(ResultSet.class);
		when(containsUserStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenThrow(new SQLException());

		// when
		boolean result = authService.login("Mytthew", "123");

		// then
		assertFalse(result);
	}

	@Test
	void logoutTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);

		// when
		boolean result = authService.logout();

		// then
		assertTrue(result);
	}

	@Test
	void changeNicknameFalseContainsTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);
		PreparedStatement containsUserNewNickname = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserNewNickname);
		ResultSet rs4 = mock(ResultSet.class);
		when(containsUserNewNickname.getResultSet()).thenReturn(rs4);
		when(rs4.next()).thenReturn(true);

		// when
		boolean result = authService.changeNickname("NewNickname");

		// then
		assertFalse(result);
	}

	@Test
	void changeNicknameTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);
		PreparedStatement containsUserNewNickname = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserNewNickname);
		ResultSet rs4 = mock(ResultSet.class);
		when(containsUserNewNickname.getResultSet()).thenReturn(rs4);
		when(rs4.next()).thenReturn(false);
		PreparedStatement changeNicknameStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE users SET login = ? WHERE login = ?;"))).thenReturn(changeNicknameStatement);

		// when
		boolean result = authService.changeNickname("NewNickname");

		// then
		assertEquals("NewNickname", authService.getLoggedUser().getNickname());
		assertTrue(result);
	}

	@Test
	void changeNicknameFalseException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);
		PreparedStatement containsUserNewNickname = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenReturn(containsUserNewNickname);
		ResultSet rs4 = mock(ResultSet.class);
		when(containsUserNewNickname.getResultSet()).thenReturn(rs4);
		when(rs4.next()).thenReturn(false);
		when(connection.prepareStatement("UPDATE users SET login = ? WHERE login = ?;")).thenThrow(new SQLException());

		// when
		boolean result = authService.changeNickname("NewNickname");

		// then
		assertFalse(result);
	}

	@Test
	void changePasswordTrue() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);
		PreparedStatement checkPassword = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(checkPassword);
		ResultSet rs = mock(ResultSet.class);
		when(checkPassword.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getString(eq("password"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		PreparedStatement changePassword = mock(PreparedStatement.class);
		when(connection.prepareStatement("UPDATE users SET password = ? WHERE login = ?")).thenReturn(changePassword);
		ResultSet rs2 = mock(ResultSet.class);
		when(changePassword.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);

		// when
		boolean result = authService.changePassword("123", "12345");

		// then
		assertNotEquals("123", authService.getLoggedUser().getPassword());
		assertNotEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", authService.getLoggedUser().getPassword());
		assertEquals("5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", authService.getLoggedUser().getPassword());
		assertTrue(result);
	}

	@Test
	void changePasswordFalseWrongCurrentPassword() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);
		PreparedStatement checkPassword = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(checkPassword);
		ResultSet rs = mock(ResultSet.class);
		when(checkPassword.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getString(eq("password"))).thenReturn("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		PreparedStatement changePassword = mock(PreparedStatement.class);
		when(connection.prepareStatement("UPDATE users SET password = ? WHERE login = ?")).thenReturn(changePassword);
		ResultSet rs2 = mock(ResultSet.class);
		when(changePassword.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(false);

		// when
		boolean result = authService.changePassword("wrongpassword", "12345");

		// then
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", authService.getLoggedUser().getPassword());
		assertFalse(result);
	}

	@Test
	void changePasswordFalseNoUser() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);
		PreparedStatement checkPassword = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(checkPassword);
		ResultSet rs = mock(ResultSet.class);
		when(checkPassword.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = authService.changePassword("wrongpassword", "12345");

		// then
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", authService.getLoggedUser().getPassword());
		assertFalse(result);
	}

	@Test
	void changePasswordFalse() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);
		PreparedStatement checkPassword = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenReturn(checkPassword);
		ResultSet rs = mock(ResultSet.class);
		when(checkPassword.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = authService.changePassword("wrongpassword", "12345");

		// then
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", authService.getLoggedUser().getPassword());
		assertFalse(result);
	}

	@Test
	void changePasswordFalseException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		IAuthService authService = new DatabaseAuthService(connection);
		login(connection, authService);
		PreparedStatement checkPassword = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT password FROM users WHERE login = ?"))).thenThrow(new SQLException());
		ResultSet rs = mock(ResultSet.class);
		when(checkPassword.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = authService.changePassword("wrongpassword", "12345");

		// then
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", authService.getLoggedUser().getPassword());
		assertFalse(result);
	}
}
