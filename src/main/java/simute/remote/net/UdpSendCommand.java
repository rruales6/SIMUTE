
package simute.remote.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import udp.Server;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class UdpSendCommand extends Thread {       
        private final BlockingQueue<String> commandQueue;
        private final String host;
        private final int puerto;
        private DatagramSocket socket;
        private DatagramPacket paquete;
        private InetAddress direccion;
        byte[] buffer;

        public UdpSendCommand(String host, int puerto) throws IOException {
            this.host = host;
            this.puerto = puerto;
            commandQueue = new LinkedBlockingQueue<>();
            this.socket= new DatagramSocket();
            this.direccion= InetAddress.getByName(host);
        }
        //deben enviarse caracter por caracter
        @Override
        public void run() {
            //Recupere las instrucciones que se encuentran en la cola (getInstruccion()) y envíelas al vehículo remoto
        	this.paquete= new DatagramPacket(this.getInstruccion(), this.getInstruccion().length, direccion, this.puerto); 
        	try {
				this.socket.send(paquete);
				String.format("Enviado: %s; A: %s", paquete.toString(), paquete.getSocketAddress());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	finally 
        	{
        		this.socket.close();
        	}
        }
        
        //Alguien más debe llamar este método y poner las instrucciones en la cola        
        public void addInstruccion(String instruccion) {
            try {
                commandQueue.put(instruccion);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        private byte[] getInstruccion() {
            try {
                String instruccion = commandQueue.take();
                return instruccion.getBytes();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }    
