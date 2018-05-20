package tmall.DAO;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-19 00:48
 **/
public class PropertyDAO {
    public int getTotal(int cid){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT COUNT(*) FROM property WHERE cid="+cid;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Property property){
        String sql = "INSERT INTO property VALUES(NULL,?,?);";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            int cid = property.getCategory().getId();
            String name = property.getName();
            ps.setInt(1,cid);
            ps.setString(2,name);

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()){
                int id = rs.getInt(1);
                property.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Property property){
        String sql = "UPDATE property SET cid=?,`name`=? WHERE id=?";
        try(Connection c = DBUtil.getConnection();PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,property.getCategory().getId());
            ps.setString(2,property.getName());
            ps.setInt(3,property.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "DELETE FROM property WHERE id="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Property get(int id){
        Property property = new Property();
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM property WHERE id="+id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                property.setId(id);
                int cid = rs.getInt("cid");
                Category category = new CategoryDAO().get(cid);
                property.setCategory(category);
                property.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return property;
    }

    public List<Property> list(int cid){
        return list(cid,1,Short.MAX_VALUE);
    }

    public List<Property> list(int cid,int start,int count){
        List<Property> propertyList = new ArrayList<>();
        String sql = "SELECT * FROM property WHERE cid=? LIMIT ?,?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,cid);
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Property property = new Property();
                property.setId(rs.getInt(1));
                Category category = new CategoryDAO().get(cid);
                property.setCategory(category);
                property.setName(rs.getString("name"));

                propertyList.add(property);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return propertyList;
    }
}
