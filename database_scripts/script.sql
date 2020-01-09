CREATE DATABASE "VarusMessagingProxy"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Ukraine.1251'
    LC_CTYPE = 'Russian_Ukraine.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

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