import java.net.*;

public class FindIP{
	public static void main(String[] args)throws UnknownHostException{
		InetAddress addr = InetAddress.getLocalHost();
    	String ip = String.valueOf(addr.getHostAddress());
		System.out.println(ip);
	}
}