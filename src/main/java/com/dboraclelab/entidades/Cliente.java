package com.dboraclelab.entidades;

public class Cliente {
	private long cliente_id;
	private String nombres;
	private String apellidos;
	private boolean activo;
	
	public Cliente() {}
	
	public Cliente(long cliente_id, String nombres, String apellidos, boolean activo) {
		this.cliente_id = cliente_id;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.activo = activo;
	}
	
	public long getCliente_id() {
		return cliente_id;
	}
	
	public void setCliente_id(long cliente_id) {
		this.cliente_id = cliente_id;
	}
	
	public String getNombres() {
		return nombres;
	}
	
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public boolean getActivo() {
		return activo;
	}
	
	public void setActivo(boolean activo) {
		this.activo = activo;
	}	
}
