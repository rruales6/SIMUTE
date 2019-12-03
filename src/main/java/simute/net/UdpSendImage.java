package simute.net;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

/**
*
* @author dordonez@ute.edu.ec
*/
public class UdpSendImage extends Thread {
    private final BlockingQueue<BufferedImage> imageQueue;
    private final String host;
    private final int puerto;
    private DatagramSocket socket;
    private DatagramPacket paquete;
    private InetAddress direccion;
    byte[] buffer;
    
    public UdpSendImage(String host, int puerto) throws IOException {
        this.host = host;
        this.puerto = puerto;
        imageQueue = new LinkedBlockingQueue<>();
        this.socket= new DatagramSocket();
        this.direccion= InetAddress.getByName(host);
    }
    
    @Override
    public void run() {
        //Recupere las imágenes que se encuentran en la cola (getImageData()) y envíelas al conductor remoto
    	this.paquete= new DatagramPacket(this.getImageData(), this.getImageData().length, direccion, this.puerto); 
    	try {
			this.socket.send(paquete);
			String.format("Enviada Imagen: %s; A: %s", paquete.toString(), paquete.getSocketAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally 
    	{
    		this.socket.close();
    	}
    }
    
    //Alguien más debe llamar este método y poner las imágenes en la cola
    public void addImage(BufferedImage image) {
        try {
            imageQueue.put(image);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    private byte[] getImageData() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage image = imageQueue.take();
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            byte[] buffer = baos.toByteArray();
            baos.close();
            return buffer;
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
