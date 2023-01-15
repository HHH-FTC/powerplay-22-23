package org.firstinspires.ftc.team8109_Rise.Robots.SlidesBot.Mechanisms;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team8109_Rise.Hardware.Drivetrains.MecanumDriveTrain;
import org.firstinspires.ftc.team8109_Rise.Robots.SlidesBot.Sensors.Odometry.OdometryLocalizer;
import org.firstinspires.ftc.team8109_Rise.Sensors.InertialMeasurementUnit;
import org.firstinspires.ftc.team8109_Rise.Math.Vectors.Vector3D;
import org.firstinspires.ftc.team8109_Rise.Robots.SlidesBot.Sensors.SlidesBot_DriveConstants;

@Config
public class Chassis extends MecanumDriveTrain {

    Gamepad gamepad1;
    Telemetry telemetry;

    ElapsedTime runtime = new ElapsedTime();

//    public org.firstinspires.ftc.team8109_Rise.Sensors.InertialMeasurementUnit imu;
    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(8, 0, 0);
    public static PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);

    public static double LATERAL_MULTIPLIER = 1.101399867722112;

    public static double VX_WEIGHT = 1;
    public static double VY_WEIGHT = 1;
    public static double ω_WEIGHT = 1;

    double fLeft;
    double fRight;
    double bLeft;
    double bRight;

    double drive;
    double strafe;
    double turn;

    double max;

    double x_rotated;
    double y_rotated;

    private static final TrajectoryVelocityConstraint VEL_CONSTRAINT = getVelocityConstraint(SlidesBot_DriveConstants.MAX_VEL, SlidesBot_DriveConstants.MAX_ANG_VEL, SlidesBot_DriveConstants.TRACK_WIDTH);
    private static final TrajectoryAccelerationConstraint ACCEL_CONSTRAINT = getAccelerationConstraint(SlidesBot_DriveConstants.MAX_ACCEL);

    Vector3D controllerInput = new Vector3D(0, 0, 0);

    public enum AutonState{
        READ_PARKING,
        TO_CONE_STACK,
        CYCLE,
        PARK
    }

    public OdometryLocalizer odometry;

    public Chassis(Gamepad gamepad1, Telemetry telemetry, HardwareMap hardwareMap){
        super("fLeft", "fRight", "bRight", "bLeft",
                SlidesBot_DriveConstants.kV, SlidesBot_DriveConstants.kA, SlidesBot_DriveConstants.kStatic,
                SlidesBot_DriveConstants.TRACK_WIDTH, SlidesBot_DriveConstants.WHEEL_BASE, LATERAL_MULTIPLIER,
                TRANSLATIONAL_PID, HEADING_PID, VX_WEIGHT, VY_WEIGHT, ω_WEIGHT,
                VEL_CONSTRAINT, ACCEL_CONSTRAINT, hardwareMap);

        reset();

        odometry = new OdometryLocalizer(hardwareMap);

        frontLeft.setDirectionReverse();
        backLeft.setDirectionReverse();

        setLocalizer(odometry);
//        imu = new InertialMeasurementUnit(hardwareMap);

        this.gamepad1 = gamepad1;
        this.telemetry = telemetry;
    }

    public void setDriveVectorsRobotCentric(Vector3D input){
        //TODO: Test cube weighting
        // Inverse Kinematics Calculations
        fLeft = VX_WEIGHT * input.A - VY_WEIGHT * input.B + ω_WEIGHT * input.C;
        fRight = VX_WEIGHT * input.A + VY_WEIGHT * input.B - ω_WEIGHT * input.C;
        bRight = VX_WEIGHT * input.A - VY_WEIGHT * input.B - ω_WEIGHT * input.C;
        bLeft = VX_WEIGHT * input.A + VY_WEIGHT * input.B + ω_WEIGHT * input.C;

        max = Math.max(Math.max(Math.abs(fLeft), Math.abs(fRight)), Math.max(Math.abs(bLeft), Math.abs(bRight)));
        if (max > 1.0) {
            fLeft /= max;
            fRight /= max;
            bLeft /= max;
            bRight /= max;
        }

        setPower(fLeft, fRight, bRight, bLeft);
    }

    // Unrefined field centric code
    public void setDriveVectorsFieldCentric(Vector3D input){
        x_rotated = input.A * Math.cos(getPoseEstimate().getHeading()) - input.B * Math.sin(getPoseEstimate().getHeading());
        y_rotated = input.A * Math.sin(getPoseEstimate().getHeading()) + input.B * Math.cos(getPoseEstimate().getHeading());

        fLeft = VX_WEIGHT * x_rotated + VY_WEIGHT * y_rotated + ω_WEIGHT * input.C;
        fRight = VX_WEIGHT * x_rotated - VY_WEIGHT * y_rotated - ω_WEIGHT * input.C;
        bRight = VX_WEIGHT * x_rotated + VY_WEIGHT * y_rotated - ω_WEIGHT * input.C;
        bLeft = VX_WEIGHT * x_rotated - VY_WEIGHT * y_rotated + ω_WEIGHT * input.C;

        max = Math.max(Math.max(Math.abs(fLeft), Math.abs(fRight)), Math.max(Math.abs(bLeft), Math.abs(bRight)));
        if (max > 1.0) {
            fLeft /= max;
            fRight /= max;
            bLeft /= max;
            bRight /= max;
        }

        setPower(fLeft, fRight, bRight, bLeft);
    }

    //TODO: Test out field-centric
    public void ManualDrive(){
        controllerInput.set(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        setDriveVectorsRobotCentric(controllerInput);
    }

    public void DPad_Drive(){
        if (gamepad1.dpad_up){
            drive = 0.25;
        } else if (gamepad1.dpad_down){
            drive = -0.25;
        } else drive = 0;

        if (gamepad1.dpad_left){
            strafe = 0.25;
        } else if (gamepad1.dpad_right){
            strafe = -0.25;
        } else {
            strafe = 0;
        }

//        controllerInput.set(drive, strafe, gamepad1.right_stick_x);
        controllerInput.set(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        setDriveVectorsFieldCentric(controllerInput);
    }
}
