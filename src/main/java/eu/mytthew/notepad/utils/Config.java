package eu.mytthew.notepad.utils;

import lombok.Getter;
import org.json.JSONObject;

public class Config implements JSONSerializable {
	private final FileOperation fileOperation;
	@Getter
	private int userId;
	@Getter
	private int noteId;
	@Getter
	private int reminderId;

	public Config(FileOperation fileOperation) {
		this.fileOperation = fileOperation;
	}

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
