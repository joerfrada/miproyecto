package com.dboraclelab.servicios;

import java.util.List;

import com.dboraclelab.entidades.Cliente;

public interface IClienteManager {
	List<Cliente> getClientes() throws Exception;
	List<Cliente> getClientesById(int id) throws Exception;
	int crudClientes(Cliente request, String evento) throws Exception;
}
