package cn.uplooking.midlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

import javax.microedition.media.Manager;

public class TankGameCanvas extends GameCanvas implements Runnable,
		CommandListener {
	//----------背景图层数组----------------
	private int[][] cells = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1 },
			{ 0, 2, 2, 2, 0, 2, 2, 2, 2, 0, 0, 2, 2, 2, 2, 2 },
			{ 0, 0, 0, 0, 0, 3, 1, 1, 1, 0, 0, 3, 3, 3, 1, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2 },
			{ 0, 4, 4, 4, 0, 0, 4, 4, 4, 0, 0, 4, 4, 4, 2, 2 },
			{ 0, 4, 4, 4, 0, 0, 3, 3, 3, 0, 0, 4, 4, 4, 2, 2 },
			{ 0, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2 },
			{ 4, 4, 4, 1, 1, 0, 3, 3, 3, 0, 0, 4, 4, 3, 2, 2 },
			{ 0, 4, 4, 1, 1, 0, 4, 4, 4, 0, 0, 4, 4, 3, 2, 2 },
			{ 0, 4, 4, 4, 4, 0, 4, 4, 4, 0, 0, 4, 4, 3, 2, 2 },
			{ 0, 4, 4, 4, 4, 0, 4, 4, 4, 0, 0, 2, 2, 2, 2, 2 },
			{ 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2 },
			{ 0, 2, 2, 2, 2, 0, 1, 1, 1, 0, 0, 4, 4, 4, 2, 2 },
			{ 0, 1, 1, 1, 1, 0, 4, 4, 4, 0, 0, 4, 4, 4, 2, 2 },
			{ 0, 4, 4, 4, 4, 0, 4, 4, 4, 0, 0, 4, 4, 4, 2, 2 },
			{ 0, 4, 4, 4, 4, 0, 4, 4, 4, 0, 0, 4, 4, 4, 1, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
	//-----------生命值图片数组------
	private int[] starCell = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	// --------------定义4个图层-------------
	private TiledLayer bgLayer1;
	private TiledLayer bgLayer2;
	private TiledLayer bgLayer3;
	private TiledLayer bgLayer4;
	private TiledLayer bgLayer5;
	// -----------定义图层管理器-------------
	private LayerManager lm;
	private Graphics g;// --画笔---
	// ------------定义坦克精灵------------
	private Sprite userTankSprite;
	// private Sprite sysTankSprite;
	private boolean isRun = true;// 设置线程启动标识------------
	private int utankX, utankY;// 定义坦克旋转点坐标
	// private int tankX,tankY;//坦克的坐标
	private int bulletX, bulletY;
	private int transform = Sprite.TRANS_NONE;// -旋转角度---

	private SysTankSprite sysTankSprite;// ------敌人坦克
	private Vector sysTankVector;// --定义 敌方坦克集合8
	private Vector userTankVector;// --定义 敌方坦克集合8
	private Image img = null;
	private Image tankImg = null;
	private Image bulletImg;
	private Image tankImg2;
	private Image starImg;
	private Random random;
	private Vector bulletVector;// ---------子弹集合
	private int userTankCount = 0;// 记录用户坦克被击毁的次数
	private int systankCount = 0;// 敌方坦克被击毁的次数
	private int tankCount = 10;// 敌方坦克初始数
	private int starCount = 9;// 生命值
	private Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
			Font.SIZE_LARGE);
	private Command cmdExit = new Command("返回主界面", Command.BACK, 1);// 退出按钮
	private Command cmdRestart = new Command("重新開始", Command.OK, 1);// 退出按钮
	private TankMidlet tMidlet;
	private Player player;// 添加播放器
	private Player player2;// 添加播放器
	private BulletSprite bs;

	public TankGameCanvas(TankMidlet tMidlet) {
		super(true);
		this.tMidlet = tMidlet;
		this.addCommand(cmdExit);
		this.addCommand(cmdRestart);
		this.setCommandListener(this);
		g = this.getGraphics();// 实例化(获得)画笔
		lm = new LayerManager();// 实例化图层管理器
		sysTankVector = new Vector();// ------实例化系统坦克集合
		userTankVector = new Vector();// ------实例化用户坦克集合
		bulletVector = new Vector();// ----实例化子弹集合------
		random = new Random();
		InputStream input = this.getClass().getResourceAsStream("/Hit.wav");
		InputStream input2 = this.getClass()
				.getResourceAsStream("/bgmusic.wav");
		try {
			player = Manager.createPlayer(input, "audio/x-wav");// 构造一个播放器
			player2 = Manager.createPlayer(input2, "audio/x-wav");// 构造一个播放器
			player.prefetch();// 使播放时属于就绪状态
			player2.setLoopCount(-1);
			player2.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		init();
		new Thread(this).start();
	}

	private void init() {
		try {
			// ---加载图片资源------------
			img = Image.createImage("/bg.png");
			tankImg = Image.createImage("/tank.png");
			tankImg2 = Image.createImage("/enemyTank.png");
			starImg = Image.createImage("/star.gif");
			// ---------实例化图层-----------------
			bgLayer1 = new TiledLayer(16, 19, img, img.getWidth() / 4,
					img.getHeight());
			bgLayer2 = new TiledLayer(16, 19, img, img.getWidth() / 4,
					img.getHeight());
			bgLayer3 = new TiledLayer(16, 19, img, img.getWidth() / 4,
					img.getHeight());
			bgLayer4 = new TiledLayer(16, 19, img, img.getWidth() / 4,
					img.getHeight());
			bgLayer5 = new TiledLayer(10, 1, starImg, starImg.getWidth(),
					starImg.getHeight());
			// -------------------实例化精灵---------------
			userTankSprite = new Sprite(tankImg);
			// for (int i = 0; i < 10; i++) {
			// SysTankSprite sysTankSprite = new SysTankSprite(tankImg2,
			// this);// ---敌人坦克
			// sysTankVector.addElement(sysTankSprite);
			// }
			// -------------设置用户坦克坐标-----------
			userTankSprite.setPosition(
					this.getWidth() - userTankSprite.getWidth(),
					this.getHeight() - userTankSprite.getHeight());
			utankX = userTankSprite.getWidth() / 2;
			utankY = userTankSprite.getHeight() / 2;
			userTankSprite.defineReferencePixel(utankX, utankY);// 坦克旋转点
			// -----------把图层及精灵加至图层管理器-------------
			lm.append(bgLayer1);
			lm.append(bgLayer2);
			lm.append(bgLayer3);
			lm.append(bgLayer4);
			lm.append(bgLayer5);
			lm.append(userTankSprite);
			// lm.append(l)
			// lm.append(sysTankSprite);
			lm.setViewWindow(0, 0, this.getWidth(), this.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// --------------键盘输入状态方法--------------
	private void input() {
		// int bulletX = 0, bulletY = 0;
		// int transform = Sprite.TRANS_NONE;// -旋转角度---
		int keyState = this.getKeyStates();
		switch (keyState) {
		case UP_PRESSED:
			transform = Sprite.TRANS_NONE;
			userTankSprite.setTransform(transform);
			if (userTankSprite.getY() >= 0) {// 在画布范围内移动
				userTankSprite.move(0, -2);
				// ------碰到后向相反方向后退一步(就是原地不动)-----------
				if (userTankSprite.collidesWith(bgLayer2, true)
						|| userTankSprite.collidesWith(bgLayer3, false)
						|| userTankSprite.collidesWith(bgLayer4, true)) {
					userTankSprite.move(0, 2);
				}
			}
			break;
		case DOWN_PRESSED:
			transform = Sprite.TRANS_ROT180;
			userTankSprite.setTransform(transform);
			if (userTankSprite.getY() <= this.getHeight()
					- userTankSprite.getHeight()) {
				userTankSprite.move(0, 2);
				if (userTankSprite.collidesWith(bgLayer2, true)
						|| userTankSprite.collidesWith(bgLayer3, false)
						|| userTankSprite.collidesWith(bgLayer4, true)) {
					userTankSprite.move(0, -2);
				}
			}
			break;
		case LEFT_PRESSED:
			transform = Sprite.TRANS_ROT270;
			userTankSprite.setTransform(transform);
			if (userTankSprite.getX() >= 0) {
				userTankSprite.move(-2, 0);
				if (userTankSprite.collidesWith(bgLayer2, true)
						|| userTankSprite.collidesWith(bgLayer3, false)
						|| userTankSprite.collidesWith(bgLayer4, true)) {
					userTankSprite.move(2, 0);
				}
			}
			break;
		case RIGHT_PRESSED:
			transform = Sprite.TRANS_ROT90;
			userTankSprite.setTransform(transform);
			if (userTankSprite.getX() <= this.getWidth()
					- userTankSprite.getWidth()) {
				userTankSprite.move(2, 0);
				if (userTankSprite.collidesWith(bgLayer2, true)
						|| userTankSprite.collidesWith(bgLayer3, true)
						|| userTankSprite.collidesWith(bgLayer4, true)) {
					userTankSprite.move(-2, 0);
				}
			}
			break;
		// ----------发射子弹----------
		case FIRE_PRESSED:
			// Image bulletImg = null;
			try {
				bulletImg = Image.createImage("/bullets.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch (transform) {// --判断方向以此来确定子弹的坐标-------
			case Sprite.TRANS_NONE:
				bulletX = userTankSprite.getX() + userTankSprite.getWidth() / 2;
				bulletY = userTankSprite.getY();
				break;
			case Sprite.TRANS_ROT180:
				bulletX = userTankSprite.getX() + userTankSprite.getWidth() / 2;
				bulletY = userTankSprite.getY() + userTankSprite.getHeight();
				break;
			case Sprite.TRANS_ROT270:
				bulletX = userTankSprite.getX();
				bulletY = userTankSprite.getY() + userTankSprite.getHeight()
						/ 2;
				break;
			case Sprite.TRANS_ROT90:
				bulletX = userTankSprite.getX() + userTankSprite.getWidth();
				bulletY = userTankSprite.getY() + userTankSprite.getHeight()
						/ 2;
				break;
			}
			// ------实例化一个子弹精灵对象,传递图片,坦克方向,子弹坐标,当前屏幕的宽高,当前对象,敌人坦克
			BulletSprite bs = new BulletSprite(bulletImg, transform, bulletX,
					bulletY, this, 1);
			bulletVector.addElement(bs);
			break;
		}
	}

	// -----------系统坦克发射子弹-----------------
	public void sysBullet(BulletSprite bs) {
		bulletVector.addElement(bs);
	}

	// -----------判断敌方坦克与图层碰撞方法--------------
	public boolean isCollideLayer(SysTankSprite sts) {
		if (sts.collidesWith(bgLayer2, true)
				|| sts.collidesWith(bgLayer3, true)
				|| sts.collidesWith(bgLayer4, true)) {
			// sts.move(0, 5);
			return true;
		}
		return false;
	}

	// ---------绘图-------------
	private void draw() {
		// ---------画背景图------------
		g.setColor(0, 0, 0);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				bgLayer1.setCell(j, i, 0);
				bgLayer2.setCell(j, i, 0);
				bgLayer3.setCell(j, i, 0);
				bgLayer4.setCell(j, i, 0);
			}
		}
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				if (cells[i][j] == 1) {
					bgLayer1.setCell(j, i, cells[i][j]);
				} else if (cells[i][j] == 2) {
					bgLayer2.setCell(j, i, cells[i][j]);
				} else if (cells[i][j] == 3) {
					bgLayer3.setCell(j, i, cells[i][j]);
				} else if (cells[i][j] == 4) {
					bgLayer4.setCell(j, i, cells[i][j]);
				}
			}
		}
		// --------------------画生命值----------------
		for (int i = 0; i < 10; i++) {
			if (starCell[i] == 1) {
				bgLayer5.setCell(i, 0, starCell[i]);
			} else {
				bgLayer5.setCell(i, 0, 0);
			}
		}
		// -------------画敌方坦克-------------------
		// int sysSize = sysTankVector.size();
		for (int i = 0; i < sysTankVector.size(); i++) {
			SysTankSprite sysTankSprite = (SysTankSprite) sysTankVector
					.elementAt(i);
			sysTankSprite.paint(g);
		}
		// -------------画用户坦克-------------------
		// int userSize = userTankVector.size();
		// for (int i = 0; i < userSize; i++) {
		// userTankSprite = (Sprite)userTankVector
		// .elementAt(i);
		// userTankSprite.paint(g);
		// }
		// ----------------画子弹----------------
		// int sizeBullet = bulletVector.size();
		for (int i = 0; i < bulletVector.size(); i++) {
			bs = (BulletSprite) bulletVector.elementAt(i);
			bs.paint(g);
		}
		lm.paint(g, 0, 0);
		this.flushGraphics();
	}

	// ---------子弹击中坦克---------------
	public void bulletAndTankSpite(BulletSprite bs) {
		// int size = sysTankVector.size();
		for (int i = 0; i < sysTankVector.size(); i++) {
			SysTankSprite sysTankSprite = (SysTankSprite) sysTankVector
					.elementAt(i);
			// -----------用户的子弹击中敌方坦克--------------
			if (bs.collidesWith(sysTankSprite, true)
					&& bs.getType() != sysTankSprite.getType()) {
				systankCount++;
				// ------击中后启动播放器播放击中效果----------
				try {
					player.start();
				} catch (MediaException e) {
					e.printStackTrace();
				}
				// System.out.println("敌方" + systankCount + "辆坦克被击毁");
				sysTankSprite.sysTankDestory();
				bs.bulletDestory();
				bulletVector.removeElement(bs);
				sysTankSprite.sysTankDestory();
				sysTankVector.removeElement(sysTankSprite);
				if (systankCount == 10) {
					// System.out.println(sysTankVector.size());
					destory();
					g.setFont(font);
					g.setColor(255, 0, 0);
					g.drawString("YOU WIN!!", this.getWidth() / 2,
							this.getHeight() / 2, Graphics.HCENTER
									| Graphics.TOP);
					this.flushGraphics();
				}
				// ---------------用户坦克被击中------------------------
			} else if (bs.collidesWith(userTankSprite, true)) {
				// userTankCount++;
				// System.out.println("我方" + userTankCount + "辆坦克被击毁");
				// -------------生命值减少------------
				// for(int k = 4;k>=0;k--){
				// int k = 4;
				starCell[starCount] = 0;
				starCount--;
				// }
				userTankSprite.setVisible(false);
				bs.bulletDestory();
				bulletVector.removeElement(bs);
				lm.remove(userTankSprite);
				// userTankSprite = null;
				// if (userTankCount < 10) {// 用户有10辆坦克可用
				if (starCount >= 0) {// 用户有10辆坦克可用
					userTankSprite.setPosition(
							random.nextInt(this.getWidth()
									- userTankSprite.getWidth()),
							this.getHeight() - userTankSprite.getHeight());
					lm.append(userTankSprite);
					userTankSprite.setVisible(true);
				} else {// -------十辆坦克被击毁后游戏结束---
					g.setFont(font);
					g.setColor(255, 0, 0);
					destory();
					g.drawString("GAME OVER,YOU LOSE!", this.getWidth() / 2,
							this.getHeight() / 2, Graphics.HCENTER
									| Graphics.TOP);
					this.flushGraphics();
					// isRun = false;
				}
			}
		}
	}

	// ----------------判断游戏结束--------------
	public void destory() {
		//BulletSprite bS = null;
		//SysTankSprite sts = null;
		// ---------遍历子弹-----------------
		//for (int i = 0; i < bulletVector.size(); i++) {
			//bS = (BulletSprite) bulletVector.elementAt(i);
		//}
		bs.bulletDestory();
		// --------遍历敌方坦克---------------
		//for (int i = 0; i < sysTankVector.size(); i++) {
			//sts = (SysTankSprite) sysTankVector.elementAt(i);
		//}
		sysTankSprite.sysTankDestory();
		isRun = false;
		player2.close();

	}

	// ---判断子弹击中墙---------
	public void bulletAndWall(BulletSprite bs) {
		int row = 0;
		int col = 0;
		// -------判断击打方向以此来确定被击毁的墙坐标-----
		switch (transform) {
		case ConStants.BULLET_ORIENTATION_UP:
			row = bs.getY() / img.getHeight();
			col = bs.getX() / (img.getWidth() / 4);
			break;
		case ConStants.BULLET_ORIENTATION_DOWN:
			row = bs.getY() % img.getHeight() > 5 ? bs.getY() / img.getHeight()
					+ 1 : bs.getY() / img.getHeight();
			col = bs.getX() / (img.getWidth() / 4);
			break;
		case ConStants.BULLET_ORIENTATION_LEFT:
			row = bs.getY() / img.getHeight();
			col = bs.getX() / (img.getWidth() / 4);
			break;
		case ConStants.BULLET_ORIENTATION_RIGHT:
			row = bs.getY() / img.getHeight();
			col = bs.getX() % (img.getWidth() / 4) > 5 ? bs.getX()
					/ (img.getWidth() / 4) + 1 : bs.getX()
					/ (img.getWidth() / 4);
			break;
		}
		if (bs.collidesWith(bgLayer3, true)) {// 击中铁墙
			bs.bulletDestory();
			// bulletVector.removeElement(bs);
		} else if (bs.collidesWith(bgLayer4, true)) {// 击中砖墙
			// bs.setVisible(false);
			bs.bulletDestory();
			// bulletVector.removeElement(bs);
			cells[row][col] = 0;
		}
	}

	public void run() {
		// int tankCount = 10;
		while (isRun) {
			input();
			// -------------生成十辆敌方坦克---------------
			if (tankCount > 0) {
				if (random.nextInt(20) == 1) {// ---当随机取数等于1时生成一辆
					sysTankSprite = new SysTankSprite(tankImg2, this);
					sysTankVector.addElement(sysTankSprite);
					// System.out.println(tankCount);
					// System.out.println("----:"+sysTankVector.size());
					tankCount--;

				}
			}
			draw();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void commandAction(Command cmd, Displayable displayable) {
		if (cmd == cmdExit) {
			try {
				player.close();
				player2.close();
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tMidlet.changeCurrent(new WellcomeCanvas(tMidlet));
		} else if (cmd == cmdRestart) {
			try {
				player.close();
				player2.close();
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tMidlet.changeCurrent(new TankGameCanvas(tMidlet));
		}
	}

}
