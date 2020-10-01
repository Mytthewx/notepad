package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseNotesService implements INotesService {
	Connection connection;

	public DatabaseNotesService(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Note> getAllNotes(User user) {
		List<Note> noteList = new ArrayList<>();
		try {
			int loggedUserId = user.getId();
			String notesSQL = "SELECT * FROM notes WHERE user_id = ?";
			PreparedStatement notesStatement = connection.prepareStatement(notesSQL);
			notesStatement.setInt(1, loggedUserId);
			notesStatement.execute();
			while (notesStatement.getResultSet().next()) {
				int id = notesStatement.getResultSet().getInt("id");
				String title = notesStatement.getResultSet().getString("title");
				String content = notesStatement.getResultSet().getString("content");
				String localDate = notesStatement.getResultSet().getString("date");
				int userId = notesStatement.getResultSet().getInt("user_id");
				Note note = new Note(id, title, content, LocalDate.parse(localDate), userId);
				noteList.add(note);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noteList;
	}

	@Override
	public List<Reminder> getAllReminders(int noteId) {
		List<Reminder> reminderList = new ArrayList<>();
		try {
			String remindersSQL = "SELECT * FROM reminders WHERE note_id = ?";
			PreparedStatement reminderStatement = connection.prepareStatement(remindersSQL);
			reminderStatement.setInt(1, noteId);
			reminderStatement.execute();
			ResultSet rs = reminderStatement.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String localDate = rs.getString("date");
				int noteIdDatabase = rs.getInt("note_id");
				Reminder reminder = new Reminder(id, name, LocalDate.parse(localDate), noteIdDatabase);
				reminderList.add(reminder);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reminderList;
	}

	@Override
	public void addNote(User user, Note note) {
		String noteToSQL = "INSERT INTO notes(title, content, date, user_id) VALUES (?, ?, ?, ?)";
		try {
			int loggedUserId = user.getId();
			PreparedStatement preparedStatement = connection.prepareStatement(noteToSQL);
			preparedStatement.setString(1, note.getTitle());
			preparedStatement.setString(2, note.getContent());
			preparedStatement.setString(3, note.getNoteDate().toString());
			preparedStatement.setInt(4, loggedUserId);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addReminder(int noteId, Reminder reminder) {
		String reminderSQL = "INSERT INTO reminders (name, date, note_id) VALUES (?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(reminderSQL);
			preparedStatement.setString(1, reminder.getName());
			preparedStatement.setString(2, reminder.getDate().toString());
			preparedStatement.setInt(3, noteId);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void editReminder(int reminderId, String newName, String newDate) {
		try {
			if (!newName.equals("")) {
				String editName = "UPDATE reminders SET name = ? WHERE id = ?";
				PreparedStatement editNameStatement = connection.prepareStatement(editName);
				editNameStatement.setString(1, newName);
				editNameStatement.setInt(2, reminderId);
				editNameStatement.execute();
			}
			if (!newDate.equals("")) {
				String editDate = "UPDATE reminders SET date = ? WHERE id = ?";

				PreparedStatement editDateStatement = connection.prepareStatement(editDate);
				editDateStatement.setString(1, newDate);
				editDateStatement.setInt(2, reminderId);
				editDateStatement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void editNote(int noteId, String newTitle, String newContent, String newDate) {
		try {
			if (!newTitle.equals("")) {
				String editTitle = "UPDATE notes SET title = ? WHERE id = ?";

				PreparedStatement editTitleStatement = connection.prepareStatement(editTitle);
				editTitleStatement.setString(1, newTitle);
				editTitleStatement.setInt(2, noteId);
				editTitleStatement.execute();
			}
			if (!newContent.equals("")) {
				String editContent = "UPDATE notes SET content = ? WHERE id = ?";
				PreparedStatement editContentStatement = connection.prepareStatement(editContent);
				editContentStatement.setString(1, newContent);
				editContentStatement.setInt(2, noteId);
				editContentStatement.execute();
			}
			if (!newDate.equals("")) {
				String editDate = "UPDATE notes SET date = ? WHERE id = ?";
				PreparedStatement editDateStatement = connection.prepareStatement(editDate);
				editDateStatement.setString(1, newDate);
				editDateStatement.setInt(2, noteId);
				editDateStatement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean removeNote(int id) {
		String sql = "DELETE FROM notes WHERE id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeReminder(int noteId, int reminderId) {
		try {
			String noteIdVerify = "SELECT note_id FROM reminders WHERE id = ?";
			PreparedStatement noteIdVerifyStatement = connection.prepareStatement(noteIdVerify);
			noteIdVerifyStatement.setInt(1, reminderId);
			noteIdVerifyStatement.execute();
			ResultSet rs = noteIdVerifyStatement.getResultSet();
			if (rs.next() && rs.getInt("note_id") == noteId) {
				String removeReminderSQL = "DELETE FROM reminders WHERE id = ?";
				PreparedStatement removeReminderStatement = connection.prepareStatement(removeReminderSQL);
				removeReminderStatement.setInt(1, reminderId);
				removeReminderStatement.execute();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean preparedStatementExistingById(int id, String sql) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.execute();
			if (preparedStatement.getResultSet().next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean userContainsAnyNotes(User user) {
		int loggedUserId = user.getId();
		String sql = "SELECT * FROM notes WHERE user_id = ?";
		return preparedStatementExistingById(loggedUserId, sql);
	}

	@Override
	public boolean noteContainsAnyReminders(int noteId) {
		String sql = "SELECT * FROM reminders WHERE note_id = ?";
		return preparedStatementExistingById(noteId, sql);
	}

	@Override
	public boolean noteWithThisIdExistAndBelongToUser(int noteId, User user) {
		String sql = "SELECT * FROM notes WHERE id = ? AND user_id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, noteId);
			preparedStatement.setInt(2, user.getId());
			preparedStatement.execute();
			return preparedStatement.getResultSet().next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean reminderWithThisIdExistAndBelongToNote(int reminderId, int noteId) {
		String sql = "SELECT * FROM reminders WHERE id = ? AND note_id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, reminderId);
			preparedStatement.setInt(2, noteId);
			preparedStatement.execute();
			return preparedStatement.getResultSet().next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
