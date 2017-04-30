package cn.uplooking.midlet;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class TankMidlet extends MIDlet {

	private Display dis;
	//private TankGameCanvas tgc;
	private WellcomeCanvas wellc;
	public TankMidlet() {
		dis = Display.getDisplay(this);
		//tgc = new TankGameCanvas(this);
		wellc = new WellcomeCanvas(this);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		dis.setCurrent(wellc);
	}
	
	protected void changeCurrent(Displayable displayable) {
		dis.setCurrent(displayable);
		
	}

}
