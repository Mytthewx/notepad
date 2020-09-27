package eu.mytthew.notepad.auth;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class IdProvider {
	private int counter;

	public IdProvider() {
		this(0);
	}

	public int next() {
		return counter++;
	}
}
