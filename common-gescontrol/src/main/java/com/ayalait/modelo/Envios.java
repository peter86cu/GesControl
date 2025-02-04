package com.ayalait.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;



public class Envios implements Serializable {

    private static final long serialVersionUID = 1L;
    
   
   
    private Integer idEnvio;

    private Integer idVenta;

    private String idCliente;

    private Integer idDireccion;

    private Integer estado;  // Valor por defecto '9'

    private String idUsuarioAsignado;

    private BigDecimal costoEnvio = BigDecimal.valueOf(0.00);  // Valor por defecto '0.00'

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Timestamp fechaGeneracion;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date fechaEntrega;

    private String observaciones;

    private String codigoSeguimiento;

	public Integer getIdEnvio() {
		return idEnvio;
	}

	public void setIdEnvio(Integer idEnvio) {
		this.idEnvio = idEnvio;
	}

	public Integer getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdDireccion() {
		return idDireccion;
	}

	public void setIdDireccion(Integer idDireccion) {
		this.idDireccion = idDireccion;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getIdUsuarioAsignado() {
		return idUsuarioAsignado;
	}

	public void setIdUsuarioAsignado(String idUsuarioAsignado) {
		this.idUsuarioAsignado = idUsuarioAsignado;
	}

	public BigDecimal getCostoEnvio() {
		return costoEnvio;
	}

	public void setCostoEnvio(BigDecimal costoEnvio) {
		this.costoEnvio = costoEnvio;
	}

	public Timestamp getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(Timestamp fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getCodigoSeguimiento() {
		return codigoSeguimiento;
	}

	public void setCodigoSeguimiento(String codigoSeguimiento) {
		this.codigoSeguimiento = codigoSeguimiento;
	}

	public Envios() {
		super();
		// TODO Auto-generated constructor stub
	}


}
