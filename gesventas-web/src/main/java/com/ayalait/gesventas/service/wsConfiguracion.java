package com.ayalait.gesventas.service;

import com.ayalait.gesventas.controller.LoginController;
import com.ayalait.gesventas.utils.ObservacionDTO;
import com.ayalait.modelo.*;
import com.ayalait.response.*;
import com.ayalait.utils.DiaAbierto;
import com.ayalait.utils.ErrorState;
import com.ayalait.utils.MessageCodeImpl;
import com.ayalait.web.ResponseEnvios;
import com.ayalait.web.ResponseVisitantes;
import com.ayalait.web.VisitantesLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.multishop.modelo.DireccionUsuario;
import com.multishop.modelo.ShoppingUsuarios;
import com.multishop.response.ResponseDireccion;
import com.multishop.response.ResponseUsuarioShopping;
import com.ayalait.response.ResponseEstadoEnvio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public final class wsConfiguracion {

	private String hostConfiguracion;

	private String hostUserShopping;

	ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();

	@Autowired
	RestTemplate restTemplate = new RestTemplate();

	void cargarServer() throws IOException {
		Properties p = new Properties();

		try {
			URL url = this.getClass().getClassLoader().getResource("application.properties");
			if (url == null) {
				throw new IllegalArgumentException("application.properties" + " is not found 1");
			} else {
				InputStream propertiesStream = url.openStream();
				// InputStream propertiesStream =
				// ClassLoader.getSystemResourceAsStream("application.properties");
				p.load(propertiesStream);
				propertiesStream.close();
				hostUserShopping = p.getProperty("server.userShopping");
				hostConfiguracion = p.getProperty("server.configuracion");
				// hostDashboard = "http://localhost:7007";
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(wsConfiguracion.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public wsConfiguracion() throws IOException {
		// try {
		if (LoginController.desarrollo) {
			hostConfiguracion = "http://localhost:5000";
			hostUserShopping = "http://localhost:7001";
		} else {
			cargarServer();
			// hostDashboard = "http://localhost:7007";

		}

		/*
		 * } catch (IOException ex) {
		 * Logger.getLogger(wsDashboard.class.getName()).log(Level.SEVERE, null, ex); }
		 */
	}

	public ResponseResultado validarConectividadServidor() {

		ResponseResultado responseResult = new ResponseResultado();

		try {

			String url = LoginController.conTerminal.hostTerminal + "/server";

			URI uri = new URI(url);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setResultado(response.getBody());

			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {
			ErrorState data = new ErrorState();
			data.setCode(e.getStatusCode().value());
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.springframework.web.client.ResourceAccessException e) {
			responseResult.setStatus(false);
			responseResult.setResultado("Módulo en mantenimiento.");
		}

		return responseResult;

	}

	public ResponseEnvios listadoEnvios() {

		ResponseEnvios responseResult = new ResponseEnvios();
		try {

			String url = LoginController.conTerminal.hostTerminal + "/api/v1/envios";

			URI uri = new URI(url);
			ResponseEntity<List<Envios>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Envios>>() {
					});

			if (response.getStatusCodeValue() == 200) {
				responseResult.setCode(response.getStatusCodeValue());
				responseResult.setStatus(true);
				responseResult.setEnvios(response.getBody());

			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {
			ErrorState data = new ErrorState();
			data.setCode(e.getStatusCode().value());
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.springframework.web.client.HttpClientErrorException e) {
			JsonParser jsonParser = new JsonParser();
			responseResult.setStatus(false);
			int in = e.getLocalizedMessage().indexOf("{");
			int in2 = e.getLocalizedMessage().indexOf("}");
			String cadena = e.getMessage().substring(in, in2 + 1);
			JsonObject myJson = (JsonObject) jsonParser.parse(cadena);
			responseResult.setCode(myJson.get("code").getAsInt());
			ErrorState data = new ErrorState();
			data.setCode(myJson.get("code").getAsInt());
			data.setMenssage(myJson.get("menssage").getAsString());
			responseResult.setError(data);
		}

		return responseResult;

	}

	public ResponseUsuarioShopping buscarUsuarioPorId(String id) {

		ResponseUsuarioShopping responseResult = new ResponseUsuarioShopping();

		try {
			HttpHeaders headers = new HttpHeaders();
			String url = LoginController.conUser.hostSeguridad + "/api/v1/tienda/user/shopping/usuario/id-usuario?id=" + id;
			URI uri = new URI(url);
			HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

			ResponseEntity<ShoppingUsuarios> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity,
					ShoppingUsuarios.class);

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setUser(response.getBody());

			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {
			ErrorState data = new ErrorState();
			data.setCode(e.getStatusCode().value());
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseResult;

	}

	public ResponseVenta buscarVentaPorId(String id) {

		ResponseVenta responseResult = new ResponseVenta();

		try {
			HttpHeaders headers = new HttpHeaders();
			String url = LoginController.conTerminal.hostTerminal + "/venta?id=" + id;
			URI uri = new URI(url);
			HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

			ResponseEntity<Ventas> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, Ventas.class);

			if (response.getStatusCodeValue() == 200) {

				responseResult.setStatus(true);
				responseResult.setVenta(response.getBody());

			}

		} catch (org.springframework.web.client.HttpServerErrorException e) {
			ErrorState data = new ErrorState();
			data.setCode(e.getStatusCode().value());
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseResult;

	}

	public ResponseDireccion recuperarDreccionPorId(String id) {

		ResponseDireccion responseResult = new ResponseDireccion();

		try {
			HttpHeaders headers = new HttpHeaders();
			String url = LoginController.conUser.hostSeguridad + "/api/v1/tienda/shopping/direccion/id?id=" + id;
			URI uri = new URI(url);
			// headers.set("id", idUsuario);
			HttpEntity<String> requestEntity = new HttpEntity<>(id, headers);

			ResponseEntity<DireccionUsuario> response = null;
			try {
				response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, DireccionUsuario.class);

			} catch (org.springframework.web.client.HttpClientErrorException e) {
				ErrorState data = new ErrorState();
				data.setCode(e.getStatusCode().value());
				data.setMenssage(e.getMessage());
				responseResult.setCode(data.getCode());
				responseResult.setError(data);

				return responseResult;
			}

			if (response.getStatusCodeValue() == 200) {
				responseResult.setCode(response.getStatusCodeValue());
				responseResult.setStatus(true);
				responseResult.setDireccion(response.getBody());

			}

		} catch (Exception e) {
			ErrorState data = new ErrorState();
			data.setCode(500);
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		}

		return responseResult;

	}

	public ResponseEstadoEnvio obtenerEstadoEnvio(int id) {

		ResponseEstadoEnvio responseResult = new ResponseEstadoEnvio();

		try {
			HttpHeaders headers = new HttpHeaders();
			String url = LoginController.conTerminal.hostTerminal + "/parametros/estado-ventas/id?id=" + id;
			URI uri = new URI(url);
			HttpEntity<String> requestEntity = new HttpEntity<>(String.valueOf(id), headers);

			ResponseEntity<VentasEstados> response = null;
			try {
				response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, VentasEstados.class);

			} catch (org.springframework.web.client.HttpClientErrorException e) {
				ErrorState data = new ErrorState();
				data.setCode(e.getStatusCode().value());
				data.setMenssage(e.getMessage());
				responseResult.setCode(data.getCode());
				responseResult.setError(data);

				return responseResult;
			}

			if (response.getStatusCodeValue() == 200) {
				responseResult.setCode(response.getStatusCodeValue());
				responseResult.setStatus(true);
				responseResult.setEstadoVentas(response.getBody());

			}

		} catch (Exception e) {
			ErrorState data = new ErrorState();
			data.setCode(500);
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		}

		return responseResult;

	}

	public ResponseResultado asigarUsuarioEnvio(int idVenta, String userId) {

		ResponseResultado responseResult = new ResponseResultado();

		try {
			String url = LoginController.conTerminal.hostTerminal + "/api/v1/envios/asignar/" + idVenta + "/" + userId;
			URI uri = new URI(url);

			ResponseEntity<Boolean> response = null;
			try {
				response = restTemplate.exchange(uri, HttpMethod.POST, null, Boolean.class);

			} catch (org.springframework.web.client.HttpClientErrorException e) {
				ErrorState data = new ErrorState();
				data.setCode(e.getStatusCode().value());
				data.setMenssage(e.getMessage());
				responseResult.setCode(data.getCode());
				responseResult.setError(data);

				return responseResult;
			}

			if (response.getStatusCodeValue() == 200) {
				responseResult.setCode(response.getStatusCodeValue());
				responseResult.setStatus(true);
				responseResult.setStatus(response.getBody());

			}

		} catch (Exception e) {
			ErrorState data = new ErrorState();
			data.setCode(500);
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		}

		return responseResult;

	}
	
	
	
	public ResponseResultado actualizarObservaciones(int idEnvio, String observacion) {
	    ResponseResultado responseResult = new ResponseResultado();

	    try {
	        String url = LoginController.conTerminal.hostTerminal + "/api/v1/envios/" + idEnvio + "/actualizar-observacion";
	        URI uri = new URI(url);

	        // Crear el DTO con la observación
	        ObservacionDTO observacionDTO = new ObservacionDTO();
	        observacionDTO.setObservacion(observacion);

	        // Configurar los headers para JSON
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Crear la petición HTTP con el cuerpo y los headers
	        HttpEntity<ObservacionDTO> requestEntity = new HttpEntity<>(observacionDTO, headers);

	        ResponseEntity<Envios> response;
	        try {
	            response = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, Envios.class);
	        } catch (org.springframework.web.client.HttpClientErrorException e) {
	            ErrorState data = new ErrorState();
	            data.setCode(e.getStatusCode().value());
	            data.setMenssage(e.getMessage());
	            responseResult.setCode(data.getCode());
	            responseResult.setError(data);
	            return responseResult;
	        }

	        if (response.getStatusCodeValue() == 200) {
	            responseResult.setCode(response.getStatusCodeValue());
	            responseResult.setStatus(true);
	        }

	    } catch (Exception e) {
	        ErrorState data = new ErrorState();
	        data.setCode(500);
	        data.setMenssage(e.getMessage());
	        responseResult.setCode(data.getCode());
	        responseResult.setError(data);
	    }

	    return responseResult;

	}
	
	public ResponseResultado actualizarEstadoEnvio(int idEnvio, int nuevoEstado) {

		ResponseResultado responseResult = new ResponseResultado();

		try {
			String url = LoginController.conTerminal.hostTerminal + "/api/v1/envios/cambiarEstado/"+idEnvio+"/"+nuevoEstado+"";
			URI uri = new URI(url);

			ResponseEntity<Boolean> response = null;
			try {
				response = restTemplate.exchange(uri, HttpMethod.PUT, null, Boolean.class);

			} catch (org.springframework.web.client.HttpClientErrorException e) {
				ErrorState data = new ErrorState();
				data.setCode(e.getStatusCode().value());
				data.setMenssage(e.getMessage());
				responseResult.setCode(data.getCode());
				responseResult.setError(data);

				return responseResult;
			}

			if (response.getStatusCodeValue() == 200) {
				responseResult.setCode(response.getStatusCodeValue());
				responseResult.setStatus(true);
				responseResult.setStatus(response.getBody());

			}

		} catch (Exception e) {
			ErrorState data = new ErrorState();
			data.setCode(500);
			data.setMenssage(e.getMessage());
			responseResult.setCode(data.getCode());
			responseResult.setError(data);

		}

		return responseResult;

	}
}


