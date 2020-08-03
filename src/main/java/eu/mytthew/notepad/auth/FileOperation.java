package eu.mytthew.notepad.auth;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileOperation {
	public JSONObject openFile(String filename) {
		File temp = new File("users", filename + ".json");
		try (FileInputStream fileInputStream = new FileInputStream(temp)) {
			JSONTokener tokener = new JSONTokener(fileInputStream);
			return new JSONObject(tokener);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean createFile(String filename, JSONObject jsonObject) {
		File temp = new File("users", filename.toLowerCase() + ".json");
		if (temp.exists()) {
			return false;
		}
		try (FileWriter fileWriter = new FileWriter(temp)) {
			fileWriter.write(jsonObject.toString(4));
			return true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public void deleteFile(String filename) {
		try {
			Files.delete(Paths.get("users", filename + ".json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
