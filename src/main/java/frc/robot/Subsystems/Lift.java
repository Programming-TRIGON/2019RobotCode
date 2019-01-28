package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Lift extends Subsystem {
  /** the two motors that turn to higher the lift */
  private TalonSRX leftMotor, rightMotor;
  /** these limit switches make sure the lift doesn't pass the required highet */
  private DigitalInput topSwitch, bottomSwitch;

  private AnalogPotentiometer potentiometer;

  public Lift(TalonSRX rightMotor, TalonSRX leftMotor, DigitalInput topwSwitch, DigitalInput bottomSwitch,
      AnalogPotentiometer potentiometer) {
    this.rightMotor = rightMotor;
    this.leftMotor = leftMotor;
    this.topSwitch = topwSwitch;
    this.bottomSwitch = bottomSwitch;
    this.potentiometer = potentiometer;
  }

  /** sets the speed of the motors of the lift to higher/lower it */
  public void setMotorSpeed(Double speed) {
    leftMotor.set(ControlMode.PercentOutput, speed);
    rightMotor.set(ControlMode.PercentOutput, speed);
  }

  /** This function checks whether the lift has activated the top micro switch. */
  public boolean getTopSwitch() {
    return topSwitch.get();
  }

  /**
   * This function checks whether the lift has activated the botton micro switch.
   */
  public boolean getBottomSwitch() {
    return bottomSwitch.get();
  }

  /** This function returns the curent state of the potentiometer. */
  public double getPotentiometer() {
    return this.potentiometer.get();
  }

  @Override
  public void initDefaultCommand() {

  }
}