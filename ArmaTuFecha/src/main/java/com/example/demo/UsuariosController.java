package com.example.demo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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

import com.example.model.Usuario;

@Controller
public class UsuariosController {

	@Autowired
	private Environment env;

	@Autowired
	private UsuariosHelper UsuariosHelper;

	@GetMapping("/")
	public String paginaPrincipal () {
		
		
		return "pagina-principal";
	}
	
	@GetMapping("/{nombre}/{id_usuario}/home")
	public String paginaPrincipalLogueado (Model template, @PathVariable String nombre, @PathVariable int id_usuario) {
		template.addAttribute("nombre", nombre);
		template.addAttribute("id_usuario", id_usuario);
		
		return "pagina-principal-logueado";
	}
	
	@GetMapping ("/{nombre}/{id_usuario}/logout")
	public String cerrarSesion(Model template, @PathVariable String nombre, @PathVariable int id_usuario) {
		
		template.addAttribute("nombre", nombre);
		template.addAttribute("id_usuario", id_usuario);
		
		return "logout";
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
			template.addAttribute("provincia", provincia);
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
			template.addAttribute("provincia", provincia);
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
				template.addAttribute("provincia", provincia);
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

			
			
			connection.close();

			redirectAttribute.addFlashAttribute("mensaje_bienvenida", "Gracias por unirte!");	
				
			return "redirect:/login";
		}

	}

	@PostMapping("/procesar-perfil/musico")
	public String procesarPerfilMusico(Model template, @RequestParam String mail,
			@RequestParam String contrasenia, @RequestParam String contrasenia2, @RequestParam String nombre,
			@RequestParam String provincia, @RequestParam String localidad, @RequestParam String telefono, @RequestParam String mail_contacto, @RequestParam(required=false)String genero1, 
			@RequestParam(required=false)String genero2, @RequestParam String descripcion, @RequestParam(required=false) String foto1, @RequestParam(required=false) String foto2, @RequestParam(required=false) String foto3, 
			@RequestParam String red_social1, @RequestParam String red_social2,
			@RequestParam String red_social3, @RequestParam String link_musica1, @RequestParam String link_musica2,
			@RequestParam String link_musica3) throws SQLException {

		if (mail.length() == 0 || contrasenia.length() == 0 || contrasenia2.length() == 0
				|| nombre.length() == 0 || provincia.length() == 0 || localidad.length() == 0 || telefono.length() == 0
				|| mail_contacto.length() == 0 || descripcion.length() == 0) {

			template.addAttribute("aviso_incompleto", "Por favor completar los campos obligatorios");
			template.addAttribute("mailCargado", mail);
			template.addAttribute("nombreCargado", nombre);
			template.addAttribute("provincia", provincia);
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
			template.addAttribute("provincia", provincia);
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
				template.addAttribute("provincia", provincia);
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
					"UPDATE perfiles_musicos SET nombre = ?, provincia = ?, localidad = ?, telefono = ?, mail_contacto = ?, descripcion = ?, genero1 = ?, genero2 = ?,"
					+ "foto1 = ?, foto2 = ?, foto3 = ?, red_social1 = ?, red_social2 = ?, red_social3 = ?, link_musica1 = ?, link_musica2 = ?, link_musica3 = ? WHERE id_usuario = ?;");

			consulta.setString(1, nombre);
			consulta.setString(2, provincia);
			consulta.setString(3, localidad);
			consulta.setString(4, telefono);
			consulta.setString(5, mail_contacto);
			consulta.setString(6, descripcion);
			consulta.setString(7, genero1);
			consulta.setString(8, genero2);
			consulta.setString(9, foto1);
			consulta.setString(10, foto2);
			consulta.setString(11, foto3);
			consulta.setString(12, red_social1);
			consulta.setString(13, red_social2);
			consulta.setString(14, red_social3);
			consulta.setString(15, link_musica1);
			consulta.setString(16, link_musica2);
			consulta.setString(17, link_musica3);
			consulta.setInt(18, nuevoIdUsuario);

			consulta.executeUpdate();

			
			connection.close();

			
			return "redirect:/login";

		}

	}
	
	@GetMapping("/locales/{nombre}/{id_usuario}")
	public String perfilLocal(Model template, @PathVariable String nombre, @PathVariable int id_usuario) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT * FROM perfiles_locales WHERE id_usuario = ?;");

		consulta.setInt(1, id_usuario);

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
			

			template.addAttribute("nombre", nombre1);
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

		connection.close();
		return "perfil-local";
	}

	
	@GetMapping("/musicos/{nombre}/{id_usuario}")
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
			String genero1 = resultado.getString("genero1");
			String genero2 = resultado.getString("genero2");
			String foto1 = resultado.getString("foto1");
			String foto2 = resultado.getString("foto2");
			String foto3 = resultado.getString("foto3");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			String link_musica1 = resultado.getString("link_musica1");
			String link_musica2 = resultado.getString("link_musica2");
			String link_musica3 = resultado.getString("link_musica3");

			

			template.addAttribute("nombre", nombre);
			template.addAttribute("provincia", provincia);
			template.addAttribute("localidad", localidad);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("genero1", genero1);
			template.addAttribute("genero2", genero2);			
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

		connection.close();
		return "perfil-musico";
	}

	




	
	
	
	@GetMapping ("/{nombre}/{id_usuario}/mi-perfil")
	public String perfilLogueado(HttpSession session, Model template, @PathVariable String nombre, @PathVariable int id_usuario)
			throws SQLException {
		
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);
		

		if (logueado == null) {
			return "redirect:/login";
		}
	
		Connection connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT tipo FROM usuarios WHERE id = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();
		
			if ( resultado.next() ) {
				String tipo = resultado.getString("tipo");
	
				if ( tipo.equals("local") ) {
					
					consulta = connection.prepareStatement("SELECT * FROM perfiles_locales WHERE id_usuario = ?;");

					consulta.setInt(1, id_usuario);

					ResultSet resultado1 = consulta.executeQuery();

					if (resultado1.next()) {
						String nombre1 = resultado1.getString("nombre");
						String provincia = resultado1.getString("provincia");
						String localidad = resultado1.getString("localidad");
						String direccion = resultado1.getString("direccion");
						String telefono = resultado1.getString("telefono");
						String mail_contacto = resultado1.getString("mail_contacto");
						String descripcion = resultado1.getString("descripcion");
						String foto1 = resultado1.getString("foto1");
						String foto2 = resultado1.getString("foto2");
						String foto3 = resultado1.getString("foto3");
						String red_social1 = resultado1.getString("red_social1");
						String red_social2 = resultado1.getString("red_social2");
						String red_social3 = resultado1.getString("red_social3");
						

						// faltan fotos en template

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
	
				} else if ( tipo.equals("musico") ) {
						
					consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos WHERE id_usuario = ?;");

					consulta.setInt(1, id_usuario);

					ResultSet resultado2 = consulta.executeQuery();

					if (resultado2.next()) {
						String nombre1 = resultado2.getString("nombre");
						String provincia = resultado2.getString("provincia");
						String localidad = resultado2.getString("localidad");
						String telefono = resultado2.getString("telefono");
						String mail_contacto = resultado2.getString("mail_contacto");
						String descripcion = resultado2.getString("descripcion");
						String genero1 = resultado2.getString("genero1");
						String genero2 = resultado2.getString("genero2");
						String foto1 = resultado2.getString("foto1");
						String foto2 = resultado2.getString("foto2");
						String foto3 = resultado2.getString("foto3");
						String red_social1 = resultado2.getString("red_social1");
						String red_social2 = resultado2.getString("red_social2");
						String red_social3 = resultado2.getString("red_social3");
						String link_musica1 = resultado2.getString("link_musica1");
						String link_musica2 = resultado2.getString("link_musica2");
						String link_musica3 = resultado2.getString("link_musica3");

					

						template.addAttribute("nombre", nombre1);
						template.addAttribute("provincia", provincia);
						template.addAttribute("localidad", localidad);
						template.addAttribute("telefono", telefono);
						template.addAttribute("mail_contacto", mail_contacto);
						template.addAttribute("descripcion", descripcion);
						template.addAttribute("genero1", genero1);
						template.addAttribute("genero2", genero2);
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
				connection.close();	
			}
			return "redirect:/";
	}	
	
	

	@GetMapping("/{nombre}/{id_usuario}/mi-perfil/editar-datos-usuario")
	public String editarUsuario(HttpSession session, Model template, @PathVariable String nombre, @PathVariable int id_usuario) throws SQLException {
		
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);
		

		if (logueado == null) {
			return "redirect:/login";
		}
		
		String mail = logueado.getMail();
		String contrasenia = logueado.getContrasenia();
		
		template.addAttribute("nombre", nombre);

		template.addAttribute("mail", mail);
		template.addAttribute("contrasenia", contrasenia);

		
		return "editar-datos-usuario";
	}

	@PostMapping("/procesar-edicion-usuario/{nombre}/{id_usuario}")
	public String procesarEdicionUsuario(Model template, @PathVariable int id_usuario, @PathVariable String nombre,
			@RequestParam String mail, @RequestParam String contrasenia, @RequestParam String contrasenia2)
			throws SQLException {

		if (mail.length() == 0 || contrasenia.length() == 0 || contrasenia2.length() == 0) {
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
			PreparedStatement consulta = connection
					.prepareStatement("UPDATE usuarios SET mail = ?, contrasenia = ? WHERE id = ?;");

			consulta.setString(1, mail);
			consulta.setString(2, contrasenia);
			consulta.setInt(3, id_usuario);

			consulta.executeUpdate();
			connection.close();

		}
		template.addAttribute("nombre", nombre);

		
		return "redirect:/" + nombre + "/" + id_usuario + "/mi-perfil";
	}
	
	@GetMapping("/{nombre}/{id_usuario}/mi-perfil/editar")
	public String editarPerfil(HttpSession session, Model template, @PathVariable String nombre, @PathVariable int id_usuario) throws SQLException {
		
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);
		

		if (logueado == null) {
			return "redirect:/login";
		}
		
		
		Connection connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT tipo FROM usuarios WHERE id = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();
		
			if ( resultado.next() ) {
				String tipo = resultado.getString("tipo");

				if ( tipo.equals("local") ) {		
				
				consulta = connection.prepareStatement("SELECT * FROM perfiles_locales WHERE id_usuario = ?;");
		
				consulta.setInt(1, id_usuario);
		
				ResultSet resultado1 = consulta.executeQuery();
		
					if (resultado1.next()) {
						
						String nombre1 = resultado1.getString("nombre");
						String provincia = resultado1.getString("provincia");
						String localidad = resultado1.getString("localidad");
						String direccion = resultado1.getString("direccion");
						String telefono = resultado1.getString("telefono");
						String mail_contacto = resultado1.getString("mail_contacto");
						String descripcion = resultado1.getString("descripcion");
						String foto1 = resultado1.getString("foto1");
						String foto2 = resultado1.getString("foto2");
						String foto3 = resultado1.getString("foto3");
						String red_social1 = resultado1.getString("red_social1");
						String red_social2 = resultado1.getString("red_social2");
						String red_social3 = resultado1.getString("red_social3");
			
						template.addAttribute("nombre", nombre1);
						template.addAttribute("localidad", localidad);
						template.addAttribute("provincia", provincia);
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
						template.addAttribute("id_usuario", id_usuario);
						
					return "editar-perfil-local";
					}
					
				} else if ( tipo.equals("musico") ) {	
				
				consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos WHERE id_usuario = ?;");
				
				consulta.setInt(1, id_usuario);
		
				ResultSet resultado2 = consulta.executeQuery();
				
					if (resultado2.next()) {
						
						String nombre2 = resultado2.getString("nombre");
						String provincia = resultado2.getString("provincia");
						String localidad = resultado2.getString("localidad");
						String telefono = resultado2.getString("telefono");
						String mail_contacto = resultado2.getString("mail_contacto");
						String descripcion = resultado2.getString("descripcion");
						String foto1 = resultado2.getString("foto1");
						String foto2 = resultado2.getString("foto2");
						String foto3 = resultado2.getString("foto3");
						String genero1 = resultado2.getString("genero1");
						String genero2 = resultado2.getString("genero2");
						String red_social1 = resultado2.getString("red_social1");
						String red_social2 = resultado2.getString("red_social2");
						String red_social3 = resultado2.getString("red_social3");
						String link_musica1 = resultado2.getString("link_musica1");
						String link_musica2 = resultado2.getString("link_musica2");
						String link_musica3 = resultado2.getString("link_musica3");
		
						
		
						template.addAttribute("nombre", nombre2);
						template.addAttribute("provincia", provincia);
						template.addAttribute("localidad", localidad);
						template.addAttribute("telefono", telefono);
						template.addAttribute("mail_contacto", mail_contacto);
						template.addAttribute("descripcion", descripcion);
						template.addAttribute("foto1", foto1);
						template.addAttribute("foto2", foto2);
						template.addAttribute("foto3", foto3);
						template.addAttribute("genero1", genero1);
						template.addAttribute("genero2", genero2);
						template.addAttribute("red_social1", red_social1);
						template.addAttribute("red_social2", red_social2);
						template.addAttribute("red_social3", red_social3);
						template.addAttribute("link_musica1", link_musica1);
						template.addAttribute("link_musica2", link_musica2);
						template.addAttribute("link_musica3", link_musica3);
						template.addAttribute("id_usuario", id_usuario);

			
					return "editar-perfil-musico";
					}
						
				}
						
			}
			
			
			connection.close();
			return "redirect:/";
		}

	
	

	@PostMapping("/locales/mi-perfil/procesar-edicion/{id_usuario}")
	public String procesarEdicionLocal(Model template, @PathVariable int id_usuario, @RequestParam String nombre, 
			@RequestParam String provincia, @RequestParam String localidad,	@RequestParam String direccion, @RequestParam String telefono, @RequestParam String mail_contacto,
			@RequestParam String descripcion, @RequestParam(required=false)String foto1, @RequestParam(required=false)String foto2, @RequestParam(required=false) String foto3, @RequestParam String red_social1, @RequestParam String red_social2,
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
			template.addAttribute("aviso_fotos", "No es necesario que vuelvas a cargar las fotos!");
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
					"UPDATE perfiles_locales SET nombre = ?, provincia = ?, localidad = ?, direccion = ?, telefono = ?, mail_contacto = ?, descripcion = ?, "
					+ "foto1 = ?, foto2 = ?, foto3 = ?, red_social1 = ?, red_social2 = ?, red_social3 = ? WHERE id_usuario = ?;");

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
			consulta.setInt(14, id_usuario);
			// faltan fotos

			consulta.executeUpdate();

			consulta = connection.prepareStatement("UPDATE usuarios SET nombre = ? WHERE id = ?;");
			consulta.setString(1, nombre);
			consulta.setInt(2, id_usuario);
			
			consulta.executeUpdate();
			connection.close();

			return "redirect:/" + nombre + "/" + id_usuario + "/mi-perfil";

		}
	}

	

	@PostMapping("/musicos/mi-perfil/procesar-edicion/{id_usuario}")
	public String procesarEdicionLocal(Model template, @PathVariable int id_usuario, @RequestParam String nombre, @RequestParam String provincia, 
			@RequestParam String localidad, @RequestParam String telefono, @RequestParam String mail_contacto,
			@RequestParam String descripcion, @RequestParam(required=false)String foto1, @RequestParam(required=false)String foto2, @RequestParam(required=false) String foto3, @RequestParam(required=false)String genero1, 
			@RequestParam(required=false)String genero2, @RequestParam String red_social1, @RequestParam String red_social2,
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
			template.addAttribute("aviso_fotos", "No es necesario que vuelvas a cargar las fotos!");
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
					"UPDATE perfiles_musicos SET nombre = ?, provincia = ?, localidad = ?, telefono = ?, mail_contacto = ?, descripcion = ?, genero1 = ?, genero2 = ?, foto1 = ?, foto2 = ?, foto3 = ?,"
					+ " red_social1 = ?, red_social2 = ?, red_social3 = ?, link_musica1 = ?, link_musica2 = ?, link_musica3 = ? WHERE id_usuario = ?;");

			consulta.setString(1, nombre);
			consulta.setString(2, provincia);
			consulta.setString(3, localidad);
			consulta.setString(4, telefono);
			consulta.setString(5, mail_contacto);
			consulta.setString(6, descripcion);
			consulta.setString(7, genero1);
			consulta.setString(8, genero2);
			consulta.setString(9, foto1);
			consulta.setString(10, foto2);
			consulta.setString(11, foto3);
			consulta.setString(12, red_social1);
			consulta.setString(13, red_social2);
			consulta.setString(14, red_social3);
			consulta.setString(15, link_musica1);
			consulta.setString(16, link_musica2);
			consulta.setString(17, link_musica3);
			consulta.setInt(18, id_usuario);

			consulta.executeUpdate();
			
			consulta = connection.prepareStatement("UPDATE usuarios SET nombre = ? WHERE id = ?;");
			consulta.setString(1, nombre);
			consulta.setInt(2, id_usuario);
			
			consulta.executeUpdate();

			connection.close();

			return "redirect:/" + nombre + "/" + id_usuario + "/mi-perfil";

		}
	}
	
    
	@GetMapping("/{nombre}/{id_usuario}/mi-perfil/eliminar-cuenta")
	public String eliminarCuenta (HttpSession session, Model template, @PathVariable String nombre, @PathVariable int id_usuario) throws SQLException {
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);
		

		if (logueado == null){
			return "redirect:/login";
		}	
		
			template.addAttribute("nombre", nombre);
			template.addAttribute("id_usuario", id_usuario);
			
			
		return "eliminar-cuenta";
	}

	//procesar eliminar cuenta
	@GetMapping ("/{nombre}/{id_usuario}/eliminar-cuenta")
	public String eliminarCuentaComnfirmacion (Model template, @PathVariable String nombre, @PathVariable int id_usuario, RedirectAttributes redirectAttribute) throws SQLException {
	
	Connection connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
			env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

	PreparedStatement consulta = connection
			.prepareStatement("SELECT tipo FROM usuarios WHERE id = ?;");

	consulta.setInt(1, id_usuario);

	ResultSet resultado = consulta.executeQuery();
	
		if ( resultado.next() ) {
			String tipo = resultado.getString("tipo");

			if ( tipo.equals("local") ) {
				
				consulta = connection.prepareStatement("DELETE FROM perfiles_locales WHERE id_usuario = ?;");
				consulta.setInt(1, id_usuario);

				consulta.executeUpdate();

				consulta = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?;");
				consulta.setInt(1, id_usuario);

				consulta.executeUpdate();
				
				redirectAttribute.addFlashAttribute("cuenta_eliminada", "Perfil y datos de usuario eliminados");

				return "redirect:/";
				
			} else if ( tipo.equals("musico") ) {
				
				consulta = connection.prepareStatement("DELETE FROM perfiles_musicos WHERE id_usuario = ?;");
				consulta.setInt(1, id_usuario);

				consulta.executeUpdate();

				consulta = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?;");
				consulta.setInt(1, id_usuario);

				consulta.executeUpdate();

				connection.close();
				
				redirectAttribute.addFlashAttribute("cuenta_eliminada", "Perfil y datos de usuario eliminados");

				return "redirect:/";
			}
			connection.close();
		}
		return "redirect:/";	
				
	}
		
	
}
