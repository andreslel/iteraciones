package uniandes.isis2304.alohandes.persistencia;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Hoteleria;
import uniandes.isis2304.alohandes.negocio.Oferta;
import uniandes.isis2304.alohandes.negocio.Vivienda;

public class REQC9 {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAlohandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	protected PersistenciaAlohandes pa;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/** 
	* Constructor
	* @param pa - El Manejador de persistencia de la aplicación
	*/
	public REQC9 (PersistenciaAlohandes pa)
	{
	   this.pa = pa;
	}

	public List<Oferta> ofertasConPocaDemanda(PersistenceManager pm){
<<<<<<< Updated upstream:Alohandes/src/main/java/uniandes/isis2304/alohandes/persistencia/REQC9.java
		Query q = pm.newQuery(SQL, "SELECT *\r\n" + 
				"FROM oferta o\r\n" + 
				"WHERE o.id NOT IN\r\n" + 
				"(\r\n" + 
				"	SELECT re1.oferta\r\n" + 
				"	FROM reserva re1\r\n" + 
				"	WHERE (\r\n" + 
				"			SELECT re2.id\r\n" + 
				"			FROM reserva re2\r\n" + 
				"			WHERE to_number(to_char(re2.inicio,'DDD')) > to_number(to_char(re1.fin,'DDD'))\r\n" + 
				"			AND to_number(to_char(re1.fin,'DDD')) + 30 >  to_number(to_char(re2.inicio,'DDD')) \r\n" + 
				"			AND to_number(to_char(re1.fin,'YYYY')) = to_number(to_char(re2.inicio,'YYYY'))\r\n" + 
				"		) IS NOT NULL\r\n" + 
				"		OR (\r\n" + 
				"			SELECT re2.id\r\n" + 
				"			FROM reserva re2\r\n" + 
				"			WHERE to_number(to_char(re2.inicio,'DDD')) < to_number(to_char(re1.fin,'DDD'))\r\n" + 
				"			AND to_number(to_char(re1.fin,'DDD')) + 30 - 365 >  to_number(to_char(re2.inicio,'DDD'))\r\n" + 
				"			AND to_number(to_char(re1.fin,'YYYY'))+1 = to_number(to_char(re2.inicio,'YYYY'))\r\n" + 
				"		) IS NOT NULL\r\n" + 
				"\r\n" + 
				"	GROUP BY re1.oferta\r\n" + 
				");");
		q.setResultClass(Oferta.class);
		return (List<Oferta>) q.executeList();
=======
//		Timestamp hoy = new Timestamp(System.currentTimeMillis());
//		int a�o = hoy.getYear();
//		int mes = hoy.getMonth();
//		Query q = pm.newQuery(SQL, "SELECT FROM " + pa.darTablaOferta() + "WHERE id NOT IN " +"("
//				+ "SELECT o.id,o.precio, o.periodo,o.vivienda,o.fechaInicio,o.fechaFin FROM "
//				+ pa.darTablaOferta() + " o, " + pa.darTablaReserva()+ " r"
//				+" WHERE "
//				+ ")");
//		q.setParameters(idOperador,year);
//		q.setResultClass(Oferta.class);
//		return (List<Oferta>) q.executeList();
		return null;
>>>>>>> Stashed changes:Alohandes/src/main/java/uniandes/isis2304/alohandes/persistencia/REQC5.java
	}
}
