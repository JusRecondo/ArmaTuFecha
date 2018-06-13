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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.PerfilLocal;
import com.example.model.PerfilMusico;
import com.example.model.Usuario;

@Controller
public class ListadosController {

	@Autowired
	private Environment env;

	@Autowired
	private UsuariosHelper UsuariosHelper;


	@GetMapping("/listado-locales/{pagina}")
	public String listadoLocales(HttpSession session, Model template, RedirectAttributes redirectAttribute, @PathVariable int pagina) throws SQLException {
		
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);
		
		if (pagina == 0) {
			return "redirect:/listado-locales/1";
		}

		if (logueado != null) {
			UsuariosHelper.cerrarSesion(session);
			
			redirectAttribute.addFlashAttribute("mensaje_logout", "Tu sesion se ha cerrado!");
			
			
			return "redirect:/";
		
		}
		
		
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_locales OFFSET ? LIMIT 3;");
		
		consulta.setInt(1, (pagina-1)*3 );
		

		ResultSet resultado = consulta.executeQuery();

		ArrayList<PerfilLocal> listadoLocales = new ArrayList<PerfilLocal>();

		while (resultado.next()) {
			int id = resultado.getInt("id");
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
			int id_usuario = resultado.getInt("id_usuario");

			PerfilLocal x = new PerfilLocal(id, nombre, provincia, localidad, direccion, telefono, mail_contacto, descripcion, foto1, foto2, foto3, red_social1,
					red_social2, red_social3, id_usuario);
			listadoLocales.add(x);
		}
		
		consulta = connection.prepareStatement("SELECT COUNT (*) AS total_perfiles FROM perfiles_locales;");		

		ResultSet resultado1 = consulta.executeQuery();
		
		if (resultado1.next()) {
			int total_perfiles = resultado1.getInt("total_perfiles");
			
			template.addAttribute("total_perfiles", total_perfiles);
		}
		
		template.addAttribute("listadoLocales", listadoLocales);
		connection.close();

