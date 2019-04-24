package com.yychatclient.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.yychatclient.model.*;
import com.yychatclient.view.*;

public class ClientReceiverThread extends Thread{

	private Socket s;
	public ClientReceiverThread(Socket s) {
		this.s=s;
	}
	
	public void run() {
		ObjectInputStream ois;
		while(true){
			try{
				ois=new ObjectInputStream(s.getInputStream());
				Message mess=(Message)ois.readObject();//����������Ϣ
				String showMessage=mess.getSender()+"��"+mess.getReceiver()+"˵:\r\n"+mess.getContent();
				if (mess.getMessageType().equals(MessageType.message_Common)) {
					
					System.out.println(showMessage);
					//jta.append(showMessage+"\r\n");
				
					FriendChat1 friendChat1=(FriendChat1)FriendList.hmFriendChat1.get(mess.getReceiver()+"to"+mess.getSender());
				
					friendChat1.appendJta(showMessage);
				}
				
				if (mess.getMessageType().equals(MessageType.message_OnlineFriend)) {
					System.out.println("���ߺ���" + mess.getContent());
					System.out.println(mess.getReceiver());
					FriendList friendList = (FriendList)ClientLogin.hmFriendList.get(mess.getReceiver());
					
					
					friendList.setEnableFriendIcon(mess.getContent());
				}
				
				
				
				
				
			}catch (IOException | ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		

	}

}