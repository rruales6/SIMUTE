
package simute.remote.net;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.imageio.ImageIO;

/**
 *
 * @author dordonez@ute.edu.ec
 */
public class UdpReceiveImage extends Thread {
    private final JLabel monitor;
    private final int puerto;
    DatagramSocket socket ;
    DatagramPacket packet;
    byte[] buffer;
    BufferedImage img;
    ByteArrayInputStream bais;
     
    
    public UdpReceiveImage(JLabel monitor, int puerto) throws IOException {
        this.monitor = monitor;
        this.puerto = puerto;
        this.socket= new DatagramSocket(puerto);
        this.buffer= new byte [32];
        this.packet=new DatagramPacket(buffer, buffer.length);
    }

    @Override
    public void run() {
        //Capture las imagenes enviadas por el vehículo remoto y muéstrelas en el monitor (JLabel)
    	try {
			socket.receive(packet);
			buffer= packet.getData();
			bais= new ByteArrayInputStream(buffer);
			img= ImageIO.read(bais);
			this.monitor.setIcon(new ImageIcon(img));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally
    	{
    		socket.close();
    	}
    }

}
