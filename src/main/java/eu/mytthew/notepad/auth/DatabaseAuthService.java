package eu.mytthew.notepad.auth;

import com.google.common.hash.Hashing;
import eu.mytthew.notepad.entity.User;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseAuthService implements IAuthService {
	DatabaseOperations databaseOperations = new DatabaseOperations();
	Connection connection = null;
	@Getter
	private User loggedUser;


	@Override
	public boolean containsNickname(String nickname) {
		connection = databaseOperations.connectToDatabase();
		String sql = "SELECT login FROM users WHERE login = ?";
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
		connection = databaseOperations.connectToDatabase();
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
					if (preparedStatement.getResultSet().next()) {
						String getUserID = "SELECT id FROM users WHERE login = ?";
						PreparedStatement getUserIdStatement = connection.prepareStatement(getUserID);
						getUserIdStatement.setString(1, nickname);
						getUserIdStatement.execute();
						getUserIdStatement.getResultSet().next();
						int loggedUserId = getUserIdStatement.getResultSet().getInt("id");
						System.out.println("logged user id " + loggedUserId);
					}
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

	@Override
	public boolean logout() {
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
