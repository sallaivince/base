package hu.bme.mit.train.interfaces;

import java.time.LocalDateTime;

import com.google.common.collect.Table;

public interface TrainSensor {

	int getSpeedLimit();

	void overrideSpeedLimit(int speedLimit);

	void initTachoGraph();

	void appendRowToTachograph(LocalDateTime time, int joystickPosition, int referenceSpeed);

	Table<LocalDateTime, Integer, Integer> getTachoGraph();

}
