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
	//----------����ͼ������----------------
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
	//-----------����ֵͼƬ����------
	private int[] starCell = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	// --------------����4��ͼ��-------------
	private TiledLayer bgLayer1;
	private TiledLayer bgLayer2;
	private TiledLayer bgLayer3;
	private TiledLayer bgLayer4;
	private TiledLayer bgLayer5;
	// -----------����ͼ�������-------------
	private LayerManager lm;
	private Graphics g;// --����---
	// ------------����̹�˾���------------
	private Sprite userTankSprite;
	// private Sprite sysTankSprite;
	private boolean isRun = true;// �����߳�������ʶ------------
	private int utankX, utankY;// ����̹����ת������
	// private int tankX,tankY;//̹�˵�����
	private int bulletX, bulletY;
	private int transform = Sprite.TRANS_NONE;// -��ת�Ƕ�---

	private SysTankSprite sysTankSprite;// ------����̹��
	private Vector sysTankVector;// --���� �з�̹�˼���8
	private Vector userTankVector;// --���� �з�̹�˼���8
	private Image img = null;
	private Image tankImg = null;
	private Image bulletImg;
	private Image tankImg2;
	private Image starImg;
	private Random random;
	private Vector bulletVector;// ---------�ӵ�����
	private int userTankCount = 0;// ��¼�û�̹�˱����ٵĴ���
	private int systankCount = 0;// �з�̹�˱����ٵĴ���
	private int tankCount = 10;// �з�̹�˳�ʼ��
	private int starCount = 9;// ����ֵ
	private Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
			Font.SIZE_LARGE);
	private Command cmdExit = new Command("����������", Command.BACK, 1);// �˳���ť
	private Command cmdRestart = new Command("�����_ʼ", Command.OK, 1);// �˳���ť
	private TankMidlet tMidlet;
	private Player player;// ��Ӳ�����
	private Player player2;// ��Ӳ�����
	private BulletSprite bs;

	public TankGameCanvas(TankMidlet tMidlet) {
		super(true);
		this.tMidlet = tMidlet;
		this.addCommand(cmdExit);
		this.addCommand(cmdRestart);
		this.setCommandListener(this);
		g = this.getGraphics();// ʵ����(���)����
		lm = new LayerManager();// ʵ����ͼ�������
		sysTankVector = new Vector();// ------ʵ����ϵͳ̹�˼���
		userTankVector = new Vector();// ------ʵ�����û�̹�˼���
		bulletVector = new Vector();// ----ʵ�����ӵ�����------
		random = new Random();
		InputStream input = this.getClass().getResourceAsStream("/Hit.wav");
		InputStream input2 = this.getClass()
				.getResourceAsStream("/bgmusic.wav");
		try {
			player = Manager.createPlayer(input, "audio/x-wav");// ����һ��������
			player2 = Manager.createPlayer(input2, "audio/x-wav");// ����һ��������
			player.prefetch();// ʹ����ʱ���ھ���״̬
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
			// ---����ͼƬ��Դ------------
			img = Image.createImage("/bg.png");
			tankImg = Image.createImage("/tank.png");
			tankImg2 = Image.createImage("/enemyTank.png");
			starImg = Image.createImage("/star.gif");
			// ---------ʵ����ͼ��-----------------
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
			// -------------------ʵ��������---------------
			userTankSprite = new Sprite(tankImg);
			// for (int i = 0; i < 10; i++) {
			// SysTankSprite sysTankSprite = new SysTankSprite(tankImg2,
			// this);// ---����̹��
			// sysTankVector.addElement(sysTankSprite);
			// }
			// -------------�����û�̹������-----------
			userTankSprite.setPosition(
					this.getWidth() - userTankSprite.getWidth(),
					this.getHeight() - userTankSprite.getHeight());
			utankX = userTankSprite.getWidth() / 2;
			utankY = userTankSprite.getHeight() / 2;
			userTankSprite.defineReferencePixel(utankX, utankY);// ̹����ת��
			// -----------��ͼ�㼰�������ͼ�������-------------
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

	// --------------��������״̬����--------------
	private void input() {
		// int bulletX = 0, bulletY = 0;
		// int transform = Sprite.TRANS_NONE;// -��ת�Ƕ�---
		int keyState = this.getKeyStates();
		switch (keyState) {
		case UP_PRESSED:
			transform = Sprite.TRANS_NONE;
			userTankSprite.setTransform(transform);
			if (userTankSprite.getY() >= 0) {// �ڻ�����Χ���ƶ�
				userTankSprite.move(0, -2);
				// ------���������෴�������һ��(����ԭ�ز���)-----------
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
		// ----------�����ӵ�----------
		case FIRE_PRESSED:
			// Image bulletImg = null;
			try {
				bulletImg = Image.createImage("/bullets.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			switch (transform) {// --�жϷ����Դ���ȷ���ӵ�������-------
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
			// ------ʵ����һ���ӵ��������,����ͼƬ,̹�˷���,�ӵ�����,��ǰ��Ļ�Ŀ��,��ǰ����,����̹��
			BulletSprite bs = new BulletSprite(bulletImg, transform, bulletX,
					bulletY, this, 1);
			bulletVector.addElement(bs);
			break;
		}
	}

	// -----------ϵͳ̹�˷����ӵ�-----------------
	public void sysBullet(BulletSprite bs) {
		bulletVector.addElement(bs);
	}

	// -----------�жϵз�̹����ͼ����ײ����--------------
	public boolean isCollideLayer(SysTankSprite sts) {
		if (sts.collidesWith(bgLayer2, true)
				|| sts.collidesWith(bgLayer3, true)
				|| sts.collidesWith(bgLayer4, true)) {
			// sts.move(0, 5);
			return true;
		}
		return false;
	}

	// ---------��ͼ-------------
	private void draw() {
		// ---------������ͼ------------
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
		// --------------------������ֵ----------------
		for (int i = 0; i < 10; i++) {
			if (starCell[i] == 1) {
				bgLayer5.setCell(i, 0, starCell[i]);
			} else {
				bgLayer5.setCell(i, 0, 0);
			}
		}
		// -------------���з�̹��-------------------
		// int sysSize = sysTankVector.size();
		for (int i = 0; i < sysTankVector.size(); i++) {
			SysTankSprite sysTankSprite = (SysTankSprite) sysTankVector
					.elementAt(i);
			sysTankSprite.paint(g);
		}
		// -------------���û�̹��-------------------
		// int userSize = userTankVector.size();
		// for (int i = 0; i < userSize; i++) {
		// userTankSprite = (Sprite)userTankVector
		// .elementAt(i);
		// userTankSprite.paint(g);
		// }
		// ----------------���ӵ�----------------
		// int sizeBullet = bulletVector.size();
		for (int i = 0; i < bulletVector.size(); i++) {
			bs = (BulletSprite) bulletVector.elementAt(i);
			bs.paint(g);
		}
		lm.paint(g, 0, 0);
		this.flushGraphics();
	}

	// ---------�ӵ�����̹��---------------
	public void bulletAndTankSpite(BulletSprite bs) {
		// int size = sysTankVector.size();
		for (int i = 0; i < sysTankVector.size(); i++) {
			SysTankSprite sysTankSprite = (SysTankSprite) sysTankVector
					.elementAt(i);
			// -----------�û����ӵ����ез�̹��--------------
			if (bs.collidesWith(sysTankSprite, true)
					&& bs.getType() != sysTankSprite.getType()) {
				systankCount++;
				// ------���к��������������Ż���Ч��----------
				try {
					player.start();
				} catch (MediaException e) {
					e.printStackTrace();
				}
				// System.out.println("�з�" + systankCount + "��̹�˱�����");
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
				// ---------------�û�̹�˱�����------------------------
			} else if (bs.collidesWith(userTankSprite, true)) {
				// userTankCount++;
				// System.out.println("�ҷ�" + userTankCount + "��̹�˱�����");
				// -------------����ֵ����------------
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
				// if (userTankCount < 10) {// �û���10��̹�˿���
				if (starCount >= 0) {// �û���10��̹�˿���
					userTankSprite.setPosition(
							random.nextInt(this.getWidth()
									- userTankSprite.getWidth()),
							this.getHeight() - userTankSprite.getHeight());
					lm.append(userTankSprite);
					userTankSprite.setVisible(true);
				} else {// -------ʮ��̹�˱����ٺ���Ϸ����---
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

	// ----------------�ж���Ϸ����--------------
	public void destory() {
		//BulletSprite bS = null;
		//SysTankSprite sts = null;
		// ---------�����ӵ�-----------------
		//for (int i = 0; i < bulletVector.size(); i++) {
			//bS = (BulletSprite) bulletVector.elementAt(i);
		//}
		bs.bulletDestory();
		// --------�����з�̹��---------------
		//for (int i = 0; i < sysTankVector.size(); i++) {
			//sts = (SysTankSprite) sysTankVector.elementAt(i);
		//}
		sysTankSprite.sysTankDestory();
		isRun = false;
		player2.close();

	}

	// ---�ж��ӵ�����ǽ---------
	public void bulletAndWall(BulletSprite bs) {
		int row = 0;
		int col = 0;
		// -------�жϻ������Դ���ȷ�������ٵ�ǽ����-----
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
		if (bs.collidesWith(bgLayer3, true)) {// ������ǽ
			bs.bulletDestory();
			// bulletVector.removeElement(bs);
		} else if (bs.collidesWith(bgLayer4, true)) {// ����שǽ
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
			// -------------����ʮ���з�̹��---------------
			if (tankCount > 0) {
				if (random.nextInt(20) == 1) {// ---�����ȡ������1ʱ����һ��
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
