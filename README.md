# Collision Simulator

## An event-driven simulation to depict a system of particles and various phenomena with elastic collisions.

---

This is a simulation that depicts a system of particles and various phenomena like brownian motion, pendulum motion, diffusion, billiards set and much more.

<p>&nbsp;</p>

We can specify the number of particles initially along with attributes like mass, position etc. or just choose a number of particles and the program with specify the values randomly. By altering these values, we can simulate different systems.

<p>&nbsp;</p>
Following is an example of the diffusion simulation:
<p align="center">
    <img align="center" src="/MnsProject/assets/diffusionGif.gif"></img>
</p>

# <p>&nbsp;</p>

## Running the simulation for n random particles

You can run the simulation for n random particles by mentioning n as command line argument as following:

```bash
java Simulation.CollisionSystem 10
```

## Running the simulation for predefined particle systems

The program also takes files that contain initial parameters of particles to simulate different systems including:

- Diffusion
- Brownian Motion
- Pendulum Motion
- Particles at different temperatures
- Billiard balls

It can be run as following:

```bash
cat ..\DataFiles\pendulum.txt | java Simulation.CollisionSystem
```
