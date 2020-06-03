package eu.mytthew.notepad;

import eu.mytthew.notepad.auth.IAuthService;
import eu.mytthew.notepad.auth.RuntimeAuthService;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class NotepadTest {
	@Test
	public void addNoteWithTitleAndContent() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.now());

		// when
		authService.getLoggedUser().addNote(note);

		// then
		assertEquals(1, authService.getLoggedUser().getNotes().size());
		assertEquals(note, authService.getLoggedUser().getNotes().get(0));
	}

	@Test
	public void removeNoteAfterAddingNoteWithCorrectId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		User loggedUser = authService.getLoggedUser();
		Note note = new Note("Title", "Content", LocalDate.now());
		authService.getLoggedUser().addNote(note);

		// when
		boolean result = loggedUser.removeNote(note.getUuid());

		// then
		assertTrue(result);
		assertEquals(0, authService.getLoggedUser().getNotes().size());
	}

	@Test
	public void removeNoteAfterAddingNoteWithWrongId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		User loggedUser = authService.getLoggedUser();
		Note note = new Note("Title", "Content", LocalDate.now());
		Note note2 = new Note("Title", "Content", LocalDate.now());
		loggedUser.addNote(note);

		// when
		boolean result = loggedUser.removeNote(note2.getUuid());

		// then
		assertFalse(result);
		assertEquals(1, authService.getLoggedUser().getNotes().size());
	}

	@Test
	public void removeNonExistingNotesAfterAddingTwoNotes() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		User loggedUser = authService.getLoggedUser();
		Note note = new Note("Title1", "Content1", LocalDate.now());
		Note note2 = new Note("Title2", "Content2", LocalDate.now());
		Note note3 = new Note("Title1", "Content3", LocalDate.now());
		loggedUser.addNote(note);
		loggedUser.addNote(note2);

		// when
		boolean result = loggedUser.removeNote(UUID.fromString("00000000-0000-0000-0000-000000000000"));

		// then
		assertFalse(result);
		assertEquals(2, loggedUser.getNotes().size());
	}

	@Test
	public void removeNoteUsingNoteObjectArgument() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		User loggedUser = authService.getLoggedUser();
		Note note = new Note("Title1", "Content1", LocalDate.now());
		Note note2 = new Note("Title2", "Content2", LocalDate.now());
		loggedUser.addNote(note);
		loggedUser.addNote(note2);

		// when
		boolean result = loggedUser.removeNote(note);

		// then
		assertTrue(result);
		assertEquals(1, loggedUser.getNotes().size());
	}

	@Test
	public void removeEmptyListOfNotesUsingNoteObjectArgument() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		User loggedUser = authService.getLoggedUser();
		Note note = new Note("Title1", "Content1", LocalDate.now());

		// when
		boolean result = loggedUser.removeNote(note);

		// then
		assertFalse(result);
		assertEquals(0, loggedUser.getNotes().size());
	}
}
