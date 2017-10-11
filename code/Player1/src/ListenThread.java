/**
 * Created by liuyin14 on 2016/11/28.
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by liuyin14 on 2016/11/27.
 */
public class ListenThread extends Thread {
    LianLianKan llk;
    public ListenThread(LianLianKan a) {
        llk = a;
    }
    public void run() {
        try {
            DatagramSocket clientSocket = new DatagramSocket(1234);
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

//                InetAddress jbn = receivePacket.getAddress();
//                friendIP = String.valueOf(jbn);

                System.out.println("Before");
                clientSocket.receive(receivePacket);
                System.out.println("After");
                String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                llk.receiveMessage(modifiedSentence);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

