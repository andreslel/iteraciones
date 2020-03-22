--- tabla de cliente
CREATE TABLE CLIENTE(
    ID NUMBER,
    NOMBRE VARCHAR2(255) NOT NULL,
    EMAIL VARCHAR2(255) NOT NULL,
    NUMERO VARCHAR2(255) NOT NULL,
    DOCUMENTO VARCHAR2(255) NOT NULL,
    TIPO_CLIENTE VARCHAR(255) NOT NULL,
    PRIMARY KEY(ID),
    CHECK (TIPO_CLIENTE IN ('PROFESOR','EMPLEADO','EGRESADO',
        'ESTUDIANTE','PADRE','NO_RELACIONADO','VECINO')),
    CONSTRAINT DOCUMENTO_UN UNIQUE(DOCUMENTO)
);


--- crea la tabla de operador
CREATE TABLE OPERADOR(
    ID NUMBER,
    NOMBRE VARCHAR2(255) NOT NULL,
    EMAIL VARCHAR2(255) NOT NULL,
    NUMERO VARCHAR2(255) NOT NULL,
    TIPO_OPERADOR VARCHAR2(255) NOT NULL,
    PRIMARY KEY(ID),
    CHECK (TIPO_OPERADOR IN ('HOTELERIA','VIVIENDA_UNIVERSITARIA',
                        'PERSONA_NATURAL'))
);

--- crea la tabla de persona natural
CREATE TABLE PERSONA_NATURAL(
    ID NUMBER,
    DOCUMENTO VARCHAR2(255) NOT NULL,
    TIPO_PERSONA VARCHAR(255) NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY (ID) REFERENCES OPERADOR,
    CHECK (TIPO_PERSONA IN ('PROFESOR','EMPLEADO',
        'EGRESADO','ESTUDIANTE','PADRE','NO_RELACIONADO',
        'VECINO')),
    CONSTRAINT DOCUMENTO_UN1 UNIQUE(DOCUMENTO)
);

--- tabla de hoteles
CREATE TABLE HOTELERIA(
    ID NUMBER,
    TIPO_HOTELERIA VARCHAR2(255) NOT NULL,
    HORA_APERTURA TIMESTAMP,
    HORA_CIERRE TIMESTAMP,
    PRIMARY KEY(ID),
    FOREIGN KEY(ID) REFERENCES OPERADOR,
    CHECK (TIPO_HOTELERIA IN ('HOSTAL','HOTEL'))
);


--- tabla de ganancias
CREATE TABLE GANANCIAS(
    ID NUMBER,
    CANTIDAD NUMBER NOT NULL,
    MES NUMBER NOT NULL,
    AÑO NUMBER NOT NULL,
    OPERADOR NUMBER NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY(OPERADOR) REFERENCES OPERADOR(ID),
    CHECK (CANTIDAD > 0),
    CHECK (MES > 0 AND MES < 13)
);

--- crea la tabla de servicio
CREATE TABLE SERVICIO(
    ID NUMBER,
    NOMBRE VARCHAR2(255) NOT NULL,
    COSTO NUMBER NOT NULL,
    INCLUIDO NUMBER NOT NULL,
    PRIMARY KEY(ID),
    CHECK (COSTO > 0),
    CHECK (INCLUIDO = 0 OR INCLUIDO = 1)
);
--- crea una vivienda
CREATE TABLE VIVIENDA(
    ID NUMBER,
    DIRECCION VARCHAR2(255) NOT NULL,
    CUPOS NUMBER NOT NULL,
    OPERADOR NUMBER,
    PRIMARY KEY (ID),
    FOREIGN KEY (OPERADOR) REFERENCES OPERADOR(ID)
);

--- crea una oferta
CREATE TABLE OFERTA(
    ID NUMBER,
    PRECIO NUMBER NOT NULL,
    PERIODO VARCHAR2(255) NOT NULL,
    VIVIENDA NUMBER NOT NULL,
    FECHAINICIO TIMESTAMP NOT NULL,
    FECHAFIN TIMESTAMP NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (VIVIENDA) REFERENCES VIVIENDA(ID),
    CHECK (PRECIO > 0),
	CHECK (FECHAINICIO < FECHAFIN),
    CHECK (PERIODO IN ('DIAS','MESES','SEMESTRES'))
);


