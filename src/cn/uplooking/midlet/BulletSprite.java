package cn.uplooking.midlet;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class BulletSprite extends Sprite implements Runnable {

	private boolean isRun = true;
	private int orientation;
	// private int width, height;
	private int x;
	private int y;
	private TankGameCanvas tgc;
	private int Type;


	protected BulletSprite(Image image, int orientation, int x, int y,
			TankGameCanvas tgc, int Type) {
		// int width, int height,TankGameCanvas tgc,sysTankSprite sts) {
		super(image);
		this.Type = Type;
		this.orientation = orientation;
		this.x = x;
		this.y = y;
		// this.width = width;
		// this.height = height;
		this.tgc = tgc;
		this.setPosition(x, y);
		this.setVisible(true);
		new Thread(this).start();
	}
	//用来判断子弹是系统坦克的还是用户坦克的
	public int getType(){
		return Type;
	}
	//----------销毁方法------------
	public void bulletDestory(){
		this.setVisible(false);
		isRun = false;
	}

	public void run() {
		while (isRun) {
			switch (orientation) {
			case ConStants.BULLET_ORIENTATION_UP:
				this.move(0, -2);
				break;
			case ConStants.BULLET_ORIENTATION_DOWN:
				this.move(0, 2);
				break;
			case ConStants.BULLET_ORIENTATION_LEFT:
				this.move(-2, 0);
				break;
			case ConStants.BULLET_ORIENTATION_RIGHT:
				this.move(2, 0);
				break;
			}
			if (this.getY() <= 0 || this.getY() >= tgc.getHeight()
					|| this.getX() <= 0 || this.getX() >= tgc.getWidth()) {
				isRun = false;
				this.setVisible(false);
			}
			tgc.bulletAndTankSpite(this);
			// tgc.bulletAndTankSpite(this,sts);
			tgc.bulletAndWall(this);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
