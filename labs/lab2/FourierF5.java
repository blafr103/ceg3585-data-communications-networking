public class FourierF5 {

    public static void main(String[] args) {
        double A = 10.0;
        double T = 2.0;
        double w0 = 2 * Math.PI / T;
        int harmonics = 100;
        int steps = 200;
        double tEnd = 2 * T;

        double a0 = 10.0 / Math.PI;    // A/pi, mean of a half-wave rectified sine

        for (int i = 0; i <= steps; i++) {
            double t = tEnd * i / steps;
            double sum = a0;
            for (int n = 1; n <= harmonics; n++) {
                double an = 0.0;
                double bn = 0.0;
                if (n == 1) {
                    bn = 5.0;          // A/2, the single fundamental sine term
                } else if (n % 2 == 0) {
                    an = -20.0 / (Math.PI * (n * n - 1)); // even-harmonic cosine terms
                }
                sum += an * Math.cos(n * w0 * t) + bn * Math.sin(n * w0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
    }
}
