package eu.mytthew.notepad;

import lombok.Value;

@Value
public class MenuItem {
	private int id;
	private String name;
	private Runnable body;
}
