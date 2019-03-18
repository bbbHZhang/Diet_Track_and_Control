package hw1;

public class Test {
	public static void main(String[] args) {

		System.out.printf("%-30.30s %s%n", "asdf","askdjhfkashdf");
		System.out.printf("%-30.30s %s%n", "asdfdsaf","askdjhfkashdf");
	}
}
//Exception in thread "main" java.lang.NumberFormatException: empty String
//at sun.misc.FloatingDecimal.readJavaFormatString(Unknown Source)
//at sun.misc.FloatingDecimal.parseFloat(Unknown Source)
//at java.lang.Float.parseFloat(Unknown Source)
//at hw1.Model.readServingSizes(Model.java:128)
//at hw1.NutriByte.main(NutriByte.java:22)