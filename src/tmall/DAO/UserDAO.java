package tmall.DAO;

import com.sun.jndi.dns.DnsUrl;
import tmall.bean.User;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-18 14:28
 **/
public class UserDAO {
    public int getTotal(){
        int total = 0;

        try(
                Connection c = DBUtil.getConnection(); Statement s = c.createStatement();
        ) {
            String sql = "SELECT COUNT(*) FROM `user`;";
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(User user){
        String sql = "INSERT INTO `user` VALUES(NULL,?,?);";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,user.getName());
            ps.setString(2,user.getPassword());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = rs.getInt(1);
                user.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user){
        String sql = "UPDATE `user` SET `name`=?,`password`=? WHERE id=?;";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,user.getName());
            ps.setString(2,user.getPassword());
            ps.setInt(3,user.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();) {
            String sql = "DELETE FROM `user` WHERE id = "+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User get(int id){
        User user = null;
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();) {
            String sql = "SELECT * FROM `user` WHERE id="+id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                user = new User();
                user.setId(id);
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> list(){
        return list(0,Short.MAX_VALUE);
    }

    public List<User> list(int start, int count){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM `user` order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,start);
            ps.setInt(2,count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                users.add(user);
//                System.out.println("有");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public boolean isExist(String name){
        User user = get(name);
        return null != user;
    }

    public User get(String name){
        User user = null;
        String sql = "SELECT * FROM `user` WHERE `name`=?;";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                user = new User();
                user.setId(rs.getInt(1));
                user.setName(name);
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User get(String name,String password){
        User user = null;
        String sql = "SELECT * FROM `user` WHERE `name`=? AND `password`=?;";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,name);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                user = new User();
                user.setId(rs.getInt(1));
                user.setName(name);
                user.setPassword(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
/*
    public List<Category> list(int start, int count){
        List<Category> categories = new ArrayList<>();

        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM category order by id desc limit "+start+","+count;
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()){
                Category category = new Category();
                category.setId(rs.getInt(1));
                category.setName(rs.getString(2));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }*/

  /*  public static void main(String[] args){
        List<Category> list = new CategoryDAO().list();
        System.out.println(list.get(0).getName());
    }*/
}
