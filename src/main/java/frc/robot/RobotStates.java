package frc.robot;

/**
 * listeninng to the statse of the robot
 */
public class RobotStates {
    static boolean hasCargo = false;
    static boolean driveInverted = false;
    static boolean oneEightyOverride = false;
    static boolean LiftOverride = false;
    static int heightIndex = -1; // The values can be 0,1,2 for rocket - low, middle and high. 3 for cargo ship height And -1 for none of these heights
    static double liftHeight = 0;

    public static void increaseHeight() {
        if (RobotStates.heightIndex < 2)
            RobotStates.heightIndex++;
    }

    public static void decreaseHeight() {
        if (RobotStates.heightIndex > 0)
            RobotStates.heightIndex--;
    }

    public static int getHeightIndex() {
        return RobotStates.heightIndex;
    }

    public static void setHeightIndex(int index) {
        RobotStates.heightIndex = index;
    }

    public static boolean isHasCargo() {
        return hasCargo;
    }

    public static void setHasCargo(boolean hasCargo) {
        RobotStates.hasCargo = hasCargo;
    }

    public static boolean isDriveInverted() {
        return driveInverted;
    }

    public static void setDriveInverted(boolean driveInverted) {
        RobotStates.driveInverted = driveInverted;
    }

    public static void toggleDriveInverted() {
        RobotStates.driveInverted = !RobotStates.driveInverted;
    }

    public static boolean isOneEightyOverride() {
        return RobotStates.oneEightyOverride;
    }

    public static void toggleOneEightyOverride() {
        RobotStates.oneEightyOverride = !RobotStates.oneEightyOverride;
    }

    public static boolean isLiftOverride() {
        return RobotStates.LiftOverride;
    }

    public static void toggleLiftOverride() {
        RobotStates.LiftOverride = !RobotStates.LiftOverride;
    }
    
    public static double getLiftHeight() {
        return RobotStates.liftHeight;
    }

    public static void setLiftHeight(double liftHeight) {
        RobotStates.liftHeight = liftHeight;
    }
}
