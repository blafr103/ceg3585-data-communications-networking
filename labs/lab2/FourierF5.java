public class FourierF5 {
	
	public static void main(String[] args) {
		
		double a0 = 0.0;
		double an = 0.0;
		double sum = 0.0;
		
		
		for(int n=1;n<=100;n++) {
			
			double bn = (-10*n*Math.PI*Math.sin(1))/(Math.pow(n,2)*Math.pow(Math.PI,2)-1);
			
			sum += a0 + an*Math.cos(n*Math.PI)+bn*Math.sin(n*Math.PI);
		}
		
		System.out.println("The Fourier series up to the 100th harmonic is: "+sum);
		
	}

}
