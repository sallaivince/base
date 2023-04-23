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

    /**
     * In this partion we are testing the lower boundary.
     * The first speedLimit is -1, which is 1 unit lower than the boundary.
     * The second is 0, which is exactly the boundary value.
     * The third is 1, which is 1 unit higher than the boundary.
     */
    @Test 
    public void alertBottomBoundarySuccess(){
        trainSensorImpl.overrideSpeedLimit(-1);
        verify(user).setAlarmState(true);
        trainSensorImpl.overrideSpeedLimit(0);
        verify(user).setAlarmState(false);
        trainSensorImpl.overrideSpeedLimit(1);
        verify(user, times(2)).setAlarmState(false);
    }

    /**
     * In this partion we are testing the upper boundary.
     * The first speedLimit is 499, which is 1 unit lower than the boundary.
     * The second is 500, which is exactly the boundary value.
     * The third is 501, which is 1 unit higher than the boundary.
     */
    @Test 
    public void alertTopBoundarySuccess(){
        trainSensorImpl.overrideSpeedLimit(501);
        verify(user).setAlarmState(true);
        trainSensorImpl.overrideSpeedLimit(500);
        verify(user).setAlarmState(false);
        trainSensorImpl.overrideSpeedLimit(499);
       verify(user, times(2)).setAlarmState(false);
    }

    /**
     * In this partion we are testing with a middle value.
     * The speedLimit is 250, which is between the boundaries.
     */
    @Test 
    public void alertMiddleBoundarySuccess(){
        trainSensorImpl.overrideSpeedLimit(250);
        verify(user).setAlarmState(false);
    }

   
    /**
     * In this partion we are testing the relative boundary.
     * The referenceSpeed is set to 450, the speedLimit is set to 10.
     */
    @Test 
    public void alertRelativeBoundarySuccess(){
        when(controller.getReferenceSpeed()).thenReturn(450);
        trainSensorImpl.overrideSpeedLimit(10);
        verify(user).setAlarmState(true);
    }

}