public class FourierF4 {

    public static void main(String[] args) {
        double A = 10.0;
        double T = 2.0;
        double w0 = 2 * Math.PI / T;
        int harmonics = 100;
        int steps = 200;
        double tEnd = 2 * T;

        double a0 = 5.0;               // A/2 (was -5.0)

        for (int i = 0; i <= steps; i++) {
            double t = tEnd * i / steps;
            double sum = a0;
            for (int n = 1; n <= harmonics; n++) {
                // triangle: numerator uses cos, not sin (that was the bug)
                double an = 20.0 * (Math.cos(n * Math.PI) - 1) / (n * n * Math.PI * Math.PI);
                double bn = 0.0;
                sum += an * Math.cos(n * w0 * t) + bn * Math.sin(n * w0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
    }
}
