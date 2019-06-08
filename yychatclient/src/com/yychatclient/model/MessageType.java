package com.yychatclient.model;

public interface MessageType {
	
	
	String message_LoginFailure="0";//×Ö·û´®³£Á¿
	String message_LoginSuccess="1";
	String message_Common="2";
	String message_RequestOnlineFriend="3";
	String message_OnlineFriend="4";
	String message_NewOnlineFriend = "5";
	String message_RegisterSuccess = "6";
	String message_RegisterFaliure = "7";
	String message_RequestAddFriend = "8";
	String message_AddFriendSuccess = "9";
	String message_AddFriendFailure_UserNotExist = "10";
	String message_AddFriendFailure_ReplicateAddFriend= "11";
}
