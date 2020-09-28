//package eu.mytthew.notepad.auth;
//
//import eu.mytthew.notepad.entity.User;
//import lombok.Getter;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import static eu.mytthew.notepad.auth.HashPassword.hashPassword;
//
//
//public class DatabaseAuthService implements IAuthService {
//	private final Connection connection;
//	@Getter
//	private User loggedUser;
//
//
//	public DatabaseAuthService(Connection connection) {
//		this.connection = connection;
//	}
//
//	@Override
//	public boolean containsNickname(String nickname) {
//		String sql = "SELECT login FROM users WHERE login = ?";
//		try {
//			PreparedStatement preparedStatement = connection.prepareStatement(sql);
//			preparedStatement.setString(1, nickname);
//			preparedStatement.execute();
//			if (preparedStatement.getResultSet().next()) {
//				return true;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@Override
//	public boolean login(String nickname, String password) {
//		if (containsNickname(nickname)) {
//			try {
//				String userPasswordSQL = "SELECT password FROM users WHERE login = ?";
//				PreparedStatement userPassword = connection.prepareStatement(userPasswordSQL);
//				userPassword.setString(1, nickname);
//				userPassword.execute();
//				if (userPassword.getResultSet().next()) {
//					if (!userPassword.getResultSet().getString("password").equals(hashPassword(password))) {
//						return false;
//					}
//					String sql = "SELECT * FROM users WHERE login = ?";
//					PreparedStatement preparedStatement = connection.prepareStatement(sql);
//					preparedStatement.setString(1, nickname);
//					preparedStatement.execute();
//					if (preparedStatement.getResultSet().next()) {
//						int userId = preparedStatement.getResultSet().getInt("id");
//						loggedUser = new User(userId, nickname, password);
//					}
//					return true;
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public boolean changePassword(String oldPassword, String newPassword) {
//		try {
//			String userPasswordSQL = "SELECT password FROM users WHERE login = ?";
//			PreparedStatement userPassword = connection.prepareStatement(userPasswordSQL);
//			userPassword.setString(1, getLoggedUser().getNickname());
//			userPassword.execute();
//			if (userPassword.getResultSet().next()) {
//				if (userPassword.getResultSet().getString("password").equals(hashPassword(oldPassword))) {
//					String sql = "UPDATE users SET password = ? WHERE login = ?";
//					PreparedStatement preparedStatement = connection.prepareStatement(sql);
//					preparedStatement.setString(1, hashPassword(newPassword));
//					preparedStatement.setString(2, getLoggedUser().getNickname());
//					preparedStatement.execute();
//					return true;
//				} else {
//					return false;
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@Override
//	public boolean changeNickname(String newNickname) {
//		if (containsNickname(newNickname)) {
//			return false;
//		}
//		try {
//			String oldNickname = getLoggedUser().getNickname();
//			String sql = "UPDATE users SET login = ? WHERE login = ?;";
//			PreparedStatement preparedStatement = connection.prepareStatement(sql);
//			preparedStatement.setString(1, newNickname);
//			preparedStatement.setString(2, oldNickname);
//			preparedStatement.execute();
//			if (containsNickname(newNickname) && !containsNickname(oldNickname) && !newNickname.equals(oldNickname)) {
//				getLoggedUser().setNickname(newNickname);
//				return true;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return true;
//	}
//
//	@Override
//	public boolean addUser(String nickname, String userPassword) {
//		if (containsNickname(nickname)) {
//			return false;
//		}
//		try {
//			String sql = "INSERT INTO users(login, password) VALUES (?, ?);";
//			PreparedStatement preparedStatement = connection.prepareStatement(sql);
//			preparedStatement.setString(1, nickname);
//			preparedStatement.setString(2, hashPassword(userPassword));
//			preparedStatement.execute();
//			return true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@Override
//	public boolean logout() {
//		loggedUser = null;
//		return true;
//	}
//}
