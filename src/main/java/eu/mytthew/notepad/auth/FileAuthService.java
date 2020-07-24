package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

public class FileAuthService implements IAuthService {
	//@Getter
	private User loggedUser;

	@Override
	public boolean containsNickname(String nickname) {
		File temp = new File("users/" + nickname + ".json");
		return temp.exists();
	}

	@Override
	public boolean login(String nickname, String password) {
		File temp = new File("users/" + nickname + ".json");
		JSONObject jsonObject = new JSONObject(temp);
		if (jsonObject.getJSONObject(nickname).getString(password).equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		return false;
	}

	@Override
	public boolean addUser(String nickname, String password) {
		File temp = new File("users/" + nickname + ".json");
		if (temp.exists()) {
			return false;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nick", nickname);
		jsonObject.put("pass", password);
		try (FileWriter fileWriter = new FileWriter(temp)) {
			fileWriter.write(jsonObject.toString(4));
			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public User getLoggedUser() {
		return null;
	}
}
