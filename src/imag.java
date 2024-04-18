
public class imag {
	private final double r; //real number
	private final double im; //imaginary number
	public imag(double real, double imaginary) {
		r=real;
		im=imaginary;
	}
	//Object methods
	public double realpart() {
		return r;
	}
	public double imaginarypart() {
		return im;
	}
	
	public String toString() {
		return "("+String.valueOf(r)+" + "+String.valueOf(im)+"i)";
	}
	
	
	//Static operation functions
	public static imag add(imag first, imag second) {	
		return new imag(first.realpart()+second.realpart(),first.imaginarypart()+second.imaginarypart());
		
	}
	public static imag multiply(imag f, imag s) {
		/*(1+4i)(5+i)
		 * = (1)(5) + (1)(i)
		 * + (4i)(5) + (4i)(i)
		 * _____________________
		 * 5 + i + 20i + 4i²
		 * = 5 + 21i + 4i²
		 * = 1 + 21i
		 */
//		double r1 = f.realpart()*s.realpart();
//		double i1 = f.realpart()*s.imaginarypart();
//		
//		double i2 = f.imaginarypart()*s.realpart();
//		double isq = f.imaginarypart()*s.imaginarypart();
//		
//		i1 = i1+i2;
//		r1 = r1+(-isq);
		return new imag((f.realpart()*s.realpart())-(f.imaginarypart()*s.imaginarypart()),(f.realpart()*s.imaginarypart())+(f.realpart()*s.imaginarypart()));
	}
	public static imag divide(imag f, double s) {
		return new imag(f.realpart()/s, f.imaginarypart()/s);
	}
	public static imag divide(double f, imag s) {
		return new imag(f/s.realpart(), f/s.imaginarypart());
	}
}
