CREATE SEQUENCE public.usuarios_id_seq
    INCREMENT 1
    START 38
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.usuarios_id_seq
    OWNER TO postgres;
    
    
-- Table: public.usuarios

-- DROP TABLE public.usuarios;

CREATE TABLE public.usuarios
(
    id integer NOT NULL DEFAULT nextval('usuarios_id_seq'::regclass),
    mail text COLLATE pg_catalog."default" NOT NULL,
    contrasenia text COLLATE pg_catalog."default" NOT NULL,
    codigo text COLLATE pg_catalog."default",
    tipo text COLLATE pg_catalog."default" NOT NULL,
    nombre text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT usuarios_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.usuarios
    OWNER to postgres;
    
    
CREATE SEQUENCE public.perfiles_locales_id_seq
    INCREMENT 1
    START 21
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.perfiles_locales_id_seq
    OWNER TO postgres;
    
-- Table: public.perfiles_locales

-- DROP TABLE public.perfiles_locales;

CREATE TABLE public.perfiles_locales
(
    id integer NOT NULL DEFAULT nextval('perfiles_locales_id_seq'::regclass),
    nombre text COLLATE pg_catalog."default",
    provincia text COLLATE pg_catalog."default",
    localidad text COLLATE pg_catalog."default",
    direccion text COLLATE pg_catalog."default",
    telefono text COLLATE pg_catalog."default",
    mail_contacto text COLLATE pg_catalog."default",
    descripcion character varying(5000) COLLATE pg_catalog."default",
    foto1 text COLLATE pg_catalog."default",
    foto2 text COLLATE pg_catalog."default",
    foto3 text COLLATE pg_catalog."default",
    red_social1 text COLLATE pg_catalog."default",
    red_social2 text COLLATE pg_catalog."default",
    red_social3 text COLLATE pg_catalog."default",
    id_usuario integer,
    CONSTRAINT perfiles_locales_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.perfiles_locales
    OWNER to postgres;
    
CREATE SEQUENCE public.perfiles_musicos_id_seq
    INCREMENT 1
    START 15
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.perfiles_musicos_id_seq
    OWNER TO postgres;
    
-- Table: public.perfiles_musicos

-- DROP TABLE public.perfiles_musicos;

CREATE TABLE public.perfiles_musicos
(
    id integer NOT NULL DEFAULT nextval('perfiles_musicos_id_seq'::regclass),
    nombre text COLLATE pg_catalog."default",
    provincia text COLLATE pg_catalog."default",
    localidad text COLLATE pg_catalog."default",
    telefono text COLLATE pg_catalog."default",
    mail_contacto text COLLATE pg_catalog."default",
    descripcion character varying(5000) COLLATE pg_catalog."default",
    genero1 text COLLATE pg_catalog."default",
    genero2 text COLLATE pg_catalog."default",
    foto1 text COLLATE pg_catalog."default",
    foto2 text COLLATE pg_catalog."default",
    foto3 text COLLATE pg_catalog."default",
    red_social1 text COLLATE pg_catalog."default",
    red_social2 text COLLATE pg_catalog."default",
    red_social3 text COLLATE pg_catalog."default",
    link_musica1 text COLLATE pg_catalog."default",
    link_musica2 text COLLATE pg_catalog."default",
    link_musica3 text COLLATE pg_catalog."default",
    id_usuario integer,
    CONSTRAINT perfiles_musicos_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.perfiles_musicos
    OWNER to postgres;
    

    
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebalocal@gmail.com','admin','local', 'pruebalocal');
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebamusico@gmail.com','admin','musico', 'pruebamusico');
INSERT INTO perfiles_locales (nombre, provincia, localidad, direccion, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebalocal','Buenos Aires', 'merlo', 'sdlfjsdlfsjdlf','123455','prueba@gmail.com','lasjdfsakfsdf', '1');
INSERT INTO perfiles_musicos (nombre, provincia, localidad, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebamusico', 'Buenos Aires', 'merlo', '123455','prueba@gmail.com','lasjdfsakfsdf', '1');

INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebalocal1@gmail.com','admin','local', 'pruebalocal1');
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebamusico1@gmail.com','admin','musico', 'pruebamusico1');
INSERT INTO perfiles_locales (nombre, provincia, localidad, direccion, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebalocal1','Buenos Aires', 'padua', 'sdlfjsdlfsjdlf','123455','prueba@gmail.com','lasjdfsakfsdf', '1');
INSERT INTO perfiles_musicos (nombre, provincia, localidad, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebamusico1', 'Buenos Aires', 'padua', '123455','prueba@gmail.com','lasjdfsakfsdf', '1');
    
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebalocal2@gmail.com','admin','local', 'pruebalocal2');
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebamusico2@gmail.com','admin','musico', 'pruebamusico2');
INSERT INTO perfiles_locales (nombre, provincia, localidad, direccion, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebalocal2','Buenos Aires', 'CABA', 'sdlfjsdlfsjdlf','123455','prueba@gmail.com','lasjdfsakfsdf', '1');
INSERT INTO perfiles_musicos (nombre, provincia, localidad, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebamusico2', 'Buenos Aires', 'CABA', '123455','prueba@gmail.com','lasjdfsakfsdf', '1');
    
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebalocal3@gmail.com','admin','local', 'pruebalocal3');
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebamusico3@gmail.com','admin','musico', 'pruebamusico3');
INSERT INTO perfiles_locales (nombre, provincia, localidad, direccion, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebalocal3','Buenos Aires', 'CABA', 'sdlfjsdlfsjdlf','123455','prueba@gmail.com','lasjdfsakfsdf', '1');
INSERT INTO perfiles_musicos (nombre, provincia, localidad, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebamusico3', 'Buenos Aires', 'CABA', '123455','prueba@gmail.com','lasjdfsakfsdf', '1');
    
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebalocal4@gmail.com','admin','local', 'pruebalocal4');
INSERT INTO usuarios (mail, contrasenia, tipo, nombre) VALUES ('pruebamusico4@gmail.com','admin','musico', 'pruebamusico4');
INSERT INTO perfiles_locales (nombre, provincia, localidad, direccion, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebalocal4','Córdoba', 'cordoba', 'sdlfjsdlfsjdlf','123455','prueba@gmail.com','lasjdfsakfsdf', '1');
INSERT INTO perfiles_musicos (nombre, provincia, localidad, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebamusico4', 'Santa Fe', 'Rosario', '123455','prueba@gmail.com','lasjdfsakfsdf', '1');
    
