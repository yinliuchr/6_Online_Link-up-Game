/**
 * Created by liuyin14 on 2016/11/28.
 */
import java.net.UnknownHostException;

/**
 * Created by liuyin14 on 2016/11/27.
 */
public class LinkGame {
    public static LianLianKan llk;
    public static void main(String[] args) throws UnknownHostException {
        llk = new LianLianKan();
        System.out.println(llk.ip);
    }
}