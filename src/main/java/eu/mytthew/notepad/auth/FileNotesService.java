package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Config;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.Reminder;
import eu.mytthew.notepad.entity.User;
import org.json.JSONObject;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileNotesService implements INotesService {
	private final Config config;
	private final IdProvider noteProvider;
	private final IdProvider reminderProvider;
	private final FileOperation reminderOperation;
	private final FileOperation file;

	public FileNotesService(FileOperation file, FileOperation reminderOperation, Config config) {
		this.file = file;
		this.config = config;
		this.reminderOperation = reminderOperation;
		noteProvider = new IdProvider(config.getNoteId());
		reminderProvider = new IdProvider(config.getReminderId());
	}

	@Override
	public Note addNote(User user, Note note) {
		int id = noteProvider.next();
		Note newNote = new Note(id, note.getTitle(), note.getContent(), note.getNoteDate(), user.getId());
		file.createFile(String.valueOf(newNote.getId()), newNote.serialize());
		return newNote;
	}

	@Override
	public void editNote(int noteId, String newTitle, String newContent, String newDate) {
		Note note = new Note(noteId, "", "", null, 0);
		note.deserialize(file.openFile(String.valueOf(noteId)));
		if (newTitle.equals("")) {
			newTitle = note.getTitle();
		}
		if (newContent.equals("")) {
			newContent = note.getContent();
		}
		if (newDate.equals("")) {
			newDate = note.getNoteDate().toString();
		}
		file.deleteFile(String.valueOf(noteId));
		note.setTitle(newTitle);
		note.setContent(newContent);
		note.setNoteDate(LocalDate.parse(newDate));
		file.createFile(String.valueOf(noteId), note.serialize());
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
		Stream<Path> pathStream = reminderOperation.filesStream("reminders");
		pathStream.forEach(filename -> {
			JSONObject jsonObject = reminderOperation.openFile(filename.getFileName().toString());
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
		reminderOperation.createFile(String.valueOf(newReminder.getId()), newReminder.serialize());
		return newReminder;
	}

	@Override
	public void editReminder(int reminderId, String newName, String newDate) {
		Reminder reminder = new Reminder(reminderId, "", null, 0);
		reminder.deserialize(reminderOperation.openFile(String.valueOf(reminderId)));
		if (newName.equals("")) {
			newName = reminder.getName();
		}
		if (newDate.equals("")) {
			newDate = reminder.getDate().toString();
		}
		reminderOperation.deleteFile(String.valueOf(reminderId));
		reminder.setName(newName);
		reminder.setDate(LocalDate.parse(newDate));
		reminderOperation.createFile(String.valueOf(reminderId), reminder.serialize());
	}

	@Override
	public boolean removeReminder(int noteId, int reminderId) {
		if (reminderOperation.fileExist(String.valueOf(reminderId))) {
			reminderOperation.deleteFile(String.valueOf(reminderId));
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
