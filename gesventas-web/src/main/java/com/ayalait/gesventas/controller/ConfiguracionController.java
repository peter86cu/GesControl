package com.ayalait.gesventas.controller;




import com.ayalait.modelo.*;
import com.ayalait.response.*;
import com.ayalait.web.ResponseEnvios;
import com.google.gson.Gson;
import com.multishop.response.ResponseDireccion;
import com.multishop.response.ResponseUsuarioShopping;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;


@Controller
public class ConfiguracionController {

   

    public ConfiguracionController() {
        
    }

    

    @GetMapping({"/envios"})
    public String accesoPuntoVenta(Model modelo, RedirectAttributes attribute) {
        if (LoginController.session.getToken() != null) {
            modelo.addAttribute("user", LoginController.session.getUser());
            ResponseListaConfiguraciones response = LoginController.conParam.cargarConfiguraciones();
            if(response.isStatus()) {
                modelo.addAttribute("claves", LoginController.conParam.cargarConfiguraciones().getConfiguraciones());

            }else {
                modelo.addAttribute("claves", new ArrayList<Configuraciones>());

            }
            ResponseEnvios responseVD= LoginController.conConfiguracion.listadoEnvios();
            if(responseVD.isStatus()) {
                modelo.addAttribute("envioslst", responseVD.getEnvios());

            }else {
            	modelo.addAttribute("envioslst", new ArrayList<Envios>());
            }
            ResponseListaEstadoVentas estados=LoginController.conTerminal.lstadoEstadoVentas();
            if(estados.isStatus()) {
            	 modelo.addAttribute("estados", estados.getEstadoVentas());
            }else {
           	 modelo.addAttribute("estados", new ArrayList<VentasEstados>());

            }
           
            return "envios";


        } else {
            return "login";
        }
    }
    
    
    @PostMapping(LoginController.ruta + "/user-by-id-envios") 
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public void buscarUsuarioPorId(@ModelAttribute("id_usuario") String idUsuario,HttpServletResponse responseHttp) throws IOException {
        if (LoginController.session.getToken() != null) {
            ResponseUsuarioShopping userResponse = LoginController.conConfiguracion.buscarUsuarioPorId(idUsuario);

            
                String json = (new Gson()).toJson(userResponse);
				responseHttp.setContentType("application/json");
				responseHttp.setCharacterEncoding("UTF-8");
				responseHttp.getWriter().write(json);
           
        }else {
        	ResponseResultado response = new ResponseResultado();

            response.setCode(406);
    		response.setResultado("Sessión caducada");
    		String json = new Gson().toJson(response);
    		responseHttp.setContentType("application/json");
    		responseHttp.setCharacterEncoding("UTF-8");
    		responseHttp.getWriter().write(json);
        }
		
    }
    
    
      
    @PostMapping(LoginController.ruta + "/user-by-venta") 
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public void buscarVentasUsuarioPorId(@ModelAttribute("idVenta") String idVenta,HttpServletResponse responseHttp) throws IOException {
        if (LoginController.session.getToken() != null) {
            ResponseVenta userResponse = LoginController.conConfiguracion.buscarVentaPorId(idVenta);

            
                String json = (new Gson()).toJson(userResponse);
				responseHttp.setContentType("application/json");
				responseHttp.setCharacterEncoding("UTF-8");
				responseHttp.getWriter().write(json);
           
        }else {
        	ResponseResultado response = new ResponseResultado();

            response.setCode(406);
    		response.setResultado("Sessión caducada");
    		String json = new Gson().toJson(response);
    		responseHttp.setContentType("application/json");
    		responseHttp.setCharacterEncoding("UTF-8");
    		responseHttp.getWriter().write(json);
        }
		
    }
    
    
    
    @PostMapping(LoginController.ruta + "/user-by-address") 
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public void buscarDireccionVentaCliente(@ModelAttribute("idDireccion") String idDireccion,HttpServletResponse responseHttp) throws IOException {
        if (LoginController.session.getToken() != null) {
            ResponseDireccion response = LoginController.conConfiguracion.recuperarDreccionPorId(idDireccion);

            
                String json = (new Gson()).toJson(response);
				responseHttp.setContentType("application/json");
				responseHttp.setCharacterEncoding("UTF-8");
				responseHttp.getWriter().write(json);
           
        }else {
        	ResponseResultado response = new ResponseResultado();

            response.setCode(406);
    		response.setResultado("Sessión caducada");
    		String json = new Gson().toJson(response);
    		responseHttp.setContentType("application/json");
    		responseHttp.setCharacterEncoding("UTF-8");
    		responseHttp.getWriter().write(json);
        }
		
    }
    
