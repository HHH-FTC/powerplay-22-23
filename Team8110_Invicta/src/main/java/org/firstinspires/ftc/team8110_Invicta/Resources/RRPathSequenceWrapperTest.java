package org.firstinspires.ftc.team8110_Invicta.Resources;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RRPathSequenceWrapperTest")
public class RRPathSequenceWrapperTest extends RobotCommon {

    /**
     * Override this method and place your code here.
     * <p>
     * Please do not swallow the InterruptedException, as it is used in cases
     * where the op mode needs to be terminated early.
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        pathing = new RoadrunnerWrapper(hardwareMap, RoadrunnerUnit.CM);
        pathing.sequenceWrapper = new SequenceWrapper(new WrapperBuilder(pathing)
                .lineToLinearHeading(50, 20, 90)
                .strafeLeft(10)
                .forward(10)
                .lineToSplineHeading(50, 20, 0)
        );
        initialize();

        run();
    }
}
