
public class Servidor extends Thread{

	//---------------------------------------------------------------------------------------------
	//-------------------------------------------Atributos-----------------------------------------
	//---------------------------------------------------------------------------------------------

	/**
	 * Buffer con el que se comunica el servidor
	 */
	private Buffer buffer;

	//------------------------------------------------------------
	//----------------------Constructor---------------------------
	//------------------------------------------------------------

	/**
	 * Constructor del servidor <br>
	 * @param buffer buffer  con el cual se comunica con el cliente
	 */
	public Servidor(Buffer buffer){
		this.buffer=buffer;
	}

	//------------------------------------------------------------
	//----------------------Metodos---------------------------
	//------------------------------------------------------------

	/**
	 * Metodo run  del servidor <br>
	 * <b>pre: </b> El Servidos ya se ha creado<br>
	 * <b>post: </b> EL Servidor . <br>
	 */
	public void run(){

		//se crean  una bandera, para saber cuando es necesario de dejar de correr el servidor
		boolean salir=false;
		// se crea un entero Obj para saber 
		Integer respuesta = null;

		synchronized(buffer)
		{
			boolean r=buffer.seTerminoProcesarClientes();
			//System.out.println("Se acabaron de procesar clientes? "+r);
			if(!r)
			{
				respuesta=buffer.obtenerMensaje();
			}
		}
		// se procesan clientes,mientras odavia hays clientes sin ser atendidos
		while(!salir)
		{
			// se sede el procesador (espera activa)
			yield();
			synchronized(buffer)
			{
				//System.out.println("[2] Se acabaron de procesar clientes? "+buffer.seTerminoProcesarClientes());
				if(!buffer.seTerminoProcesarClientes())
				{
					respuesta=buffer.obtenerMensaje();
					//System.out.println("Mensaje que se obtubo resp: "+respuesta+" - "+salir);
				}
				else
				{
					salir=true;
					//System.out.println("[2] Mensaje que se obtubo resp: "+respuesta+" - "+salir);
				}
			}
		}
	}
}
