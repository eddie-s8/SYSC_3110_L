import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {
    private AddressBook addressBook;
    private JLabel status;
    private JList<BuddyInfo> buddyList;
    private DefaultListModel<BuddyInfo> modelList;

    public GUI() {
        this.addressBook = new AddressBook();
        this.modelList = new DefaultListModel<>();
        initializeGUI();

    }

    private void initializeGUI() {
        setTitle("AddressBook manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        status = new JLabel();

        createMenuBar();
        setupMainPanel();
        setupStatusBar();
        updateBuddyList();
    }
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu addressBookMenu = new JMenu("AddressBook");

        JMenuItem newAddressBookItem = new JMenuItem("New AddressBook");
        JMenuItem displayBuddyItem = new JMenuItem("Display Buddy");
        JMenuItem exportAddressBookItem = new JMenuItem("Export AddressBook");
        JMenuItem importAddressBookItem = new JMenuItem("Import AddressBook");
        JMenuItem serializeAddressBookItem = new JMenuItem("Serialize AddressBook");
        JMenuItem deserializeAddressBookItem = new JMenuItem("Deserialize AddressBook");

        newAddressBookItem.addActionListener(e -> createNewAddressBook());
        displayBuddyItem.addActionListener(e -> displayBuddy());
        exportAddressBookItem.addActionListener(e -> exportAddressBook());
        importAddressBookItem.addActionListener(e -> importAddressBook());
        serializeAddressBookItem.addActionListener(e -> serilializeAddressBook());
        deserializeAddressBookItem.addActionListener(e -> deserializeAddressBook());

        addressBookMenu.add(newAddressBookItem);
        addressBookMenu.add(displayBuddyItem);
        addressBookMenu.addSeparator();
        addressBookMenu.add(exportAddressBookItem);
        addressBookMenu.add(importAddressBookItem);
        addressBookMenu.addSeparator();
        addressBookMenu.add(serializeAddressBookItem);
        addressBookMenu.add(deserializeAddressBookItem);

        JMenu buddyInfoMenu = new JMenu("BuddyInfo");

        JMenuItem addBuddyItem = new JMenuItem("Add Buddy");
        JMenuItem removeBuddyItem = new JMenuItem("Remove Buddy");

        addBuddyItem.addActionListener(e -> addBuddy());
        removeBuddyItem.addActionListener(e -> removeBuddy());

        buddyInfoMenu.add(addBuddyItem);
        buddyInfoMenu.add(removeBuddyItem);

        menuBar.add(addressBookMenu);
        menuBar.add(buddyInfoMenu);

        setJMenuBar(menuBar);

    }

    private void setupMainPanel() {
        setLayout(new BorderLayout());

        buddyList = new JList<>(modelList);
        buddyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buddyList.setFont(new Font("Times New Roman", Font.BOLD, 16));
        buddyList.setVisible(true);

        buddyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e){
                if (!e.getValueIsAdjusting()){
                    BuddyInfo selected = buddyList.getSelectedValue();
                    if (selected != null){
                        setStatus("selected: " + selected.getName());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(buddyList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Buddy List"));
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addBuddyButton = new JButton("Add Buddy");
        JButton removeBuddyButton = new JButton("Remove Buddy");

        addBuddyButton.addActionListener(e -> addBuddy());
        removeBuddyButton.addActionListener(e -> removeBuddy());


        buttonPanel.add(addBuddyButton);
        buttonPanel.add(removeBuddyButton);

        JPanel main = new JPanel(new BorderLayout());
        main.add(scrollPane, BorderLayout.CENTER);
        main.add(buddyList, BorderLayout.NORTH);

        add(main, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.PAGE_END);

    }

    public void setupStatusBar(){
        status = new JLabel("ready");
        status.setFont(new Font("Times New Roman", Font.BOLD, 18));
        status.setBorder(BorderFactory.createLineBorder(Color.black));
        status.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(status, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.PAGE_START);
    }

    private void createNewAddressBook() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to start a new address book? the current one will be deleted" ,
                    "New address book",
                        JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            addressBook = new AddressBook();
            updateBuddyList();
            setStatus("New address book created");
        }
    }

    private void displayBuddy() {
        if (modelList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "address book has no buddies currently",
                    "Display Buddy",
                    JOptionPane.INFORMATION_MESSAGE);

        } else  {
            int selectedIndex = buddyList.getSelectedIndex();
            if (selectedIndex == -1) {
                StringBuilder details = new StringBuilder();
                details.append("All buddies information: \n\n");
                for (int i = 0; i < modelList.size(); i++) {
                    BuddyInfo buddy = modelList.getElementAt(i);
                    details.append("Name: ").append(buddy.getName()).append("\n");
                    details.append("Address: ").append(buddy.getAddress()).append("\n");
                    details.append("Phone: ").append(buddy.getPhoneNumber()).append("\n");
                    details.append("-----------------------------------------\n");
                }
                JOptionPane.showMessageDialog(this, details.toString(), "Display Buddy", JOptionPane.INFORMATION_MESSAGE);
            } else {
                BuddyInfo selectedBuddy = buddyList.getSelectedValue();
                String details = selectedBuddy.getName() + " " + selectedBuddy.getAddress() + " "  + selectedBuddy.getPhoneNumber();
                JOptionPane.showMessageDialog(this, details, "Display Buddy", JOptionPane.INFORMATION_MESSAGE);
            }

        }
        setStatus("display buddy information");
    }

    private void addBuddy() {

        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneNumberField = new JTextField();

        JPanel buddyPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        buddyPanel.add(new JLabel("Name:"));
        buddyPanel.add(nameField);
        buddyPanel.add(new JLabel("Address:"));
        buddyPanel.add(addressField);
        buddyPanel.add(new JLabel("Phone Number:"));
        buddyPanel.add(phoneNumberField);

        int choice = JOptionPane.showConfirmDialog(this,
                buddyPanel,
                "add new buddy",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (choice == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();

            int phoneNum = Integer.parseInt(phoneNumber);
            BuddyInfo newBuddy = new BuddyInfo(name, address, phoneNum);
            addressBook.addBuddy(newBuddy);
            updateBuddyList();
            setStatus("buddy added: " + name);
        }
    }

    private void removeBuddy() {
        int selectedIndex = buddyList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select which buddy for removal",
                    "no buddy chose",
                    JOptionPane.WARNING_MESSAGE);
            return;


        }

         BuddyInfo buddyR = buddyList.getSelectedValue();
         String buddyName = buddyR.getName();


         int choice = JOptionPane.showConfirmDialog(this,
                 "are you sure you want to remive this buddy: " + buddyName,
                 "remove buddy",
                 JOptionPane.YES_NO_OPTION,
                 JOptionPane.QUESTION_MESSAGE);
         if (choice == JOptionPane.YES_OPTION) {
                 addressBook.removeBuddy(selectedIndex);
                 updateBuddyList();
                 setStatus("buddy removed: " + buddyName);
         } else {
                 JOptionPane.showMessageDialog(this,
                         "Buddy not found",
                         "error",
                         JOptionPane.ERROR_MESSAGE);
             }
        }

        public void exportAddressBook() {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("export address book");
            chooser.setApproveButtonText("export");

            int input =chooser.showSaveDialog(this);
            if (input == JFileChooser.APPROVE_OPTION) {
                java.io.File f = chooser.getSelectedFile();

                try {
                    addressBook.save(f.getAbsolutePath());

                    JOptionPane.showMessageDialog(this, "Address book saved to: " + f.getAbsolutePath(), "Export success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Export error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        public void importAddressBook() {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("import address book");
            chooser.setApproveButtonText("import");

            int input =chooser.showOpenDialog(this);
            if (input == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                System.out.println("File: " + f.getAbsolutePath());

                try {
                    AddressBook importBook = AddressBook.importAddressBook(f.getAbsolutePath());
                    this.addressBook = importBook;
                    updateBuddyList();
                    JOptionPane.showMessageDialog(this, "Address book imported: " + f.getAbsolutePath(), "Import success", JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Import error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        private void serilializeAddressBook() {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("serialize address book");
            chooser.setApproveButtonText("serialize");

            int input =chooser.showSaveDialog(this);
            if (input == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".ser")) {
                    f = new File(f.getAbsolutePath() + ".ser");
                }
                try {
                    addressBook.serializeAddressBook(f.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Address book serialized: " + f.getAbsolutePath(), "Serialize success", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Serialize error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        private void deserializeAddressBook() {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("deserialize address book");
            chooser.setApproveButtonText("deserialize");

            int input =chooser.showSaveDialog(this);
            if (input == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();

                try {
                    AddressBook deserializeBook = AddressBook.deserializeAddressBook(f.getAbsolutePath());
                    this.addressBook = deserializeBook;
                    updateBuddyList();

                    JOptionPane.showMessageDialog(this, "Address book deserialized: " + f.getAbsolutePath(), "Deserialize success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Deserialize error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }


    private void setStatus(String statusX) {
        status.setText(statusX);
    }

    private void updateBuddyList() {
        modelList.clear();
        for (BuddyInfo buddy : addressBook.getBuddies()) {
            modelList.addElement(buddy);
        }
        setStatus("buddy list updated");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }
}
