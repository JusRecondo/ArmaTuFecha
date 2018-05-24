package com.example.model;

public class Usuario {

		private int id;
		private String mail;
		private String contrasenia;
		private String tipo;


		public Usuario(int i, String m, String c, String t ){
			this.id = i;
			this.mail = m;
			this.contrasenia = c;
			this.tipo = t; 
			
		}


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public String getTipo() {
			return tipo;
		}


		public void setTipo(String tipo) {
			this.tipo = tipo;
		}


		public String getMail() {
			return mail;
		}


		public void setMail(String mail) {
			this.mail = mail;
		}


		public String getContrasenia() {
			return contrasenia;
		}


		public void setContrasenia(String contrasenia) {
			this.contrasenia = contrasenia;
		}
		
		



}

