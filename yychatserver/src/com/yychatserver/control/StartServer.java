package com.yychatserver.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.yychatclient.model.*;

public class StartServer {
	public static HashMap hmSocket=new HashMap<String,Socket>();
	
	ServerSocket ss;
	String userName;
	String passWord;
	Message mess;
	public StartServer() {
		
		try {
			ss=new ServerSocket(3456);
			System.out.println("服务器已经启动，监听3456端口");
			while(true){
			
			Socket s=ss.accept();
			System.out.println("连接成功:"+s);
		
		ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
		User user=(User)ois.readObject();
		userName=user.getUserName();
		passWord=user.getPassWord();
		System.out.println(user.getUserName());
		System.out.println(user.getPassWord());
		
		//密码验证功能
		mess=new Message();
		mess.setSender("Server");
		mess.setReceiver(userName);
		
		System.out.println("mysqlDB（8.0.15）驱动加载中：com.mysql.cj.jdbc.Driver");
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("mysqlDB（8.0.15）驱动加载成功");
		
		String url = "jdbc:mysql://localhost:3306/yychat?useSSL=FALSE&serverTimezone=UTC";
		String dbUser = "root";
		String dbPassword = "lierlulu00388239";
		
		System.out.println("数据库（yychat）连接请求中...");
		Connection conn = DriverManager.getConnection(url,dbUser,dbPassword);
		System.out.println("数据库（yychat）连接成功");
		
		
		String user_login_sql = "select * from user where username=? and password=?;";
		System.out.println("与数据库交互中...\r\n" + user_login_sql);
		PreparedStatement psmt = conn.prepareStatement(user_login_sql);
		psmt.setString(1,userName);
		psmt.setString(2,passWord);
		
		System.out.println("数据库返回数据中...");
		ResultSet rs = psmt.executeQuery();
		
		boolean loginSuccess = rs.next();
		
		
		
		if(loginSuccess){
			//告诉客户端密码验证通过,可以创建Message类
		mess.setMessageType("Message.message_LoginSuccess");//1为验证通过
		
		String friend_Relation_Sql = "select slaveuser from relation where majoruser = ? and relation_type = '1'";
		psmt = conn.prepareStatement(friend_Relation_Sql);
		psmt.setString(1, userName);
		rs = psmt.executeQuery();
		
		String friendString = new String();
		while (rs.next()) {
			friendString += rs.getString("slaveuser") + " ";
		}
		
		mess.setContent(friendString);
		System.out.println(userName + "的好友列表：" + friendString);
		
		}else{	
			
			mess.setMessageType("Message.message_LoginFailure");//0为验证不通过
			
		}
		sendMessage(s, mess);
		
		
		
		if(loginSuccess){
			
			
			mess.setMessageType(MessageType.message_NewOnlineFriend);
			mess.setSender("Server");
			mess.setContent(userName);
			
			Set onlineFriendSet = hmSocket.keySet();
			Iterator it = onlineFriendSet.iterator();
			String friendName;
			
			while (it.hasNext()) {
				friendName = (String)it.next();
				mess.setReceiver(friendName);
				
				Socket s1 = (Socket)hmSocket.get(friendName);
				sendMessage(s1,mess);
			}
			hmSocket.put(userName,s);
			
			new ServerReceiverThread(s).start();
			
			}
	
			}
		
		} catch (IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Socket s, Message mess) throws IOException {
		ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(mess);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}


