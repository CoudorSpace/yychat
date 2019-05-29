package com.yychatserver.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.yychatclient.model.User;

public class YychatDbUtil {
	public static final String URL = "jdbc:mysql://localhost:3306/yychat?useSSL=FALSE&serverTimezone=UTC";
	public static final String DBUSER = "root";
	public static final String DBPASSWORD = "lierlulu00388239";
	
	public static void loadDriver() {
		try {
			System.out.println("mysqlDB（8.0.15）驱动加载中：com.mysql.cj.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("mysqlDB（8.0.15）驱动加载成功");
	}
	
	public static Connection getConnection() {
		loadDriver();
		
		Connection conn = null;
		try {
			System.out.println("数据库（yychat）连接请求中...");
			conn = DriverManager.getConnection(URL,DBUSER,DBPASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("数据库（yychat）连接成功");
		return conn;
	}
	
	public static boolean loginValidate(String userName, String passWord) {
		boolean loginSuccess = false;
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String user_login_sql = "select * from user where username=? and password=?;";
		System.out.println("与数据库交互中...\r\n" + user_login_sql);
		try {
			psmt = conn.prepareStatement(user_login_sql);
			psmt.setString(1,userName);
			psmt.setString(2,passWord);
			
			System.out.println("数据库返回数据中...");
			rs = psmt.executeQuery();
			
			loginSuccess = rs.next();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			closeDB(conn, psmt, rs);
		}
		
		return loginSuccess;
		
	}
	
	public static String getFriendString(String userName) {
		
		String friend_Relation_Sql = "select slaveuser from relation where majoruser = ? and relation_type = '1'";
		
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String friendString = new String();
		try {
			psmt = conn.prepareStatement(friend_Relation_Sql);
			psmt.setString(1, userName);
			rs = psmt.executeQuery();
			while (rs.next()) {
				friendString += rs.getString("slaveuser") + " ";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDB(conn, psmt, rs);
		}
		
		return friendString;
		
		
		
		
	}
	
	
	
	public static void closeDB(Connection conn, PreparedStatement psmt, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (psmt != null) {
			try {
				psmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static boolean seekUser(String userName) {
		boolean noRepeatUser = false;
		String seek_user_sql = "select username from yychat.user where username = ?";
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			psmt = conn.prepareStatement(seek_user_sql);
			psmt.setString(1, userName);
			rs = psmt.executeQuery();

			if (!rs.next()) {
				noRepeatUser = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			closeDB(conn, psmt, rs);
		}

		
		return noRepeatUser;
	}

	public static void addUser(User user) {
		boolean insertSuccess = false;
		
		String add_friend_sql = "insert into yychat.user (username, password,register_timestamp) values (?,?,?);";
		
		Connection conn = getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			psmt = conn.prepareStatement(add_friend_sql);
			psmt.setString(1, user.getUserName());
			psmt.setString(2, user.getPassWord());
			Date date = new Date();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
			psmt.setTimestamp(3, timestamp);
			psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB(conn, psmt, rs);
		}
		
		
		
	}
	
	
	
}
