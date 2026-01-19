package GUI;

import Entity.*;
import FileIO.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class PumpManagementSystemPage extends JFrame implements ActionListener {

    private final Color PRIMARY_COLOR = new Color(33, 47, 61);   
    private final Color SECONDARY_COLOR = new Color(52, 152, 219); 
    private final Color ACCENT_COLOR = new Color(46, 204, 113);    
    private final Color BG_COLOR = new Color(236, 240, 241);    
    
    private final Color FORM_BG = new Color(235, 245, 251);     
    private final Color INVENTORY_BG = new Color(232, 248, 245); 
    private final Color PRICE_BG = Color.WHITE;                
    private final Color CARD_BG = Color.WHITE;

    private JTextField nameField, phoneField, amountField;
    private JRadioButton petrolBtn, octaneBtn, dieselBtn, gasBtn;
    private JComboBox<String> vehicleCombo;
    private ButtonGroup fuelGroup;
    private JButton confirmBtn, refillBtn, calculateBtn;
    private JLabel litreLabel, revenueLabel;
    private JLabel petrolInvLabel, octaneInvLabel, dieselInvLabel, gasInvLabel;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    private double petrolInventory, octaneInventory, dieselInventory, gasInventory;

    public PumpManagementSystemPage() {
        super("Fuel Pump Management System");
        setupFrame();
        initializeInventory();
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0; gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 0, 0, 5);
        centerPanel.add(createCustomerForm(), gbc);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        rightPanel.setOpaque(false);
        rightPanel.add(createInventoryPanel());
        rightPanel.add(createPricePanel());
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 5, 0, 0);
        centerPanel.add(rightPanel, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(1100, 250));
        bottomPanel.add(createHistoryPanel(), BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        
        updateInventoryDisplay();
        loadCustomerHistory();
        updateRevenueDisplay();
    }

    private void setupFrame() {
        setSize(1250, 900);
        setMinimumSize(new Dimension(1100, 850));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setOpaque(false);
        
        try {
            ImageIcon logoIcon = new ImageIcon("pic/fuel_pump.png");
            Image img = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            titlePanel.add(new JLabel(new ImageIcon(img)));
        } catch (Exception e) {}

        JLabel title = new JLabel("FUEL PUMP MANAGEMENT SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        header.add(titlePanel, BorderLayout.WEST);

        revenueLabel = new JLabel("Total Revenue: 0.00 BDT");
        revenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        revenueLabel.setForeground(ACCENT_COLOR);
        header.add(revenueLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createCustomerForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FORM_BG); 
        panel.setBorder(createTitledBorder(" Transaction Details "));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Customer Name:"), gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Phone Number:"), gbc);
        phoneField = new JTextField(15);
        gbc.gridx = 1; panel.add(phoneField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Fuel Type:"), gbc);
        JPanel fuelPanel = new JPanel(new GridLayout(2, 2));
        fuelPanel.setOpaque(false);
        petrolBtn = new JRadioButton("Petrol"); petrolBtn.setOpaque(false);
        octaneBtn = new JRadioButton("Octane"); octaneBtn.setOpaque(false);
        dieselBtn = new JRadioButton("Diesel"); dieselBtn.setOpaque(false);
        gasBtn = new JRadioButton("Gas"); gasBtn.setOpaque(false);
        fuelGroup = new ButtonGroup();
        fuelGroup.add(petrolBtn); fuelGroup.add(octaneBtn); fuelGroup.add(dieselBtn); fuelGroup.add(gasBtn);
        fuelPanel.add(petrolBtn); fuelPanel.add(octaneBtn); fuelPanel.add(dieselBtn); fuelPanel.add(gasBtn);
        gbc.gridx = 1; panel.add(fuelPanel, gbc);

        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Vehicle Type:"), gbc);
        vehicleCombo = new JComboBox<>(new String[]{"Car", "Bus", "Truck", "Cng", "Bike"});
        gbc.gridx = 1; panel.add(vehicleCombo, gbc);

        gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("Amount (BDT):"), gbc);
        amountField = new JTextField(15);
        gbc.gridx = 1; panel.add(amountField, gbc);

        gbc.gridy = 5; gbc.gridx = 0; panel.add(new JLabel("Calculated Litres:"), gbc);
        litreLabel = new JLabel("0.00 L");
        litreLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        litreLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 1; panel.add(litreLabel, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        calculateBtn = createStyledButton("Calculate", new Color(52, 152, 219), SECONDARY_COLOR); 
        confirmBtn = createStyledButton("Confirm", new Color(46, 204, 113), ACCENT_COLOR);       
        btnPanel.add(calculateBtn);
        btnPanel.add(confirmBtn);
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(btnPanel, gbc);

        calculateBtn.addActionListener(this);
        confirmBtn.addActionListener(this);

        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(INVENTORY_BG);
        panel.setBorder(createTitledBorder(" Inventory Status "));

        JPanel labelsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        labelsPanel.setOpaque(false);
        labelsPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        
        petrolInvLabel = new JLabel("Petrol: 0.00 L");
        octaneInvLabel = new JLabel("Octane: 0.00 L");
        dieselInvLabel = new JLabel("Diesel: 0.00 L");
        gasInvLabel = new JLabel("Gas: 0.00 L");
        
        Font invFont = new Font("Segoe UI", Font.BOLD, 15);
        petrolInvLabel.setFont(invFont); octaneInvLabel.setFont(invFont);
        dieselInvLabel.setFont(invFont); gasInvLabel.setFont(invFont);
        
        labelsPanel.add(petrolInvLabel); labelsPanel.add(octaneInvLabel);
        labelsPanel.add(dieselInvLabel); labelsPanel.add(gasInvLabel);
        
        panel.add(labelsPanel, BorderLayout.CENTER);

        refillBtn = createStyledButton("Refill All Tanks", new Color(231, 76, 60), new Color(255, 165, 0)); // Orange text
        refillBtn.addActionListener(this);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        southPanel.add(refillBtn);
        panel.add(southPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPricePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRICE_BG);
        panel.setBorder(createTitledBorder(" Fuel Prices "));

        JLabel priceLabel = new JLabel();
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        
        try {
            ImageIcon priceIcon = new ImageIcon("pic/Pic.png.png");
            Image img = priceIcon.getImage();
            Image scaledImg = img.getScaledInstance(400, 180, Image.SCALE_SMOOTH);
            priceLabel.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            priceLabel.setText("Price chart not found");
        }
        
        panel.add(priceLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(createTitledBorder(" Transaction History "));

        String[] columns = {"Name", "Phone", "Fuel", "Vehicle", "Amount", "Litres"};
        tableModel = new DefaultTableModel(columns, 0);
        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(25);
        historyTable.getTableHeader().setBackground(PRIMARY_COLOR);
        historyTable.getTableHeader().setForeground(Color.BLUE);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)), 
            title, TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 14), PRIMARY_COLOR);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }

    private void initializeInventory() {
        try {
            double[] inventory = FileIO.readFuelInventory();
            petrolInventory = inventory[0];
            octaneInventory = inventory[1];
            dieselInventory = inventory[2];
            gasInventory = inventory[3];
        } catch (IOException e) {
            petrolInventory = octaneInventory = dieselInventory = gasInventory = 5000.0;
        }
    }

    private void updateInventoryDisplay() {
        petrolInvLabel.setText(String.format("Petrol: %.2f L", petrolInventory));
        octaneInvLabel.setText(String.format("Octane: %.2f L", octaneInventory));
        dieselInvLabel.setText(String.format("Diesel: %.2f L", dieselInventory));
        gasInvLabel.setText(String.format("Gas: %.2f L", gasInventory));
        
        petrolInvLabel.setForeground(petrolInventory < 500 ? Color.RED : PRIMARY_COLOR);
        octaneInvLabel.setForeground(octaneInventory < 500 ? Color.RED : PRIMARY_COLOR);
        dieselInvLabel.setForeground(dieselInventory < 500 ? Color.RED : PRIMARY_COLOR);
        gasInvLabel.setForeground(gasInventory < 500 ? Color.RED : PRIMARY_COLOR);
    }

    private void updateRevenueDisplay() {
        try {
            double totalRevenue = FileIO.calculateTotalRevenue();
            revenueLabel.setText(String.format("Total Revenue: %.2f BDT", totalRevenue));
        } catch (IOException e) {
            revenueLabel.setText("Revenue: Error");
        }
    }

    private void loadCustomerHistory() {
        try {
            List<Customer> customers = FileIO.readAllCustomers();
            tableModel.setRowCount(0);
            for (Customer c : customers) {
                tableModel.addRow(new Object[]{
                    c.getCustomerName(), c.getPhoneNumber(), c.getFuelType(),
                    c.getVehicleType(), String.format("%.2f", c.getAmount()),
                    String.format("%.2f", c.calLitre())
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateBtn) {
            calculateLitres();
        } else if (e.getSource() == confirmBtn) {
            confirmTransaction();
        } else if (e.getSource() == refillBtn) {
            refillTanks();
        }
    }

    private String getSelectedFuelType() {
        if (petrolBtn.isSelected()) return "PETROL";
        if (octaneBtn.isSelected()) return "OCTANE";
        if (dieselBtn.isSelected()) return "DIESEL";
        if (gasBtn.isSelected()) return "GAS";
        return null;
    }

    private void calculateLitres() {
        try {
            String fuel = getSelectedFuelType();
            if (fuel == null) throw new Exception("Select fuel type");
            double amount = Double.parseDouble(amountField.getText());
            double litres = amount / Customer.getFuelPrice(fuel);
            litreLabel.setText(String.format("%.2f L", litres));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
        }
    }

    private void confirmTransaction() {
        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String fuel = getSelectedFuelType();
            String vehicle = (String) vehicleCombo.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());

            if (name.isEmpty() || !phone.matches("\\d{11}") || fuel == null) {
                throw new Exception("Fill all fields (Phone: 11 digits)");
            }

            Customer customer = new Customer(name, phone, fuel, vehicle, amount);
            double required = customer.calLitre();

            if (!hasEnoughFuel(fuel, required)) throw new Exception("Insufficient fuel!");

            updateInventory(fuel, required);
            FileIO.saveCustomer(customer);
            FileIO.saveFuelInventory(petrolInventory, octaneInventory, dieselInventory, gasInventory);

            updateInventoryDisplay();
            updateRevenueDisplay();
            loadCustomerHistory();
            clearForm();

            JOptionPane.showMessageDialog(this, "Transaction Successful!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private boolean hasEnoughFuel(String fuel, double req) {
        switch(fuel) {
            case "PETROL": return petrolInventory >= req;
            case "OCTANE": return octaneInventory >= req;
            case "DIESEL": return dieselInventory >= req;
            case "GAS": return gasInventory >= req;
            default: return false;
        }
    }

    private void updateInventory(String fuel, double req) {
        switch(fuel) {
            case "PETROL": petrolInventory -= req; break;
            case "OCTANE": octaneInventory -= req; break;
            case "DIESEL": dieselInventory -= req; break;
            case "GAS": gasInventory -= req; break;
        }
    }

    private void clearForm() {
        nameField.setText(""); phoneField.setText(""); amountField.setText("");
        fuelGroup.clearSelection(); vehicleCombo.setSelectedIndex(0);
        litreLabel.setText("0.00 L");
    }

    private void refillTanks() {
        if (JOptionPane.showConfirmDialog(this, "Refill all tanks to 5000L?") == JOptionPane.YES_OPTION) {
            try {
                petrolInventory = octaneInventory = dieselInventory = gasInventory = 5000.0;
                FileIO.saveFuelInventory(5000, 5000, 5000, 5000);
                updateInventoryDisplay();
                JOptionPane.showMessageDialog(this, "Inventory Refilled!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}
