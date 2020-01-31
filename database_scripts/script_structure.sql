--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 12.1

-- Started on 2020-01-31 13:30:46

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "VarusMessagingProxy";
--
-- TOC entry 2861 (class 1262 OID 16393)
-- Name: VarusMessagingProxy; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "VarusMessagingProxy" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Russian_Russia.1251' LC_CTYPE = 'Russian_Russia.1251';


ALTER DATABASE "VarusMessagingProxy" OWNER TO postgres;

\connect "VarusMessagingProxy"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 208 (class 1259 OID 16445)
-- Name: client_config_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.client_config_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 16447)
-- Name: client_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client_config (
    id integer DEFAULT nextval('public.client_config_id_seq'::regclass) NOT NULL,
    client_name character varying(100),
    number_of_attempts numeric,
    priority numeric,
    failover_sms_allowed boolean,
    time_window_restricted boolean
);


ALTER TABLE public.client_config OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16397)
-- Name: global_config_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.global_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.global_config_id_seq OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 16401)
-- Name: global_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.global_config (
    id integer DEFAULT nextval('public.global_config_id_seq'::regclass) NOT NULL,
    number_of_attempts numeric,
    secondary_channel_timeslot numeric,
    default_provider_id integer,
    ad_time_window_start character varying,
    ad_time_window_end character varying,
    auth_user_name character varying(100),
    auth_password character varying(100)
);


ALTER TABLE public.global_config OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 16399)
-- Name: gsm_provider_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.gsm_provider_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.gsm_provider_id_seq OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16410)
-- Name: gsm_provider; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.gsm_provider (
    id integer DEFAULT nextval('public.gsm_provider_id_seq'::regclass) NOT NULL,
    provider_name character varying(100),
    primary_url character varying(500),
    secondary_url character varying(500),
    username character varying(100),
    user_password character varying(100),
    report_url character varying(500)
);


ALTER TABLE public.gsm_provider OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16419)
-- Name: message_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.message_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.message_log_id_seq OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 16421)
-- Name: message_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message_log (
    id integer DEFAULT nextval('public.message_log_id_seq'::regclass) NOT NULL,
    recipient_list character varying(500),
    msg_text character varying(5000),
    date_stamp timestamp with time zone,
    status numeric,
    message_id character varying(200),
    internal_message_id character varying(200)
);


ALTER TABLE public.message_log OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16462)
-- Name: message_report_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.message_report_id_seq
    START WITH 228
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE public.message_report_id_seq OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 16464)
-- Name: message_report; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message_report (
    id integer DEFAULT nextval('public.message_report_id_seq'::regclass) NOT NULL,
    message_id character varying(100),
    phone_number character varying(100),
    sent_at timestamp with time zone,
    channel character varying(100),
    status character varying(200),
    error_str character varying(200),
    sent_by_provider character varying(100),
    internal_message_id character varying(100),
    price character varying(50),
    msg_text character varying(500)
);


ALTER TABLE public.message_report OWNER TO postgres;

--
-- TOC entry 2728 (class 2606 OID 16455)
-- Name: client_config client_config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client_config
    ADD CONSTRAINT client_config_pkey PRIMARY KEY (id);


--
-- TOC entry 2722 (class 2606 OID 16409)
-- Name: global_config global_config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.global_config
    ADD CONSTRAINT global_config_pkey PRIMARY KEY (id);


--
-- TOC entry 2724 (class 2606 OID 16418)
-- Name: gsm_provider gsm_provider_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.gsm_provider
    ADD CONSTRAINT gsm_provider_pkey PRIMARY KEY (id);


--
-- TOC entry 2726 (class 2606 OID 16429)
-- Name: message_log message_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message_log
    ADD CONSTRAINT message_log_pkey PRIMARY KEY (id);


--
-- TOC entry 2729 (class 2606 OID 16440)
-- Name: global_config default_provider_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.global_config
    ADD CONSTRAINT default_provider_fkey FOREIGN KEY (default_provider_id) REFERENCES public.gsm_provider(id) NOT VALID;


-- Completed on 2020-01-31 13:30:47

--
-- PostgreSQL database dump complete
--

