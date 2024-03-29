package simute.exec;

import java.awt.event.KeyListener;
import java.io.IOException;

import simute.Env;
import simute.ImageRenderer;
import simute.gui.I_Canvas;
import simute.net.UdpReceiveCommand;
import simute.net.UdpSendImage;

/**
*
* @author dordonez@ute.edu.ec
*/
public class VehicleLocalUdpDriver implements I_Driver {
	protected boolean wFlag = false;
	protected boolean sFlag = false;
	protected boolean aFlag = false;
	protected boolean dFlag = false;	
	private float speed = 0;
	private float turn = 0;
	private I_Canvas canvas;
	
	private static final int LOCAL_PORT = 8888;
	private static final int REMOTE_PORT = 8889;
	private static final String REMOTE_HOST = "localhost";
	
	private final UdpReceiveCommand getCommand;
	private final UdpSendImage sendImage;
	
	public VehicleLocalUdpDriver() throws IOException {
		getCommand = new UdpReceiveCommand(LOCAL_PORT);
		sendImage = new UdpSendImage(REMOTE_HOST, REMOTE_PORT);
		getCommand.start();
		sendImage.start();
	}
	
	public void update() {
		sendImage.addImage(ImageRenderer.getResizedImg(canvas.getFrameImage(), 400, 300));
		updateDrivingFlags(getCommand.getInstruccion());
		if(wFlag && (speed < (Env.MAX_SPEED-Env.SPEED_STEP))) speed += Env.SPEED_STEP;
		if(sFlag && (speed > -(Env.MAX_SPEED-Env.SPEED_STEP))) speed -= Env.SPEED_STEP;
		if(aFlag && (turn > -(Env.MAX_TURN-Env.TURN_STEP))) turn -= Env.TURN_STEP;
		if(dFlag && (turn < (Env.MAX_TURN-Env.TURN_STEP))) turn += Env.TURN_STEP;		
		if(!aFlag && !dFlag) turn = Env.STOP;	
		System.out.println("Speed: " + speed + " / Turn: " + turn);
	}

	public float getLoopParam(String par) {
		if(par.equals("speed")) {
			return speed;
		} else if(par.equals("turn")) {
			return turn;
		}
		throw new RuntimeException("Parameter: '" + par + "' does not exist !!!");
	}
	
	public void reset() {
	}
	
	public void setCanvas(I_Canvas canvas) {
		this.canvas = canvas;
	}
	
	public KeyListener getKeyListener() {
		return null;
	}
		
	private void updateDrivingFlags(String action) {
		if(action == null) return;
		switch(action) {
	        case "w":
	        case "W":
	        	wFlag = true;
	        	break;
	        case "s":
	        case "S":
	        	sFlag = true;
	        	break;                	
	        case "a":
	        case "A":
	        	aFlag = true;
	    		break;                	
	        case "d":
	        case "D":
	        	dFlag = true;
	    		break; 
	        case "!w":
	        case "!W":
	        	wFlag = false;
	        	break;
	        case "!s":
	        case "!S":
	        	sFlag = false;
	        	break;                	
	        case "!a":
	        case "!A":
	        	aFlag = false;
	    		break;                	
	        case "!d":
	        case "!D":
	        	dFlag = false;
	    		break; 	    		
	        case " ":
	        case "! ":	
	    		speed = Env.STOP;
	    		break;	
		}
	}		
	
}
