package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NoteService {
	private int getUserId(Connection connection, User user) {
		String getUserSQL = "SELECT id FROM users WHERE login = ?";
		try {
			PreparedStatement getUserIdStatement = connection.prepareStatement(getUserSQL);
			getUserIdStatement.setString(1, user.getNickname());
			getUserIdStatement.execute();
			getUserIdStatement.getResultSet().next();
			return getUserIdStatement.getResultSet().getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public List<Note> readNotes(Connection connection, User user) {
		List<Note> noteList = new ArrayList<>();
		try {
			int loggedUserId = getUserId(connection, user);
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

	public void addNoteToDatabase(Connection connection, User user, Note note) {
		String noteToSQL = "INSERT INTO notes(title, content, date, user_id) VALUES (?, ?, ?, ?)";
		try {
			int loggedUserId = getUserId(connection, user);
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

	public void addReminderToDatabase(Connection connection, int noteId, Reminder reminder) {
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

	public void editNoteInDatabase(Connection conection, int noteId, String newTitle, String newContent, String newDate) {
		if (!newTitle.equals("")) {
			String editTitle = "UPDATE notes SET title = ? WHERE id = ?";
			try {
				PreparedStatement preparedStatement = conection.prepareStatement(editTitle);
				preparedStatement.setString(1, newTitle);
				preparedStatement.setInt(2, noteId);
				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (!newContent.equals("")) {
			String editContent = "UPDATE notes SET content = ? WHERE id = ?";
			try {
				PreparedStatement preparedStatement = conection.prepareStatement(editContent);
				preparedStatement.setString(1, newContent);
				preparedStatement.setInt(2, noteId);
				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (!newDate.equals("")) {
			String editDate = "UPDATE notes SET date = ? WHERE id = ?";
			try {
				PreparedStatement preparedStatement = conection.prepareStatement(editDate);
				preparedStatement.setString(1, newDate);
				preparedStatement.setInt(2, noteId);
				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

	private boolean preparedStatementExistingById(Connection connection, int loggedUserId, String sql) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, loggedUserId);
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
		int loggedUserId = getUserId(connection, user);
		String sql = "SELECT * FROM notes WHERE user_id = ?";
		return preparedStatementExistingById(connection, loggedUserId, sql);
	}

	public boolean noteWithThisIdExist(Connection connection, int id) {
		String sql = "SELECT * FROM notes WHERE id = ?";
		return preparedStatementExistingById(connection, id, sql);
	}
}
