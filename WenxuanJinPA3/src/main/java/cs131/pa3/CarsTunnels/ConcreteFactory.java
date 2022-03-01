/**
* This is a class initialize the concrete factory
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 11/6/2021
* COSI 131A PA3
*/
package cs131.pa3.CarsTunnels;

import cs131.pa3.Abstract.Direction;
import cs131.pa3.Abstract.Factory;
import cs131.pa3.Abstract.Tunnel;
import cs131.pa3.Abstract.Vehicle;

/**
 * The class implementing the Factory interface for creating instances of classes
 * @author cs131a
 *
 */
public class ConcreteFactory implements Factory {

	/**
	 * This is the method to create a new tunnel
	 */
    @Override
    public Tunnel createNewBasicTunnel(String name){
    		return new BasicTunnel(name);
    }

    /**
     * This is a method to create a new car
     */
    @Override
    public Vehicle createNewCar(String name, Direction direction){
    		return new Car(name, direction);
    }

    /**
     * This is a method to create a new sled
     */
    @Override
    public Vehicle createNewSled(String name, Direction direction){
    		return new Sled(name, direction);
    }
}
