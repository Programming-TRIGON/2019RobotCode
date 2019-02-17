/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Autonomous.SecondHatch;

import com.spikes2212.genericsubsystems.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.genericsubsystems.drivetrains.commands.OrientWithPID;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotComponents;
import frc.robot.RobotConstants;

public class SecondHatchSide extends CommandGroup {

  enum CargoShipHatch {
    kFirstHatch(6.9 - RobotConstants.robotLength), kSecondHatch(7.45 - RobotConstants.robotLength),
    kThirdHatch(8 - RobotConstants.robotLength);

    public double key;

    CargoShipHatch(double distance) {
      this.key = distance;
    }
  }

  /**
   * Add your docs here.
   */
  public SecondHatchSide(CargoShipHatch driveDistance, boolean isLeft) {
    final double start_feederDistance = 2.32;
    final double angle = Math.atan(start_feederDistance / driveDistance.key);
    final double TURN_TO_HATCH = 90 + angle;
    final double TURN_TO_ROCKET = 180 - angle;
    final double distanceToRocket = Math.sqrt(Math.pow(start_feederDistance, 2) + Math.pow(driveDistance.key, 2));
    final double TARGET_TRACK_TIME = 5;

    addSequential(new OrientWithPID(Robot.driveTrain, RobotComponents.DriveTrain.GYRO, TURN_TO_ROCKET  * (isLeft ? 1:-1) ,
        RobotConstants.RobotPIDSettings.TURN_SETTINGS, 360, true));

    addSequential(new DriveTankWithPID(Robot.driveTrain, RobotComponents.DriveTrain.LEFT_ENCODER,
        RobotComponents.DriveTrain.RIGHT_ENCODER, distanceToRocket, RobotConstants.RobotPIDSettings.DRIVE_SETTINGS));

    addSequential(new OrientWithPID(Robot.driveTrain, RobotComponents.DriveTrain.GYRO, TURN_TO_HATCH  * (isLeft ? 1:-1), RobotConstants.RobotPIDSettings.TURN_SETTINGS, 360, true));

  }

}
