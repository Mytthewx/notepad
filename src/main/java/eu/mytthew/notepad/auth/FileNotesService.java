package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.Reminder;
import eu.mytthew.notepad.entity.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Path;
import java.time.LocalDate;
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
		return false;
	}

	@Override
	public List<Note> getAllNotes(User user) {
		List<Note> notes = new ArrayList<>();
		Stream<Path> pathStream = file.filesStream("notes");
		pathStream.forEach(filename -> {
			JSONObject jsonObject = file.openFile(filename.getFileName().toString());
			int id = jsonObject.getInt("id");
			String title = jsonObject.getString("title");
			String content = jsonObject.getString("content");
			String date = jsonObject.getString("date");
			int user_id = jsonObject.getInt("user_id");
			Note note = new Note(id, title, content, LocalDate.parse(date), user_id);
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
			int id = jsonObject.getInt("id");
			String name = jsonObject.getString("name");
			String date = jsonObject.getString("date");
			int note_id = jsonObject.getInt("note_id");
			Reminder reminder = new Reminder(id, name, LocalDate.parse(date), note_id);
			if (reminder.getNoteId() == noteId) {
				reminders.add(reminder);
			}
		});
		return reminders;
	}

	@Override
	public Reminder addReminder(int noteId, Reminder reminder) {
		JSONObject generalJSON = new JSONObject();
		JSONArray reminderArray = new JSONArray();
		if (noteContainsAnyReminders(noteId)) {
			List<Reminder> currentReminders = getAllReminders(noteId);
			for (Reminder reminder1 : currentReminders) {
				JSONObject reminderObject = new JSONObject();
				int id = reminder1.getId();
				String name = reminder1.getName();
				LocalDate localDate = reminder1.getDate();
				int note_id = reminder1.getNoteId();
				reminderObject.put("id", id);
				reminderObject.put("name", name);
				reminderObject.put("date", localDate);
				reminderObject.put("note_id", note_id);
				reminderArray.put(reminderObject);
			}
			reminderOperation.deleteFile(String.valueOf(noteId));
		}
		Reminder newReminder = new Reminder(reminderProvider.next(), reminder.getName(), reminder.getDate(), noteId);
		JSONObject newReminderObject = new JSONObject();
		newReminderObject.put("id", newReminder.getId());
		newReminderObject.put("name", newReminder.getName());
		newReminderObject.put("date", newReminder.getDate());
		newReminderObject.put("note_id", newReminder.getNoteId());
		reminderArray.put(newReminderObject);
		generalJSON.put("reminders", reminderArray);
		reminderOperation.createFile(String.valueOf(noteId), generalJSON);
		return newReminder;
	}

	@Override
	public void editReminder(int reminderId, String newName, String newDate) {

	}

	@Override
	public boolean removeReminder(int noteId, int reminderId) {
		return false;
	}

	@Override
	public boolean noteWithThisIdExistAndBelongToUser(int noteId, User user) {
		if (file.fileExist(user.getNickname())) {
			List<Note> notes = getAllNotes(user);
			return notes.stream()
					.filter(note -> note.getUserId() == user.getId())
					.anyMatch(note -> note.getId() == noteId);
		}
		return false;
	}

	@Override
	public boolean reminderWithThisIdExistAndBelongToNote(int reminderId, int noteId) {
		if (reminderOperation.fileExist(String.valueOf(noteId))) {
			List<Reminder> reminders = getAllReminders(noteId);
			return reminders.stream()
					.filter(reminder -> reminder.getId() == reminderId)
					.anyMatch(reminder -> reminder.getNoteId() == noteId);
		}
		return false;
	}

	@Override
	public boolean userContainsAnyNotes(User user) {
		return true;
	}

	@Override
	public boolean noteContainsAnyReminders(int noteId) {
		return reminderOperation.fileExist(String.valueOf(noteId));
	}
}
