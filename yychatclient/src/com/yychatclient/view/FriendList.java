package com.yychatclient.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.*;

import com.yychatclient.control.ClientConnect;
import com.yychatclient.model.Message;
import com.yychatclient.model.MessageType;

public class FriendList extends JFrame implements ActionListener,MouseListener{//顶层容器
	
	public static HashMap hmFriendChat1=new HashMap<String,FriendChat1>();//键值对
	CardLayout cardLayout;//卡片布局
	
	JPanel myFriendPanel;
	JButton myFriendJButton;
	
	JScrollPane myFriendScrollPane,myStrangerScrollPane,BlackListScrollpane;
	JPanel myFriendListJPanel,myStrangerListJPanel,BlackListJpanel;
	static final int FRIENDCOUNT=51;
	static final int STRANGER=21;
	static final int BLACK=101;
	JLabel[] myFriendJLabel=new JLabel[FRIENDCOUNT];//对象数组
	JLabel[] myStrangerJLabel=new JLabel[STRANGER];
	JLabel[] BlackListJLabel=new JLabel[BLACK];
	
	JPanel myStrangerBlackListJPanel;
	JButton myStrangerJButton;
	JButton blackListJButton;
	
	JPanel myStrangerPanel;
	
	JPanel myFriendStrangerPanel;
	JButton myFriendJButton1;
	JButton myStrangerJButton1;
	JButton addFriendJButton;
	JPanel addFriendJPanel;
	
	JButton blackListJButton1;
	
	JPanel BlackList;
	JPanel blacklist;
	JPanel fsb;
	JButton friend;
	JButton stranger;
	JButton black;
	String userName;
	
