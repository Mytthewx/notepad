package eu.mytthew.notepad.auth;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public class FileOperation {
	String parent;

	public Stream<Path> filesStream(String path) {
		parent = "reminders";
		return Optional.of(path)
				.map(File::new)
				.map(File::listFiles)
				.stream()
				.flatMap(Arrays::stream)
				.map(File::toPath);
	}

	public JSONObject openFile(String filename) {
		if (!filename.contains(".json")) {
			filename = filename + ".json";
		}
		File temp = new File(parent, filename);
		try (FileInputStream fileInputStream = new FileInputStream(temp)) {
			return new JSONObject(new JSONTokener(fileInputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean createFile(String filename, JSONObject jsonObject) {
		File temp = new File(parent, filename.toLowerCase() + ".json");
		if (temp.exists()) {
			return false;
		}
		try (FileWriter fileWriter = new FileWriter(temp)) {
			fileWriter.write(jsonObject.toString(4));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void deleteFile(String filename) {
		try {
			Files.delete(Paths.get(parent, filename + ".json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean fileExist(String filename) {
		File file = new File(parent, filename + ".json");
		return file.exists();
	}
}
