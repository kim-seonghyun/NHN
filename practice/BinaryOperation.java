package practice;

//2항연산의 규칙.
public interface BinaryOperation {
    //모든 연산은 2항이기 때문에 2항연산이다., 
    public double apply(double x, double y);
}
