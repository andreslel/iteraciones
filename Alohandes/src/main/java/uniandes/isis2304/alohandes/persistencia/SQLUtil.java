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

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 */
class SQLUtil
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
	private PersistenciaAlohandes pa;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pa - El Manejador de persistencia de la aplicación
	 */
	public SQLUtil (PersistenciaAlohandes pa)
	{
		this.pa = pa;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "SELECT "+ pa.darSeqAlohandes () + ".nextval FROM DUAL");
        q.setResultClass(Long.class);
        long resp = (long) q.executeUnique();
        return resp;
	}

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 8 números que indican el número de tuplas borradas en las tablas INCLUYEN, RESERVA, CLIENTE, OFERTA,
	 * SERVICIO, VIVIENDA, SEGURO Y OPERADOR, respectivamente
	 */
	public long [] limpiarAlohandes (PersistenceManager pm)
	{
		Query qReservaColectiva = pm.newQuery(SQL,"DELETE FROM RESERVA_COLECTIVA");
		Query qReserva = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaReserva());
		Query qApartamento = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaApartamento());
		Query qCuarto = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaCuarto());
		Query qEsporadico = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaEsporadico());
		Query qSeguro = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaSeguro());
		Query qHabitacion = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaHabitacion());
		Query qIncluye = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaIncluye());
		Query qOferta = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaOferta());
		Query qVivienda = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaVivienda());
		Query qServicio = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaServicio());
		Query qGanancias = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaGanancias());
		Query qHoteleria = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaHoteleria());
		Query qPersona_natural = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaPersona_Natural());	
		Query qOperador = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaOperador());	
		Query qCliente = pm.newQuery(SQL, "DELETE FROM " + pa.darTablaCliente());
		
		long ReservaEliminados = (long) qReserva.executeUnique();
		qReservaColectiva.executeUnique();
		qApartamento.executeUnique();
		qCuarto.executeUnique();
		qEsporadico.executeUnique();
		long SeguroEliminados = (long) qSeguro.executeUnique();
		qHabitacion.executeUnique();
		long IncluyeEliminados = (long) qIncluye.executeUnique();
		long OfertaEliminados = (long) qOferta.executeUnique();
		long ViviendaEliminados = (long) qVivienda.executeUnique();
		long ServicioEliminados = (long) qServicio.executeUnique();
		qGanancias.executeUnique();
		qHoteleria.executeUnique();
		qPersona_natural.executeUnique();
		long OperadorEliminados = (long) qOperador.executeUnique();
		long ClienteEliminados = (long) qCliente.executeUnique();

		return new long[] {IncluyeEliminados, ReservaEliminados, ClienteEliminados, 
				OfertaEliminados, ServicioEliminados, ViviendaEliminados, SeguroEliminados, OperadorEliminados};
	}

}
