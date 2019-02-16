package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.Robot;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto extends Subsystem{

    public AHRS ahrs = null;

    public double lastTimeAuto = 0;

    public Auto(){

       ahrs = new AHRS(SerialPort.Port.kUSB);

    }

    public double getGyro(){

        double output = ahrs.getAngle();

        return output;
    }

    public void resetGyro(){

        ahrs.reset();

    }


    //Gyro turn left and right
    public void turn(double setpoint, double speed, double timeout){
        
    lastTimeAuto = 0;

    while(lastTimeAuto < timeout){

        lastTimeAuto = lastTimeAuto + 0.0025;

        double P = RobotMap.NAV_GYRO_P;
		double I = RobotMap.NAV_GYRO_I;
		double D = RobotMap.NAV_GYRO_D;

		double integral = 0;
        double previous_error = 0;
        double error = 0;
    
		error = setpoint - ahrs.getAngle();
		integral = integral + (error*0.02);
		double derivative = (error - previous_error) / 0.02;
		double rcw = P*error + I*integral + D*derivative;

        Robot.m_drivetrain.mecanumDrive(0, 0, rcw, 0, 0.1);
    
    }

    lastTimeAuto = 0;

    }

    //Drive forward and back
    public void drive(double setpoint, double speed, double timeout){

    Robot.m_drivetrain.resetEncoders();

    lastTimeAuto = 0;

    while(lastTimeAuto < timeout){

        lastTimeAuto = lastTimeAuto + 0.0025;

        double P = RobotMap.ENCODER_P;
	    double I = RobotMap.ENCODER_I;
		double D = RobotMap.ENCODER_D;

	    double integral = 0;
        double previous_error = 0;
        double error = 0;
    
		error = setpoint - Robot.m_drivetrain.getEncoderAvg();
		integral = integral + (error*0.02);
		double derivative = (error - previous_error) / 0.02;
		double rcw = P*error + I*integral + D*derivative;

        Robot.m_drivetrain.mecanumDrive(0, -rcw, 0, 0, speed);


        SmartDashboard.putNumber("ERROR", error);
    }

    lastTimeAuto = 0;

    }

    

    //Drive and turn
    public void turnDrive(double GYROsetpoint, double ENCODERsetpoint, double speed, double timeout){

    Robot.m_drivetrain.resetEncoders();

    lastTimeAuto = 0;

    while(lastTimeAuto < timeout){

        lastTimeAuto = lastTimeAuto + 0.0025;

        double ENCODERP = RobotMap.ENCODER_P;
	    double ENCODERI = RobotMap.ENCODER_I;
		double ENCODERD = RobotMap.ENCODER_D;

	    double ENCODERintegral = 0;
        double ENCODERprevious_error = 0;
        double ENCODERerror = 0;

        double GYROP = RobotMap.NAV_GYRO_P;
		double GYROI = RobotMap.NAV_GYRO_I;
		double GYROD = RobotMap.NAV_GYRO_D;

		double GYROintegral = 0;
        double GYROprevious_error = 0;
        double GYROerror = 0;
    
		GYROerror = GYROsetpoint - ahrs.getAngle();
		GYROintegral = GYROintegral + (GYROerror*0.02);
		double GYROderivative = (GYROerror - GYROprevious_error) / 0.02;
		double GYROrcw = GYROP*GYROerror + GYROI*GYROintegral + GYROD*GYROderivative;
    
		ENCODERerror = ENCODERsetpoint - Robot.m_drivetrain.getEncoderAvg();
		ENCODERintegral = ENCODERintegral + (ENCODERerror*0.02);
		double ENCODERderivative = (ENCODERerror - ENCODERprevious_error) / 0.02;
		double ENCODERrcw = ENCODERP*ENCODERerror + ENCODERI*ENCODERintegral + ENCODERD*ENCODERderivative;

        Robot.m_drivetrain.mecanumDrive(0, -ENCODERrcw, GYROrcw, 0, speed);

    }

    lastTimeAuto = 0;

    }

    

    //Vision forward drive and side to side
    public void vision(double setpoint, double speed, double timeout){

    }

    

    //Lift up and down
    public void lift(double setpoint, double speed, double timeout){

    }

    

    //Lift and drive
    public void liftDrive(double LIFTsetpoint,  double LIFTspeed, double ENCODERsetpoint, double ENCODERspeed, double timeout){

    }
    

    @Override
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		//setDefaultCommand(new DriveArcade());
	}

}