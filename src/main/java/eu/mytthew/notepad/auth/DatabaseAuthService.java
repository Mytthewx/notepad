package eu.mytthew.notepad.auth;

import com.google.common.hash.Hashing;
import eu.mytthew.notepad.entity.User;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAuthService implements IAuthService {
	Connection connection = null;
	@Getter
	private User loggedUser;
	private String oldNickname;

	public Connection connectToDatabase() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/notepad", "root", "");
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean containsNickname(String nickname) {
		String sql = "SELECT login FROM users WHERE login = '" + nickname + "'";
		connectToDatabase();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();
			if (preparedStatement.getResultSet().next()) {
				return true;
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean login(String nickname, String password) {
		connectToDatabase();
		if (containsNickname(nickname)) {
			try {
				String userPasswordSQL = "SELECT password FROM users WHERE login = '" + nickname + "'";
				PreparedStatement userPassword = connection.prepareStatement(userPasswordSQL);
				userPassword.execute();
				if (userPassword.getResultSet().next()) {
					if (!userPassword.getResultSet().getString("password").equals(hashPassword(password))) {
						return false;
					}
					String sql = "SELECT * FROM users WHERE login = '" + nickname + "'";
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.execute();
					loggedUser = new User(nickname, password);
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
		return false;
	}

	@Override
	public boolean changeNickname(String newNickname) {
		if (!containsNickname(newNickname)) {
			try {
				String sql = "UPDATE users SET login = '" + newNickname + "'" + " WHERE login = '" + oldNickname + "'";
				connectToDatabase();
				Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if (rs.next()) {
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
