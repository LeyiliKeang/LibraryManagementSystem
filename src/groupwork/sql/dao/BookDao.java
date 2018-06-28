package groupwork.sql.dao;

import groupwork.sql.db.DbTools;
import groupwork.sql.model.Book;
import groupwork.sql.model.Manager;
import groupwork.sql.model.Reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    public BookDao() {
    }

    //获取单个图书的信息
    public Book getOne(String id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM BookInfo_BookType WHERE Bid = ?";
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        Book b = new Book();
        while (rs.next()) {
            b.setBookID(rs.getString("Bid"));
            b.setBookName(rs.getString("Bname"));
            b.setAuthor(rs.getString("Bauthor"));
            b.setPress(rs.getString("Bpress"));
            b.setBookType(rs.getString("BTname"));
            b.setPage(rs.getInt("Bpage"));
            b.setPrice(rs.getInt("Bprice"));
            b.setExisting(rs.getInt("Bexisting"));
            b.setInventory(rs.getInt("Binventory"));
            b.setStorageDate(rs.getString("BstorageDate"));
        }
        return b;
    }

    //添加图书
    public int addBook(Book book) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "INSERT INTO BookInfo(Bid, Bname, Bauthor, Bpress, BTid, Bpage, Bprice, Bexisting, Binventory, BstorageDate) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, book.getBookID());
        ps.setString(2, book.getBookName());
        ps.setString(3, book.getAuthor());
        ps.setString(4, book.getPress());
        ps.setString(5, book.getBookType());
        ps.setInt(6, book.getPage());
        ps.setInt(7, book.getPrice());
        ps.setInt(8, book.getExisting());
        ps.setInt(9, book.getInventory());
        ps.setString(10, book.getStorageDate());
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    //修改图书
    public int updateBook(Book book, String id) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "UPDATE BookInfo SET Bid = ?, Bname = ?, Bauthor = ?, Bpress = ?, BTid = ?, Bpage = ?, Bprice = ?, Bexisting = ?, Binventory = ?, BstorageDate = ? WHERE Bid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, book.getBookID());
        ps.setString(2, book.getBookName());
        ps.setString(3, book.getAuthor());
        ps.setString(4, book.getPress());
        ps.setString(5, book.getBookType());
        ps.setInt(6, book.getPage());
        ps.setInt(7, book.getPrice());
        ps.setInt(8, book.getExisting());
        ps.setInt(9, book.getInventory());
        ps.setString(10, book.getStorageDate());
        ps.setString(11, id);
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    //删除图书
    public int deleteBook(String id) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "DELETE FROM BookInfo WHERE Bid = " + id;
        Statement statement = connection.createStatement();
        i = statement.executeUpdate(sql);
        statement.close();
        connection.close();
        return i;
    }

    public List getBookTypeBook(String id) throws ClassNotFoundException, SQLException {
        List<Book> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "SELECT * FROM BookInfo WHERE BTid like ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Book b = new Book();
            b.setBookID(rs.getString("Bid"));
            b.setBookName(rs.getString("Bname"));
            b.setAuthor(rs.getString("Bauthor"));
            b.setPress(rs.getString("Bpress"));
            b.setBookType(rs.getString("BTid"));
            b.setPage(rs.getInt("Bpage"));
            b.setPrice(rs.getInt("Bprice"));
            b.setExisting(rs.getInt("Bexisting"));
            b.setInventory(rs.getInt("Binventory"));
            b.setStorageDate(rs.getString("BstorageDate"));
            list.add(b);
        }
        return list;
    }

    //模糊查询，文本框内文本变化即进行查询
    public List fuzzyQuery(String selectItem, String selectContent) throws ClassNotFoundException, SQLException {
        List<Book> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement statement;
        ResultSet rs;
        String sql;
        switch (selectItem) {
            case "图书编号":
                sql = "SELECT * FROM BookInfo_BookType WHERE Bid like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Book b = new Book();
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setAuthor(rs.getString("Bauthor"));
                    b.setPress(rs.getString("Bpress"));
                    b.setBookType(rs.getString("BTname"));
                    b.setPage(rs.getInt("Bpage"));
                    b.setPrice(rs.getInt("Bprice"));
                    b.setExisting(rs.getInt("Bexisting"));
                    b.setInventory(rs.getInt("Binventory"));
                    b.setStorageDate(rs.getString("BstorageDate"));
                    list.add(b);
                }
                break;
            case "书名":
                sql = "SELECT * FROM BookInfo_BookType WHERE Bname like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Book b = new Book();
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setAuthor(rs.getString("Bauthor"));
                    b.setPress(rs.getString("Bpress"));
                    b.setBookType(rs.getString("BTname"));
                    b.setPage(rs.getInt("Bpage"));
                    b.setPrice(rs.getInt("Bprice"));
                    b.setExisting(rs.getInt("Bexisting"));
                    b.setInventory(rs.getInt("Binventory"));
                    b.setStorageDate(rs.getString("BstorageDate"));
                    list.add(b);
                }
                break;
            case "作者":
                sql = "SELECT * FROM BookInfo_BookType WHERE Bauthor like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Book b = new Book();
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setAuthor(rs.getString("Bauthor"));
                    b.setPress(rs.getString("Bpress"));
                    b.setBookType(rs.getString("BTname"));
                    b.setPage(rs.getInt("Bpage"));
                    b.setPrice(rs.getInt("Bprice"));
                    b.setExisting(rs.getInt("Bexisting"));
                    b.setInventory(rs.getInt("Binventory"));
                    b.setStorageDate(rs.getString("BstorageDate"));
                    list.add(b);
                }
                break;
            case "出版社":
                sql = "SELECT * FROM BookInfo_BookType WHERE Bpress like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Book b = new Book();
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setAuthor(rs.getString("Bauthor"));
                    b.setPress(rs.getString("Bpress"));
                    b.setBookType(rs.getString("BTname"));
                    b.setPage(rs.getInt("Bpage"));
                    b.setPrice(rs.getInt("Bprice"));
                    b.setExisting(rs.getInt("Bexisting"));
                    b.setInventory(rs.getInt("Binventory"));
                    b.setStorageDate(rs.getString("BstorageDate"));
                    list.add(b);
                }
                break;
            case "类型":
                sql = "SELECT * FROM BookInfo_BookType WHERE BTname like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Book b = new Book();
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setAuthor(rs.getString("Bauthor"));
                    b.setPress(rs.getString("Bpress"));
                    b.setBookType(rs.getString("BTname"));
                    b.setPage(rs.getInt("Bpage"));
                    b.setPrice(rs.getInt("Bprice"));
                    b.setExisting(rs.getInt("Bexisting"));
                    b.setInventory(rs.getInt("Binventory"));
                    b.setStorageDate(rs.getString("BstorageDate"));
                    list.add(b);
                }
                break;
            case "入库时间":
                sql = "SELECT * FROM BookInfo_BookType WHERE BstorageDate like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Book b = new Book();
                    b.setBookID(rs.getString("Bid"));
                    b.setBookName(rs.getString("Bname"));
                    b.setAuthor(rs.getString("Bauthor"));
                    b.setPress(rs.getString("Bpress"));
                    b.setBookType(rs.getString("BTname"));
                    b.setPage(rs.getInt("Bpage"));
                    b.setPrice(rs.getInt("Bprice"));
                    b.setExisting(rs.getInt("Bexisting"));
                    b.setInventory(rs.getInt("Binventory"));
                    b.setStorageDate(rs.getString("BstorageDate"));
                    list.add(b);
                }
                break;
            default:
                break;
        }
        return list;
    }
}