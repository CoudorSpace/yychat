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
import com.yychatserver.control.*;

public class StartServer {
	public static HashMap hmSocket=new HashMap<String,Socket>();
	
	ServerSocket ss;
	String userName;
	String passWord;
	Message mess;
	public StartServer() {
		
		try {
			ss=new ServerSocket(3456);
			System.out.println("�������Ѿ�����������3456�˿�");
			while(true){
			
			Socket s=ss.accept();
			System.out.println("���ӳɹ�:"+s);
		
		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		User user = (User)ois.readObject();
		userName = user.getUserName();
		passWord = user.getPassWord();
		System.out.println(user.getUserName());
		System.out.println(user.getPassWord());
		
		if (user.getUserMessageType().equals("USER_REGISTER")) {
			boolean canRegister = YychatDbUtil.seekUser(userName);
			System.out.println("canRegister == " + canRegister);
			Message mess = new Message();
			mess.setReceiver("Client");
			mess.setSender("Server");
			if (canRegister) {
				YychatDbUtil.addUser(user);
				mess.setMessageType(MessageType.message_RegisterSuccess);
			} else {
				mess.setMessageType(MessageType.message_RegisterFaliure);
			}
			
			//mess.setMessageType(MessageType.message_RegisterSuccess);
			
			
			sendMessage(s, mess);
		}
		
		if (user.getUserMessageType().equals("USER_LOGIN")) {
			mess=new Message();
			mess.setSender("Server");
			mess.setReceiver(userName);
			
			
			boolean loginSuccess = YychatDbUtil.loginValidate(userName, passWord);
			
			if(loginSuccess){
				//���߿ͻ���������֤ͨ��,���Դ���Message��
			mess.setMessageType(Message.message_LoginSuccess);//1Ϊ��֤ͨ��
			

			String friendString = YychatDbUtil.getFriendString(userName);
			
			mess.setContent(friendString);
			System.out.println(userName + "�ĺ����б�" + friendString);
			
			}else{	
				
				mess.setMessageType(Message.message_LoginFailure);//0Ϊ��֤��ͨ��
				
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
			

		}
		
		//������֤����
		} catch (IOException | ClassNotFoundException e) {
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


