package eu.mytthew.notepad.entity;

import eu.mytthew.notepad.utils.JSONSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Note implements JSONSerializable {
	private int id;
	private String title;
	private String content;
	private LocalDate noteDate;
	private int userId;

	public Note(String title, String content, LocalDate noteDate) {
		this(0, title, content, noteDate, 0);
		this.title = title;
		this.content = content;
		this.noteDate = noteDate;
	}

	@Override
	public void deserialize(JSONObject self) {
		id = self.getInt("id");
		title = self.getString("title");
		content = self.getString("content");
		noteDate = LocalDate.parse(self.getString("date"));
		userId = self.getInt("user_id");
	}

	@Override
	public JSONObject serialize() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("title", title);
		jsonObject.put("content", content);
		jsonObject.put("date", noteDate.toString());
		jsonObject.put("user_id", userId);
		return jsonObject;
	}
}
