package com.huawei.cloud.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

class ExecuteSqlCount extends TimerTask {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String host;
	private String port;
	private String user;
	private String pass;
	final String selectCount = "SELECT count(*) FROM product_table";

	public ExecuteSqlCount(String host, String port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
	}

	public void run() {
		logger.info("-----Execute getcount perioodic task---------");
		logger.info("daskdjaksldjalsdkajsdkljaskdljasdjlsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssdaskdjaksldjalsdkajsdkljaskdljasdjlssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		try {
			String myUrl = "jdbc:mysql://" + host + ":" + port + "/ShoppingMallDB";
			conn = DriverManager.getConnection(myUrl, user, pass);
			preparedStmt = conn.prepareStatement(selectCount);

			preparedStmt.executeQuery();
			logger.info("----------Finiched getcount perioodic task----------");
		} catch (Exception e) {
			logger.error("Got an exception! --- >"+e.getMessage());
			
		} finally {
			try {
				if (preparedStmt != null) {
					preparedStmt.close();
				}
				if (conn != null)
					conn.close();

			} catch (Exception e) {

				logger.error("Got an exception! --- >"+e.getMessage());
			}
		}

	}
}