	public FriendList(String userName, String friendString){
		this.userName=userName;
		this.setTitle(userName);
		//第一张卡片
		myFriendPanel=new JPanel(new BorderLayout());//边界布局
		addFriendJButton = new JButton("添加沙雕网友");
		addFriendJButton.addActionListener(this);
		myFriendJButton=new JButton("我的沙雕网友");
		addFriendJPanel = new JPanel(new GridLayout(2,1));
		addFriendJPanel.add(addFriendJButton);
		addFriendJPanel.add(myFriendJButton);
		myFriendPanel.add(addFriendJPanel,"North");
		//中部
		myFriendListJPanel=new JPanel();
		updateFriendIcon(friendString);
		//myFriendJLabel[Integer.parseInt(userName)].setEnabled(true);
		myFriendScrollPane=new JScrollPane(myFriendListJPanel);
		myFriendPanel.add(myFriendScrollPane);
		
		
		myStrangerBlackListJPanel=new JPanel(new GridLayout(2,1));//网络布局
		myStrangerJButton=new JButton("我不认识的沙雕网友");
		//添加事件监听器
		myStrangerJButton.addActionListener(this);
		
		blackListJButton=new JButton("我不想认识的沙雕网友");
		blackListJButton.addActionListener(this);
		myStrangerBlackListJPanel.add(myStrangerJButton);
		myStrangerBlackListJPanel.add(blackListJButton);
		myFriendPanel.add(myStrangerBlackListJPanel,"South");
		
		//第二张卡片
		myStrangerPanel = new JPanel(new BorderLayout());
		
		myFriendStrangerPanel=new JPanel(new GridLayout(2,1));
		
	
		myFriendJButton1=new JButton("我的沙雕网友");//添加监听器
		myFriendJButton1.addActionListener(this);
		myStrangerJButton1=new JButton("我不认识的沙雕网友");
		myFriendStrangerPanel.add(myFriendJButton1);
		myFriendStrangerPanel.add(myStrangerJButton1);
		myStrangerPanel.add(myFriendStrangerPanel,"North");
		
		myStrangerListJPanel=new JPanel(new GridLayout(STRANGER-1,1));
		for(int i=1;i<STRANGER;i++)
		{
			myStrangerJLabel[i]=new JLabel(i+"",new ImageIcon("images/yy1.jpg"),JLabel.LEFT);//"2"
			myStrangerJLabel[i].addMouseListener(this);
			myStrangerListJPanel.add(myStrangerJLabel[i]);
		}
		myStrangerScrollPane=new JScrollPane(myStrangerListJPanel);
		myStrangerPanel.add(myStrangerScrollPane);
		
		
		blackListJButton1=new JButton("我不想认识的沙雕网友");
		blackListJButton1.addActionListener(this);
		myStrangerPanel.add(blackListJButton1,"South");
		
		
		
		//第三张卡
		BlackList=new JPanel(new BorderLayout());
		fsb=new JPanel(new GridLayout(3,1));//网络布局
		friend=new JButton("我的沙雕网友");
		friend.addActionListener(this);
		stranger=new JButton("我不认识的沙雕网友");
		stranger.addActionListener(this);
		black=new JButton("我不想认识的沙雕网友");
		fsb.add(friend);
		fsb.add(stranger);
		fsb.add(black);
		BlackList.add(fsb,"North");
		blacklist=new JPanel(new GridLayout(BLACK-1,1));
		for(int i=1;i<BLACK;i++)
		{
			BlackListJLabel[i]=new JLabel(i+"",new ImageIcon("images/dhbh.jpg"),JLabel.LEFT);//"2"
			BlackListJLabel[i].addMouseListener(this);
			blacklist.add(BlackListJLabel[i]);
		}
		BlackListScrollpane=new JScrollPane(blacklist);
		BlackList.add(BlackListScrollpane);
		
		
		
		cardLayout=new CardLayout();
		this.setLayout(cardLayout);
		this.add(myFriendPanel,"1");
		this.add(myStrangerPanel,"2");
		this.add(BlackList,"3");
		
		this.setSize(240,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	
	public static void main(String[] args){
		//FriendList friendList=new FriendList();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent argo){
		if(argo.getSource()==myStrangerJButton){
			cardLayout.show(this.getContentPane(), "2");
		}
		if(argo.getSource()==myFriendJButton1){
			cardLayout.show(this.getContentPane(), "1");
			
		}
		if(argo.getSource()==blackListJButton){
			cardLayout.show(this.getContentPane(), "3");
		}
		if(argo.getSource()==blackListJButton1){
			cardLayout.show(this.getContentPane(), "3");
		}
		if(argo.getSource()==friend){
			cardLayout.show(this.getContentPane(), "1");
		}
		if(argo.getSource()==stranger){
			cardLayout.show(this.getContentPane(), "2");
		}
		
		if (argo.getSource() == addFriendJButton) {
			String addFriendName = JOptionPane.showInputDialog(null,"请输入你想一起沙雕的沙雕网友的名字：","结识沙雕网友",JOptionPane.DEFAULT_OPTION);
			Message mess = new Message();
			mess.setSender(userName);
			mess.setReceiver("Server");
			mess.setContent(addFriendName);
			mess.setMessageType(MessageType.message_RequestAddFriend);
			
			Socket s = (Socket)ClientConnect.hmSocket.get(userName);
			ObjectOutputStream oos;
			
			try {
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(mess);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getClickCount()==2){
			JLabel jlbl=(JLabel)arg0.getSource();
			String receiver =jlbl.getText();
			//new FriendChatClient(this.userName,receiver);
			//new Thread(new FriendChat(this.userName,receiver)).start();
			
			FriendChat1 friendChat1=(FriendChat1)hmFriendChat1.get(userName+"to"+receiver);
			if(friendChat1==null){
			friendChat1=new FriendChat1(this.userName,receiver);//friendChat是一个变量
			hmFriendChat1.put(userName+"to"+receiver,friendChat1);//保存对象到HashMap中
			}else{
				friendChat1.setVisible(true);
			}
			
		}
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		JLabel jLabel=(JLabel)arg0.getSource();
		jLabel.setForeground(Color.red);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		JLabel jLabel=(JLabel)arg0.getSource();
		jLabel.setForeground(Color.black);
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void setEnableFriendIcon(String friendString) {
		// TODO Auto-generated method stub
		//friendString = friendString.trim();
		String[] friendName = friendString.split(" ");
		int count = friendName.length;
		for (int i=0;i<count; i++) {
			myFriendJLabel[Integer.parseInt(friendName[i])].setEnabled(true);
		}
		
	}
	
	public void setEnableNewFriendIcon(String friendName) {
		this.myFriendJLabel[Integer.parseInt(friendName)].setEnabled(true);
	}


	public void updateFriendIcon(String friendString) {
		myFriendListJPanel.removeAll();
		String[] friendsName = friendString.trim().split(" ");
		int count = friendsName.length;
		myFriendListJPanel.setLayout(new GridLayout(count,1));
		for(int i=0;i<count;i++)
		{
			myFriendJLabel[i]=new JLabel(friendsName[i],new ImageIcon("images/house.png"),JLabel.LEFT);//"1"
			//myFriendJLabel[i].setEnabled(false);
			
			myFriendJLabel[i].addMouseListener(this);
			myFriendListJPanel.add(myFriendJLabel[i]);
		}
		
	}
}