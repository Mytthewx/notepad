package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
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
		User user = new User("Mytthew", "123");
		authService.addUser(user.getNickname(), user.getPassword());
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
		User user = new User("Mytthew", "123");
		authService.addUser(user.getNickname(), user.getPassword());
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
	void removeNoteTrue() {
		// given
		IAuthService authService = new RuntimeAuthService();
		INotesService notesService = new RuntimeNotesService();
		User user = new User("Mytthew", "123");
		authService.addUser(user.getNickname(), user.getPassword());
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
		User user = new User("Mytthew", "123");
		authService.addUser(user.getNickname(), user.getPassword());
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-09-27"), user.getId());
		notesService.addNote(user, note);

		// when
		boolean result = notesService.removeNote(1);

		// then
		assertFalse(result);
	}
}
