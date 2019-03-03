package frc.robot.LiftCommands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.RobotStates;
import frc.robot.RobotConstants.LiftHeight;

public class SetLiftHeight extends CommandGroup {
  /**
   * Set lift height with overraid option
   */
  public SetLiftHeight(LiftHeight height) {
    //set height index acorrding to height 
    switch (height) {
    case kLiftBottomHatch:
      RobotStates.setHeightIndex(0);
      break;
    case kRocketBottomCargo:
      RobotStates.setHeightIndex(0);
      break;
    case kRocketMiddleCargo:
      RobotStates.setHeightIndex(1);
      break;
    case kRocketMiddleHatch:
      RobotStates.setHeightIndex(1);
    case kRocketTopCargo:
      RobotStates.setHeightIndex(2);
      break;
    case kRocketTopHatch:
      RobotStates.setHeightIndex(2);
      break;
    case kCargoShip:
      RobotStates.setHeightIndex(3);
      break;
    default: 
      RobotStates.setHeightIndex(-1);
      break;
    }
    addSequential(new ReachLiftHeight(height)); // this command will end when overraide lift state is true else it will do pid on the height given
    addSequential(new SetLiftOverride()); // this command will run when overraide lift state is true
  }
}