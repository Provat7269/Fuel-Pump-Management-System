package FileIO;

import Entity.*;
import java.io.*;
import java.util.*;

public class FileIO {
    private static final String DATABASE_DIR = "Database";
    private static final String DATABASE_FILE = DATABASE_DIR + File.separator + "customers.txt";
    private static final String FUEL_INVENTORY_FILE = DATABASE_DIR + File.separator + "fuel_inventory.txt";
    
    static {
        File dbDir = new File(DATABASE_DIR);
        if (!dbDir.exists()) {
            if (!dbDir.mkdirs()) {
                System.err.println("Warning: Could not create Database directory");
            }
        }
    }

    public static void saveCustomer(Customer customer) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE, true))) {
            writer.write(customer.getCustomerName() + "," + 
                        customer.getPhoneNumber() + "," + 
                        customer.getFuelType() + "," + 
                        customer.getVehicleType() + "," + 
                        customer.getAmount() + "," + 
                        String.format("%.2f", customer.calLitre()));
            writer.newLine();
        } catch (IOException e) {
            throw new IOException("Error saving customer data: " + e.getMessage());
        }
    }
    
    public static List<Customer> readAllCustomers() throws IOException {
        List<Customer> customers = new ArrayList<>();
        File file = new File(DATABASE_FILE);
        
        if (!file.exists()) {
            return customers;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        Customer customer = new Customer(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            Double.parseDouble(parts[4].trim())
                        );
                        customers.add(customer);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Warning: Skipping corrupted line " + lineNumber + ": " + line);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading customer data: " + e.getMessage());
        }
        
        return customers;
    }
    
    public static double calculateTotalRevenue() throws IOException {
        double totalRevenue = 0.0;
        List<Customer> customers = readAllCustomers();
        
        for (Customer customer : customers) {
            totalRevenue += customer.getAmount();
        }
        
        return totalRevenue;
    }
    public static void saveFuelInventory(double petrol, double octane, double diesel, double gas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FUEL_INVENTORY_FILE))) {
            writer.write(String.format("%.2f,%.2f,%.2f,%.2f", petrol, octane, diesel, gas));
        } catch (IOException e) {
            throw new IOException("Error saving fuel inventory: " + e.getMessage());
        }
    }
    
    public static double[] readFuelInventory() throws IOException {
        double[] inventory = {5000.0, 5000.0, 5000.0, 5000.0}; 
        File file = new File(FUEL_INVENTORY_FILE);
        
        if (!file.exists()) {
            saveFuelInventory(5000.0, 5000.0, 5000.0, 5000.0);
            return inventory;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            double value = Double.parseDouble(parts[i].trim());
                            if (value >= 0) {
                                inventory[i] = value;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Warning: Invalid inventory value at index " + i + ", using default");
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Error reading fuel inventory, using defaults: " + e.getMessage());
        }
        
        return inventory;
    }
    public static void resetFuelInventory() throws IOException {
        saveFuelInventory(5000.0, 5000.0, 5000.0, 5000.0);
    }
}
