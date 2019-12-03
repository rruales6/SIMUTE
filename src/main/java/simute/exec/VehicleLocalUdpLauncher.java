package simute.exec;

import java.io.IOException;

/**
*
* @author dordonez@ute.edu.ec
*/
public class VehicleLocalUdpLauncher extends A_VehicleLauncher {

	public static void main(String[] args) throws IOException {
		new VehicleLocalUdpLauncher().init();
	}
	
	public VehicleLocalUdpLauncher() throws IOException{
		super(new VehicleLocalUdpDriver());
		((VehicleLocalUdpDriver)driver).setCanvas(canvas.getPovCanvas());
	}

	@Override
	public void loopConcreteImp() {}
	
}
