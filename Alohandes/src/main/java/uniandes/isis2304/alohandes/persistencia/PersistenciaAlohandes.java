/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Alohandes Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jim�nez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.alohandes.persistencia;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import oracle.sql.DATE;
import oracle.sql.DATE;
import uniandes.isis2304.alohandes.negocio.Apartamento;
import uniandes.isis2304.alohandes.negocio.Cliente;
import uniandes.isis2304.alohandes.negocio.Cuarto;
import uniandes.isis2304.alohandes.negocio.Esporadico;
import uniandes.isis2304.alohandes.negocio.Ganancias;
import uniandes.isis2304.alohandes.negocio.Habitacion;
import uniandes.isis2304.alohandes.negocio.Hoteleria;
import uniandes.isis2304.alohandes.negocio.Incluye;
import uniandes.isis2304.alohandes.negocio.Oferta;
import uniandes.isis2304.alohandes.negocio.Operador;
import uniandes.isis2304.alohandes.negocio.Persona_Natural;
import uniandes.isis2304.alohandes.negocio.Reserva;
import uniandes.isis2304.alohandes.negocio.ReservaColectiva;
import uniandes.isis2304.alohandes.negocio.Seguro;
import uniandes.isis2304.alohandes.negocio.Servicio;
import uniandes.isis2304.alohandes.negocio.Usuario;
import uniandes.isis2304.alohandes.negocio.Vivienda;
import uniandes.isis2304.alohandes.negocio.ViviendaUniversitaria;

/**
 * Clase para el manejador de persistencia del proyecto Alohandes
 * Traduce la información entre objetos Java y tuplas de la base de datos, en ambos sentidos
 * Sigue un patrón SINGLETON (Sólo puede haber UN objeto de esta clase) para comunicarse de manera correcta
 * con la base de datos
 * Se apoya en las clases SQLBar, SQLBebedor, SQLBebida, SQLGustan, SQLSirven, SQLTipoBebida y SQLVisitan, que son 
 * las que realizan el acceso a la base de datos
 * 
 * @author Germán Bravo
 */
