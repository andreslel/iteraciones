/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.alohandes.persistencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.sql.DATE;
import uniandes.isis2304.alohandes.negocio.Ganancias;
import uniandes.isis2304.alohandes.negocio.Oferta;
import uniandes.isis2304.alohandes.negocio.Operador;
import uniandes.isis2304.alohandes.negocio.Oferta;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BEBEDOR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLOferta 
{
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
	public SQLOferta (PersistenciaAlohandes pa)
	{
		this.pa = pa;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un OFERTA a la base de datos de Alohandes
	 * @param idOferta - 
	 * @param precio - 
	 * @param periodo - 
	 * @param idVivienda - 
	 * @param fechaInicio - 
	 * @param fechaFin - 
	 * @return El número de tuplas insertadas 
	 */
	public long adicionarOferta (PersistenceManager pm, long idOferta, long precio, String periodo, long idVivienda, DATE fechaInicio, DATE fechaFin)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pa.darTablaOferta () + "(id, precio, periodo, vivienda, fechainicio, fechafin, habilitada) values (? ,? ,? ,? ,? ,? ,?)");
		q.setParameters( idOferta, precio, periodo, idVivienda, fechaInicio, fechaFin, 1 );
		return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar un OFERTA de la base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idOferta - El identificador de la Oferta
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarOfertaPorId (PersistenceManager pm, long idOferta)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaOferta () + " WHERE id = ?");
		q.setParameters(idOferta);
		return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un OFERTA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idOferta - El identificador de la Oferta
	 * @return El objeto OFERTA que tiene el identificador dado
	 */
	public Oferta darOfertaPorId (PersistenceManager pm, long idOferta)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pa.darTablaOferta () + " WHERE id = ?");
		q.setResultClass(Oferta.class);
		q.setParameters(idOferta);
		Oferta what = (Oferta)q.executeUnique();
		return what;
	}


	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS(AS) Ofertas de la
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Oferta
	 */
	public List<Oferta> darOfertas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pa.darTablaOferta ());
		q.setResultClass(Oferta.class);
		return (List<Oferta>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la informacion de todas las Ofertas de un
	 * operador en la base de datos Alohandes
	 * @param pm - El manejador de persistencia
	 * @param idOperador Identificador del operador
	 * @return Lista con todas las ofertas del operador buscado
	 */
	public List<Oferta> darOfertasPorOperador(PersistenceManager pm, long idOperador) {
		Query q = pm.newQuery(SQL, "SELECT o.id,o.precio, o.periodo,o.vivienda,o.fechaInicio,o.fechaFin  FROM " + pa.darTablaOferta () +" o, "+ pa.darTablaVivienda() +" v "
				+ " WHERE o.vivienda = v.id AND v.operador = ?");
		q.setParameters(idOperador);
		List<Oferta> resp = new LinkedList<>();
		List results = q.executeList();
		for (Object obj : results)
		{
			Object [] datos = (Object []) obj;
			long idoferta =  ((BigDecimal) datos [0]).longValue ();
			int precio = ((BigDecimal) datos [1]).intValue();
			String periodo = ((String) datos [2]);
			long vivienda = ((BigDecimal) datos [3]).longValue();
			DATE fechaInicio = (DATE) datos [4];
			System.out.println("veamos esa fecha que gonorrea es: "+ fechaInicio);
			DATE fechaFin = (DATE) datos [5];
			resp.add (new Oferta(idoferta, precio, periodo, fechaInicio, fechaFin, vivienda));
		}
		return resp;
	}

	/**
	 * M�todo que devuelve las ofertas por rangos de fechas
	 * @param pm
	 * @param ini
	 * @param fin
	 * @return
	 */
	public Oferta darOfertasPorRangoFechaDisponibles(PersistenceManager pm, DATE inicio, DATE fin){
		Query q = pm.newQuery(SQL, ""
				+ "SELECT * FROM " + pa.darTablaOferta()
				+ " WHERE fechaInicio <= ? AND fechaFin >= ? AND habilitado = 1 AND id NOT IN"
				+ "("
				+ "SELECT o.id FROM "
				+ pa.darTablaOferta() + " o, " + pa.darTablaReserva()+ " r"
				+" WHERE o.id = r.oferta AND ((r.inicio >= ?  AND r.inicio <= ?) OR (r.inicio <= ? AND r.fin >= ?))"
				+ ")"
				);
		q.setParameters(inicio,fin,inicio,fin,inicio,inicio);
		List<Oferta> ans = (List<Oferta>)q.executeList();
		if(ans.size() > 0)
			return ans.get(0);
		return null;
	}

	/**
	 * M�todo que cambia el atributo de habilitado de una oferta
	 * @param pm
	 * @param idOferta
	 * @return
	 */
	public long deshabilitarOferta(PersistenceManager pm, long idOferta) {
		Query q = pm.newQuery(SQL, "UPDATE " + pa.darTablaOferta () + " "
				+ "SET habilitada = 0"
				+ " WHERE id = ?");
		q.setParameters(idOferta);
		return (long) q.executeUnique();
	}

	/**
	 * M�todo que cambia el atributo de habilitado de una oferta
	 * @param pm
	 * @param idOferta
	 * @return
	 */
	public long habilitarOferta(PersistenceManager pm, long idOferta) {
		Query q = pm.newQuery(SQL, "UPDATE " + pa.darTablaOferta () + " "
				+ "SET habilitada = 1"
				+ " WHERE id = ?");
		q.setParameters(idOferta);
		return (long) q.executeUnique();
	}
}
