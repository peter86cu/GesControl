package com.ayalait.web;

import java.util.List;

import com.ayalait.modelo.Envios;
import com.ayalait.utils.ErrorState;

public class ResponseEnvios {
	 private boolean status;
	    private int code;
	    private List<Envios> envios;
	    private ErrorState error;
	    private String resultado;
		public boolean isStatus() {
			return status;
		}
		public void setStatus(boolean status) {
			this.status = status;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		
		public List<Envios> getEnvios() {
			return envios;
		}
		public void setEnvios(List<Envios> envios) {
			this.envios = envios;
		}
		public ErrorState getError() {
			return error;
		}
		public void setError(ErrorState error) {
			this.error = error;
		}
		public String getResultado() {
			return resultado;
		}
		public void setResultado(String resultado) {
			this.resultado = resultado;
		}
	    
	    
}
