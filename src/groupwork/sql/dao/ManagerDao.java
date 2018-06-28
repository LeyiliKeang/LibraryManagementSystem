package groupwork.sql.dao;

import groupwork.sql.db.DbTools;
import groupwork.sql.model.Book;
import groupwork.sql.model.Manager;
import groupwork.sql.model.Reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerDao {
    public ManagerDao() {
    }

    public int addManager(Manager manager) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "INSERT INTO ManagerInfo (Mid, Mname, Mpassword, Mphone, Mpower)values(?, ?, ?, ? ,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, manager.getManagerIDNumber());
        ps.setString(2, manager.getManagerName());
        ps.setString(3, manager.getManagerPassword());
        ps.setString(4, manager.getManagerPhone());
        ps.setString(5, "0");
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    public void deleteManager(String number) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM ManagerInfo WHERE Mid = ?";
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, number);
        ps.executeUpdate();
    }

    public int updateBook(Manager manager, String id) throws ClassNotFoundException, SQLException {
        int i = 0;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        String sql = "UPDATE ManagerInfo SET Mid = ?, Mname = ?, Mpassword = ?, Mphone = ? WHERE Mid = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, manager.getManagerIDNumber());
        ps.setString(2, manager.getManagerName());
        ps.setString(3, manager.getManagerPassword());
        ps.setString(4, manager.getManagerPhone());
        ps.setString(5, id);
        i = ps.executeUpdate();
        ps.close();
        connection.close();
        return i;
    }

    public Manager getOne(String id) throws ClassNotFoundException, SQLException {
        Manager manager = new Manager();
        String sql = "SELECT * FROM ManagerInfo WHERE Mid = ?";
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            manager.setManagerIDNumber(rs.getString("Mid"));
            manager.setManagerName(rs.getString("Mname"));
            manager.setManagerPassword(rs.getString("Mpassword"));
            manager.setManagerPhone(rs.getString("Mphone"));
        }
        return manager;
    }

    public boolean isLogin(String managerID, String password) throws ClassNotFoundException, SQLException {
        boolean flag = false;
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT Mid, Mpassword, Mpower FROM ManagerInfo";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            if (managerID.equals(rs.getString("Mid")) && password.equals(rs.getString("Mpassword"))) {
                Manager.setManagerPower(rs.getString("Mpower"));
                Manager.setIdNumber(rs.getString("Mid"));
                flag = true;
                break;
            }
        }
        return flag;
    }

    public List fuzzyQuery(String selectItem, String selectContent) throws ClassNotFoundException, SQLException {
        List<Manager> list = new ArrayList<>();
        DbTools dbTools = new DbTools();
        Connection connection = dbTools.getConnection();
        PreparedStatement statement;
        ResultSet rs;
        String sql;
        switch (selectItem) {
            case "身份证号":
                sql = "SELECT * FROM ManagerInfo WHERE Mid like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Manager m = new Manager();
                    m.setManagerIDNumber(rs.getString("Mid"));
                    m.setManagerName(rs.getString("Mname"));
                    m.setManagerPhone(rs.getString("Mphone"));
                    list.add(m);
                }
                break;
            case "姓名":
                sql = "SELECT * FROM ManagerInfo WHERE Mname like ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + selectContent + "%");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Manager m = new Manager();
                    m.setManagerIDNumber(rs.getString("Mid"));
                    m.setManagerName(rs.getString("Mname"));
                    m.setManagerPhone(rs.getString("Mphone"));
                    list.add(m);
                }
                break;
        }
        return list;
    }
}
