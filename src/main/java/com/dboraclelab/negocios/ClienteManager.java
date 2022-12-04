package com.dboraclelab.negocios;

import java.util.*;
import java.sql.*;

import org.springframework.stereotype.Service;

import com.dboraclelab.dao.DBConnection;
import com.dboraclelab.entidades.Cliente;
import com.dboraclelab.jdbc.OracleTypes;
import com.dboraclelab.servicios.IClienteManager;

@Service
public class ClienteManager implements IClienteManager {

	private DBConnection db = null;
	
	public ClienteManager() {
		if (this.db == null)
			this.db = new DBConnection(); 
	}
	
	@Override
	public List<Cliente> getClientes() throws Exception {
		ArrayList<Cliente> lstClientes = new ArrayList<Cliente>();
		
		try
		{
			String sqlProc = "pq_api_clientes.pr_get_ad_clientes(?)";
            
            CallableStatement stmt = db.ExecuteProcedure(sqlProc);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            
            ResultSet rs = (ResultSet) stmt.getObject(1);
            
            while (rs.next()) {
            	Cliente cliente = new Cliente();
            	cliente.setCliente_id(rs.getInt("cliente_id"));
            	cliente.setNombres(rs.getString("nombres"));
            	cliente.setApellidos(rs.getString("apellidos"));
            	cliente.setActivo(rs.getBoolean("activo"));
            	
            	lstClientes.add(cliente);
            }
		}
		catch (Exception ex) {
            throw new Exception("Cliente - findAll: " + ex.getMessage());
        }
		
		return lstClientes;
	}

	@Override
	public List<Cliente> getClientesById(int id) throws Exception {
		ArrayList<Cliente> lstClientes = new ArrayList<Cliente>();
		
		try
		{
			String sqlProc = "pq_api_clientes.pr_get_ad_clientes_by_id(?,?)";
            
            CallableStatement stmt = db.ExecuteProcedure(sqlProc);
            stmt.setInt(1, id);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            
            ResultSet rs = (ResultSet) stmt.getObject(2);
            
            while (rs.next()) {
            	Cliente cliente = new Cliente();
            	cliente.setCliente_id(rs.getInt("cliente_id"));
            	cliente.setNombres(rs.getString("nombres"));
            	cliente.setApellidos(rs.getString("apellidos"));
            	cliente.setActivo(rs.getBoolean("activo"));
            	
            	lstClientes.add(cliente);
            }
		}
		catch (Exception ex) {
            throw new Exception("Cliente - findById: " + ex.getMessage());
        }
		
		return lstClientes;
	}

	@Override
	public int crudClientes(Cliente request, String evento) throws Exception {
		int res = 0;
		
		try
		{
			String sqlProc = "pq_api_clientes.pr_crud_ad_clientes(?,?,?,?,?)";
						
			CallableStatement stmt = db.ExecuteProcedure(sqlProc);
			stmt.setString(1, evento);
			stmt.registerOutParameter(2, OracleTypes.NUMBER);
			stmt.setInt(2, request.getCliente_id());
			stmt.setString(3, request.getNombres());
			stmt.setString(4, request.getApellidos());
			stmt.setString(5, request.getActivo() == true ? "true" : "false");
			stmt.execute();
			
			res = stmt.getInt(2);
		}
		catch (Exception ex) {
			throw new Exception("Cliente - crudClientes: " + ex.getMessage());
		}
		
		return res;
	}

}
