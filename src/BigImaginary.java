import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigImaginary {
	private final BigDecimal r; //real number
	private final BigDecimal im; //imaginary number
	public BigImaginary(BigDecimal real, BigDecimal imaginary) {
		r=real;
		im=imaginary;
	}
	public BigImaginary(double real, double imaginary) {
		r=new BigDecimal(real);
		im=new BigDecimal(imaginary);
	}
	//Object methods
	public BigDecimal realpart() {
		return r;
	}
	public BigDecimal imaginarypart() {
		return im;
	}
	
	public String toString() {
		return "("+String.valueOf(r)+" + "+String.valueOf(im)+"i)";
	}
	
	
	//Static operation functions
	public static BigImaginary add(BigImaginary first, BigImaginary second) {	
		return new BigImaginary(first.realpart().add(second.realpart(),MathContext.DECIMAL128),first.imaginarypart().add(second.imaginarypart(),MathContext.DECIMAL128));
		
	}
	public static BigImaginary multiply(BigImaginary f, BigImaginary s) {
		/*(1+4i)(5+i)
		 * = (1)(5) + (1)(i)
		 * + (4i)(5) + (4i)(i)
		 * _____________________
		 * 5 + i + 20i + 4i²
		 * = 5 + 21i + 4i²
		 * = 1 + 21i
		 */
		BigDecimal r1 = f.realpart().multiply(s.realpart(),MathContext.DECIMAL128);
		BigDecimal i1 = f.realpart().multiply(s.imaginarypart(),MathContext.DECIMAL128);
		
		BigDecimal i2 = f.imaginarypart().multiply(s.realpart(),MathContext.DECIMAL128);
		BigDecimal isq = f.imaginarypart().multiply(s.imaginarypart(),MathContext.DECIMAL128);
		
		i1 = i1.add(i2,MathContext.DECIMAL128);
		r1 = r1.subtract(isq,MathContext.DECIMAL128);
		
		return new BigImaginary(r1,i1);
	}
	public static BigImaginary divide(BigImaginary f, BigDecimal s) {
		return new BigImaginary(f.realpart().divide(s,MathContext.DECIMAL128), f.imaginarypart().divide(s,MathContext.DECIMAL128));
	}
	public static BigImaginary divide(BigDecimal f, BigImaginary s) {
		return new BigImaginary(f.divide(s.realpart(),MathContext.DECIMAL128), f.divide(s.imaginarypart(),MathContext.DECIMAL128));
	}
}
