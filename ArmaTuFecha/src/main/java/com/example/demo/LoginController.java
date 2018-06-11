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
public class LoginController {
	
	@Autowired
	private Environment env;

	@Autowired
	private UsuariosHelper UsuariosHelper;
	
	
	@GetMapping("/login")
	public String login(HttpSession session, Model template) throws SQLException {
		
		Usuario logueado = UsuariosHelper.usuarioLogueado(session);

		if (logueado != null) {
			
			String codigo = (String)session.getAttribute("codigo-autorizacion");
			
			Connection connection;
			connection = DriverManager.getConnection( env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password") );
			
			PreparedStatement consulta = 
					connection.prepareStatement("SELECT id, nombre FROM usuarios WHERE codigo = ?;");
			                                                  
			consulta.setString(1, codigo);
			
			ResultSet resultado = consulta.executeQuery();
			
				if (resultado.next()) {
					
					String nombre = resultado.getString("nombre");
					int id = resultado.getInt("id");
					
					return "redirect:/" + nombre + "/" + id + "/mi-perfil";				
				}
				
				connection.close();
		}
		
		
		return "login";
	}

	@PostMapping("/procesar-login")
	public String procesarLogin(HttpSession session, Model template, @RequestParam String mail, @RequestParam String contrasenia)
			throws SQLException {
		template.addAttribute("mail", mail);
		
		boolean correcto = UsuariosHelper.IntentarLoguearse(session, mail, contrasenia);

		if (correcto) {

			Connection connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
					env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

			PreparedStatement consulta = connection
					.prepareStatement("SELECT nombre, id FROM usuarios WHERE mail = ?;");

			consulta.setString(1, mail);

			ResultSet resultado = consulta.executeQuery();

			if (resultado.next()) {
				
				String nombre = resultado.getString("nombre");
				int id = resultado.getInt("id");
				
				return "redirect:/" + nombre + "/" + id + "/mi-perfil";				
			}
			
			connection.close();
		} else {
			
			template.addAttribute("login_incorrecto", "El mail o contraseña ingresados son incorrectos");
			template.addAttribute("mail", mail);
		
			return "login";
		}
	
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, Model template, RedirectAttributes redirectAttribute) throws SQLException {
		UsuariosHelper.cerrarSesion(session);
		
		redirectAttribute.addFlashAttribute("mensaje_logout", "Tu sesion se ha cerrado!");
		return "redirect:/";
	}

	@GetMapping ("/olvide-mi-contrasenia")
	public String olvidoContrasenia () {
		return "recuperar-contrasenia";
	}
	
	@PostMapping("/recuperar-contrasenia")
	public String recuperarContrasenia (@RequestParam String mail, RedirectAttributes redirectAttribute, Model template) throws SQLException{
		
		if (mail.length() == 0) {
			
			template.addAttribute("aviso_mail", "Por favor, ingresá primero un mail y luego hace click en recuperar contraseña");
			return "recuperar-contrasenia";
		} 
		
		String codigoRecuperacion = UUID.randomUUID().toString(); 
        
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		
		PreparedStatement consulta = connection.prepareStatement("SELECT mail FROM usuarios WHERE mail = ? ;");
		
		consulta.setString(1, mail);
		
		ResultSet resultado = consulta.executeQuery();
		
			if ( resultado.next() ) {
		
				consulta = connection.prepareStatement("UPDATE usuarios SET codigo_recuperacion = ? WHERE mail = ? ;");
				
				consulta.setString(1, codigoRecuperacion);
				consulta.setString(2, mail);
				
				consulta.executeUpdate();
				
				connection.close();
				
				
				Email email = EmailBuilder.startingBlank()
					    .from("Arma Tu Fecha", "armatufecha@gmail.com")
					    .to("Usuario", "justina.recondo@gmail.com")
					    .withSubject("[Arma Tu Fecha]Recuperar contraseña")
					    .withPlainText("Para restablecer tu contraseña, ingresá en el siguiente link: "
					    		+ "localhost:8081/recuperar-contrasenia/" + mail + "/" + codigoRecuperacion)
					    .buildEmail();
		
					MailerBuilder
					  .withSMTPServer("smtp.sendgrid.net", 587, "apikey", env.getProperty("sendgrid.apikey") )
					  .buildMailer()
					  .sendMail(email);
					
					redirectAttribute.addFlashAttribute("mensaje_contrasenia", "Te enviamos un mail de recuperación (revisa la sección de spam)");	
					return "redirect:/login";

		
		} else {
		
		return "redirect:/login";
	   }
	}
	
	@GetMapping ("/recuperar-contrasenia/{mail}/{codigoRecuperacion}")
	public String verificacionCodigo(@PathVariable String mail, @PathVariable String codigoRecuperacion, Model template, RedirectAttributes redirectAttribute) throws SQLException {
		
		
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));

		PreparedStatement consulta = connection.prepareStatement("SELECT codigo_recuperacion FROM usuarios WHERE mail = ? ;");
		
		consulta.setString(1, mail);

		ResultSet resultado = consulta.executeQuery();

		if (resultado.next()) {
			String codigoRecuperacionTabla = resultado.getString("codigo_recuperacion");
			
			if (codigoRecuperacionTabla.equals(codigoRecuperacion)) {
				
				template.addAttribute("mail", mail);
				return "nueva-contrasenia";
				} else {
				
					redirectAttribute.addFlashAttribute("mensaje_contrasenia2", "Código inválido");		
					return "redirect:/login";
				}
			}
		connection.close();
		
		return "login";
	}
	
	@PostMapping ("/restablecer-contraseña/{mail}")
	public String restablecerContrasenia(@PathVariable String mail, @RequestParam String contrasenia, @RequestParam String contrasenia2, Model template, RedirectAttributes redirectAttribute) throws SQLException {
		
		if (!contrasenia.equals(contrasenia2)) {

			template.addAttribute("aviso_contrasenia", "Las contraseñas ingresadas no coinciden.");
		
			return "redirect:/restablecer-contraseña/" + mail;
		}
		
		Connection connection;
		connection = DriverManager.getConnection(env.getProperty("spring.datasource.url"),
				env.getProperty("spring.datasource.username"), env.getProperty("spring.datasource.password"));
		PreparedStatement consulta = connection
				.prepareStatement("UPDATE usuarios SET contrasenia = ? WHERE mail = ?;");
		
		consulta.setString(1, contrasenia);
		consulta.setString(2, mail);
		
		consulta.executeUpdate();
		connection.close();
		
		redirectAttribute.addFlashAttribute("mensaje_contrasenia3", "Listo! Ahora podes loguearte con tu nueva contraseña");	
		
		return "redirect:/login";
	}

}