--- tabla de incluye
CREATE TABLE INCLUYE(
    OFERTA NUMBER,
    SERVICIO NUMBER,
    INCLUIDO NUMBER NOT NULL,
    PRIMARY KEY(OFERTA,SERVICIO),
    FOREIGN KEY (OFERTA) REFERENCES OFERTA(ID),
    FOREIGN KEY (SERVICIO) REFERENCES SERVICIO(ID),
    CHECK (INCLUIDO = 0 OR INCLUIDO = 1)
);

--- crea habitación
CREATE TABLE HABITACION(
    ID NUMBER,
    TIPO_HABITACION VARCHAR2(255) NOT NULL,
    CATEGORIA VARCHAR2(255) NOT NULL,
    NUMERO INTEGER NOT NULL,
    PRIMARY KEY(ID),
    CHECK ( TIPO_HABITACION IN ('ESTANDAR','SEMISUITE','SUITE')),
    CHECK (NUMERO > 0),
    FOREIGN KEY (ID) REFERENCES VIVIENDA
);

--- tabla de seguro
CREATE TABLE SEGURO(
    ID NUMBER,
    EMPRESA VARCHAR2(255) NOT NULL,
    MONTO NUMBER NOT NULL,
    INICIO_SEGURO TIMESTAMP NOT NULL,
    FIN_SEGURO TIMESTAMP NOT NULL,
    PRIMARY KEY(ID),
    CHECK (MONTO > 0)
);


---crea una vivienda esporadica
CREATE TABLE ESPORADICO(
    ID NUMBER,
    AREA NUMBER NOT NULL,
    AMOBLADO NUMBER NOT NULL,
    NUMERO_HABITACIONES NUMBER NOT NULL,
    NOCHES_AÑO NUMBER NOT NULL,
    SEGURO NUMBER NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY(ID) REFERENCES VIVIENDA,
	FOREIGN KEY(SEGURO) REFERENCES SEGURO(ID),
    CHECK ( AREA > 0),
    CHECK (AMOBLADO = 0 OR AMOBLADO = 1),
    CHECK (NUMERO_HABITACIONES > 0),
    CHECK (NOCHES_AÑO > 0 AND NOCHES_AÑO < 30)
);

-- tabla para la creación de un cuarto
CREATE TABLE CUARTO(
    ID NUMBER,
    BAÑO_PRIVADO NUMBER NOT NULL,
    CUARTO_PRIVADO NUMBER NOT NULL,
    ESQUEMA VARCHAR2(255) NOT NULL,
    MENAJE VARCHAR2(255) NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (ID) REFERENCES VIVIENDA,
    CHECK (BAÑO_PRIVADO = 0 OR BAÑO_PRIVADO = 1),
    CHECK (CUARTO_PRIVADO = 0 OR CUARTO_PRIVADO = 1)
);

--- tabla para el apartamento
CREATE TABLE APARTAMENTO(
    ID NUMBER,
    AREA NUMBER NOT NULL,
    AMOBLADO NUMBER NOT NULL,
    NUMERO_HABITACIONES NUMBER NOT NULL,
    PRIMARY KEY(ID),
    FOREIGN KEY(ID) REFERENCES VIVIENDA,
    CHECK ( AREA > 0),
    CHECK (AMOBLADO = 0 OR AMOBLADO = 1),
    CHECK (NUMERO_HABITACIONES > 0)
);

--- tabla de reservas
CREATE TABLE RESERVA(
    ID NUMBER,
    INICIO TIMESTAMP NOT NULL,
    FIN TIMESTAMP NOT NULL,
    PERIODO_ARRENDAMIENTO VARCHAR2(255) NOT NULL,
    OFERTA NUMBER,
    CLIENTE NUMBER,
	FOREIGN KEY (OFERTA) REFERENCES OFERTA(ID),
    FOREIGN KEY (CLIENTE) REFERENCES CLIENTE(ID),
    PRIMARY KEY(ID,OFERTA,CLIENTE),
    CHECK (FIN > INICIO),
    CHECK (PERIODO_ARRENDAMIENTO IN ('DIAS','MESES','SEMESTRES'))
);
