package com.example.demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuariosController {

	@Autowired
	private Environment env;

	@Autowired
	private UsuariosHelper UsuariosHelper;

	@GetMapping("/")
	public String paginaPrincipal (HttpSession session, Model template, RedirectAttributes redirectAttribute) throws SQLException  {
		
		int idLogueado = UsuariosHelper.usuarioLogueado(session);

		if (idLogueado != 0) {
			
			UsuariosHelper.cerrarSesion(session);
			redirectAttribute.addFlashAttribute("mensaje_logout", "Tu sesion se ha cerrado!");	
			
			return "redirect:/";
		}
		
		return "pagina-principal";
	}
	

	@GetMapping("/crear-perfil")
	public String crearPerfil() {
		return "crear-perfil";
	}

	@PostMapping("/procesar-perfil/local")
	public String procesarPerfilLocal(Model template, @RequestParam String mail,
			@RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam String nombre, @RequestParam String provincia, 
			@RequestParam String localidad, @RequestParam String direccion, @RequestParam String telefono, @RequestParam String mail_contacto,
			@RequestParam String descripcion, @RequestParam(required=false)String foto1, @RequestParam(required=false)String foto2, @RequestParam(required=false) String foto3, 
			@RequestParam String red_social1, @RequestParam String red_social2,
			@RequestParam String red_social3, RedirectAttributes redirectAttribute) throws SQLException {

		if (mail.length() == 0 || contrasenia.length() == 0 || contrasenia2.length() == 0
				|| nombre.length() == 0 || provincia.length() == 0 || localidad.length() == 0 || direccion.length() == 0 || telefono.length() == 0
				|| mail_contacto.length() == 0 || descripcion.length() == 0) {

			template.addAttribute("aviso_incompleto", "Por favor completar los campos obligatorios");
			template.addAttribute("mailCargado", mail);
			template.addAttribute("nombreCargado", nombre);
			template.addAttribute("localidadCargada", localidad);
			template.addAttribute("direccionCargada", direccion);
			template.addAttribute("telefonoCargado", telefono);
			template.addAttribute("mail_contactoCargado", mail_contacto);
			template.addAttribute("descripcionCargada", descripcion);
			template.addAttribute("aviso_fotos", "No es necesario que vuelvas a cargar las fotos!");
			template.addAttribute("foto1", foto1);
			template.addAttribute("foto2", foto2);
			template.addAttribute("foto3", foto3);
			template.addAttribute("red_social1Cargada", red_social1);
			template.addAttribute("red_social2Cargada", red_social2);
			template.addAttribute("red_social3Cargada", red_social3);

			// faltan fotos
			return "crear-perfil";

		} else if (!contrasenia.equals(contrasenia2)) {

			template.addAttribute("aviso_contrasenia", "Las contraseñas ingresadas no coinciden.");
			template.addAttribute("mailCargado", mail);
			template.addAttribute("nombreCargado", nombre);
			template.addAttribute("localidadCargada", localidad);
			template.addAttribute("direccionCargada", direccion);
			template.addAttribute("aviso_fotos", "No es necesario que vuelvas a cargar las fotos!");
			template.addAttribute("foto1", foto1);
			template.addAttribute("foto2", foto2 );
			template.addAttribute("foto3", foto3);
			template.addAttribute("telefonoCargado", telefono);
			template.addAttribute("mail_contactoCargado", mail_contacto);
			template.addAttribute("descripcionCargada", descripcion);
			template.addAttribute("red_social1Cargada", red_social1);
			template.addAttribute("red_social2Cargada", red_social2);
			template.addAttribute("red_social3Cargada", red_social3);

			return "crear-perfil";

		} else {

			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement("SELECT mail FROM usuarios WHERE mail = ? ;");
			
			consulta.setString(1, mail);

			ResultSet resultado = consulta.executeQuery();

			if (resultado.next()) {
				String mail1 = resultado.getString("mail");

				template.addAttribute("aviso_mail", "El e-mail ingresado ya esta en uso.");
				template.addAttribute("nombreCargado", nombre);
				template.addAttribute("localidadCargada", localidad);
				template.addAttribute("direccionCargada", direccion);
				template.addAttribute("telefonoCargado", telefono);
				template.addAttribute("mail_contactoCargado", mail_contacto);
				template.addAttribute("descripcionCargada", descripcion);
				template.addAttribute("aviso_fotos", "No es necesario que vuelvas a cargar las fotos!");
				template.addAttribute("foto1", foto1);
				template.addAttribute("foto2", foto2 );
				template.addAttribute("foto3", foto3);
				template.addAttribute("red_social1Cargada", red_social1);
				template.addAttribute("red_social2Cargada", red_social2);
				template.addAttribute("red_social3Cargada", red_social3);

				return "crear-perfil";

			}
           
			consulta = connection.prepareStatement(
					"INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES (?, ?, 'local', ?);",
					PreparedStatement.RETURN_GENERATED_KEYS);

			consulta.setString(1, mail);
			consulta.setString(2, contrasenia);
			consulta.setString(3, nombre);

			consulta.executeUpdate();

			ResultSet generatedKeys = consulta.getGeneratedKeys();

			int nuevoIdUsuario = 0;
			if (generatedKeys.next()) {
				nuevoIdUsuario = generatedKeys.getInt(1);
			}

			consulta = connection.prepareStatement("INSERT INTO perfiles_locales (id_usuario) VALUES( ? );");

			consulta.setInt(1, nuevoIdUsuario);

			consulta.executeUpdate();

			consulta = connection.prepareStatement(
					"UPDATE perfiles_locales SET nombre = ?, provincia = ?, localidad = ?, direccion = ?, telefono = ?, mail_contacto = ?, descripcion = ?, foto1 = ?,"
					+ "foto2 = ?, foto3 = ?, red_social1 = ?, red_social2 = ?, red_social3 = ? WHERE id_usuario = ?;");
			consulta.setString(1, nombre);
			consulta.setString(2, provincia);
			consulta.setString(3, localidad);
			consulta.setString(4, direccion);
			consulta.setString(5, telefono);
			consulta.setString(6, mail_contacto);
			consulta.setString(7, descripcion);
			consulta.setString(8, foto1);
			consulta.setString(9, foto2);
			consulta.setString(10, foto3);
			consulta.setString(11, red_social1);
			consulta.setString(12, red_social2);
			consulta.setString(13, red_social3);
			consulta.setInt(14, nuevoIdUsuario);
			// faltan fotos

			consulta.executeUpdate();

			Email email = EmailBuilder.startingBlank()
				    .from("app", "pepe@gmail.com")
				    //se puede poner el mail del usuario que se acaba de registrar
				    .to("asd", "pepe@gmail.com")
				    .withSubject("[Arma Tu Fecha]prueba mail")
				    .withPlainText("Esto es un mensaje de prueba")
				    .buildEmail();

				MailerBuilder
				  .withSMTPServer("smtp.sendgrid.net", 587, "apikey", env.getProperty("sendgrid.apikey") )
				  .buildMailer()
				  .sendMail(email);
			
			connection.close();

			redirectAttribute.addFlashAttribute("mensaje_bienvenida", "Gracias por unirte!");	
				
			return "redirect:/login";
		}

	}

	@PostMapping("/procesar-perfil/musico")
	public String procesarPerfilMusico(Model template, @RequestParam String mail,
			@RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam String nombre,
			@RequestParam String provincia, @RequestParam String localidad, @RequestParam String telefono, @RequestParam String mail_contacto,
			@RequestParam String descripcion, @RequestParam(required=false) String foto1, @RequestParam(required=false) String foto2, @RequestParam(required=false) String foto3, 
			@RequestParam String red_social1, @RequestParam String red_social2,
			@RequestParam String red_social3, @RequestParam String link_musica1, @RequestParam String link_musica2,
			@RequestParam String link_musica3) throws SQLException {

		if (mail.length() == 0 || contrasenia.length() == 0 || contrasenia2.length() == 0
				|| nombre.length() == 0 || provincia.length() == 0 || localidad.length() == 0 || telefono.length() == 0
				|| mail_contacto.length() == 0 || descripcion.length() == 0) {

			template.addAttribute("aviso_incompleto", "Por favor completar los campos obligatorios");
			template.addAttribute("mailCargado", mail);
			template.addAttribute("nombreCargado", nombre);
			template.addAttribute("localidadCargada", localidad);
			template.addAttribute("telefonoCargado", telefono);
			template.addAttribute("mailCargado", mail_contacto);
			template.addAttribute("descripcionCargada", descripcion);
			template.addAttribute("aviso_fotos", "No es necesario que vuelvas a cargar las fotos!");
			template.addAttribute("foto1", foto1);
			template.addAttribute("foto2", foto2 );
			template.addAttribute("foto3", foto3);
			template.addAttribute("red_social1Cargada", red_social1);
			template.addAttribute("red_social2Cargada", red_social2);
			template.addAttribute("red_social3Cargada", red_social3);
			template.addAttribute("link_musicaCargado1", link_musica1);
			template.addAttribute("link_musicaCargado2", link_musica2);
			template.addAttribute("link_musicaCargado3", link_musica3);
			// faltan fotos

			return "crear-perfil";

		} else if (!contrasenia.equals(contrasenia2)) {

			template.addAttribute("aviso_contrasenia", "Las contraseñas ingresadas no coinciden.");
			template.addAttribute("mailCargado", mail);
			template.addAttribute("nombreCargado", nombre);
			template.addAttribute("localidadCargada", localidad);
			template.addAttribute("telefonoCargado", telefono);
			template.addAttribute("mail_contactoCargado", mail_contacto);
			template.addAttribute("descripcionCargada", descripcion);
			template.addAttribute("aviso_fotos", "No es necesario que vuelvas a cargar las fotos!");
			template.addAttribute("foto1", foto1);
			template.addAttribute("foto2", foto2 );
			template.addAttribute("foto3", foto3);
			template.addAttribute("red_social1Cargada", red_social1);
			template.addAttribute("red_social2Cargada", red_social2);
			template.addAttribute("red_social3Cargada", red_social3);
			template.addAttribute("link_musicaCargado1", link_musica1);
			template.addAttribute("link_musicaCargado2", link_musica2);
			template.addAttribute("link_musicaCargado3", link_musica3);

			return "crear-perfil";

		} else {

			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement("SELECT mail FROM usuarios WHERE mail =  ? ;");
			
			consulta.setString(1, mail);

			ResultSet resultado = consulta.executeQuery();

			if (resultado.next()) {
				String mail1 = resultado.getString("mail");

				template.addAttribute("aviso_mail", "El e-mail ingresado ya esta en uso.");
				template.addAttribute("nombreCargado", nombre);
				template.addAttribute("localidadCargada", localidad);
				template.addAttribute("telefonoCargado", telefono);
				template.addAttribute("mail_contactoCargado", mail_contacto);
				template.addAttribute("descripcionCargada", descripcion);
				template.addAttribute("aviso_fotos", "No es necesario que vuelvas a cargar las fotos!");
				template.addAttribute("foto1", foto1);
				template.addAttribute("foto2", foto2 );
				template.addAttribute("foto3", foto3);
				template.addAttribute("red_social1Cargada", red_social1);
				template.addAttribute("red_social2Cargada", red_social2);
				template.addAttribute("red_social3Cargada", red_social3);
				template.addAttribute("link_musicaCargado1", link_musica1);
				template.addAttribute("link_musicaCargado2", link_musica2);
				template.addAttribute("link_musicaCargado3", link_musica3);

				return "crear-perfil";
			}

			consulta = connection.prepareStatement(
					"INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES (?, ?, 'musico', ?);",
					PreparedStatement.RETURN_GENERATED_KEYS);

			consulta.setString(1, mail);
			consulta.setString(2, contrasenia);
			consulta.setString(3, nombre);

			consulta.executeUpdate();

			ResultSet generatedKeys = consulta.getGeneratedKeys();

			int nuevoIdUsuario = 0;
			if (generatedKeys.next()) {
				nuevoIdUsuario = generatedKeys.getInt(1);
			}

			consulta = connection.prepareStatement("INSERT INTO perfiles_musicos (id_usuario) VALUES( ? );");

			consulta.setInt(1, nuevoIdUsuario);

			consulta.executeUpdate();

			consulta = connection.prepareStatement(
					"UPDATE perfiles_musicos SET nombre = ?, provincia = ?, localidad = ?, telefono = ?, mail_contacto = ?, descripcion = ?, "
					+ "foto1 = ?, foto2 = ?, foto3 = ?, red_social1 = ?, red_social2 = ?, red_social3 = ?, link_musica1 = ?, link_musica2 = ?, link_musica3 = ? WHERE id_usuario = ?;");

			consulta.setString(1, nombre);
			consulta.setString(2, provincia);
			consulta.setString(3, localidad);
			consulta.setString(4, telefono);
			consulta.setString(5, mail_contacto);
			consulta.setString(6, descripcion);
			consulta.setString(7, foto1);
			consulta.setString(8, foto2);
			consulta.setString(9, foto3);
			consulta.setString(10, red_social1);
			consulta.setString(11, red_social2);
			consulta.setString(12, red_social3);
			consulta.setString(13, link_musica1);
			consulta.setString(14, link_musica2);
			consulta.setString(15, link_musica3);
			consulta.setInt(16, nuevoIdUsuario);

			consulta.executeUpdate();

			Email email = EmailBuilder.startingBlank()
				    .from("app", "pepe@gmail.com")
				    //se puede poner el mail del usuario que se acaba de registrar
				    .to("asd", "pepe@gmail.com")
				    .withSubject("[Arma Tu Fecha]prueba mail")
				    .withPlainText("Esto es un mensaje de prueba")
				    .buildEmail();

				MailerBuilder
				  .withSMTPServer("smtp.sendgrid.net", 587, "apikey", env.getProperty("sendgrid.apikey") )
				  .buildMailer()
				  .sendMail(email);
			
			connection.close();

			
			return "redirect:/login";

		}

	}

	// vista publica, modifcar ruta por /{nombre} y modificar donde haga falta
	@GetMapping("/vista-perfil-local/{id_usuario}")
	public String perfilLocal(Model template, @PathVariable int id_usuario) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT * FROM perfiles_locales WHERE id_usuario = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String nombre = resultado.getString("nombre");
			String provincia = resultado.getString("provincia");
			String localidad = resultado.getString("localidad");
			String direccion = resultado.getString("direccion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String foto1 = resultado.getString("foto1");
			String foto2 = resultado.getString("foto2");
			String foto3 = resultado.getString("foto3");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			// faltan fotos

			template.addAttribute("nombre", nombre);
			template.addAttribute("provincia", provincia);
			template.addAttribute("localidad", localidad);
			template.addAttribute("direccion", direccion);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("foto1", foto1);
			template.addAttribute("foto2", foto2);
			template.addAttribute("foto3", foto3);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);
		}

		return "perfil-local";
	}

	// vista publica, modifcar ruta por /{nombre} y modificar donde haga falta
	@GetMapping("/vista-perfil-musico/{id_usuario}")
	public String perfilMusico(Model template, @PathVariable int id_usuario) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT * FROM perfiles_musicos WHERE id_usuario = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String nombre = resultado.getString("nombre");
			String provincia = resultado.getString("provincia");
			String localidad = resultado.getString("localidad");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String foto1 = resultado.getString("foto1");
			String foto2 = resultado.getString("foto2");
			String foto3 = resultado.getString("foto3");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			String link_musica1 = resultado.getString("link_musica1");
			String link_musica2 = resultado.getString("link_musica2");
			String link_musica3 = resultado.getString("link_musica3");

			// faltan fotos

			template.addAttribute("nombre", nombre);
			template.addAttribute("provincia", provincia);
			template.addAttribute("localidad", localidad);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("foto1", foto1);
			template.addAttribute("foto2", foto2);
			template.addAttribute("foto3", foto3);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);
			template.addAttribute("link_musica1", link_musica1);
			template.addAttribute("link_musica2", link_musica2);
			template.addAttribute("link_musica3", link_musica3);
		}

		return "perfil-musico";
	}

	




	@GetMapping("/login")
	public String login(HttpSession session) throws SQLException {

		int idLogueado = UsuariosHelper.usuarioLogueado(session);

		if (idLogueado != 0) {
			
			String codigo = (String)session.getAttribute("codigo-autorizacion");
			
			Connection connection;
			connection = DriverManager.getConnection( env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password") );
			
			PreparedStatement consulta = 
					connection.prepareStatement("SELECT tipo, nombre FROM usuarios WHERE codigo = ?;");
			                                                  
			consulta.setString(1, codigo);
			
			ResultSet resultado = consulta.executeQuery();
			
				if (resultado.next()) {
					String tipo = resultado.getString("tipo");
					String nombre = resultado.getString("nombre");
	
					if (tipo.equals("musico")) {
						return "redirect:/musicos/" + nombre;
	
					} else if (tipo.equals("local")) {
						return "redirect:/locales/" + nombre;
					}
				}
			return "redirect:/";
		} else {
		
		return "login";
		
		}
	}

	@PostMapping("/procesar-login")
	public String procesarLogin(HttpSession session, Model template, @RequestParam String mail, @RequestParam String contrasenia)
			throws SQLException {
		boolean correcto = UsuariosHelper.IntentarLoguearse(session, mail, contrasenia);

		if (correcto) {

			Connection connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection
					.prepareStatement("SELECT tipo, nombre FROM usuarios WHERE mail = ?;");

			consulta.setString(1, mail);

			ResultSet resultado = consulta.executeQuery();

			if (resultado.next()) {
				String tipo = resultado.getString("tipo");
				String nombre = resultado.getString("nombre");

				if (tipo.equals("musico")) {
					return "redirect:/musicos/" + nombre;

				} else if (tipo.equals("local")) {
					return "redirect:/locales/" + nombre;
				}
			}
			return "redirect:/";
		} else {
			
			template.addAttribute("login_incorrecto", "El mail o contraseña ingresados son incorrectos");
			return "login";

		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, Model template, RedirectAttributes redirectAttribute) throws SQLException {
		UsuariosHelper.cerrarSesion(session);
		
		redirectAttribute.addFlashAttribute("mensaje_logout", "Tu sesion se ha cerrado!");
		return "redirect:/";
	}

	// modificar ruta a /{nombre}/mi perfil y modificar donde haga falta
	@GetMapping("/locales/{nombre}")
	public String perfilLocalLogueado(HttpSession session, Model template, @PathVariable String nombre)
			throws SQLException {

		int idLogueado = UsuariosHelper.usuarioLogueado(session);

		if (idLogueado == 0) {
			return "redirect:/login";
		}

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_locales WHERE nombre = ?;");

		consulta.setString(1, nombre);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String nombre1 = resultado.getString("nombre");
			String provincia = resultado.getString("provincia");
			String localidad = resultado.getString("localidad");
			String direccion = resultado.getString("direccion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String foto1 = resultado.getString("foto1");
			String foto2 = resultado.getString("foto2");
			String foto3 = resultado.getString("foto3");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			int id_usuario = resultado.getInt("id_usuario");

			// faltan fotos

			template.addAttribute("nombre", nombre1);
			template.addAttribute("direccion", direccion);
			template.addAttribute("provincia", provincia);
			template.addAttribute("localidad", localidad);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("foto1", foto1);
			template.addAttribute("foto2", foto2);
			template.addAttribute("foto3", foto3);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);
			template.addAttribute("id_usuario", id_usuario);

			consulta = connection.prepareStatement("SELECT mail, contrasenia FROM usuarios WHERE id = ?;");

			consulta.setInt(1, id_usuario);

			resultado = consulta.executeQuery();

			if (resultado.next()) {
				String mail = resultado.getString("mail");
				String contrasenia = resultado.getString("contrasenia");

				template.addAttribute("mail", mail);
				template.addAttribute("contrasenia", contrasenia);

			}
		}
		return "perfil-local-logueado";
	}

	// modificar ruta a /{nombre}/mi perfil y modificar donde haga falta
	@GetMapping("/musicos/{nombre}")
	public String perfilMusicoLogueado(HttpSession session, Model template, @PathVariable String nombre)
			throws SQLException {

		int idLogueado = UsuariosHelper.usuarioLogueado(session);

		if (idLogueado == 0) {
			return "redirect:/login";
		}

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos WHERE nombre = ?;");

		consulta.setString(1, nombre);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String nombre1 = resultado.getString("nombre");
			String provincia = resultado.getString("provincia");
			String localidad = resultado.getString("localidad");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String foto1 = resultado.getString("foto1");
			String foto2 = resultado.getString("foto2");
			String foto3 = resultado.getString("foto3");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			String link_musica1 = resultado.getString("link_musica1");
			String link_musica2 = resultado.getString("link_musica2");
			String link_musica3 = resultado.getString("link_musica3");
			int id_usuario = resultado.getInt("id_usuario");

			// faltan fotos

			template.addAttribute("nombre", nombre1);
			template.addAttribute("provincia", provincia);
			template.addAttribute("localidad", localidad);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("foto1", foto1);
			template.addAttribute("foto2", foto2);
			template.addAttribute("foto3", foto3);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);
			template.addAttribute("link_musica1", link_musica1);
			template.addAttribute("link_musica2", link_musica2);
			template.addAttribute("link_musica3", link_musica3);
			template.addAttribute("id_usuario", id_usuario);

			consulta = connection.prepareStatement("SELECT mail, contrasenia FROM usuarios WHERE id = ?;");

			consulta.setInt(1, id_usuario);

			resultado = consulta.executeQuery();

			if (resultado.next()) {
				String mail = resultado.getString("mail");
				String contrasenia = resultado.getString("contrasenia");

				template.addAttribute("mail", mail);
				template.addAttribute("contrasenia", contrasenia);

			}

		}
		return "perfil-musico-logueado";
	}

	// falta comprobacion de sesion iniciada
	@GetMapping("/editar-datos-usuario/{id_usuario}")
	public String editarUsuario(Model template, @PathVariable int id_usuario) throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM usuarios WHERE id = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String mail = resultado.getString("mail");
			String contrasenia = resultado.getString("contrasenia");

			template.addAttribute("mailCargado", mail);
			template.addAttribute("contrasenia", contrasenia);

		}

		return "editar-datos-usuario";
	}

	@PostMapping("/procesar-edicion-usuario/{id}")
	public String procesarEdicionUsuario(Model template, @PathVariable int id, @RequestParam String mail,
			@RequestParam String contrasenia, @RequestParam String contrasenia2) throws SQLException {

		if ( mail.length() == 0 || contrasenia.length() == 0 || contrasenia2.length() == 0) {
			template.addAttribute("aviso_incompleto", "Por favor completar los campos obligatorios");
			template.addAttribute("mailCargado", mail);
			return "editar-datos-usuario";

		} else if (!contrasenia.equals(contrasenia2)) {

			template.addAttribute("aviso_contrasenia", "Las contraseñas ingresadas no coinciden.");
			template.addAttribute("mailCargado", mail);
			
			return "editar-datos-usuario";
		
		} else {

			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement("SELECT mail FROM usuarios WHERE mail =  ?;");
			
			consulta.setString(1, mail);

			ResultSet resultado = consulta.executeQuery();

			if (resultado.next()) {
				String mail1 = resultado.getString("mail");

				template.addAttribute("aviso_mail", "El e-mail ingresado ya esta en uso.");
				
				return "editar-datos-usuario";
			}
	

		    consulta = connection
					.prepareStatement("UPDATE usuarios SET mail = ?, contrasenia = ? WHERE id = ?;");

			consulta.setString(1, mail);
			consulta.setString(2, contrasenia);
			consulta.setInt(3, id);

			consulta.executeUpdate();

			consulta = connection.prepareStatement("SELECT tipo, nombre FROM usuarios WHERE id = ?;");

			consulta.setInt(1, id);

			ResultSet resultado1 = consulta.executeQuery();

			if (resultado1.next()) {
				String tipo = resultado1.getString("tipo");
				String nombre = resultado1.getString("nombre");

				if (tipo.equals("musico")) {
					return "redirect:/musicos/" + nombre;

				} else if (tipo.equals("local")) {
					return "redirect:/locales/" + nombre;
				}
			}
		}
		return "redirect:/";
	}

	@GetMapping("/locales/mi-perfil/editar/{id_usuario}")
	public String editarPerfilLocal(Model template, @PathVariable int id_usuario) throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT * FROM perfiles_locales WHERE id_usuario = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String nombre = resultado.getString("nombre");
			String provincia = resultado.getString("provincia");
			String localidad = resultado.getString("localidad");
			String direccion = resultado.getString("direccion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");

			template.addAttribute("nombre", nombre);
			template.addAttribute("localidad", localidad);
			template.addAttribute("provincia", provincia);
			template.addAttribute("direccion", direccion);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);

		}

		return "editar-perfil-local";
	}

	@PostMapping("/locales/mi-perfil/procesar-edicion/{id_usuario}")
	public String procesarEdicionLocal(Model template, @PathVariable int id_usuario, @RequestParam String nombre, 
			@RequestParam String provincia, @RequestParam String localidad,	@RequestParam String direccion, @RequestParam String telefono, @RequestParam String mail_contacto,
			@RequestParam String descripcion, @RequestParam String red_social1, @RequestParam String red_social2,
			@RequestParam String red_social3) throws SQLException {

		if (nombre.length() == 0 || provincia.length() == 0 || localidad.length() == 0 || direccion.length() == 0 || telefono.length() == 0 || mail_contacto.length() == 0
				|| descripcion.length() == 0) {
			template.addAttribute("aviso_incompleto", "Por favor completar los campos obligatorios");
			template.addAttribute("nombre", nombre);
			template.addAttribute("localidad", localidad);
			template.addAttribute("direccion", direccion);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);
			// faltan fotos

			return "editar-perfil-local";

		} else {

			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement(
					"UPDATE perfiles_locales SET nombre = ?, provincia = ?, localidad = ?, direccion = ?, telefono = ?, mail_contacto = ?, descripcion = ?, red_social1 = ?, red_social2 = ?, red_social3 = ? WHERE id_usuario = ?;");

			consulta.setString(1, nombre);
			consulta.setString(2, provincia);
			consulta.setString(3, localidad);
			consulta.setString(4, direccion);
			consulta.setString(5, telefono);
			consulta.setString(6, mail_contacto);
			consulta.setString(7, descripcion);
			consulta.setString(8, red_social1);
			consulta.setString(9, red_social2);
			consulta.setString(10, red_social3);
			consulta.setInt(11, id_usuario);
			// faltan fotos

			consulta.executeUpdate();

			connection.close();

			return "redirect:/locales/" + nombre;

		}
	}

	@GetMapping("/musicos/mi-perfil/editar/{id_usuario}")
	public String editarPerfilMusico(Model template, @PathVariable int id_usuario) throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT * FROM perfiles_musicos WHERE id_usuario = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String nombre = resultado.getString("nombre");
			String provincia = resultado.getString("provincia");
			String localidad = resultado.getString("localidad");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			String link_musica1 = resultado.getString("link_musica1");
			String link_musica2 = resultado.getString("link_musica2");
			String link_musica3 = resultado.getString("link_musica3");

			// faltan fotos

			template.addAttribute("nombre", nombre);
			template.addAttribute("provincia", provincia);
			template.addAttribute("localidad", localidad);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);
			template.addAttribute("link_musica1", link_musica1);
			template.addAttribute("link_musica2", link_musica2);
			template.addAttribute("link_musica3", link_musica3);

		}

		return "editar-perfil-musico";
	}

	@PostMapping("/musicos/mi-perfil/procesar-edicion/{id_usuario}")
	public String procesarEdicionLocal(Model template, @PathVariable int id_usuario, @RequestParam String nombre, @RequestParam String provincia, 
			@RequestParam String localidad, @RequestParam String telefono, @RequestParam String mail_contacto,
			@RequestParam String descripcion, @RequestParam String red_social1, @RequestParam String red_social2,
			@RequestParam String red_social3, @RequestParam String link_musica1, @RequestParam String link_musica2,
			@RequestParam String link_musica3) throws SQLException {

		if (nombre.length() == 0 || provincia.length() == 0 || localidad.length() == 0 || telefono.length() == 0 || mail_contacto.length() == 0
				|| descripcion.length() == 0) {
			template.addAttribute("aviso_incompleto", "Por favor completar los campos obligatorios");
			template.addAttribute("nombre", nombre);
			template.addAttribute("localidad", localidad);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);
			template.addAttribute("link_musica", link_musica1);
			template.addAttribute("link_musica2", link_musica2);
			template.addAttribute("link_musica3", link_musica3);
			// faltan fotos

			return "editar-perfil-musico";
		} else {

			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement(
					"UPDATE perfiles_musicos SET nombre = ?, provincia = ?, localidad = ?, telefono = ?, mail_contacto = ?, descripcion = ?, red_social1 = ?, red_social2 = ?, red_social3 = ?, link_musica1 = ?, link_musica2 = ?, link_musica3 = ? WHERE id_usuario = ?;");

			consulta.setString(1, nombre);
			consulta.setString(2, provincia);
			consulta.setString(3, localidad);
			consulta.setString(4, telefono);
			consulta.setString(5, mail_contacto);
			consulta.setString(6, descripcion);
			consulta.setString(7, red_social1);
			consulta.setString(8, red_social2);
			consulta.setString(9, red_social3);
			consulta.setString(10, link_musica1);
			consulta.setString(11, link_musica2);
			consulta.setString(12, link_musica3);
			consulta.setInt(13, id_usuario);

			consulta.executeUpdate();

			connection.close();

			return "redirect:/musicos/" + nombre;

		}
	}
	
    //no funciona el volver
	@GetMapping("/locales/eliminar-cuenta/{id_usuario}")
	public String eliminarCuentaLocal(HttpServletRequest request, Model template, @PathVariable int id_usuario) throws MalformedURLException {
		String referer = request.getHeader("Referer");
		if (referer != null) {
			URL url = new URL(referer);
			
				template.addAttribute("volver", url.getPath());
				
				template.addAttribute("id_usuario", id_usuario);
			
		}
		return "eliminar-cuenta-local";
	}

	
	
	
	@GetMapping("/musicos/eliminar-cuenta/{id_usuario}")
	public String eliminarCuentaMusico(Model template, @PathVariable int id_usuario) {
		template.addAttribute("id_usuario", id_usuario);

		return "eliminar-cuenta-musico";
	}

	@GetMapping("/locales/confirmacion-eliminar-perfil/{id_usuario}")
	public String eliminarPerfilLocal(@PathVariable int id_usuario, RedirectAttributes redirectAttribute) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("DELETE FROM perfiles_locales WHERE id_usuario = ?;");
		consulta.setInt(1, id_usuario);

		consulta.executeUpdate();

		consulta = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?;");
		consulta.setInt(1, id_usuario);

		consulta.executeUpdate();

		connection.close();
		
		redirectAttribute.addFlashAttribute("cuenta_eliminada", "Perfil y datos de usuario eliminados");

		return "redirect:/";
	}

	@GetMapping("/musicos/confirmacion-eliminar-perfil/{id_usuario}")
	public String eliminarPerfilMusico(@PathVariable int id_usuario) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("DELETE FROM perfiles_musicos WHERE id_usuario = ?;");
		consulta.setInt(1, id_usuario);

		consulta.executeUpdate();

		consulta = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?;");
		consulta.setInt(1, id_usuario);

		consulta.executeUpdate();

		connection.close();

		return "redirect:/";
	}
}
