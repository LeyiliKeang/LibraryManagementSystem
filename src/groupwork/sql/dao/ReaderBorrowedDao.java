package groupwork.sql.dao;

import groupwork.sql.db.DbTools;
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

public class ReaderBorrowedDao {
    public ReaderBorrowedDao() {
    }

    public List getDateReaderBorrowedInfo(String fromDate, String toDate) throws ClassNotFoundException, SQLException {
        List<Reader> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement statement;
        ResultSet rs;
        String sql;
        if (fromDate == null && toDate == null) {
            sql = "SELECT Rid, Rname, Rsex, Rclass, COUNT(*) AS Rcount FROM ReaderBorrowedInfo GROUP BY Rid, Rname, Rsex, Rclass ORDER BY Rcount DESC";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                Reader r = new Reader();
                r.setReaderID(rs.getString("Rid"));
                r.setReaderName(rs.getString("Rname"));
                r.setReaderSex(rs.getString("Rsex"));
                r.setReaderClass(rs.getString("Rclass"));
                r.setCount(rs.getInt("Rcount"));
                list.add(r);
            }
        }
        if (fromDate == null && toDate != null) {
            sql = "SELECT Rid, Rname, Rsex, Rclass, COUNT(*) AS Rcount FROM ReaderBorrowedInfo WHERE BRborrowdate >= ? AND BRborrowdate <= ? GROUP BY Rid, Rname, Rsex, Rclass ORDER BY Rcount DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "2015-05-01");
            statement.setString(2, toDate);
            rs = statement.executeQuery();
            while (rs.next()) {
                Reader r = new Reader();
                r.setReaderID(rs.getString("Rid"));
                r.setReaderName(rs.getString("Rname"));
                r.setReaderSex(rs.getString("Rsex"));
                r.setReaderClass(rs.getString("Rclass"));
                r.setCount(rs.getInt("Rcount"));
                list.add(r);
            }
        }
        if (fromDate != null && toDate == null) {
            sql = "SELECT Rid, Rname, Rsex, Rclass, COUNT(*) AS Rcount FROM ReaderBorrowedInfo WHERE BRborrowdate >= ? AND BRborrowdate <= ? GROUP BY Rid, Rname, Rsex, Rclass ORDER BY Rcount DESC";
            statement = connection.prepareStatement(sql);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            statement.setString(1, fromDate);
            statement.setString(2, sdf.format(date));
            rs = statement.executeQuery();
            while (rs.next()) {
                Reader r = new Reader();
                r.setReaderID(rs.getString("Rid"));
                r.setReaderName(rs.getString("Rname"));
                r.setReaderSex(rs.getString("Rsex"));
                r.setReaderClass(rs.getString("Rclass"));
                r.setCount(rs.getInt("Rcount"));
                list.add(r);
            }
        }
        if (fromDate != null && toDate != null) {
            sql = "SELECT Rid, Rname, Rsex, Rclass, COUNT(*) AS Rcount FROM ReaderBorrowedInfo WHERE BRborrowdate >= ? AND BRborrowdate <= ? GROUP BY Rid, Rname, Rsex, Rclass ORDER BY Rcount DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, fromDate);
            statement.setString(2, toDate);
            rs = statement.executeQuery();
            while (rs.next()) {
                Reader r = new Reader();
                r.setReaderID(rs.getString("Rid"));
                r.setReaderName(rs.getString("Rname"));
                r.setReaderSex(rs.getString("Rsex"));
                r.setReaderClass(rs.getString("Rclass"));
                r.setCount(rs.getInt("Rcount"));
                list.add(r);
            }
        }
        return list;
    }

    public List getOneReaderAllBorrowedInfo(String id) throws ClassNotFoundException, SQLException {
        List<Borrowed> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "SELECT Bid, Bname, BRborrowdate, BRshouldreturndate, BRreturndate FROM BorrowedInfo_ReaderInfo_BookInfo WHERE Rid = ? ORDER BY BRborrowdate DESC";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Borrowed b = new Borrowed();
            b.setBookID(rs.getString("Bid"));
            b.setBookName(rs.getString("Bname"));
            b.setBorrowDate(rs.getString("BRborrowdate"));
            b.setShouldReturnDate(rs.getString("BRshouldreturndate"));
            b.setReturnDate(rs.getString("BRreturndate"));
            list.add(b);
        }
        return list;
    }
}