    @PostMapping(LoginController.ruta + "/send-status") 
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public void obtenerEstadosEnvio(@ModelAttribute("idEstado") int idEstado,HttpServletResponse responseHttp) throws IOException {
        if (LoginController.session.getToken() != null) {
            ResponseEstadoEnvio response = LoginController.conConfiguracion.obtenerEstadoEnvio(idEstado);

            
                String json = (new Gson()).toJson(response);
				responseHttp.setContentType("application/json");
				responseHttp.setCharacterEncoding("UTF-8");
				responseHttp.getWriter().write(json);
           
        }else {
        	ResponseResultado response = new ResponseResultado();

            response.setCode(406);
    		response.setResultado("Sessión caducada");
    		String json = new Gson().toJson(response);
    		responseHttp.setContentType("application/json");
    		responseHttp.setCharacterEncoding("UTF-8");
    		responseHttp.getWriter().write(json);
        }
		
    }

    
    @PostMapping(LoginController.ruta + "/envios/asignar") 
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public void asignarUsuario(@ModelAttribute("idEnvio") int idEnvio,@ModelAttribute("userId") String userId,HttpServletResponse responseHttp) throws IOException {
        if (LoginController.session.getToken() != null) {
            ResponseResultado response = LoginController.conConfiguracion.asigarUsuarioEnvio(idEnvio,userId);

            
                String json = (new Gson()).toJson(response);
				responseHttp.setContentType("application/json");
				responseHttp.setCharacterEncoding("UTF-8");
				responseHttp.getWriter().write(json);
           
        }else {
        	ResponseResultado response = new ResponseResultado();

            response.setCode(406);
    		response.setResultado("Sessión caducada");
    		String json = new Gson().toJson(response);
    		responseHttp.setContentType("application/json");
    		responseHttp.setCharacterEncoding("UTF-8");
    		responseHttp.getWriter().write(json);
        }
		
    }
    
    
    @PostMapping(LoginController.ruta + "/envios/actualizar-observacion") 
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public void actualizarObservaciones(@ModelAttribute("idEnvio") int idEnvio,@ModelAttribute("observaciones") String observaciones,HttpServletResponse responseHttp) throws IOException {
        if (LoginController.session.getToken() != null) {
            ResponseResultado response = LoginController.conConfiguracion.actualizarObservaciones(idEnvio,observaciones);

            
                String json = (new Gson()).toJson(response);
				responseHttp.setContentType("application/json");
				responseHttp.setCharacterEncoding("UTF-8");
				responseHttp.getWriter().write(json);
           
        }else {
        	ResponseResultado response = new ResponseResultado();

            response.setCode(406);
    		response.setResultado("Sessión caducada");
    		String json = new Gson().toJson(response);
    		responseHttp.setContentType("application/json");
    		responseHttp.setCharacterEncoding("UTF-8");
    		responseHttp.getWriter().write(json);
        }
		
    }
    
    
    @PostMapping(LoginController.ruta + "/envios/cambiarEstado") 
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@ResponseStatus(HttpStatus.CREATED)
    public void actualizarEnvio(@ModelAttribute("envioId") int envioId,@ModelAttribute("nuevoEstado") int nuevoEstado,HttpServletResponse responseHttp) throws IOException {
        if (LoginController.session.getToken() != null) {
            ResponseResultado response = LoginController.conConfiguracion.actualizarEstadoEnvio(envioId,nuevoEstado);

            
                String json = (new Gson()).toJson(response);
				responseHttp.setContentType("application/json");
				responseHttp.setCharacterEncoding("UTF-8");
				responseHttp.getWriter().write(json);
           
        }else {
        	ResponseResultado response = new ResponseResultado();

            response.setCode(406);
    		response.setResultado("Sessión caducada");
    		String json = new Gson().toJson(response);
    		responseHttp.setContentType("application/json");
    		responseHttp.setCharacterEncoding("UTF-8");
    		responseHttp.getWriter().write(json);
        }
		
    }

}