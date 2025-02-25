package admin;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.*;
import database.DBConnect;

public class HandleStaffRequests extends JFrame {

    JPanel buttonPanel;
    static JPanel contentPanel;
    JButton showNewRequestsButton, showMonthwiseRequestsButton, backButton;  // Back button
    static JTable newRequestsTable;
    JTable monthwiseRequestsTable;
    DefaultTableModel newRequestsTableModel, monthwiseRequestsTableModel;

    public static List<Object[]> getrequests() {
        List<Object[]> requests = new ArrayList<>();

        String query = "SELECT request_id, staff_id, name, leave_date, reason, response from staff_requests WHERE is_viewed=FALSE";
        try (Connection con = DBConnect.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(new Object[]{
                        rs.getInt("request_id"),
                        rs.getString("staff_id"),
                        rs.getString("name"),
                        rs.getDate("leave_date"),
                        rs.getString("reason"),
                        rs.getString("response"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static void markRequest(int requestid) {
        String updatequery = "UPDATE staff_requests SET is_viewed=TRUE , response=? WHERE request_id=?";
        try (Connection con = DBConnect.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(updatequery);
            stmt.setString(1,"Accepted");
            stmt.setInt(2, requestid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HandleStaffRequests() {
        // Frame setup
        setTitle("Manage Requests");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Button panel with back button
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10)); // Align buttons to the right side
        backButton = new JButton("Back");
        showNewRequestsButton = new JButton("Show New Requests");
        showMonthwiseRequestsButton = new JButton("Show Monthwise Requests");

        // Add back button and other buttons to the panel
       
        buttonPanel.add(showNewRequestsButton);
        buttonPanel.add(showMonthwiseRequestsButton);
        buttonPanel.add(backButton);

        // Content panel
        contentPanel = new JPanel(new CardLayout());

        // Monthwise Requests Panel
        JPanel monthwiseRequestsPanel = new JPanel(new BorderLayout());

        // Dropdown for selecting month
        JPanel monthSelectionPanel = new JPanel(new FlowLayout());
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(java.time.LocalDate.now().getMonthValue() - 1); // Default to current month
        monthSelectionPanel.add(new JLabel("Select Month:"));
        monthSelectionPanel.add(monthComboBox);

        // Add month selection panel to monthwise requests panel
        monthwiseRequestsPanel.add(monthSelectionPanel, BorderLayout.NORTH);

        // Create the table for displaying requests
        monthwiseRequestsTableModel = new DefaultTableModel(new Object[]{"Staff-ID","Staff Name", "Month", "Accepted Requests"}, 0);
        monthwiseRequestsTable = new JTable(monthwiseRequestsTableModel);
        monthwiseRequestsPanel.add(new JScrollPane(monthwiseRequestsTable), BorderLayout.CENTER);

        // Add panels to content panel
        contentPanel.add(new JPanel(), "NewRequests"); // Placeholder for new requests
        contentPanel.add(monthwiseRequestsPanel, "MonthwiseRequests");

        // Add components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Show the "Monthwise Requests" panel by default
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "MonthwiseRequests");

        // Button actions
        showNewRequestsButton.addActionListener(e -> {
            loadNewRequests();
            cardLayout.show(contentPanel, "NewRequests");
        });

        showMonthwiseRequestsButton.addActionListener(e -> {
            // Fetch monthwise requests when the button is clicked
            loadMonthwiseRequests((String) monthComboBox.getSelectedItem());
            cardLayout.show(contentPanel, "MonthwiseRequests");
        });
        
        backButton.addActionListener(e->{
        	new MainMenu();
        	dispose();
        });

        // Add a listener to update the table when a new month is selected
        monthComboBox.addActionListener(e -> loadMonthwiseRequests((String) monthComboBox.getSelectedItem()));

       
        

        setVisible(true);
    }

    // Function to load new requests when the button is clicked
    private void loadNewRequests() {
        // Create and display the new requests table only when needed
        if (newRequestsTable == null) {
            newRequestsTable = createNewRequestsTable(); // Create the table
        }

        // Add the new requests table to the panel
        JPanel newRequestsPanel = new JPanel(new BorderLayout());
        newRequestsPanel.add(new JScrollPane(newRequestsTable), BorderLayout.CENTER);

        // Replace the placeholder panel with the new requests panel
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        contentPanel.add(newRequestsPanel, "NewRequests"); // Add it to the content panel
        cardLayout.show(contentPanel, "NewRequests"); // Show the new requests panel
    }

    // Function to create the New Requests table
    static JTable createNewRequestsTable() {
        String[] columns = {"RequestID", "StaffID", "Name", "Date", "Reason", "Response", "Action", ""};
        List<Object[]> requests = getrequests();
//        if(requests==null) {
//        	JOptionPane.showMessageDialog(null, "NO New Requests");
//        	return null;
//        }
        DefaultTableModel tble = new DefaultTableModel(columns, 0);
        for (Object[] row : requests) {
            tble.addRow(row);
        }

        JTable table = new JTable(tble);

        // Accept button renderer
        table.getColumnModel().getColumn(6).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel acceptLabel = new JLabel("Accept");
                acceptLabel.setOpaque(true);
                acceptLabel.setBackground(isSelected ? Color.LIGHT_GRAY : new Color(0, 128, 128));
                acceptLabel.setForeground(Color.WHITE);
                acceptLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
                return acceptLabel;
            }
        });

        // Reject button renderer
        table.getColumnModel().getColumn(7).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel rejectLabel = new JLabel("Reject");
                rejectLabel.setOpaque(true);
                rejectLabel.setBackground(isSelected ? Color.LIGHT_GRAY : Color.RED);
                rejectLabel.setForeground(Color.WHITE);
                rejectLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                return rejectLabel;
            }
        });

