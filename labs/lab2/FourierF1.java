public class FourierF1 {

	public static void main(String[] args) {

		double A = 10.0;
        double T = 2.0;
        double w0 = 2 * Math.PI / T;   // fundamental angular frequency (= pi here)
        int harmonics = 100;
        int steps = 200;               // samples across the window
        double tEnd = 2 * T;           // two periods, so periodicity is visible

        double a0 = 0.0;               // DC term, added once per sample

        // print the reconstructed curve as "t,f(t)" for pasting into Desmos or a sheet
        for (int i = 0; i <= steps; i++) {
            double t = tEnd * i / steps;
            double sum = a0;
            for (int n = 1; n <= harmonics; n++) {
                double an = 0.0;
                double bn = 20.0 * (1 - Math.cos(n * Math.PI)) / (n * Math.PI); // square wave
                sum += an * Math.cos(n * w0 * t) + bn * Math.sin(n * w0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
}
