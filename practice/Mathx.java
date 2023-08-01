package practice;

public class Mathx {
    // product: double... ->double
    // static double <product>(double ... numbers){
    //     double result = <1>;
    //     for(double number : numbers){
    //         result <*>= number;
    //     }
    //     return result;
    // }

    //binaryOperation -> +, * 연산 도움.
    static double reduce(BinaryOperation binaryOperation, double init, double ... numbers){
        double result = init;
        for(double number : numbers){
            result = binaryOperation.apply(result, number);
        }
        return result;
    }

    public double product(double... numbers){
        return reduce(new Multiply(), 1,numbers);
    }
    //numbers변수 -> 미지수, 남이 값을 binding 해줘야 한다.
    static double sum(double ... numbers){
        return Mathx.reduce(new Plus(),0, numbers);
    }

    static double reduceIf(Predicate predicate, BinaryOperation binaryOperation,double init, double ... numbers){
        double result = init;
        for(double number : numbers){
            if (predicate.apply(number)){
                result = binaryOperation.apply(result, number);
            }
        }
        return result;
    }
}
