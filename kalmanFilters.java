/**
 * A simple 1D Kalman Filter implementation
 * 
 * The Kalman filter is an optimal estimator that combines predictions
 * with noisy measurements to estimate the true state of a system.
 */
public class KalmanFilter {
    
    // State estimate
    private double x;
    
    // Estimate uncertainty (covariance)
    private double P;
    
    // Process noise covariance
    private double Q;
    
    // Measurement noise covariance
    private double R;
    
    /**
     * Constructor
     * @param initialEstimate Initial state estimate
     * @param initialCovariance Initial estimate uncertainty
     * @param processNoise Process noise covariance
     * @param measurementNoise Measurement noise covariance
     */
    public KalmanFilter(double initialEstimate, double initialCovariance, 
                        double processNoise, double measurementNoise) {
        this.x = initialEstimate;
        this.P = initialCovariance;
        this.Q = processNoise;
        this.R = measurementNoise;
    }
    
    /**
     * Predict step - estimates the current state
     * @param u Control input (optional, use 0 if no control)
     * @param A State transition factor (usually 1 for simple systems)
     * @param B Control input factor (usually 0 if no control)
     */
    public void predict(double u, double A, double B) {
        // Predict the state
        x = A * x + B * u;
        
        // Predict the covariance
        P = A * P * A + Q;
    }
    
    /**
     * Simplified predict for systems with no control input
     */
    public void predict() {
        predict(0, 1, 0);
    }
    
    /**
     * Update step - corrects the estimate using a measurement
     * @param measurement The observed measurement
     * @param H Measurement function (usually 1)
     */
    public void update(double measurement, double H) {
        // Calculate Kalman Gain
        double K = (P * H) / (H * P * H + R);
        
        // Update estimate with measurement
        x = x + K * (measurement - H * x);
        
        // Update covariance
        P = (1 - K * H) * P;
    }
    
    /**
     * Simplified update assuming direct measurement (H = 1)
     * @param measurement The observed measurement
     */
    public void update(double measurement) {
        update(measurement, 1);
    }
    
    /**
     * Get the current state estimate
     * @return Current state estimate
     */
    public double getEstimate() {
        return x;
    }
    
    /**
     * Get the current estimate uncertainty
     * @return Current covariance
     */
    public double getCovariance() {
        return P;
    }
    
    /**
     * Set the process noise covariance
     * @param Q Process noise covariance
     */
    public void setProcessNoise(double Q) {
        this.Q = Q;
    }
    
    /**
     * Set the measurement noise covariance
     * @param R Measurement noise covariance
     */
    public void setMeasurementNoise(double R) {
        this.R = R;
    }
    
    /**
     * Example usage demonstrating the Kalman filter
     */
    public static void main(String[] args) {
        // Create a Kalman filter
        // Parameters: initial estimate, initial covariance, process noise, measurement noise
        KalmanFilter kf = new KalmanFilter(0, 1, 0.01, 0.1);
        
        // Simulate some noisy measurements (true value is around 10)
        double[] measurements = {9.8, 10.2, 9.9, 10.5, 9.7, 10.1, 10.3, 9.6, 10.0, 10.2};
        
        System.out.println("Kalman Filter Example:");
        System.out.println("----------------------");
        System.out.printf("%-15s %-15s %-15s%n", "Measurement", "Estimate", "Uncertainty");
        
        for (double measurement : measurements) {
            // Predict step (no control input, simple state transition)
            kf.predict();
            
            // Update step with measurement
            kf.update(measurement);
            
            // Display results
            System.out.printf("%-15.4f %-15.4f %-15.6f%n", 
                            measurement, kf.getEstimate(), kf.getCovariance());
        }
        
        System.out.println("\nFinal estimate: " + kf.getEstimate());
    }
}
