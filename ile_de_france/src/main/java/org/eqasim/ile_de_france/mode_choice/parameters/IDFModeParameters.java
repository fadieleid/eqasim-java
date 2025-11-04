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
		parameters.betaCost_u_MU = -0.206;							//base value = -0.206
		parameters.lambdaCostEuclideanDistance = -0.4;				//base value = -0.4
		parameters.referenceEuclideanDistance_km = 40.0;			//base value = 40.0

		// Car
		parameters.car.alpha_u = 1.35;								//base value = 1.35
		parameters.car.betaTravelTime_u_min = -0.06;				//base value = -0.06

		parameters.car.additionalAccessEgressWalkTime_min = 4.0;	//base value = 4.0
		parameters.car.constantParkingSearchPenalty_min = 4.0;		//base value = 4.0

		parameters.idfCar.betaInsideUrbanArea = -0.5;				//base value = -0.5
		parameters.idfCar.betaCrossingUrbanArea = -1.0;				//base value = -1.0

		// Drt
		parameters.drt.alpha_u = 0.0;								//base value = 0.0
		parameters.drt.betaTravelTime_u_min = -0.2;					//base value = -0.2
		parameters.drt.betaWaitingTime_u_min = -0.1;				//base value = -0.1
		parameters.drt.betaAccessEgressTime_u_min = 1.0;		//base value = -0.0804

		// PT
		parameters.pt.alpha_u = 0.0;								//base value = 0.0
		parameters.pt.betaLineSwitch_u = -0.17;						//base value = -0.17
		parameters.pt.betaInVehicleTime_u_min = -0.017;				//base value = -0.017
		parameters.pt.betaWaitingTime_u_min = -0.0484;				//base value = -0.0484
		parameters.pt.betaAccessEgressTime_u_min = -0.0804;			//base value = -0.0804

		// Bike
		parameters.bike.alpha_u = -2.0;								//base value = -2.0
		parameters.bike.betaTravelTime_u_min = -0.05;				//base value = -0.05
		parameters.bike.betaAgeOver18_u_a = -0.0496;				//base value = -0.0496

		parameters.idfBike.betaInsideUrbanArea = 1.5;				//base value = 1.5

		// Walk
		parameters.walk.alpha_u = 1.43;								//base value = 1.43
		parameters.walk.betaTravelTime_u_min = -0.15;				//base value = -0.15

		return parameters;
	}
}
