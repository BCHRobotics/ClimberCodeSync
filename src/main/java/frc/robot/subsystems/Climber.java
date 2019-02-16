package frc.robot.subsystems;

import frc.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Subsystem;


/* Code written by Noah and modified by Sean
 * February 15, 2019                        */

public class Climber extends Subsystem{

    double ARMencoderCal = 1;
    double LEGencoderCal = 1;

    double encoderARM1last = 0;
    double encoderARM2last = 0;

    CANSparkMax ARMLEFT = new CANSparkMax(RobotMap.SPARKARMLEFT, MotorType.kBrushless);
    CANSparkMax ARMRIGHT = new CANSparkMax(RobotMap.SPARKARMRIGHT, MotorType.kBrushless);

    CANSparkMax SPARKLEG = new CANSparkMax(RobotMap.SPARKLEG, MotorType.kBrushless);
    TalonSRX TALONLEGWHEEL = new TalonSRX(RobotMap.TALONLEGWHEEL);

    
    public CANEncoder encoderLEG = new CANEncoder(SPARKLEG);
    public CANEncoder encoderLEGLEFT = new CANEncoder(ARMLEFT);
    public CANEncoder encoderLEGRIGHT = new CANEncoder(ARMRIGHT);

    double errorAllowed = 5;

    public Climber(){

        ARMLEFT.setInverted(true);
        ARMRIGHT.setInverted(false);

        SPARKLEG.setInverted(false);
        TALONLEGWHEEL.setInverted(false);

    }

    public void armsMove(double speed){

        double error = encoderLEGLEFT - encoderLEGRIGHT; //Not sure what to do
        double speedL = speed;
        double speedR = speed;

        //If the error of the two arms is different, the farther arm ahead will slow down to 25% in efforts for the other arm to catch up [Sean Tomkins]
        if(error > errorAllowed){
        speedL = speed * .25;
        } else if(error < errorAllowed){
        speedR = speed * .25;
        }

        //Set arm speeds after calculations and apply them
        ARMLEFT.set(speedL);
        ARMRIGHT.set(speedR);

    }

    public void legExtend(double speed){

        SPARKLEG.set(speed);

    }

    public void legWheel(double speed){

        TALONLEGWHEEL.set(ControlMode.PercentOutput, speed);

    }

    public double getEncoderARM1(){

        double encoderVal = -((encoderLEGLEFT.getPosition() * ARMencoderCal) - encoderARM1last);

        return encoderVal;
    }

    public double getEncoderARM2(){

        double encoderVal = -((encoderLEGRIGHT.getPosition() * ARMencoderCal) - encoderARM2last);

        return encoderVal;
    }

    public double getEncoderAvg(){

        double result = (getEncoderARM1() + getEncoderARM2()) / 2;

        return result;
    }

 /*   public double getEncoderLEG(){ //Noah needs to fix the code here VVVVVVVVVV

        double encoderVal = SPARKLEG.getSelectedSensorPosition(0);

        return encoderVal;
 }

    public void resetEncoderLEG(){
        SPARKLEG.setSelectedSensorPosition(0, 0, this.kTimeoutMs);
    }*/

    public void resetEncoders(){

        encoderARM1last = encoderLEGLEFT.getPosition() * ARMencoderCal;
        encoderARM2last = encoderLEGRIGHT.getPosition() * ARMencoderCal;
            
    }
    
    @Override
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		//setDefaultCommand(new DriveArcade());
	}

}