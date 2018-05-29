package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.model.PerfilLocal;
import com.example.model.PerfilMusico;

@Controller
public class ListadosController {

	@Autowired
	private Environment env;

	@Autowired
	private UsuariosHelper UsuariosHelper;

	@GetMapping ("/listado2")
	public String listado() {
		return "listado"; 
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

			return "listado-musicos2";
		}
	
	
	
	
}
