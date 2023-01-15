package org.firstinspires.ftc.team8110_Invicta.OpModes.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.addLine("Test Hi");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()){
            telemetry.addLine("Test bye");
            telemetry.update();
        }
    }
}
