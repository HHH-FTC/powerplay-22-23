package org.firstinspires.ftc.team8109_Rise.Robots.SlidesBot.Sensors.Odometry;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.team8109_Rise.Resources.RoadRunnerQuickstart.util.Encoder;
import org.firstinspires.ftc.team8109_Rise.Sensors.Odometry.StandardThreeWheelOdoLocalizer;

public class OdometryLocalizer extends StandardThreeWheelOdoLocalizer {

    static String[] configNames = {"fLeft", "fRight", "middleEncoder"};

    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_RADIUS = 0.688975; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double LATERAL_DISTANCE = 10.915; // in; distance between the left and right wheels
    public static double FORWARD_OFFSET = 4.5; // in; offset of the lateral wheel

    static double X_MULTIPLIER = 0.9896; // Multiplier in the X direction
    static double Y_MULTIPLIER = 0.94; // Multiplier in the Y direction  71

    static double[] DriveConstants = {TICKS_PER_REV, WHEEL_RADIUS, GEAR_RATIO, LATERAL_DISTANCE, FORWARD_OFFSET};

    public OdometryLocalizer(HardwareMap hardwareMap) {
        super(DriveConstants, configNames, X_MULTIPLIER, Y_MULTIPLIER, hardwareMap);
        // TODO: test if any encoders need to be reversed
        rightEncoder.setDirection(Encoder.Direction.REVERSE);
    }
}
