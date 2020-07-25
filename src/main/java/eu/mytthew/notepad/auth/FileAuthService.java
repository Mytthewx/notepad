package eu.mytthew.notepad.auth;

import eu.mytthew.notepad.entity.User;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class FileAuthService implements IAuthService {
	@Getter
	private User loggedUser;

	@Override
	public boolean containsNickname(String nickname) {
		File temp = new File("users/" + nickname + ".json");
		return temp.exists();
	}

	@Override
	public boolean login(String nickname, String password) {
		File temp = new File("users/" + nickname + ".json");
		JSONTokener tokener = null;
		try {
			tokener = new JSONTokener(new FileInputStream(temp));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject(tokener);
		String p = obj.getString("pass");
		if (p.equals(password)) {
			loggedUser = new User(nickname, password);
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
	public boolean logout() {
		if (loggedUser == null) {
			return false;
		}
		File temp = new File("users/" + loggedUser.getNickname() + ".json");
		JSONObject jsonObject = new JSONObject(temp);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < loggedUser.getNotes().size(); i++) {
			JSONObject note = new JSONObject();
			note.put("Title", loggedUser.getNotes().get(i).getTitle());
			note.put("Content", loggedUser.getNotes().get(i).getContent());
			note.put("Date", loggedUser.getNotes().get(i).getNoteDate());
			jsonArray.put(note);
		}
		jsonObject.put("notes", jsonArray);
		try (FileWriter fileWriter = new FileWriter(temp)) {
			fileWriter.append(jsonObject.toString(5));
			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}
}
