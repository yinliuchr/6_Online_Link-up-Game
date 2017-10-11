/**
 * Created by liuyin14 on 2016/11/28.
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;


import javax.swing.*;


public class LianLianKan implements ActionListener {

    InetAddress addr = InetAddress.getLocalHost();
    String ip = String.valueOf(addr.getHostAddress());

    private AudioClip audioClip;
    //主面板
    JFrame mainFrame;
    //面板容器
    Container thisContainer;

    //子面板
    JPanel  southPanel, northPanel, westPanel, eastPanel;

    JPanel centerPanel = new JPanel();

    //游戏按钮数组
    JButton diamondsButton[][] = new JButton[6][10];

    //开始，退出，重列，重新开始按钮
    JButton exitButton,  newlyButton;

    //分数标签
    JLabel fractionLable = new JLabel("0");

    //时间标签
    JLabel time = new JLabel("");

    //分别记录两次被选中的按钮
    JButton fristButton, secondButton;

    //储存游戏按钮位置
    int grid[][] = new int[6][10];

    //判断是否有按钮被击中
    static boolean pressInformation = false;

    //被选中的两个游戏按钮的位置坐标(x0,y0),(x,y)
    int x0 = 0,y0 = 0,x = 0,y = 0;

    //两个被选中按钮上相应的数字
    int fristMsg = 0,secondMsg = 0;
    int i, j, k, n;

    final Timer t=new Timer();
    Color b = new Color(233, 84, 178,255);
    Color g = new Color(181, 232, 63,255);
    Color m = Color.pink;
    boolean flag;

    String friendIP;

    boolean zanting = false;

    int remainblocks = 60;

    String sendMessage;

    ListenThread listenThread = new ListenThread(this);

    JLabel linkCondition = new JLabel("尚未连接");

    LianLianKan() throws UnknownHostException {
        try {
            audioClip = Applet.newAudioClip(new URL("file:琵琶语.wav"));
            audioClip.loop();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }





        randomBuild();
        init();
//		timerDemo();

//		server.start();
//		client.start();
    }

    //初始化
    public void init() {
        listenThread.start();
        mainFrame = new JFrame("欢迎你！玩家二!");
        thisContainer = mainFrame.getContentPane();
        mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );	//关闭窗口，结束程序
        mainFrame.setResizable(false); 								//设置窗口大小为不可改变
//		thisContainer.setBackground(c);
        /**
         * 把thisContainer划分为Center、South、North三个区域
         * North区域添加分数
         * Center区域添加游戏按钮
         * South区域添加退出、重列、下一局等按钮
         */

        thisContainer.setLayout(new BorderLayout());

        centerPanel.setBackground(b);

        southPanel = new JPanel();
        southPanel.setBackground(g);
        southPanel.setLayout(new FlowLayout());

        northPanel = new JPanel(new GridLayout(2,0));
        northPanel.setBackground(m);

        westPanel = new JPanel();
        westPanel.setBackground(m);

        eastPanel = new JPanel();
        eastPanel.setBackground(m);

        thisContainer.add(centerPanel, "Center");
        thisContainer.add(southPanel, "South");
        thisContainer.add(northPanel, "North");

        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT));//设置左对齐
        JPanel panel2 = new JPanel(new GridLayout(1,4) );

        JMenuBar menubar = new JMenuBar();
        menubar.setBackground(g);

        JMenu settingMenu = new JMenu("音乐");
        settingMenu .setForeground(Color.BLUE);
        menubar.add(settingMenu);
        JMenuItem on = new JMenuItem("音乐开");
        JMenuItem off = new JMenuItem("音乐关");
        settingMenu.add(on);
        settingMenu.add(off);
        settingMenu.setFont(new Font("楷体", Font.BOLD, 20));

        JButton stopButton = new JButton("暂停/继续");
        stopButton.setFont(new Font("楷体", Font.ITALIC, 20));
        stopButton.setForeground(Color.black);

        JLabel duifangIP = new JLabel("请输入对方IP：");
        JTextField textIP	 = new JTextField(20);

        JButton setupLink = new JButton("确定");


        setupLink.addActionListener(e -> friendIP = textIP.getText());


        northPanel.add(panel1);
        panel1.setBackground(g);
        panel1.add(menubar);
        panel1.add(stopButton);
        panel1.add(duifangIP);
        panel1.add(textIP);
        panel1.add(setupLink);
        panel1.add(linkCondition);

        northPanel.add(panel2,"South");
        panel2.add(BorderLayout.WEST, new JLabel("      剩余时间:"));
        panel2.add(BorderLayout.EAST, time);
        time.setFont(new Font("宋体", Font.BOLD, 20));
        panel2.add(BorderLayout.CENTER, new JLabel("您的得分:"));
        panel2.add(BorderLayout.EAST, fractionLable);
        panel2.add(fractionLable, "Center");
        fractionLable.setFont(new Font("宋体", Font.BOLD, 20));
        panel2.setBackground(m);

        on.addActionListener(e -> audioClip.loop());
        off.addActionListener(e -> audioClip.stop());

        stopButton.addActionListener(e -> {
            zanting = !zanting;
            try {
                sendMessage("Z");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        exitButton = new JButton("退出");
        exitButton.addActionListener(this);
        newlyButton = new JButton("下一局");
        newlyButton.addActionListener(this);

        southPanel.add(exitButton);
        southPanel.add(newlyButton);
        /**
         *为该标签设置一个文本字符串
         *该字符串为标签字符串所显示的文本字符串作为有符号的十进制整数为参数的字符串
         */
        fractionLable.setText(String.valueOf(Integer.parseInt(fractionLable.getText())));
        mainFrame.setBounds(180, 10, 1400, 1400);//设置主面板的位置和大小
        mainFrame.setResizable(true);

        mainFrame.setVisible(true);
    }

    public void dispalyGirls(){
        centerPanel.removeAll();
        centerPanel.setLayout(new GridLayout(6,10));
        for(int cols = 0; cols < 6; cols ++) {
            for(int rows = 0; rows < 10; rows ++ ) {
                if(grid[cols][rows] != 0) {//给指定按钮添加图片
                    diamondsButton[cols][rows] = createImgBtn("/grid1/" + grid[cols][rows] + ".jpg", String.valueOf(grid[cols][rows]));
                }
                else {//当指定按钮为空时传空字符串即照片为传的照片为空
                    diamondsButton[cols][rows] .setVisible(false);
                }
                diamondsButton[cols][rows].addActionListener(this);
                centerPanel.add(diamondsButton[cols][rows]);
            }
        }
    }

    //创建带有图片的按钮
    public JButton createImgBtn(String ing, String txt) {//根据给定名称的资源创建一个 ImageIcon。
        ImageIcon image = new ImageIcon(getClass().getResource(ing));
        JButton button = new JButton(txt, image);
        //设置文本（即数字）相对于图标的垂直位置为底及水平位置为中心
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setHorizontalTextPosition(JButton.CENTER);
        return button;
    }

    /**
     *产生游戏中的随机数字
     *数字至少两两相同
     */
    public void randomBuild() {
        int cols, rows;
        for(int twins = 1; twins <= 15; ++ twins) {

            for(int alike = 1; alike <= 4; ++ alike) { 		//产生4个随机的坐标来存放同一个数字
                cols = (int)(Math.random() * 6 );
                rows = (int)(Math.random() * 10 );
                while(grid[cols][rows] != 0) {
                    cols = (int)(Math.random() * 6 );
                    rows = (int)(Math.random() * 10 );
                }
                this.grid[cols][rows] = twins + 30;
            }
        }
    }

    public void timerDemo() {
        t.schedule(new TimerTask(){					//创建一个新的计时器任务
            int s = 60;
            public void run() {					//此计时器任务要执行的操作
                if(flag==true) {
                    this.cancel();				//取消此计时器任务
                    flag=false;
                }
                if(s==0) {						//时间跑完,则游戏结束
                    time.setText("很遗憾时间到！");
                    try {
                        sendMessage("G");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //当时间跑完时，将未点击的按钮设为不可见
                    for(int i=0;i<6;i++)
                        for(int j=0;j<10;j++ )
                            if(grid[i][j]!=0)
                                diamondsButton[i][j].setVisible(false);

                    newlyButton.setVisible(false);
                }

                else if(!remaining()){
                    time.setText("恭喜你们成功了！");
                    try {
                        sendMessage("W");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //当时间跑完时，将未点击的按钮设为不可见
                    for(int i=0;i<6;i++)
                        for(int j=0;j<10;j++ )
                            if(grid[i][j]!=0)
                                diamondsButton[i][j].setVisible(false);

                    newlyButton.setVisible(false);
                }

                else if(zanting == true) time.setText("" + s);

                else time.setText("" + s--);

            }
        }, 1000,1000);



    }

    //计算得分
    public void fraction()
    {
        fractionLable.setText(String.valueOf(30 - remainblocks/2));
    }


    //选中按钮信息的存储与操作
    public void estimateEven(int placeX , int placeY , JButton bz) {
        if(pressInformation == false) {//如果第一个按钮未被击中，则将传来的按钮坐标赋给(x,y)
            x = placeX;
            y = placeY;
            fristMsg = grid[x][y];//将按钮上的数字赋给fristMsg
            fristButton = bz;//将(placeX，placey)位置上的按钮bz赋给fristButton
            pressInformation = true;//将按钮点击信息设置为true
        }
        else
        { /**
         *如果第一个按钮被击中 ,则将第一个按钮坐标赋给(x0,y0)
         *将fristButton按钮赋给secondButton按钮
         *将传来的按钮坐标赋给坐标（x，y）
         *如果当两个不同的按钮上的数字相等时则调用remove()函数消去
         */
            x0 = x;
            y0 = y;
            secondMsg = fristMsg;//将击中的第一个按钮上的数字赋给第二个按钮
            secondButton = fristButton;//将第一个按钮赋给第二个按钮
            x = placeX;
            y = placeY;
            fristMsg = grid[x][y];//将传过来的按钮上的数字赋给fristMsg
            fristButton = bz;//将传过来的按钮赋给fristButton
            if(fristMsg == secondMsg && secondButton != fristButton) xiao();
        }
    }

    //消去
    public void xiao() {
        if((x0 == x && (y0 == y+1 || y0 == y-1)) || ((x0 == x+1||x0 == x-1) && (y0 == y))) remove();
        else { //如果两个按钮不相邻
            //判断与第一按钮同行的情况
            for (j = 0;j < 10; j++ ) { //判断第一个按钮同行哪个按钮为空
                if (grid[x0][j] == 0) { //如果同行有空按钮
                    if (y>j) { //如果第二个按钮的y坐标大于空按钮的j坐标说明空按钮在第二按钮左边
                        for (i = y-1; i >= j; i--) { //判断第二按钮左侧直到位置(x,j)有没有按钮
                            //即判断与空按钮同列、与第二按钮同行的位置到第二按钮的左侧为止有没有按钮
                            if (grid[x][i]!=0) {//如果有按钮，则将k初始化为零，并将跳出循环
                                k=0;
                                break;
                            }
                            else k = 1; //如果没有按钮//K=1说明通过了第一次验证

                        }
                        if (k==1)
                        { //k==1说明横坐标为x,纵坐标从j到(y-1)的位置都没有按钮
                            //即说明与空按钮同列、与第二按钮同行的位置到第二按钮的左侧为止没有按钮
                            linePassOne();
                        }
                    }
                    if (y<j)
                    { //如果第二个按钮的y坐标小于空按钮的j坐标说明空按钮在第二按钮右边
                        for (i=y+1;i<=j;i++)
                        { //判断第二按钮右侧直到位置(x,j)有没有按钮
                            if (grid[x][i]!=0)
                            { //如果有按钮，则将k初始化为零，并将跳出循环
                                k=0;
                                break;
                            }
                            else
                            {//如果没有按钮
                                k=1;
                            }
                        }
                        if (k==1)
                        { //通过第一次验证，即第二按钮右侧直到位置(x,j)没有按钮
                            linePassOne();
                        }
                    }
                    if (y==j )
                    { //第二个按钮与空按钮同列，即第二按钮与第一按钮同行
                        linePassOne();
                    }
                }
                if (k==2)
                {//通过第二验证
                    if (x0==x)
                    { //两个按钮在同一行
                        remove();
                    }
                    if (x0<x)
                    { //第二个按钮所在行在第一按钮所在行的下面
                        for (n=x0;n<=x-1;n++)
                        { //判断空按钮下侧直到位置(x-1,j)有没有按钮
                            if (grid[n][j]!=0)
                            { //如果有按钮，将k初始化为零，并跳出循环
                                k=0;
                                break;
                            }
                            if(grid[n][j]==0&&n==x-1)
                            { //如果直到位置(x-1,j)没有按钮
                                remove();
                            }
                        }
                    }
                    if (x0>x)
                    { //第二个按钮所在行在第一按钮所在行的上面
                        for (n=x0;n>=x+1;n--)
                        { //判断空按钮上侧直到位置(x+1,j)有没有按钮
                            if (grid[n][j]!=0)
                            { //如果有按钮，将k初始化为零，并跳出循环
                                k=0;
                                break;
                            }
                            if(grid[n][j]==0&&n==x+1)
                            { //如果直到位置(x+1,j)没有按钮
                                remove();
                            }
                        }
                    }
                }
            }
            //判断与第一按钮同列情况
            for (i = 0;i < 6; i ++)
            { //判断第一个按钮同列哪个按钮为空
                if (grid[i][y0] == 0)
                { //同列有空按钮
                    if (x > i)
                    { //如果第二个按钮的x坐标大于空按钮的i坐标说明空按钮在第二按钮上边
                        for(j = x-1; j >= i; j --)
                        {//判断第二按钮上侧直到位置(i,y)有没有按钮
                            if (grid[j][y] != 0)
                            { //如果有按钮，将k初始化为零，并跳出循环
                                k = 0;
                                break;
                            }
                            else
                            { //如果没有按钮
                                k = 1; //说明通过第一次验证
                            }
                        }
                        if (k == 1)
                        { //第二按钮上侧直到位置(i,y)没有按钮
                            rowPassOne();
                        }
                    }
                    if (x<i)
                    { //空按钮在第二按钮下边
                        for (j=x+1;j<=i;j++)
                        { //判断第二按钮下侧直到位置(i,y)有没有按钮
                            if (grid[j][y]!=0)
                            {
                                k=0;
                                break;
                            }
                            else
                            {
                                k=1;
                            }
                        }
                        if (k==1)
                        { //第二按钮下侧直到位置(i,y)没有按钮
                            rowPassOne();
                        }
                    }
                    if (x==i)
                    { //第二按钮与空按钮同行
                        rowPassOne();
                    }
                }
                if (k==2)
                { //通过第二次验证
                    if (y0==y)
                    { //两个按钮同列
                        remove();
                    }
                    if (y0<y)
                    { //第二按钮所在行在第一按钮所在行的下面
                        for (n=y0;n<=y-1;n++)
                        { //判断空按钮右侧直到位置(i,y-1)有没有按钮
                            if (grid[i][n]!=0)
                            { //如果有按钮，将k初始化为零，并跳出循环
                                k=0;
                                break;
                            }
                            if(grid[i][n]==0&&n==y-1)
                            { //空按钮右侧直到位置(i,y-1)没有按钮
                                remove();
                            }
                        }
                    }
                    if (y0>y)
                    {  //第二按钮所在行在第一按钮所在行的上面
                        for (n=y0;n>=y+1;n--)
                        { //判断空按钮左侧直到位置(i,y+1)有没有按钮
                            if (grid[i][n]!=0)
                            {
                                k=0;
                                break;
                            }
                            if(grid[i][n]==0&&n==y+1)
                            { //空按钮左侧直到位置(i,y+1)没有按钮
                                remove();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 第一按钮的同行中存在空按钮
     * 判断在同一行中空按钮与第一个按钮之间的位置是否有按钮存在，如果有则k=0,否则k=2
     * */
    public void linePassOne() {
        if (y0>j)
        { //第一按钮在同行空按钮的右边
            for (i=y0-1;i>=j;i--)
            { //判断第一按钮同左侧空按钮之间有没按钮
                if (grid[x0][i]!=0)
                { //如果有按钮，将k初始化为零，并跳出循环
                    k=0;
                    break;
                }
                else
                {//如果没有按钮
                    k=2;//K=2说明通过了第二次验证
                }
            }
        }
        if (y0<j)
        { //第一按钮在同行空按钮的左边
            for (i=y0+1;i<=j;i++)
            { //判断第一按钮同右侧空按钮之间有没按钮
                if (grid[x0][i]!=0)
                { //如果有按钮，将k初始化为零，并跳出循环
                    k=0;
                    break;
                }
                else
                {
                    k=2;
                }
            }
        }
    }

    /**
     * 第一按钮的同列中存在空按钮
     * 判断在同一列中空按钮与第一个按钮之间的位置是否有按钮存在，如果有则k=0,否则k=2
     * */
    public void rowPassOne() {
        if (x0>i)
        { //第一按钮在同列空按钮的下边
            for (j=x0-1;j>=i;j--)
            { //判断第一按钮同上侧空按钮之间有没按钮
                if (grid[j][y0]!=0)
                { //如果有按钮，将k初始化为零，并跳出循环
                    k=0;
                    break;
                }
                else
                {//如果没有按钮
                    k=2;//K=2说明通过了第二次验证
                }
            }
        }
        if (x0<i)
        { //第一按钮在同列空按钮的上边
            for (j=x0+1;j<=i;j++)
            {  //判断第一按钮同下侧空按钮之间有没按钮
                if (grid[j][y0]!=0)
                {
                    k=0;
                    break;
                }
                else
                {
                    k=2;
                }
            }
        }
    }

    //将相同两个按钮消去，即设为不可见
    public void remove() {
        fristButton.setVisible(false);
        secondButton.setVisible(false);


//        resetButton.setVisible(false);
//        newlyButton.setVisible(false);
        /**
         * 将点击按钮信息归为初始
         * 将K和被消去的两个按钮的坐标初始为零
         */
        pressInformation=false;
        k=0;
        grid[x0][y0]=0;
        grid[x][y]=0;
        remainblocks = 60;
        for(int i=0;i<6;i++)
            for(int j=0;j<10;j++ )
                if(grid[i][j] == 0) -- remainblocks;
        fraction();
    }



    //实现事件监听
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newlyButton) {//点击下一局按钮事件
            this.grid = new int[6][10];
            this.randomBuild();

            String remsg = "A";
            for(int i = 0; i < 6 ; ++ i){
                for(int j = 0; j < 10; ++ j){
                    String temp = String.valueOf(grid[i][j]);
                    remsg = remsg + " " + temp;
                }
            }
            try {
                sendMessage(remsg);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            dispalyGirls();
            timerDemo();
        }

        if(e.getSource() == exitButton) {
            try {
                sendMessage("E");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            audioClip.stop();
            System.exit(0);
        }

        for(int cols = 0; cols < 6; cols++) {
            for(int rows = 0;rows < 10;rows++ ) {
                diamondsButton[cols][rows].setBorder(null);
                if(e.getSource()==diamondsButton[cols][rows]) {
                    diamondsButton[cols][rows].setBorder(BorderFactory.createRaisedBevelBorder());
                    try {
                        sendMessage("S-" + String.valueOf(cols) + "-" + String.valueOf(rows));
                    } catch (IOException e1) {
                        System.out.println("发送失败，请检查是否设置了正确的IP地址。");
                    }
                    estimateEven(cols, rows, diamondsButton[cols][rows]);
                }
            }
        }
    }

    public boolean remaining(){
        for(int i=0;i<6;i++)
            for(int j=0;j<10;j++ )
                if(grid[i][j]!=0) return true;
        return false;
    }



    // 如果接收到对方玩家的信息，进行下面的处理
    public void receiveMessage(String msg) throws IOException {
        if(msg.equals(ip)) try {
            sendMessage(friendIP);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        if(msg.charAt(0) == 'A'){
            String[] arr = msg.split(" ");
            int count = 1;
            for(int i = 0; i < 6; ++ i){
                for(int j = 0; j < 10; ++ j){
                    grid[i][j] = Integer.parseInt(arr[count]);
                    count ++;
                }
            }
            this.dispalyGirls();
            this.timerDemo();
            linkCondition.setText("成功建立连接！开始游戏！");
        }

        if(msg.charAt(0) == 'R'){
            String[] arr = msg.split(" ");
            int count = 1;
            for(int i = 0; i < 6; ++ i){
                for(int j = 0; j < 10; ++ j){
                    grid[i][j] = Integer.parseInt(arr[count]);
                    count ++;
                }
            }
            this.dispalyGirls();
        }

        switch (msg.charAt(0)){
            case 'E':{
                audioClip.stop();
                System.exit(0);
            }

            case 'S': {
                String[] arr = msg.split("-");
                int cols = Integer.parseInt(arr[1]);
                int rows = Integer.parseInt(arr[2]);
                for(int i = 0; i < 6; ++ i){
                    for(int j = 0; j < 10; ++ j){
                        diamondsButton[i][j].setBorder(null);
                    }
                }
                diamondsButton[cols][rows].setBorder(BorderFactory.createRaisedBevelBorder());
                estimateEven(cols, rows, diamondsButton[cols][rows]);
            }
            case 'Z': zanting = !zanting;

//			case 'G': {
//				time.setText("很遗憾时间到！");
//				for(int i=0;i<6;i++)
//					for(int j=0;j<10;j++ )
//						if(grid[i][j]!=0)
//							diamondsButton[i][j].setVisible(false);
//
//				newlyButton.setVisible(false);
//			}
//
//			case 'W': {
//				time.setText("恭喜你们成功了！");
//				for(int i=0;i<6;i++)
//					for(int j=0;j<10;j++ )
//						if(grid[i][j]!=0)
//							diamondsButton[i][j].setVisible(false);
//
//				newlyButton.setVisible(false);
//			}

        }
    }

    // 向对方主机发送内容
    public void sendMessage(String msg) throws IOException {
        System.out.println(friendIP);
        byte[] sendData = msg.getBytes();
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(friendIP);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1234);
        clientSocket.send(sendPacket);
        clientSocket.close();
    }
}

