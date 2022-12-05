package com.dboraclelab.negocios;

import java.util.*;
import java.sql.*;

import org.springframework.stereotype.Service;

import com.dboraclelab.dao.DBConnection;
import com.dboraclelab.entidades.Cliente;
import com.dboraclelab.jdbc.OracleTypes;
import com.dboraclelab.servicios.IClienteManager;

import com.dboraclelab.configuration.ConfigProperties;

@Service
public class ClienteManager implements IClienteManager {

	private DBConnection db = null;
	
	public void setDBConnection(ConfigProperties config) {
		if (this.db == null)
			this.db = new DBConnection(config);
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
            	cliente.setCliente_id(rs.getLong("cliente_id"));
            	cliente.setNombres(rs.getString("nombres"));
            	cliente.setApellidos(rs.getString("apellidos"));
            	cliente.setActivo(rs.getBoolean("activo"));
            	
            	lstClientes.add(cliente);
            }
		}
		catch (Exception ex) {
            throw new Exception("ClienteManager - getClientes: " + ex.getMessage());
        }
		
		return lstClientes;
	}

	@Override
	public List<Cliente> getClientesById(long id) throws Exception {
		ArrayList<Cliente> lstClientes = new ArrayList<Cliente>();
		
		try
		{
			String sqlProc = "pq_api_clientes.pr_get_ad_clientes_by_id(?,?)";
            
            CallableStatement stmt = db.ExecuteProcedure(sqlProc);
            stmt.setLong(1, id);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            
            ResultSet rs = (ResultSet) stmt.getObject(2);
            
            while (rs.next()) {
            	Cliente cliente = new Cliente();
            	cliente.setCliente_id(rs.getLong("cliente_id"));
            	cliente.setNombres(rs.getString("nombres"));
            	cliente.setApellidos(rs.getString("apellidos"));
            	cliente.setActivo(rs.getBoolean("activo"));
            	
            	lstClientes.add(cliente);
            }
		}
		catch (Exception ex) {
            throw new Exception("ClienteManager - getClientesById: " + ex.getMessage());
        }
		
		return lstClientes;
	}

	@Override
	public long crudClientes(Cliente request, String evento) throws Exception {
		long res = 0;
		
		try
		{
			String sqlProc = "pq_api_clientes.pr_crud_ad_clientes(?,?,?,?,?)";
						
			CallableStatement stmt = db.ExecuteProcedure(sqlProc);
			stmt.setString(1, evento);
			stmt.registerOutParameter(2, OracleTypes.NUMBER);
			stmt.setLong(2, request.getCliente_id());
			stmt.setString(3, request.getNombres());
			stmt.setString(4, request.getApellidos());
			stmt.setString(5, request.getActivo() == true ? "true" : "false");
			stmt.execute();
			
			res = stmt.getLong(2);
		}
		catch (Exception ex) {
			throw new Exception("ClienteManager - crudClientes: " + ex.getMessage());
		}
		
		return res;
	}

}
