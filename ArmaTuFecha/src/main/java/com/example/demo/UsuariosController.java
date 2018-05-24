package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.PerfilLocal;
import com.example.model.PerfilMusico;

@Controller
public class UsuariosController {

	@Autowired
	private Environment env;

	@Autowired
	private UsuariosHelper UsuariosHelper;

	@GetMapping("/arma-tu-fecha")
	public String paginaPrincipal() {
		return "pagina-principal";
	}

	// TODO poner links en el nav bar de la vista perfil logueado
	@GetMapping("/arma-tu-fecha/local/{id_usuario}")
	public String paginaPrincipalLocalLogueado(Model template, @PathVariable int id_usuario) {
		template.addAttribute("id_usuario", id_usuario);
		return "pagina-principal-local-logueado";
	}

	@GetMapping("/arma-tu-fecha/musico/{id_usuario}")
	public String paginaPrincipalMusicoLogueado(Model template, @PathVariable int id_usuario) {
		template.addAttribute("id_usuario", id_usuario);
		return "pagina-principal-musico-logueado";
	}

	@GetMapping("/registro")
	public String elegirPerfil() {
		return "registro";
	}

	@PostMapping("/procesar-perfil/local")
	public String procesarPerfilLocal(Model template, @RequestParam String mail, @RequestParam String contrasenia,
			@RequestParam String nombre, @RequestParam String direccion, @RequestParam String telefono,
			@RequestParam String mail_contacto, @RequestParam String descripcion, @RequestParam String red_social1,
			@RequestParam String red_social2, @RequestParam String red_social3) throws SQLException {

		if (nombre.length() == 0 || direccion.length() == 0 || telefono.length() == 0 || mail_contacto.length() == 0
				|| descripcion.length() == 0) {
			template.addAttribute("nombreCargado", nombre);
			template.addAttribute("direccionCargada", direccion);
			template.addAttribute("telefonoCargado", telefono);
			template.addAttribute("mailCargado", mail_contacto);
			template.addAttribute("descripcionCargada", descripcion);
			template.addAttribute("redSocial1Cargada", red_social1);
			template.addAttribute("redSocial2Cargada", red_social2);
			template.addAttribute("redSocial3Cargada", red_social3);
			// faltan fotos
			return "registro";
		} else {

			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement("SELECT mail FROM usuarios;");
			
			ResultSet resultado = consulta.executeQuery();

			if (resultado.next()) {
				String mail1 = resultado.getString ("mail");
				
				
				template.addAttribute("aviso_mail", "El e-mail ingresado ya esta en uso.");
				template.addAttribute("nombreCargado", nombre);
				template.addAttribute("direccionCargada", direccion);
				template.addAttribute("telefonoCargado", telefono);
				template.addAttribute("mailCargado", mail_contacto);
				template.addAttribute("descripcionCargada", descripcion);
				template.addAttribute("redSocial1Cargada", red_social1);
				template.addAttribute("redSocial2Cargada", red_social2);
				template.addAttribute("redSocial3Cargada", red_social3);
				return "registro";
			}
			
			
			consulta = connection.prepareStatement(
					"INSERT INTO usuarios (mail, contrasenia, tipo) VALUES (?, ?, 'local');",
					PreparedStatement.RETURN_GENERATED_KEYS);

			consulta.setString(1, mail);
			consulta.setString(2, contrasenia);

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
					"UPDATE perfiles_locales SET nombre = ?, direccion = ?, telefono = ?, mail_contacto = ?, descripcion = ?, red_social1 = ?, red_social2 = ?, red_social3 = ? WHERE id_usuario = ?;");
			consulta.setString(1, nombre);
			consulta.setString(2, direccion);
			consulta.setString(3, telefono);
			consulta.setString(4, mail_contacto);
			consulta.setString(5, descripcion);
			consulta.setString(6, red_social1);
			consulta.setString(7, red_social2);
			consulta.setString(8, red_social3);
			consulta.setInt(9, nuevoIdUsuario);
			// faltan fotos

			consulta.executeUpdate();

			connection.close();

			return "redirect:/login";

		}

	}

	@PostMapping("/procesar-perfil/musico")
	public String procesarPerfilMusico(Model template, @RequestParam String mail, @RequestParam String contrasenia,
			@RequestParam String nombre, @RequestParam String ubicacion, @RequestParam String telefono,
			@RequestParam String mail_contacto, @RequestParam String descripcion, @RequestParam String red_social1,
			@RequestParam String red_social2, @RequestParam String red_social3, @RequestParam String link_musica1,
			@RequestParam String link_musica2, @RequestParam String link_musica3) throws SQLException {

		if (nombre.length() == 0 || ubicacion.length() == 0 || telefono.length() == 0 || mail_contacto.length() == 0
				|| descripcion.length() == 0) {
			template.addAttribute("nombreCargado", nombre);
			template.addAttribute("ubicacionCargada", ubicacion);
			template.addAttribute("telefonoCargado", telefono);
			template.addAttribute("mailCargado", mail_contacto);
			template.addAttribute("descripcionCargada", descripcion);
			template.addAttribute("redSocial1Cargada", red_social1);
			template.addAttribute("redSocial2Cargada", red_social2);
			template.addAttribute("redSocial3Cargada", red_social3);
			template.addAttribute("linkMusicaCargado1", link_musica1);
			template.addAttribute("linkMusicaCargado2", link_musica2);
			template.addAttribute("linkMusicaCargado3", link_musica3);
			// faltan fotos

			return "formulario-musico";
		} else {

			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement(
					"INSERT INTO usuarios (mail, contrasenia, tipo) VALUES (?, ?, 'musico');",
					PreparedStatement.RETURN_GENERATED_KEYS);

			consulta.setString(1, mail);
			consulta.setString(2, contrasenia);

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
					"UPDATE perfiles_musicos SET nombre = ?, ubicacion = ?, telefono = ?, mail_contacto = ?, descripcion = ?, red_social1 = ?, red_social2 = ?, red_social3 = ?, link_musica1 = ?, link_musica2 = ?, link_musica3 = ? WHERE id_usuario = ?;");
			consulta.setString(1, nombre);
			consulta.setString(2, ubicacion);
			consulta.setString(3, telefono);
			consulta.setString(4, mail_contacto);
			consulta.setString(5, descripcion);
			consulta.setString(6, red_social1);
			consulta.setString(7, red_social2);
			consulta.setString(8, red_social3);
			consulta.setString(9, link_musica1);
			consulta.setString(10, link_musica2);
			consulta.setString(11, link_musica3);
			consulta.setInt(12, nuevoIdUsuario);

			consulta.executeUpdate();

			connection.close();

			return "redirect:/login";

		}

	}

	// vista publica
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
			String direccion = resultado.getString("direccion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			// faltan fotos

			template.addAttribute("nombre", nombre);
			template.addAttribute("direccion", direccion);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);
		}

		return "vista-perfil-local";
	}

	// vista publica
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
			String ubicacion = resultado.getString("ubicacion");
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
			template.addAttribute("ubicacion", ubicacion);
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

		return "vista-perfil-musico";
	}

	@GetMapping("/listado-locales")
	public String listadoLocales(Model template) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_locales;");

		ResultSet resultado = consulta.executeQuery();

		ArrayList<PerfilLocal> listadoLocales = new ArrayList<PerfilLocal>();

		while (resultado.next()) {
			int id = resultado.getInt("id");
			String nombre = resultado.getString("nombre");
			String direccion = resultado.getString("direccion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			int id_usuario = resultado.getInt("id_usuario");

			PerfilLocal x = new PerfilLocal(id, nombre, direccion, telefono, mail_contacto, descripcion, red_social1,
					red_social2, red_social3, id_usuario);
			listadoLocales.add(x);
		}

		template.addAttribute("listadoLocales", listadoLocales);

		return "listado-locales";
	}

	// ejemplo template con fragments
	@GetMapping("/listado-locales1")
	public String listadoLocales1(Model template) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_locales;");

		ResultSet resultado = consulta.executeQuery();

		ArrayList<PerfilLocal> listadoLocales = new ArrayList<PerfilLocal>();

		while (resultado.next()) {
			int id = resultado.getInt("id");
			String nombre = resultado.getString("nombre");
			String direccion = resultado.getString("direccion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			int id_usuario = resultado.getInt("id_usuario");

			PerfilLocal x = new PerfilLocal(id, nombre, direccion, telefono, mail_contacto, descripcion, red_social1,
					red_social2, red_social3, id_usuario);
			listadoLocales.add(x);
		}

		template.addAttribute("listadoLocales", listadoLocales);

		return "listadolocales1";
	}

	@GetMapping("/listado-musicos")
	public String listadoMusicos(Model template) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos;");

		ResultSet resultado = consulta.executeQuery();

		ArrayList<PerfilMusico> listadoMusicos = new ArrayList<PerfilMusico>();

		while (resultado.next()) {
			int id = resultado.getInt("id");
			String nombre = resultado.getString("nombre");
			String ubicacion = resultado.getString("ubicacion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");
			String link_musica1 = resultado.getString("link_musica1");
			String link_musica2 = resultado.getString("link_musica2");
			String link_musica3 = resultado.getString("link_musica3");
			int id_usuario = resultado.getInt("id_usuario");

			PerfilMusico x = new PerfilMusico(id, nombre, ubicacion, telefono, mail_contacto, descripcion, red_social1,
					red_social2, red_social3, link_musica1, link_musica2, link_musica3, id_usuario);
			listadoMusicos.add(x);
		}

		template.addAttribute("listadoMusicos", listadoMusicos);

		return "listado-musicos";
	}

	@GetMapping("/index")
	public String estructura() {
		return "index";
	}

	// pruebas fragments

	@GetMapping("/A")
	public String A() {
		return "A";

	}

	@GetMapping("/prueba-formulario")
	public String pruebaFormulario() {
		return "prueba-formulario";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/procesar-login")
	public String procesarLogin(HttpSession session, @RequestParam String mail, @RequestParam String contrasenia)
			throws SQLException {
		boolean correcto = UsuariosHelper.IntentarLoguearse(session, mail, contrasenia);

		if (correcto) {

			Connection connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement("SELECT id, tipo FROM usuarios WHERE mail = ?;");

			consulta.setString(1, mail);

			ResultSet resultado = consulta.executeQuery();

			if (resultado.next()) {
				int id = resultado.getInt("id");
				String tipo = resultado.getString("tipo");

				if (tipo.equals("musico")) {
					return "redirect:/musicos/mi-perfil/" + id;

				} else if (tipo.equals("local")) {
					return "redirect:/locales/mi-perfil/" + id;
				}
			}
			return "redirect:/arma-tu-fecha ";
		} else {

			return "redirect:/login";

		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) throws SQLException {
		UsuariosHelper.cerrarSesion(session);
		return "redirect:/login";
	}

	@GetMapping("/locales/mi-perfil/{id_usuario}")
	public String perfilLocalLogueado(HttpSession session, Model template, @PathVariable int id_usuario)
			throws SQLException {

		// int idLogueado = UsuariosHelper.usuarioLogueado(session);

		// if (idLogueado == 0) {
		// return "redirect:/login";
		// }

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT * FROM perfiles_locales WHERE id_usuario = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String nombre = resultado.getString("nombre");
			String direccion = resultado.getString("direccion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");

			// faltan fotos

			template.addAttribute("nombre", nombre);
			template.addAttribute("direccion", direccion);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_social3", red_social3);

		}

		consulta = connection.prepareStatement("SELECT mail, contrasenia FROM usuarios WHERE id = ?;");

		consulta.setInt(1, id_usuario);

		resultado = consulta.executeQuery();

		if (resultado.next()) {
			String mail = resultado.getString("mail");
			String contrasenia = resultado.getString("contrasenia");

			template.addAttribute("mail", mail);
			template.addAttribute("contrasenia", contrasenia);

		}

		return "vista-perfil-local-logueado";
	}

	@GetMapping("/musicos/mi-perfil/{id_usuario}")
	public String perfilMusicoLogueado(HttpSession session, Model template, @PathVariable int id_usuario)
			throws SQLException {

		// int idLogueado = UsuariosHelper.usuarioLogueado(session);

		// if (idLogueado == 0) {
		// return "redirect:/login";
		// }

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("SELECT * FROM perfiles_musicos WHERE id_usuario = ?;");

		consulta.setInt(1, id_usuario);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String nombre = resultado.getString("nombre");
			String ubicacion = resultado.getString("ubicacion");
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
			template.addAttribute("ubicacion", ubicacion);
			template.addAttribute("telefono", telefono);
			template.addAttribute("mail_contacto", mail_contacto);
			template.addAttribute("descripcion", descripcion);
			template.addAttribute("red_social1", red_social1);
			template.addAttribute("red_social2", red_social2);
			template.addAttribute("red_Social3", red_social3);
			template.addAttribute("link_musica1", link_musica1);
			template.addAttribute("link_musica2", link_musica2);
			template.addAttribute("link_musica3", link_musica3);
		}

		consulta = connection.prepareStatement("SELECT mail, contrasenia FROM usuarios WHERE id = ?;");

		consulta.setInt(1, id_usuario);

		resultado = consulta.executeQuery();

		if (resultado.next()) {
			String mail = resultado.getString("mail");
			String contrasenia = resultado.getString("contrasenia");

			template.addAttribute("mail", mail);
			template.addAttribute("contrasenia", contrasenia);

		}

		return "vista-perfil-musico-logueado";
	}

	// falta comprobacion de sesion iniciada
	@GetMapping("/editar-datos-usuario/{id}")
	public String editarUsuario(Model template, @PathVariable int id) throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM usuarios WHERE id = ?;");

		consulta.setInt(1, id);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String mail = resultado.getString("mail");
			String contrasenia = resultado.getString("contrasenia");

			template.addAttribute("mail", mail);
			template.addAttribute("contrasenia", contrasenia);

		}

		return "editar-usuario";
	}

	@PostMapping("/procesar-edicion-usuario/{id}")
	public String procesarEdicionUsuario(Model template, @PathVariable int id, @RequestParam String mail,
			@RequestParam String contrasenia) throws SQLException {
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection
				.prepareStatement("UPDATE usuarios SET mail = ?, contrasenia = ? WHERE id = ?;");

		consulta.setString(1, mail);
		consulta.setString(2, contrasenia);
		consulta.setInt(3, id);

		consulta.executeUpdate();

		consulta = connection.prepareStatement("SELECT tipo FROM usuarios WHERE id = ?;");

		consulta.setInt(1, id);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String tipo = resultado.getString("tipo");

			if (tipo.equals("musico") ) {
				return "redirect:/musicos/mi-perfil/" + id;

			} else if (tipo.equals("local") ) {
				return "redirect:/locales/mi-perfil/" + id;
			}
		}

		return "redirect:/arma-tu-fecha";
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
			String direccion = resultado.getString("direccion");
			String telefono = resultado.getString("telefono");
			String mail_contacto = resultado.getString("mail_contacto");
			String descripcion = resultado.getString("descripcion");
			String red_social1 = resultado.getString("red_social1");
			String red_social2 = resultado.getString("red_social2");
			String red_social3 = resultado.getString("red_social3");

			template.addAttribute("nombre", nombre);
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
			@RequestParam String direccion, @RequestParam String telefono, @RequestParam String mail_contacto,
			@RequestParam String descripcion, @RequestParam String red_social1, @RequestParam String red_social2,
			@RequestParam String red_social3) throws SQLException {

		if (nombre.length() == 0 || direccion.length() == 0 || telefono.length() == 0 || mail_contacto.length() == 0
				|| descripcion.length() == 0) {

			template.addAttribute("nombre", nombre);
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
					"UPDATE perfiles_locales SET nombre = ?, direccion = ?, telefono = ?, mail_contacto = ?, descripcion = ?, red_social1 = ?, red_social2 = ?, red_social3 = ? WHERE id_usuario = ?;");

			consulta.setString(1, nombre);
			consulta.setString(2, direccion);
			consulta.setString(3, telefono);
			consulta.setString(4, mail_contacto);
			consulta.setString(5, descripcion);
			consulta.setString(6, red_social1);
			consulta.setString(7, red_social2);
			consulta.setString(8, red_social3);
			consulta.setInt(9, id_usuario);
			// faltan fotos

			consulta.executeUpdate();

			connection.close();

			return "redirect:/locales/mi-perfil/" + id_usuario;

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
			String ubicacion = resultado.getString("ubicacion");
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
			template.addAttribute("ubicacion", ubicacion);
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
	public String procesarEdicionLocal(Model template, @PathVariable int id_usuario, @RequestParam String nombre,
			@RequestParam String ubicacion, @RequestParam String telefono, @RequestParam String mail_contacto,
			@RequestParam String descripcion, @RequestParam String red_social1, @RequestParam String red_social2,
			@RequestParam String red_social3, @RequestParam String link_musica1, @RequestParam String link_musica2,
			@RequestParam String link_musica3) throws SQLException {

		if (nombre.length() == 0 || ubicacion.length() == 0 || telefono.length() == 0 || mail_contacto.length() == 0
				|| descripcion.length() == 0) {
			template.addAttribute("nombre", nombre);
			template.addAttribute("ubicacion", ubicacion);
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
					"UPDATE perfiles_musicos SET nombre = ?, ubicacion = ?, telefono = ?, mail_contacto = ?, descripcion = ?, red_social1 = ?, red_social2 = ?, red_social3 = ?, link_musica1 = ?, link_musica2 = ?, link_musica3 = ? WHERE id_usuario = ?;");

			consulta.setString(1, nombre);
			consulta.setString(2, ubicacion);
			consulta.setString(3, telefono);
			consulta.setString(4, mail_contacto);
			consulta.setString(5, descripcion);
			consulta.setString(6, red_social1);
			consulta.setString(7, red_social2);
			consulta.setString(8, red_social3);
			consulta.setString(9, link_musica1);
			consulta.setString(10, link_musica2);
			consulta.setString(11, link_musica3);
			consulta.setInt(12, id_usuario);

			consulta.executeUpdate();

			connection.close();

			return "redirect:/musicos/mi-perfil/" + id_usuario;

		}
	}

	@GetMapping("/locales/eliminar-cuenta/{id_usuario}")
	public String eliminarCuentaLocal(Model template, @PathVariable int id_usuario) {
		template.addAttribute("id_usuario", id_usuario);

		return "eliminar-cuenta-local";
	}

	@GetMapping("/musicos/eliminar-cuenta/id_usuario")
	public String eliminarCuentaMusico(Model template, @PathVariable String id_usuario) {
		template.addAttribute("id_usuario", id_usuario);
		return "eliminar-cuenta-musico";
	}

	@GetMapping("/locales/confirmacion-eliminar-perfil/{id_usuario}")
	public String eliminarPerfilLocal(@PathVariable int id_usuario) throws SQLException {

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

		return "redirect:/arma-tu-fecha";
	}

	@GetMapping("/musicos/confirmacion-eliminar-perfil/{id_usuario}")
	public String eliminarPerfilMusico(@PathVariable int id_usuario) throws SQLException {

		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("DELETE FROM perfiles_musicos WHERE id_usuario = ?");
		consulta.setInt(1, id_usuario);

		consulta.executeUpdate();

		consulta = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?");
		consulta.setInt(1, id_usuario);

		consulta.executeUpdate();

		connection.close();

		return "redirect:/arma-tu-fecha";
	}
}
