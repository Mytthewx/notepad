package eu.mytthew.notepad;

import eu.mytthew.notepad.auth.IAuthService;
import eu.mytthew.notepad.auth.RuntimeAuthService;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class NotepadTest {
	@Test
	public void addNoteWithTitleAndContent() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");

		// when
		authService.getLoggedUser().addNote(new Note("Title", "Content"));

		// then
		assertEquals(1, authService.getLoggedUser().getNotes().size());
		assertEquals("Title", authService.getLoggedUser().getNotes().get(0).getTitle());
		assertEquals("Content", authService.getLoggedUser().getNotes().get(0).getContent());
	}

	@Test
	public void removeNoteAfterAddingNoteWithCorrectId() {
		// given
		IAuthService authService = new RuntimeAuthService();
		authService.addUser("Mytthew", "123");
		authService.login("Mytthew", "123");
		User loggedUser = authService.getLoggedUser();
		Note note = new Note("Title", "Content");
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
		Note note = new Note("Title", "Content");
		Note note2 = new Note("Title", "Content");
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
		Note note = new Note("Title1", "Content1");
		Note note2 = new Note("Title2", "Content2");
		Note note3 = new Note("Title1", "Content3");
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
		Note note = new Note("Title1", "Content1");
		Note note2 = new Note("Title2", "Content2");
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
		Note note = new Note("Title1", "Content1");

		// when
		boolean result = loggedUser.removeNote(note);

		// then
		assertFalse(result);
		assertEquals(0, loggedUser.getNotes().size());
	}
}
