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

public class NoteService {
	public List<Note> getAllNotes(Connection connection, User user) {
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
				Note note = new Note(id, title, content, LocalDate.parse(localDate));
				noteList.add(note);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noteList;
	}

	public List<Reminder> getAllReminders(Connection connection, int noteId) {
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
				Reminder reminder = new Reminder(id, name, LocalDate.parse(localDate));
				reminderList.add(reminder);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reminderList;
	}

	public void addNote(Connection connection, User user, Note note) {
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

	public void addReminder(Connection connection, int noteId, Reminder reminder) {
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

	public void editReminder(Connection connection, int reminderId, String newName, String newDate) {
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

	public void editNote(Connection conection, int noteId, String newTitle, String newContent, String newDate) {
		try {
			if (!newTitle.equals("")) {
				String editTitle = "UPDATE notes SET title = ? WHERE id = ?";

				PreparedStatement editTitleStatement = conection.prepareStatement(editTitle);
				editTitleStatement.setString(1, newTitle);
				editTitleStatement.setInt(2, noteId);
				editTitleStatement.execute();
			}
			if (!newContent.equals("")) {
				String editContent = "UPDATE notes SET content = ? WHERE id = ?";
				PreparedStatement editContentStatement = conection.prepareStatement(editContent);
				editContentStatement.setString(1, newContent);
				editContentStatement.setInt(2, noteId);
				editContentStatement.execute();
			}
			if (!newDate.equals("")) {
				String editDate = "UPDATE notes SET date = ? WHERE id = ?";
				PreparedStatement editDateStatement = conection.prepareStatement(editDate);
				editDateStatement.setString(1, newDate);
				editDateStatement.setInt(2, noteId);
				editDateStatement.execute();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean removeNote(Connection connection, int id) {
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

	public boolean removeReminder(Connection connection, int noteId, int reminderId) {
		try {
			String noteIdVerify = "SELECT note_id FROM reminders WHERE id = ?";
			PreparedStatement noteIdVerifyStatement = connection.prepareStatement(noteIdVerify);
			noteIdVerifyStatement.setInt(1, noteId);
			noteIdVerifyStatement.execute();
			ResultSet rs = noteIdVerifyStatement.getResultSet();
			if (rs.next()) {
				if (rs.getInt("note_id") == noteId) {
					String removeReminderSQL = "DELETE FROM reminders WHERE id = ?";
					PreparedStatement removeReminderStatement = connection.prepareStatement(removeReminderSQL);
					removeReminderStatement.setInt(1, reminderId);
					removeReminderStatement.execute();
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean preparedStatementExistingById(Connection connection, int id, String sql) {
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

	public boolean userContainsAnyNotes(Connection connection, User user) {
		int loggedUserId = user.getId();
		String sql = "SELECT * FROM notes WHERE user_id = ?";
		return preparedStatementExistingById(connection, loggedUserId, sql);
	}

	public boolean noteContainsAnyReminders(Connection connection, int noteId) {
		String sql = "SELECT * FROM reminders WHERE note_id = ?";
		return preparedStatementExistingById(connection, noteId, sql);
	}

	public boolean noteWithThisIdExist(Connection connection, int id) {
		String sql = "SELECT * FROM notes WHERE id = ?";
		return preparedStatementExistingById(connection, id, sql);
	}

	public boolean reminderWithThisIdExist(Connection connection, int id) {
		String sql = "SELECT * FROM reminders WHERE id = ?";
		return preparedStatementExistingById(connection, id, sql);
	}
}
