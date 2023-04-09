package com.leesb.surfacestarcraft;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class GameSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback, SensorEventListener {
	AssetManager assetManager;

	private int score = 0;
	private int highScore = 0;

	private boolean isEndGame = false;
	private boolean isCanvasEnd = false;
	private boolean noticeFlag = false;

	private int enemyCreDelay = 2000;

	private int width = 0;
	private int height = 0;
	private int dValue = 0;

	private int bgY = 0;
	private int bgSpeed = 5;

	private int playerXSize;
	private int playerYSize;
	private int playerXHalfSize;
	private int playerYHalfSize;

	private int enemyXSize;
	private int enemyYSize;
	private int enemyXHalfSize;
	private int enemyYHalfSize;


	private int laserIdx;

	private float[] firstSensorValue;
	private float[] sensorValue;

	private SurfaceHolder sHolder;
	private SensorManager sensorManager;
	private Sensor sensor;
	private SharedPreferences sp;


	Bitmap bgImg;
	Bitmap loseImg;

	Bitmap[] enemyImg;
	Bitmap[] enemyAttImg;
	Bitmap[] enemyDeaImg;
	Bitmap laserImg;
	Bitmap superLaserImg;


	Timer enemyCreTm;
	Timer noticeTm;
	Timer endTm;

	TimerTask enemyCreTmTask;
	TimerTask noticeTmTask;
	TimerTask endTmTask;

	LinkedList<Enemy> enemyVec;

	private SoundMgr sound;

	Player player;
	Bullet[] laser;
	Bullet superLaser;

	public GameSurfaceView(Context context, SharedPreferences sf) {
		super(context);
		sHolder = getHolder();
		sHolder.addCallback(this);

		this.sp = sf;

		highScore = sf.getInt("score", 0);

		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View view, int i, KeyEvent keyEvent) {
				return false;
			}
		});
		assetManager = getContext().getAssets();

		sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

		width = MainActivity.width;
		height = MainActivity.height;
		dValue = height / width * 100;

		firstSensorValue = new float[3];
		firstSensorValue[0] = 1501213;

		enemyVec = new LinkedList<Enemy>();
		sound = new SoundMgr(context);


		loadImage();

		player = Player.getInstance(15, getResources());
		laser = new Bullet[3];

		playerXSize = player.width;
		playerYSize = player.height;
		enemyXSize = enemyImg[0].getWidth();
		enemyYSize = enemyImg[0].getHeight();

		playerXHalfSize = playerXSize/2;
		playerYHalfSize = playerYSize/2;
		enemyXHalfSize = enemyXSize/2;
		enemyYHalfSize = enemyYSize/2;

		for(int i=0 ; i< laser.length ; i++)
			laser[i] = new Bullet(laserImg.getWidth(), laserImg.getHeight(), (int)(dValue * 0.5), 900);

		superLaser = new Bullet(superLaserImg.getWidth(), superLaserImg.getHeight(), (int)(dValue * 1.2), 6000);
	}

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		new Thread(this).start();

		enemyCreTm = new Timer();
		noticeTm = new Timer();
		endTm = new Timer();

		enemyCreTmTask = new TimerTask() {
			@Override
			public void run() {
				if(enemyCreDelay>505)
					enemyCreDelay -= 50;

				if(enemyCreDelay>305)
					enemyCreDelay -= 5;

				for(int i=0 ; i<score/25 + 1 ; i++)
				{
					Enemy enemy = new Enemy();
					enemy.setSpeed(enemy.getSpeed() + (score/55 * MainActivity.height/180));
					enemyVec.add(enemy);
				}

				if(score > 35 && enemyCreDelay > 500)
					enemyCreDelay -= 3;

				if(score > 70 && enemyCreDelay > 500)
					enemyCreDelay -= 3;

			}
		};
		enemyCreTm.schedule(enemyCreTmTask, enemyCreDelay, enemyCreDelay);

		noticeTmTask= new TimerTask()
		{
			@Override
			public void run() {
				noticeTm.cancel();
				if(score > highScore)
				{
					SharedPreferences.Editor edit = sp.edit();
					edit.putInt("score", score);
					edit.commit();
				}

				endTm.schedule(endTmTask, 2000);
				noticeFlag = true;
				sound.beep();
			}
		};

		endTmTask = new TimerTask() {
			@Override
			public void run() {
				enemyCreTm.cancel();
				endTm.cancel();
				isEndGame = true;
				sound.lose();
			}
		};
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

	}

	public void loadImage()
	{
		enemyImg = new Bitmap[Enemy.LENGTH_NORM];
		enemyAttImg = new Bitmap[Enemy.LENGTH_ATT];
		enemyDeaImg = new Bitmap[Enemy.LENGTH_DEA];

		//BitmapFactory.Options option = new BitmapFactory.Options();
		Resources res = getResources();


		bgImg = BitmapFactory.decodeResource(res, R.drawable.bg);
		loseImg = BitmapFactory.decodeResource(res, R.drawable.lose);

		laserImg = BitmapFactory.decodeResource(res, R.drawable.laser);
		superLaserImg =BitmapFactory.decodeResource(res, R.drawable.laser_super);


		for(int i=0 ; i < enemyImg.length ; i++)
			enemyImg[i] = BitmapFactory.decodeResource(res, res.getIdentifier( "enemy_"+i, "drawable" , "com.leesb.surfacestarcraft"));

		for(int i=0 ; i < enemyDeaImg.length ; i++)
			enemyDeaImg[i] = BitmapFactory.decodeResource(res, res.getIdentifier( "enemy_d_"+i, "drawable" , "com.leesb.surfacestarcraft"));

		for(int i=0 ; i < enemyAttImg.length ; i++)
			enemyAttImg[i] = BitmapFactory.decodeResource(res, res.getIdentifier( "enemy_a_"+i, "drawable" , "com.leesb.surfacestarcraft"));
	}


	public void drawShadowStr(String str, int x, int y, int color, Canvas canvas, Paint paint)
	{
		paint.setColor(new Color().rgb(10, 10, 9));
		canvas.drawText(str, x+5, y+5, paint);
		paint.setColor(color);
		canvas.drawText(str, x, y, paint);
	}

	public boolean checkPlayerCollision(int enemyX, int enemyY)
	{
		if(	player.x-playerXHalfSize <= enemyX+enemyXHalfSize &&
			player.x+playerXHalfSize >= enemyX-enemyXHalfSize &&
			player.y-playerYHalfSize <= enemyY+enemyYHalfSize &&
			player.y+playerYHalfSize >= enemyY-enemyYHalfSize)
			return true;
		else
			return false;
	}
	public boolean checkLaserCollision(int idx, int enemyX, int enemyY)
	{
		if(	laser[idx].x-laser[idx].getWidth()/2 <= enemyX+enemyXHalfSize &&
			laser[idx].x+laser[idx].getWidth()/2 >= enemyX-enemyXHalfSize &&
			laser[idx].y-laser[idx].getHeight()/2 <= enemyY-enemyYHalfSize &&
			laser[idx].y+laser[idx].getHeight()/2 >= enemyY-enemyYHalfSize)
			return true;
		else
			return false;
	}

	public boolean checkSuperLaserCollision(int enemyX, int enemyY)
	{
		if(	superLaser.x-superLaser.getWidth()/2 <= enemyX+enemyXHalfSize &&
			superLaser.x+superLaser.getWidth()/2 >= enemyX-enemyXHalfSize &&
			superLaser.y-superLaser.getHeight()/2 <= enemyY+enemyYHalfSize &&
			superLaser.y+superLaser.getHeight()/2 >= enemyY-enemyYHalfSize)
			return true;
		else
			return false;
	}

	public void killEnemy(Enemy enemy)
	{
		enemy.isDeath = true;
		enemy.state = Enemy.STATE_DEATH;
		enemy.death();
		sound.enemyDeath();
		score++;
	}

	//Thread���� �����
	@Override
	public void run() {
		while(!isCanvasEnd) {

			if(player.life <= 0 && !player.isDeath)
			{
				noticeTm.schedule(noticeTmTask, 2000);
				player.death();
				sound.playerDeath();
			}

			bgY += bgSpeed;

			if(+bgY >= 0)
				bgY = -bgImg.getHeight() / 2;

			synchronized(sHolder) {

				Paint paint = new Paint();
				Canvas canvas = sHolder.lockCanvas();
				paint.setTypeface(Typeface.createFromAsset(assetManager, "EUROSTILE-REG.TTF"));
				paint.setTextSize(dValue);

				canvas.drawBitmap(bgImg, 0, bgY, paint);

				for (int i = enemyVec.size() - 1; i >= 0; i--) {
					Enemy enemy = enemyVec.get(i); //이부분 에러는 vector 스레드 문제라 역 for문으로 하니 해결된듯
					if (enemy.isEnd)
						enemyVec.remove(i);
					if (!enemy.isDeath) {
						enemy.y = enemy.y + enemy.getSpeed();
						if (!player.isDeath && checkPlayerCollision(enemy.x, enemy.y)) {
							enemy.isDeath = true;
							player.life--;
							enemy.state = Enemy.STATE_ATTACK;
							sound.enemyAttack();
							enemy.death();
							continue;
						}
						for (int j = 0; j < laser.length; j++) {
							if (laser[j].isFired) {
								if (checkLaserCollision(j, enemy.x, enemy.y)) {
									killEnemy(enemy);
									laser[j].y = -laser[j].getHeight();
									continue;
								}
							}
						}

						if (superLaser.isFired && checkSuperLaserCollision(enemy.x, enemy.y)) {
							killEnemy(enemy);
							continue;
						}
					}

					switch (enemy.state) {
						case Enemy.STATE_NORMAL:
							canvas.drawBitmap(enemyImg[enemy.imgIdx], enemy.x - enemyXHalfSize, enemy.y - enemyYHalfSize, paint);
							break;

						case Enemy.STATE_ATTACK:
							canvas.drawBitmap(enemyAttImg[enemy.imgIdx], enemy.x - enemyAttImg[0].getWidth() / 2, enemy.y - enemyAttImg[0].getHeight() / 2, paint);
							break;

						default:
							canvas.drawBitmap(enemyDeaImg[enemy.imgIdx], enemy.x - enemyDeaImg[0].getWidth() / 2, enemy.y - enemyDeaImg[0].getHeight() / 2, paint);
							break;
					}
				}
				for (int i = 0; i < laser.length; i++)
					canvas.drawBitmap(laserImg, laser[i].x - laser[i].getWidth() / 2, laser[i].y - laser[i].getHeight() / 2, paint);

				canvas.drawBitmap(superLaserImg, superLaser.x - superLaser.getWidth() / 2, superLaser.y - superLaser.getHeight() / 2, paint);

				if (!player.isDeath) {
					if (player.life >= 1) {
						canvas.drawBitmap(player.getHpImg(), player.x - player.getHpImg().getWidth() / 2, player.y + (int) (player.getNormImg().getHeight() / 2.1), paint);
						canvas.drawBitmap(player.getManaImg(), player.x - player.getManaImg().getWidth() / 2, player.y + (int) (player.getNormImg().getHeight() / 1.85), paint);
					}

					canvas.drawBitmap(player.getNormImg(), player.x - playerXHalfSize, player.y - playerYHalfSize, paint);
				} else
					canvas.drawBitmap(player.getDeaImg(), player.x - player.getDeaImg().getWidth() / 2, player.y - player.getDeaImg().getHeight() / 2, paint);

				//g.drawRect(player.x - playerXHalfSize, player.y - playerYHalfSize, playerXSize, playerYSize);


				paint.setColor(new Color().rgb(44, 180, 148));
				canvas.drawRect(dValue / 2, dValue / 2, (int) (dValue * 1.5), (int) (dValue * 1.5), paint);

				paint.setColor(new Color().rgb(244, 4, 4));
				canvas.drawRect(dValue / 2, dValue * 2, (int) (dValue * 1.5), dValue * 3, paint);

				int scoreX = (int) (dValue * 2.1);
				String scoreStr;


					scoreStr = " <-Troll";


				if (score <= 0 && player.isDeath)
					scoreStr = "<-Troll";
				else
				{
					if(score <= highScore)
						scoreStr = highScore + " <-HighScore";
					else
						scoreStr = " <-NewHighScore";
				}


				drawShadowStr(score + "  " + scoreStr, scoreX, (int) (dValue * 1.3), Color.GREEN, canvas, paint);

				if (!player.isDeath)
					drawShadowStr("0", scoreX, (int) (dValue * 2.8), Color.GREEN, canvas, paint);

				else
					drawShadowStr("1", scoreX, (int) (dValue * 2.8), Color.GREEN, canvas, paint);

				if (noticeFlag) {
					paint.setColor(Color.WHITE);
					paint.setTextSize((int) (dValue * 0.8));
					drawShadowStr("Norad II has been destroyed", (int) (dValue * 0.5), (int) (dValue * 20), new Color().rgb(184, 184, 232), canvas, paint);
				}

				if (isEndGame) {
					canvas.drawBitmap(loseImg, width / 2 - loseImg.getWidth() / 2, height / 2 - loseImg.getHeight() / 2, null);
					isCanvasEnd = true;
				}


				sHolder.unlockCanvasAndPost(canvas);

				try {
					Thread.sleep(5);
				} catch (Exception e) {

				}
			}
		}
	}


	//Timer�� �����

	public void fireLaser()
	{
		if(!laser[laserIdx].isWaitDelay && !player.isDeath)
		{
			if(laserIdx<2)
				laserIdx++;
			else
				laserIdx = 0;

			laser[laserIdx].x = player.x;
			laser[laserIdx].y = player.y + (int)(laser[laserIdx].height * 0.05);
			laser[laserIdx].fire();
			sound.laser();
		}
	}
	public void fireSuperLaser()
	{
		if(!player.isDeath)
		{
			if(player.manaIdx == 8)
			{
				superLaser.x = player.x;
				superLaser.y = player.y - (player.getHeight());
				superLaser.fire();
				player.manaIdx = 0;
				sound.superLaser();
			}
			else
				sound.errEnergy();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_UP:
				fireLaser();
				break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		sensorValue = sensorEvent.values;

		if(firstSensorValue[0] == 1501213)
		{
			firstSensorValue[0] = sensorValue[0];
			firstSensorValue[1] = sensorValue[1];
			firstSensorValue[2] = sensorValue[2];
		}

		int xValue = (int)(sensorValue[2] - firstSensorValue[2]);
		int yValue = (int)(sensorValue[1] - firstSensorValue[1]);

		//System.out.println(xValue + " - " + yValue);

		if(xValue > player.speed)
			xValue = player.speed;
		else if(xValue < -player.speed)
			xValue = -player.speed;
		else if(yValue > player.speed)
			yValue = player.speed;
		else if(yValue < -player.speed)
			yValue = -player.speed;

		if(!player.isDeath)
		{
			if(player.x - playerXHalfSize - xValue >= 0 && player.x - playerXHalfSize - xValue <= width - playerXSize - xValue)
				player.x = player.x - xValue;
			else if(!(player.x - playerXHalfSize - xValue >= 0))
				player.x = playerXHalfSize - xValue;
			else
				player.x = width - playerXHalfSize - xValue;


			if(player.y - playerYHalfSize - yValue >= 0 && player.y - playerYSize - yValue <= height - playerYSize*2 - yValue)
				player.y = player.y -yValue;
			else if(!(player.y - playerYHalfSize - yValue >= 0))
				player.y = playerYHalfSize - yValue;
			else
				player.y = height - playerYSize - yValue;
		}

		//System.out.println(sensorValue[0] + "\n" + sensorValue[1] + "\n" + sensorValue[2]);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}
}
