package org.firstinspires.ftc.team8110_Invicta.Hardware;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.team8110_Invicta.Hardware.States.DriveSpeeds.SLOW;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team8110_Invicta.Hardware.Drivetrains.StraferChassis;
import org.firstinspires.ftc.team8110_Invicta.Hardware.Mechanisms.Claw;
import org.firstinspires.ftc.team8110_Invicta.Hardware.Mechanisms.Lift;
import org.firstinspires.ftc.team8110_Invicta.Hardware.Mechanisms.Motor;
import org.firstinspires.ftc.team8110_Invicta.Hardware.Mechanisms.Webcam;
import org.firstinspires.ftc.team8110_Invicta.Hardware.States.DriveSpeeds;
import org.openftc.easyopencv.OpenCvCamera;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElefanteMcNuggets extends StraferChassis {
    HardwareMap hwmap;
    ScissorLift lift;

    ConeClaw claw;

    OpenCvCamera camera;

    DriveSpeeds speed = SLOW;
    double diff = 0.5;

    public ElefanteMcNuggets(HardwareMap hardwareMap) {
        super(hardwareMap);
        this.hwmap = hardwareMap;
        this.lift = new ScissorLift("lift");
        this.claw = new ConeClaw("claw");
        this.camera = new Webcam(hardwareMap).getCamera();

        this.backRight.setDirectionReverse();
        this.frontRight.setDirectionReverse();


    }

    public class ScissorLift implements Lift {
        private Motor liftMotor;
        private double initial;

        public ScissorLift(String name) {
            liftMotor = new Motor(name, hwmap);
            liftMotor.reset();
            initial = liftMotor.getPosition();
        }

        @Override
        public void reverse() {
            switch (liftMotor.getDirection()) {
                case REVERSE:
                    liftMotor.setDirectionForward();
                    break;
                case FORWARD:
                    liftMotor.setDirectionReverse();
                    break;
            }
        }

        @Override
        public void power(double power) {
            liftMotor.setPower(power);
        }

        public void run(double position) {
//            telemetry.addData("started running to position", 0);
//            telemetry.update();
            liftMotor.runToPosition(position);
//            telemetry.addData("ran to position", 0);
//            telemetry.update();
        }

        @Override
        public double getPosition() {
            return liftMotor.getCurrentPosition();
        }

        @Override
        public double getInches() {
            return liftMotor.getCurrPosInches();
        }

        @Override
        public void goToHigh() {
            liftMotor.runToPosition(3000, 1);
            liftMotor.reset();
        }

        public void goToGround() {
            liftMotor.runToPosition(3000, -1);
            liftMotor.reset();
        }

        @Override
        public void downLevel() {
            liftMotor.runToPosition((int) (getPosition() - 160));
        }
    }

    public class ConeClaw implements Claw {
        private Servo servo;

        public ConeClaw(String clawName) {
            servo = hwmap.get(Servo.class, clawName);
        }

        @Override
        public void open() {
            servo.setPosition(.8);
        }

        @Override
        public void close() {
            servo.setPosition(.875);
        }

        @Override
        public double getPosition() {
            return servo.getPosition();
        }
    }

    public ScissorLift getLift() {
        return lift;
    }

    public ConeClaw getClaw() {
        return claw;
    }

    public DriveSpeeds getSpeed() {
        return speed;
    }

    /*public void teleOpDrive(Gamepad gamepad) {
        switch (speed) {
            case NORMAL:
                if (gamepad.x) {
                    diff = 3.0;
                    speed = SLOW;
                }
                break;
            case SLOW:
                if (gamepad.x) {
                    diff = 2.0;
                    speed = NORMAL;
                }
                break;
        }

        this.setDrivePower(new Pose2d(
            gamepad.left_stick_y/diff,
            gamepad.right_stick_x/diff,
            gamepad.left_stick_x/diff
        ));
    }
*/
    public void teleOpDrive(Gamepad gamepad) {
        double drive = Math.pow(gamepad.left_stick_y, 3);
        double strafe = Math.pow(gamepad.left_stick_x, 3);
        double turn = Math.pow(gamepad.right_stick_x, 3);
        setDrivePower(gamepad.left_stick_y, gamepad.left_stick_x, gamepad.right_stick_x);
    }

    public void setDrivePower(double drive, double strafe, double turn) {

        double fLeft = 0.875 * drive - 1 * strafe - 0.8 * turn;
        double fRight = 0.875 * drive + 1 * strafe + 0.8 * turn;
        double bRight = -0.875 * drive - 1 * strafe + 0.8 * turn;
        double bLeft = -0.875 * drive + 1 * strafe - 0.8 * turn;

//        switch (speed) {
//            case FASTER:
//                if (gamepad.x) {
//                    diff = 0.5;
//                    speed = SLOW;
//                }
//                break;
//            case SLOW:
//                if (gamepad.x) {
//                    diff = 1.5;
//                    speed = DriveSpeeds.FASTER;
//                }
//                break;
//        }

        this.setPower(fLeft, fRight, bRight, -bLeft);
    }

    @Override
    public void setDrivePower(Pose2d pose) {
        this.setDrivePower(pose.getX(), pose.getY(), pose.getHeading());
    }

    public void teleOpClaw(Gamepad gamepad) {
        if (gamepad.a) {
            claw.open();
        } else if (gamepad.y) {
            claw.close();
        }
    }

    public void teleOpLift(Gamepad gamepad) {
        double down = (gamepad.left_bumper || gamepad.dpad_down) ? 0.75 : 0;
        double up = (gamepad.right_bumper || gamepad.dpad_up) ? 0.75 : 0;

        lift.power(up-down);
    }

    public void runToPosition(int position, double power) {
        List<Motor> wheels = this.getWheels();

        for (Motor motor : wheels) {
            motor.runToPosition(position,power);
        }
    }

    public void runToPosition(int position) {
        this.runToPosition(position*538,1);
    }

    public void strafeRight(int position){
        this.frontLeft.runToPosition(position*538,1);
        this.backLeft.runToPosition(position*5378,1);
        this.frontRight.runToPosition(position*538,1);
        this.backRight.runToPosition(position*538,1);
    }

    public void strafeLeft(int position){
        this.frontLeft.runToPosition(position*538,-1);
        this.backLeft.runToPosition(position*538,1);
        this.frontRight.runToPosition(position*538,1);
        this.backRight.runToPosition(position*538,1);
   }

    public void rotate180(int position){
        this.frontLeft.runToPosition(position*538,1);
        this.frontRight.runToPosition(position*538,-1);
        this.backRight.runToPosition(position*538,0);
        this.backLeft.runToPosition(position*538,0);
    }

    public void runForward(int pos){
        this.frontLeft.runToPosition(pos*538,1);
        this.frontRight.runToPosition(pos*538,1);
        this.backRight.runToPosition(pos*538,1);
        this.backLeft.runToPosition(pos*538,1);
    }

}
