package frc.robot.Commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotStates;

/**
 * This command lets the operator control the one eighty with a joystick incase
 * the sensors break
 */
public class SetOneEightyOverride extends Command {
  public SetOneEightyOverride() {
    requires(Robot.oneEighty);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    Robot.oneEighty.moveOneEightyOverride(Robot.oi.operatorXbox.getY(Hand.kLeft));
  }

  @Override
  protected boolean isFinished() {
    return !RobotStates.isOneEightyOverride();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.oneEighty.moveOneEightyOverride(0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}