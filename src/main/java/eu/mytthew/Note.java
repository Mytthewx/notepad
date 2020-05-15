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

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Notes for this user:\n" + "Title: '" + getTitle() + '\'' +
				"\nContent:\n" + getContent();
	}
}
