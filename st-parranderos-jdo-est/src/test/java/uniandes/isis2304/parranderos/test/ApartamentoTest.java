package uniandes.isis2304.parranderos.test;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import uniandes.isis2304.alohandes.negocio.Apartamento;
import uniandes.isis2304.alohandes.negocio.Operador;
import uniandes.isis2304.alohandes.persistencia.PersistenciaAlohandes;

public class ApartamentoTest {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(ApartamentoTest.class.getName());
	
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	private PersistenciaAlohandes pm;
	
	/**
	 * Método que prueba las operaciones sobre la tabla apartamento
	 * 1. Adicionar un apartamento
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar un apartamento por su identificador
	 * 4. Borrar un apartamento por su nombre
	 */
    @Test
	public void CRDApartamentoTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre apartamento");
			pm = PersistenciaAlohandes.getInstance ();
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de apartamento incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de apartamento incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de pm y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
    		Operador op = pm.adicionarOperador("..", "..", "..", "PERSONA_NATURAL");
			// Lectura de los tipos de bebida con la tabla vacía
			List <Apartamento> lista = pm.darApartamentos();
			assertEquals ("No debe haber tipos de bebida creados!!", 0, lista.size ());

			// Lectura de los tipos de bebida con un apartamento adicionado
			String direccionapartamento1 = "calle23C#69C-20";
			int cuposapartamento1 = 4;
			String tipoapartamento1 = "ESTANDAR";
			String Categoria1 = "cualquiera";
			int capacidad1 = 4;
			int numero1 = 3;
			double area1 = 45.5;
			int numeroHabitaciones1 = 2;
			Apartamento apartamento1 = pm.adicionarApartamento(direccionapartamento1, cuposapartamento1, op.getId(), area1, true, numeroHabitaciones1);
			lista = pm.darApartamentos();
			
			assertEquals ("Debe haber un apartamento creado !!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", apartamento1, lista.get (0));

			// Lectura de los tipos de bebida con dos tipos de bebida adicionados
			String direccionapartamento2 = "calle23C#69C-20";
			int cuposapartamento2 = 4;
			String tipoapartamento2 = "ESTANDAR";
			String Categoria2 = "cualquiera";
			int capacidad2 = 4;
			int numero2 = 3;
			double area2 = 45.5;
			int numeroHabitaciones2 = 2;
			Apartamento apartamento2 = pm.adicionarApartamento(direccionapartamento2, cuposapartamento2, op.getId(), area2, true, numeroHabitaciones2);
			lista = pm.darApartamentos();
			assertEquals ("Debe haber dos tipos de bebida creados !!", 2, lista.size ());
			assertTrue ("El primer apartamento adicionado debe estar en la tabla", apartamento1.equals (lista.get (0)) || apartamento1.equals (lista.get (1)));
			assertTrue ("El segundo apartamento adicionado debe estar en la tabla", apartamento2.equals (lista.get (0)) || apartamento2.equals (lista.get (1)));

			// Prueba de eliminación de un apartamento, dado su identificador
			long tbEliminados = pm.eliminarApartamentoPorId (apartamento1.getId ());
			assertEquals ("Debe haberse eliminado un apartamento !!", 1, tbEliminados);
			lista = pm.darApartamentos();
			assertEquals ("Debe haber un solo apartamento !!", 1, lista.size ());
			assertFalse ("El primer apartamento adicionado NO debe estar en la tabla", apartamento1.equals (lista.get (0)));
			assertTrue ("El segundo apartamento adicionado debe estar en la tabla", apartamento2.equals (lista.get (0)));
			
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla apartamento.\n";
			msg += "Revise el log de pm y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla apartamento");
		}
		finally
		{
    		pm.cerrarUnidadPersistencia ();    		
		}
	}
	
    /**
     * Método de prueba de la restricción de unicidad sobre el nombre de apartamento
     */
	@Test
	public void unicidadapartamentoTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del id del apartamento");
			pm = PersistenciaAlohandes.getInstance ();
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de apartamento incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de apartamento incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			Operador op = pm.adicionarOperador("..", "..", "..", "PERSONA_NATURAL");
			
			// Lectura de los tipos de bebida con la tabla vacía
			List <Apartamento> lista = pm.darApartamentos();
			assertEquals ("No debe haber tipos de bebida creados!!", 0, lista.size ());
			pm.modoPruebas();
			pm.asignarID(1000);
			String direccionapartamento1 = "calle23C#69C-20";
			int cuposapartamento1 = 4;
			String tipoapartamento1 = "ESTANDAR";
			String Categoria1 = "cualquiera";
			int capacidad1 = 4;
			int numero1 = 3;
			double area1 = 45.5;
			int numeroHabitaciones1 = 2;
			Apartamento apartamento1 = pm.adicionarApartamento(direccionapartamento1, cuposapartamento1, op.getId(), area1, true, numeroHabitaciones1);
			lista = pm.darApartamentos();
			assertEquals ("Debe haber un apartamento creado !!", 1, lista.size ());
			
			
			String direccionapartamento2 = "calle23C#69C-20";
			int cuposapartamento2 = 4;
			String tipoapartamento2 = "ESTANDAR";
			String Categoria2 = "cualquiera";
			int capacidad2 = 4;
			int numero2 = 3;
			double area2 = 45.5;
			int numeroHabitaciones2 = 2;
			Apartamento apartamento2 = pm.adicionarApartamento(direccionapartamento2, cuposapartamento2, op.getId(), area2, true, numeroHabitaciones2);
			assertNull ("No puede adicionar dos apartamentos con el mismo id !!", apartamento2);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla apartamento.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla apartamento");
		}    				
		finally
		{
    		pm.cerrarUnidadPersistencia ();    		
		}
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
}