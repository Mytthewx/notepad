package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RuntimeNotesService implements INotesService {
	private User loggedUser;

	@Override
	public void addNote(User user, Note note) {
		int id = IdProvider.instance.getNextSequence();
		String title = note.getTitle();
		String content = note.getContent();
		LocalDate localDate = note.getNoteDate();
		Note newNote = new Note(id, title, content, localDate);
		loggedUser.addNote(newNote);
	}

	@Override
	public void editNote(int noteId, String newTitle, String newContent, String newDate) {
		if (!newTitle.equals("")) {
			loggedUser.getNotes().get(noteId).setTitle(newTitle);
		}
		if (!newContent.equals("")) {
			loggedUser.getNotes().get(noteId).setTitle(newContent);
		}
		if (!newDate.equals("")) {
			loggedUser.getNotes().get(noteId).setNoteDate(LocalDate.parse(newDate));
		}
	}

	@Override
	public boolean removeNote(int id) {
		Optional<Note> optionalNote = loggedUser.getNotes()
				.stream()
				.filter(note -> note.getId() == id)
				.findAny();
		if (optionalNote.isPresent()) {
			loggedUser.removeNote(optionalNote.get());
			return true;
		}
		return false;
	}

	@Override
	public boolean reminderWithThisIdExist(int id) {
		return false;
	}

	@Override
	public boolean noteWithThisIdExist(int id) {
		return loggedUser
				.getNotes()
				.stream()
				.anyMatch(note -> note.getId() == id);
	}

	@Override
	public boolean userContainsAnyNotes(User user) {
		return !user.getNotes().isEmpty();
	}

	@Override
	public boolean noteContainsAnyReminders(int noteId) {
		return false;
	}

	@Override
	public List<Note> getAllNotes(User user) {
		loggedUser = user;
		return user.getNotes();
	}

	@Override
	public List<Reminder> getAllReminders(int noteId) {
		return loggedUser.getNotes().get(noteId).getReminders();
	}

	@Override
	public void addReminder(int noteId, Reminder reminder) {

	}

	@Override
	public void editReminder(int reminderId, String newName, String newDate) {

	}

	@Override
	public boolean removeReminder(int noteId, int reminderId) {
		return false;
	}

	@Override
	public boolean checkIfUserHasNoteWithId(int idUser, int idNote) {
		return loggedUser.getNotes()
				.stream()
				.anyMatch(note -> note.getId() == idNote);
	}
}
