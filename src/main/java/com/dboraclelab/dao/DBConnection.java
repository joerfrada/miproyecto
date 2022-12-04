package com.dboraclelab.dao;

import java.sql.*;

public class DBConnection {
	private String username;
    private String password;
    private String url = null;
    
    public DBConnection() {
    	this.url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST = 192.168.211.180)(PORT = 1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=pdb1)))";
    	this.username = "personal";
    	this.password = "oracle";
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
            throw new Exception("ClassNotFoundException: " + ex.getMessage());
        }
        catch (SQLException ex) {
            throw new Exception("SQLException: " + ex.getMessage());
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
            throw new Exception("ExecuteProcedure: " + ex.getMessage());
        }
        
        return callStmt;
    }
}
