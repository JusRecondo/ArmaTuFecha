package com.example.model;

public class PerfilLocal {

	private int id;
	private String nombre;
	private String provincia;
	private String localidad;
	private String direccion;
	private String telefono;
	private String mail_contacto;
	private String descripcion; 
	private String foto1;
	private String foto2;
	private String foto3;
	private String red_social1;
	private String red_social2;
	private String red_social3;
	private int id_usuario;
	

	
	

	public PerfilLocal(int i, String n, String p, String l, String d, String t, String m, String des, String f1, String f2, String f3, String rd1, String rd2, String rd3, int iu){
		
		this.id = i;
		this.nombre = n;
		this.provincia = p;
		this.localidad = l;
		this.direccion = d;
		this.telefono = t;
		this.mail_contacto = m;
		this.descripcion = des;
		this.foto1 = f1;
		this.foto2 = f2;
		this.foto3 = f3;
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


	public String getProvincia() {
		return provincia;
	}



	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getLocalidad() {
		return localidad;
	}



	public void setLocalidad(String localidad) {
		this.localidad = localidad;
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
	
	public String getFoto1() {
		return foto1;
	}




	public void setFoto1(String foto1) {
		this.foto1 = foto1;
	}




	public String getFoto2() {
		return foto2;
	}




	public void setFoto2(String foto2) {
		this.foto2 = foto2;
	}




	public String getFoto3() {
		return foto3;
	}




	public void setFoto3(String foto3) {
		this.foto3 = foto3;
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
