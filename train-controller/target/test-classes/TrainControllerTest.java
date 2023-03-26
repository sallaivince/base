package hu.bme.mit.train.controller;

public class TrainControllerTest {
    TrainControllerImpl trainControllerImpl;

    @Before
    public void before() {
        trainControllerImpl = new TrainControllerImpl();
    }

    /**
     * The train should stop after the emergency brake is applied
     * The test will wait for the train to stop for a maximum of 10 seconds
     * The test will poll the train's state every 1 second
     */
    @Test
    public void testTrainStopped() {
        trainControllerImpl.emergencyBrake();
        Wait
                .forCondition(() -> !trainControllerImpl.isTraingMoving())
                .withTimeout(10, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS);

        assertFalse(trainControllerImpl.isTraingMoving());
    }
}
