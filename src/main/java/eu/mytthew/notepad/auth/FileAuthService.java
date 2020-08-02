package eu.mytthew.notepad.auth;

import com.google.common.hash.Hashing;
import eu.mytthew.notepad.Reminder;
import eu.mytthew.notepad.entity.Note;
import eu.mytthew.notepad.entity.User;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

public class FileAuthService implements IAuthService {
	@Getter
	private User loggedUser;
	private String oldNickname;
	OpenFileClass openFile;

	@Override
	public boolean containsNickname(String nickname) {
		File temp = new File("users", nickname.toLowerCase() + ".json");
		return temp.exists();
	}

	public FileAuthService(OpenFileClass openFile) {
		this.openFile = openFile;
	}

	@Override
	public boolean login(String nickname, String password) {
		JSONObject obj = openFile.openFile(nickname);
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
			oldNickname = getLoggedUser().getNickname();
			return true;
		}
		return false;
	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword) {
		if (loggedUser.getPassword().equals(hashPassword(oldPassword))) {
			loggedUser.setPassword(hashPassword(newPassword));
			return true;
		}
		return false;
	}

	@Override
	public boolean changeNickname(String newNickname) {
		if (containsNickname(newNickname)) {
			return false;
		}
		getLoggedUser().setNickname(newNickname);
		return true;
	}

	@Override
	public boolean addUser(String nickname, String password) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nick", nickname);
		jsonObject.put("pass", hashPassword(password));
		return openFile.createFile(nickname);
	}

	@Override
	public boolean logout() {
		if (loggedUser == null) {
			return false;
		}
		File temp = new File("users", loggedUser.getNickname().toLowerCase() + ".json");
		try {
			Files.delete(Paths.get("users", oldNickname.toLowerCase() + ".json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject generalJSON = new JSONObject();
		generalJSON.put("nick", getLoggedUser().getNickname());
		generalJSON.put("pass", getLoggedUser().getPassword());
		JSONArray array = new JSONArray();
		for (int i = 0; i < getLoggedUser().getNotes().size(); i++) {
			JSONObject innerObject = new JSONObject();
			innerObject.put("title", getLoggedUser()
					.getNotes()
					.get(i)
					.getTitle());
			innerObject.put("content", getLoggedUser()
					.getNotes()
					.get(i)
					.getContent());
			innerObject.put("date", getLoggedUser()
					.getNotes()
					.get(i)
					.getNoteDate());
			JSONArray innerArray = new JSONArray();
			for (int j = 0; j < getLoggedUser()
					.getNotes()
					.get(i)
					.getReminders()
					.size(); j++) {
				JSONObject secInnerObject = new JSONObject();
				secInnerObject.put("name", getLoggedUser()
						.getNotes()
						.get(i)
						.getReminders()
						.get(j)
						.getName());
				secInnerObject.put("date", getLoggedUser()
						.getNotes()
						.get(i)
						.getReminders()
						.get(j)
						.getDate());
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
