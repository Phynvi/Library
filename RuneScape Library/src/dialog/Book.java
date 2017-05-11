package dialog;

public class Book {

    private Page[] pages;
    private int pageIndex;

    public Book(Page... pages) {
	this.pages = pages;
    }

    public void flipPage(int page) {
	pageIndex = page;
    }

    public void nextPage() {
	flipPage(pageIndex++);
    }

}