public class PersistenciaAlohandes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaAlohandes.class.getName());

	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaAlohandes instance;

	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;

	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan
	 */
	private List <String> tablas;

	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaAlohandes
	 */
	private SQLUtil sqlUtil;

	/**
	 * Atributo para el acceso a la tabla VIVIENDA de la base de datos
	 */
	private SQLVivienda sqlVivienda;

	/**
	 * Atributo para el acceso a la tabla HABITACION de la base de datos
	 */
	private SQLHabitacion sqlHabitacion;

	/**
	 * Atributo para el acceso a la tabla APARTAMENTO de la base de datos
	 */
	private SQLApartamento sqlApartamento;

	/**
	 * Atributo para el acceso a la tabla ESPORADICO de la base de datos
	 */
	private SQLEsporadico sqlEsporadico;

	/**
	 * Atributo para el acceso a la tabla SEGURO de la base de datos
	 */
	private SQLSeguro sqlSeguro;

	/**
	 * Atributo para el acceso a la tabla CUARTO de la base de datos
	 */
	private SQLCuarto sqlCuarto;

	/**
	 * Atributo para el acceso a la tabla OPERADOR de la base de datos
	 */
	private SQLOperador sqlOperador;

	/**
	 * Atributo para el acceso a la tabla HOTELERIA de la base de datos
	 */
	private SQLHoteleria sqlHoteleria;

	/**
	 * Atributo para el acceso a la tabla PERSONA_NATURAL de la base de datos
	 */
	private SQLPersona_Natural sqlPersona_Natural;

	/**
	 * Atributo para el acceso a la tabla GANANCIAS de la base de datos
	 */
	private SQLGanancias sqlGanancias;

	/**
	 * Atributo para el acceso a la tabla CLIENTE de la base de datos
	 */
	private SQLCliente sqlCliente;

	/**
	 * Atributo para el acceso a la tabla RESERVA de la base de datos
	 */
	private SQLReserva sqlReserva;

	/**
	 * Atributo para el acceso a la tabla OFERTA de la base de datos
	 */
	private SQLOferta sqlOferta;

	/**
	 * Atributo para el acceso a la tabla SERVICIO de la base de datos
	 */
	private SQLServicio sqlServicio;

	/**
	 * Atributo para el acceso a la tabla INCLUYE de la base de datos
	 */
	private SQLIncluye sqlIncluye;

	/**
	 * Atributo para el acceso a la tabla RESERVA_COLECTIVA de la base de daatos
	 */
	private SQLReservaColectiva sqlReservaColectiva;

	/**
	 * Atributo para la clase que maneja la consulta 1
	 */
	private REQC1 reqc1;

	/**
	 * Atributo para la clase que maneja la consutla 2
	 */
	private REQC2 reqc2;

	/**
	 * Atributo para la clase que maneja la consutla 7
	 */
	private REQC7 reqc7;

	/**
	 * Atributo para la clase que maneja la consutla 7
	 */
	private REQC8 reqc8;

	/**
	 * Atributo para la clase que maneja la consutla 9
	 */
	private REQC9 reqc9;

	/**
	 * Atributo para la clase que maneja la consutla 10
	 */
	private REQC10 reqc10;
	
	/**
	 * Atributo para la clase que maneja la consutla 11
	 */
	private REQC11 reqc11;
	
	private boolean modoPerron = false;

	private long idPerron;


	/* ****************************************************************
	 * 			M�todos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaAlohandes ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");		
		crearClasesSQL ();

		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("Parranderos_sequence");
		tablas.add ("VIVIENDA");
		tablas.add ("HABITACION");
		tablas.add ("APARTAMENTO");
		tablas.add ("ESPORADICO");
		tablas.add ("SEGURO");
		tablas.add ("CUARTO");
		tablas.add ("OPERADOR");
		tablas.add ("HOTELERIA");
		tablas.add ("PERSONA_NATURAL");
		tablas.add ("GANANCIAS");
		tablas.add ("CLIENTE");
		tablas.add ("RESERVA");
		tablas.add ("OFERTA");
		tablas.add ("SERVICIO");
		tablas.add ("INCLUYE");
		tablas.add("RESERVA_COLECTIVA");
	}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrón SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaAlohandes (JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas (tableConfig);

		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaAlohandes existente - Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaAlohandes ();
		}
		return instance;
	}

	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaAlohandes existente - Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaAlohandes (tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}

	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}

		return resp;
	}

	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{
		sqlVivienda = new SQLVivienda(this);
		sqlHabitacion = new SQLHabitacion(this);
		sqlApartamento = new SQLApartamento(this);
		sqlEsporadico = new SQLEsporadico(this);
		sqlSeguro = new SQLSeguro(this);
		sqlCuarto = new SQLCuarto(this);
		sqlOperador = new SQLOperador(this);
		sqlHoteleria = new SQLHoteleria(this);
		sqlPersona_Natural = new SQLPersona_Natural(this);
		sqlGanancias = new SQLGanancias(this);
		sqlCliente = new SQLCliente(this);
		sqlReserva = new SQLReserva(this);
		sqlOferta = new SQLOferta(this);
		sqlServicio = new SQLServicio(this);
		sqlIncluye = new SQLIncluye(this);	
		sqlReservaColectiva = new SQLReservaColectiva(this);
		sqlUtil = new SQLUtil(this);
		reqc1 = new REQC1(this);
		reqc2 = new REQC2(this);
		reqc7 = new REQC7(this);
		reqc8 = new REQC8(this);
		reqc9 = new REQC9(this);
		reqc10 = new REQC10(this);
		reqc11 = new REQC11(this);
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de parranderos
	 */
	public String darSeqAlohandes ()
	{
		return tablas.get (0);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Vivienda de alohandes
	 */
	public String darTablaVivienda ()
	{
		return tablas.get (1);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Habitacion de alohandes
	 */
	public String darTablaHabitacion ()
	{
		return tablas.get (2);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Apartamento de alohandes
	 */
	public String darTablaApartamento ()
	{
		return tablas.get (3);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Esporadico de alohandes
	 */
	public String darTablaEsporadico ()
	{
		return tablas.get (4);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Seguro de alohandes
	 */
	public String darTablaSeguro ()
	{
		return tablas.get (5);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cuarto de alohandes
	 */
	public String darTablaCuarto ()
	{
		return tablas.get (6);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Operador de alohandes
	 */
	public String darTablaOperador ()
	{
		return tablas.get (7);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Hoteleria de alohandes
	 */
	public String darTablaHoteleria ()
	{
		return tablas.get (8);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Persona_Natural de alohandes
	 */
	public String darTablaPersona_Natural ()
	{
		return tablas.get (9);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Ganancias de alohandes
	 */
	public String darTablaGanancias ()
	{
		return tablas.get (10);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cliente de alohandes
	 */
	public String darTablaCliente ()
	{
		return tablas.get (11);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Reserva de alohandes
	 */
	public String darTablaReserva ()
	{
		return tablas.get (12);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Oferta de alohandes
	 */
	public String darTablaOferta ()
	{
		return tablas.get (13);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Servicio de alohandes
	 */
	public String darTablaServicio ()
	{
		return tablas.get (14);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Incluye de alohandes
	 */
	public String darTablaIncluye ()
	{
		return tablas.get (15);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Reserva Colectiva de alohandes
	 */
	public String darTablaReservaColectiva ()
	{
		return tablas.get (16);
	}

	/**
	 * Transacción para el generador de secuencia de Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return El siguiente número del secuenciador de Alohandes
	 */
	private long nextval ()
	{
		long resp = sqlUtil.nextval (pmf.getPersistenceManager());
		log.trace ("Generando secuencia: " + resp);
		return resp;
	}

	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle específico del problema encontrado
	 * @param e - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/* ****************************************************************
	 * 			M�todos para manejar las VIVIENDAS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Vivienda
	 * Adiciona entradas al log de la aplicación
	 * @param direccion - direccion de la vivienda
	 * @param cupos - cupos de la vivienda
	 * @param idOperador - idOperador de la vivienda
	 * @return El objeto Vivienda adicionado. null si ocurre alguna Excepción
	 */
	public Vivienda adicionarVivienda(String direccion, int cupos, long idOperador,String tipo)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idVivienda = nextval ();
			if(modoPerron)
				idVivienda = idPerron;
			long tuplasInsertadas = sqlVivienda.adicionarVivienda(pm, idVivienda, direccion, cupos, idOperador,tipo);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idVivienda + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Vivienda(idVivienda, direccion, cupos, idOperador,tipo);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}


	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Vivienda, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idVivienda - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarViviendaPorId (long idVivienda) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlVivienda.eliminarViviendaPorId(pm, idVivienda);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Vivienda
	 * @return La lista de objetos Vivienda, construidos con base en las tuplas de la tabla VIVIENDA
	 */
	public List<Vivienda> darViviendas ()
	{
		return sqlVivienda.darViviendas(pmf.getPersistenceManager());
	}

	/**
	 * M�todo que retorna la Vivienda con el id buscado
	 * @return La Vivienda con el id buscado
	 */
	public Vivienda darViviendaPorId(long idVivienda) {
		return sqlVivienda.darViviendaPorId(pmf.getPersistenceManager(), idVivienda);
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) HABITACIONS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Habitacion
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Habitacion
	 * @return El objeto Habitacion adicionado. null si ocurre alguna Excepción
	 */
	public Habitacion adicionarHabitacion(String direccion, int cupos, long idOperador,	String tipoHabitacion, String categoria, int capacidad, int numero)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idHabitacion = nextval ();
			if(modoPerron)
				idHabitacion = idPerron;
			long tuplasInsertadas = sqlHabitacion.adicionarHabitacion(pm, idHabitacion, direccion, cupos, idOperador, tipoHabitacion, categoria, capacidad, numero);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idHabitacion + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Habitacion(idHabitacion, direccion, cupos, idOperador, tipoHabitacion, categoria, capacidad, numero);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Habitacion, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idHabitacion - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarHabitacionPorId (long idHabitacion)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlHabitacion.eliminarHabitacionPorId(pm, idHabitacion);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Habitacion
	 * @return La lista de objetos Habitacion, construidos con base en las tuplas de la tabla HABITACION
	 */
	public List<Habitacion> darHabitaciones ()
	{
		return sqlHabitacion.darHabitaciones(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) APARTAMENTOS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Apartamento
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Apartamento
	 * @return El objeto Apartamento adicionado. null si ocurre alguna Excepción
	 */
	public Apartamento adicionarApartamento( String direccion, int cupos, long idOperador, double area, int amoblado, int numeroHabitaciones)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idApartamento = nextval ();
			if(modoPerron) 
				idApartamento = idPerron;
			//System.out.println("el modo esta activado? " + modoPerron);
			long tuplasInsertadas = sqlApartamento.adicionarApartamento(pm, idApartamento, direccion, cupos, idOperador, area, amoblado, numeroHabitaciones);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idApartamento + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Apartamento(idApartamento, direccion, cupos, idOperador, area, amoblado, numeroHabitaciones);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Apartamento, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idApartamento - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarApartamentoPorId (long idApartamento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlApartamento.eliminarApartamentoPorId(pm, idApartamento);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Apartamento
	 * @return La lista de objetos Apartamento, construidos con base en las tuplas de la tabla APARTAMENTO
	 */
	public List<Apartamento> darApartamentos ()
	{
		return sqlApartamento.darApartamentos(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) ESPORADICOS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Esporadico
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Esporadico
	 * @return El objeto Esporadico adicionado. null si ocurre alguna Excepción
	 */
	public Esporadico adicionarEsporadico(String direccion, int cupos, long idOperador,  double area, int amoblado, int numeroHabitaciones, int nochesAnio, long idSeguro)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idEsporadico = nextval ();
			if(modoPerron)
				idEsporadico = idPerron;
			long tuplasInsertadas = sqlEsporadico.adicionarEsporadico(pm, idEsporadico, direccion, cupos, idOperador, area, amoblado, numeroHabitaciones, nochesAnio, idSeguro);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idEsporadico + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Esporadico(idEsporadico, direccion, cupos, idOperador, area, amoblado, numeroHabitaciones, nochesAnio, idSeguro);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Esporadico, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idEsporadico - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarEsporadicoPorId (long idEsporadico)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlEsporadico.eliminarEsporadicoPorId(pm, idEsporadico);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Esporadico
	 * @return La lista de objetos Esporadico, construidos con base en las tuplas de la tabla ESPORADICO
	 */
	public List<Esporadico> darEsporadicos ()
	{
		return sqlEsporadico.darEsporadicos(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) SEGUROS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Seguro
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Seguro
	 * @return El objeto Seguro adicionado. null si ocurre alguna Excepción
	 */
	public Seguro adicionarSeguro(String empresa, int monto, DATE inicioSeguro, DATE finSeguro)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idSeguro = nextval ();
			if(modoPerron)
				idSeguro = idPerron;
			long tuplasInsertadas = sqlSeguro.adicionarSeguro(pm, idSeguro, empresa, monto, inicioSeguro, finSeguro);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idSeguro + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Seguro(idSeguro, empresa, monto, inicioSeguro, finSeguro);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Seguro, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idSeguro - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarSeguroPorId (long idSeguro)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			//System.out.println("intenta borrar el seguro?");
			long resp = sqlSeguro.eliminarSeguroPorId(pm, idSeguro);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Seguro
	 * @return La lista de objetos Seguro, construidos con base en las tuplas de la tabla SEGURO
	 */
	public List<Seguro> darSeguros ()
	{
		return sqlSeguro.darSeguros(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) CUARTOS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Cuarto
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Cuarto
	 * @return El objeto Cuarto adicionado. null si ocurre alguna Excepción
	 */
	public Cuarto adicionarCuarto(String direccion, int cupos, long idOperador, int banio_privado, int cuarto_privado, String esquema, String menaje)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idCuarto = nextval ();
			if(modoPerron)
				idCuarto = idPerron;
			long tuplasInsertadas = sqlCuarto.adicionarCuarto(pm, idCuarto, direccion, cupos, idOperador, banio_privado, cuarto_privado, esquema, menaje);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idCuarto + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Cuarto(idCuarto, direccion, cupos, idOperador, banio_privado, cuarto_privado, esquema, menaje);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Cuarto, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idCuarto - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarCuartoPorId (long idCuarto)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlCuarto.eliminarCuartoPorId(pm, idCuarto);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Cuarto
	 * @return La lista de objetos Cuarto, construidos con base en las tuplas de la tabla CUARTO
	 */
	public List<Cuarto> darCuartos ()
	{
		return sqlCuarto.darCuartos(pmf.getPersistenceManager());
	}


	/* ****************************************************************
	 * 			M�todos para manejar los(as) OPERADORES
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Operador
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Operador
	 * @return El objeto Operador adicionado. null si ocurre alguna Excepción
	 */
	public Operador adicionarOperador(String nombre, String email, String numero, String tipoOperador)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idOperador = nextval ();
			if(modoPerron)
				idOperador = idPerron;
			long tuplasInsertadas = sqlOperador.adicionarOperador(pm, idOperador, nombre, email, numero, tipoOperador);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idOperador + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Operador(idOperador, nombre, email, numero, tipoOperador);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Operador, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idOperador - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarOperadorPorId (long idOperador)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlOperador.eliminarOperadorPorId(pm, idOperador);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Operador
	 * @return La lista de objetos Operador, construidos con base en las tuplas de la tabla OPERADOR
	 */
	public List<Operador> darOperadores ()
	{
		return sqlOperador.darOperadores(pmf.getPersistenceManager());
	}

	/**
	 * M�todo que retorna el operador con el nombre y tipo buscados
	 * @return Objeto Operador con el nombre y tipo buscados
	 */
	public Operador darOperadorPorNombre(String nombre, String tipoOperador) {
		return sqlOperador.darOperadorPorNombre(pmf.getPersistenceManager(), nombre, tipoOperador);
	}
	
	/**
	 * M�todo que retorna el operador con el id y tipo buscados
	 * @return Objeto Operador con el id y tipo buscados
	 */
	public Operador darOperadorPorId(long id) {
		return sqlOperador.darOperadorPorId(pmf.getPersistenceManager(), id);
	}


	/* ****************************************************************
	 * 			M�todos para manejar los(as) VIVIENDAS_UNIVERSITARIAS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Vivienda_Universitaria
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Operador
	 * @return El objeto Operador adicionado. null si ocurre alguna Excepción
	 */
	public ViviendaUniversitaria adicionarViviendaUniversitaria(String nombre, String email, String numero)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idOperador = nextval ();
			if(modoPerron)
				idOperador = idPerron;
			long tuplasInsertadas = sqlOperador.adicionarOperador(pm, idOperador, nombre, email, numero, "VIVIENDA_UNIVERSITARIA");
			tx.commit();

			log.trace ("Inserción de vivienda: " + idOperador + ": " + tuplasInsertadas + " tuplas insertadas");

			return new ViviendaUniversitaria(idOperador, nombre, email, numero);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Operador, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idViviendaUniversitaria - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarViviendaUniversitariaPorId (long idViviendaUniversitaria)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlOperador.eliminarOperadorPorId(pm, idViviendaUniversitaria);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla ViviendaUniversitaria
	 * @return La lista de objetos Operador, construidos con base en las tuplas de la tabla OPERADOR
	 */
	public List<ViviendaUniversitaria> darViviendasUniversitarias ()
	{
		return sqlOperador.darViviendasUniversitarias(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) HOTELERIAS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Hoteleria
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Hoteleria
	 * @return El objeto Hoteleria adicionado. null si ocurre alguna Excepción
	 */
	public Hoteleria adicionarHoteleria(String nombre, String email, String numero, String tipoHoteleria, String horaApertura, String horaCierre)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idHoteleria = nextval ();
			if(modoPerron)
				idHoteleria = idPerron;
			long tuplasInsertadas = sqlHoteleria.adicionarHoteleria(pm, idHoteleria, nombre, email, numero, tipoHoteleria, horaApertura, horaCierre);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idHoteleria + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Hoteleria(idHoteleria, nombre, email, numero, tipoHoteleria, horaApertura, horaCierre);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Hoteleria, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idHoteleria - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarHoteleriaPorId (long idHoteleria)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlHoteleria.eliminarHoteleriaPorId(pm, idHoteleria);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Hoteleria
	 * @return La lista de objetos Hoteleria, construidos con base en las tuplas de la tabla HOTELERIA
	 */
	public List<Hoteleria> darHotelerias ()
	{
		return sqlHoteleria.darHotelerias(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) PERSONA_NATURALES
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Persona_Natural
	 * Adiciona entradas al log de la aplicación
	 * @param nombre 
	 * @param email 
	 * @param documento 
	 * @param numero 
	 * @param tipoPersona 
	 * @param x - x de Persona_Natural
	 * @return El objeto Persona_Natural adicionado. null si ocurre alguna Excepción
	 */
	public Persona_Natural adicionarPersona_Natural(String nombre, String email, String documento, String numero, String tipoPersona)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idPersona_Natural = nextval ();
			if(modoPerron)
				idPersona_Natural = idPerron;
			long tuplasInsertadas = sqlPersona_Natural.adicionarPersona_Natural(pm, idPersona_Natural, nombre, email, numero, documento, tipoPersona);
			tx.commit();

			log.trace ("Inserción de persona: " + idPersona_Natural + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Persona_Natural(idPersona_Natural, nombre, email, numero, tipoPersona, documento);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Persona_Natural, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idPersona_Natural - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarPersona_NaturalPorId (long idPersona_Natural)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlPersona_Natural.eliminarPersona_NaturalPorId(pm, idPersona_Natural);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Persona_Natural
	 * @return La lista de objetos Persona_Natural, construidos con base en las tuplas de la tabla PERSONA_NATURAL
	 */
	public List<Persona_Natural> darPersona_Naturales ()
	{
		return sqlPersona_Natural.darPersona_Naturales(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) GANANCIAS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Ganancias
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Ganancias
	 * @return El objeto Ganancias adicionado. null si ocurre alguna Excepción
	 */
	public Ganancias adicionarGanancias(long cantidad, DATE fecha ,long idOperador)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idGanancias = nextval ();
			if(modoPerron)
				idGanancias = idPerron;
			long tuplasInsertadas = sqlGanancias.adicionarGanancias(pm, idGanancias, cantidad, fecha, idOperador);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idGanancias + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Ganancias(idGanancias, cantidad, fecha, idOperador);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Ganancias, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idGanancias - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarGananciasPorId (long idGanancias)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlGanancias.eliminarGananciasPorId(pm, idGanancias);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Ganancias
	 * @return La lista de objetos Ganancias, construidos con base en las tuplas de la tabla GANANCIAS
	 */
	public List<Ganancias> darGanancias ()
	{
		return sqlGanancias.darGanancias(pmf.getPersistenceManager());
	}

	/**
	 * M�todo que aumetna las gananacias de cierto operador para cierta fecha
	 * @param aumento
	 * @param idOperador
	 * @param mes
	 * @param anio
	 */
	public void aumentarGanancias(Long aumento, long idOperador, int mes, int anio) {
		if(sqlGanancias.darGananciasPorFechaOperador(pmf.getPersistenceManager(), idOperador, mes, anio) == null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(anio, mes, 0, 0, 0, 0 );

			adicionarGanancias(aumento, new DATE(new Timestamp(cal.getTime().getTime())), idOperador);
		}
		else
			sqlGanancias.aumentarGanancias(pmf.getPersistenceManager(), aumento, idOperador, mes, anio);
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) CLIENTES
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Cliente
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Cliente
	 * @return El objeto Cliente adicionado. null si ocurre alguna Excepción
	 */
	public Cliente adicionarCliente(String nombre, String email, String numero, String tipoCliente, String documento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idCliente = nextval ();
			if(modoPerron)
				idCliente = idPerron;
			long tuplasInsertadas = sqlCliente.adicionarCliente(pm, idCliente, nombre, email, numero, tipoCliente, documento);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idCliente + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Cliente(idCliente, nombre, email, numero, tipoCliente, documento);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Cliente, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idCliente - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarClientePorId (long idCliente)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlCliente.eliminarClientePorId(pm, idCliente);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Cliente
	 * @return La lista de objetos Cliente, construidos con base en las tuplas de la tabla USUARIO
	 */
	public List<Cliente> darClientes ()
	{
		return sqlCliente.darClientes(pmf.getPersistenceManager());
	}

	/**
	 * M�todo que busca una tupla en Cliente según su nombre
	 * @return El objeto Cliente con el nombre buscado
	 */
	public Cliente buscarClientePorNombre(String nombreCliente) {
		return sqlCliente.darClientePorNombre(pmf.getPersistenceManager(), nombreCliente);
	}

	/**
	 * M�todo que busca una tupla en Cliente según su nombre
	 * @return El objeto Cliente con el nombre buscado
	 */
	public Cliente buscarClientePorId(long idCliente) {
		return sqlCliente.darClientePorId(pmf.getPersistenceManager(), idCliente);
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) RESERVAS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Reserva
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Reserva
	 * @return El objeto Reserva adicionado. null si ocurre alguna Excepción
	 */
	public Reserva adicionarReserva(DATE inicio, DATE fin, String periodoArrendamiento, long idUsuario, long idOferta, long idColectiva)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idReserva = nextval ();
			if(modoPerron)
				idReserva = idPerron;
			long tuplasInsertadas = sqlReserva.adicionarReserva(pm, idReserva, inicio, fin,periodoArrendamiento, idUsuario, idOferta, idColectiva);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idReserva + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Reserva(idReserva, inicio, fin, periodoArrendamiento, idUsuario, idOferta, idColectiva);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}
	/**
	 * Encuentra todas la reserva de determinado ID
	 * @param idReserva Identificador de la reserva
	 * @return Todas las reservas del cliente buscado
	 */
	public Reserva darReservaPorId(long idReserva) {
		return sqlReserva.darReservaPorId(pmf.getPersistenceManager(), idReserva);
	}


	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Reserva, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idReserva - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarReservaPorId (long idReserva)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlReserva.eliminarReservaPorId(pm, idReserva);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Reserva
	 * @return La lista de objetos Reserva, construidos con base en las tuplas de la tabla RESERVA
	 */
	public List<Reserva> darReservas ()
	{
		return sqlReserva.darReservas(pmf.getPersistenceManager());
	}

	/**
	 * Encuentra todas las reservas de un cliente
	 * @param idCliente Identificador del cliente
	 * @return Todas las reservas del cliente buscado
	 */
	public List<Reserva> darReservasPorCliente(long idCliente) {
		return sqlReserva.darReservasPorCliente(pmf.getPersistenceManager(), idCliente);
	}

	public List<Reserva> darReservasColectiva(long idReserva){
		return sqlReserva.darReservasColectiva(pmf.getPersistenceManager(), idReserva);
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) RESERVAS_COLECTIVAS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Reserva
	 * Adiciona entradas al log de la aplicación
	 * @param fechaRealizacion 
	 * @param cantidad 
	 * @param idCliente 
	 * @param x - x de Reserva
	 * @return El objeto Reserva adicionado. null si ocurre alguna Excepción
	 */
	public ReservaColectiva adicionarReservaColectiva(DATE fechaRealizacion, int cantidad, long idCliente, ArrayList<String> lista, String tipo, 
			String periodo, DATE inicio, DATE fin)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idReserva = nextval ();

			List<Oferta> ofertas = darOfertasConServiciosYTipo(lista, tipo, periodo, inicio, fin);

			System.out.println("Hay " + ofertas.size() + " ofertas disponibles");
			long tuplasInsertadas; ReservaColectiva colectiva;

			if(cantidad <= ofertas.size()) {
				System.out.println("Es posible hacer las reservas");
				System.out.println("Reservando ofertas");

				tuplasInsertadas = sqlReservaColectiva.adicionarReservaColectiva(pm, idReserva, fechaRealizacion, cantidad, idCliente);
				colectiva = new ReservaColectiva(idReserva, cantidad, fechaRealizacion, idCliente);

				for (Oferta oferta : ofertas) {
					adicionarReserva(inicio, fin, periodo, oferta.getId(), idCliente, colectiva.getId());	
				}
			}
			else
			{
				System.out.println("Lo sentimos, no hay suficientes reservas");
				tuplasInsertadas = 0;
				colectiva = null;
			}

			tx.commit();

			log.trace ("Inserción de vivienda: " + idReserva + ": " + tuplasInsertadas + " tuplas insertadas");

			return colectiva;
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Reserva, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idReserva - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarReservaColectivaPorId (long idReserva)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			List<Reserva> reservas = darReservasColectiva(idReserva);
			for (Reserva reserva : reservas) {
				eliminarReservaPorId(reserva.getId());
			}
			long resp = sqlReservaColectiva.eliminarReservaColectivaPorId(pm, idReserva);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/* ****************************************************************
	 * 			M�todos para manejar los(as) OFERTAS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Oferta
	 * Adiciona entradas al log de la aplicación
	 * @param x - x de Oferta
	 * @return El objeto Oferta adicionado. null si ocurre alguna Excepción
	 */
	public Oferta adicionarOferta(long precio, String periodo, long vivienda, DATE fechaInicio, DATE fechaFin)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idOferta = nextval ();
			if(modoPerron)
				idOferta = idPerron;
			long tuplasInsertadas = sqlOferta.adicionarOferta(pm, idOferta, precio, periodo, vivienda, fechaInicio, fechaFin);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idOferta + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Oferta(idOferta, precio, periodo, fechaInicio, fechaFin, vivienda);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que retorna la oferta con el id buscado
	 * @param idOferta Identificador de la oferta buscada
	 * @return Oferta con el id buscado
	 */
	public Oferta darOfertaPorId ( long idOferta) {
		return sqlOferta.darOfertaPorId(pmf.getPersistenceManager(),idOferta);
	}


	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Oferta, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idOferta - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarOfertaPorId (long idOferta)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlOferta.eliminarOfertaPorId(pm, idOferta);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Oferta
	 * @return La lista de objetos Oferta, construidos con base en las tuplas de la tabla OFERTA
	 */
	public List<Oferta> darOfertas ()
	{
		return sqlOferta.darOfertas(pmf.getPersistenceManager());
	}

	/**
	 * Retorna un grupo de ofertas que cumplan con los servicios pedidos
	 * @param lista Servicios requeridos
	 * @return lista de Ofertas con los servicios requeridos
	 */
	public List<Oferta> darOfertasConServicios(ArrayList<String> lista, DATE inicio, DATE fin) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			List<Long> ids = sqlIncluye.darOfertasConServicios(pmf.getPersistenceManager(), lista, inicio, fin);
			List<Oferta> ofertas = new LinkedList<Oferta>();

			for (Long id : ids) {
				ofertas.add(darOfertaPorId(id));
			}

			tx.commit();
			return ofertas;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Retorna un grupo de ofertas que cumplan con los servicios pedidos
	 * @param lista Servicios requeridos
	 * @param tipo Tipo de operador
	 * @return lista de Ofertas con los servicios requeridos
	 */
	public List<Oferta> darOfertasConServiciosYTipo(ArrayList<String> lista, String tipo, String periodo, DATE inicio, DATE fin) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			List<Oferta> ofertas;
			if(lista == null || lista.size() == 0) {
				ofertas = sqlIncluye.darOfertasConServiciosYTipo(pm, tipo, periodo, inicio, fin);
			}
			else {
				List<Long> ids = sqlIncluye.darOfertasConServiciosYTipo(pm, lista, tipo, periodo, inicio, fin);			

				ofertas = new LinkedList<Oferta>();
				if(ids == null)
					return ofertas;
				for (Long id : ids) {
					ofertas.add(darOfertaPorId(id));
				}
			}
				

			tx.commit();
			return ofertas;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}

	/**
	 * Encuentra todas las ofertas de un operador
	 * @param idOperador Identificador del operador
	 * @return Todas las ofertas del operador buscado
	 */
	public List<Oferta> darOfertasPorOperador(long idOperador) {
		return sqlOferta.darOfertasPorOperador(pmf.getPersistenceManager(), idOperador);
	}


	/* ****************************************************************
	 * 			M�todos para manejar los(as) SERVICIOS
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla Servicio
	 * Adiciona entradas al log de la aplicación
	 * @param nombre 
	 * @param costo 
	 * @param x - x de Servicio
	 * @return El objeto Servicio adicionado. null si ocurre alguna Excepción
	 */
	public Servicio adicionarServicio(String nombre, long costo)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idServicio = nextval ();
			if(modoPerron)
				idServicio = idPerron;
			long tuplasInsertadas = sqlServicio.adicionarServicio(pm, idServicio, nombre, costo);
			tx.commit();

			log.trace ("Inserción de vivienda: " + idServicio + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Servicio(idServicio, nombre, costo);
		}
		catch (Exception e)
		{
			//	  	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla Servicio, dado el id
	 * Adiciona entradas al log de la aplicación
	 * @param idServicio - el id
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarServicioPorId (long idServicio)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlServicio.eliminarServicioPorId(pm, idServicio);
			tx.commit();
			return resp;
		}
		catch (Exception e)
		{
			//	    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla Servicio
	 * @return La lista de objetos Servicio, construidos con base en las tuplas de la tabla SERVICIO
	 */
	public List<Servicio> darServicios ()
	{
		return sqlServicio.darServicios(pmf.getPersistenceManager());
	}

	/* ****************************************************************
	 * 			M�todos para manejar la relación INCLUYE
	 *****************************************************************/

	/**
	 * M�todo que inserta, de manera transaccional, una tupla en la tabla INCLUYE
	 * Adiciona entradas al log de la aplicación
	 * @param idServicio - El identificador del servicio - Debe haber un servicio con ese identificador
	 * @param idOferta - El identificador de la oferta - Debe haber una oferta con ese identificador
	 * @param incluido - 
	 * @return Un objeto INCLUYE con la información dada. Null si ocurre alguna Excepción
	 */
	public Incluye adicionarIncluye (long idServicio, long idOferta, int incluido) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlIncluye.adicionarIncluye (pmf.getPersistenceManager(), idServicio, idOferta, incluido);
			tx.commit();

			log.trace ("Inserción de gustan: [" + idServicio + ", " + idOferta + "]. " + tuplasInsertadas + " tuplas insertadas");

			return new Incluye (idServicio, idOferta, incluido);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que elimina, de manera transaccional, una tupla en la tabla INCLUYE, dados los identificadores de servicio y oferta
	 * @param idServicio - El identificador del servicio
	 * @param idOferta - El identificador de la oferta
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarIncluye (long idServicio, long idOferta) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlIncluye.eliminarIncluye (pm, idServicio, idOferta);	            
			tx.commit();

			return resp;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * M�todo que consulta todas las tuplas en la tabla INCLUYE
	 * @return La lista de objetos INCLUYE, construidos con base en las tuplas de la tabla INCLUYE
	 */
	public List<Incluye> darIncluye ()
	{
		return sqlIncluye.darIncluye (pmf.getPersistenceManager());
	}

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Parranderos
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas
	 */
	public long [] limpiarAlohandes ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			//System.out.println("funciono???ñolool");
			long [] resp = sqlUtil.limpiarAlohandes(pm);
			tx.commit ();
			//System.out.println("funciono???");
			log.info ("Borrada la base de datos");
			return resp;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return new long[] {-1, -1, -1, -1, -1, -1, -1};
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}

	/* ****************************************************************
	 * 			M�todos para manejar las consultas
	 *****************************************************************/

	public long reqc1Actual(long idOperador) {
		return reqc1.gananciasAnioActual(pmf.getPersistenceManager(), idOperador);
	}

	public long reqc1Corrido(long idOperador) {
		return reqc1.gananciasAnioCorrido(pmf.getPersistenceManager(), idOperador);
	}

	public List<Oferta> reqc2(){
		return reqc2.ofertasPopulares(pmf.getPersistenceManager());
	}

	public String reqC7MayorDemanda(String tiempo, String alojamiento	) {
		if(alojamiento.equals("SEMANA"))
			return reqc7.respuestaSemanaMayorDemanda(pmf.getPersistenceManager(), alojamiento);
		else
			return reqc7.respuestaMesMayorDemanda(pmf.getPersistenceManager(), alojamiento);
	}

	public String reqC7MenorDemanda(String tiempo, String alojamiento) {
		switch (tiempo) {
		case "SEMANA":
			return reqc7.respuestaSemanaMenorDemanda(pmf.getPersistenceManager(), alojamiento);
		case "MES":
			return reqc7.respuestaMesMenorDemanda(pmf.getPersistenceManager(), alojamiento);
		default:
			return null;
		}
	}

	public String reqC7Ganancia(String tiempo, String alojamiento) {
		switch (tiempo) {
		case "SEMANA":
			return reqc7.respuestaSemanaGanancias(pmf.getPersistenceManager(), alojamiento);
		case "MES":
			return reqc7.respuestaMesGanancias(pmf.getPersistenceManager(), alojamiento);
		default:
			return null;
		}
	}

	public List<Cliente> reqC8(long vivienda) {
		return reqc8.clientesFrecuentes(pmf.getPersistenceManager(), vivienda);	
	}

	public List<Oferta> reqC9(){
		return reqc9.ofertasConPocaDemanda(pmf.getPersistenceManager());
	}

	public List<Cliente> reqC10(DATE inicio, DATE fin, long tipo, String ad) {
		return reqc10.darClientesReservaFechas(pmf.getPersistenceManager(), inicio, fin, tipo, ad);
	}
	
	public List<Object []> reqC11(DATE inicio, DATE fin, long tipo, String ad) {
		return reqc11.darClientesReservaFechas(pmf.getPersistenceManager(), inicio, fin, tipo, ad);
	}
	
	/* ****************************************************************
	 * 			M�todos para activar el modo perron pruebas :v
	 *****************************************************************/
	/**
	 * 
	 */
	public void modoPruebas() {
		modoPerron = true;
	}


	public void desactivarModoPruebas() {
		modoPerron = false;
	}

	/**
	 * 
	 * @param id
	 */
	public void asignarID(long id) {
		idPerron = id;
	}

	/**
	 * 
	 * @param idOferta
	 * @return
	 */
	public List<Reserva> deshabilitarOferta(long idOferta){
		//System.out.println("Entra aqui??????");
		LinkedList<Reserva> ans = new LinkedList<Reserva>();
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			sqlOferta.deshabilitarOferta(pm, idOferta);
			List<Reserva> reservasACancelar = sqlReserva.darReservasPorOferta(pm, idOferta);
			DATE currentTimestamp = new DATE( new Timestamp(System.currentTimeMillis()));
			for(Reserva va: reservasACancelar) {
				if(!va.getFin().dateValue().after(currentTimestamp.dateValue())) continue;
				long idColectiva =va.getColectiva();
				if(va.getColectiva() != null) {
					sqlReservaColectiva.disminuirCantidadColectiva(pm,idColectiva);
					System.out.println("La reserva " + va.getId() + " queda desligada de la reserva colectiva: " + idColectiva);
				}
				eliminarReservaPorId(va.getId());
				DATE ini = va.getInicio(), fin = va.getFin();
				Oferta of = sqlOferta.darOfertasPorRangoFechaDisponibles(pm, ini, fin);
				if(of == null)
					ans.add(va);
				else {
					long nuevo = adicionarReserva(ini, fin,va.getPeriodo_arrendamiento(), va.getCliente(), of.getId(), va.getColectiva()).getId();
					System.out.println("La reserva " + va.getId() + " queda reubicada a la oferta: " + of.getId() + " y su nuevo id es: " + nuevo);
				}
			}
			tx.commit();


			return ans;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * 
	 * @param idOferta
	 * @return
	 */
	public long habilitarOferta(long idOferta) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		long ans;
		try
		{
			tx.begin();
			ans = sqlOferta.habilitarOferta(pm, idOferta);
			tx.commit();
			return ans;
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public void extra() {
		REQCEX extra = new REQCEX(this);
		extra.extraOfertasHabilitadas(pmf.getPersistenceManager());
	}

}
