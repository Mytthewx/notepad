package eu.mytthew.notepad;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Supplier;

@Value
@AllArgsConstructor
public class MenuItem {
	int id;
	String name;
	Supplier<Boolean> body;

	public MenuItem(int id, String name, Runnable body) {
		this(id, name, () -> {
			body.run();
			return false;
		});
	}
}
