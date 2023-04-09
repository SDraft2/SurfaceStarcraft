package com.leesb.surfacestarcraft;

import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends Unit{
	final static byte STATE_NORMAL = 0;
	final static byte STATE_ATTACK = 1;
	final static byte STATE_DEATH = 2;
	final static byte LENGTH_NORM = 4;
	final static byte LENGTH_ATT= 8;
	final static byte LENGTH_DEA= 8;
	
	boolean isDeath = false;
	boolean isEnd = false;

	byte imgIdx = 0;
	byte idxAdder = -1;
	byte state = 0;
	
	Timer animTm;
	TimerTask animTmTask;
	
	public byte getState()
	{
		return state;
	}
	public void setState(byte state)
	{
		this.state = state;
	}
	
	
	public Enemy()
	{
		super((int) (Math.random()*MainActivity.width *0.8) + (int) (MainActivity.width*0.1), 0, 0, 0, (int)(Math.random()*MainActivity.height/180) + MainActivity.height/180);

		animTm = new Timer();
		animTmTask = new TimerTask()
		{
			@Override
			public void run() {
				if(!isDeath)
				{
					if(imgIdx<=0)
						idxAdder *= -1;
					else if(imgIdx>=LENGTH_NORM-1)
						idxAdder *= -1;

					imgIdx += idxAdder;
				}
				else
				{
					imgIdx++;

					if(imgIdx >= LENGTH_ATT - 1)
					{
						animTm.cancel();
						isEnd = true;
					}
				}
			}
		};

		animTm.schedule(animTmTask, 0, 80);
	}
	
	public void death()
	{
		imgIdx = 0;
	}
}
