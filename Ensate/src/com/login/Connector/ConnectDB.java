/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.login.Connector;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 *
 * @author user
 */
public class ConnectDB {
    
    
     static Connection con ;
	static String driver = "com.mysql.cj.jdbc.Driver";
	static String url ="jdbc:mysql://localhost:3308/ensaspace";
	static String uname = "root";
	static String pass = "";

	public static Connection ConnectToDB() throws Exception{

		if (con == null) {
			Class.forName(driver);
			con = DriverManager.getConnection(url,uname,pass);
		}
		return con;
	}
    
}
