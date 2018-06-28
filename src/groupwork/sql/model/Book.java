package groupwork.sql.model;

public class Book {
    private String bookID;
    private String bookName;
    private String author;
    private String press;
    private String bookType;
    private int page;
    private int price;
    private int existing;
    private int inventory;
    private String storageDate;
    private int count;
    static String selectID;

    public Book() {
    }

    public String getBookID() {
        return bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getPress() {
        return press;
    }

    public String getBookType() {
        return bookType;
    }

    public int getPage() {
        return page;
    }

    public int getPrice() {
        return price;
    }

    public int getExisting() {
        return existing;
    }

    public int getInventory() {
        return inventory;
    }

    public String getStorageDate() {
        return storageDate;
    }

    public int getCount() {
        return count;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setExisting(int existing) {
        this.existing = existing;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static String getSelectID() {
        return selectID;
    }

    public static void setSelectID(String selectID) {
        Book.selectID = selectID;
    }
}
