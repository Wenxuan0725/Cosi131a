/**
* This is a class initialize the priority scheduler
* Known Bugs: None
*
* @author Wenxuan Jin
* wenxuanjin@brandeis.edu
* 12/4/2021
* COSI 131A PA4
*/
package cs131.pa4.CarsTunnels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


import cs131.pa4.Abstract.Scheduler;
import cs131.pa4.Abstract.Tunnel;
import cs131.pa4.Abstract.Vehicle;

/**
 * The priority scheduler assigns vehicles to tunnels based on their priority
 * It extends the Scheduler class.
 * @author cs131a
 *
 */
public class PriorityScheduler extends Scheduler{
	/**
	 * vehiclesPriority is a priority queue which store all cars information by their priority
	 */
	private PriorityQueue<Vehicle> vehiclesPriority = new PriorityQueue<>(new Comparator<Vehicle>() {
		public int compare(Vehicle vehicle1,Vehicle vehicle2) {
			return vehicle2.getPriority()-vehicle1.getPriority();
		}
	});
	
	/**
	 * tunnels is a arraylist which collect all the tunnels
	 */
	private ArrayList<Tunnel> tunnels = new ArrayList<>();
	
	/**
	 * lock is a lock used when try to put a car into a tunnel or let a car exit a tunnel
	 */
	private Lock lock = new ReentrantLock();
	
	/**
	 * vehicleReady and tunnelReady are two condition notifying if vehicle or tunnel is ready
	 */
	private Condition vehicleReady = lock.newCondition();
	private Condition tunnelReady = lock.newCondition();
	
	/**
	 * Creates an instance of a priority scheduler with the given name and tunnels
	 * @param name the name of the priority scheduler
	 * @param tunnels the tunnels where the vehicles will be scheduled to
	 */
	public PriorityScheduler(String name, Collection<Tunnel> tunnels) {
		super(name, tunnels);
		this.tunnels = (ArrayList<Tunnel>) tunnels;
	}
	
	/**
	 * Try to put a car into the tunnel
	 */
	@Override
	public Tunnel admit(Vehicle vehicle) {
		lock.lock();
		vehiclesPriority.add(vehicle);
		while(true) {
			if(vehiclesPriority.peek().getPriority()>vehicle.getPriority()) {
				try {
					//if current peek car has a higher priority, this car should wait
					vehicleReady.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				for(Tunnel tunnel: tunnels) {
					if(tunnel.tryToEnter(vehicle)) {//if a car successfully enter a tunnel
						vehiclesPriority.remove(vehicle);
						vehicleReady.signalAll();
						lock.unlock();
						return tunnel;
					}
				}
				try {//no tunnel can enter, then wait if until there is an available tunnel.
					tunnelReady.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * This is a method to remove a car from the tunnel
	 */
	@Override  
	public void exit(Vehicle vehicle) {
		lock.lock();
		for(Tunnel tunnel: tunnels) {
			tunnel.exitTunnel(vehicle);
		}
		tunnelReady.signalAll();
		vehicleReady.signalAll();
		lock.unlock();
	}
	
}
