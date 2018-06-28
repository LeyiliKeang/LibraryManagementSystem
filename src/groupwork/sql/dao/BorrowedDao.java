package groupwork.sql.dao;

import groupwork.sql.db.DbTools;
import groupwork.sql.model.Book;
import groupwork.sql.model.Borrowed;
import groupwork.sql.model.Manager;
import groupwork.sql.model.Reader;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowedDao {
    public BorrowedDao() {
    }

//    public List selectAll() throws ClassNotFoundException, SQLException {
//        String sql = "SELECT * FROM BorrowedInfo";
//        DbTools dbTools = new DbTools();
//        Connection connection = dbTools.getConnection();
//        List<Borrowed> list = new ArrayList<Borrowed>();
//        Statement statement = connection.createStatement();
//        ResultSet rs = statement.executeQuery(sql);
//        while (rs.next()) {
//            Borrowed b = new Borrowed();
//            b.setReaderID(rs.getString("Rid"));
//            b.setBookID(rs.getString("Bid"));
//            b.setBorroweDate(rs.getString("BRborroweddate"));
//            b.setReturnDate(rs.getString("BRreturndate"));
//            list.add(b);
//        }
//        return list;
//    }

    //添加借阅信息
    public int addBorrowed(Borrowed borrowed) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        //增加借阅信息的同时将被借书的数量-1
        String sql1 = "INSERT INTO BorrowedInfo(Rid, Bid, BRborrowdate, BRshouldreturndate) VALUES(?, ?, ?, ?)";
        String sql2 = "UPDATE BookInfo SET Bexisting -= 1 WHERE Bid = ?";
        PreparedStatement ps1 = connection.prepareStatement(sql1);
        PreparedStatement ps2 = connection.prepareStatement(sql2);
        ps1.setString(1, borrowed.getReaderID());
        ps1.setString(2, borrowed.getBookID());
        ps1.setString(3, borrowed.getBorrowDate());
        ps1.setString(4, borrowed.getShouldReturnDate());
        ps2.setString(1, borrowed.getBookID());
        i = ps1.executeUpdate();
        i += ps2.executeUpdate();
        ps1.close();
        ps2.close();
        connection.close();
        return i;
    }

    //还书
    public int returnBook(String readerID, String bookID, String borrowDate, boolean flag) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        //此处flag为true的话是书可以还上，即图书未损坏的情况下，将还书日期设置为操作当天日期并将本图书数量+1
        //如果flag为false，即代表图书损坏，读者需赔款，这里将还书日期设置为0001-01-01代表图书损坏
        if (flag) {
            String sql1 = "UPDATE BorrowedInfo SET BRreturndate = ? WHERE Rid = ? AND Bid = ? AND BRborrowdate = ?";
            String sql2 = "UPDATE BookInfo SET Bexisting += 1 WHERE Bid = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date returnDate = new Date();

            ps1.setString(1, sdf.format(returnDate));
            ps1.setString(2, readerID);
            ps1.setString(3, bookID);
            ps1.setString(4, borrowDate);
            ps2.setString(1, bookID);
            i = ps1.executeUpdate();
            i += ps2.executeUpdate();
            ps1.close();
            ps2.close();
        } else {
            String sql1 = "UPDATE BorrowedInfo SET BRreturndate = ? WHERE Rid = ? AND Bid = ? AND BRborrowdate = ?";
            String sql2 = "UPDATE BookInfo SET Binventory -= 1 WHERE Bid = ?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps1.setString(1, "0001-01-01");
            ps1.setString(2, readerID);
            ps1.setString(3, bookID);
            ps1.setString(4, borrowDate);
            ps2.setString(1, bookID);
            i = ps1.executeUpdate();
            i += ps2.executeUpdate();
            ps1.close();
            ps2.close();
        }
        connection.close();
        return i;
    }

    //模糊查询
    public List fuzzyQuery(String selectItem, String selectContent) throws ClassNotFoundException, SQLException {
        List<Borrowed> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement statement;
        ResultSet rs;
        String sql;
        switch (selectItem) {
            case "学号":
                sql = "SELECT * FROM BorrowedInfo_ReaderInfo_BookInfo WHERE Rid like ? ORDER BY BRborrowdate DESC";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Borrowed b = new Borrowed();
                    b.setReaderID(rs.getString("Rid"));
                    b.setReaderName(rs.getString("Rname"));
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setBorrowDate(rs.getString("BRborrowdate"));
                    b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
                    b.setReturnDate(rs.getString("BRreturndate"));
                    list.add(b);
                }
                break;
            case "图书编号":
                sql = "SELECT * FROM BorrowedInfo_ReaderInfo_BookInfo WHERE Bid like ? ORDER BY BRborrowdate DESC";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Borrowed b = new Borrowed();
                    b.setReaderID(rs.getString("Rid"));
                    b.setReaderName(rs.getString("Rname"));
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setBorrowDate(rs.getString("BRborrowdate"));
                    b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
                    b.setReturnDate(rs.getString("BRreturndate"));
                    list.add(b);
                }
                break;
            case "姓名":
                sql = "SELECT * FROM BorrowedInfo_ReaderInfo_BookInfo WHERE Rname like ? ORDER BY BRborrowdate DESC";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Borrowed b = new Borrowed();
                    b.setReaderID(rs.getString("Rid"));
                    b.setReaderName(rs.getString("Rname"));
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setBorrowDate(rs.getString("BRborrowdate"));
                    b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
                    b.setReturnDate(rs.getString("BRreturndate"));
                    list.add(b);
                }
                break;
            case "书名":
                sql = "SELECT * FROM BorrowedInfo_ReaderInfo_BookInfo WHERE Bname like ? ORDER BY BRborrowdate DESC";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Borrowed b = new Borrowed();
                    b.setReaderID(rs.getString("Rid"));
                    b.setReaderName(rs.getString("Rname"));
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setBorrowDate(rs.getString("BRborrowdate"));
                    b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
                    b.setReturnDate(rs.getString("BRreturndate"));
                    list.add(b);
                }
                break;
            case "借书日期":
                sql = "SELECT * FROM BorrowedInfo_ReaderInfo_BookInfo WHERE BRborrowdate like ? ORDER BY BRborrowdate DESC";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Borrowed b = new Borrowed();
                    b.setReaderID(rs.getString("Rid"));
                    b.setReaderName(rs.getString("Rname"));
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setBorrowDate(rs.getString("BRborrowdate"));
                    b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
                    b.setReturnDate(rs.getString("BRreturndate"));
                    list.add(b);
                }
                break;
            case "应还书日期":
                sql = "SELECT * FROM BorrowedInfo_ReaderInfo_BookInfo WHERE BRshouldreturndate like ? ORDER BY BRborrowdate DESC";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Borrowed b = new Borrowed();
                    b.setReaderID(rs.getString("Rid"));
                    b.setReaderName(rs.getString("Rname"));
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setBorrowDate(rs.getString("BRborrowdate"));
                    b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
                    b.setReturnDate(rs.getString("BRreturndate"));
                    list.add(b);
                }
                break;
            case "实还书日期":
                sql = "SELECT * FROM BorrowedInfo_ReaderInfo_BookInfo WHERE BRreturndate like ? ORDER BY BRborrowdate DESC";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Borrowed b = new Borrowed();
                    b.setReaderID(rs.getString("Rid"));
                    b.setReaderName(rs.getString("Rname"));
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setBorrowDate(rs.getString("BRborrowdate"));
                    b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
                    b.setReturnDate(rs.getString("BRreturndate"));
                    list.add(b);
                }
                break;
            case "未还":
                sql = "SELECT * FROM BorrowedInfo_ReaderInfo_BookInfo WHERE BRreturndate IS NULL ORDER BY BRborrowdate DESC";
                statement = connection.prepareStatement(sql);
                rs = statement.executeQuery();
                while (rs.next()) {
                    Borrowed b = new Borrowed();
                    b.setReaderID(rs.getString("Rid"));
                    b.setReaderName(rs.getString("Rname"));
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setBorrowDate(rs.getString("BRborrowdate"));
                    b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
                    b.setReturnDate(rs.getString("BRreturndate"));
                    list.add(b);
                }
                break;
            default:
                break;
        }
        return list;
    }
}
