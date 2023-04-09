package com.leesb.surfacestarcraft;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Timer;
import java.util.TimerTask;

public class Player extends Unit
{		
		final static byte LENGTH_HP = 3;
		final static byte LENGTH_MANA = 9;
		final static byte LENGTH_NORM = 1;
		final static byte LENGTH_DEA = 9;
		
		static Player instance = null;

		boolean isDeath = false;


		Bitmap[] normImg;
		Bitmap[] deaImg;
		Bitmap[] hpImg;
		Bitmap[] manaImg;
		
		byte life = 3;
		byte manaIdx = 0;
		byte normImgIdx = 0;
		byte deaImgIdx = 0;

		Timer deathTm;
		TimerTask deathTmTask;

		Timer superLaserTm;

		public Player(int speed, Resources res)
		{
			super(0, 0, 0, 0, speed);


			normImg = new Bitmap[LENGTH_NORM];
			deaImg = new Bitmap[LENGTH_DEA];
			hpImg = new Bitmap[LENGTH_HP];
			manaImg = new Bitmap[LENGTH_MANA];

			for(int i=0 ; i < normImg.length ; i++)
				normImg[i] = BitmapFactory.decodeResource(res, res.getIdentifier( "player_"+i, "drawable" , "com.leesb.surfacestarcraft"));

			for(int i=0 ; i < deaImg.length ; i++)
				deaImg[i] = BitmapFactory.decodeResource(res, res.getIdentifier( "player_d_"+i, "drawable" , "com.leesb.surfacestarcraft"));

			for(int i=0 ; i < hpImg.length ; i++)
				hpImg[i] = BitmapFactory.decodeResource(res, res.getIdentifier( "player_hp_"+i, "drawable" , "com.leesb.surfacestarcraft"));

			for(int i=0 ; i < manaImg.length ; i++)
				manaImg[i] = BitmapFactory.decodeResource(res, res.getIdentifier( "player_mana_"+i, "drawable" , "com.leesb.surfacestarcraft"));

			width = normImg[0].getWidth();
			height = normImg[0].getHeight();

			x = MainActivity.width/2;
			y = MainActivity.height;

			superLaserTm = new Timer();

			superLaserTm.schedule(new TimerTask() {
				public void run() {
					if (manaIdx < 8)
						manaIdx++;
				}
			}, 1000, 1000);

			deathTm = new Timer();

			deathTmTask = new TimerTask() {
				public void run() {
					if(isDeath && deaImgIdx < LENGTH_DEA-1)
					{
						deaImgIdx++;
					}
				}
			};
		}

		public Bitmap getNormImg()
		{
			return normImg[normImgIdx];
		}
		public Bitmap getDeaImg()
		{
			return deaImg[deaImgIdx];
		}
		public Bitmap getHpImg()
		{
			return hpImg[life-1];
		}
		public Bitmap getManaImg()
		{
			return manaImg[manaIdx];
		}

		public static Player getInstance(int speed, Resources res)
		{
			if(instance == null)
				instance = new Player(speed, res);

				return instance;
		}

		public void death()
		{
			isDeath = true;
			deathTm.schedule(deathTmTask, 0, 100);
		}

	}
	