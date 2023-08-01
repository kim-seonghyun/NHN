package practice;

//Product 클래스는 BinaryOperation이라는 규칙을 지키도록 계약하겠습니다.
public final class Multiply implements BinaryOperation{

    @Override
    public double apply(double x, double y) {
        // TODO Auto-generated method stub
       return x * y;
    }
    
}
