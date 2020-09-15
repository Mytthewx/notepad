package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAuthService implements IAuthService {
	String url = "jdbc:mysql://localhost:3306/notepad";
	String user = "root";
	String dbpassword = "";

	@Override
	public boolean containsNickname(String nickname) {
		try {
			Connection con = DriverManager.getConnection(url, user, dbpassword);
			String sql = "SELECT login FROM users WHERE login = '" + nickname + "'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				con.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean login(String nickname, String password) {
		try {
			Connection con = DriverManager.getConnection(url, user, dbpassword);
			String userPassword = "SELECT password FROM users WHERE login = '" + nickname + "'";
			String sql = "SELECT * FROM users WHERE login = '" + nickname + "'";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.execute();
			con.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		return false;
	}

	@Override
	public boolean changeNickname(String newNickname) {
		return false;
	}

	@Override
	public boolean addUser(String nickname, String userPassword) {
		try {
			Connection con = DriverManager.getConnection(url, user, dbpassword);
			String sql =
					"INSERT INTO users(login, password) VALUES (?, ?);";
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, nickname);
			preparedStatement.setString(2, userPassword);
			preparedStatement.execute();
			con.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public User getLoggedUser() {
		return null;
	}
}
