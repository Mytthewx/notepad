package eu.mytthew.notepad.auth;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class OpenFileClass {
	public JSONObject openFile(String filename) {
		File temp = new File("users", filename + ".json");
		try (FileInputStream fileInputStream = new FileInputStream(temp)) {
			JSONTokener tokener = new JSONTokener(fileInputStream);
			fileInputStream.close();
			return new JSONObject(tokener);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
