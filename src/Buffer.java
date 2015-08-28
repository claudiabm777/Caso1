import java.util.ArrayList;
import java.util.List;

/**
 * Clase Buffer del Caso 1
 * @author Asus
 *
 */
public class Buffer {
	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//Atributos------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	
	/**
	 * Número de clientes.
	 */
	private int numClientes;
	
	/**
	 * Número de servidores.
	 */
	private int numServidores;
	
	/**
	 * Tamaño del buffer. Número de mensajes que puede almacenar
	 */
	private int tamanioBuffer;
	
	/**
	 * Esta es la lista que va a almacenar los mensajes que los clientes depositan en el buffer.
	 * El tamaño de esta lista es la capacidad del buffer (tamanioBuffer). Esta lista es el buffer.
	 */
	private List<Mensaje> mensajes;
	
	/**
	 * Indica si ya se terminaron de procesar todos los clientes 
	 * (TRUE si ya se termino, FALSE de lo contrario)
	 */
	private boolean finalizado;
	
	/**
	 * Arreglo de clientes que van a participar en el sistema
	 */
	private Cliente[] clientes;
	
	/**
	 * Arreglo de servidores que van a participar en el sistema
	 */
	private Servidor[] servidores;
	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//Constructor----------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	/**
	 * Constructor de la clase
	 * @param numClientes: número de clientes del sistema
	 * @param numServidores: numero de servidores del sistema
	 * @param tamanioBuffer: capacidad de almacenaje de mensajes que va a tener el buffer
	 */
	public Buffer(int numClientes,int numServidores,int tamanioBuffer){
		this.numClientes=numClientes;
		this.numServidores=numServidores;
		this.tamanioBuffer=tamanioBuffer;
		this.mensajes=new ArrayList<Mensaje>(this.tamanioBuffer);
		this.finalizado=false;
		this.clientes=new Cliente[this.numClientes];
		this.servidores=new Servidor[this.numServidores];
		
	}

	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//Métodos--------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	
	/**
	 * Método que recibe un mensaje del cliente y lo guarda en el buffer
	 * @param mensaje: mensaje a guardar en el buffer, si no lo logra, queda esperando sobre el buffer
	 * @return El mensaje guardado
	 */
	public synchronized Mensaje guardarMensaje(Mensaje mensaje){
		while(mensajes.size()==tamanioBuffer){
			try {
				System.out.println("En espera para guardar: "+mensaje.getNumMensaje());
				wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mensajes.add(mensaje);
		
		System.out.println("Mensaje guardado: "+mensaje.getNumMensaje());		
		return mensaje;
	}
	/**
	 * Método que saca el mensaje del buffer e incrementa este mensaje en 1
	 * @return un entero con el nuevo valor del mensaje
	 */
	public  Integer obtenerMensaje(){
		Integer respuesta=null;
		if(mensajes.size()==0){
			respuesta=null;
		}
		else{
		Mensaje mensajeProcesado=mensajes.remove(0);
		System.out.println("Antes: "+(mensajeProcesado.getNumMensaje()));
		mensajeProcesado.setNumMensaje(mensajeProcesado.getNumMensaje()+1);
		System.out.println("Despues: "+(mensajeProcesado.getNumMensaje()));
		notifyAll();
		synchronized(mensajeProcesado){mensajeProcesado.notify();}
		
		respuesta= mensajeProcesado.getNumMensaje();
		}
		return respuesta;
	}
	
	/**
	 * Método que determina si se terminaron de procesar los clientes
	 * @return un booleano indicando si ya se atendieron a todos los clientes (TRUE) o no (FALSE)
	 */
	public  boolean seTerminoProcesarClientes(){
		synchronized(clientes){
		int contadorClientes=0;
		for(int i=0;i<numClientes;i++){
			if(clientes[i].revisarEstadoMensajes()){
				contadorClientes++;
			}
		}
		if(contadorClientes==clientes.length){
			finalizado=true;
		}
		return finalizado;
		}
	}
	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//Main-----------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	
	/**
	 * MAIN del programa
	 * @param args
	 */
	public static void main(String[] args) {
		Buffer buffer=new Buffer(20,6,1);
		for(int i=0;i<buffer.numClientes;i++){
			Cliente cliente=new Cliente(1,buffer);
			buffer.clientes[i]=cliente;
			buffer.clientes[i].start();
		}
		
		for(int i=0;i<buffer.numServidores;i++){
			Servidor servidor=new Servidor(buffer);
			buffer.servidores[i]=servidor;
			buffer.servidores[i].start();
		}

	}
	
}
