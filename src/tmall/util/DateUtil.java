package tmall.util;

/**
 * @program: tmall
 * @description:
 * @author: Mr.Longyy
 * @create: 2018-05-16 23:35
 **/
public class DateUtil {
    public static java.sql.Timestamp d2t(java.util.Date d){
        if (null == d){
            return null;
        }
        return new java.sql.Timestamp(d.getTime());
    }

    public static java.util.Date t2d(java.sql.Timestamp t){
        if (null == t){
            return null;
        }
        return new java.util.Date(t.getTime());
    }
}
