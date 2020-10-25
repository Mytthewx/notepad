package eu.mytthew.notepad.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class IdProvider {
	private int counter;
	
	public int next() {
		return counter++;
	}
}
