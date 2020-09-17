package eu.mytthew.notepad.auth;

import com.google.common.hash.Hashing;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAuthService implements IAuthService {
	Connection connection = null;
	private int loggedUserid;
	private List<Integer> listOfIndexes = new ArrayList<>();
	@Getter
	private User loggedUser;

	public void connectToDatabase() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/notepad", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void readNotes(User user) {
		String getUserID = "SELECT id FROM users WHERE login = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(getUserID);
			preparedStatement.setString(1, user.getNickname());
			preparedStatement.execute();
			while (preparedStatement.getResultSet().next()) {
				loggedUserid = preparedStatement.getResultSet().getInt("id");
				String notesSQL = "SELECT * FROM notes WHERE user_id = ?";
				PreparedStatement notesStatement = connection.prepareStatement(notesSQL);
				notesStatement.setInt(1, loggedUserid);
				notesStatement.execute();
				while (notesStatement.getResultSet().next()) {
					listOfIndexes.add(notesStatement.getResultSet().getInt("id"));
					String title = notesStatement.getResultSet().getString("title");
					String content = notesStatement.getResultSet().getString("content");
					String localDate = notesStatement.getResultSet().getString("date");
					user.addNote(new Note(title, content, LocalDate.parse(localDate)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean containsNickname(String nickname) {
		String sql = "SELECT login FROM users WHERE login = ?";
		connectToDatabase();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, nickname);
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
	public boolean login(String nickname, String password) {
		connectToDatabase();
		if (containsNickname(nickname)) {
			try {
				String userPasswordSQL = "SELECT password FROM users WHERE login = ?";
				PreparedStatement userPassword = connection.prepareStatement(userPasswordSQL);
				userPassword.setString(1, nickname);
				userPassword.execute();
				if (userPassword.getResultSet().next()) {
					if (!userPassword.getResultSet().getString("password").equals(hashPassword(password))) {
						return false;
					}
					String sql = "SELECT * FROM users WHERE login = ?";
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, nickname);
					preparedStatement.execute();
					loggedUser = new User(nickname, password);
					readNotes(loggedUser);
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		try {
			String userPasswordSQL = "SELECT password FROM users WHERE login = ?";
			PreparedStatement userPassword = connection.prepareStatement(userPasswordSQL);
			userPassword.setString(1, getLoggedUser().getNickname());
			userPassword.execute();
			if (userPassword.getResultSet().next()) {
				if (!userPassword.getResultSet().getString("password").equals(hashPassword(oldPassword))) {
					return false;
				}
				String sql = "UPDATE users SET password = ? WHERE login = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, hashPassword(newPassword));
				preparedStatement.setString(2, getLoggedUser().getNickname());
				preparedStatement.execute();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changeNickname(String newNickname) {
		if (!containsNickname(newNickname)) {
			try {
				String oldNickname = getLoggedUser().getNickname();
				String sql = "UPDATE users SET login = ? WHERE login = ?;";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, newNickname);
				preparedStatement.setString(2, oldNickname);
				preparedStatement.execute();
				if (containsNickname(newNickname) && !containsNickname(oldNickname) && !newNickname.equals(oldNickname)) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean addUser(String nickname, String userPassword) {
		if (containsNickname(nickname)) {
			return false;
		}
		try {
			String sql = "INSERT INTO users(login, password) VALUES (?, ?);";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, nickname);
			preparedStatement.setString(2, hashPassword(userPassword));
			preparedStatement.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

//	public void deleteAllNotesFromDatabase() {
//		String sql = "DELETE FROM notes";
//		try {
//			PreparedStatement preparedStatement = connection.prepareStatement(sql);
//			preparedStatement.execute();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void addAllNotesFromUserToDatabase(User user) {
//		String sql = "INSERT INTO notes (title, content, date, user_id) VALUES (?, ?, ?, ?)";
//		try {
//			PreparedStatement addNotesStatement = connection.prepareStatement(sql);
//			for (int i = 0; i < user.getNotes().size(); i++) {
//				addNotesStatement.setString(1, user.getNotes().get(i).getTitle());
//				addNotesStatement.setString(2, user.getNotes().get(i).getContent());
//				addNotesStatement.setString(3, user.getNotes().get(i).getNoteDate().toString());
//				addNotesStatement.setString(4, String.valueOf(loggedUserid));
//				addNotesStatement.execute();
//			}
//		} catch (SQLException throwables) {
//			throwables.printStackTrace();
//		}
//	}

	public void deleteOldNotesAndAddAllNewNotes(User user) {
		String notesSQL = "INSERT INTO notes (title, content, date, user_id) VALUES (?, ?, ?, ?);";
		try {
			PreparedStatement addNotesStatement = connection.prepareStatement(notesSQL);
			String sql = "START TRANSACTION;" +
					"DELETE FROM notes;" + notesSQL +
					"COMMIT;";
			PreparedStatement finish = connection.prepareStatement(sql);
			for (int i = 0; i < user.getNotes().size(); i++) {
				addNotesStatement.setString(1, user.getNotes().get(i).getTitle());
				addNotesStatement.setString(2, user.getNotes().get(i).getContent());
				addNotesStatement.setString(3, user.getNotes().get(i).getNoteDate().toString());
				addNotesStatement.setString(4, String.valueOf(loggedUserid));
				addNotesStatement.execute();
			}
			finish.execute();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	@Override
	public boolean logout() {
		deleteOldNotesAndAddAllNewNotes(getLoggedUser());
		loggedUser = null;
		try {
			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return true;
	}

	private String hashPassword(String password) {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
