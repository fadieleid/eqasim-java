package org.eqasim.ile_de_france.mode_choice.parameters;

import org.eqasim.core.simulation.mode_choice.parameters.ModeParameters;

public class IDFModeParameters extends ModeParameters {
	public class IDFCarParameters {
		public double betaInsideUrbanArea;
		public double betaCrossingUrbanArea;
	}

	public class IDFBikeParameters {
		public double betaInsideUrbanArea;
	}


	public final IDFCarParameters idfCar = new IDFCarParameters();
	public final IDFBikeParameters idfBike = new IDFBikeParameters();
	public DrtParameters drt = new DrtParameters();

	public static IDFModeParameters buildDefault() {
		IDFModeParameters parameters = new IDFModeParameters();

		// Cost
		parameters.betaCost_u_MU = -0.310998;							//eqasim-bavaria-main value
		parameters.lambdaCostEuclideanDistance = -0.257501;				//eqasim-bavaria-main value
		parameters.referenceEuclideanDistance_km = 4.4;					//eqasim-bavaria-main value

		// Car
		parameters.car.alpha_u = 0.4;								    //eqasim-bavaria-main value
		parameters.car.betaTravelTime_u_min = -0.042431;				//eqasim-bavaria-main value

		parameters.car.additionalAccessEgressWalkTime_min = 4.0;	//base value = 4.0
		parameters.car.constantParkingSearchPenalty_min = 4.0;		//base value = 4.0

		parameters.idfCar.betaInsideUrbanArea = -0.5;				//base value = -0.5
		parameters.idfCar.betaCrossingUrbanArea = -1.0;				//base value = -1.0

		// Drt
		parameters.drt.alpha_u = -10.0;								//base value = 0.0
		parameters.drt.betaTravelTime_u_min = -20;					//base value = -0.2
		parameters.drt.betaWaitingTime_u_min = -20;				//base value = -0.1
		parameters.drt.betaAccessEgressTime_u_min = -10.0;		//base value = -0.0804

		// PT
		parameters.pt.alpha_u = 0.0;								//eqasim-bavaria-main value
		parameters.pt.betaLineSwitch_u = -0.417658;					//eqasim-bavaria-main value
		parameters.pt.betaInVehicleTime_u_min = -0.025501;			//eqasim-bavaria-main value
		parameters.pt.betaWaitingTime_u_min = -0.021801;			//eqasim-bavaria-main value
		parameters.pt.betaAccessEgressTime_u_min = -0.0804;			//base value = -0.0804

		// Bike
		parameters.bike.alpha_u = -0.5;								//eqasim-bavaria-main value
		parameters.bike.betaTravelTime_u_min = -0.093485;			//eqasim-bavaria-main value
		parameters.bike.betaAgeOver18_u_a = -0.0496;				//base value = -0.0496

		parameters.idfBike.betaInsideUrbanArea = 1.5;				//base value = 1.5

		// Walk
		parameters.walk.alpha_u = 1.8;								//eqasim-bavaria-main value
		parameters.walk.betaTravelTime_u_min = -0.162285;			//eqasim-bavaria-main value

		return parameters;
	}
}
