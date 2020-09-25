package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.util.List;

public interface INotesService {
	void addNote(User user, Note note);

	void editNote(int noteId, String newTitle, String newContent, String newDate);

	boolean removeNote(int id);

	boolean reminderWithThisIdExist(int id);

	boolean noteWithThisIdExist(int id);

	boolean userContainsAnyNotes(User user);

	boolean noteContainsAnyReminders(int noteId);

	List<Note> getAllNotes(User user);

	List<Reminder> getAllReminders(int noteId);

	void addReminder(int noteId, Reminder reminder);

	void editReminder(int reminderId, String newName, String newDate);

	boolean removeReminder(int noteId, int reminderId);

	boolean checkIfUserHasNoteWithId(int idUser, int idNote);
}
