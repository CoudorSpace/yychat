package com.yychatserver.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.yychatclient.model.*;

public class StartServer {
	public static HashMap hmSocket=new HashMap<String,Socket>();
	
	ServerSocket ss;
	String userName;
	String passWord;
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
		Message mess=new Message();
		mess.setSender("Server");
		mess.setReceiver(userName);
		if(user.getPassWord().equals("123456")){
			//告诉客户端密码验证通过,可以创建Message类
		mess.setMessageType("Message.message_LoginSuccess");//1为验证通过
		
		}else{	
			
			mess.setMessageType("Message.message_LoginFailure");//0为验证不通过
			
		}
		ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(mess);
		if(user.getPassWord().equals("123456")){
			
			hmSocket.put(userName,s);
			new ServerReceiverThread(s).start();
			
			}
	
			}
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}


