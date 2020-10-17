/**
 * Mian class
 */
public class Main {
    public static void  main(String[] args){

        Expression expr1 = new Expression("a1*a1 + b - 2.5 /( a1 + 2*b) ");
        Expression expr2 = new Expression("2 /0");
        System.out.println(expr1.showResult());
        System.out.println(expr2.showResult());
    }
}
