package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import frc.robot.RobotConstants.LiftHeight;
import frc.robot.RobotConstants.OneEightyAngle;
import frc.robot.RobotConstants.PrepareToScoreHeight;
import frc.robot.CargoCollectorCommands.CollectCargo;
import frc.robot.CargoCollectorCommands.PushCargo;
import frc.robot.CargoFolderCommands.SetCargoFolderState;
import frc.robot.OICommands.CollectCargoFromFloor;
import frc.robot.Commands.ReachCargoShipHeight;
import frc.robot.Commands.SetHasCargo;
import frc.robot.DrivingCommands.ToggleDriveInverted;
import frc.robot.HatchCollectorCommands.SetHatchCollectorState;
import frc.robot.HatchHolderCommands.EjectHatch;
import frc.robot.HatchHolderCommands.SetHatchLock;
import frc.robot.LiftCommands.LiftSwitchOverride;
import frc.robot.LiftCommands.ReachLiftHeight;
import frc.robot.LiftCommands.SetLiftOverride;
import frc.robot.OICommands.AfterCargoFloorPreparation;
import frc.robot.OICommands.AfterHatchFeederPreparation;
import frc.robot.OICommands.CollectHatchFromFeeder;
import frc.robot.OICommands.CollectHatchFromFloor;
import frc.robot.OICommands.DefenceMode;
import frc.robot.OICommands.PrepareToScore;
import frc.robot.OICommands.Push;
import frc.robot.OICommands.PushWhenLiftMoved;
import frc.robot.OneEightyCommands.OneEightyToggleOverride;
import frc.robot.OneEightyCommands.SetOneEightyAngle;
import frc.robot.OneEightyCommands.SetOneEightyOverride;

public class OI {
    public XboxController operatorXbox = new XboxController(0);
    public XboxController driverXbox = new XboxController(1);
    Button driverButtonY, driverButtonA, driverButtonB, driverButtonX, driverButtonLB, driverButtonRB;
    Button operatorButtonX, operatorButtonY, operatorButtonLB, operatorButtonRB, operatorButtonA, operatorButtonB, operatorStartButton, operatorButtonAxisRight, operatorButtonAxisLeft;
    POVButton operatorRightPOVButton, operatorLeftPOVButton; 

    UsbCamera cam0, cam1;
    UsbCamera[] cams = new UsbCamera[2];
    int currentCam = 0;

    public OI() {
        // driver buttons
        this.driverButtonA = new JoystickButton(driverXbox, 1);
        this.driverButtonB = new JoystickButton(driverXbox, 2);
        this.driverButtonX = new JoystickButton(driverXbox, 3); 
        this.driverButtonY = new JoystickButton(driverXbox, 4);
        this.driverButtonLB = new JoystickButton(driverXbox, 5);
        this.driverButtonRB = new JoystickButton(driverXbox, 6);

        // operator buttons
        this.operatorButtonA = new JoystickButton(operatorXbox, 1);
        this.operatorButtonB = new JoystickButton(operatorXbox, 2);
        this.operatorButtonX = new JoystickButton(operatorXbox, 3);
        this.operatorButtonY = new JoystickButton(operatorXbox, 4);
        this.operatorButtonLB = new JoystickButton(operatorXbox, 5);
        this.operatorButtonRB = new JoystickButton(operatorXbox, 6);
        this.operatorStartButton = new JoystickButton(operatorXbox, 8);
        this.operatorButtonAxisLeft = new JoystickButton(operatorXbox, 9);
        this.operatorButtonAxisRight = new JoystickButton(operatorXbox, 10);
        this.operatorRightPOVButton = new POVButton(operatorXbox, 90);
        this.operatorLeftPOVButton = new POVButton(operatorXbox, 270);

        cam0 = CameraServer.getInstance().startAutomaticCapture(0);
        cam1 = CameraServer.getInstance().startAutomaticCapture(1);
        cams[0] = cam0;
        cams[1] = cam1;

        /*this.operatorButtonB.whenPressed(new Push());
        this.operatorButtonB.whenReleased(new PushWhenLiftMoved());

        this.operatorButtonY.whenPressed(new CollectHatchFromFeeder());
        this.operatorButtonY.whenReleased(new AfterHatchFeederPreparation());

        this.operatorButtonRB.whenPressed(new PrepareToScore(true));  
        this.operatorButtonLB.whenPressed(new PrepareToScore(false));
        this.operatorButtonX.whenPressed(new PrepareToScore(PrepareToScoreHeight.kCargoShip)); 

        this.operatorButtonAxisRight.whenPressed(new OneEightyToggleOverride());
        this.operatorButtonAxisLeft.whenPressed(new LiftSwitchOverride());

        this.operatorRightPOVButton.whileHeld(new SetHasCargo(true)); 
        this.operatorLeftPOVButton.whileHeld(new SetHasCargo(false));

        this.operatorButtonA.whenPressed(new CollectCargoFromFloor());
        this.operatorButtonA.whenReleased(new AfterCargoFloorPreparation());  
        
        this.operatorStartButton.whenPressed(new DefenceMode());*/
        
        //-------------------- DRIVER --------------------------------------------
        
        this.driverButtonLB.whenPressed(new EjectHatch());
        this.driverButtonRB.whenPressed(new PushCargo());
        this.driverButtonA.whenPressed(new ToggleDriveInverted()); 

        driverButtonB.whenPressed(new CollectHatchFromFloor());
        driverButtonB.whenReleased(new SetHatchCollectorState(Value.kForward));//should be cmdG?

        driverButtonY.whileHeld(new CollectCargo(-1, -1,false));

        //-------------------- OPERATOR --------------------------------------------

        this.operatorButtonRB.whenPressed(new ReachCargoShipHeight());
        this.operatorButtonLB.whenPressed(new ReachLiftHeight(LiftHeight.kLiftBottomHatch));//make cmdG like reachcargoshipheight

        this.operatorButtonA.whenPressed(new CollectCargoFromFloor());        
        this.operatorButtonA.whenReleased(new AfterCargoFloorPreparation());
        
        //this.operatorButtonX.whileHeld(new CollectCargo(0, 0));// use hatch feeder cmdG

        this.operatorButtonY.whenPressed(new SetCargoFolderState(Value.kForward));//might change that btn

        this.operatorLeftPOVButton.whenPressed(new SetOneEightyAngle(OneEightyAngle.kBack));
        this.operatorRightPOVButton.whenPressed(new SetOneEightyAngle(OneEightyAngle.kStraight));
        
        this.operatorButtonB.whenPressed(new SetHatchLock(Value.kReverse));
        this.operatorButtonB.whenReleased(new SetHatchLock(Value.kForward));

        operatorStartButton.whenPressed(new DefenceMode());

        //this.operatorButtonAxisRight.whenPressed(new SetOneEightyOverride());
        //this.operatorButtonAxisLeft.whenPressed(new SetLiftOverride());
    }

	public void changeCam(int cam) {
        NetworkTableInstance.getDefault().getTable("").getEntry("cameraSelection")
        .setValue(cams[cam].getName());
        currentCam = cam;
	}

	public void tooggleCam() {
        NetworkTableInstance.getDefault().getTable("").getEntry("cameraSelection")
        .setValue(cams[1-currentCam].getName());
        currentCam = 1 - currentCam;
	}
}
