package tmall.DAO;

import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-19 16:08
 **/
public class PropertyValueDAO {
    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();){
            String sql = "SELECT COUNT(*) FROM propertyvalue";
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(PropertyValue bean){
        String sql = "INSERT INTO propertyvalue VALUES(NULL,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getProperty().getId());
            ps.setString(3,bean.getValue());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()){
                bean.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(PropertyValue bean){
        String sql = "UPDATE propertyvalue SET pid=?,ptid=?,`value`=? WHERE id=?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getProperty().getId());
            ps.setString(3,bean.getValue());
            ps.setInt(4,bean.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "DELETE FROM propertyvalue WHERE id="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PropertyValue get(int id){
        PropertyValue bean = new PropertyValue();
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM propertyvalue WHERE id="+id;
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                bean.setId(id);
                Product product = new ProductDAO().get(rs.getInt("pid"));
                Property property = new PropertyDAO().get(rs.getInt("ptid"));
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(rs.getString("value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public PropertyValue get(int pid, int ptid){
        PropertyValue bean = new PropertyValue();
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM propertyvalue WHERE pid="+pid+" AND ptid="+ptid;
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                bean.setId(rs.getInt("id"));
                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(rs.getString("value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<PropertyValue> list(){
        return list(0,Short.MAX_VALUE);
    }

    public List<PropertyValue> list(int start, int count){
        List<PropertyValue> beans = new ArrayList<>();
        String sql = "SELECT * FROM propertyvalue ORDER BY id DESC LIMIT ?,?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,start);
            ps.setInt(2,count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                PropertyValue bean = new PropertyValue();
                Product product = new ProductDAO().get(rs.getInt("pid"));
                Property property = new PropertyDAO().get(rs.getInt("ptid"));
                bean.setId(rs.getInt("id"));
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(rs.getString("value"));
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public void init(Product p){
        List<Property> pts = new PropertyDAO().list(p.getCategory().getId());
        for (Property pt:pts){
            PropertyValue pv = get(p.getId(),pt.getId());
            if (null == pv){
                pv.setProduct(p);
                pv.setProperty(pt);
                this.add(pv);
            }

        }
    }

    public List<PropertyValue> list(int pid){
        List<PropertyValue> beans = new ArrayList<>();
        String sql = "SELECT * FROM propertyvalue WHERE pid=? ORDER BY ptid DESC";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                PropertyValue bean = new PropertyValue();
                bean.setId(rs.getInt("id"));
                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(rs.getInt("ptid"));
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(rs.getString("value"));
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }


}
