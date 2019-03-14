package com.dongnaoedu.tony;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TonyJdbcConnect {
	private static String driverClass = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://database.tony.com:3306/12306?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
	private static String username = "tony";
	private static String password = "tony";

	static {
		try {
			// 加载驱动
			Class.forName(driverClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 数据库连接对象
	private Connection connection;

	public TonyJdbcConnect() {
		// 创建一个数据库连接
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 关闭数据连接
	public void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
