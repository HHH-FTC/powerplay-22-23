package org.firstinspires.ftc.team8109_Rise.OpModes.TeleOp_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team8109_Rise.Control.TeleOp.BotToPoint_Control;

@TeleOp
public class BotToPoint extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        BotToPoint_Control control = new BotToPoint_Control("frontLeft", "frontRight", "backRight", "backLeft", hardwareMap, gamepad1, telemetry);
        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            control.Drive();
            control.Telemetry();
        }
    }
}
