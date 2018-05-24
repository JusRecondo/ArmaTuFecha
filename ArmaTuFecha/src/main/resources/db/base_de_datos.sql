CREATE SEQUENCE public.usuarios_id_seq
    INCREMENT 1
    START 9
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
    START 6
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
    direccion text COLLATE pg_catalog."default",
    telefono text COLLATE pg_catalog."default",
    mail_contacto text COLLATE pg_catalog."default",
    descripcion character varying(1400) COLLATE pg_catalog."default",
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
    START 3
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
    ubicacion text COLLATE pg_catalog."default",
    telefono text COLLATE pg_catalog."default",
    mail_contacto text COLLATE pg_catalog."default",
    descripcion character varying(1400) COLLATE pg_catalog."default",
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
    

    
    
    
    
INSERT INTO usuarios (mail, contrasenia, tipo) VALUES ('pruebalocal@gmail.com','admin','local');
INSERT INTO usuarios (mail, contrasenia, tipo) VALUES ('pruebamusico@gmail.com','admin','musico');
INSERT INTO perfiles_locales (nombre, direccion, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebalocal','aslkdj','123455','prueba@gmail.com','lasjdfsakfsdf', '1');
INSERT INTO perfiles_musicos (nombre, ubicacion, telefono,  mail_contacto, descripcion, id_usuario) VALUES ('pruebamusico','aslkdj','123455','prueba@gmail.com','lasjdfsakfsdf', '1');