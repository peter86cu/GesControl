package com.ayalait.gesventas.controller;import com.google.gson.Gson;import java.io.FileNotFoundException;import java.io.IOException;import java.io.InputStream;import java.net.URL;import java.sql.SQLException;import java.text.ParseException;import java.util.*;import java.util.logging.Level;import java.util.logging.Logger;import com.ayalait.gesventas.service.*; import com.ayalait.gesventas.utils.*;import com.ayalait.modelo.*;import com.ayalait.response.*;import com.ayalait.utils.DiaAbierto;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.http.HttpHeaders;import org.springframework.http.ResponseEntity;import org.springframework.stereotype.Controller;import org.springframework.ui.Model;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.ModelAttribute;import org.springframework.web.bind.annotation.PostMapping;import org.springframework.web.bind.annotation.RequestParam;import org.springframework.web.client.RestTemplate;import org.springframework.web.servlet.mvc.support.RedirectAttributes;import javax.servlet.http.HttpServletResponse;@Controllerpublic class LoginController {	@Autowired	RestTemplate restTemplate;	public static Session session = new Session();	HttpHeaders headers = new HttpHeaders();	Seguridad seguridad = new Seguridad();	Gson gson = new Gson();	ResponseEntity<String> result;	public static wsUsuarios conUser;	public static wsEmpleado conEmpl ;	public static wsRoles conRol ;	public static wsStock conStock ;	public static wsParametros conParam ;	public static wsCaja conCaja ;	public static wsContable conContable;	public  static wsTerminal conTerminal;	public static wsDashboard conDashboard;	public static wsNotificaciones conNotificaciones;	public static wsMail conMail= new wsMail();	private static FormatoFecha fechaSinHora;	private static FormatoFecha fechaConHora;	private static FormatoFecha hora;	public  static final String ruta = "GesVentas";	public  String ip="127.0.0.1";		public static String rutaDowloadProducto;	public static String rutaDowloadTitulos;	public static String rutaDowloadEmpleado;	public static String rutaPDFPrefacturas;	public static String rutaPDFOrdenes;	public static String rutaFacturas;	public static   boolean desarrollo=false;	public LoginController() throws IOException {			fechaSinHora = FormatoFecha.YYYYMMDD;			fechaConHora = FormatoFecha.YYYYMMDDH24;			hora = FormatoFecha.H24;			ip=Utils.ObtenerIPCaja();			conUser = new wsUsuarios();			session = new Session();		    parametros();	}				void parametros() throws IOException {		Properties p = new Properties();		try {			URL url = this.getClass().getClassLoader().getResource("application.properties");			if (url == null) {				throw new IllegalArgumentException("application.properties" + " No se pudo cargar el fichero properties");			} else {				InputStream propertiesStream = url.openStream();				p.load(propertiesStream);				propertiesStream.close();				String ambiente=(p.getProperty("server.ambiente"));				if(ambiente.equalsIgnoreCase("true"))					desarrollo=true;				if(desarrollo) {										LoginController.rutaDowloadProducto = "C:\\recursos\\productos\\";					LoginController.rutaDowloadTitulos = "C:\\recursos\\titulos\\";					LoginController.rutaDowloadEmpleado = "C:\\recursos\\empleados\\";					LoginController.rutaPDFPrefacturas="C:\\recursos\\prefacturas\\";					LoginController.rutaPDFOrdenes= "C:\\recursos\\ordenes\\";					LoginController.rutaFacturas= "C:\\recursos\\facturas\\";				}else {										LoginController.rutaDowloadProducto = p.getProperty("server.uploaderProductos");					LoginController.rutaDowloadTitulos = p.getProperty("server.uploaderTitulos");					LoginController.rutaDowloadEmpleado = p.getProperty("server.uploaderEmpleado");					LoginController.rutaPDFPrefacturas=p.getProperty("server.prefacturasPDF");					LoginController.rutaPDFOrdenes= p.getProperty("server.ordenesPDF");					LoginController.rutaFacturas= p.getProperty("server.facturasCompras");				}				 conUser = new wsUsuarios();				 conEmpl = new wsEmpleado();				 conRol = new wsRoles();				 conStock = new wsStock();				 conParam = new wsParametros();				 conCaja = new wsCaja();				 conContable= new wsContable();				 conTerminal = new wsTerminal();				 conDashboard= new wsDashboard();				 conNotificaciones= new wsNotificaciones();			}		} catch (FileNotFoundException var3) {			Logger.getLogger(Session.class.getName()).log(Level.SEVERE, (String) null, var3);		}	}	@GetMapping({ "/" })	public String showForm(Model modelo, RedirectAttributes attribute) {		if (session.getToken() != null) {			modelo.addAttribute("user", session.getUser());			ResponseMonedas response = LoginController.conParam.listarMonedas(); 			if (response.isStatus()) {				modelo.addAttribute("listaMoneda", response.getMonedas());			}			return "inicio";		}		return "login";	}		@GetMapping({ "/template" })	public String showForm2(Model modelo, RedirectAttributes attribute) {		if (session.getToken() != null) {			modelo.addAttribute("user", session.getUser());			return "template";		}		return "login";	}	@PostMapping(value = { ruta+"/login" }, produces = { "application/json" })	public void login(@RequestParam("username") String user, @RequestParam("password") String pwd, Model modelo,					  RedirectAttributes redirectAttrs, HttpServletResponse responseHttp) throws IOException {		ResponseResultado response = new ResponseResultado();		String moduloAcceso = "";		Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, (String) user+pwd, "Login");		if (session.getToken() == null) {			response = conUser.obtenerToken(user, pwd);			if (response.isStatus()) {				Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Token", response);				session.setToken(response.getResultado());				ResponseUsuario usuarioLogin = conUser.obtenerDatosUsuarioLogin(response.getResultado(), user);				if (usuarioLogin.isStatus()) {					session.setUser(usuarioLogin.getUser());					session.setModuloAcceso(moduloAcceso);					session.setId_empresa(usuarioLogin.getUser().getEmpleado().getIdempresa());					modelo.addAttribute("user", usuarioLogin.getUser());					modelo.addAttribute("empleado", usuarioLogin.getUser().getEmpleado());					session.setIp(ip);					//Obtener id apertura del dia					                    ResponseOpenDay aperturaDia = LoginController.conCaja.openDay(Utils.obtenerFechaPorFormato(FormatoFecha.YYYYMMDD.getFormato()));					if(aperturaDia.isStatus()) {						session.setOepnDay(aperturaDia.getOpen());					}else {						session.setOepnDay(new ArrayList<DiaAbierto>());					}										String json = new Gson().toJson(usuarioLogin);					responseHttp.setContentType("application/json");					responseHttp.setCharacterEncoding("UTF-8");					responseHttp.getWriter().write(json);				} else {					usuarioLogin.setResultado("El recurso requerido no está disponible.");					String json = new Gson().toJson(usuarioLogin);					responseHttp.setContentType("application/json");					responseHttp.setCharacterEncoding("UTF-8");					responseHttp.getWriter().write(json);				}			} else {				//response.setResultado("El recurso requerido no está disponible.");				String json = new Gson().toJson(response);				responseHttp.setContentType("application/json");				responseHttp.setCharacterEncoding("UTF-8");				responseHttp.getWriter().write(json);			}		} else {			session = new Session();			response.setCode(400);			//response.setResultado();			String json = new Gson().toJson(response);			responseHttp.setContentType("application/json");			responseHttp.setCharacterEncoding("UTF-8");			responseHttp.getWriter().write(json);		}	}	@PostMapping({ ruta+"/validar-server" })	public void validarDisponibilidadServer(@ModelAttribute("accion") String server, Model modelo,											HttpServletResponse responseHttp) throws IOException, ParseException {		if (session.getToken() != null) {			ResponseResultado response = new ResponseResultado();			modelo.addAttribute("user", session.getUser());			if (server.equalsIgnoreCase("productos") || server.equalsIgnoreCase("orden-compra")					|| server.equalsIgnoreCase("facturas-compra") || server.equalsIgnoreCase("prefactura")) {				response = conStock.validarConectividadServidor();			}			/*if (server.equalsIgnoreCase("mailbox")) {				response = conMail.validarConectividadServidor();			}*/			if (server.equalsIgnoreCase("user-registrados") ) {				response = conUser.validarConectividadServidor();			}			if(server.equalsIgnoreCase("empleados") || server.equalsIgnoreCase("pagos") || server.equalsIgnoreCase("calendario-marcas")					|| server.equalsIgnoreCase("users-profile")) {				response=conEmpl.validarConectividadServidor();			}			if (server.equalsIgnoreCase("gestion-terminal") || server.equalsIgnoreCase("listado-ventas") ) {				response = conTerminal.validarConectividadServidor();			}			if (server.equalsIgnoreCase("contabilidad") || server.equalsIgnoreCase("contable")) {				response = conContable.validarConectividadServidor();			}			if (server.equalsIgnoreCase("punto-venta")) {				response = conStock.validarConectividadServidor();				if(response.isStatus())					response=conStock.validarConectividadServidorTerminal();			}			if (server.equalsIgnoreCase("abrir-caja")) {				response = conCaja.validarConectividadServidor();				if(response.isStatus()) {					ResponseValidarEstadoCaja responseApertura = LoginController.conCaja							.validarCaja(LoginController.session.getToken(), Utils.obtenerFechaPorFormato(fechaSinHora.getFormato()));					if(!responseApertura.isStatus()) {						server="/cargar-open-dia";					}				}			}			if (!response.isStatus()) {				String json = (new Gson()).toJson(response);				responseHttp.setContentType("application/json");				responseHttp.setCharacterEncoding("UTF-8");				responseHttp.getWriter().write(json);			} else {				response.setResultado(server);				String json = (new Gson()).toJson(response);				responseHttp.setContentType("application/json");				responseHttp.setCharacterEncoding("UTF-8");				responseHttp.getWriter().write(json);			}		}	}				@GetMapping({ "/perfil-empresa" })	public String empresa(Model modelo) throws SQLException {		if (session.getToken() != null) {			Empresa empresa = Utils.obtenerDatosEmpresa();			modelo.addAttribute("user", session.getUser());			modelo.addAttribute("empresa", empresa);			ResponseMonedas responseM = LoginController.conParam.listarMonedas();			if (responseM.isStatus()) {				modelo.addAttribute("listaMoneda", responseM.getMonedas());			}else {				modelo.addAttribute("listaMoneda", new ArrayList<Moneda>());			}			return "perfil-empresa";		}		return "redirect:/";	}		@GetMapping({ "/logout" })	public String logout() {		if (session.getToken() != null) {			String val = (String) restTemplate.postForObject(conUser.getHostSeguridad() + "/login/salir", null, String.class, new Object[0]);			ResponseResultado conectividad=conStock.validarConectividadServidor();			if(conectividad.isStatus()) {				ResponseResultado response = conStock.limpiarCacheParametros();				if (response.isStatus()) {					seguridad = null;					session = new Session();				}			}else {				seguridad = null;				session = new Session();			}						return val;		}		return "redirect:/";	}	}