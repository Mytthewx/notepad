package eu.mytthew.notepad.auth;

import org.json.JSONObject;

public interface JSONSerializable {
	void deserializable(JSONObject self);

	JSONObject serialize();
}
