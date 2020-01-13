CREATE DATABASE "VarusMessagingProxy"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Ukraine.1251'
    LC_CTYPE = 'Russian_Ukraine.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

CREATE SEQUENCE public.global_config_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.global_config_id_seq
    OWNER TO postgres;

CREATE SEQUENCE public.gsm_provider_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.gsm_provider_id_seq
    OWNER TO postgres;

CREATE SEQUENCE public.message_log_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.message_log_id_seq
    OWNER TO postgres;

CREATE TABLE public.global_config
(
    id integer NOT NULL DEFAULT nextval('global_config_id_seq'::regclass),
    number_of_attempts numeric,
    secondary_channel_timeslot numeric,
    CONSTRAINT global_config_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.global_config
    OWNER to postgres;

CREATE TABLE public.gsm_provider
(
    id integer NOT NULL DEFAULT nextval('gsm_provider_id_seq'::regclass),
    provider_name character varying(100) COLLATE pg_catalog."default",
    primary_url character varying(500) COLLATE pg_catalog."default",
    secondary_url character varying(500) COLLATE pg_catalog."default",
    username character varying(100) COLLATE pg_catalog."default",
    user_password character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT gsm_provider_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.gsm_provider
    OWNER to postgres;

CREATE TABLE public.message_log
(
    id integer NOT NULL DEFAULT nextval('message_log_id_seq'::regclass),
    recipient_list character varying(500) COLLATE pg_catalog."default",
    msg_text character varying(5000) COLLATE pg_catalog."default",
    date_stamp timestamp with time zone,
    status numeric,
    message_id character varying(200) COLLATE pg_catalog."default",
    CONSTRAINT message_log_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.message_log
    OWNER to postgres;