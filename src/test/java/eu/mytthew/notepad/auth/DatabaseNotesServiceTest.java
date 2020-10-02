package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseNotesServiceTest {
//	@Test
//	void getAllNotesTest() throws SQLException {
//		// given
//		Connection connection = mock(Connection.class);
//		INotesService notesService = new DatabaseNotesService(connection);
//		PreparedStatement notesStatement = mock(PreparedStatement.class);
//		when(connection.prepareStatement("SELECT * FROM notes WHERE user_id = ?")).thenReturn(notesStatement);
//		ResultSet rs = mock(ResultSet.class);
//		when(notesStatement.getResultSet()).thenReturn(rs);
//		when(rs.next()).thenReturn(true);
//		User user = new User(0, "Mytthew", "123");
//		List<Note> noteList = new ArrayList<>();
//		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-02"));
//		noteList.add(note);
//
//		// when
//		 = notesService.getAllNotes(User)
//	}


	@Test
	void addNoteTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO notes(title, content, date, user_id) VALUES (?, ?, ?, ?)"))).thenReturn(preparedStatement);
		User user = new User("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-02"));

		// when
		Note result = notesService.addNote(user, note);

		// then
		assertEquals(note, result);
	}

	@Test
	void addNoteFalseTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO notes(title, content, date, user_id) VALUES (?, ?, ?, ?)"))).thenThrow(new SQLException());
		User user = new User("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-02"));

		// when
		Note result = notesService.addNote(user, note);

		// then
		assertNotEquals(note, result);
	}
}
