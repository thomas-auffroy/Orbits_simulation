import java.util.ArrayList;

public class Parameters {
	/*
	 * All the parameters related to the stars
	 */
	
	private Constants constants = new Constants();
	
	protected ArrayList<ArrayList<Double>> routesCoordinates = new ArrayList<ArrayList<Double>>(); 
	
	protected ArrayList<ArrayList<Double>> coordinates = new ArrayList<ArrayList<Double>>();
	protected ArrayList<ArrayList<Double>> speeds = new ArrayList<ArrayList<Double>>();
	protected ArrayList<ArrayList<Double>> accelerations = new ArrayList<ArrayList<Double>>();
	
	protected ArrayList<Double> mass = new ArrayList<Double>();
	protected ArrayList<Integer> radii = new ArrayList<Integer>();
	
	
	// **********************************************************************************************************************************************
	// ************************************************************** INITIALIZATIONS ***************************************************************
	// **********************************************************************************************************************************************
	public Parameters(){
		this.addNewStar(900,500,0.0,0.0,1000.0,30); // addNewStar(center of the star, velocity, mass, radius)
	}
	
	
	// **********************************************************************************************************************************************
	// ****************************************************************** ACCESORS ******************************************************************
	// **********************************************************************************************************************************************
	public ArrayList<Double> addSpeeds(ArrayList<Double> initialSpeeds, ArrayList<Double> speedsToAdd) {
		ArrayList<Double> newSpeeds = new ArrayList<Double>();
		for(int i = 0; i < initialSpeeds.size(); i++) {newSpeeds.add(initialSpeeds.get(i)+speedsToAdd.get(i));}
		return newSpeeds;		
	}
	public ArrayList<Double> addCoordinates(ArrayList<Double> initialCoordinates, ArrayList<Double> CoordinatesToAdd) {
		ArrayList<Double> newCoordinates = new ArrayList<Double>();
		for(int i = 0; i < initialCoordinates.size(); i++) {newCoordinates.add(initialCoordinates.get(i)+CoordinatesToAdd.get(i));}
		return newCoordinates;		
	}
	public ArrayList<Double> addAccelerations(ArrayList<Double> initialAccelerations, ArrayList<Double> AccelerationsToAdd) {
		ArrayList<Double> newAccelerations = new ArrayList<Double>();
		for(int i = 0; i < initialAccelerations.size(); i++) {newAccelerations.add(initialAccelerations.get(i)+AccelerationsToAdd.get(i));}
		return newAccelerations;		
	}
	
	public ArrayList<Double> computeGravitationalInteraction(ArrayList<Double> movingObject, ArrayList<Double> fixedObject, double massFixedObject){
		ArrayList<Double> inertialCoordinates = new ArrayList<Double>();
		ArrayList<Double> gravitation = new ArrayList<Double>();
		for(int i = 0; i < movingObject.size(); i++) {inertialCoordinates.add(movingObject.get(i)-fixedObject.get(i));}
		for(int i = 0; i < inertialCoordinates.size(); i++) {
			if(inertialCoordinates.get(i) == 0) {
				gravitation.add(0.0);
			}
			else if (i == 0) {
				gravitation.add(-Math.abs(inertialCoordinates.get(i))*Math.cos(this.getTheta(inertialCoordinates))*this.constants.gravitational*massFixedObject/(Math.pow(this.getDistance(movingObject, fixedObject),2)*inertialCoordinates.get(i)));
			} else {
				gravitation.add(-Math.abs(inertialCoordinates.get(i))*Math.sin(this.getTheta(inertialCoordinates))*this.constants.gravitational*massFixedObject/(Math.pow(this.getDistance(movingObject, fixedObject),2)*inertialCoordinates.get(i)));
			}
		}
		return gravitation;	
	}
	
	public double getDistance(ArrayList<Double> objectA, ArrayList<Double> objectB) {
		/*
		 * Return the Euclidean distance between the object A and the object B 
		 */
		double xA = objectA.get(0), xB = objectB.get(0), yA = objectA.get(1), yB = objectB.get(1);
		return Math.pow(Math.pow(xA-xB,2)+Math.pow(yA-yB, 2), 0.5);
	}
	
	public double getTheta(ArrayList<Double> objectA) {
		/*
		 * Compute and return the angle between the origin and the objectA
		 */
		double theta;
		if(objectA.get(0)/objectA.get(1)>0) {theta =Math.atan(objectA.get(1)/objectA.get(0));}
		else {theta =-Math.atan(objectA.get(1)/objectA.get(0));}
		return theta;
	}
	
	public boolean isCollisionTrue(ArrayList<Double> objectA, int radiusA, ArrayList<Double> objectB, int radiusB) {
		/*
		 * Return false if there is no collision between the objectA and the objectB or true if objectA and objectB overlap
		 */
		boolean collision = false;
		if(this.getDistance(objectA,objectB) < radiusA+radiusB) {collision = true;}
		return collision;		
	}
	
