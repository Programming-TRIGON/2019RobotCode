package frc.robot.OICommands;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.RobotConstants;
import frc.robot.RobotStates;
import frc.robot.CargoFolderCommands.SetCargoFolderState;
import frc.robot.LiftCommands.SetHeightIndex;
import frc.robot.OneEightyCommands.SetOneEightyDesireAngle;
import frc.robot.RobotConstants.LiftHeight;
import frc.robot.RobotConstants.OneEightyAngle;
import frc.robot.RobotConstants.PrepareToScoreHeight;

public class PrepareToScore extends CommandGroup {
  /**
   * preapre the lift and 180 to score in different heights
   */
  PrepareToScoreHeight height;
  boolean increaseHeight;
  LiftHeight heightToSet = LiftHeight.kOneEightySafety;
  OneEightyAngle angleToSet = OneEightyAngle.kStraight;
  PrepareToScoreHeight prepareToScoreHeight;

  public PrepareToScore(PrepareToScoreHeight height) {
    this.height = height;
    addSequential(new SetCargoFolderState(Value.kReverse, ()-> heightToSet.equals(LiftHeight.kLiftBottomHatch)));
    addSequential(new SetHeightIndex(()-> heightToSet));
    addSequential(new WaitCommand(0.3));   
    addSequential(new SetOneEightyDesireAngle(()-> angleToSet));
  }

  public PrepareToScore(boolean increaseHeight) {
    this.increaseHeight = increaseHeight;
    addSequential(new SetCargoFolderState(Value.kReverse, ()-> heightToSet.equals(LiftHeight.kLiftBottomHatch)));
    addSequential(new SetHeightIndex(()-> heightToSet));
    addSequential(new WaitCommand(0.3));
    addSequential(new SetOneEightyDesireAngle(()-> angleToSet));
  }

  @Override
  protected void initialize(){
    if(RobotStates.getHeightIndex()==-1)
      RobotStates.setHeightIndex(1);

    if(this.height==null){
      if(this.increaseHeight)
        RobotStates.increaseHeight();
      else 
        RobotStates.decreaseHeight();
      prepareToScoreHeight = RobotConstants.heights[RobotStates.getHeightIndex()];
    }else 
      prepareToScoreHeight = this.height;
      
    // Choose which height should be set based on what the operator input and what game piece we have
    switch (prepareToScoreHeight){
      case kLow:
        if (RobotStates.isHasCargo())
          heightToSet = LiftHeight.kRocketBottomCargo;
        else 
          if(angleToSet.equals(OneEightyAngle.kStraight))   
            heightToSet = LiftHeight.kLiftBottomHatch;
          else
            heightToSet = LiftHeight.kLiftBottomHatchCargoSide;
        break;
      case kMedium:
        if (RobotStates.isHasCargo())
          heightToSet = LiftHeight.kRocketMiddleCargo;
        else 
          heightToSet = LiftHeight.kRocketMiddleHatch;
        break;
      case kHigh:
        if (RobotStates.isHasCargo()){
          heightToSet = LiftHeight.kRocketTopCargo;
          // The only time the 180 isn't straight is when it has to be angled up
          angleToSet = RobotStates.isDriveInverted() ? OneEightyAngle.kTopBack : OneEightyAngle.kTopStraight;
        }
        else {
          heightToSet = LiftHeight.kRocketTopHatch;
          angleToSet = OneEightyAngle.kStraight;
        }
        break;
      case kCargoShip:
        if (RobotStates.isHasCargo()){
          heightToSet = LiftHeight.kCargoShip;
          angleToSet = RobotStates.isDriveInverted() ? OneEightyAngle.kCargoShipBack : OneEightyAngle.kCargoShipForward;
        }else 
          heightToSet = LiftHeight.kLiftBottomHatch;
        break;
    } 
  }
}
