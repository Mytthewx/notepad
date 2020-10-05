package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.Reminder;
import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

class RuntimeNotesServiceTest {
	@Test
	void addNoteTest() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-29"));

		// when
		notesService.addNote(user, note);

		// then
		assertEquals(1, notesService.getAllNotes(user).size());
		assertNotEquals(0, notesService.getAllNotes(user).size());
		assertNotEquals(2, notesService.getAllNotes(user).size());
	}

	@Test
	void addNoteWithReminderTest() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-29"));
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder", LocalDate.parse("2020-09-28"));

		// when
		notesService.addReminder(0, reminder);

		// then
		assertEquals(1, notesService.getAllNotes(user).size());
		assertNotEquals(0, notesService.getAllNotes(user).size());
		assertNotEquals(2, notesService.getAllNotes(user).size());
	}

	@Test
	void editNoteWhenExist() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note(0, "Title", "Content", LocalDate.parse("2020-09-27"), user.getId());
		notesService.addNote(user, note);

		// when
		notesService.editNote(0, "New Title", "New Content", "2020-09-29");

		// then
		assertEquals("New Title", notesService.getAllNotes(user).get(0).getTitle());
	}

	@Test
	void editNoteWhenNotExist() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note(0, "Title", "Content", LocalDate.parse("2020-09-27"), user.getId());
		notesService.addNote(user, note);

		// when
		notesService.editNote(1, "New Title", "New Content", "2020-09-29");

		// then
		assertTrue(notesService.noteWithThisIdExistAndBelongToUser(0, user));
		assertFalse(notesService.noteWithThisIdExistAndBelongToUser(1, user));
	}

	@Test
	void editNoteEmptyNewLines() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note(0, "Title", "Content", LocalDate.parse("2020-09-27"), user.getId());
		notesService.addNote(user, note);

		// when
		notesService.editNote(0, "", "", "");

		// then
		assertEquals("Title", notesService.getAllNotes(user).get(0).getTitle());
	}

	@Test
	void editReminderWhenExist() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note(0, "Title", "Content", LocalDate.parse("2020-09-27"), user.getId());
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));
		notesService.addReminder(0, reminder);

		// when
		notesService.editReminder(0, "New name", "2020-09-29");

		// then
		assertEquals("New name", notesService.getAllReminders(0).get(0).getName());
	}

	@Test
	void editReminderWrongId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note(0, "Title", "Content", LocalDate.parse("2020-09-27"), user.getId());
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));
		notesService.addReminder(0, reminder);

		// when
		notesService.editReminder(1, "New name", "2020-09-29");

		// then
		assertTrue(notesService.reminderWithThisIdExistAndBelongToNote(0, 0));
		assertFalse(notesService.reminderWithThisIdExistAndBelongToNote(1, 0));
	}

	@Test
	void editReminderWithEmptyLines() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note(0, "Title", "Content", LocalDate.parse("2020-09-27"), user.getId());
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));
		notesService.addReminder(0, reminder);

		// when
		notesService.editReminder(0, "", "");

		// then
		assertEquals("Reminder name", notesService.getAllReminders(0).get(0).getName());
	}

	@Test
	void removeNoteTrue() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note(0, "Title", "Content", LocalDate.parse("2020-09-27"), user.getId());
		notesService.addNote(user, note);

		// when
		boolean result = notesService.removeNote(note.getId());

		// then
		assertTrue(result);
		assertEquals(0, notesService.getAllNotes(user).size());
	}

	@Test
	void removeNoteFalse() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.removeNote(1);

		// then
		assertFalse(result);
	}

	@Test
	void addReminder() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));

		// when
		notesService.addReminder(0, reminder);

		// then
		assertEquals(1, notesService.getAllReminders(0).size());
	}

	@Test
	void removeReminderTrue() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));
		notesService.addReminder(0, reminder);

		// when
		boolean result = notesService.removeReminder(0, 0);

		// then
		assertTrue(result);
	}

	@Test
	void removeReminderFalse() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.removeReminder(0, 0);

		// then
		assertFalse(result);
	}

	@Test
	void removeReminderFalseWrongId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));
		notesService.addReminder(0, reminder);

		// when
		boolean result = notesService.removeReminder(0, 1);

		// then
		assertFalse(result);
	}

	@Test
	void containsAnyNotesTrue() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.userContainsAnyNotes(user);

		// then
		assertTrue(result);
	}

	@Test
	void containsAnyNotesFalse() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = notesService.userContainsAnyNotes(user);

		// then
		assertFalse(result);
	}

	@Test
	void containsAnyNotesFalseWithWrongUserId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		User user2 = authService.addUser("Andret", "12345");
		authService.login("Andret", "12345");
		Note note = new Note("title", "content", LocalDate.parse("2020-09-28"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.userContainsAnyNotes(user2);

		// then
		assertFalse(result);
	}

	@Test
	void containsAnyRemindersTrue() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));
		notesService.addReminder(0, reminder);

		// when
		boolean result = notesService.noteContainsAnyReminders(0);

		// then
		assertTrue(result);
	}

	@Test
	void containsAnyRemindersFalse() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.noteContainsAnyReminders(0);

		// then
		assertFalse(result);
	}

	@Test
	void containsAnyRemindersFalseWrongId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));
		notesService.addReminder(0, reminder);

		// when
		boolean result = notesService.noteContainsAnyReminders(1);

		// then
		assertFalse(result);
	}

	@Test
	void containsAnyRemindersFalseEmptyList() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.noteContainsAnyReminders(1);

		// then
		assertFalse(result);
	}

	@Test
	void containsAnyRemindersFalseEmptyReminderAndNotesList() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = notesService.noteContainsAnyReminders(1);

		// then
		assertFalse(result);
	}

	@Test
	void reminderExistAndBelongsToNoteTrue() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Reminder name", LocalDate.parse("2020-09-27"));
		notesService.addReminder(0, reminder);

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(0, 0);

		// then
		assertTrue(result);
	}

	@Test
	void reminderExistAndBelongsToNoteFalse() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(0, 0);

		// then
		assertFalse(result);
	}

	@Test
	void reminderExistAndBelongsToNoteFalseWithTwoWrongId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);
		Reminder reminder = new Reminder("Name", LocalDate.parse("2020-09-28"));
		notesService.addReminder(0, reminder);

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(0, 2);

		// then
		assertFalse(result);
	}

	@Test
	void reminderExistAndBelongsToNoteFalseEmptyNoteList() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(0, 0);

		// then
		assertFalse(result);
	}

	@Test
	void reminderExistAndBelongsToNoteFalseWrongNoteId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(0, 1);

		// then
		assertFalse(result);
	}

	@Test
	void reminderExistAndBelongsToNoteFalseWrongReminderId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.reminderWithThisIdExistAndBelongToNote(1, 0);

		// then
		assertFalse(result);
	}

	@Test
	void noteExistAndBelongsToUserTrue() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertTrue(result);
	}

	@Test
	void noteExistAndBelongsToUserFalse() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user);

		// then
		assertFalse(result);
	}

	@Test
	void noteExistAndBelongsToUserFalseWrongId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(1, user);

		// then
		assertFalse(result);
	}

	@Test
	void noteExistAndBelongsToUserFalseTwoWrongId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = authService.addUser("Myttew", "123");
		User user2 = authService.addUser("Andret", "12345");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.noteWithThisIdExistAndBelongToUser(0, user2);

		// then
		assertFalse(result);
	}
}
