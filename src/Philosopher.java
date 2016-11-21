/**
* Creates the philosopher Object that runs as a thread
* 
* @param id Philosopher number
* 
* @param leftChopStick
* @param rightChopStick  
 */

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher implements Runnable {

	//ReentrantLock = synchronised but more complex, no notifys
    private ReentrantLock leftFork;   // The left and right forks of each philosopher
    private ReentrantLock rightFork;
    private int Id;

    public AtomicBoolean isTummyFull=new AtomicBoolean(false);  //Did they eat? If they did, they're full

    //To randomize eat/Think time
    private Random randomGenerator = new Random();
    private int noOfTurnsToEat=0;

    public int getId(){   // gets the ID of the Philosopher
        return this.Id;
    }
    public int getNoOfTurnsToEat(){   // returns the number of times the philosopher has eaten
        return noOfTurnsToEat;
    }


    public Philosopher(int id, ReentrantLock leftFork, ReentrantLock rightFork) {
        this.Id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;  // Identifies itself and the forks around it
    }

    @Override
    public void run() {
        while ( !isTummyFull.get()) {
            try {
                think();
                if (pickupLeftFork() && pickupRightFork()) {   // if the philosopher has not eaten, then attempt to grab the forks
                    eat();
                }
                putDownForks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void think() throws InterruptedException {
        System.out.println
        		(String.format("Philosopher %s is thinking", this.Id));  // allows the philosopher to think about life and creation
        System.out.flush();
        Thread.sleep(randomGenerator.nextInt(1000));//nextInt can return 0 and z-
    }

    private void eat() throws InterruptedException {
        System.out.println(String.format("Philosopher %s is eating", this.Id));  // allows the philosopher to eat
        System.out.flush();
        noOfTurnsToEat++;
        Thread.sleep(randomGenerator.nextInt(1000));
    }

    private boolean pickupLeftFork() throws InterruptedException {
        if (leftFork.tryLock(10, TimeUnit.MILLISECONDS)) {
            System.out.println(String.format(
                    "Philosopher %s pickedup Left Fork", this.Id));  // attemps to pick up the fork to their left
            System.out.flush();
            return true;
        }
        return false;
    }

    private boolean pickupRightFork() throws InterruptedException {
        if (rightFork.tryLock(10, TimeUnit.MILLISECONDS)) {
            System.out.println(String.format(
                    "Philosopher %s pickedup Right Fork", this.Id));  // attempts to pick up the fork to their right
            System.out.flush();
            return true;
        }
        return false;
    }

    private void putDownForks() {
        if (leftFork.isHeldByCurrentThread()) {
            leftFork.unlock();
            System.out.println(String.format(
                    "Philosopher %s putdown Left Forkk", this.Id));
            System.out.flush();                                         //puts down both forks
        }
        if (rightFork.isHeldByCurrentThread()) {
            rightFork.unlock();
            System.out.println(String.format(
                    "Philosopher %s putdown Right Fork", this.Id));
            System.out.flush();
        }
    }
}