import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		while(true) {
			math(new ScaledDecimal(Double.parseDouble(keyboard.nextLine())),new ScaledDecimal(Double.parseDouble(keyboard.nextLine())));
		}
	}
	public static void math(ScaledDecimal A,ScaledDecimal B) {
		double a = A.doubleValue();
		double b = B.doubleValue();
		System.out.println("Add");
		System.out.println(a+b);
		System.out.println(A.add(B));
		System.out.println("Sub");
		System.out.println(a-b);
		System.out.println(A.subtract(B));
		System.out.println("Mult");
		System.out.println(a*b);
		System.out.println(A.multiply(B));
		System.out.println("Div");
		System.out.println(a/b);
		System.out.println(A.divide(B));
		System.out.println("---");
		
	}
}
