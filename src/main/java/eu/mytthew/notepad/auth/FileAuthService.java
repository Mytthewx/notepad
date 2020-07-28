package eu.mytthew.notepad.auth;

import com.google.common.hash.Hashing;
import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class FileAuthService implements IAuthService {
	@Getter
	private User loggedUser;

	@Override
	public boolean containsNickname(String nickname) {
		File temp = new File("users", nickname + ".json");
		return temp.exists();
	}

	@Override
	public boolean login(String nickname, String password) {
		File temp = new File("users", nickname + ".json");
		JSONTokener tokener = null;
		try {
			tokener = new JSONTokener(new FileInputStream(temp));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject(tokener);
		String p = obj.getString("pass");
		if (p.equals(hashPassword(password))) {
			loggedUser = new User(nickname, hashPassword(password));
			if (obj.has("notes")) {
				JSONArray arrayNotes = obj.getJSONArray("notes");
				for (int i = 0; i < arrayNotes.length(); i++) {
					JSONObject innerNoteObject = arrayNotes.getJSONObject(i);
					String noteTitle = innerNoteObject.getString("title");
					String noteContent = innerNoteObject.getString("content");
					LocalDate noteDate = LocalDate.parse(innerNoteObject.getString("date"));
					Note note = new Note(noteTitle, noteContent, noteDate);
					JSONArray arrayReminders = innerNoteObject.getJSONArray("reminders");
					for (int j = 0; j < arrayReminders.length(); j++) {
						JSONObject innerReminderObject = arrayReminders.getJSONObject(j);
						String reminderName = innerReminderObject.getString("name");
						LocalDate reminderDate = LocalDate.parse(innerReminderObject.getString("date"));
						note.getReminders().add(new Reminder(reminderName, reminderDate));
					}
					getLoggedUser().addNote(note);
				}
			}
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
		File temp = new File("users", nickname + ".json");
		if (temp.exists()) {
			return false;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nick", nickname);
		jsonObject.put("pass", hashPassword(password));
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
		File temp = new File("users", loggedUser.getNickname() + ".json");
		if (temp.exists()) {
			temp.delete();
		}
		JSONObject generalJSON = new JSONObject();
		generalJSON.put("nick", getLoggedUser().getNickname());
		generalJSON.put("pass", getLoggedUser().getPassword());
		JSONArray array = new JSONArray();
		for (int i = 0; i < getLoggedUser().getNotes().size(); i++) {
			JSONObject innerObject = new JSONObject();
			innerObject.put("title", getLoggedUser().getNotes().get(i).getTitle());
			innerObject.put("content", getLoggedUser().getNotes().get(i).getContent());
			innerObject.put("date", getLoggedUser().getNotes().get(i).getNoteDate());
			JSONArray innerArray = new JSONArray();
			for (int j = 0; j < getLoggedUser().getNotes().get(i).getReminders().size(); j++) {
				JSONObject secInnerObject = new JSONObject();
				secInnerObject.put("name", getLoggedUser().getNotes().get(i).getReminders().get(j).getName());
				secInnerObject.put("date", getLoggedUser().getNotes().get(i).getReminders().get(j).getDate());
				innerArray.put(secInnerObject);
			}
			innerObject.put("reminders", innerArray);
			array.put(innerObject);
		}
		generalJSON.put("notes", array);
		try (FileWriter fileWriter = new FileWriter(temp)) {
			fileWriter.write(generalJSON.toString(4));
			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	private String hashPassword(String password) {
		return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
	}
}
