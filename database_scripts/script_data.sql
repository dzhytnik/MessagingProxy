--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 12.1

-- Started on 2020-01-31 13:32:00

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
-- TOC entry 2863 (class 0 OID 16447)
-- Dependencies: 209
-- Data for Name: client_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.client_config VALUES (1, 'MobileAppAndroid', 10, 1, true, false);
INSERT INTO public.client_config VALUES (2, 'ExternalClient', 10, 1, true, true);


--
-- TOC entry 2858 (class 0 OID 16401)
-- Dependencies: 204
-- Data for Name: global_config; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.global_config VALUES (1, 5, 1, 1, '0 06 11 * * FRI', '0 07 11 * * FRI', 'admin', 'password');


--
-- TOC entry 2859 (class 0 OID 16410)
-- Dependencies: 205
-- Data for Name: gsm_provider; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.gsm_provider VALUES (2, 'gmsu', 'https://api-v2.hyber.im/2157', NULL, 'VARUS_T', 'r2a9Ro16', 'https://dr-v2.hyber.im/2157/api/dr/%s/simple');
INSERT INTO public.gsm_provider VALUES (1, 'Infobip', 'https://api.infobip.com/omni/1/advanced', NULL, 'c7bb509341ec624a0a41de7754356208-7d2e4651-6117-4a75-be96-2ceb5683fc72', '6CB8B6B51D49EA3049A0FA7BCA94AD51', 'https://api.infobip.com/omni/1/logs');


--
-- TOC entry 2861 (class 0 OID 16421)
-- Dependencies: 207
-- Data for Name: message_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

--
-- TOC entry 2865 (class 0 OID 16464)
-- Dependencies: 211
-- Data for Name: message_report; Type: TABLE DATA; Schema: public; Owner: postgres
--

--
-- TOC entry 2872 (class 0 OID 0)
-- Dependencies: 208
-- Name: client_config_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.client_config_id_seq', 1, false);


--
-- TOC entry 2873 (class 0 OID 0)
-- Dependencies: 202
-- Name: global_config_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.global_config_id_seq', 1, false);


--
-- TOC entry 2874 (class 0 OID 0)
-- Dependencies: 203
-- Name: gsm_provider_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.gsm_provider_id_seq', 1, false);


--
-- TOC entry 2875 (class 0 OID 0)
-- Dependencies: 206
-- Name: message_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.message_log_id_seq', 608, true);


--
-- TOC entry 2876 (class 0 OID 0)
-- Dependencies: 210
-- Name: message_report_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.message_report_id_seq', 299, true);


-- Completed on 2020-01-31 13:32:00

--
-- PostgreSQL database dump complete
--

