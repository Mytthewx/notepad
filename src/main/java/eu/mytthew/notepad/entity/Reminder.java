package eu.mytthew.notepad.entity;

import eu.mytthew.notepad.utils.JSONSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Reminder implements JSONSerializable {
	private int id;
	private String name;
	private LocalDate date;
	private int noteId;

	public Reminder(String name, LocalDate date) {
		this(0, name, date, 0);
	}

	@Override
	public void deserialize(JSONObject self) {
		id = self.getInt("id");
		name = self.getString("name");
		date = LocalDate.parse(self.getString("date"));
		noteId = self.getInt("note_id");
	}

	@Override
	public JSONObject serialize() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		jsonObject.put("date", date.toString());
		jsonObject.put("note_id", noteId);
		return jsonObject;
	}
}
