package eu.mytthew.notepad.auth;

import org.json.JSONObject;

public interface JSONSerializable {
	void deserialize(JSONObject self);

	JSONObject serialize();
}
