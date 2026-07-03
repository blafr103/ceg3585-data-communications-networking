public class FourierSeries {

    static final double A = 10.0;
    static final double T = 2.0;
    static final double W0 = 2 * Math.PI / T;   // fundamental angular frequency (= pi here)
    static final int HARMONICS = 100;
    static final int STEPS = 200;
    static final double T_END = 2 * T;          // two periods

    // Running this file alone prints all six curves locally (lab part 1).
    public static void main(String[] args) {
        FourierSeries f = new FourierSeries();
        System.out.print(f.function1());
        System.out.print(f.function2());
        System.out.print(f.function3());
        System.out.print(f.function4());
        System.out.print(f.function5());
        System.out.print(f.function6());
    }

    String function1() {            // square wave
        StringBuilder out = new StringBuilder("# Function 1: square wave\n");
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = 0.0;       // a0 = 0
            for (int n = 1; n <= HARMONICS; n++) {
                double bn = 20.0 * (1 - Math.cos(n * Math.PI)) / (n * Math.PI);
                sum += bn * Math.sin(n * W0 * t);
            }
            out.append(String.format("%.4f,%.6f%n", t, sum));
        }
        return out.toString();
    }

    String function2() {            // rectangular pulse train, duty 0.2
        StringBuilder out = new StringBuilder("# Function 2: rectangular pulse train\n");
        double a0 = 2.0;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double an = (20.0 / (n * Math.PI)) * Math.sin(0.2 * n * Math.PI);
                sum += an * Math.cos(n * W0 * t);
            }
            out.append(String.format("%.4f,%.6f%n", t, sum));
        }
        return out.toString();
    }

    String function3() {            // sawtooth
        StringBuilder out = new StringBuilder("# Function 3: sawtooth wave\n");
        double a0 = 5.0;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double bn = -10.0 / (n * Math.PI);
                sum += bn * Math.sin(n * W0 * t);
            }
            out.append(String.format("%.4f,%.6f%n", t, sum));
        }
        return out.toString();
    }

    String function4() {            // triangle
        StringBuilder out = new StringBuilder("# Function 4: triangular wave\n");
        double a0 = 5.0;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double an = 20.0 * (Math.cos(n * Math.PI) - 1) / (n * n * Math.PI * Math.PI);
                sum += an * Math.cos(n * W0 * t);
            }
            out.append(String.format("%.4f,%.6f%n", t, sum));
        }
        return out.toString();
    }

    String function5() {            // half-wave rectified sine
        StringBuilder out = new StringBuilder("# Function 5: half-wave rectified sine\n");
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
            out.append(String.format("%.4f,%.6f%n", t, sum));
        }
        return out.toString();
    }

    String function6() {            // full-wave rectified sine
        StringBuilder out = new StringBuilder("# Function 6: full-wave rectified sine\n");
        double a0 = 20.0 / Math.PI;
        for (int i = 0; i <= STEPS; i++) {
            double t = T_END * i / STEPS;
            double sum = a0;
            for (int n = 1; n <= HARMONICS; n++) {
                double an = -40.0 / (Math.PI * (4 * n * n - 1));
                sum += an * Math.cos(n * W0 * t);
            }
            out.append(String.format("%.4f,%.6f%n", t, sum));
        }
        return out.toString();
    }
}
