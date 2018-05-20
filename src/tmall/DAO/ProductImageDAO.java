package tmall.DAO;


import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-19 14:33
 **/
public class ProductImageDAO {
    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT COUNT(*) FROM productimage";
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(ProductImage bean){
        String sql= "INSERT INTO productimage VALUES(NULL,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,bean.getProduct().getId());
            ps.setString(2,bean.getType());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                bean.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ProductImage bean){

    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "DELETE FROM productimage WHERE id="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ProductImage get(int id){
        ProductImage bean = new ProductImage();
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM productimage WHERE id="+id;
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                bean.setId(id);
                int pid = rs.getInt("pid");
                Product product = new ProductDAO().get(pid);
                bean.setProduct(product);
                bean.setType(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<ProductImage> list(Product product, String type){
        return list(product,type,1,Short.MAX_VALUE);
    }

    public List<ProductImage> list(Product product, String type, int start, int count){
        List<ProductImage> beans = new ArrayList<>();
        String sql = "SELECT * FROM productimage WHERE pid=? AND type=? LIMIT ?,?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,product.getId());
            ps.setString(2,type);
            ps.setInt(3,start);
            ps.setInt(4,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                ProductImage bean = new ProductImage();
                bean.setId(rs.getInt(1));
                bean.setProduct(product);
                bean.setType(type);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

}
