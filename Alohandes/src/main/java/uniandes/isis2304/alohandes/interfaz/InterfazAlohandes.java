package uniandes.isis2304.alohandes.interfaz;

public class InterfazAlohandes {

	public void printMenu() {
		System.out.println("---------ISIS 2304_01 - Sistemas transaccionales----------");
		System.out.println("---------------------Iteraci�n 2----------------------");
		System.out.println("(1) RF1.Registrar operador");
		System.out.println("(2) RF2.Registrar propuesta");
		System.out.println("(3) RF3.Registrar usuario");
		System.out.println("(4) RF4.Registrar reserva");
		System.out.println("(5) RF5.Cancelar una reserva");
		System.out.println("(6) RF6.Retirar una propuesta");
		System.out.println("(7) RF7.Registrar una reserva colectiva");
		System.out.println("(8) RF8.Cancelar reserva colectiva");
		System.out.println("(9) RF9.Deshabilitar una oferta");
		System.out.println("(10) RF10.Rehabilitar una oferta");

		
		System.out.println("---------------------------------------------------");
		System.out.println("(11) RFC1.MOSTRAR EL DINERO RECIBIDO POR CADA PROVEEDOR DE ALOJAMIENTO DURANTE EL AÑO ACTUAL Y EL AÑO CORRIDO");
		System.out.println("(12) RFC2.MOSTRAR LAS 20 OFERTAS M�S POPULARES");		
		System.out.println("(13) RFC7.ANALIZAR OPERACI�N ALOHANDES");		
		System.out.println("(14) RFC8.ENCONTRAR LOS CLIENTES M�S FRECUENTES");		
		System.out.println("(15) RFC9.ENCONTRAR LAS OFERTAS DE ALOJAMIENTO QUE NO TIENEN MUCHA DEMANDA");		
		System.out.println("(16) RFC10.CONSULTAR CONSUMO ALOHANDES");
		System.out.println("(17) RFC11.CONSULTAR CONSUMO ALOHANDES V2");
		
		System.out.println("---------------------------------------------------");
		System.out.println("(20) Salir");
	}
	
	public void printMessage(String mensaje) {
		System.out.println(mensaje);
	}
}
