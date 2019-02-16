/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.*;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static Drivetrain m_drivetrain = null;
  public static OI m_oi = null;
  public static Grabber m_grabber = null;
  public static Lift m_lift = null;
  public static Auto m_auto = null;
  //public static Climber m_climber = null;

  //Gyro
  public ADXRS450_Gyro m_gyro = null;
  //public AHRS ahrs = null;

  public double y = 0;           //variable for forward/backward movement
	public double x = 0;           //variable for side to side movement
	public double turn = 0;        //variable for turning movement
	public double deadzone = 0.08;	//variable for amount of deadzone
  
  public double pidTime = 0;

  public double lastTimeAuto = 0;


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    SmartDashboard.putNumber("ERROR", 454);
    SmartDashboard.putNumber("AUTO", 666);
    
    SmartDashboard.putNumber("EncoderFR", 6969);
    SmartDashboard.putNumber("EncoderBR", 6969);
    SmartDashboard.putNumber("EncoderFL", 6969);
    SmartDashboard.putNumber("EncoderBL", 6969);
    SmartDashboard.putNumber("EncoderAvg", 6969);

    SmartDashboard.putNumber("EncoderFRlast", 2222);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    SmartDashboard.putNumber("NAVX", 4546);

    m_drivetrain = new Drivetrain();
    m_oi = new OI();
    m_grabber = new Grabber();
    m_lift = new Lift();   
    m_gyro = new ADXRS450_Gyro();
    m_auto = new Auto();
    //m_climber = new Climber();

    //ahrs = new AHRS(SerialPort.Port.kUSB);

    //m_gyro.reset();
    //m_gyro.calibrate();

    m_drivetrain.resetEncoders();

    /*
    m_drivetrain.encoderBLlast = 0;
    m_drivetrain.encoderBRlast = 0;
    m_drivetrain.encoderFLlast = 0;
    m_drivetrain.encoderFRlast = 0;
    */

    m_auto.resetGyro();
    
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    SmartDashboard.putNumber("Nav Gyro", m_auto.getGyro());
    SmartDashboard.putNumber("EncoderFR", m_drivetrain.getEncoderFR());
    SmartDashboard.putNumber("EncoderBR", m_drivetrain.getEncoderBR());
    SmartDashboard.putNumber("EncoderFL", m_drivetrain.getEncoderFL());
    SmartDashboard.putNumber("EncoderBL", m_drivetrain.getEncoderBL());
    SmartDashboard.putNumber("EncoderAvg", m_drivetrain.getEncoderAvg());

    SmartDashboard.putNumber("EncoderFRlast", m_drivetrain.encoderFRlast);

  }

  @Override
  public void autonomousInit() {

    m_drivetrain.resetEncoders();
    lastTimeAuto = 0;
    m_autoSelected = m_chooser.getSelected();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

   m_auto.resetGyro();
  }

  @Override
  public void autonomousPeriodic() {

    m_auto.drive(96, 0.005, 100);


    /*
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
        
    }*/

  }

  //Reset gyro when the robot is disabled
  @Override
  public void disabledInit(){
    m_gyro.reset();
    m_auto.resetGyro();
    
    m_drivetrain.resetEncoders();
    lastTimeAuto = 0;
  }

  
  //Reset gyro when the robot is entering teleop
  @Override
  public void teleopInit() {
    super.teleopInit();
    //m_gyro.reset();
    m_auto.resetGyro();

    //m_drivetrain.resetEncoders();
    
  }

  @Override
  public void teleopPeriodic() {

    

    //Init the moveSpeed variable
    double moveSpeed = 0.7;

    //Check for Snail and Turbo buttons to speed or slow the robot
    if(Robot.m_oi.ButtonSnail.get() == true){

      moveSpeed = 0.5;
    
    } else if(Robot.m_oi.ButtonTurbo.get() == true){

      moveSpeed = 1;
    
    }


  if(Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEY) > deadzone || Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEY) < -deadzone) {
     y = Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEY);
  } else {
    y = 0;
  }

  if(Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEX) > deadzone || Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEX) < -deadzone) {
    x = Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_MOVEX);
  } else {
    x = 0;
  }

  if(Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_ROTATE) > deadzone*2 || Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_ROTATE) < -deadzone*2){
    turn = Robot.m_oi.driveStick.getRawAxis(RobotMap.OI_DRIVESTICK_ROTATE);
  } else {
    turn = 0;
  }
    
  // m_drivetrain.mecanumDrive(y, x, turn, ahrs.getAngle(), moveSpeed);

  m_drivetrain.mecanumDrive(y, x, turn, 0, moveSpeed);

    
    //Check if grabber buttons are pressed then open or close grabber
    /*
    if(Robot.m_oi.ButtonGrabOpen.get() == true){
        Robot.m_grabber.grabberOpen();
    } else if(Robot.m_oi.ButtonGrabClose.get() == true){
      Robot.m_grabber.grabberClose();
    } 
*/
    //Manipulate the Lift
    if(Robot.m_oi.ButtonLiftEnable.get() == true){
      Robot.m_lift.MoveLift(Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_LIFT));
    } else {
      Robot.m_lift.MoveLift(-0.22);
    }

      m_grabber.tilt(Robot.m_oi.funStick.getRawAxis(4)*-0.2);
      m_grabber.extend(Robot.m_oi.funStick.getRawAxis(5)*-0.8);

      SmartDashboard.putNumber("ERROR", Robot.m_oi.funStick.getRawAxis(RobotMap.OI_FUNSTICK_LIFT));
    
    
    
    
    
    }

  @Override
  public void testPeriodic() {

  }
}



