package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseAuthServiceTest {
	@Test
	void addNoteTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(any())).thenReturn(preparedStatement);
		IAuthService authService = new DatabaseAuthService(connection);

		// when
		User result = authService.addUser("Mytthew", "123");

		// then
		assertEquals("Mytthew", result.getNickname());
		assertEquals("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", result.getPassword());
		verify(connection).prepareStatement("INSERT INTO users(login, password) VALUES (?, ?);");
	}
}
