package hu.bme.mit.train.controller;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import hu.bme.mit.train.interfaces.TrainController;

public class TrainControllerImpl implements TrainController {

	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 0;
	private Timer timer;
	private Random random;

	public TrainControllerImpl() {
    Thread thread = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                followSpeed();
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    });
    thread.start();
}


	@Override
	public void followSpeed() {
		if (referenceSpeed < 0) {
			referenceSpeed = 0;
		} else {
			if (referenceSpeed + step > 0) {
				referenceSpeed += step;
			} else {
				referenceSpeed = 0;
			}
		}

		enforceSpeedLimit();
	}

	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();

	}

	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
	}

	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;
	}

	public boolean isTraingMoving(){
		return referenceSpeed > 0 ? true : false; 
	}

	public void emergencyBrake() {
		this.timer = new Timer();
		this.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
				if (referenceSpeed > 0) {
					int decreaseValue = random.nextInt(10);
					
					if (referenceSpeed - decreaseValue < 0) {
						referenceSpeed = 0;
					} else {
						referenceSpeed -= decreaseValue;
					}
					
					System.out.println("Current speed: " + referenceSpeed);
				} else {
					timer.cancel();
				}
			}
		}, 1000);
	}

}
