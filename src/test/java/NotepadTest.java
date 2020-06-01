import eu.mytthew.notepad.auth.IAuthService;
import eu.mytthew.notepad.auth.RuntimeAuthService;
import eu.mytthew.notepad.entity.Note;
import org.junit.jupiter.api.Test;

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
		authService.getLoggedUser().addNote(new Note("Title", "Content"));

		// when
		boolean result = authService.getLoggedUser().removeNote(0);

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
		authService.getLoggedUser().addNote(new Note("Title", "Content"));

		// when
		boolean result = authService.getLoggedUser().removeNote(1);

		// then
		assertFalse(result);
		assertEquals(1, authService.getLoggedUser().getNotes().size());
	}
}
