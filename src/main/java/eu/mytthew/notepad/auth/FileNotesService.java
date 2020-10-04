package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		JSONObject generalJSON = new JSONObject();
		JSONArray notesArray = new JSONArray();
		if (userContainsAnyNotes(user)) {
			List<Note> currentNotes = getAllNotes(user);
			for (Note note1 : currentNotes) {
				JSONObject noteObject = new JSONObject();
				int id = note1.getId();
				String title = note1.getTitle();
				String content = note1.getContent();
				LocalDate localDate = note1.getNoteDate();
				int userId = note1.getUserId();
				noteObject.put("id", id);
				noteObject.put("title", title);
				noteObject.put("content", content);
				noteObject.put("date", localDate);
				noteObject.put("user_id", userId);
				notesArray.put(noteObject);
			}
			file.deleteFile(user.getNickname());
		}
		Note newNote = new Note(noteProvider.next(), note.getTitle(), note.getContent(), note.getNoteDate(), user.getId());
		JSONObject newNoteObject = new JSONObject();
		newNoteObject.put("id", newNote.getId());
		newNoteObject.put("title", newNote.getTitle());
		newNoteObject.put("content", newNote.getContent());
		newNoteObject.put("date", newNote.getNoteDate());
		newNoteObject.put("user_id", newNote.getUserId());
		notesArray.put(newNoteObject);
		generalJSON.put("notes", notesArray);
		file.createFile(user.getNickname(), generalJSON);
		return newNote;

	}

	@Override
	public void editNote(User user, int noteId, String newTitle, String newContent, String newDate) {
	}

	@Override
	public boolean removeNote(int id) {
		return false;
	}

	@Override
	public List<Note> getAllNotes(User user) {
		if (!userContainsAnyNotes(user)) {
			return Collections.emptyList();
		}
		List<Note> notes = new ArrayList<>();
		JSONObject obj = file.openFile(user.getNickname());
		JSONArray arrayNotes = obj.getJSONArray("notes");
		for (int i = 0; i < arrayNotes.length(); i++) {
			JSONObject innerNoteObject = arrayNotes.getJSONObject(i);
			int id = innerNoteObject.getInt("id");
			String noteTitle = innerNoteObject.getString("title");
			String noteContent = innerNoteObject.getString("content");
			LocalDate noteDate = LocalDate.parse(innerNoteObject.getString("date"));
			int userId = innerNoteObject.getInt("user_id");
			Note note = new Note(id, noteTitle, noteContent, noteDate, userId);
			notes.add(note);
		}
		return notes;

	}

	@Override
	public List<Reminder> getAllReminders(int noteId) {
		if (!noteContainsAnyReminders(noteId)) {
			return Collections.emptyList();
		}
		List<Reminder> reminders = new ArrayList<>();
		JSONObject obj = reminderOperation.openFile(String.valueOf(noteId));
		JSONArray arrayReminders = obj.getJSONArray("reminders");
		for (int i = 0; i < arrayReminders.length(); i++) {
			JSONObject innerReminderObject = arrayReminders.getJSONObject(i);
			int id = innerReminderObject.getInt("id");
			String reminderName = innerReminderObject.getString("name");
			LocalDate reminderDate = LocalDate.parse(innerReminderObject.getString("date"));
			int note_id = innerReminderObject.getInt("note_id");
			Reminder reminder = new Reminder(id, reminderName, reminderDate, note_id);
			reminders.add(reminder);
		}
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
		return file.fileExist(user.getNickname());
	}

	@Override
	public boolean noteContainsAnyReminders(int noteId) {
		return reminderOperation.fileExist(String.valueOf(noteId));
	}
}