        // Add mouse listener to handle "Accept" and "Reject" actions
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == 6) {  // Accept button clicked
                    int requestId = (Integer) table.getValueAt(row, 0);
                    markRequest(requestId);
                    ((DefaultTableModel) table.getModel()).removeRow(row);
                }

                if (col == 7) {  // Reject button clicked
                    int requestId = (Integer) table.getValueAt(row, 0);
                    markRequest(requestId);

                    // Switch to the rejection response card layout
                    showRejectionCard(requestId, row);
                }
            }
        });

        return table;
    }

    // Function to load and display monthwise requests with accepted responses
    private void loadMonthwiseRequests(String selectedMonth) {
    	String query = "SELECT staff_id, name, EXTRACT(MONTH FROM leave_date) AS month, COUNT(*) AS requests " +
                "FROM staff_requests WHERE EXTRACT(MONTH FROM leave_date) = ? AND response = 'Accepted' GROUP BY staff_id, name, month";

        try (Connection con = DBConnect.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, getMonthNumber(selectedMonth));
            ResultSet rs = stmt.executeQuery();

            monthwiseRequestsTableModel.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                monthwiseRequestsTableModel.addRow(new Object[]{
                        rs.getString("staff_id"),
                        rs.getString("name"),
                        selectedMonth,
                        rs.getInt("requests")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Convert month name to its corresponding number
    private int getMonthNumber(String monthName) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(monthName)) {
                return i + 1;
            }
        }
        return -1; // Default fallback if not found
    }

    // Function to show rejection card
    private static void showRejectionCard(int requestId, int row) {
    	   JFrame rejectionFrame = new JFrame("Rejection Reason");
        // Create the panel for the rejection card layout with a BorderLayout
        JPanel rejectionPanel = new JPanel(new BorderLayout());

        // Create a panel to hold the JTextArea and restrict its size
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JTextArea rejectionResponseArea = new JTextArea(10, 10);
        rejectionResponseArea.setLineWrap(true);
        rejectionResponseArea.setWrapStyleWord(true);
        rejectionResponseArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Set preferred size to make the JTextArea medium-sized
        rejectionResponseArea.setPreferredSize(new Dimension(200, 50));

        // Add the JTextArea inside the textPanel
        textPanel.add(rejectionResponseArea);

        // Create a JScrollPane for the JTextArea
        JScrollPane scrollPane = new JScrollPane(rejectionResponseArea);
        textPanel.add(scrollPane);

        JButton saveButton = new JButton("Save Response");
        saveButton.setMaximumSize(new Dimension(300, 300));
        textPanel.add(Box.createVerticalStrut(50));
        textPanel.add(saveButton);

        // Add the textPanel to the rejectionPanel
        rejectionPanel.add(textPanel, BorderLayout.CENTER);

        // Button action listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rejectionResponse = rejectionResponseArea.getText().trim();
                if (!rejectionResponse.isEmpty()) {
                    // Save rejection response to the database
                    saveResponse(requestId, rejectionResponse);
                    rejectionFrame.dispose();

                    // Remove the row from the new requests table
                    DefaultTableModel tableModel = (DefaultTableModel) newRequestsTable.getModel();
                    tableModel.removeRow(row);

                    // Redirect to the New Requests page
                    ((CardLayout) contentPanel.getLayout()).show(contentPanel, "NewRequests");
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a rejection reason.");
                }
            }
        });

        // Create a frame to display the rejection card
     
        rejectionFrame.setSize(400, 300);
        rejectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rejectionFrame.add(rejectionPanel);
        rejectionFrame.setLocationRelativeTo(null);
        rejectionFrame.setVisible(true);
    }

    // Save rejection response to the database
    private static void saveResponse(int requestId, String rejectionResponse) {
        String updateQuery = "UPDATE staff_requests SET response=? WHERE request_id=?";
        try (Connection con = DBConnect.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(updateQuery);
            stmt.setString(1,"Rejected: "+ rejectionResponse);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
