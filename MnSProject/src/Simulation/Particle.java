package Simulation;

import java.awt.Color;
import java.util.Random;

public class Particle {
	private static final double INFINITY = Double.POSITIVE_INFINITY;

	private double rx, ry; // position
	private double vx, vy; // velocity
	private int count; // number of collisions so far
	private final double radius; // radius
	private final double mass; // mass
	private final Color color; // color

	public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
		this.vx = vx;
		this.vy = vy;
		this.rx = rx;
		this.ry = ry;
		this.radius = radius;
		this.mass = mass;
		this.color = color;
	}

	public Particle() {
		Random stream = new Random();
		rx = stream.nextDouble();
		ry = stream.nextDouble();
		vx = -0.005 + (stream.nextDouble() * 0.01);
		vy = -0.005 + (stream.nextDouble() * 0.01);
		radius = 0.02;
		mass = 0.5;
		color = Color.BLACK;
	}

	public void move(double dt) {
		rx += vx * dt;
		ry += vy * dt;
	}

	public void draw() {
		StdDraw.setPenColor(color);
		StdDraw.filledCircle(rx, ry, radius);
	}

	public int count() {
		return count;
	}

	public double timeToHit(Particle that) {
		if (this == that)
			return INFINITY;
		double dx = that.rx - this.rx;
		double dy = that.ry - this.ry;
		double dvx = that.vx - this.vx;
		double dvy = that.vy - this.vy;
		double dvdr = dx * dvx + dy * dvy;
		if (dvdr > 0)
			return INFINITY;
		double dvdv = dvx * dvx + dvy * dvy;
		if (dvdv == 0)
			return INFINITY;
		double drdr = dx * dx + dy * dy;
		double sigma = this.radius + that.radius;
		double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);
		// if (drdr < sigma*sigma) StdOut.println("overlapping particles");
		if (d < 0)
			return INFINITY;
		return -(dvdr + Math.sqrt(d)) / dvdv;
	}

	/**
	 * Returns the amount of time for this particle to collide with a vertical wall,
	 * assuming no interening collisions.
	 *
	 * @return the amount of time for this particle to collide with a vertical wall,
	 *         assuming no interening collisions; {@code Double.POSITIVE_INFINITY}
	 *         if the particle will not collide with a vertical wall
	 */
	public double timeToHitVerticalWall() {
		if (vx > 0)
			return (1.0 - rx - radius) / vx;
		else if (vx < 0)
			return (radius - rx) / vx;
		else
			return INFINITY;
	}

	public double timeToHitHorizontalWall() {
		if (vy > 0)
			return (1.0 - ry - radius) / vy;
		else if (vy < 0)
			return (radius - ry) / vy;
		else
			return INFINITY;
	}

	public void bounceOff(Particle that) {
		double dx = that.rx - this.rx;
		double dy = that.ry - this.ry;
		double dvx = that.vx - this.vx;
		double dvy = that.vy - this.vy;
		double dvdr = dx * dvx + dy * dvy; // dv dot dr
		double dist = this.radius + that.radius; // distance between particle centers at collison

		// magnitude of normal force
		double magnitude = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);

		// normal force, and in x and y directions
		double fx = magnitude * dx / dist;
		double fy = magnitude * dy / dist;

		// update velocities according to normal force
		this.vx += fx / this.mass;
		this.vy += fy / this.mass;
		that.vx -= fx / that.mass;
		that.vy -= fy / that.mass;

		// update collision counts
		this.count++;
		that.count++;
	}

	public void bounceOffVerticalWall() {
		vx = -vx;
		count++;
	}

	public void bounceOffHorizontalWall() {
		vy = -vy;
		count++;
	}

	public double kineticEnergy() {
		return 0.5 * mass * (vx * vx + vy * vy);
	}
}