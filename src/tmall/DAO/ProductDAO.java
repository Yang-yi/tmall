package tmall.DAO;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.*;
import java.util.*;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-19 14:55
 **/
public class ProductDAO {
    public int getTotal(int cid){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT COUNT(*) FROM product WHERE cid=";

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Product bean){
        String sql = "INSERT INTO product VALUES(NULL,?,?,?,?,?,?,?)";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,bean.getName());
            ps.setString(2,bean.getSubTitle());
            ps.setFloat(3,bean.getOriginalPrice());
            ps.setFloat(4,bean.getPromotePrice());
            ps.setInt(5,bean.getStock());
            ps.setInt(6,bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreationDate()));

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()){
                bean.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Product bean){
        String sql = "UPDATE product SET `name`=?,subTitle=?,orignalPrice=?,promotePrice=?,stock=?,cid=?,createDate=? WHERE id=?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,bean.getName());
            ps.setString(2,bean.getSubTitle());
            ps.setFloat(3,bean.getOriginalPrice());
            ps.setFloat(4,bean.getPromotePrice());
            ps.setInt(5,bean.getStock());
            ps.setInt(6,bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreationDate()));
            ps.setInt(8,bean.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "DELETE FROM product WHERE id="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product get(int id){
        Product bean = new Product();

        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM product WHERE id="+id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                Category category = new CategoryDAO().get(rs.getInt("cid"));
                java.util.Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                bean.setId(id);
                bean.setName(rs.getString("name"));
                bean.setSubTitle(rs.getString("subTitle"));
                bean.setOriginalPrice(rs.getFloat("originalPrice"));
                bean.setPromotePrice(rs.getFloat("promotePrice"));
                bean.setStock(rs.getInt("stock"));
                bean.setCategory(category);
                bean.setCreationDate(createDate);
                setFirstProductImage(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<Product> list(int cid){
        return list(cid, 0, Short.MAX_VALUE);
    }

    public List<Product> list(int cid, int start, int count){
        List<Product> beans = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE cid=? ORDER BY id DESC LIMIT ?,?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,cid);
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Product bean = new Product();
                Category category = new CategoryDAO().get(cid);
                java.util.Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                bean.setId(rs.getInt("id"));
                bean.setName(rs.getString("name"));
                bean.setSubTitle(rs.getString("subTitle"));
                bean.setOriginalPrice(rs.getFloat("originalPrice"));
                bean.setPromotePrice(rs.getFloat("promotePrice"));
                bean.setStock(rs.getInt("stock"));
                bean.setCategory(category);
                bean.setCreationDate(createDate);
                setFirstProductImage(bean);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<Product> list(){
        return list(0, Short.MAX_VALUE);
    }

    public List<Product> list(int start, int count){
        List<Product> beans = new ArrayList<>();
        String sql = "SELECT * FROM product ORDER BY id DESC LIMIT ?,?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,start);
            ps.setInt(2,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Product bean = new Product();
                Category category = new CategoryDAO().get(rs.getInt("cid"));
                java.util.Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                bean.setId(rs.getInt("id"));
                bean.setName(rs.getString("name"));
                bean.setSubTitle(rs.getString("subTitle"));
                bean.setOriginalPrice(rs.getFloat("originalPrice"));
                bean.setPromotePrice(rs.getFloat("promotePrice"));
                bean.setStock(rs.getInt("stock"));
                bean.setCategory(category);
                bean.setCreationDate(createDate);
                setFirstProductImage(bean);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<Product> search(String keyword, int start, int count){
        List<Product> beans = new ArrayList<>();
        if (null == keyword || 0 == keyword.trim().length())
            return beans;
        String sql = "SELECT * FROM product WHERE `name` LIKE ? LIMIT ?,?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,"%"+keyword.trim()+"%");
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Product bean = new Product();
                Category category = new CategoryDAO().get(rs.getInt("cid"));
                java.util.Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                bean.setId(rs.getInt("id"));
                bean.setName(rs.getString("name"));
                bean.setSubTitle(rs.getString("subTitle"));
                bean.setOriginalPrice(rs.getFloat("originalPrice"));
                bean.setPromotePrice(rs.getFloat("promotePrice"));
                bean.setStock(rs.getInt("stock"));
                bean.setCategory(category);
                bean.setCreationDate(createDate);
                setFirstProductImage(bean);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public void setFirstProductImage(Product p){
        List<ProductImage> pis = new ProductImageDAO().list(p,ProductImageDAO.type_single);
        if (!pis.isEmpty()){
            p.setFirstProductImage(pis.get(0));
        }
    }

    public void setSaleAndReviewNumber(Product p){
        int saleCount = new OrderItemDAO().getSaleCount(p.getId());
        p.setSaleCount(saleCount);

        int reviewCount = new ReviewDAO().getCount(p.getId());
        p.setReviewCount(reviewCount);
    }

    public void setSaleAndReviewNumber(List<Product> products){
        for (Product p:products){
            setSaleAndReviewNumber(p);
        }
    }

    public void fill(List<Category> cs){
        for (Category c:cs){
            fill(c);
        }
    }

    public void fill(Category c){
        List<Product> products = this.list(c.getId());
        c.setProducts(products);
    }

    public void fillByRow(List<Category> cs){
        int productNumberEachRow = 8;
        for (Category c:cs){
            List<Product> products = c.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i=0; i<products.size(); i+=productNumberEachRow){
                int size = i+productNumberEachRow;
                size = size>products.size()?products.size():size;
                List<Product> productOfEachRow = products.subList(i,size);
                productsByRow.add(productOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }


}
