package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RuntimeNotesService implements INotesService {
	private final List<Note> noteList = new ArrayList<>();
	private final List<Reminder> reminderList = new ArrayList<>();
	private final IdProvider noteProvider = new IdProvider();
	private final IdProvider reminderProvider = new IdProvider();

	@Override
	public Note addNote(User user, Note note) {
		int id = noteProvider.next();
		String title = note.getTitle();
		String content = note.getContent();
		LocalDate localDate = note.getNoteDate();
		int userId = user.getId();
		Note newNote = new Note(id, title, content, localDate, userId);
		noteList.add(newNote);
		return note;
	}

	@Override
	public void editNote(User user, int noteId, String newTitle, String newContent, String newDate) {
		Optional<Note> optionalNote = noteList.stream()
				.filter(note -> note.getId() == noteId)
				.findAny();
		optionalNote.ifPresent(x -> {
			if (!newTitle.equals("")) {
				optionalNote.get().setTitle(newTitle);
			}
			if (!newContent.equals("")) {
				optionalNote.get().setContent(newContent);
			}
			if (!newDate.equals("")) {
				optionalNote.get().setNoteDate(LocalDate.parse(newDate));
			}
		});
	}

	@Override
	public boolean removeNote(int id) {
		Optional<Note> optionalNote = noteList.stream()
				.filter(note -> note.getId() == id)
				.findAny();
		if (optionalNote.isPresent()) {
			noteList.remove(optionalNote.get());
			return true;
		}
		return false;
	}

	@Override
	public List<Note> getAllNotes(User user) {
		return Collections.unmodifiableList(noteList);
	}

	@Override
	public List<Reminder> getAllReminders(int noteId) {
		return Collections.unmodifiableList(reminderList);
	}

	@Override
	public Reminder addReminder(int noteId, Reminder reminder) {
		int id = reminderProvider.next();
		String name = reminder.getName();
		LocalDate localDate = reminder.getDate();
		Reminder newReminder = new Reminder(id, name, localDate, noteId);
		reminderList.add(newReminder);
		return reminder;
	}

	@Override
	public void editReminder(int reminderId, String newName, String newDate) {
		Optional<Reminder> optionalReminder = reminderList.stream()
				.filter(reminder -> reminder.getId() == reminderId)
				.findAny();
		optionalReminder.ifPresent(x -> {
			if (!newName.equals("")) {
				optionalReminder.get().setName(newName);
			}
			if (!newDate.equals("")) {
				optionalReminder.get().setDate(LocalDate.parse(newDate));
			}
		});
	}

	@Override
	public boolean removeReminder(int noteId, int reminderId) {
		Optional<Reminder> optionalReminder = reminderList
				.stream()
				.filter(reminder -> reminder.getId() == reminderId)
				.findAny();
		if (optionalReminder.isPresent()) {
			reminderList.remove(optionalReminder.get());
			return true;
		}
		return false;
	}

	@Override
	public boolean noteWithThisIdExistAndBelongToUser(int noteId, User user) {
		if (noteList.isEmpty()) {
			return false;
		}
		return noteList
				.stream()
				.anyMatch(note -> note.getId() == noteId
						&& note.getUserId() == user.getId());
	}

	@Override
	public boolean reminderWithThisIdExistAndBelongToNote(int reminderId, int noteId) {
		if (noteList.isEmpty()) {
			return false;
		}
		if (reminderList.isEmpty()) {
			return false;
		}
		return getAllReminders(noteId)
				.stream()
				.anyMatch(reminder -> reminder.getId() == reminderId
						&& reminder.getNoteId() == noteId);
	}

	@Override
	public boolean userContainsAnyNotes(User user) {
		if (noteList.isEmpty()) {
			return false;
		}
		return getAllNotes(user)
				.stream()
				.anyMatch(note -> note.getUserId() == user.getId());
	}

	@Override
	public boolean noteContainsAnyReminders(int noteId) {
		if (noteList.isEmpty()) {
			return false;
		}
		if (reminderList.isEmpty()) {
			return false;
		}
		return getAllReminders(noteId)
				.stream()
				.anyMatch(reminder -> reminder.getNoteId() == noteId);
	}
}
