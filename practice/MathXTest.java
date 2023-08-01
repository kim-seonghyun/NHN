package practice;

public class MathXTest { // name space(충돌을 막기위해 만들어진 명칭공간)

    public static void main(String[] args){
        //Heap (땅) - reference count
        double[] numbers = new double[args.length];
        for (int i=0; i<args.length; i++){
            numbers[i] = Double.valueOf(args[i]);
        }
        System.out.println(Mathx.sum(numbers));
        //anonymous class
        System.out.println(Mathx.reduce(new BinaryOperation(){
            public double apply(double x, double y){
                return x + y;
            }

        }, 0, 1,2,3,4));
        BinaryOperation plus = (x,y)-> x+y;
        BinaryOperation multiply = (x,y)-> x*y;
        System.out.println(Mathx.reduce(plus, 0, 1,2,3,4));
        System.out.println(Mathx.reduce(new BinaryOperation(){
            public double apply(double x, double y){
                return x * y;
            }
        }, 1, 1,2,3,4));
        System.out.println(Mathx.reduce(multiply,1 , 1,2,3,4));
        System.out.println(Mathx.reduceIf(x -> x % 2== 0, (x,y) -> x*y, 1, 1,2,3));
    }
    
}

//항등원, *, + 갈아끼우기
