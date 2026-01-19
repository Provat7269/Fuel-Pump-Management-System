import GUI.PumpManagementSystemPage;
import javax.swing.*;

public class Start {
    public static void main(String[] args) {
    
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel");
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                PumpManagementSystemPage frame = new PumpManagementSystemPage();
                frame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Failed to start application:\n" + e.getMessage(),
                    "Startup Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}