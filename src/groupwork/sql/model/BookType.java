package groupwork.sql.model;

public class BookType {
    private String bookTypeID;
    private String bookTypeName;
    static String selectID;

    public String getBookTypeID() {
        return bookTypeID;
    }

    public String getBookTypeName() {
        return bookTypeName;
    }

    public void setBookTypeID(String bookTypeID) {
        this.bookTypeID = bookTypeID;
    }

    public void setBookTypeName(String bookTypeName) {
        this.bookTypeName = bookTypeName;
    }

    public static String getSelectID() {
        return selectID;
    }

    public static void setSelectID(String selectID) {
        BookType.selectID = selectID;
    }
}
