# Genetic Optimization

This is a java based implementation of genetic optimization. It is written in such a way that users implement the
steps of the genetic optimization algorithm and the optimizer handles running the optimization.

## About Genetic Optimization

Genetic optimization is a algorithm that is inspired by the process of biological evolution. Generally speaking, 
biological evolution is the process in which individuals mate with one another, which leads to the creation of new 
individuals who have a mixture of their parents genes. The individuals that are more likely to mate are
those who have genes that are more beneficial for surviving (the strong live and are able to mate with others that are
strong). Since the strong are more likely to mate, the genes inherited by offspring should have a higher chance of also
being beneficial for surviving. So as generations of individuals go by, the genes that are beneficial will likely be 
passed down to the new generations.

The generic steps of the genetic optimization algorithm are the following:
1. Generate an initial population of individuals
2. Calculate the fitness of the individuals in the population
3. Select the best individuals in the population to be used for "reproduction"
4. Choose pairs of individuals and cross their genes together to create the next generation's individuals
5. Randomly mutate the genes of the newly created individuals
6. Go back to step 2 until stopping condition is met

Now some notes about important aspects of the above steps.

### Fitness
We need some way to measure how beneficial the genes of an individual are. This fitness measure is what is being
 optimized, that is the goal is to the genes that optimize fitness.

### Individual Selection
The best individuals of a population should be used to create the next generation. In this implementation a specified 
percentage of the best individuals are used for the creation of the next generation while the others are not considered.

### Generating New Individuals/Crossing Genes
Individuals should be randomly paired together(individuals can be paired multiple times) and then their genes should be
crossed to make the genes of a new individual. A simple scheme for pairing individuals can be the random selection of
two individuals where all individuals have an equal chance of being selected. A more complicated scheme can make the 
chance of being selected proportional to the individual's fitness scores(individuals with better genes are more likely 
to reproduce). Genes are crossed by selecting a random crossing point in their genes. Everything from the beginning of 
the first individual's genes to the crossing point are used as the first half of the new individual's genes. Then 
everything from the crossing point to the end of the second individual's genes are used as the second half of the new 
individual's genes.


### Mutations
Genes of newly created individuals are mutated to add more diversity to the gene pool. In this implementation
a specified probability is provided which is used to decide if a gene should be mutated.


### Stopping Conditions
There are a few stopping condition that are popular with genetic optimization: when a specified number of generations 
are created, when a specified fitness goal is reached, and when there are minimal changes between generations fitness.
In this implementation the available stopping conditions are: specifying a max number of generations to be created and
specifying a fitness goal. Specifying a fitness goal makes sense when there is a max/min achievable fitness.




## How To Use

The GeneticOptimization class is responsible for conducting the optimization of an instance of a class that implements
IGenOptimizeProblem. IGenOptimizeProblem interface requires methods that represent steps 1-5 from the genetic
optimization algorithm and an extra method to get the best individual in the current population. IGenOptimizeProblem
uses a generic that will represent an individual of the population problem, this generic must extend the Individual
class. You can just use the Individual class if you like, but extending it and creating your own toString() and
constructor can be beneficial. The Individual class simply contains the individual's fitness and genes.

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

Below are some implemented optimization problems to follow as examples.

## Implemented Optimization Problems

### String Matching

Starting with random strings, generate a desired target string using these random strings. The initial population will
be a collection of random strings. These strings then are used in the genetic optimization algorithm to create strings
closer to the target until the target string is generated.

### N Queens Problem

Given a N x N chess board containing N queens(restrict a single queen per column for simplicity), find an arrangement
queens on the board such that no queen is in conflict. Queens are in conflict if they may take another, that is if two 
queens are in the same row or are diagonal of one another.

### N Queens Problem - Parallel

Same at the above problem but written using Java 1.8's parallel streams. The work done in each generation(fitness 
calculation, selection, crossing, and mutation) is performed in parallel. This is useful when the population size is
large or if N is large.


## Possible Future Work

* Early stopping if there is minimal change between generations fitness
* Optimization Problem - Find largest noo-overlapping circle that can be placed in a finite region that contains other
 circles
* Optimization Problem - Find the arrangement of rectangles in a finite area that gives the most free space


