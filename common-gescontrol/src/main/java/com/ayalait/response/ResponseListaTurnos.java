package com.ayalait.response;

import java.util.List;

import com.ayalait.modelo.Turnos;
import com.ayalait.utils.ErrorState;

public class ResponseListaTurnos {
	
	private boolean status;
    private int code;
    private List<Turnos> turnos;
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

	public List<Turnos> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turnos> turnos) {
		this.turnos = turnos;
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
