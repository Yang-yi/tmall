package tmall.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import tmall.DAO.*;
import tmall.util.Page;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-22 08:16
 **/
public abstract class BaseBackServlet extends HttpServlet {
    public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);
    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);

    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    public void service(HttpServletRequest request, HttpServletResponse response){
        try{
            int start = 0;
            int count = 5;
            try{
                start = Integer.parseInt(request.getParameter("page.start"));
            }catch (Exception e){

            }
            try {
                count = Integer.parseInt(request.getParameter("page.count"));
            }catch (Exception e){

            }

            Page page = new Page(start,count);

//            借助反射，调用对应的方法
            String method = (String) request.getAttribute("method");
//            System.out.println(method);
            Method m = this.getClass().getMethod(method,HttpServletRequest.class,HttpServletResponse.class,Page.class);
            String redirect = m.invoke(this,request,response,page).toString();

            if (redirect.startsWith("@")){
                response.sendRedirect(redirect.substring(1));
            }
            else if (redirect.startsWith("%")){
                response.getWriter().print(redirect.substring(1));
            }
            else {
                request.getRequestDispatcher(redirect).forward(request,response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public InputStream parseUpload(HttpServletRequest request, Map<String, String> params){
        InputStream is = null;
        try{
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            //设置文件上传大小为10M
            factory.setSizeThreshold(1024*1024);

            List items = upload.parseRequest(request);
            Iterator iterator = items.iterator();

            while (iterator.hasNext()){
                FileItem fileItem = (FileItem) iterator.next();
                if (!fileItem.isFormField()){
                    is = fileItem.getInputStream();
                }else {
                    String paramName = fileItem.getFieldName();
                    String paramValue = fileItem.getString();
                    paramValue = new String(paramValue.getBytes("ISO-8859-1"),"UTF-8");
                    params.put(paramName,paramValue);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

}
