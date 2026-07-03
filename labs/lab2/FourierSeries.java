public class FourierSeries {

    static final double A = 10.0;
    static final double T = 2.0;
    static final double W0 = 2 * Math.PI / T;
    static final int HARMONICS = 100;
    static final int STEPS = 200;
    static final double T_END = 2 * T;

    public static void main(String[] args) {
        function1();
        function2();
        function3();
        function4();
        function5();
        function6();
    }

    static void function1() {           // square wave
        System.out.println("# Function 1: square wave");
        double a0 = 0.0;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double bn = 20.0 * (1 - Math.cos(n * Math.PI)) / (n * Math.PI);
                sum += bn * Math.sin(n * W0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
    }

    static void function2() {           // rectangular pulse train, duty 0.2
        System.out.println("# Function 2: rectangular pulse train");
        double a0 = 2.0;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double an = (20.0 / (n * Math.PI)) * Math.sin(0.2 * n * Math.PI);
                sum += an * Math.cos(n * W0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
    }

    static void function3() {           // sawtooth
        System.out.println("# Function 3: sawtooth wave");
        double a0 = 5.0;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double bn = -10.0 / (n * Math.PI);
                sum += bn * Math.sin(n * W0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
    }

    static void function4() {           // triangle
        System.out.println("# Function 4: triangular wave");
        double a0 = 5.0;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double an = 20.0 * (Math.cos(n * Math.PI) - 1) / (n * n * Math.PI * Math.PI);
                sum += an * Math.cos(n * W0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
    }

    static void function5() {           // half-wave rectified sine
        System.out.println("# Function 5: half-wave rectified sine");
        double a0 = 10.0 / Math.PI;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double an = 0.0;
                double bn = 0.0;
                if (n == 1) {
                    bn = 5.0;
                } else if (n % 2 == 0) {
                    an = -20.0 / (Math.PI * (n * n - 1));
                }
                sum += an * Math.cos(n * W0 * t) + bn * Math.sin(n * W0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
    }

    static void function6() {           // full-wave rectified sine
        System.out.println("# Function 6: full-wave rectified sine");
        double a0 = 20.0 / Math.PI;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double an = -40.0 / (Math.PI * (4 * n * n - 1));
                sum += an * Math.cos(n * W0 * t);
            }
            System.out.printf("%.4f,%.6f%n", t, sum);
        }
    }
}
