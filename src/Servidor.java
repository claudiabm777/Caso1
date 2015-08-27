
public class Servidor extends Thread{
	private Buffer buffer;
	
	public Servidor(Buffer buffer){
		this.buffer=buffer;
	}
	
	
	
	public void run(){
		Integer respuesta=null;
		synchronized(buffer){
			 respuesta=buffer.obtenerMensaje();}
			while(respuesta==null){
				yield();
				synchronized(buffer){respuesta=buffer.obtenerMensaje();
			}
		}
	}
}
