package simute.net;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public UdpSendImage(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
        imageQueue = new LinkedBlockingQueue<>();
    }
    
    @Override
    public void run() {
        //Recupere las imágenes que se encuentran en la cola (getImageData()) y envíelas al conductor remoto

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
