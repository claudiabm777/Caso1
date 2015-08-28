
public class Servidor extends Thread{
	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//Atributos------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	
	/**
	 * Buffer con el que se comunica el servidor
	 */
	private Buffer buffer;
	
	
	
	
	public Servidor(Buffer buffer){
		this.buffer=buffer;
	}
	
	
	
	public void run(){

		Integer respuesta=null;
		boolean salir=false;
		synchronized(buffer){
			boolean r=buffer.seTerminoProcesarClientes();
			//System.out.println("Se acabaron de procesar clientes? "+r);
			if(!r){
				respuesta=buffer.obtenerMensaje();
			}
		}
		
		while(!salir){
			yield();
			synchronized(buffer){
				//System.out.println("[2] Se acabaron de procesar clientes? "+buffer.seTerminoProcesarClientes());
				if(!buffer.seTerminoProcesarClientes()){
					respuesta=buffer.obtenerMensaje();
					//System.out.println("Mensaje que se obtubo resp: "+respuesta+" - "+salir);
				}
				else{
					salir=true;
					//System.out.println("[2] Mensaje que se obtubo resp: "+respuesta+" - "+salir);
				}

			}

		}

	}
}
