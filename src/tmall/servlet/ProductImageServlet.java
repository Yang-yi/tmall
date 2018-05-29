package tmall.servlet;

import tmall.DAO.ProductImageDAO;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.ImageUtil;
import tmall.util.Page;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-27 17:19
 **/
public class ProductImageServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        InputStream is = null;
        Map<String, String> params = new HashMap<>();
        is = parseUpload(request,params);

        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        String type = request.getParameter("type");

        ProductImage pi = new ProductImage();

        pi.setProduct(p);
        pi.setType(type);
        productImageDAO.add(pi);

        //生成文件
        String fileName = pi.getId()+".jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;
        if (ProductImageDAO.type_single.equals(type)){
            imageFolder = request.getSession().getServletContext().getRealPath("img/productSingle");
            imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");
        }else
            imageFolder = request.getSession().getServletContext().getRealPath("img/productDetail");

        File f = new File(imageFolder,fileName);
        f.getParentFile().mkdirs();

        try{
            if (null != is && 0 != is.available()){
                try(FileOutputStream fos = new FileOutputStream(f)){
                    byte[] b = new byte[1024*1024];
                    int length = is.read(b);
                    while (-1 != length){
                        fos.write(b,0,length);
                    }
                    fos.flush();

                    //保存为jpg格式
                    BufferedImage bi = ImageUtil.change2jpg(f);
                    ImageIO.write(bi,"jpg",f);

                    if (ProductImageDAO.type_single.equals(type)){
                        File f_small = new File(imageFolder_small,fileName);
                        File f_middle = new File(imageFolder_middle,fileName);

                        ImageUtil.resizeImage(f,56,56,f_small);
                        ImageUtil.resizeImage(f,217,190,f_middle);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "@admin_productImage_list?pid="+pid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductImage pi = productImageDAO.get(id);
        String type = pi.getType();
        productImageDAO.delete(id);

        if (ProductImageDAO.type_single.equals(type)){
            String imageFolder_single = request.getSession().getServletContext().getRealPath("img/productSingle");
            String imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");

            File f_single = new File(imageFolder_single,pi.getId()+".jpg");
            f_single.delete();
            File f_small = new File(imageFolder_small,pi.getId()+".jpg");
            f_small.delete();
            File f_middle = new File(imageFolder_middle,pi.getId()+".jpg");
            f_middle.delete();
        }else {
            String imageFolder_detail = request.getSession().getServletContext().getRealPath("img/productDetail");
            File f_detail = new File(imageFolder_detail,pi.getId()+".jpg");
            f_detail.delete();
        }
        return "@admin_productImage_list?pid="+pi.getProduct().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {

        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        List<ProductImage> pisSingle = productImageDAO.list(p, ProductImageDAO.type_single);
        List<ProductImage> pisDetail = productImageDAO.list(p, ProductImageDAO.type_detail);

        request.setAttribute("p",p);
        request.setAttribute("pisSingle",pisSingle);
        request.setAttribute("pisDetail",pisDetail);
        return "admin/listProductImage.jsp";
    }
}
