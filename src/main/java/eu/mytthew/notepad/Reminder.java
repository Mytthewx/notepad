package eu.mytthew.notepad;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Reminder {
	private String name;
	private LocalDate date;
}
