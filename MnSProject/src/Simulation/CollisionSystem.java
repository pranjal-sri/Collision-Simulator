package Simulation;

import java.awt.Color;
import java.util.PriorityQueue;
import java.util.Scanner;


public class CollisionSystem {
    private static final double HZ = 0.5;    // number of redraw events per clock tick

    private PriorityQueue<Event> pq;          // the priority queue
    private double t  = 0.0;          // simulation clock time
    private Particle[] particles;     // the array of particles


    public CollisionSystem(Particle[] particles) {
        this.particles = particles.clone();   // defensive copy
    }

    // updates priority queue with all new events for particle a
    private void predict(Particle a, double limit) {
        if (a == null) return;

        // particle-particle collisions
        for (int i = 0; i < particles.length; i++) {
            double dt = a.timeToHit(particles[i]);
            if (t + dt <= limit)
                pq.offer(new Event(t + dt, a, particles[i]));
        }

        // particle-wall collisions
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (t + dtX <= limit) pq.offer(new Event(t + dtX, a, null));
        if (t + dtY <= limit) pq.offer(new Event(t + dtY, null, a));
    }

    // redraw all particles
    private void redraw(double limit) {
        StdDraw.clear();
        for (int i = 0; i < particles.length; i++) {
            particles[i].draw();
        }
        StdDraw.show();
        StdDraw.pause(20);
        if (t < limit) {
            pq.offer(new Event(t + 1.0 / HZ, null, null));
        }
    }

      
    /**
     * Simulates the system of particles for the specified amount of time.
     *
     * @param  limit the amount of time
     */
    public void simulate(double limit) {
        
        // initialize PQ with collision events and redraw event
        pq = new PriorityQueue<Event>();
        for (int i = 0; i < particles.length; i++) {
            predict(particles[i], limit);
        }
        pq.offer(new Event(0, null, null));        // redraw event


        // the main event-driven simulation loop
        while (!pq.isEmpty()) { 

            // get impending event, discard if invalidated
            Event e = pq.poll();
            if (!e.isValid()) continue;
            Particle a = e.a;
            Particle b = e.b;

            // physical collision, so update positions, and then simulation clock
            for (int i = 0; i < particles.length; i++)
                particles[i].move(e.time - t);
            t = e.time;

            // process event
            if      (a != null && b != null) a.bounceOff(b);              // particle-particle collision
            else if (a != null && b == null) a.bounceOffVerticalWall();   // particle-wall collision
            else if (a == null && b != null) b.bounceOffHorizontalWall(); // particle-wall collision
            else if (a == null && b == null) redraw(limit);               // redraw event

            // update the priority queue with new collisions involving a or b
            predict(a, limit);
            predict(b, limit);
        }
    }
    public static void main(String[] args) {

        StdDraw.setCanvasSize(600, 600);

        // enable double buffering
        StdDraw.enableDoubleBuffering();

        // the array of particles
        Particle[] particles;
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter 1 for random particle placement, 2 for input: ");
        int choice =sc.nextInt();
        // create n random particles
        if (choice == 1) {
        	System.out.print("\nEnter the number of Particles: ");
            int n = sc.nextInt();
            particles = new Particle[n];
            for (int i = 0; i < n; i++)
                particles[i] = new Particle();
        }

        // or read from standard input
        else {
        	
            int n = sc.nextInt();
            particles = new Particle[n];
            for (int i = 0; i < n; i++) {
            	System.out.println("enter parameters: ");
                double rx     =sc.nextDouble();
                double ry     =sc.nextDouble();
                double vx     = sc.nextDouble();
                double vy     = sc.nextDouble();
                double radius = sc.nextDouble();
                double mass   = sc.nextDouble();
                int r         = sc.nextInt();
                int g         = sc.nextInt();
                int b         = sc.nextInt();
                Color color   = new Color(r, g, b);
                particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
            }
        }

        // create collision system and simulate
        CollisionSystem system = new CollisionSystem(particles);
        system.simulate(10000);
    }
}