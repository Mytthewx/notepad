package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseOperations {
	@Getter
	Connection connection = null;

	public Connection connectToDatabase() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/notepad", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public void readNotes(Connection connection, User user, int loggedUserid) {
		try {
			String notesSQL = "SELECT * FROM notes WHERE user_id = ?";
			PreparedStatement notesStatement = connection.prepareStatement(notesSQL);
			notesStatement.setInt(1, loggedUserid);
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
}
