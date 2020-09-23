package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class NoteService {
	public void readNotes(Connection connection, User user, int loggedUserId) {
		try {
			String notesSQL = "SELECT * FROM notes WHERE user_id = ?";
			PreparedStatement notesStatement = connection.prepareStatement(notesSQL);
			notesStatement.setInt(1, loggedUserId);
			notesStatement.execute();
			while (notesStatement.getResultSet().next()) {
				String title = notesStatement.getResultSet().getString("title");
				String content = notesStatement.getResultSet().getString("content");
				String localDate = notesStatement.getResultSet().getString("date");
				user.addNote(new Note(title, content, LocalDate.parse(localDate)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addNoteToDatabase(Connection connection, User user, Note note) {
		String noteToSQL = "INSERT INTO notes(title, content, date, user_id) VALUES (?, ?, ?, ?)";
		try {
			String getUserSQL = "SELECT id FROM users WHERE login = ?";
			PreparedStatement getUserIdStatement = connection.prepareStatement(getUserSQL);
			getUserIdStatement.setString(1, user.getNickname());
			getUserIdStatement.execute();
			getUserIdStatement.getResultSet().next();
			int loggedUserId = getUserIdStatement.getResultSet().getInt("id");
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
}
