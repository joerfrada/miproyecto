package com.dboraclelab.controller;

import java.util.List;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dboraclelab.configuration.ConfigProperties;
import com.dboraclelab.entidades.Cliente;
import com.dboraclelab.entidades.MensajeEntity;
import com.dboraclelab.entidades.RequestEntity;
import com.dboraclelab.servicios.IClienteManager;

@RestController()
@RequestMapping(value = "/api")
public class ClienteController {
	@Autowired
	private IClienteManager cliMgr;
	
	@Autowired
	private ConfigProperties config;
	
	@GetMapping(value = "/getClientes")
	public MensajeEntity getClientes() {
		MensajeEntity msg = new MensajeEntity();		
		List<Cliente> clientes = null;
		
		cliMgr.setDBConnection(config);
		
		try {
			clientes = cliMgr.getClientes();
			
			msg.set(MensajeEntity.SUCCESS, null, clientes, 0);
			
		}
		catch (Exception e) {
			msg.set(MensajeEntity.ERROR, e.getMessage(), null, 0);
		}
		
		return msg;
	}
	
	@PostMapping(value = "/getClientesById", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public MensajeEntity getClientesById(@RequestBody RequestEntity request) {
		MensajeEntity msg = new MensajeEntity();
		List<Cliente> clientes = null;
		
		cliMgr.setDBConnection(config);
		
		try {
			clientes = cliMgr.getClientesById(request.getId());
			
			msg.set(MensajeEntity.SUCCESS, null, clientes, 0);
		}
		catch (Exception e) {
			msg.set(MensajeEntity.ERROR, e.getMessage(), null, 0);
		}
		
		return msg;
	}
	
	@PostMapping(value = "/crearClientes", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public MensajeEntity crearClientes(@RequestBody Cliente request) {
		MensajeEntity msg = new MensajeEntity();
		
		try
		{
			long id = cliMgr.crudClientes(request, "C");
			
			msg.set(MensajeEntity.SUCCESS, "El cliente ha guardado exitosamente.", null, id);
		}
		catch (Exception e) {
			msg.set(MensajeEntity.ERROR, e.getMessage(), null, 0);
		}
		
		return msg;
	}
	
	@PostMapping(value = "/actualizarClientes", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public MensajeEntity actualizarClientes(@RequestBody Cliente request) {
		MensajeEntity msg = new MensajeEntity();
		
		cliMgr.setDBConnection(config);
		
		try
		{
			cliMgr.crudClientes(request, "U");
			
			msg.set(MensajeEntity.SUCCESS, "El cliente ha actualizado exitosamente.", null, 0);
		}
		catch (Exception e) {
			msg.set(MensajeEntity.ERROR, e.getMessage(), null, 0);
		}
		
		return msg;
	}
}
