import java.util.ArrayList;
import java.util.List;


public class Buffer {
	
	private int numClientes;
	private int numServidores;
	private int tamanioBuffer;
	private List<Mensaje> mensajes;
	private boolean finalizado;
	private Cliente[] clientes;
	private Servidor[] servidores;
	//private boolean esperaServidor;
	
	public Buffer(int numClientes,int numServidores,int tamanioBuffer){
		this.numClientes=numClientes;
		this.numServidores=numServidores;
		this.tamanioBuffer=tamanioBuffer;
		this.mensajes=new ArrayList<Mensaje>(this.tamanioBuffer);
		this.finalizado=false;
		this.clientes=new Cliente[this.numClientes];
		this.servidores=new Servidor[this.numServidores];
		//esperaServidor=false;
	}
	
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
	
	public static void main(String[] args) {
		Buffer buffer=new Buffer(2,6,1);
		for(int i=0;i<buffer.numClientes;i++){
			Cliente cliente=new Cliente(15,buffer);
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
