package eu.mytthew;


public class Note {
	private static int globalID = 0;
	private final String title;
	private final String content;
	private final int ID = globalID++;

	public Note(String title, String content) {
		this.title = title;
		this.content = content;
	}

	@Override
	public String toString() {
		return "ID: " + ID +
				"\nTitle: " + title +
				"\nContent: '" + content + '\'' + "\n";
	}
}