		return "listado-locales";
	}
	
	
	
	
	@GetMapping("/locales/busqueda-nombre/procesar")
	public String procesarBusquedaLocalesNombre (Model template, @RequestParam String busquedaNombre) throws SQLException {
		
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		
		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_locales WHERE LOWER (nombre) LIKE ?;");
		
		consulta.setString(1, "%" + busquedaNombre.toLowerCase() + "%");
	
		ResultSet resultado = consulta.executeQuery();
		
		ArrayList<PerfilLocal> listadoLocales = new ArrayList<PerfilLocal>();
		
		while (resultado.next()) {
			int id = resultado.getInt("id");
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
			int id_usuario = resultado.getInt("id_usuario");

			PerfilLocal x = new PerfilLocal(id, nombre, provincia, localidad, direccion, telefono, mail_contacto, descripcion, foto1, foto2, foto3,
					red_social1, red_social2, red_social3, id_usuario);
			listadoLocales.add(x);
		}
		


		template.addAttribute("listadoLocales", listadoLocales);
		connection.close();

		return "resultado-busqueda-locales-nombre";
	}
	

	@GetMapping("/locales/busqueda-provincia/procesar/{pagina}")
	public String procesarBusquedaLocalesProvincia (Model template, @RequestParam String busquedaProvincia, @PathVariable int pagina) throws SQLException {
		
		if (pagina == 0) {
			return "redirect: /locales/busqueda-provincia/procesar/1";
		}
		
		template.addAttribute("busquedaProvincia", busquedaProvincia);
		
		Connection connection;
		connection = DriverManager.getConnection( env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password") );

		
		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_locales WHERE LOWER (provincia) LIKE ? OFFSET ? LIMIT 3;");
		
		consulta.setString(1, "%" + busquedaProvincia.toLowerCase() + "%");
		consulta.setInt(2, (pagina-1)*3 );
		
	
		ResultSet resultado = consulta.executeQuery();
		
		ArrayList<PerfilLocal> listadoLocales = new ArrayList<PerfilLocal>();
		
		while (resultado.next()) {
			int id = resultado.getInt("id");
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
			int id_usuario = resultado.getInt("id_usuario");

			PerfilLocal x = new PerfilLocal(id, nombre, provincia, localidad, direccion, telefono, mail_contacto, descripcion, foto1, foto2, foto3,
					red_social1, red_social2, red_social3, id_usuario);
			listadoLocales.add(x);
		}

		consulta = connection.prepareStatement("SELECT COUNT (*) AS total_perfiles FROM perfiles_locales WHERE LOWER (provincia) LIKE ?;");		

		consulta.setString(1, "%" + busquedaProvincia.toLowerCase() + "%");
		
		ResultSet resultado1 = consulta.executeQuery();
		
		if (resultado1.next()) {
			int total_perfiles = resultado1.getInt("total_perfiles");
			
			template.addAttribute("total_perfiles", total_perfiles);
		}
		template.addAttribute("listadoLocales", listadoLocales);
		connection.close();

		return "resultado-busqueda-locales-provincia";
	}
	
	@GetMapping("/locales/busqueda-localidad/procesar/{pagina}")
	public String procesarBusquedaLocalesLocalidad (Model template, @RequestParam String busquedaLocalidad, @PathVariable int pagina) throws SQLException {
		
		if (pagina == 0) {
			return "redirect: /locales/busqueda-localidad/procesar/1";
		}
		
		template.addAttribute("busquedaLocalidad", busquedaLocalidad);
		
		Connection connection;
		connection = DriverManager.getConnection( env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password") );

		
		PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_locales WHERE LOWER (localidad) LIKE ? OFFSET ? LIMIT 3;");
		
		consulta.setString(1, "%" + busquedaLocalidad.toLowerCase() + "%");
		consulta.setInt(2, (pagina-1)*3 );
		
	
		ResultSet resultado = consulta.executeQuery();
		
		ArrayList<PerfilLocal> listadoLocales = new ArrayList<PerfilLocal>();
		
		while (resultado.next()) {
			int id = resultado.getInt("id");
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
			int id_usuario = resultado.getInt("id_usuario");

			PerfilLocal x = new PerfilLocal(id, nombre, provincia, localidad, direccion, telefono, mail_contacto, descripcion, foto1, foto2, foto3,
					red_social1, red_social2, red_social3, id_usuario);
			listadoLocales.add(x);
		}

		consulta = connection.prepareStatement("SELECT COUNT (*) AS total_perfiles FROM perfiles_locales WHERE LOWER (localidad) LIKE ?;");		

		consulta.setString(1, "%" + busquedaLocalidad.toLowerCase() + "%");
		
		ResultSet resultado1 = consulta.executeQuery();
		
		if (resultado1.next()) {
			int total_perfiles = resultado1.getInt("total_perfiles");
			
			template.addAttribute("total_perfiles", total_perfiles);
		}
		
		template.addAttribute("listadoLocales", listadoLocales);
		connection.close();

		return "resultado-busqueda-locales-localidad";
	}
	
	
		@GetMapping("/listado-musicos/{pagina}")
		public String listadoMusicos(HttpSession session, Model template, RedirectAttributes redirectAttribute, @PathVariable int pagina) throws SQLException {

			Usuario logueado = UsuariosHelper.usuarioLogueado(session);
			
			if (pagina == 0) {
				return "redirect:/listado-locales/1";
			}

				
			if (logueado != null) {
				UsuariosHelper.cerrarSesion(session);
				
				redirectAttribute.addFlashAttribute("mensaje_logout", "Tu sesion se ha cerrado!");
				
				
				return "redirect:/";
			
			}
			
			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos OFFSET ? LIMIT 3;");
			
			consulta.setInt(1, (pagina-1)*3 );

			ResultSet resultado = consulta.executeQuery();

			ArrayList<PerfilMusico> listadoMusicos = new ArrayList<PerfilMusico>();

			while (resultado.next()) {
				int id = resultado.getInt("id");
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
				int id_usuario = resultado.getInt("id_usuario");

				PerfilMusico x = new PerfilMusico(id, nombre, provincia, localidad,  telefono, mail_contacto, descripcion, genero1, genero2, foto1, foto2, foto3,
						red_social1, red_social2, red_social3, link_musica1, link_musica2, link_musica3, id_usuario);
				listadoMusicos.add(x);
			}
			
			consulta = connection.prepareStatement("SELECT COUNT (*) AS total_perfiles FROM perfiles_musicos;");		

			ResultSet resultado1 = consulta.executeQuery();
			
			if (resultado1.next()) {
				int total_perfiles = resultado1.getInt("total_perfiles");
				
				template.addAttribute("total_perfiles", total_perfiles);
			}
			
			template.addAttribute("listadoMusicos", listadoMusicos);
			connection.close();

			return "listado-musicos";
		}
	
	
		@GetMapping("/musicos/busqueda-nombre/procesar")
		public String procesarBusquedaMusicosNombre (Model template, @RequestParam String busquedaNombre) throws SQLException {
			
			Connection connection;
			connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			
			PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos WHERE LOWER (nombre) LIKE ?;");
			
			consulta.setString(1, "%" + busquedaNombre.toLowerCase() + "%");
		
			ResultSet resultado = consulta.executeQuery();
			
			ArrayList<PerfilMusico> listadoMusicos = new ArrayList<PerfilMusico>();
			
			while (resultado.next()) {
				int id = resultado.getInt("id");
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
				int id_usuario = resultado.getInt("id_usuario");

				PerfilMusico x = new PerfilMusico(id, nombre, provincia, localidad,  telefono, mail_contacto, descripcion, genero1, genero2, foto1, foto2, foto3,
						red_social1, red_social2, red_social3, link_musica1, link_musica2, link_musica3, id_usuario);
				listadoMusicos.add(x);
			}

			template.addAttribute("listadoMusicos", listadoMusicos);
			connection.close();

			return "resultado-busqueda-musicos-nombre";
		}
		

		@GetMapping("/musicos/busqueda-provincia/procesar/{pagina}")
		public String procesarBusquedaMusicosProvincia (Model template, @RequestParam String busquedaProvincia, @PathVariable int pagina) throws SQLException {
			
			if (pagina == 0) {
				return "redirect: /locales/busqueda-provincia/procesar/1";
			}
			
			template.addAttribute("busquedaProvincia", busquedaProvincia);
			
			Connection connection;
			connection = DriverManager.getConnection( env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password") );

			
			PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos WHERE LOWER (provincia) LIKE ? OFFSET ? LIMIT 3;");
			
			consulta.setString(1, "%" + busquedaProvincia.toLowerCase() + "%");
			consulta.setInt(2, (pagina-1)*3 );
		
			ResultSet resultado = consulta.executeQuery();
			
			ArrayList<PerfilMusico> listadoMusicos = new ArrayList<PerfilMusico>();
			
			while (resultado.next()) {
				int id = resultado.getInt("id");
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
				int id_usuario = resultado.getInt("id_usuario");

				PerfilMusico x = new PerfilMusico(id, nombre, provincia, localidad,  telefono, mail_contacto, descripcion, genero1, genero2, foto1, foto2, foto3,
						red_social1, red_social2, red_social3, link_musica1, link_musica2, link_musica3, id_usuario);
				listadoMusicos.add(x);
			}

			consulta = connection.prepareStatement("SELECT COUNT (*) AS total_perfiles FROM perfiles_musicos WHERE LOWER (provincia) LIKE ?;");		

			consulta.setString(1, "%" + busquedaProvincia.toLowerCase() + "%");
			
			ResultSet resultado1 = consulta.executeQuery();
			
			if (resultado1.next()) {
				int total_perfiles = resultado1.getInt("total_perfiles");
				
				template.addAttribute("total_perfiles", total_perfiles);
			}
			
			template.addAttribute("listadoMusicos", listadoMusicos);
			connection.close();

			return "resultado-busqueda-musicos-provincia";
		}
		
		@GetMapping("/musicos/busqueda-localidad/procesar/{pagina}")
		public String procesarBusquedaMusicosLocalidad (Model template, @RequestParam String busquedaLocalidad, @PathVariable int pagina) throws SQLException {
			
			if (pagina == 0) {
				return "redirect: /locales/busqueda-localidad/procesar/1";
			}
			
			template.addAttribute("busquedaLocalidad", busquedaLocalidad);
			
			Connection connection;
			connection = DriverManager.getConnection( env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password") );

			
			PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos WHERE LOWER (localidad) LIKE ? OFFSET ? LIMIT 3;");
			
			consulta.setString(1, "%" + busquedaLocalidad.toLowerCase() + "%");
			consulta.setInt(2, (pagina-1)*3 );
			
		
			ResultSet resultado = consulta.executeQuery();
			
			ArrayList<PerfilMusico> listadoMusicos = new ArrayList<PerfilMusico>();
			
			while (resultado.next()) {
				int id = resultado.getInt("id");
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
				int id_usuario = resultado.getInt("id_usuario");

				PerfilMusico x = new PerfilMusico(id, nombre, provincia, localidad,  telefono, mail_contacto, descripcion, genero1, genero2, foto1, foto2, foto3,
						red_social1, red_social2, red_social3, link_musica1, link_musica2, link_musica3, id_usuario);
				listadoMusicos.add(x);
			}

			consulta = connection.prepareStatement("SELECT COUNT (*) AS total_perfiles FROM perfiles_musicos WHERE LOWER (localidad) LIKE ?;");		

			consulta.setString(1, "%" + busquedaLocalidad.toLowerCase() + "%");
			
			ResultSet resultado1 = consulta.executeQuery();
			
			if (resultado1.next()) {
				int total_perfiles = resultado1.getInt("total_perfiles");
				
				template.addAttribute("total_perfiles", total_perfiles);
			}
			
			template.addAttribute("listadoMusicos", listadoMusicos);
			connection.close();

			return "resultado-busqueda-musicos-localidad";
		}
		
		@GetMapping ("/musicos/busqueda-genero/procesar/{pagina}")
		public String procesarBusquedaMusicosGenero (Model template, @RequestParam String busquedaGenero, @PathVariable int pagina) throws SQLException {
			
			if (pagina == 0) {
				return "redirect: /locales/busqueda-genero/procesar/1";
			}
			
			template.addAttribute("busquedaGenero", busquedaGenero);
			
			Connection connection;
			connection = DriverManager.getConnection( env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password") );

			
			PreparedStatement consulta = connection.prepareStatement("SELECT * FROM perfiles_musicos WHERE LOWER (genero1) LIKE ? OFFSET ? LIMIT 3;");
			
			consulta.setString(1, "%" + busquedaGenero.toLowerCase() + "%");
			consulta.setInt(2, (pagina-1)*3 );
			
		
			ResultSet resultado = consulta.executeQuery();
			
			ArrayList<PerfilMusico> listadoMusicos = new ArrayList<PerfilMusico>();
			
			while (resultado.next()) {
				int id = resultado.getInt("id");
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
				int id_usuario = resultado.getInt("id_usuario");

				PerfilMusico x = new PerfilMusico(id, nombre, provincia, localidad,  telefono, mail_contacto, descripcion, genero1, genero2, foto1, foto2, foto3,
						red_social1, red_social2, red_social3, link_musica1, link_musica2, link_musica3, id_usuario);
				listadoMusicos.add(x);
			}

			consulta = connection.prepareStatement("SELECT COUNT (*) AS total_perfiles FROM perfiles_musicos WHERE LOWER (genero1) LIKE ?;");		

			consulta.setString(1, "%" + busquedaGenero.toLowerCase() + "%");
			
			ResultSet resultado1 = consulta.executeQuery();
			
			if (resultado1.next()) {
				int total_perfiles = resultado1.getInt("total_perfiles");
				
				template.addAttribute("total_perfiles", total_perfiles);
			}
			
			template.addAttribute("listadoMusicos", listadoMusicos);
			connection.close();

			return "resultado-busqueda-musicos-genero";
		}
}
