package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.bean.PropertyValue;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-27 15:16
 **/
public class ProductServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        Product p = new Product();

        p.setName(name);
        p.setCategory(c);
        p.setSubTitle(subTitle);
        p.setOriginalPrice(originalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);

        productDAO.add(p);

        return "@admin_product_list?cid="+cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.get(id);
        int cid = p.getCategory().getId();
        productDAO.delete(id);
        return "@admin_product_list?cid="+cid;
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.get(id);

        request.setAttribute("p",p);
        return "admin/editCategory.jsp?id="+id;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int cid = Integer.getInteger(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);

        Product p = new Product();

        p.setId(id);
        p.setName(name);
        p.setSubTitle(subTitle);
        p.setOriginalPrice(originalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);
        p.setCategory(c);

        productDAO.update(p);
        return "admin_product_list?cid="+cid;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        List<Product> ps = productDAO.list(cid,page.getStart(),page.getCount());
        int total = productDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("?cid="+cid);
        request.setAttribute("ps","ps");
        request.setAttribute("c",c);
        request.setAttribute("page",page);
        return "admin/listProduct.jsp?cid="+cid;
    }

    public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page){
        int pid = Integer.parseInt(request.getParameter("id"));
        Product p = productDAO.get(pid);
        propertyValueDAO.init(p);
        List<PropertyValue> pvs = propertyValueDAO.list(pid);

        request.setAttribute("p",p);
        request.setAttribute("pvs",pvs);

        return "admin/editPropertyValue.jsp";
    }

    public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page){
        String value = request.getParameter("value");
        int pvid = Integer.parseInt(request.getParameter("pvid"));
        PropertyValue pv = propertyValueDAO.get(pvid);

        pv.setId(pvid);
        pv.setValue(value);
        propertyValueDAO.update(pv);

        return "%success";

    }
}
