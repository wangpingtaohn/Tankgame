package cn.uplooking.midlet;

import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

public class WellcomeCanvas extends GameCanvas implements CommandListener {

	private TankMidlet tMidlet;
	private Command cmdExit = new Command("�˳�", Command.EXIT, 1);
	private Command cmdStart = new Command("��ʼ", Command.OK, 1);
	private Graphics g;
	private Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
			Font.SIZE_LARGE);
	private Player player;

	// private Player player2;// ��Ӳ�����

	protected WellcomeCanvas(TankMidlet tMidlet) {
		super(true);
		this.tMidlet = tMidlet;
		this.addCommand(cmdExit);
		this.addCommand(cmdStart);
		this.setCommandListener(this);
		g = this.getGraphics();
		g.setColor(0, 50, 50);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		try {
			// Image bgImage = Image.createImage("/bg.JPG");
			Image logoImage = Image.createImage("/tanklogo.png");
			//Image logoImage3 = Image.createImage("/tanklogo3.PNG");
			// g.drawImage(bgImage, 0, 0, Graphics.LEFT | Graphics.TOP);
			g.drawImage(logoImage, this.getWidth() / 2, this.getHeight() / 2,
					Graphics.HCENTER | Graphics.TOP);
			// g.drawImage(logoImage3, this.getWidth() / 2, this.getHeight() /
			// 2,
			// Graphics.HCENTER | Graphics.TOP);
			// ----------����һ��������----------------
			InputStream input = this.getClass().getResourceAsStream(
					"/wellcome.wav");
			// InputStream input2 = this.getClass().getResourceAsStream(
			// "/bgmusic.wav");
			player = Manager.createPlayer(input, "audio/x-wav");
			// player2 = Manager.createPlayer(input2, "audio/x-wav");// ����һ��������
			player.realize();
			player.setLoopCount(-1);
			player.start();
			// ----------����������Ƶ�ļ����ء��������-------------
		} catch (Exception e) {
			e.printStackTrace();
		}
		g.setColor(255, 0, 0);
		g.setFont(font);
		g.drawString("��ӭ��½̹�˴�ս", this.getWidth() / 2, this.getHeight() / 3,
				Graphics.HCENTER | Graphics.TOP);
		g.drawString("Author:��ƽ��", this.getWidth() / 2, this.getHeight() - 40,
				Graphics.HCENTER | Graphics.TOP);
		this.paint(g);
		this.flushGraphics();
	}

	public void commandAction(Command cmd, Displayable displayable) {
		if (cmd == cmdExit) {
			tMidlet.notifyDestroyed();
		} else if (cmd == cmdStart) {
			player.close();
			try {
				Thread.sleep(200);
				// player2.setLoopCount(-1);
				// player2.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			g.drawString("������...", this.getWidth() / 2, this.getHeight() - 20,
					Graphics.HCENTER | Graphics.TOP);
			this.paint(g);
			this.flushGraphics();
			tMidlet.changeCurrent(new TankGameCanvas(tMidlet));
		}
	}
}
