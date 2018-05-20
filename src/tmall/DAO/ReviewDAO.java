package tmall.DAO;

import tmall.bean.Product;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-19 17:57
 **/
public class ReviewDAO {
    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT COUNT(*) FROM review";
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getTotal(int pid){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT COUNT(*) FROM review WHERE pid="+pid;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Review bean){
        String sql = "INSERT INTO review VALUES(NULL,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()){
                bean.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Review bean){
        String sql = "UPDATE review SET content=?,uid=?,pid=?,createDate=? WHERE id=?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4,DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(5,bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "DELETE FROM review WHERE id="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Review get(int id){
        Review bean = new Review();
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM review WHERE id="+id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                bean.setId(id);
                bean.setContent(rs.getString("content"));
                Product product = new ProductDAO().get(rs.getInt("pid"));
                User user = new UserDAO().get(rs.getInt("uid"));
                bean.setUser(user);
                bean.setProduct(product);
                bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public int getCount(int pid){
        int count = 0;
        String sql = "SELECT COUNT(*) FROM review WHERE pid=?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Review> list(int pid){
        return list(pid,0,Short.MAX_VALUE);
    }

    public List<Review> list(int pid, int start, int count){
        List<Review> beans = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE pid=? ORDER BY id LIMIT ?,?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,pid);
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Review bean = new Review();
                bean.setId(rs.getInt("id"));
                bean.setContent(rs.getString("content"));
                Product product = new ProductDAO().get(rs.getInt("pid"));
                User user = new UserDAO().get(rs.getInt("uid"));
                Date CreateDate = DateUtil.d2t(rs.getTimestamp("createDate"));
                bean.setProduct(product);
                bean.setUser(user);
                bean.setCreateDate(CreateDate);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public boolean isExist(String content, int pid){
        String sql = "SELECT * FROM review WHERE content=? AND pid=?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,content);
            ps.setInt(2,pid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