	public double computeEnergy(int i, int j) {
		/*
		 * Return the energy of the star at the index 'i' related to the star at the index 'k'
		 */
		double potentialEnergy = -this.constants.gravitational*this.mass.get(i)*this.mass.get(j)/this.getDistance(this.coordinates.get(i), coordinates.get(j)); 
		double kineticEnergy = this.mass.get(i)*Math.pow(speeds.get(i).get(0),2)+Math.pow(speeds.get(i).get(1),2);	
		return potentialEnergy+kineticEnergy;
	}
	
	
	// **********************************************************************************************************************************************
	// ****************************************************************** MUTATORS ******************************************************************
	// **********************************************************************************************************************************************
	public void addNewStar(int x, int y, double vX, double vY, double mass, int radius) {
		/*
		 * Add a new star to the system
		 */
		ArrayList<Double> initializationCoordinates = new ArrayList<Double>();
		initializationCoordinates.add((double) x);
		initializationCoordinates.add((double) y);
		
		ArrayList<Double> initializationSpeeds = new ArrayList<Double>();
		initializationSpeeds.add(vX);
		initializationSpeeds.add(vY);
		
		ArrayList<Double> initializationAccelerations = new ArrayList<Double>();
		initializationAccelerations.add(0.0);
		initializationAccelerations.add(0.0);
		
		this.coordinates.add(initializationCoordinates);
		this.speeds.add(initializationSpeeds);
		this.accelerations.add(initializationAccelerations);
		
		this.routesCoordinates.add(initializationCoordinates);
		
		this.mass.add(mass);
		this.radii.add(radius);
	}
	
	public void mergeStars(int i, int k, boolean b) {
		/*
		 * Merge the two stars at the index i and k. 
		 * The one with the lowest energy will gave its radius and mass
		 */
		double energyA = this.computeEnergy(i,k), energyB = this.computeEnergy(k,i);
		if(b) {
			if (energyA<energyB) {
				this.mass.set(i,mass.get(k)+mass.get(i));
				this.radii.set(i,radii.get(k)+radii.get(i));
				this.erasePlanet(k, true);
			} else {
				this.mass.set(k,mass.get(i)+mass.get(k));
				this.radii.set(k,radii.get(i)+radii.get(k));
				this.erasePlanet(i, true);
			}
		}
	}
	
	public void erasePlanet(int i, boolean b) {
		/*
		 * Erase the planet at the index "i"
		 */
		if (b) {
			coordinates.remove(i);
			speeds.remove(i);
			accelerations.remove(i);
			routesCoordinates.remove(i);
			radii.remove(i);
			mass.remove(i);
		}
	}
	
	public ArrayList<Double> getBarycenterCoordinates(){
		ArrayList<Double> barycenterCoordinates = new ArrayList<Double>();
		double xDoom=0.0, yDoom=0.0, sumMass=0.0;
		for(int i = 0; i < coordinates.size(); i++) {
			xDoom += mass.get(i)*coordinates.get(i).get(0);
			yDoom += mass.get(i)*coordinates.get(i).get(1);
			sumMass += mass.get(i);
		}
		barycenterCoordinates.add(xDoom/sumMass);
		barycenterCoordinates.add(yDoom/sumMass);
		return barycenterCoordinates;
	}
	
	public ArrayList<Double> additionArray(ArrayList<ArrayList<Double>> arrayToAdd){
		/*
		 * Return the sum of all the components on X and Y in an array
		 */
		ArrayList<Double> finalResult = new ArrayList<Double>();
		double x = 0.0, y = 0.0;
		for(int i = 0; i < arrayToAdd.size(); i++) {
			x += arrayToAdd.get(i).get(0);
			y += arrayToAdd.get(i).get(1);
		}
		finalResult.add(x);
		finalResult.add(y);
		return finalResult;
	}
	@SuppressWarnings("unchecked")
	public void moveStars() {

		ArrayList<ArrayList<Double>> coordinatesBuffer = (ArrayList<ArrayList<Double>>) coordinates.clone();
		ArrayList<ArrayList<Double>> speedsBuffer = (ArrayList<ArrayList<Double>>) speeds.clone();
		ArrayList<ArrayList<Double>> accelerationsBuffer = (ArrayList<ArrayList<Double>>) accelerations.clone();
	
		
		for(int i = 0; i < coordinatesBuffer.size(); i++) {
			ArrayList<ArrayList<Double>> gravitationalInteraction = new ArrayList<ArrayList<Double>>();
			for (int j = 0; j < coordinatesBuffer.size(); j++) {
				if (i!=j) {
					gravitationalInteraction.add(computeGravitationalInteraction(coordinates.get(i), coordinates.get(j), mass.get(j)));
				}
				accelerationsBuffer.set(i,additionArray(gravitationalInteraction));			
			}
			speedsBuffer.set(i,this.addSpeeds(speedsBuffer.get(i),accelerationsBuffer.get(i)));
			coordinatesBuffer.set(i,this.addCoordinates(coordinatesBuffer.get(i), speedsBuffer.get(i)));	
		}
		
		for(int i = 0; i < coordinatesBuffer.size(); i++) {
			coordinates.set(i, coordinatesBuffer.get(i));
			speeds.set(i, speedsBuffer.get(i));
			accelerations.set(i, accelerationsBuffer.get(i));
			for(int j = 0; j < 2; j++) {routesCoordinates.get(i).add(coordinates.get(i).get(j));}
		}
		
		for(int i = 0; i < coordinates.size(); i++) {
			for(int j = 0; j < coordinates.size(); j++) {
				if (i!=j) {
					this.mergeStars(i, j, this.isCollisionTrue(this.coordinates.get(i), this.radii.get(i), this.coordinates.get(j), this.radii.get(j)));
				}
			}
		}
	}

}
