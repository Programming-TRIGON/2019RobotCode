package frc.robot.SubSystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A SS that collects the cargo and holds it by turning wheels on top of the
 * cargo and bringing it in.
 */
public class CargoCollector extends Subsystem {
  /** The motor that turns the wheel for bringing in the cargo. */
  private TalonSRX collectorMotor;

  /**
   * After the cargo collector collects the cargo it goes into the cargo holder.
   * these motors put the cargo in the holder. after lifted up these motors shoot
   * the cargo out of them.
   */

  private VictorSPX leftHolder, rightHolder;

  //TODO: change this to a light sensor and change isHoldingBall respectively. 
  private AnalogInput sensor;

  public CargoCollector(TalonSRX collectorMotor, VictorSPX rightCargoHolder,
      VictorSPX leftCargoHolder,AnalogInput AI) {
    this.collectorMotor = collectorMotor;
    this.leftHolder = leftCargoHolder;
    this.rightHolder = rightCargoHolder;  
    this.sensor =AI;
  }

  /**
   * This function sets the speed at which the cargo collector motor will turn the
   * wheels to bring the cargo into the robot.
   */
  public void setCargoCollectorMotor(Double speed) {
    this.collectorMotor.set(ControlMode.PercentOutput, speed);

  }

  /**
   * This function sets the speed at which the cargo holder motor will turn to
   * bring the cargo into the holder.
   */
  public void setCargoHolderMotors(Double speed) {
    this.leftHolder.set(ControlMode.PercentOutput, speed);
    this.rightHolder.set(ControlMode.PercentOutput, speed);
  }
  /**
   * This function returns true if the ball is being held
   */
  public boolean isHoldingBall(){
    return false;

  }

  @Override
  public void initDefaultCommand() {

  }
}