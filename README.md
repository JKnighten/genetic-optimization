# Genetic Optimization

This is a java based implementation of genetic optimization. It is written as a framework so that a variety of problems
can be used by the supplied optimizer. Users simply need to create a class that represents their problem and implement
a small set of methods.

## How To Use

This framework uses gradle to build javadocs and a JAR file containing the framework.

### Building A JAR of The Framework

Execute the following to generate a JAR file containing the framework:

```
./gradlew jar
```

The JAR file can be found in /build/libs/.

### Building The javadocs For The Framework

Execute the following to generate the javadocs for the framework:

```
./gradlew javadoc
```

The javadocs can be found in /out/javadoc/.

### Creating Your Optimization Problem 

The GeneticOptimization class is responsible for performing the optimization of a class that implements
IGenOptimizeProblem.

The IGenOptimizeProblem interface specifies methods that correspond to the steps of the genetic optimization algorithm.
More specifically, the methods represent steps 1-5 found 
[here](https://github.com/JKnighten/genetic-optimization/wiki/Genetic-Optimization---Overview).
IGenOptimizeProblem uses a generic that will represent an individual of the population problem, this generic must extend
the Individual class. You can just use the Individual class without extendning it if you like, but extending it and 
creating your own toString() and constructor can be beneficial. The Individual class simply contains the individual's
 fitness and genes(data that represents the individual).

The GeneticOptimizationParams class is used to provide parameters used in the genetic optimization algorithm that
GeneticOptimization uses. Possible genetic optimization parameters:
- Max Generation - The max number of generations created
- Selection Percentage - The percentage of best individuals used for generating the next generation
- Mutation Probability - The chance a single gene will be mutated
- Population Size - The number of individuals in each generation
- Target Value(Optional) - The fitness value which will stop the optimization early


GeneticOptimization's solve() method will start the optimization process. The solve() method will return an ordered list
containing the best individual from each generation produced.

To summarize, in order to perform genetic optimization:
1. Create a class that implements the IGenOptimizeProblem interface which represents the optimization problem
2. If desired create an class that extends Individual that represents the individuals of the problem
3. Create an instance of the optimization problem
4. Create an instance of GeneticOptimizationParams with desired optimization parameters
5. Create an instance of GeneticOptimization supplying it with the problem and parameter instances
6. Call the solve() method of GeneticOptimization to start optimizing

A quick abstract example on how to use the optimizer:

```java
// Create A Problem Instance
IGenOptimizeProblem problem = new SomeProblem();
        
// Create Optimization Parameter Object
GeneticOptimizationParams params = new GeneticOptimizationParams(1000, 5000, .05, .01);
params.setTargetValue(0.0);

// Create An Optimizer
GeneticOptimization optimizer = new GeneticOptimization(problem, params);

// Perform Optimization
List<Individual> optimizationGeneration = optimizer.optimize();
```

## Example Optimization Problem Implementation

A couple of example optimization problems have been implemented and packaged with this framework. Demo runs can be found
in their associated IGenOptimizeProblem class.

Also here is a small writeup about the steps followed to create a problem class - [link](https://github.com/JKnighten/genetic-optimization/wiki/Example:-Creating-An-Optimization-Problem).

### String Matching

Starting with a set of random strings, generate a desired target string using the supplied random strings. The algorithm
will keep generating strings that become closer to the target until the target is found.

### N Queens Problem

Given a N x N chess board containing N queens(restrict a single queen per column for simplicity), find an arrangement of
queens on the board such that no queen is in conflict. Queens are in conflict if they may take another, that is if two 
queens are in the same row or are diagonal of one another.

### N Queens Problem - Parallel

Same at the above problem but written using Java 1.8's parallel streams. The work done in each generation(fitness 
calculation, selection, crossing, and mutation) is performed in a parallel stream. This is useful when the population
size is large or if N is large.

### Minimizing/Maximizing A One Variable Real Valued Function

Given an one variable real valued function(R->R ex. f(x)=x, f(x)=x^2, f(x)=log(x)) find the maximum/minimum of the
function. You could use this to minimize/maximize a simple function or something more complicated like a loss function
for a machine learning model.


## Possible Future Work

* Early stopping if there is minimal change between generations fitness
* Optimization Problem - Find largest non-overlapping circle that can be placed in a finite region that contains other
 circles
* Optimization Problem - Find the arrangement of rectangles in a finite area that gives the most free space


