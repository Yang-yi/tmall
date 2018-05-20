package tmall.DAO;

import tmall.bean.Category;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-16 23:52
 **/
public class CategoryDAO {
    public int getTotal(){
        int total = 0;

        try(
            Connection c = DBUtil.getConnection(); Statement s = c.createStatement();
        ) {
            String sql = "SELECT COUNT(*) FROM category;";
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Category category){
        String sql = "INSERT INTO category VALUES(NULL,?);";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,category.getName());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = rs.getInt(1);
                category.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Category category){
        String sql = "UPDATE category SET `name`=? WHERE id=?;";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,category.getName());
            ps.setInt(2,category.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();) {
            String sql = "DELETE FROM category WHERE id = "+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Category get(int id){
        Category category = null;
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();) {
            String sql = "SELECT * FROM category WHERE id="+id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                category = new Category();
                category.setId(id);
                category.setName(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public List<Category> list(){
        return list(0,Short.MAX_VALUE);
    }

    public List<Category> list(int start, int count){
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category order by id desc limit ?,?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,start);
            ps.setInt(2,count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Category category = new Category();
                category.setId(rs.getInt(1));
                category.setName(rs.getString(2));
                categories.add(category);
//                System.out.println("有");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
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

    public static void main(String[] args){
        List<Category> list = new CategoryDAO().list();
        System.out.println(list.get(0).getName());
    }


}
