package eu.mytthew.notepad.utils;

import org.json.JSONObject;

public interface JSONSerializable {
	void deserialize(JSONObject self);

	JSONObject serialize();
}
