package com.example.model;

public class PerfilLocal {

	private int id;
	private String nombre;
	private String direccion;
	private String telefono;
	private String mail_contacto;
	private String descripcion; 
	private String red_social1;
	private String red_social2;
	private String red_social3;
	private int id_usuario;
	//faltan fotos

	
	

	public PerfilLocal(int i, String n, String d, String t, String m, String des, String rd1, String rd2, String rd3, int iu){
		
		this.id = i;
		this.nombre = n;
		this.direccion = d;
		this.telefono = t;
		this.mail_contacto = m;
		this.descripcion = des; 
		this.red_social1 = rd1;
		this.red_social2 = rd2;
		this.red_social3 = rd3;
		this.id_usuario = iu;
		
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getNombre() {
		return nombre;
	}




	public void setNombre(String nombre) {
		this.nombre = nombre;
	}




	public String getDireccion() {
		return direccion;
	}




	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}




	public String getTelefono() {
		return telefono;
	}




	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}




	public String getMail_contacto() {
		return mail_contacto;
	}




	public void setMail_contacto(String mail_contacto) {
		this.mail_contacto = mail_contacto;
	}




	public String getDescripcion() {
		return descripcion;
	}




	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}




	public String getRed_social1() {
		return red_social1;
	}




	public void setRed_social1(String red_social1) {
		this.red_social1 = red_social1;
	}




	public String getRed_social2() {
		return red_social2;
	}




	public void setRed_social2(String red_social2) {
		this.red_social2 = red_social2;
	}




	public String getRed_social3() {
		return red_social3;
	}




	public void setRed_social3(String red_social3) {
		this.red_social3 = red_social3;
	}




	public int getId_usuario() {
		return id_usuario;
	}




	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	


}
