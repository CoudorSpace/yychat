package com.yychatclient.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import com.yychatclient.model.*;

public class ClientConnect {
	
	 
//public static Socket s;//静态成员变量，类变量
	public  Socket s;
	public static HashMap hmSocket=new HashMap<String,Socket>();
	public ClientConnect() {
		try {
			s=new Socket("127.0.0.1",3456);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean loginValidate(User user){
		boolean loginSuccess=false;
		ObjectOutputStream oos;
		ObjectInputStream ois;
		Message mess= null;
		try {
			oos =new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(user);
			
			
			
			//接收验证通过的mess
			 ois=new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();
			
			if(mess.getMessageType().equals("Message.message_LoginSuccess")){
				loginSuccess=true;
				System.out.println(user.getUserName()+"登陆成功");
				hmSocket.put(user.getUserName(),s);
				new ClientReceiverThread(s).start();
			}
			
		} catch (IOException|ClassNotFoundException e) {
			e.printStackTrace();
		}
		return loginSuccess;
		
	}
}