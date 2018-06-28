package groupwork.sql.dao;

import groupwork.sql.db.DbTools;
import groupwork.sql.model.Reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderDao {
    public ReaderDao() {
    }

    //获取单个读者的信息
    public Reader getOne(String id) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM ReaderInfo WHERE Rid = ?";
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        Reader r = new Reader();
        while (rs.next()) {
            r.setReaderID(rs.getString("Rid"));
            r.setReaderName(rs.getString("Rname"));
            r.setReaderSex(rs.getString("Rsex"));
            r.setReaderClass(rs.getString("Rclass"));
            r.setReaderPhone(rs.getString("Rphone"));
        }
        return r;
    }

    //增加读者信息
    public int addReader(Reader reader) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "INSERT INTO ReaderInfo(Rid, Rname, Rsex, Rclass, Rphone) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, reader.getReaderID());
        ps.setString(2, reader.getReaderName());
        ps.setString(3, reader.getReaderSex());
        ps.setString(4, reader.getReaderClass());
        ps.setString(5, reader.getReaderPhone());
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    //修改读者信息
    public int updateReader(Reader reader, String id) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "UPDATE ReaderInfo SET Rid = ?, Rname = ?, Rsex = ?, Rclass = ?, Rphone = ? WHERE Rid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, reader.getReaderID());
        ps.setString(2, reader.getReaderName());
        ps.setString(3, reader.getReaderSex());
        ps.setString(4, reader.getReaderClass());
        ps.setString(5, reader.getReaderPhone());
        ps.setString(6, id);
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    //删除读者
    public int deleteReader(String id) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "DELETE FROM ReaderInfo WHERE Rid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    //获取读者借阅信息
    public List getReaderBorrowedInfo(String id) throws ClassNotFoundException, SQLException {
        List<Reader> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement statement;
        ResultSet rs;
        String sql;
        //如果传入一个读者编号，将查询该读者未还书的数量
        //如果该读者书籍全部归还，该sql语句查询不到该读者的为还书数量
        //便在后面再增加一个Reader
        //获取是用List的get(0)方法即可
        if (id != null) {
            sql = "SELECT Rid, COUNT(*) AS Notreturn FROM BorrowedInfo WHERE BRreturndate IS NULL GROUP BY Rid HAVING Rid = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            rs = statement.executeQuery();
            while (rs.next()) {
                Reader r = new Reader();
                r.setNotReturn(rs.getInt("Notreturn"));
                list.add(r);
            }
            Reader r = new Reader();
            r.setNotReturn(0);
            list.add(r);
        } else {
            sql = "SELECT * FROM ReaderBorrowedInfo";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                Reader r = new Reader();
                r.setReaderID(rs.getString("Rid"));
                r.setReaderName(rs.getString("Rname"));
                r.setReaderSex(rs.getString("Rsex"));
                r.setReaderClass(rs.getString("Rclass"));
                r.setNotReturn(rs.getInt("Notreturn"));
                r.setAllBorrowed(rs.getInt("Allborrowed"));
                list.add(r);
            }
        }
        return list;
    }

    //模糊查询
    public List fuzzyQuery(String selectItem, String selectContent) throws ClassNotFoundException, SQLException {
        List<Reader> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement statement;
        ResultSet rs;
        String sql;
        switch (selectItem) {
            case "学号":
                sql = "SELECT * FROM ReaderInfo WHERE Rid like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Reader r = new Reader();
                    r.setReaderID(rs.getString("Rid"));
                    r.setReaderName(rs.getString("Rname"));
                    r.setReaderSex(rs.getString("Rsex"));
                    r.setReaderClass(rs.getString("Rclass"));
                    r.setReaderPhone(rs.getString("Rphone"));
                    list.add(r);
                }
                break;
            case "姓名":
                sql = "SELECT * FROM ReaderInfo WHERE Rname like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Reader r = new Reader();
                    r.setReaderID(rs.getString("Rid"));
                    r.setReaderName(rs.getString("Rname"));
                    r.setReaderSex(rs.getString("Rsex"));
                    r.setReaderClass(rs.getString("Rclass"));
                    r.setReaderPhone(rs.getString("Rphone"));
                    list.add(r);
                }
                break;
            case "性别":
                sql = "SELECT * FROM ReaderInfo WHERE Rsex like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Reader r = new Reader();
                    r.setReaderID(rs.getString("Rid"));
                    r.setReaderName(rs.getString("Rname"));
                    r.setReaderSex(rs.getString("Rsex"));
                    r.setReaderClass(rs.getString("Rclass"));
                    r.setReaderPhone(rs.getString("Rphone"));
                    list.add(r);
                }
                break;
            case "区队":
                sql = "SELECT * FROM ReaderInfo WHERE Rclass like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Reader r = new Reader();
                    r.setReaderID(rs.getString("Rid"));
                    r.setReaderName(rs.getString("Rname"));
                    r.setReaderSex(rs.getString("Rsex"));
                    r.setReaderClass(rs.getString("Rclass"));
                    r.setReaderPhone(rs.getString("Rphone"));
                    list.add(r);
                }
                break;
            default:
                break;
        }
        return list;
    }
}
