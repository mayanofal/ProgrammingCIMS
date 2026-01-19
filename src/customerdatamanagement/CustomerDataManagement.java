package customerdatamanagement;

import javax.swing.*;
import java.util.ArrayList;

// Base class
class Person {
    protected String name;
    protected String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

// Inheritance + Encapsulation
class Customer extends Person {
    private String customerID;

    public Customer(String customerID, String name, String email) {
        super(name, email);
        this.customerID = customerID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public String getCustomerInfo() {
        return "ID: " + customerID + ", Name: " + name + ", Email: " + email;
    }
}

// Audit Logger
class AuditLogger {
    private String log;

    public void recordLog(String action) {
        log = "Audit Log: " + action;
    }

    public String getLog() {
        return log;
    }
}

// Main GUI Application with CRUD + Binary Search
public class CustomerDataManagement {

    static ArrayList<Customer> customers = new ArrayList<>();
    static AuditLogger audit = new AuditLogger();

    public static void main(String[] args) {

        JFrame frame = new JFrame("CIMS - Customer Management System");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        // Labels and TextFields
        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setBounds(20, 20, 100, 25);
        panel.add(idLabel);

        JTextField idText = new JTextField();
        idText.setBounds(130, 20, 200, 25);
        panel.add(idText);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 60, 100, 25);
        panel.add(nameLabel);

        JTextField nameText = new JTextField();
        nameText.setBounds(130, 60, 200, 25);
        panel.add(nameText);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 100, 100, 25);
        panel.add(emailLabel);

        JTextField emailText = new JTextField();
        emailText.setBounds(130, 100, 200, 25);
        panel.add(emailText);

        // Output area
        JTextArea output = new JTextArea();
        output.setBounds(20, 240, 440, 180);
        output.setEditable(false);
        panel.add(output);

        // Buttons
        JButton createButton = new JButton("Create");
        createButton.setBounds(20, 150, 100, 25);
        panel.add(createButton);

        JButton readButton = new JButton("Read");
        readButton.setBounds(130, 150, 100, 25);
        panel.add(readButton);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(240, 150, 100, 25);
        panel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(350, 150, 100, 25);
        panel.add(deleteButton);

        // Create
        createButton.addActionListener(e -> {
            if(idText.getText().isEmpty() || nameText.getText().isEmpty() || emailText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!");
                return;
            }

            Customer customer = new Customer(idText.getText(), nameText.getText(), emailText.getText());
            customers.add(customer);
            audit.recordLog("Customer " + customer.getCustomerID() + " added");
            displayAll(output);
            clearFields(idText, nameText, emailText);
        });

        // Read
        readButton.addActionListener(e -> displayAll(output));

        // Update using Binary Search
        updateButton.addActionListener(e -> {
            if(idText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter Customer ID to update!");
                return;
            }

            int index = binarySearchCustomer(idText.getText());
            if(index != -1) {
                Customer c = customers.get(index);
                c.setName(nameText.getText());
                c.setEmail(emailText.getText());
                audit.recordLog("Customer " + c.getCustomerID() + " updated");
            } else {
                JOptionPane.showMessageDialog(frame, "Customer ID not found!");
            }
            displayAll(output);
            clearFields(idText, nameText, emailText);
        });

        // Delete using Binary Search
        deleteButton.addActionListener(e -> {
            if(idText.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter Customer ID to delete!");
                return;
            }

            int index = binarySearchCustomer(idText.getText());
            if(index != -1) {
                Customer c = customers.get(index);
                audit.recordLog("Customer " + c.getCustomerID() + " deleted");
                customers.remove(index);
            } else {
                JOptionPane.showMessageDialog(frame, "Customer ID not found!");
            }
            displayAll(output);
            clearFields(idText, nameText, emailText);
        });

        frame.setVisible(true);
    }

    // Helper method to display all customers
    static void displayAll(JTextArea output) {
        StringBuilder sb = new StringBuilder();
        for(Customer c : customers) {
            sb.append(c.getCustomerInfo()).append("\n");
        }
        sb.append("\n").append(audit.getLog());
        output.setText(sb.toString());
    }

    // Helper method to clear input fields
    static void clearFields(JTextField id, JTextField name, JTextField email) {
        id.setText("");
        name.setText("");
        email.setText("");
    }

    // Binary Search method for Customer by ID
    static int binarySearchCustomer(String id) {
        // Sort customers by ID
        customers.sort((c1, c2) -> c1.getCustomerID().compareTo(c2.getCustomerID()));

        int left = 0;
        int right = customers.size() - 1;

        while(left <= right) {
            int mid = left + (right - left) / 2;
            Customer c = customers.get(mid);
            int cmp = c.getCustomerID().compareTo(id);

            if(cmp == 0) return mid;      // Found
            else if(cmp < 0) left = mid + 1;
            else right = mid - 1;
        }

        return -1; // Not found
    }
}

