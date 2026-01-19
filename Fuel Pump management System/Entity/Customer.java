package Entity;

public class Customer {
    private String customerName;
    private String phoneNumber;
    private String fuelType;
    private String vehicleType;
    private double amount;
    
    public Customer(String customerName, String phoneNumber, String fuelType, String vehicleType, double amount) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.fuelType = fuelType;
        this.vehicleType = vehicleType;
        this.amount = amount;
    }
    public Customer() {
    }    
    public double calLitre() {
        double pricePerLitre = 0.0;
        
        switch(fuelType.toUpperCase()) {
            case "PETROL":
                pricePerLitre = 118.0;
                break;
            case "OCTANE":
                pricePerLitre = 122.0;
                break;
            case "DIESEL":
                pricePerLitre = 102.0;
                break;
            case "GAS":
                pricePerLitre = 65.13;
                break;
            default:
                throw new IllegalArgumentException("Invalid fuel type: " + fuelType);
        }
        
        return amount / pricePerLitre;
    }
    public static double getFuelPrice(String fuelType) {
        switch(fuelType.toUpperCase()) {
            case "PETROL":
                return 118.0;
            case "OCTANE":
                return 122.0;
            case "DIESEL":
                return 102.0;
            case "GAS":
                return 65.13;
            default:
                throw new IllegalArgumentException("Invalid fuel type: " + fuelType);
        }
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerName() {
        return customerName;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    public String getFuelType() {
        return fuelType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    public String getVehicleType() {
        return vehicleType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %.2f BDT | %.2f L", 
                           customerName, phoneNumber, fuelType, vehicleType, amount, calLitre());
    }
}
