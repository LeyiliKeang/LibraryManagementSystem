package groupwork.sql.model;

public class Reader {
    private String readerID;
    private String readerName;
    private String readerSex;
    private String readerClass;
    private String readerPhone;
    private int notReturn;
    private int allBorrowed;
    private int count;
    static String selectID;

    public Reader() {
    }

    public String getReaderID() {
        return readerID;
    }

    public String getReaderName() {
        return readerName;
    }

    public String getReaderSex() {
        return readerSex;
    }

    public String getReaderClass() {
        return readerClass;
    }

    public String getReaderPhone() {
        return readerPhone;
    }

    public int getNotReturn() {
        return notReturn;
    }

    public int getAllBorrowed() {
        return allBorrowed;
    }

    public int getCount() {
        return count;
    }

    public void setReaderID(String readerID) {
        this.readerID = readerID;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public void setReaderSex(String readSex) {
        this.readerSex = readSex;
    }

    public void setReaderClass(String readerClass) {
        this.readerClass = readerClass;
    }

    public void setReaderPhone(String readerPhone) {
        this.readerPhone = readerPhone;
    }

    public void setNotReturn(int notReturn) {
        this.notReturn = notReturn;
    }

    public void setAllBorrowed(int allBorrowed) {
        this.allBorrowed = allBorrowed;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static String getSelectID() {
        return selectID;
    }

    public static void setSelectID(String selectID) {
        Reader.selectID = selectID;
    }
}
