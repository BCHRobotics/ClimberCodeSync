package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Lift extends Subsystem{

    double encoderCal = 1;

    //Init talonSRX
    TalonSRX TALONLIFT = new TalonSRX(RobotMap.TALONLIFT);

    final int kTimeoutMs = 30;
    final boolean kDiscontinuityPresent = true;
	final int kBookEnd_0 = 910;		/* 80 deg */
	final int kBookEnd_1 = 1137;	/* 100 deg */


    public Lift(){

        double rampRate = 1;

        TALONLIFT.configFactoryDefault();

        initQuadrature();

        TALONLIFT.configOpenloopRamp(rampRate);

        TALONLIFT.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, kTimeoutMs);

    }

    //Function that actuate the lift
    public void MoveLift(double liftSpeed){

        TALONLIFT.set(ControlMode.PercentOutput, -liftSpeed);

    }

    public void resetEncoder(){
        TALONLIFT.setSelectedSensorPosition(0, 0, this.kTimeoutMs);
    }

    public double getEncoder(){

        double encoderMove = TALONLIFT.getSelectedSensorPosition(0);
        encoderMove = encoderMove * encoderCal;

        return encoderMove;
    }

    public void initQuadrature() {
		/* get the absolute pulse width position */
		int pulseWidth = TALONLIFT.getSensorCollection().getPulseWidthPosition();

		/**
		 * If there is a discontinuity in our measured range, subtract one half
		 * rotation to remove it
		 */
		if (kDiscontinuityPresent) {

			/* Calculate the center */
			int newCenter;
			newCenter = (kBookEnd_0 + kBookEnd_1) / 2;
			newCenter &= 0xFFF;

			/**
			 * Apply the offset so the discontinuity is in the unused portion of
			 * the sensor
			 */
			pulseWidth -= newCenter;
		}

		/**
		 * Mask out the bottom 12 bits to normalize to [0,4095],
		 * or in other words, to stay within [0,360) degrees 
		 */
		pulseWidth = pulseWidth & 0xFFF;

		/* Update Quadrature position */
		TALONLIFT.getSensorCollection().setQuadraturePosition(pulseWidth, kTimeoutMs);
	}

    @Override
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		//setDefaultCommand(new DriveArcade());
	}

}