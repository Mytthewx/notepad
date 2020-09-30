package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import org.junit.function.ThrowingRunnable;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DatabaseAuthServiceTest {
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
		PreparedStatement addUserStatement = mock(PreparedStatement.class);
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
		PreparedStatement containsUserStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT login FROM users WHERE login = ?"))).thenThrow(new SQLException());
		IAuthService authService = new DatabaseAuthService(connection);

		// when
		boolean result = authService.containsNickname("Mytthew");
		ThrowingRunnable throwingRunnable = () -> connection.prepareStatement("SELECT login FROM users WHERE login = ?");

		// then
		assertFalse(result);
		assertThrows(SQLException.class, throwingRunnable);
	}
}
