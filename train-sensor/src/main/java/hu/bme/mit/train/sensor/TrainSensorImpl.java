package hu.bme.mit.train.sensor;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;

import java.time.LocalDateTime;
import java.util.Random;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class TrainSensorImpl implements TrainSensor {

	private TrainController controller;
	private TrainUser user;
	private int speedLimit = 5;

	Table<LocalDateTime, Integer, Integer> tachoGraph;

	public TrainSensorImpl(TrainController controller, TrainUser user) {
		this.controller = controller;
		this.user = user;
	}

	@Override
	public int getSpeedLimit() {
		return speedLimit;
	}

	@Override
	public void overrideSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		controller.setSpeedLimit(speedLimit);
	}

	@Override
	public void initTachoGraph() {
		tachoGraph = HashBasedTable.create();
	}

	@Override
	public void appendRowToTachograph(LocalDateTime time, int joystickPosition, int referenceSpeed) {
		tachoGraph.put(time, joystickPosition, referenceSpeed);
	}

	public Table<LocalDateTime, Integer, Integer> getTachoGraph() {
		return tachoGraph;
	}

}
