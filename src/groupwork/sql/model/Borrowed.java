package groupwork.sql.model;

public class Borrowed {
    private String readerID;
    private String readerName;
    private String bookID;
    private String bookName;
    private String borrowDate;
    private String shouldReturnDate;
    private String returnDate;
    static String selectID;

    public Borrowed() {
    }

    public String getReaderID() {
        return readerID;
    }

    public String getReaderName() {
        return readerName;
    }

    public String getBookID() {
        return bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getShouldReturnDate() {
        return shouldReturnDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReaderID(String readerID) {
        this.readerID = readerID;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setShouldReturnDate(String shouldReturnDate) {
        this.shouldReturnDate = shouldReturnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public static String getSelectID() {
        return selectID;
    }

    public static void setSelectID(String selectID) {
        Borrowed.selectID = selectID;
    }
}
