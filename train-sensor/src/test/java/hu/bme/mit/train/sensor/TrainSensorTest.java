package hu.bme.mit.train.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Table;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainUser;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

public class TrainSensorTest {
    private TrainController controller;
    private TrainUser user;

    TrainSensorImpl trainSensorImpl;
    LocalDateTime firstTimestamp, secondTimestamp;

    @Before
    public  void setUp() {
        controller = mock(TrainController.class);
        user = mock(TrainUser.class);

        trainSensorImpl = new TrainSensorImpl(controller, user);
        trainSensorImpl.initTachoGraph();

        firstTimestamp = LocalDateTime.now();
        trainSensorImpl.appendRowToTachograph(firstTimestamp, 0,10);

        secondTimestamp = LocalDateTime.now();
        trainSensorImpl.appendRowToTachograph(secondTimestamp, 1,20);
           
        for (Table.Cell<LocalDateTime, Integer, Integer> cell : trainSensorImpl.getTachoGraph().cellSet()) {
           System.out.println(cell.getRowKey() + " " + cell.getColumnKey() + " " + cell.getValue());
        }
    }

    @Test
    public void tachographTableValue() {    
        Assert.assertEquals(Integer.valueOf(10), trainSensorImpl.getTachoGraph().get(firstTimestamp, 0));
    }

    @Test
    public void tachographTableLengthTest() {
        Assert.assertEquals(2, trainSensorImpl.getTachoGraph().size());
    }    
}