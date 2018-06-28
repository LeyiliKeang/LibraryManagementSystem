package groupwork.sql.dao;

import groupwork.sql.db.DbTools;
import groupwork.sql.model.Book;
import groupwork.sql.model.Borrowed;
import groupwork.sql.model.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookBorrowedDao {
    public BookBorrowedDao() {
    }

    public List getDateBookBorrowedInfo(String fromDate, String toDate) throws ClassNotFoundException, SQLException {
        List<Book> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement statement;
        ResultSet rs;
        String sql;
        if (fromDate == null && toDate == null) {
            sql = "SELECT Bid, Bname, Bauthor, Bpress, BTname, COUNT(*) AS Bcount FROM BookBorrowedInfo GROUP BY Bid, Bname, Bauthor, Bpress, BTname ORDER BY Bcount DESC";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                Book b = new Book();
                b.setBookID(rs.getString("Bid"));
                b.setBookName(rs.getString("Bname"));
                b.setAuthor(rs.getString("Bauthor"));
                b.setPress(rs.getString("Bpress"));
                b.setBookType(rs.getString("BTname"));
                b.setCount(rs.getInt("Bcount"));
                list.add(b);
            }
        }
        if (fromDate == null && toDate != null) {
            sql = "SELECT Bid, Bname, Bauthor, Bpress, BTname, COUNT(*) AS Bcount FROM BookBorrowedInfo WHERE BRborrowdate >= ? AND BRborrowdate <= ? GROUP BY Bid, Bname, Bauthor, Bpress, BTname ORDER BY Bcount DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "2015-05-01");
            statement.setString(2, toDate);
            rs = statement.executeQuery();
            while (rs.next()) {
                Book b = new Book();
                b.setBookID(rs.getString("Bid"));
                b.setBookName(rs.getString("Bname"));
                b.setAuthor(rs.getString("Bauthor"));
                b.setPress(rs.getString("Bpress"));
                b.setBookType(rs.getString("BTname"));
                b.setCount(rs.getInt("Bcount"));
                list.add(b);
            }
        }
        if (fromDate != null && toDate == null) {
            sql = "SELECT Bid, Bname, Bauthor, Bpress, BTname, COUNT(*) AS Bcount FROM BookBorrowedInfo WHERE BRborrowdate >= ? AND BRborrowdate <= ? GROUP BY Bid, Bname, Bauthor, Bpress, BTname ORDER BY Bcount DESC";
            statement = connection.prepareStatement(sql);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            statement.setString(1, fromDate);
            statement.setString(2, sdf.format(date));
            rs = statement.executeQuery();
            while (rs.next()) {
                Book b = new Book();
                b.setBookID(rs.getString("Bid"));
                b.setBookName(rs.getString("Bname"));
                b.setAuthor(rs.getString("Bauthor"));
                b.setPress(rs.getString("Bpress"));
                b.setBookType(rs.getString("BTname"));
                b.setCount(rs.getInt("Bcount"));
                list.add(b);
            }
        }
        if (fromDate != null && toDate != null) {
            sql = "SELECT Bid, Bname, Bauthor, Bpress, BTname, COUNT(*) AS Bcount FROM BookBorrowedInfo WHERE BRborrowdate >= ? AND BRborrowdate <= ? GROUP BY Bid, Bname, Bauthor, Bpress, BTname ORDER BY Bcount DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, fromDate);
            statement.setString(2, toDate);
            rs = statement.executeQuery();
            while (rs.next()) {
                Book b = new Book();
                b.setBookID(rs.getString("Bid"));
                b.setBookName(rs.getString("Bname"));
                b.setAuthor(rs.getString("Bauthor"));
                b.setPress(rs.getString("Bpress"));
                b.setBookType(rs.getString("BTname"));
                b.setCount(rs.getInt("Bcount"));
                list.add(b);
            }
        }
        return list;
    }

    public List getOneBookAllBorrowedInfo(String id) throws ClassNotFoundException, SQLException {
        List<Borrowed> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "SELECT Rid, Rname, BRborrowdate, BRshouldreturndate, BRreturndate FROM BorrowedInfo_ReaderInfo_BookInfo WHERE Bid = ? ORDER BY BRborrowdate DESC";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Borrowed b = new Borrowed();
            b.setReaderID(rs.getString("Rid"));
            b.setReaderName(rs.getString("Rname"));
            b.setBorrowDate(rs.getString("BRborrowdate"));
            b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
            b.setReturnDate(rs.getString("BRreturndate"));
            list.add(b);
        }
        return list;
    }
}
