package simute.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
*
* @author dordonez@ute.edu.ec
*/
public class UdpReceiveCommand extends Thread {
	private static final int PACKET_SIZE = 32;
	private final BlockingQueue<String> commandQueue;
    private final int puerto;
    DatagramSocket socket ;
    DatagramPacket packet;
    byte[] buffer;
    public UdpReceiveCommand(int puerto) throws IOException {
        this.puerto = puerto;
        commandQueue = new LinkedBlockingQueue<>();
        this.socket= new DatagramSocket(puerto);
        this.buffer= new byte [32];
        this.packet=new DatagramPacket(buffer, buffer.length);
    }

    @Override
    public void run()   {
        //Capture las instrucciones enviadas por el conductor remoto y póngalas en la cola (addInstruccion(...))
    	try 
    	{
			socket.receive(packet);
			String instruccion = new String(packet.getData(), 0, packet.getLength());
			System.out.println(
		    String.format("Recibido: %s; Desde: %s", instruccion, packet.getSocketAddress()));      
		    this.addInstruccion(instruccion);
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally 
    	{
	        socket.close();
    	}
    }

    private void addInstruccion(String instruccion) {
        try {
            commandQueue.put(instruccion);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    //Alguien más debe llamar este método y recuperar las instrucciones de la cola
    public String getInstruccion() {
        return commandQueue.poll();
    }  
}
