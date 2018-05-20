package tmall.DAO;

import com.sun.org.apache.xpath.internal.operations.Or;
import tmall.bean.Order;
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
 * @create: 2018-05-19 20:34
 **/
public class OrderDAO {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT COUNT(*) FROM order_";
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Order bean){
        String sql = "INSERT INTO order_ VALUES(NULL,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,bean.getOrderCode());
            ps.setString(2,bean.getAddress());
            ps.setString(3,bean.getPost());
            ps.setString(4,bean.getReceiver());
            ps.setString(5,bean.getMobile());
            ps.setString(6,bean.getUserMessage());

            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8,DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9,DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10,DateUtil.d2t(bean.getConfirmDate()));

            ps.setInt(11,bean.getUser().getId());
            ps.setString(12,bean.getStatus());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()){
                bean.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Order bean){
        String sql = "UPDATE order_ SET orderCode=?,address=?,post=?,receiver=?,mobile=?,userMessage=?,createDate=?,payDate=?,deliveryDate=?,confirmDate=?,uid=?,`status`=? WHERE id=?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1,bean.getOrderCode());
            ps.setString(2,bean.getAddress());
            ps.setString(3,bean.getPost());
            ps.setString(4,bean.getReceiver());
            ps.setString(5,bean.getMobile());
            ps.setString(6,bean.getUserMessage());

            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8,DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9,DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10,DateUtil.d2t(bean.getConfirmDate()));

            ps.setInt(11,bean.getUser().getId());
            ps.setString(12,bean.getStatus());
            ps.setInt(13,bean.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "DELETE FROM order_ WHERE id="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order get(int id){
        Order bean = new Order();

        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM order_ WHERE id="+id;
            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                String orderCode = rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                int uid = rs.getInt("uid");
                User user = new UserDAO().get(uid);
                String status = rs.getString("status");

                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setUser(user);
                bean.setStatus(status);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<Order> list(){
        return list(0,Short.MAX_VALUE);
    }

    public List<Order> list(int start, int count){
        List<Order> beans = new ArrayList<>();
        String sql = "SELECT * FROM order_ ORDER BY id DESC LIMIT ?,?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,start);
            ps.setInt(2,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Order bean = new Order();

                int id = rs.getInt("id");
                String orderCode = rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                int uid = rs.getInt("uid");
                User user = new UserDAO().get(uid);
                String status = rs.getString("status");

                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setUser(user);
                bean.setStatus(status);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<Order> list(int uid, String excludeStatus){
        return list(uid,excludeStatus,0,Short.MAX_VALUE);
    }

    public List<Order> list(int uid, String excludeStatus, int start, int count){
        List<Order> beans = new ArrayList<>();
        String sql = "SELECT * FROM order_ WHERE uid=? AND `status`<>? ORDER BY id DESC LIMIT ?,?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,uid);
            ps.setString(2,excludeStatus);
            ps.setInt(3,start);
            ps.setInt(4,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Order bean = new Order();

                int id = rs.getInt("id");
                String orderCode = rs.getString("orderCode");
                String address = rs.getString("address");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                User user = new UserDAO().get(uid);
                String status = rs.getString("status");

                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setAddress(address);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setMobile(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setUser(user);
                bean.setStatus(status);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }


}
