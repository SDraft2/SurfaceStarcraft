package com.leesb.surfacestarcraft;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundMgr {
	private static SoundMgr instance = null;

	private static final int STREAM_LENGTH = 9;

	private int laser = -1;
	private int laserSuper = -1;
	private int playerDea = -1;
	private int enemyDea = -1;
	private int enemyAtt = -1;
	private int enemyExp = -1;
	private int lose = -1;
	private int errEnergy = -1;
	private int beep = -1;

	private SoundPool _SoundPool  = new SoundPool(STREAM_LENGTH, AudioManager.STREAM_MUSIC, 0);
	private MediaPlayer mp;
	private MediaPlayer errMp;

	public static SoundMgr getInstance(Context context) {
		if (instance == null) {
			instance = new SoundMgr(context);
		}
		return instance;
	}

	public SoundMgr(Context context)
	{
		laser = _SoundPool.load(
				context,
				R.raw.laser,
				1
		);

		laserSuper = _SoundPool.load(
				context,
				R.raw.laser_super,
				1
		);
		playerDea = _SoundPool.load(
				context,
				R.raw.player_death,
				1
		);

		enemyDea = _SoundPool.load(
				context,
				R.raw.enemy_dea,
				1
		);

		enemyAtt = _SoundPool.load(
				context,
				R.raw.enemy_att,
				1
		);

		enemyExp = _SoundPool.load(
				context,
				R.raw.enemy_explo,
				1
		);

		lose = _SoundPool.load(
				context,
				R.raw.youlose,
				1
		);

		errEnergy = _SoundPool.load(
				context,
				R.raw.err_energy,
				1
		);

		beep = _SoundPool.load(
				context,
				R.raw.beep0,
				1
		);

		mp = MediaPlayer.create(MainActivity.context, R.raw.bgm);
		mp.setLooping(true);
		mp.start();

		errMp = MediaPlayer.create(MainActivity.context, R.raw.err_energy);
	}

	public void laser()
	{
		_SoundPool.play(laser, 1, 1, 0, 0, 1);
	}

	public void superLaser()
	{
		_SoundPool.play(laserSuper, 1, 1, 0, 0, 1);
	}
	
	public void playerDeath()
	{
		_SoundPool.play(playerDea, 1, 1, 0, 0, 1);
	}
	
	public void enemyAttack()
	{
		_SoundPool.play(enemyAtt, 1, 1, 0, 0, 1);
		enemyExplosion();
	}
	public void enemyExplosion()
	{
		_SoundPool.play(enemyExp, 1, 1, 0, 0, 1);
	}
	
	public void enemyDeath()
	{
		_SoundPool.play(enemyDea, 1, 1, 0, 0, 1);
	}
	
	public void lose()
	{
		_SoundPool.play(lose, 1, 1, 0, 0, 1);
	}

	public void beep()
	{
		_SoundPool.play(beep, 1, 1, 0, 0, 1);
	}

	public void errEnergy()
	{
		if(!errMp.isPlaying())
		{
			errMp.seekTo(0);
			errMp.start();
		}
	}

}
