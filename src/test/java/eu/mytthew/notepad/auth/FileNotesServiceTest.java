package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

class FileNotesServiceTest {
	@Test
	void addNoteTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-04"));

		// when
		Note result = notesService.addNote(user, note);

		// then
		assertEquals(note, result);
	}

	@Test
	void removeNoteTrueTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-04"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.removeNote(0);

		// then
		assertTrue(result);
	}

	@Test
	void removeNoteFalseTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-04"));
		notesService.addNote(user, note);

		// when
		boolean result = notesService.removeNote(1);

		// then
		assertFalse(result);
	}

	@Test
	void getAllNotesTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation);
		IAuthService authService = new RuntimeAuthService();
		User user = authService.addUser("Mytthew", "123");
		User user2 = authService.addUser("Andret", "12345");
		Note note = new Note("Title", "Content", LocalDate.parse("2020-10-04"));
		Note note2 = new Note("Title2", "Content2", LocalDate.parse("2020-10-04"));
		Note note3 = new Note("Title3", "Content3", LocalDate.parse("2020-10-04"));
		notesService.addNote(user, note);
		notesService.addNote(user, note2);
		notesService.addNote(user2, note3);

		// when
		List<Note> result = notesService.getAllNotes(user);

		// then
		assertEquals(3, result.size());
	}
}
