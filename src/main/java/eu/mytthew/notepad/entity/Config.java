package eu.mytthew.notepad.entity;

import eu.mytthew.notepad.auth.FileOperation;
import eu.mytthew.notepad.auth.JSONSerializable;
import org.json.JSONObject;


public class Config implements JSONSerializable {
	FileOperation fileOperation = new FileOperation("config");
	int userId;
	int noteId;
	int reminderId;

	public void addUserId(int id) {
		userId = id;
		saveConfig();
	}

	public void addNoteId(int id) {
		noteId = id;
		saveConfig();
	}

	public void addReminderId(int id) {
		reminderId = id;
		saveConfig();
	}

	public int getUserId() {
		return userId;
	}

	public int getNoteId() {
		return noteId;
	}

	public int getReminderId() {
		return reminderId;
	}

	public void saveConfig() {
		if (fileOperation.fileExist("config")) {
			fileOperation.deleteFile("config");
		}
		fileOperation.createFile("config", serialize());
	}

	@Override
	public void deserialize(JSONObject self) {
		userId = self.getInt("user_id");
		noteId = self.getInt("note_id");
		reminderId = self.getInt("reminder_id");
	}

	@Override
	public JSONObject serialize() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_id", userId);
		jsonObject.put("note_id", noteId);
		jsonObject.put("reminder_id", reminderId);
		return jsonObject;
	}
}
