package eu.mytthew.notepad.utils;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigTest {
	@Test
	void addUserId() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		Config config = new Config(fileOperation);
		when(fileOperation.fileExist(any())).thenReturn(false);

		// when
		config.addUserId(0);

		// then
		verify(fileOperation, times(0)).deleteFile(any());
		verify(fileOperation, times(1)).createFile(any(), any());
	}

	@Test
	void addNoteId() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		Config config = new Config(fileOperation);
		when(fileOperation.fileExist(any())).thenReturn(false);

		// when
		config.addNoteId(0);

		// then
		verify(fileOperation, times(0)).deleteFile(any());
		verify(fileOperation, times(1)).createFile(any(), any());
	}

	@Test
	void addReminderId() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		Config config = new Config(fileOperation);
		when(fileOperation.fileExist(any())).thenReturn(false);

		// when
		config.addReminderId(0);

		// then
		verify(fileOperation, times(0)).deleteFile(any());
		verify(fileOperation, times(1)).createFile(any(), any());
	}

	@Test
	void saveConfigTrueTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		Config config = new Config(fileOperation);
		when(fileOperation.fileExist(any())).thenReturn(false);

		// when
		config.saveConfig();

		// then
		verify(fileOperation, times(0)).deleteFile(any());
		verify(fileOperation, times(1)).createFile(any(), any());
	}

	@Test
	void saveConfigFalseTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		Config config = new Config(fileOperation);
		when(fileOperation.fileExist(any())).thenReturn(true);

		// when
		config.saveConfig();

		// then
		verify(fileOperation, times(1)).deleteFile(any());
		verify(fileOperation, times(1)).createFile(any(), any());
	}

	@Test
	void deserializeTest() {
		// given
		FileOperation fileOperation = mock(FileOperation.class);
		Config config = new Config(fileOperation);
		JSONObject jsonObject = mock(JSONObject.class);
		when(jsonObject.getInt(eq("user_id"))).thenReturn(3);
		when(jsonObject.getInt(eq("note_id"))).thenReturn(1);
		when(jsonObject.getInt(eq("reminder_id"))).thenReturn(2);

		// when
		config.deserialize(jsonObject);

		// then
		assertEquals(3, config.getUserId());
		assertEquals(1, config.getNoteId());
		assertEquals(2, config.getReminderId());
	}
}
