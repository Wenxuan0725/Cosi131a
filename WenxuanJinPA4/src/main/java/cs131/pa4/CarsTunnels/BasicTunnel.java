/**
* This is a class initialize the basic Tunnel
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 12/4/2021
* COSI 131A PA4
*/
package cs131.pa4.CarsTunnels;


import cs131.pa4.Abstract.Tunnel;
import cs131.pa4.Abstract.Vehicle;

/**
 * 
 * The class for the Basic Tunnel, extending Tunnel.
 * @author cs131a
 *
 */
public class BasicTunnel extends Tunnel{

	/**
	 * There are three fields:
	 * numOfCars records the number of cars in tunnel
	 * numOfSled records the number of sled in tunnel
	 * vehicles is an array store all the car which in the tunnel
	 */
	private int numOfCars=0;
	private int numOfSled=0;
	private Vehicle[] vechicles;
	
	
	/**
	 * Creates a new instance of a basic tunnel with the given name
	 * @param name the name of the basic tunnel
	 */
	public BasicTunnel(String name) {
		super(name);
		vechicles=new Vehicle[3];
	}

	/**
	 * This is a method that vehicle tries to enter
	 */
	@Override
	protected synchronized boolean tryToEnterInner(Vehicle vehicle) {
		if(vehicle instanceof Car) {
			if(numOfSled!=0) {
				return false;
			}
			if(vechicles[2]!=null) {
				return false;
			}
			if(vechicles[0]!=null) {
				if(!vechicles[0].getDirection().equals(vehicle.getDirection())) {
					return false;
				}
			}
			vechicles[numOfCars]=vehicle;
			numOfCars++;
			return true;
		}else if(vehicle instanceof Sled){
			if(numOfCars!=0||numOfSled!=0) {
				return false;
			}
			vechicles[numOfSled]=vehicle;
			numOfSled++;
			return true;
		}else {
			return false;
		}
	}

	/**
	 * This is a method that a vehicle tries to exit
	 */
	@Override
	protected synchronized void exitTunnelInner(Vehicle vehicle) {
		if(vehicle instanceof Car) {
			if(numOfCars==0) {
				return;
			}
			numOfCars--;
			vechicles[numOfCars]=null;
			return;
		}else if(vehicle instanceof Sled) {
			if(numOfSled==0) {
				return;
			}
			numOfSled--;
			vechicles[numOfSled]=null;
			return;
		}else {
			return;
		}
	}
	
}
