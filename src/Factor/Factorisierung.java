package Factor;

public class Factorisierung {
	public static void main(String[] args) {
		int zuTeilen = (int) (Math.random()*100+1);
		int n = 100000;
		long startt = System.currentTimeMillis();
		for(int i = 0; i< n;i++){
			Factorisierung.factor(zuTeilen);
			zuTeilen += 13;
		}
		long endt = System.currentTimeMillis();
		System.out.println("Total time needed: " + (endt-startt) + "ms");
	}
	public static void factor(int zuTeilen){
		long startt = System.currentTimeMillis();
		int i;
		boolean firstTime = true;

		System.out.print(zuTeilen + " = ");

		for (i = 2; i <= zuTeilen;) {
			if (zuTeilen % i == 0) {
				if (!firstTime) {
					System.out.print(" * ");
				} else {
					firstTime = false;
				}
				System.out.print(i);
				zuTeilen = zuTeilen / i;
			}
			if (zuTeilen % i != 0)
				i++;
		}
		long endt = System.currentTimeMillis();
		System.out.println("\nTime needed: " + (endt-startt) + "ms");
	}
}
