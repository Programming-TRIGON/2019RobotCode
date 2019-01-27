package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SubSystems.CargoCollector;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SubSystems.CargoFolder;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  public static CargoCollector cargoCollector;
  public static CargoFolder cargoFolder;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    /**
     * creates the new SS that collects corgo by turning wheels that bring it in
     */
    this.cargoCollector = new CargoCollector(new TalonSRX(RobotMap.CARGO_COLLECTOR_MOTOR),
        new VictorSPX(RobotMap.CARGO_COLLECTOR_HOLDER_RIGHT_MOTOR),
        new VictorSPX(RobotMap.CARGO_COLLECTOR_HOLDER_LEFT_MOTOR),new AnalogInput(0));
     * creates the SS corgo fold that extends and retracts the whole SS of the cargo
     * collector with it
     */
    this.cargoFolder = new CargoFolder(
        new DoubleSolenoid(RobotMap.CARGO_FOLDER_SOLENOID_A, RobotMap.CARGO_FOLDER_SOLENOID_B),
        new DigitalInput(RobotMap.CARGO_FOLDER_TOP_SWITCH), new DigitalInput(RobotMap.CARGO_FOLDER_BOTTOM_SWITCH));
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kCustomAuto:
      break;
    case kDefaultAuto:
    default:
      break;
    }
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testPeriodic() {
  }
}