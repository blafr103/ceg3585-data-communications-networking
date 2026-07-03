public class FourierSeries {
	
	public static void main(String[] args) {
		
	}

	String function1(){
		double a0 = 0.0;
	    	double an = 0.0;
	    	double sum = 0.0;
	    
	    	for (int n = 1; n <= 100; n++) {

	      		double bn = (-20 * Math.cos(n * Math.PI)) / (n * Math.PI);
	      		sum += a0 + an*Math.cos(n*Math.PI)+bn*Math.sin(n*Math.PI);
	    	}
	    
	    	return("The Fourier series up to the 100th harmonic is: " + sum);

	}

	String function2(){
		double a0 = 2.0;
		double bn = 0.0;
		double sum = 0.0;
		
		for(int n=1;n<=100;n++) {

			double an = Math.sin(n*Math.PI)/(n*Math.PI);
			sum += a0 + an*Math.cos(n*Math.PI)+bn*Math.sin(n*Math.PI);
		}
		
		return("The Fourier series up to the 100th harmonic is: "+sum);

	}

	String function3(){

		double a0 = 5.0;
	   	double an = 0.0;
		double sum = 0.0;
			
		for(int n=1;n<=100;n++) {
				
	    	double bn = (-10 / (n * Math.PI));
			sum += a0 + an*Math.cos(n*Math.PI)+bn*Math.sin(n*Math.PI);		
		}
			
		return("The Fourier series up to the 100th harmonic is: "+sum);

	}

	String function4(){
		double a0 = -5.0;
		double bn = 0.0;
		double sum = 0.0;
		
		for(int n=1;n<=100;n++) {
			
			double an = (20*Math.sin(n*Math.PI)-20)/(Math.pow(n,2)*Math.pow(Math.PI,2));
			sum += a0 + an*Math.cos(n*Math.PI)+bn*Math.sin(n*Math.PI);
		}
		
		return("The Fourier series up to the 100th harmonic is: "+sum);

	}

	String function5(){
		double a0 = 0.0;
		double an = 0.0;
		double sum = 0.0;
		
		
		for(int n=1;n<=100;n++) {
			
			double bn = (-10*n*Math.PI*Math.sin(1))/(Math.pow(n,2)*Math.pow(Math.PI,2)-1);
			sum += a0 + an*Math.cos(n*Math.PI)+bn*Math.sin(n*Math.PI);
		}
		
		return("The Fourier series up to the 100th harmonic is: "+sum);

	}

	String function6(){
		double a0 = 0.0;
    		double an = 0.0;
		double bn = 0.0;
		double sum = 0.0;
		
		
		for(int n=1;n<=100;n++) {
					
			sum += a0 + an*Math.cos(n*Math.PI)+bn*Math.sin(n*Math.PI);
		}
		
		return("The Fourier series up to the 100th harmonic is: "+sum);

	}
}
