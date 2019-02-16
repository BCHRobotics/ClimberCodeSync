package frc.robot.subsystems;

import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.MecanumDrive;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drivetrain extends Subsystem{

    CANSparkMax SPARKLEFTFRONT = new CANSparkMax(RobotMap.SPARKLEFTFRONT, MotorType.kBrushless);
    CANSparkMax SPARKLEFTBACK = new CANSparkMax(RobotMap.SPARKLEFTBACK,MotorType.kBrushless);

    CANSparkMax SPARKRIGHTFRONT = new CANSparkMax(RobotMap.SPARKRIGHTFRONT, MotorType.kBrushless);
    CANSparkMax SPARKRIGHTBACK = new CANSparkMax(RobotMap.SPARKRIGHTBACK, MotorType.kBrushless);


    CANEncoder encoderFR = new CANEncoder(SPARKRIGHTFRONT);
    CANEncoder encoderBR = new CANEncoder(SPARKRIGHTBACK);
    CANEncoder encoderFL = new CANEncoder(SPARKLEFTFRONT);
    CANEncoder encoderBL = new CANEncoder(SPARKLEFTBACK);

    public double encoderFRlast = 0;
    public double encoderBRlast = 0;
    public double encoderFLlast = 0;
    public double encoderBLlast = 0;

    public static double encoderCal = 2.0034;

    MecanumDrive mDrive = null;

    public Drivetrain(){

        mDrive = new MecanumDrive(SPARKLEFTFRONT, SPARKLEFTBACK, SPARKRIGHTFRONT, SPARKRIGHTBACK);

        
        SPARKRIGHTBACK.setInverted(false);
        SPARKRIGHTFRONT.setInverted(false);

        SPARKLEFTBACK.setInverted(false);
        SPARKLEFTFRONT.setInverted(false);

        double m_ramp = 0.3; //1.78
        SPARKLEFTBACK.setRampRate(m_ramp);
        SPARKLEFTFRONT.setRampRate(m_ramp);
        SPARKRIGHTBACK.setRampRate(m_ramp);
        SPARKRIGHTFRONT.setRampRate(m_ramp);
        

    }

    

    public void mecanumDrive(double moveY, double moveX, double moveRot, double refAngle, double moveSpeed){

        mDrive.driveCartesian(moveY*moveSpeed*0.65, -moveX*moveSpeed, moveRot*moveSpeed*0.40, refAngle);

    }

    /*
    public void arcadeDrive(double moveSpeed, double rotateSpeed){

        SPARKLEFTBACK.set((rotateSpeed + moveSpeed));
        SPARKLEFTFRONT.set((rotateSpeed + moveSpeed));
        
        SPARKRIGHTBACK.set((rotateSpeed - moveSpeed));
        SPARKRIGHTFRONT.set((rotateSpeed - moveSpeed));

    }

    public void bfDrive(double moveSpeed){

        SPARKLEFTBACK.set(moveSpeed);
        SPARKLEFTFRONT.set(moveSpeed);
        
        SPARKRIGHTBACK.set(-moveSpeed);
        SPARKRIGHTFRONT.set(-moveSpeed);

    }
    */

    public double getEncoderFR(){

        double encoderVal = -((encoderFR.getPosition() * encoderCal) - encoderFRlast);

        return encoderVal;
    }

    public double getEncoderBR(){

        double encoderVal = -((encoderBR.getPosition() * encoderCal) - encoderBRlast);

        return encoderVal;
    }

    public double getEncoderFL(){

        double encoderVal = (encoderFL.getPosition() * encoderCal) - encoderFLlast;

        return encoderVal;
    }

    public double getEncoderBL(){

        double encoderVal = (encoderBL.getPosition() * encoderCal) - encoderBLlast;

        return encoderVal;
    }

    public double getEncoderAvg(){

        double encoderValAvg = (getEncoderFR() + getEncoderBR() + getEncoderFL() + getEncoderBL()) / 4;

        return encoderValAvg;
    }

    public void resetEncoders(){

    encoderFRlast = encoderFR.getPosition()* encoderCal;
    encoderBRlast = encoderBR.getPosition()* encoderCal;
    encoderFLlast = encoderFL.getPosition()* encoderCal;
    encoderBLlast = encoderBL.getPosition()* encoderCal;
        
    }


    @Override
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		//setDefaultCommand(new DriveArcade());
	}

}