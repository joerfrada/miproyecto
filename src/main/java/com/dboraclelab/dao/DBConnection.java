package com.dboraclelab.dao;

import java.sql.*;

import com.dboraclelab.configuration.ConfigProperties;

public class DBConnection {
		
    private String url;
    private String username;
    private String password;
    
    public DBConnection(ConfigProperties config) {
    	this.url = config.getUrl();
    	this.username = config.getUsername();
    	this.password = config.getPassword();
    }
    
    private Connection getConnection() throws Exception {
        Connection conn = null;
    	
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            conn = DriverManager.getConnection(this.url, this.username, this.password);
            
            if (conn == null) {
                System.out.println("Error: The database could not open.");
            }
        }
        catch (ClassNotFoundException ex) {
            throw new Exception("DBConnection - ClassNotFoundException: " + ex.getMessage());
        }
        catch (SQLException ex) {
            throw new Exception("DBConnection - SQLException: " + ex.getMessage());
        }
        
        return conn;
    }
    
    public CallableStatement ExecuteProcedure(String sqlProcedure) throws Exception {
        Connection conn = null;
        CallableStatement callStmt = null;
        
        try {
            conn = this.getConnection();
            String psql = "begin " + sqlProcedure + "; end;";
            
            callStmt = conn.prepareCall(psql);
        }
        catch (Exception ex) {
            throw new Exception("DBConnection - ExecuteProcedure: " + ex.getMessage());
        }
        
        return callStmt;
    }
}
