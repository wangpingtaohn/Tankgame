package cn.uplooking.midlet;

import java.util.Random;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class SysTankSprite extends Sprite implements Runnable {

	private boolean isRun = true;// 线程启动标识
	private int orientation;// 方向参数
	private TankGameCanvas tgc;
	private Random random;
	// ------------方向数组,调用接口-----------------
	private int[] irientations = { ConStants.BULLET_ORIENTATION_UP,
			ConStants.BULLET_ORIENTATION_DOWN,
			ConStants.BULLET_ORIENTATION_LEFT,
			ConStants.BULLET_ORIENTATION_RIGHT };
	private Image bulletImg;
	private int bulletX = 0, bulletY = 0;
	private int Type;

	// ------------构造方法-------------
	public SysTankSprite(Image image, TankGameCanvas tgc) {
		super(image);
		this.tgc = tgc;
		random = new Random();
		this.defineReferencePixel(this.getWidth() / 2, this.getHeight() / 2);// 设置旋转点
		orientation = irientations[random.nextInt(4)];
		this.setPosition(random.nextInt(tgc.getWidth()-this.getWidth()), 0);
		this.setVisible(true);
		try {
			bulletImg = Image.createImage("/bullets.png");
			Thread.sleep(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Thread(this).start();

	}

	// 用来判断子弹是系统坦克的还是用户坦克的
	public int getType() {
		return Type;
	}
	//----销毁方法----------
	public void sysTankDestory(){
		this.setVisible(false);
		isRun = false;
	}

	// --------------线程--------------
	public void run() {
		while (isRun) {
			if (random.nextInt(20) == 5) {// 判断如果随机数等于5的时候方向放声改变
				orientation = irientations[random.nextInt(4)];// 方向放生改變
			}
			switch (orientation) {// -----根据方向来确定坦克的行走路线
			case ConStants.BULLET_ORIENTATION_UP:
				if (this.getY() > 0) {
					this.setTransform(TRANS_NONE);
					this.move(0, -5);
					if (tgc.isCollideLayer(this)) {// 坦克与图层的碰撞事件,调用TankGameCanvas中的方法
						orientation = irientations[random.nextInt(4)];
					}
					break;
				} else {// 出界后,给它随机生成一个方向
					this.move(0, 5);
					orientation = irientations[random.nextInt(4)];
				}
			case ConStants.BULLET_ORIENTATION_DOWN:
				if (this.getY() < tgc.getHeight() - this.getHeight()) {
					this.setTransform(TRANS_ROT180);
					this.move(0, 5);
					if (tgc.isCollideLayer(this)) {
						orientation = irientations[random.nextInt(4)];
					}
					break;
				} else {
					this.move(0, -5);
					orientation = irientations[random.nextInt(4)];
				}
			case ConStants.BULLET_ORIENTATION_LEFT:
				if (this.getX() > 0) {
					this.setTransform(TRANS_ROT270);
					this.move(-5, 0);
					if (tgc.isCollideLayer(this)) {
						orientation = irientations[random.nextInt(4)];
					}
					break;
				} else {
					this.move(5, 0);
					orientation = irientations[random.nextInt(4)];
				}
			case ConStants.BULLET_ORIENTATION_RIGHT:
				if (this.getX() < tgc.getWidth() - this.getWidth()) {
					this.setTransform(TRANS_ROT90);
					this.move(5, 0);
					if (tgc.isCollideLayer(this)) {
						orientation = irientations[random.nextInt(4)];
					}
					break;
				} else {
					this.move(-5, 0);
					orientation = irientations[random.nextInt(4)];
				}
			}
			// --------------获得子弹坐标--------------------
			switch (orientation) {// --判断方向以此来确定子弹的坐标-------
			case ConStants.BULLET_ORIENTATION_UP:// 向上
				bulletX = this.getX() + this.getWidth() / 2;
				bulletY = this.getY();
				break;
			case ConStants.BULLET_ORIENTATION_DOWN:// 向下
				bulletX = this.getX() + this.getWidth() / 2;
				bulletY = this.getY() + this.getHeight();
				break;
			case ConStants.BULLET_ORIENTATION_LEFT:// 向左
				bulletX = this.getX();
				// System.out.println(this.getHeight());
				// System.out.println(bulletY);
				bulletY = this.getY() + this.getHeight() / 2;
				break;
			case ConStants.BULLET_ORIENTATION_RIGHT:// 向右
				bulletX = this.getX() + this.getWidth();
				bulletY = this.getY() + this.getHeight() / 2;
				break;
			}
			// -------------实例化子弹对象--------------------
			BulletSprite bs = new BulletSprite(bulletImg, orientation, bulletX,
					bulletY, tgc, 0);
			tgc.sysBullet(bs);// --------------调用子弹发射方法-------------------
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

}
