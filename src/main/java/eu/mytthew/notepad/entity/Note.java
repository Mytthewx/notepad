package eu.mytthew.notepad.entity;

import eu.mytthew.notepad.Reminder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Note {
	private final UUID uuid = UUID.randomUUID();
	private final List<Reminder> reminders = new ArrayList<>();
	private String title;
	private String content;
	private LocalDate noteDate;
}
