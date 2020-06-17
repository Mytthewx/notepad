package eu.mytthew.notepad;

import lombok.Value;

import java.util.function.Supplier;

@Value
public class MenuItem {
	int id;
	String name;
	Supplier<Boolean> body;
}
