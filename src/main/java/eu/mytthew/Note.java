package eu.mytthew;

public class Note {
	private final String title;
	private final String content;

	public Note(String title, String content) {
		this.title = title;
		this.content = content;
	}

	@Override
	public String toString() {
		return "Title: " + title +
				"\nContent: '" + content + '\'' + "\n";
	}
}
