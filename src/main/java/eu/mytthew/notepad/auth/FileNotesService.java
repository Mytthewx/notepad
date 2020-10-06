package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.Reminder;
import eu.mytthew.notepad.entity.User;
import org.json.JSONObject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileNotesService implements INotesService {
	private final IdProvider noteProvider = new IdProvider();
	private final IdProvider reminderProvider = new IdProvider();
	FileOperation reminderOperation = new FileOperation("reminders");
	private final FileOperation file;

	public FileNotesService(FileOperation file) {
		this.file = file;
	}

	@Override
	public Note addNote(User user, Note note) {
		Note newNote = new Note(noteProvider.next(), note.getTitle(), note.getContent(), note.getNoteDate(), user.getId());
		file.createFile(String.valueOf(newNote.getId()), newNote.serialize());
		return newNote;
	}

	@Override
	public void editNote(int noteId, String newTitle, String newContent, String newDate) {
	}

	@Override
	public boolean removeNote(int id) {
		if (file.fileExist(String.valueOf(id))) {
			file.deleteFile(String.valueOf(id));
			return true;
		}
		return false;
	}

	@Override
	public List<Note> getAllNotes(User user) {
		List<Note> notes = new ArrayList<>();
		Stream<Path> pathStream = file.filesStream("notes");
		pathStream.forEach(filename -> {
			JSONObject jsonObject = file.openFile(filename.getFileName().toString());
			Note note = new Note(0, "", "", null, 0);
			note.deserialize(jsonObject);
			notes.add(note);
		});
		return notes;
	}

	@Override
	public List<Reminder> getAllReminders(int noteId) {
		List<Reminder> reminders = new ArrayList<>();
		Stream<Path> pathStream = file.filesStream("reminders");
		pathStream.forEach(filename -> {
			JSONObject jsonObject = file.openFile(filename.getFileName().toString());
			Reminder reminder = new Reminder(0, "", null, 0);
			reminder.deserialize(jsonObject);
			if (reminder.getNoteId() == noteId) {
				reminders.add(reminder);
			}
		});
		return reminders;
	}

	@Override
	public Reminder addReminder(int noteId, Reminder reminder) {
		Reminder newReminder = new Reminder(reminderProvider.next(), reminder.getName(), reminder.getDate(), noteId);
		file.createFile(String.valueOf(newReminder.getId()), newReminder.serialize());
		return newReminder;
	}

	@Override
	public void editReminder(int reminderId, String newName, String newDate) {
	}

	@Override
	public boolean removeReminder(int noteId, int reminderId) {
		if (file.fileExist(String.valueOf(reminderId))) {
			file.deleteFile(String.valueOf(reminderId));
			return true;
		}
		return false;
	}

	@Override
	public boolean noteWithThisIdExistAndBelongToUser(int noteId, User user) {
		if (!file.fileExist(String.valueOf(noteId))) {
			return false;
		}
		return getAllNotes(user)
				.stream()
				.filter(note -> note.getUserId() == user.getId())
				.anyMatch(note -> note.getId() == noteId);
	}

	@Override
	public boolean reminderWithThisIdExistAndBelongToNote(int reminderId, int noteId) {
		if (!reminderOperation.fileExist(String.valueOf(noteId))) {
			return false;
		}
		return getAllReminders(noteId)
				.stream()
				.filter(reminder -> reminder.getId() == reminderId)
				.anyMatch(reminder -> reminder.getNoteId() == noteId);
	}

	@Override
	public boolean userContainsAnyNotes(User user) {
		return getAllNotes(user)
				.stream()
				.anyMatch(note -> note.getUserId() == user.getId());
	}

	@Override
	public boolean noteContainsAnyReminders(int noteId) {
		return getAllReminders(noteId)
				.stream()
				.anyMatch(reminder -> reminder.getNoteId() == noteId);
	}
}
