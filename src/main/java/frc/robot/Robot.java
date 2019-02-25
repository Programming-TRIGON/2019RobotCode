package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.spikes2212.dashboard.DashBoardController;
import com.spikes2212.genericsubsystems.drivetrains.TankDrivetrain;
import com.spikes2212.genericsubsystems.drivetrains.commands.DriveArcade;
import com.spikes2212.genericsubsystems.drivetrains.commands.OrientWithPID;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotConstants.LiftHeight;
import frc.robot.RobotConstants.OneEightyAngle;
import frc.robot.Subsystems.Lift;
import frc.robot.Subsystems.OneEighty;
import frc.robot.Vision.VisionPIDSource;
import frc.robot.Autonomous.TestPID;
import frc.robot.Autonomous.testAuto;
import frc.robot.CommandGroups.EjectHatch;
import frc.robot.CommandGroups.SetLiftHeight;
import frc.robot.CommandGroups.SetOneEightyAngle;
import frc.robot.Commands.CollectCargo;
import frc.robot.Commands.DriveWithGyro;
import frc.robot.Commands.HigherI;
import frc.robot.Commands.MoveSubsystemWithJoystick;
import frc.robot.Commands.ReachOneEightyAngle;
import frc.robot.Commands.SetCargoFolderState;
import frc.robot.Commands.SetHatchCollectorState;
import frc.robot.Commands.SetHatchEject;
import frc.robot.Commands.SetHatchLock;
import frc.robot.Subsystems.CargoCollector;
import frc.robot.Subsystems.CargoFolder;
import frc.robot.Subsystems.HatchCollector;
import frc.robot.Subsystems.HatchHolder;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static HatchCollector hatchCollector;
  public static Lift lift;
  public static HatchHolder hatchHolder;
  public static OneEighty oneEighty;
  public static CargoCollector cargoCollector;
  public static CargoFolder cargoFolder;
  public static TankDrivetrain driveTrain;

  public static DashBoardController dbc;
  public static OI oi;

  final SendableChooser<Command> testsChooser = new SendableChooser<Command>();;
  public static Compressor compressor;

  @Override
  public void robotInit() {
    compressor = new Compressor(1);
    compressor.start();

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    SmartDashboard.putData("Test Commands", testsChooser);

    // Set deafult vision target to reflector
    NetworkTable imageProcessingTable = NetworkTableInstance.getDefault().getTable("ImageProcessing");
    NetworkTableEntry target = imageProcessingTable.getEntry("target");
    target.setString(VisionPIDSource.VisionTarget.kReflector.toString());

  

    Robot.dbc = new DashBoardController();

    /** creates the SS htach collector that collects hatch pannels */
    Robot.hatchCollector = new HatchCollector(RobotComponents.HatchCollector.SOLENOID);

    /** defining the subsystem lift that highers the cargo and hatch holders */
    Robot.lift = new Lift(RobotComponents.Lift.LIFT_RIGHT_M, RobotComponents.Lift.LIFT_LEFT_M,

        RobotComponents.Lift.TOP_SWITCH, RobotComponents.Lift.BOTTOM_SWITCH, RobotComponents.Lift.ENCODER);

    /**
     * creates the new susbsystem with three solenoids, two that extends the whole
     * SS outward one one that catches the hatch
     */
    Robot.hatchHolder = new HatchHolder(RobotComponents.HatchHolder.PVC, RobotComponents.HatchHolder.PUSH_SOLENOID);

    /*
     * creates the SS that turns the subsytems cargo and hatch holder 180 degrees
     */

    Robot.oneEighty = new OneEighty(RobotComponents.OneEighty.MOTOR, RobotComponents.OneEighty.POT);

    /*
     * creates the new SS that collects corgo by turning wheels that bring it in
     */
    Robot.cargoCollector = new CargoCollector(RobotComponents.CargoCollector.COLECTOR_MOTOR,
        RobotComponents.CargoCollector.RIGHT_HOLDER, RobotComponents.CargoCollector.LEFT_HOLDER,
        RobotComponents.CargoCollector.SWITCH);
    /*
     * creates the SS corgo fold that extends and retracts the whole SS of the cargo
     * collector with it
     */
    Robot.cargoFolder = new CargoFolder(RobotComponents.CargoFolder.SOLENOID);

    /*
     * creates the drive train SS with SpikesLib
     */
    RobotComponents.DriveTrain.FRONT_LEFT_M.setInverted(false);
    RobotComponents.DriveTrain.REAR_LEFT_M.setInverted(false);

    RobotComponents.DriveTrain.REAR_RIGHT_M.setInverted(false);
    RobotComponents.DriveTrain.FRONT_RIGHT_M.setInverted(false);

    RobotComponents.DriveTrain.FRONT_LEFT_M.set(ControlMode.Follower,
        RobotComponents.DriveTrain.REAR_LEFT_M.getDeviceID());
    RobotComponents.DriveTrain.FRONT_RIGHT_M.set(ControlMode.Follower,
        RobotComponents.DriveTrain.REAR_RIGHT_M.getDeviceID());

    RobotComponents.DriveTrain.LEFT_ENCODER.setDistancePerPulse(RobotConstants.Sensors.DRIVE_ENCODER_DPP);
    RobotComponents.DriveTrain.RIGHT_ENCODER.setDistancePerPulse(RobotConstants.Sensors.DRIVE_ENCODER_DPP);
    RobotComponents.Lift.ENCODER.setDistancePerPulse(RobotConstants.Sensors.LIFT_ENCODER_DPP);

    Robot.driveTrain = new TankDrivetrain(
        (Double speed) -> RobotComponents.DriveTrain.REAR_LEFT_M.set(ControlMode.PercentOutput, speed),
        (Double speed) -> RobotComponents.DriveTrain.REAR_RIGHT_M.set(ControlMode.PercentOutput, -speed));
        Robot.oi = new OI();

    Robot.driveTrain.setDefaultCommand(
        new DriveArcade(Robot.driveTrain, () -> RobotStates.isDriveInverted() ? 1 * Robot.oi.driverXbox.getY(Hand.kLeft)
            : -1 * Robot.oi.driverXbox.getY(Hand.kLeft), () -> -Robot.oi.driverXbox.getX(Hand.kLeft))); 

    SmartDashboard.putData(new TestPID());
    SmartDashboard.putData(new MoveSubsystemWithJoystick(Robot.lift, oi.driverXbox));

    // Robot data to be periodically published to SmartDashboard                    
    dbc.addNumber("Gyro", RobotComponents.DriveTrain.GYRO::getAngle);
    dbc.addNumber("Right encoder", RobotComponents.DriveTrain.RIGHT_ENCODER::getDistance);
    dbc.addNumber("Left encoder", RobotComponents.DriveTrain.LEFT_ENCODER::getDistance);
    dbc.addNumber("180 potentiometer", Robot.oneEighty::getAngle);
    dbc.addNumber("Lift encoder", Robot.lift::getHeight);
    // Robot states to be periodically published to SmartDashboard
    dbc.addNumber("Height index", RobotStates::getHeightIndex);    
    dbc.addBoolean("One Eighty Override", RobotStates::isOneEightyOverride);
    dbc.addBoolean("Lift Override", RobotStates::isLiftOverride);
    dbc.addBoolean("Is Has Cargo", RobotStates::isHasCargo);
    dbc.addBoolean("Inverted Drive", RobotStates::isDriveInverted);

    addTests();
  }

  @Override
  public void robotPeriodic() {
    Robot.dbc.update();
    SmartDashboard.putData("Scheduler", Scheduler.getInstance());
    if (Robot.lift.isAtBottom() || SmartDashboard.getBoolean("reset enc", false))
      RobotComponents.Lift.ENCODER.reset();
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
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
  public void teleopInit() {
    RobotComponents.DriveTrain.RIGHT_ENCODER.reset();
    RobotComponents.DriveTrain.LEFT_ENCODER.reset();
    RobotComponents.DriveTrain.GYRO.calibrate();
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
    Scheduler.getInstance().run();
    SmartDashboard.putData("selected test command", this.testsChooser.getSelected());
  }

  private void addTests() {
    testsChooser.addDefault("Hatch Unlock Default", new SetHatchLock(Value.kReverse));
    testsChooser.addOption("cargoCollection", new CollectCargo(0.3, 0.3));

    testsChooser.addOption("Lift", new SetLiftHeight(LiftHeight.kRocketMiddleCargo));
    testsChooser.addOption("One Eighty", new SetOneEightyAngle(OneEightyAngle.kStraight));

    testsChooser.addOption("hatchEjectOn", new SetHatchEject(Value.kForward));
    testsChooser.addOption("hatchEjectOff", new SetHatchEject(Value.kReverse));

    testsChooser.addOption("hatchLockOn", new SetHatchLock(Value.kForward));
    testsChooser.addOption("hatchLockOff", new SetHatchLock(Value.kReverse));

    testsChooser.addOption("hatchCollectorOn", new SetHatchCollectorState(Value.kForward));
    testsChooser.addOption("hatchCollectorOff", new SetHatchCollectorState(Value.kReverse));
  }
}

