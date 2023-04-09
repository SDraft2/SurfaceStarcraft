package com.leesb.surfacestarcraft;


import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends Unit{
	Boolean isFired = false;
	Boolean isWaitDelay = false;

	Timer delayTm;
	Timer moveTm;

	private int delay = 0;

	public Bullet(int width, int height, int speed, int delay)
		{
			super(-width, -height, width, height, speed);
			setSpeed(speed);

			this.delay = delay;

			delayTm = new Timer();
			moveTm = new Timer();

			moveTm.schedule(new MoveTmTask(), 0, 30);
	}
	
	public void fire()
	{
		if(!isWaitDelay)
		{
			isFired = true;
			isWaitDelay = true;
			delayTm.schedule(new DelayTmTask(), delay);
		}
			
	}

	class DelayTmTask extends TimerTask
	{
		@Override
		public void run() {
			isWaitDelay = false;
		}
	}

	class MoveTmTask extends TimerTask
	{
		@Override
		public void run() {
			if(isFired)
			{
				if(y < - getHeight())
					isFired = false;

				y = y - getSpeed();
			}
		}
	}
}
