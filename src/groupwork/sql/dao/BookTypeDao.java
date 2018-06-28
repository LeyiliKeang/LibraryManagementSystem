package groupwork.sql.dao;

import groupwork.sql.db.DbTools;
import groupwork.sql.model.BookType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookTypeDao {
    public BookTypeDao() {
    }


    public BookType getOne(String id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM BookType WHERE BTid = ?";
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        BookType bookType = new BookType();
        while (rs.next()) {
            bookType.setBookTypeID(rs.getString("BTid"));
            bookType.setBookTypeName(rs.getString("BTname"));
        }
        return bookType;
    }

    //获取图书类型信息
    public List getBookType() throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM BookType";
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        List<BookType> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            BookType bookType = new BookType();
            bookType.setBookTypeID(rs.getString("BTid"));
            bookType.setBookTypeName(rs.getString("BTname"));
            list.add(bookType);
        }
        return list;
    }

    //添加图书类型信息
    public int addBookType(BookType bookType) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "INSERT INTO BookType(BTid, BTname) VALUES(?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, bookType.getBookTypeID());
        ps.setString(2, bookType.getBookTypeName());
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    //修改图书类型信息
    public int updateBookType(BookType bookType, String id) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "UPDATE BookType SET BTid = ?, BTname = ? WHERE BTid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, bookType.getBookTypeID());
        ps.setString(2, bookType.getBookTypeName());
        ps.setString(3, bookType.getSelectID());
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    //删除图书类型信息
    public int deleteBookType(String id) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "DELETE FROM BookType WHERE BTid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    //通过图书类型的名字获取图书类型的编号
    public String getBookTypeID(String bookTypeName) throws ClassNotFoundException, SQLException {
        String sql = "SELECT BTid FROM BookType WHERE BTname = ?";
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, bookTypeName);
        ResultSet rs = ps.executeQuery();
        String bookTypeID = null;
        while (rs.next()) {
            bookTypeID = rs.getString("BTid");
        }
        return bookTypeID;
    }
}
