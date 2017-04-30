package cn.uplooking.midlet;

import java.util.Random;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class SysTankSprite extends Sprite implements Runnable {

	private boolean isRun = true;// �߳�������ʶ
	private int orientation;// �������
	private TankGameCanvas tgc;
	private Random random;
	// ------------��������,���ýӿ�-----------------
	private int[] irientations = { ConStants.BULLET_ORIENTATION_UP,
			ConStants.BULLET_ORIENTATION_DOWN,
			ConStants.BULLET_ORIENTATION_LEFT,
			ConStants.BULLET_ORIENTATION_RIGHT };
	private Image bulletImg;
	private int bulletX = 0, bulletY = 0;
	private int Type;

	// ------------���췽��-------------
	public SysTankSprite(Image image, TankGameCanvas tgc) {
		super(image);
		this.tgc = tgc;
		random = new Random();
		this.defineReferencePixel(this.getWidth() / 2, this.getHeight() / 2);// ������ת��
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

	// �����ж��ӵ���ϵͳ̹�˵Ļ����û�̹�˵�
	public int getType() {
		return Type;
	}
	//----���ٷ���----------
	public void sysTankDestory(){
		this.setVisible(false);
		isRun = false;
	}

	// --------------�߳�--------------
	public void run() {
		while (isRun) {
			if (random.nextInt(20) == 5) {// �ж�������������5��ʱ��������ı�
				orientation = irientations[random.nextInt(4)];// ���������׃
			}
			switch (orientation) {// -----���ݷ�����ȷ��̹�˵�����·��
			case ConStants.BULLET_ORIENTATION_UP:
				if (this.getY() > 0) {
					this.setTransform(TRANS_NONE);
					this.move(0, -5);
					if (tgc.isCollideLayer(this)) {// ̹����ͼ�����ײ�¼�,����TankGameCanvas�еķ���
						orientation = irientations[random.nextInt(4)];
					}
					break;
				} else {// �����,�����������һ������
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
			// --------------����ӵ�����--------------------
			switch (orientation) {// --�жϷ����Դ���ȷ���ӵ�������-------
			case ConStants.BULLET_ORIENTATION_UP:// ����
				bulletX = this.getX() + this.getWidth() / 2;
				bulletY = this.getY();
				break;
			case ConStants.BULLET_ORIENTATION_DOWN:// ����
				bulletX = this.getX() + this.getWidth() / 2;
				bulletY = this.getY() + this.getHeight();
				break;
			case ConStants.BULLET_ORIENTATION_LEFT:// ����
				bulletX = this.getX();
				// System.out.println(this.getHeight());
				// System.out.println(bulletY);
				bulletY = this.getY() + this.getHeight() / 2;
				break;
			case ConStants.BULLET_ORIENTATION_RIGHT:// ����
				bulletX = this.getX() + this.getWidth();
				bulletY = this.getY() + this.getHeight() / 2;
				break;
			}
			// -------------ʵ�����ӵ�����--------------------
			BulletSprite bs = new BulletSprite(bulletImg, orientation, bulletX,
					bulletY, tgc, 0);
			tgc.sysBullet(bs);// --------------�����ӵ����䷽��-------------------
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

}
