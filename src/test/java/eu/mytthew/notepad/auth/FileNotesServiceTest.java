package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Config;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.Reminder;
import eu.mytthew.notepad.entity.User;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileNotesServiceTest {

	@Test
	void addFirstNoteTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-15"));
		when(fileOperation.fileExist(any())).thenReturn(false);

		// when
		Note result = notesService.addNote(user, note);

		// then
		assertEquals(note, result);
	}

	@Test
	void addAnotherNoteTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-04"));
		Note note2 = new Note("Title2", "Content2", LocalDate.parse("2020-10-05"));
		notesService.addNote(user, note);

		// when
		Note result = notesService.addNote(user, note2);

		// then
		assertEquals(1, result.getId());
	}

	@Test
	void removeNoteTrueTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-04"));
		notesService.addNote(user, note);
		when(fileOperation.fileExist(eq("0"))).thenReturn(true);

		// when
		boolean result = notesService.removeNote(0);

		// then
		assertTrue(result);
	}

	@Test
	void removeNoteFalseTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-04"));
		notesService.addNote(user, note);
		when(fileOperation.fileExist(eq("0"))).thenReturn(false);

		// when
		boolean result = notesService.removeNote(0);

		// then
		assertFalse(result);
	}

	@Test
	void getAllNotesTestEmptyStream() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		when(fileOperation.filesStream(any())).thenReturn(Stream.of());

		// when
		List<Note> result = notesService.getAllNotes(user);

		// then
		assertEquals(0, result.size());
	}

	@Test
	void getAllNotesTestWithNotes() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-15"));
		notesService.addNote(user, note);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.filesStream(any())).thenReturn(Stream.of(Paths.get(".")));
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("title"))).thenReturn("Title");
		when(jsonObject.getString(eq("content"))).thenReturn("Content");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-15");
		when(jsonObject.getInt(eq("user_id"))).thenReturn(0);

		// when
		List<Note> result = notesService.getAllNotes(user);

		// then
		assertEquals(1, result.size());
	}

	@Test
	void addFirstReminderTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		when(fileOperation.fileExist(any())).thenReturn(false);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-15"));

		// when
		Reminder result = notesService.addReminder(0, reminder);

		// then
		assertEquals(reminder, result);
	}

	@Test
	void addAnotherReminderTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		when(fileOperation.fileExist(any())).thenReturn(false);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-15"));
		notesService.addReminder(0, reminder);
		Reminder reminder2 = new Reminder(1, "Name", LocalDate.parse("2020-10-15"), 0);

		// when
		Reminder result = notesService.addReminder(0, reminder2);

		// then
		assertEquals(reminder2, result);
	}

	@Test
	void removeReminderTrueTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		when(fileOperation.fileExist(eq("0"))).thenReturn(true);
		when(reminderOperation.fileExist(eq("0"))).thenReturn(true);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-15"));
		notesService.addReminder(0, reminder);

		// when
		boolean result = notesService.removeReminder(0, 0);

		// then
		assertTrue(result);
	}

	@Test
	void removeReminderFalseTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		when(fileOperation.fileExist(eq("0"))).thenReturn(false);

		// when
		boolean result = notesService.removeReminder(0, 0);

		// then
		assertFalse(result);
	}

	@Test
	void getAllRemindersTestEmptyStream() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(reminderOperation.filesStream(any())).thenReturn(Stream.empty());
		when(reminderOperation.openFile(any())).thenReturn(jsonObject);

		// when
		List<Reminder> result = notesService.getAllReminders(0);

		// then
		assertEquals(0, result.size());
	}

	@Test
	void getAllRemindersTestWithReminders() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(reminderOperation.filesStream(any())).thenReturn(Stream.of(Paths.get(".")));
		when(reminderOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("name"))).thenReturn("Name");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-15");
		when(jsonObject.getInt(eq("note_id"))).thenReturn(0);

		// when
		List<Reminder> result = notesService.getAllReminders(0);

		// then
		assertEquals(1, result.size());
	}

	@Test
	void getAllRemindersTestWrongNoteId() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(reminderOperation.filesStream(any())).thenReturn(Stream.of(Paths.get(".")));
		when(reminderOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("name"))).thenReturn("Name");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-15");
		when(jsonObject.getInt(eq("note_id"))).thenReturn(0);

		// when
		List<Reminder> result = notesService.getAllReminders(1);

		// then
		assertEquals(0, result.size());
	}

	@Test
	void editNoteTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-15"));
		User user = mock(User.class);
		when(user.getId()).thenReturn(0);
		JSONObject jsonObject = mock(JSONObject.class);
		ArgumentCaptor<JSONObject> argument = ArgumentCaptor.forClass(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("title"))).thenReturn("New title");
		when(jsonObject.getString(eq("content"))).thenReturn("New content");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-16");
		when(jsonObject.getInt(eq("user_id"))).thenReturn(0);
		Note note2 = new Note("New title", "New content", LocalDate.parse("2020-10-16"));
		JSONObject expected = note2.serialize();

		// when
		notesService.editNote(0, "New title", "New content", "2020-10-16");

		// then
		verify(fileOperation, times(1)).createFile(eq("0"), argument.capture());
		assertTrue(argument.getValue().similar(expected));
	}

	@Test
	void editNoteTestWithoutChanges() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-15"));
		User user = mock(User.class);
		when(user.getId()).thenReturn(0);
		JSONObject jsonObject = mock(JSONObject.class);
		ArgumentCaptor<JSONObject> argument = ArgumentCaptor.forClass(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("title"))).thenReturn("Title");
		when(jsonObject.getString(eq("content"))).thenReturn("Content");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-15");
		when(jsonObject.getInt(eq("user_id"))).thenReturn(0);
		JSONObject expected = note.serialize();

		// when
		notesService.editNote(0, "", "", "");

		// then
		verify(fileOperation, times(1)).createFile(eq("0"), argument.capture());
		assertTrue(argument.getValue().similar(expected));
	}

	@Test
	void editReminderTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-21"));
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(reminderOperation.filesStream(any())).thenReturn(Stream.of(Paths.get(".")));
		when(reminderOperation.openFile(any())).thenReturn(jsonObject);
		ArgumentCaptor<JSONObject> argument = ArgumentCaptor.forClass(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("name"))).thenReturn("New Name");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-22");
		when(jsonObject.getInt(eq("note_id"))).thenReturn(0);
		Reminder reminder2 = new Reminder("New Name", LocalDate.parse("2020-10-22"));
		JSONObject expected = reminder2.serialize();

		// when
		notesService.editReminder(0, "New Name", "2020-10-22");

		// then
		verify(reminderOperation, times(1)).createFile(eq("0"), argument.capture());
		assertTrue(argument.getValue().similar(expected));
	}

	@Test
	void editReminderTestWithoutChanges() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-10-21"));
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(reminderOperation.filesStream(any())).thenReturn(Stream.of(Paths.get(".")));
		when(reminderOperation.openFile(any())).thenReturn(jsonObject);
		ArgumentCaptor<JSONObject> argument = ArgumentCaptor.forClass(JSONObject.class);
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("name"))).thenReturn("Name");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-21");
		when(jsonObject.getInt(eq("note_id"))).thenReturn(0);
		JSONObject expected = reminder.serialize();

		// when
		notesService.editReminder(0, "", "");

		// then
		verify(reminderOperation, times(1)).createFile(eq("0"), argument.capture());
		assertTrue(argument.getValue().similar(expected));
	}

	@Test
	void noteWithThisIdExistAndBelongToUserTrueTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.fileExist(any())).thenReturn(true);
		when(fileOperation.filesStream(any())).thenReturn(Stream.of(Paths.get(".")));
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("title"))).thenReturn("Title");
		when(jsonObject.getString(eq("content"))).thenReturn("Content");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-15");
		when(jsonObject.getInt(eq("user_id"))).thenReturn(0);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertTrue(result);
	}

	@Test
	void noteWithThisIdExistAndBelongToUserFalseTestWrongNoteId() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.fileExist(any())).thenReturn(true);
		when(fileOperation.filesStream(any())).thenReturn(Stream.of(Paths.get(".")));
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(1);
		when(jsonObject.getString(eq("title"))).thenReturn("Title");
		when(jsonObject.getString(eq("content"))).thenReturn("Content");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-15");
		when(jsonObject.getInt(eq("user_id"))).thenReturn(0);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertFalse(result);
	}

	@Test
	void noteWithThisIdExistAndBelongToUserFalseTestWrongUserId() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		JSONObject jsonObject = mock(JSONObject.class);
		when(fileOperation.fileExist(any())).thenReturn(true);
		when(fileOperation.filesStream(any())).thenReturn(Stream.of(Paths.get(".")));
		when(fileOperation.openFile(any())).thenReturn(jsonObject);
		when(jsonObject.getInt(eq("id"))).thenReturn(0);
		when(jsonObject.getString(eq("title"))).thenReturn("Title");
		when(jsonObject.getString(eq("content"))).thenReturn("Content");
		when(jsonObject.getString(eq("date"))).thenReturn("2020-10-15");
		when(jsonObject.getInt(eq("user_id"))).thenReturn(1);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertFalse(result);
	}

	@Test
	void noteWithThisIdExistAndBelongToUserFalseTestNoteNotExist() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		FileOperation reminderOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, reminderOperation, config);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		when(fileOperation.fileExist(any())).thenReturn(false);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertFalse(result);
	}
}
