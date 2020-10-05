package eu.mytthew.notepad.entity;

import eu.mytthew.notepad.auth.JSONSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

@Data
@AllArgsConstructor
public class User implements JSONSerializable {
	private int id;
	private String nickname;
	private String password;

	public User(String nickname, String password) {
		this(0, nickname, password);
	}

	@Override
	public void deserializable(JSONObject self) {
		id = self.getInt("id");
		nickname = self.getString("nick");
		password = self.getString("pass");
	}

	@Override
	public JSONObject serialize() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("nick", nickname);
		jsonObject.put("pass", password);
		return jsonObject;
	}
}
