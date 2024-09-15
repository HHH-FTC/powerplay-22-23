package org.firstinspires.ftc.team8109_Rise.Hardware.Intakes;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class ServoClawColorSensor {
    public Servo clawServo;

    public NormalizedColorSensor colorSensor;
    boolean redOr = true; // Red or blue cone, true is red, false is blue
    NormalizedRGBA colors = colorSensor.getNormalizedColors();

    public double openPosition;
    public double closedPosition;

    public ServoClawColorSensor(String sensorName, String servoName, double openPosition, double closedPosition, HardwareMap hardwareMap){
     } // End of constructor

    public enum ClawState {
        OPEN,
        CLOSED
    }

    public void colorSwitch() { // Method to be called to switch between red cones and blue cones
        redOr = !redOr;
    }

    public ServoClawColorSensor.ClawState clawState;

    public double getPositionDegrees() { // Converts position to degrees
        return clawServo.getPosition()*300;
    }

    public void setAngle(double angle){ // Sets angle
        clawServo.setPosition(angle/(300));
    }

    public void setPosition(){ // Switches the claw from open to close, and close to open
        switch (clawState){
            case OPEN:
                setAngle(openPosition);
                break;

            case CLOSED:
                setAngle(closedPosition);
                break;
        }
    }

    public boolean checkColor() {// Checks to see if the color in front of the sensor is red or blue, thus closing claw if cone is there
        if (redOr && colors.red - colors.green > Math.abs(colors.green - colors.blue)
                && colors.red - colors.blue > Math.abs(colors.green - colors.blue)) {
            setAngle(openPosition);
            return true;
        }

        if (!redOr && colors.blue - colors.green > Math.abs(colors.green - colors.red)
                && colors.blue - colors.red > Math.abs(colors.green - colors.red)) {
            setAngle(openPosition);
            return true;
        }

        return false; // Return for testing
    }
}
