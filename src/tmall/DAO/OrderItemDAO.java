package tmall.DAO;

import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-20 12:29
 **/
public class OrderItemDAO {
    public int getTotal(){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT COUNT(*) FROM orderitem";

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(OrderItem bean){
        String sql = "INSERT INTO orderitem VALUES(NULL,?,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,bean.getProduct().getId());
            if (null == bean.getOrder()){
                ps.setInt(2,-1);
            }else {
                ps.setInt(2,bean.getOrder().getId());
            }
            ps.setInt(3,bean.getUser().getId());
            ps.setInt(4,bean.getNumber());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()){
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(OrderItem bean){
        String sql = "UPDATE orderitem SET pid=?,oid=?,uid=?,number=? WHERE id=?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,bean.getProduct().getId());
            if (null == bean.getOrder())
                ps.setInt(2,-1);
            else
                ps.setInt(2,bean.getOrder().getId());
            ps.setInt(3,bean.getUser().getId());
            ps.setInt(4,bean.getNumber());
            ps.setInt(5,bean.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try(Connection c = DBUtil.getConnection();Statement s = c.createStatement();) {
            String sql = "DELETE FROM orderitem WHERE id="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrderItem get(int id){
        OrderItem bean = new OrderItem();

        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT * FROM orderitem WHERE id="+id;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);

                bean.setId(id);
                bean.setProduct(product);
                bean.setUser(user);
                if (-1 != oid){
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                bean.setNumber(number);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<OrderItem> listByUser(int uid){
        return listByUser(uid,0,Short.MAX_VALUE);
    }

    public List<OrderItem> listByUser(int uid, int start, int count){
        List<OrderItem> beans = new ArrayList<>();
        String sql = "SELECT * FROM orderitem WHERE uid=? ORDER BY id desc LIMIT ?,?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,uid);
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                OrderItem bean = new OrderItem();
                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);

                bean.setId(id);
                bean.setProduct(product);
                if (-1 != oid){
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                bean.setUser(user);
                bean.setNumber(number);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<OrderItem> listByOrder(int oid){
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByOrder(int oid, int start, int count){
        List<OrderItem> beans = new ArrayList<>();
        String sql = "SELECT * FROM orderitem WHERE oid=? ORDER BY id desc LIMIT ?,?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,oid);
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                OrderItem bean = new OrderItem();
                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);

                bean.setId(id);
                bean.setProduct(product);
                if (-1 != oid){
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                bean.setUser(user);
                bean.setNumber(number);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    public List<OrderItem> listByProduct(int pid){
        return listByProduct(pid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByProduct(int pid, int start, int count){
        List<OrderItem> beans = new ArrayList<>();
        String sql = "SELECT * FROM orderitem WHERE pid=? ORDER BY id desc LIMIT ?,?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1,pid);
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                OrderItem bean = new OrderItem();
                int id = rs.getInt("id");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);

                bean.setId(id);
                bean.setProduct(product);
                if (-1 != oid){
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                bean.setUser(user);
                bean.setNumber(number);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public int getSaleCount(int pid){
        int total = 0;
        try(Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "SELECT SUM(number) FROM orderitem WHERE pid="+pid;

            ResultSet rs = s.executeQuery(sql);

            if (rs.next()){
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void fill(List<Order> os){
        for (Order o:os){
            List<OrderItem> ois = listByOrder(o.getId());
            float total = 0;
            int totalNumber = 0;
            for (OrderItem oi:ois){
                total += oi.getNumber()*oi.getProduct().getPromotePrice();
                totalNumber += oi.getNumber();
            }
            o.setTotal(total);
            o.setTotalNumber(totalNumber);
            o.setOrderItems(ois);
        }
    }

    public void fill(Order o){
        List<OrderItem> ois = listByOrder(o.getId());
        float total = 0;

        for (OrderItem oi:ois){
            total += oi.getNumber()*oi.getProduct().getPromotePrice();
        }
        o.setTotal(total);
        o.setOrderItems(ois);
    }

}
