package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Config;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

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
import static org.mockito.Mockito.when;

class FileNotesServiceTest {

	@Test
	void addFirstNoteTest() {
		// given
		Config config = mock(Config.class);
		FileOperation fileOperation = mock(FileOperation.class);
		INotesService notesService = new FileNotesService(fileOperation, config);
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
		INotesService notesService = new FileNotesService(fileOperation, config);
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
		INotesService notesService = new FileNotesService(fileOperation, config);
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
		INotesService notesService = new FileNotesService(fileOperation, config);
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
		INotesService notesService = new FileNotesService(fileOperation, config);
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
		INotesService notesService = new FileNotesService(fileOperation, config);
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
}
