import java.math.RoundingMode;
import java.text.NumberFormat;

public class ScaledDecimal {
	private int scale =1;
	private long value=0;
	public ScaledDecimal(long v, int s) {
		value=v;
		scale=s;
	}
	public ScaledDecimal(double v) {
		//String val = String.valueOf(v);
		String val= ((Double) v).toString();
		if(val.contains("E")) {
			boolean negative=false;
			int startindex = val.indexOf('E')+1;
			if(val.charAt(startindex)=='-') {negative=true; startindex+=1;}
			int multiplier = Integer.parseInt(val.substring(startindex));
			
			
			val = val.substring(0,startindex-1);
		}
		String[] parts = val.split("\\.");
		
		scale=parts[1].length();
		value=Long.parseLong(parts[0]+parts[1]);
		
	}
	public int scale() {
		return scale;
	}
	public long value() {
		return value;
	}
	public double doubleValue() {
		return value/Math.pow(10, scale);
	}
	public int charlength() {	
		return String.valueOf(value).length();
	}
	public String toString() {
		return ""+(1.0*value/Math.pow(10, scale));
	}

	public ScaledDecimal add(ScaledDecimal a) {
		
		if(scale>a.scale()) {
			int diff = scale-a.scale();
			long newval = a.value()+Math.round((value/Math.pow(10,diff)));
			return new ScaledDecimal(newval,a.scale());
		}
		else {
			int diff = a.scale()-scale;
			long newval = Math.round((a.value()/Math.pow(10,diff)))+value();
			return new ScaledDecimal(newval,scale);
			
		}
	
	}
	public ScaledDecimal subtract(ScaledDecimal a) {
		return this.add(new ScaledDecimal(-1*a.value(),a.scale()));
	}
	public ScaledDecimal multiply(ScaledDecimal a) {
//		if(scale>a.scale()) {
//			int diff = scale-a.scale();
//			long newval = a.value()*Math.round((value/Math.pow(10,diff)));
//			return new ScaledDecimal(newval,a.scale()*2);
//		}
//		else {
//			int diff = a.scale()-scale;
//			long newval = Math.round((a.value()/Math.pow(10,diff)))*value();
//			return new ScaledDecimal(newval,scale*2);
//			
//		}
		//long newval = a.value()*value;
		return new ScaledDecimal(a.value()*value,scale+a.scale());
		
	}
	public ScaledDecimal multiply(double a) {
		return multiply(new ScaledDecimal(a));
	}
	public ScaledDecimal divide(ScaledDecimal a) {
//		if(scale>a.scale()) {
//			int diff = scale-a.scale();
//			long newval = Math.round((value/Math.pow(10,diff)))/a.value();
//			ScaledDecimal ans = new ScaledDecimal(newval,a.scale()-1);
//			if(ans.charlength()<9) {
//				int lendiff = 9-ans.charlength();
//				String remainder=String.valueOf(Math.round((value/Math.pow(10,diff)))%a.value());
//				remainder= String.valueOf(1.0*Long.parseLong(remainder)/a.value());
//				remainder=remainder.substring(2);
//				if(remainder.length()>lendiff) {
//					remainder = remainder.substring(0,lendiff);
//				}
//				ans.value = Long.parseLong(String.valueOf(ans.value())+remainder);
//				ans.scale += remainder.length();
//				
//			}
//			return ans;
//		}
//		else {
//			int diff = a.scale()-scale;
//			long newval = value/Math.round((a.value()/Math.pow(10,diff)));
//			ScaledDecimal ans = new ScaledDecimal(newval,scale()-1);
//			if(ans.charlength()<9) {
//				int lendiff = 9-ans.charlength();
//				String remainder=String.valueOf(value%Math.round((a.value()/Math.pow(10,diff))));
//				remainder= String.valueOf(1.0*Long.parseLong(remainder)/Math.round((a.value()/Math.pow(10,diff))));
//				remainder=remainder.substring(3);
//				if(remainder.length()>lendiff) {
//					remainder = remainder.substring(0,lendiff);
//				}
//				ans.value = Long.parseLong(String.valueOf(ans.value())+remainder);
//				ans.scale += remainder.length();
//				
//			}
//			return ans;
//		}
//		

		double decimal = 1.0/a.value();
		
		ScaledDecimal dividend = new ScaledDecimal(decimal);
		System.out.println( " "+(dividend));
		dividend.scale=dividend.scale()-a.scale();
		System.out.println( " "+(dividend));
		return this.multiply(dividend);
	}
	public ScaledDecimal divide(double a) {
		return divide(new ScaledDecimal(a));
	}
	
	
}
