package com.dboraclelab.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MensajeEntity {
	public static final int ERROR = -1;
	public static final int SUCCESS = 0;
	public static final int WARNING = 1;
	
	@JsonProperty("tipo")
	private int tipo;
	
	@JsonProperty("mensaje")
	private String mensaje;
	
	@JsonProperty("result")
	private Object result;
	
	@JsonProperty("id")
	private long id;
	
	public MensajeEntity() {}
	
	public void set(int tipo, String mensaje, Object result, long id) {
		this.tipo = tipo;
		this.mensaje = mensaje;
		this.result = result;
		this.id = id;
	}
}
