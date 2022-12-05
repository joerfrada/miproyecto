package com.dboraclelab.servicios;

import java.util.List;

import com.dboraclelab.configuration.ConfigProperties;
import com.dboraclelab.entidades.Cliente;

public interface IClienteManager {
	void setDBConnection(ConfigProperties config);
	List<Cliente> getClientes() throws Exception;
	List<Cliente> getClientesById(long id) throws Exception;
	long crudClientes(Cliente request, String evento) throws Exception;
}
