package org.firstinspires.ftc.team7407_Vega.OpModes.TeleOp_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.team7407_Vega.Control.TeleOp.StraferChassisTeleOp_Control;

@TeleOp
public class StraferChassis_TeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        StraferChassisTeleOp_Control control = new StraferChassisTeleOp_Control("frontLeft", "frontRight", "backRight", "backLeft", hardwareMap, gamepad1, telemetry);
        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            control.Drive();
        }
    }
}
