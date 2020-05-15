package eu.mytthew;

public class Note {
	private String title;
	private String content;

	public Note(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "Notes for this user:\n" + "Title: '" + getTitle() + '\'' +
				"\nContent:\n" + getContent();
	}
}
