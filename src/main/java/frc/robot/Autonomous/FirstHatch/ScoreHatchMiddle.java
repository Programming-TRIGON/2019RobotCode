/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Autonomous.FirstHatch;

import com.spikes2212.genericsubsystems.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.genericsubsystems.drivetrains.commands.DriveTankWithPID;
import com.spikes2212.genericsubsystems.drivetrains.commands.OrientWithPID;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.RobotComponents;
import frc.robot.RobotConstants;
import frc.robot.CommandGroups.EjectHatch;
import frc.robot.Vision.VisionPIDSource;
import frc.robot.Vision.VisionPIDSource.VisionDirectionType;
import frc.robot.Vision.VisionPIDSource.VisionTarget;

/**
 * Add your docs here.
 */
public class ScoreHatchMiddle extends CommandGroup {
  // TODO sort out theangle when turning to the feeder
  // TODO switch first distance something is wrong 
  final double FIRST_DISTANCE = 4.25 - RobotConstants.robotLength;
  final double TURN_45 = 45;
  final double SECOND_DISTANCE = 1.6907;
  final double TARGET_TRACK_TIME = 5;
  final double TURN_TO_FEEDER = -45;
  final double DRIVE_TO_FEEDER = 6.48;
  
  public ScoreHatchMiddle(boolean isLeft) {
    // drive a bit towards the cargo ship 
    addSequential(new DriveTankWithPID(Robot.driveTrain, RobotComponents.DriveTrain.LEFT_ENCODER,
    RobotComponents.DriveTrain.RIGHT_ENCODER, () -> FIRST_DISTANCE, () -> FIRST_DISTANCE, 
    RobotConstants.RobotPIDSettings.DRIVE_SETTINGS));  

    // turn a bit
    addSequential(new OrientWithPID(Robot.driveTrain, RobotComponents.DriveTrain.GYRO, 
    TURN_45 * (isLeft ? 1:-1), 
    RobotConstants.RobotPIDSettings.TURN_SETTINGS, 360, true));

    // diagonal drive in the cargo ship's general direction
    addSequential(new DriveTankWithPID(Robot.driveTrain, RobotComponents.DriveTrain.LEFT_ENCODER,
    RobotComponents.DriveTrain.RIGHT_ENCODER, () -> SECOND_DISTANCE, () -> SECOND_DISTANCE, 
    RobotConstants.RobotPIDSettings.DRIVE_SETTINGS));  

    // face the cargo ship
    addSequential(new OrientWithPID(Robot.driveTrain, RobotComponents.DriveTrain.GYRO, 
    -TURN_45 * (isLeft ? 1:-1), 
    RobotConstants.RobotPIDSettings.TURN_SETTINGS, 360, true));

    // Use vision to deliver the hatch
    addSequential(new DriveArcadeWithPID(Robot.driveTrain, 
    new VisionPIDSource(VisionTarget.kReflector, VisionDirectionType.x), 0, Robot.oi.getYLeft(), 
    RobotConstants.RobotPIDSettings.REFLECTOR_TRACK_SETTINGS, 2000, false), TARGET_TRACK_TIME);

    addSequential(new EjectHatch());

    // Turn to face the feeder
    addSequential(new OrientWithPID(Robot.driveTrain, RobotComponents.DriveTrain.GYRO, 
    TURN_TO_FEEDER * (isLeft ? 1:-1), 
    RobotConstants.RobotPIDSettings.TURN_SETTINGS, 360, true));

    // drive to feeder 
    addSequential(new DriveTankWithPID(Robot.driveTrain, RobotComponents.DriveTrain.LEFT_ENCODER,
    RobotComponents.DriveTrain.RIGHT_ENCODER, () -> DRIVE_TO_FEEDER, 
    RobotConstants.RobotPIDSettings.DRIVE_SETTINGS));  

    // Turn to face the feeder
    addSequential(new OrientWithPID(Robot.driveTrain, RobotComponents.DriveTrain.GYRO, 
    TURN_TO_FEEDER * (isLeft ? 1:-1), 
    RobotConstants.RobotPIDSettings.TURN_SETTINGS, 360, true));
  }
}