package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.Reminder;
import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DatabaseNotesServiceTest {
	Note addNote(User user, Connection connection, INotesService notesService) throws SQLException {
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO notes(title, content, date, user_id) VALUES (?, ?, ?, ?)"))).thenReturn(preparedStatement);
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-02"));
		return notesService.addNote(user, note);
	}

	@Test
	void getAllNotesWithNotesTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement notesStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM notes WHERE user_id = ?"))).thenReturn(notesStatement);
		ResultSet rs = mock(ResultSet.class);
		when(notesStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true, false);
		when(rs.getString(eq("title"))).thenReturn("Title");
		when(rs.getString(eq("content"))).thenReturn("Content");
		when(rs.getString(eq("date"))).thenReturn("2020-10-02");

		// when
		List<Note> noteList = notesService.getAllNotes(user);

		// then
		assertEquals(1, noteList.size());
	}

	@Test
	void getAllNotesWithoutNotesTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement notesStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM notes WHERE user_id = ?"))).thenReturn(notesStatement);
		ResultSet rs = mock(ResultSet.class);
		when(notesStatement.getResultSet()).thenReturn(rs);

		// when
		List<Note> noteList = notesService.getAllNotes(user);

		// then
		assertEquals(0, noteList.size());
	}

	@Test
	void getAllNotesException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("SELECT * FROM notes WHERE user_id = ?"))).thenThrow(sqlException);

		// when
		List<Note> noteList = notesService.getAllNotes(user);

		// then
		assertEquals(0, noteList.size());
	}

	@Test
	void getAllRemindersTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement addReminderStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)"))).thenReturn(addReminderStatement);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-02"));
		notesService.addReminder(0, reminder);
		PreparedStatement remindersStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM reminders WHERE note_id = ?"))).thenReturn(remindersStatement);
		ResultSet rs = mock(ResultSet.class);
		when(remindersStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true, false);
		when(rs.getString(eq("name"))).thenReturn("name");
		when(rs.getString(eq("date"))).thenReturn("2020-10-02");

		// when
		List<Reminder> reminders = notesService.getAllReminders(0);

		// then
		assertEquals(1, reminders.size());
	}

	@Test
	void getAllRemindersWithoutRemindersTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement addNoteStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)"))).thenReturn(addNoteStatement);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-02"));
		notesService.addReminder(0, reminder);
		PreparedStatement remindersStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM reminders WHERE note_id = ?"))).thenReturn(remindersStatement);
		ResultSet rs = mock(ResultSet.class);
		when(remindersStatement.getResultSet()).thenReturn(rs);

		// when
		List<Reminder> reminders = notesService.getAllReminders(0);

		// then
		assertEquals(0, reminders.size());
	}

	@Test
	void getAllRemindersException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement addNoteStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)"))).thenReturn(addNoteStatement);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-02"));
		notesService.addReminder(0, reminder);
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("SELECT * FROM reminders WHERE note_id = ?"))).thenThrow(sqlException);

		// when
		notesService.getAllReminders(0);

		// then
		verify(sqlException, times(1)).printStackTrace();
	}

	@Test
	void addNoteTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO notes(title, content, date, user_id) VALUES (?, ?, ?, ?)"))).thenReturn(preparedStatement);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
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
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-02"));
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("INSERT INTO notes(title, content, date, user_id) VALUES (?, ?, ?, ?)"))).thenThrow(sqlException);

		// when
		Note result = notesService.addNote(user, note);

		// then
		assertNotEquals(note, result);
		assertNull(result);
	}

	@Test
	void editNoteTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = addNote(user, connection, notesService);
		PreparedStatement newTitleStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE notes SET title = ? WHERE id = ?"))).thenReturn(newTitleStatement);
		ResultSet rs = mock(ResultSet.class);
		when(newTitleStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement newContentStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE notes SET content = ? WHERE id = ?"))).thenReturn(newContentStatement);
		ResultSet rs2 = mock(ResultSet.class);
		when(newContentStatement.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);
		PreparedStatement newDateStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE notes SET date = ? WHERE id = ?"))).thenReturn(newDateStatement);
		ResultSet rs3 = mock(ResultSet.class);
		when(newDateStatement.getResultSet()).thenReturn(rs3);
		when(rs3.next()).thenReturn(true);
		SQLException sqlException = mock(SQLException.class);

		// when
		notesService.editNote(0, "New Title", "New Content", "2020-10-03");

		// then
		verify(sqlException, times(0)).printStackTrace();
	}

	@Test
	void editNoteTestWithoutChanges() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = addNote(user, connection, notesService);
		PreparedStatement newTitleStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE notes SET title = ? WHERE id = ?"))).thenReturn(newTitleStatement);
		ResultSet rs = mock(ResultSet.class);
		when(newTitleStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement newContentStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE notes SET content = ? WHERE id = ?"))).thenReturn(newContentStatement);
		ResultSet rs2 = mock(ResultSet.class);
		when(newContentStatement.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);
		PreparedStatement newDateStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE notes SET date = ? WHERE id = ?"))).thenReturn(newDateStatement);
		ResultSet rs3 = mock(ResultSet.class);
		when(newDateStatement.getResultSet()).thenReturn(rs3);
		when(rs3.next()).thenReturn(true);

		// when
		notesService.editNote(note.getId(), "", "", "");

		// then
		assertEquals("Title", note.getTitle());
		assertEquals("Content", note.getContent());
		assertEquals("2020-10-02", note.getNoteDate().toString());
	}

	@Test
	void editNoteException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("UPDATE notes SET title = ? WHERE id = ?"))).thenThrow(sqlException);

		// when
		notesService.editNote(0, "New Title", "New Content", "2020-10-03");

		// then
		verify(sqlException, times(1)).printStackTrace();
	}

	@Test
	void editReminderTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement addReminder = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)"))).thenReturn(addReminder);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-02"));
		notesService.addReminder(0, reminder);
		PreparedStatement newNameStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE reminders SET name = ? WHERE id = ?"))).thenReturn(newNameStatement);
		ResultSet rs2 = mock(ResultSet.class);
		when(newNameStatement.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);
		PreparedStatement newDateStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE reminders SET date = ? WHERE id = ?"))).thenReturn(newDateStatement);
		ResultSet rs3 = mock(ResultSet.class);
		when(newDateStatement.getResultSet()).thenReturn(rs3);
		when(rs3.next()).thenReturn(true);
		SQLException sqlException = mock(SQLException.class);

		// when
		notesService.editReminder(0, "New Name", "2020-10-02");

		// then
		verify(sqlException, times(0)).printStackTrace();
	}

	@Test
	void editReminderWithoutChangesTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement addReminder = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)"))).thenReturn(addReminder);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-02"));
		notesService.addReminder(0, reminder);
		PreparedStatement newNameStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE reminders SET name = ? WHERE id = ?"))).thenReturn(newNameStatement);
		ResultSet rs2 = mock(ResultSet.class);
		when(newNameStatement.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);
		PreparedStatement newDateStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("UPDATE reminders SET date = ? WHERE id = ?"))).thenReturn(newDateStatement);
		ResultSet rs3 = mock(ResultSet.class);
		when(newDateStatement.getResultSet()).thenReturn(rs3);
		when(rs3.next()).thenReturn(true);
		SQLException sqlException = mock(SQLException.class);

		// when
		notesService.editReminder(0, "", "");

		// then
		verify(sqlException, times(0)).printStackTrace();
	}

	@Test
	void editReminderException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement addReminder = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)"))).thenReturn(addReminder);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-02"));
		notesService.addReminder(0, reminder);
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("UPDATE reminders SET name = ? WHERE id = ?"))).thenThrow(sqlException);

		// when
		notesService.editReminder(0, "NewTitle", "2020-10-03");

		// then
		verify(sqlException, times(1)).printStackTrace();
	}

	@Test
	void addReminder() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = addNote(user, connection, notesService);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-02"));
		PreparedStatement addReminderStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)"))).thenReturn(addReminderStatement);
		ResultSet rs = mock(ResultSet.class);
		when(addReminderStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);

		// when
		Reminder result = notesService.addReminder(0, reminder);

		// then
		assertEquals(reminder, result);
	}

	@Test
	void addReminderException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = addNote(user, connection, notesService);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-02"));
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)"))).thenThrow(sqlException);

		// when
		Reminder result = notesService.addReminder(0, reminder);

		// then
		assertNotEquals(reminder, result);
	}

	@Test
	void removeReminderTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement verifyNoteId = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT note_id FROM reminders WHERE id = ?"))).thenReturn(verifyNoteId);
		ResultSet rs = mock(ResultSet.class);
		when(verifyNoteId.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement removeReminderStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("DELETE FROM reminders WHERE id = ?"))).thenReturn(removeReminderStatement);
		ResultSet rs2 = mock(ResultSet.class);
		when(removeReminderStatement.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);

		// when
		boolean result = notesService.removeReminder(0, 0);

		// then
		assertTrue(result);
	}

	@Test
	void removeReminderFalseTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement verifyNoteId = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT note_id FROM reminders WHERE id = ?"))).thenReturn(verifyNoteId);
		ResultSet rs = mock(ResultSet.class);
		when(verifyNoteId.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = notesService.removeReminder(0, 0);

		// then
		assertFalse(result);
	}

	@Test
	void removeReminderFalseNoteIdTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement verifyNoteId = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT note_id FROM reminders WHERE id = ?"))).thenReturn(verifyNoteId);
		ResultSet rs = mock(ResultSet.class);
		when(verifyNoteId.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		PreparedStatement removeReminderStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("DELETE FROM reminders WHERE id = ?"))).thenReturn(removeReminderStatement);
		ResultSet rs2 = mock(ResultSet.class);
		when(removeReminderStatement.getResultSet()).thenReturn(rs2);
		when(rs2.next()).thenReturn(true);

		// when
		boolean result = notesService.removeReminder(1, 0);

		// then
		assertFalse(result);
	}

	@Test
	void removeReminderException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("SELECT note_id FROM reminders WHERE id = ?"))).thenThrow(sqlException);

		// when
		boolean result = notesService.removeReminder(0, 0);

		// then
		assertFalse(result);
	}

	@Test
	void removeNoteTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement deleteNoteStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("DELETE FROM notes WHERE id = ?"))).thenReturn(deleteNoteStatement);

		// when
		boolean result = notesService.removeNote(0);

		// then
		assertTrue(result);
	}

	@Test
	void removeNoteFalseTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("DELETE FROM notes WHERE id = ?"))).thenThrow(sqlException);

		// when
		boolean result = notesService.removeNote(1);

		// then
		assertFalse(result);
	}

	@Test
	void userContainsAnyNotesTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement("SELECT * FROM notes WHERE user_id = ?")).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);

		// when
		boolean result = notesService.userContainsAnyNotes(user);

		// then
		assertTrue(result);
	}

	@Test
	void userContainsAnyNotesFalseTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement("SELECT * FROM notes WHERE user_id = ?")).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = notesService.userContainsAnyNotes(user);

		// then
		assertFalse(result);
	}

	@Test
	void noteContainsAnyRemindersTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement("SELECT * FROM reminders WHERE note_id = ?")).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);

		// when
		boolean result = notesService.noteContainsAnyReminders(0);

		// then
		assertTrue(result);
	}

	@Test
	void noteContainsAnyRemindersFalseTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement("SELECT * FROM reminders WHERE note_id = ?")).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = notesService.noteContainsAnyReminders(0);

		// then
		assertFalse(result);
	}

	@Test
	void noteWithThisIdExistAndBelongsToUserTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement("SELECT * FROM notes WHERE id = ? AND user_id = ?")).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertTrue(result);
	}

	@Test
	void noteWithThisIdExistAndBelongsToUserFalseTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement("SELECT * FROM notes WHERE id = ? AND user_id = ?")).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertFalse(result);
	}

	@Test
	void noteWithThisIdExistAndBelongsToUserException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement("SELECT * FROM notes WHERE id = ? AND user_id = ?")).thenThrow(sqlException);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertFalse(result);
	}

	@Test
	void reminderWithThisIdExistAndBelongToNoteTrueTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM reminders WHERE id = ? AND note_id = ?"))).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(true);

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(0, 0);

		// then
		assertTrue(result);
	}

	@Test
	void reminderWithThisIdExistAndBelongToNoteFalseTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement(eq("SELECT * FROM reminders WHERE id = ? AND note_id = ?"))).thenReturn(preparedStatement);
		ResultSet rs = mock(ResultSet.class);
		when(preparedStatement.getResultSet()).thenReturn(rs);
		when(rs.next()).thenReturn(false);

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(0, 1);

		// then
		assertFalse(result);
	}

	@Test
	void reminderWithThisIdExistAndBelongToNoteException() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		addNote(user, connection, notesService);
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement(eq("SELECT * FROM reminders WHERE id = ? AND note_id = ?"))).thenThrow(sqlException);

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(0, 1);

		// then
		assertFalse(result);
		verify(sqlException, times(1)).printStackTrace();
	}

	@Test
	void preparedStatementExceptionTest() throws SQLException {
		// given
		Connection connection = mock(Connection.class);
		INotesService notesService = new DatabaseNotesService(connection);
		SQLException sqlException = mock(SQLException.class);
		when(connection.prepareStatement("SELECT * FROM reminders WHERE note_id = ?")).thenThrow(sqlException);

		// when
		boolean result = notesService.noteContainsAnyReminders(0);

		// then
		assertFalse(result);
	}
}
