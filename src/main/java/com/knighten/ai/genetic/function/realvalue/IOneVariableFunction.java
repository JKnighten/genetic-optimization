package com.knighten.ai.genetic.function.realvalue;

/**
 * A functional interface used to represent a real valued function with one variable.
 *
 * Example Lambdas:
 * (x) arrow Math.pow(x, 2) - f(x)=x^2
 * (x) arrow Math.log(x) - f(x)=log(x)
 * (x) arrow Math.log(1/x) - f(x)=log(1/x)
 * (x) arrow Math.exp(x) - f(x)=e^x
 */
public interface IOneVariableFunction {

    /**
     * Find the output of the function using the supplied x value.
     *
     * @param xValue the value of x
     * @return the about of the function using the supplied x
     */
    double getFuncValue(double xValue);
}
