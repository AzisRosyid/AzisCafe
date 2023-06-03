/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aziscafe;

import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author azisr
 */
public class Cashier extends javax.swing.JFrame {

    private CafeDB db;
    private String name, image, method, bankName, cardNumber, cashierNote;
    private Integer qty;
    private int carbo, protein;
    private double subTotal, grandTotal, cashBack;
    private boolean isPlasticBag, isCredit;
    DefaultTableModel dt;
    /**
     * Creates new form Cashier
     */
    public Cashier() {
        initComponents();
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
            //all cells false
                return false;
            }
        };
        orderTable.setModel(tableModel);
        dt = (DefaultTableModel) orderTable.getModel();
        cashierConsole.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
        Method.cashier = this;
        loadForm();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    public void loadForm() {
        cashierName.setText("Name : " + Method.name);
        cashierPosition.setText("Position : " + Method.position);
        dt.setColumnCount(0);
        String[] colNames = {"No", "Id", "Menu", "Quantity", "Carbo", "Protein", "Price", "Total", "Image"};
        for (String st: colNames) {
            dt.addColumn(st);
        }
        orderTable.getColumn("Image").setMinWidth(0);
        orderTable.getColumn("Image").setMaxWidth(0);
        orderTable.getColumn("Image").setWidth(0);
        orderTable.getColumn("Id").setMinWidth(0);
        orderTable.getColumn("Id").setMaxWidth(0);
        orderTable.getColumn("Id").setWidth(0);
        cashierPlasticBag.setSelected(false); cashierDineIn.setSelected(true); cashierPaymentType.setSelectedIndex(0);
        cashierBankName.setSelectedIndex(0); cashierCardNumber.setText(""); cashierInformation.setText(""); cashierCoupon.setSelectedIndex(0);
        name = ""; image = ""; carbo = 0; protein = 0; subTotal = 0; grandTotal = 0;  method = ""; isPlasticBag = false; isCredit = false; 
        Method.orderMenus = new ArrayList();
        bankName = "None"; cardNumber = ""; Method.cashPay = 0; cashBack = 0;
        loadType();
        loadData();
    }
    
    public void loadData() {
        setEnabled(true);
        dt.setRowCount(0);
        carbo = 0; protein = 0; subTotal = 0;
        int no = 1;
        for (OrderMenu st: Method.orderMenus) {
            if (st.getName().contains(cashierSearch.getText())) {
                Vector v = new Vector();
                v.add(no);
                v.add(st.getId());
                v.add(st.getName());
                v.add(st.getQty());
                v.add(st.getCarbo());
                v.add(st.getProtein());
                v.add(st.getPrice());
                v.add(Method.curr(st.getTotal()));
                v.add(st.getImage());
                dt.addRow(v);
                no++;
            }
            carbo += st.getCarbo() * st.getQty();
            protein += st.getProtein() * st.getQty();
            subTotal += st.getTotal();
        }
        
        loadCashier();
        
        name = ""; qty = null; image = null;
        loadDescription();
    }
    
    private void loadCashier() {
        cashBack = 0; Method.date = LocalDate.now();
        if (cashierDineIn.isSelected()) {
            method = "Dine-in";
            grandTotal = subTotal;
        }
        else if (cashierTakeaway.isSelected()) {
            method = "Takeaway";
            grandTotal = subTotal + subTotal * 1/20;
        }
        else if (cashierDelivery.isSelected()) {
            method = "Delivery";
            grandTotal = subTotal + subTotal * 1/5;
        }
        grandTotal += subTotal * 0.045;
        isPlasticBag = cashierPlasticBag.isSelected();
        if (isPlasticBag)
            grandTotal += 200;
        switch (cashierCoupon.getSelectedIndex()) {
            case 1 -> grandTotal -= subTotal * 1/5;
            case 2 -> cashBack += subTotal * 1/5 <= 20000 ? subTotal * 1/5 : 20000;
            case 3 -> grandTotal -= subTotal * 1/2 <= 50000 ? subTotal * 1/2 : 50000;
            default -> {
            }
        }
        cashierCarbo.setText("Carbo : " + carbo);
        cashierProtein.setText("Protein : " + protein);
        cashierSubTotal.setText("Grand Total : " + Method.curr(grandTotal));
        loadConsole();
    }
    
    private void loadDescription() {
        orderMenu.setText(name);
        orderQty.setText(qty == null ? "" : qty.toString());
        
        try {
            BufferedImage imageFile = ImageIO.read(ClassLoader.getSystemResource(Method.imagePath + image));
            Image scaledImage = imageFile.getScaledInstance(orderImage.getWidth(),orderImage.getHeight(),Image.SCALE_SMOOTH);
            orderImage.setIcon(new ImageIcon(scaledImage));
        } catch (Exception ex) {
            orderImage.setIcon(new ImageIcon(ClassLoader.getSystemResource(Method.imagePath + "nopict.png")));
        }
    }
    
    private void loadType() {
        if (isCredit) {
            cashierBankName.setSelectedItem(bankName);
            cashierCardNumber.setText(cardNumber);
        } else {
            cashierBankName.setSelectedIndex(0);
            cashierCardNumber.setText("");
        }
        cashierBankName.setEnabled(isCredit);
        cashierCardNumber.setEnabled(isCredit);
        cashierPay.setEnabled(!isCredit);
    }
    
    private void centerConsole(boolean value) {
        StyledDocument doc = cashierConsole.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        if (value) {
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        } else {
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_LEFT);
        }
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }
    
    private void loadConsole() {
        if (Method.orderMenus.isEmpty()) {
            centerConsole(false);
            cashierConsole.setText("Menu Order is Empty!\nPlease Add Menu Order...");
            return;
        }
        centerConsole(true);
        int spc = Method.spcKey + Method.spcValue;
        String start = ""; String end = "\n";
        String header = ""; String result = ""; String message = "";
        header += String.format("%s%" + (spc/2 + 9) + "s%" + (spc/2 - 8) + "s", end, "Azis Cafe | Console", end);
        header += start + "=".repeat(spc) + end;
        header += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Date : ", Method.date, end);
        header += start + "=".repeat(spc) + end;
        result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Sub Total :", Method.curr(subTotal), end);
        switch (method) {
            case "Dine-in" -> result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Dine-in (0%) :", Method.curr(subTotal * 0), end);
            case "Takeaway" -> result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Takeaway (5%) :", Method.curr(subTotal * 1/20), end);
            case "Delivery" -> result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Delivery (20%) :", Method.curr(subTotal * 1/5), end);
            default -> {
            }
        }
        result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Tax (4.5%) :", Method.curr(subTotal * 0.045), end);
        if (isPlasticBag)
            result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Plastic Bag :", Method.curr(200d), end);
        if (cashierCoupon.getSelectedIndex() == 1)
            result += start + "-".repeat(spc) + end + String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Discount (20%) :", Method.curr(subTotal * 1/5), end);
        else if (cashierCoupon.getSelectedIndex() == 3)
            result += start + "-".repeat(spc) + end + String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Price Cut (50%) :", Method.curr(subTotal * 1/2 <= 50000 ? subTotal * 1/2 : 50000), end);
        result += start + "-".repeat(spc) + end;
        result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Grand Total :", Method.curr(grandTotal), end);
        if (cashierCoupon.getSelectedIndex() == 2) {
            result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Cashback :", Method.curr(subTotal * 1/5 <= 20000 ? subTotal * 1/5 : 20000), end);
        }
        if (isCredit) {
            if (cashierBankName.getSelectedIndex() != 0) {
                result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Credit :", bankName, end);
                if (!cashierCardNumber.getText().isEmpty()) {
                    if (Method.isNumber(cashierCardNumber.getText())) {
                        if (cashierCardNumber.getText().length() == 16) {
                            result += start + "-".repeat(spc) + end;
                            result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Change :", Method.curr(cashBack), end);
                            message += String.format("%" + -(spc) + "s%s", "Payment Complete!", end);
                        } else {
                            message += String.format("%" + -(spc) + "s%s", "Card Number length must be 16!", end);
                        }
                    } else {
                        message += String.format("%" + -(spc) + "s%s", "Card Number format must be number!", end);
                    } 
                } else {
                    message += String.format("%" + -(spc) + "s%s", "Waiting for Card Number...", end);
                }
            } else {
                message += String.format("%" + -(spc) + "s%s", "Waiting for Payment...", end);
            }
        } else {
            if (Method.cashPay > grandTotal) {
                result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Cash :", Method.curr(Method.cashPay), end);
                result += start + "-".repeat(spc) + end;
                result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Change :", Method.curr(Method.cashPay - grandTotal + cashBack), end);
                message += String.format("%" + -(spc) + "s%s", "Payment Complete!", end);
            } else if (Method.cashPay > 0 && Method.cashPay < grandTotal) {
                result += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Cash :", Method.curr(Method.cashPay), end);
                message += String.format("%" + -(spc) + "s%s", "Insufficient Cash (" + Method.curr(grandTotal - Method.cashPay) + ")...", end);
            } else {
                message += String.format("%" + -(spc) + "s%s", "Waiting for Payment...", end);
            }
        }
        cashierNote = result;
        cashierConsole.setText(header + result + message);
    }
    
    private String loadNote() {
        int spc = Method.spcKey + Method.spcValue;
        String start = ""; String end = "\n";
        String headerNote = "", bottomNote = "";
        headerNote += start + "=".repeat(spc) + end;
        headerNote += String.format("%s%" + (spc/2 + 4) + "s%" + (spc/2 - 3) + "s", start, "Azis Cafe", end);
        headerNote += start + "=".repeat(spc) + end;
        headerNote += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Date : ", Method.date, end);
        headerNote += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Cafe Boss : ", "Azis Rosyid", end);
        headerNote += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, "Cashier : ", Method.name, end);
        headerNote += start + "-".repeat(spc) + end;
        for (OrderMenu st: Method.orderMenus) {
            headerNote += String.format("%s%" + -(Method.spcKey) + "s%" + (Method.spcValue) + "s%s", start, st.getQty() + " " +st.getName(), Method.curr(st.getTotal()), end);
        }
        headerNote += start + "-".repeat(spc) + end;
        bottomNote += start + "=".repeat(spc) + end;
        bottomNote += String.format("%s%" + (spc/2 + 11) + "s%" + (spc/2 - 10) + "s", start, "Thank you for ordering!", end);
        bottomNote += start + "=".repeat(spc);
        return headerNote + cashierNote + bottomNote;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cashierMethod = new javax.swing.ButtonGroup();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        cashierName = new javax.swing.JLabel();
        cashierPosition = new javax.swing.JLabel();
        cashierPosition1 = new javax.swing.JLabel();
        cashierProtein = new javax.swing.JLabel();
        cashierPosition3 = new javax.swing.JLabel();
        cashierSubTotal = new javax.swing.JLabel();
        cashierPosition5 = new javax.swing.JLabel();
        cashierCarbo = new javax.swing.JLabel();
        cashierPosition2 = new javax.swing.JLabel();
        cashierOrderHistory = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        orderImage = new javax.swing.JLabel();
        cashierSearch = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        orderDelete = new javax.swing.JButton();
        orderAdd = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        orderMenu = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        orderUpdate = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cashierDineIn = new javax.swing.JRadioButton();
        cashierDelivery = new javax.swing.JRadioButton();
        cashierTakeaway = new javax.swing.JRadioButton();
        orderQty = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cashierPlasticBag = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        cashierPaymentType = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cashierCardNumber = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cashierBankName = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cashierInformation = new javax.swing.JTextArea();
        cashierPay = new javax.swing.JButton();
        cashierFinish = new javax.swing.JButton();
        cashierClear = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        cashierConsole = new javax.swing.JTextPane();
        cashierCoupon = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Azis Cafe | Cashier");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        cashierName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierName.setForeground(new java.awt.Color(255, 255, 255));
        cashierName.setText("Name");

        cashierPosition.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierPosition.setForeground(new java.awt.Color(255, 255, 255));
        cashierPosition.setText("Position ");

        cashierPosition1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierPosition1.setForeground(new java.awt.Color(255, 255, 255));
        cashierPosition1.setText(" | ");

        cashierProtein.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierProtein.setForeground(new java.awt.Color(255, 255, 255));
        cashierProtein.setText("Protein");

        cashierPosition3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierPosition3.setForeground(new java.awt.Color(255, 255, 255));
        cashierPosition3.setText(" | ");

        cashierSubTotal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierSubTotal.setForeground(new java.awt.Color(255, 255, 255));
        cashierSubTotal.setText("Grand Total ");

        cashierPosition5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierPosition5.setForeground(new java.awt.Color(255, 255, 255));
        cashierPosition5.setText(" | ");

        cashierCarbo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierCarbo.setForeground(new java.awt.Color(255, 255, 255));
        cashierCarbo.setText("Carbo");

        cashierPosition2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierPosition2.setForeground(new java.awt.Color(255, 255, 255));
        cashierPosition2.setText(" | ");

        cashierOrderHistory.setBackground(new java.awt.Color(0, 51, 51));
        cashierOrderHistory.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierOrderHistory.setForeground(new java.awt.Color(255, 255, 255));
        cashierOrderHistory.setText("Order History");
        cashierOrderHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierOrderHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(cashierName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashierPosition1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashierPosition)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashierPosition2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashierOrderHistory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cashierCarbo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashierPosition5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashierProtein)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashierPosition3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashierSubTotal)
                .addGap(16, 16, 16))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashierName)
                    .addComponent(cashierPosition)
                    .addComponent(cashierPosition1)
                    .addComponent(cashierProtein)
                    .addComponent(cashierPosition3)
                    .addComponent(cashierSubTotal)
                    .addComponent(cashierPosition5)
                    .addComponent(cashierCarbo)
                    .addComponent(cashierPosition2)
                    .addComponent(cashierOrderHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cashier");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Azis Cafe");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(28, 28, 28))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(25, 25, 25)
                    .addComponent(jLabel2)
                    .addContainerGap(751, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel2)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        orderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(orderTable);

        orderImage.setBackground(new java.awt.Color(204, 255, 255));
        orderImage.setForeground(new java.awt.Color(204, 255, 255));

        cashierSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierSearchActionPerformed(evt);
            }
        });
        cashierSearch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cashierSearchPropertyChange(evt);
            }
        });
        cashierSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cashierSearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cashierSearchKeyTyped(evt);
            }
        });

        jLabel5.setText("Search :");

        orderDelete.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        orderDelete.setText("Delete");
        orderDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderDeleteActionPerformed(evt);
            }
        });

        orderAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        orderAdd.setText("Add");
        orderAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderAddActionPerformed(evt);
            }
        });

        jLabel6.setText("Menu :");

        orderMenu.setEditable(false);

        jLabel7.setText("Quantity :");

        orderUpdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        orderUpdate.setText("Update");
        orderUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderUpdateActionPerformed(evt);
            }
        });

        jLabel8.setText("Additional :");

        cashierMethod.add(cashierDineIn);
        cashierDineIn.setSelected(true);
        cashierDineIn.setText("Dine-in");
        cashierDineIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierDineInActionPerformed(evt);
            }
        });

        cashierMethod.add(cashierDelivery);
        cashierDelivery.setText("Delivery");
        cashierDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierDeliveryActionPerformed(evt);
            }
        });

        cashierMethod.add(cashierTakeaway);
        cashierTakeaway.setText("Takeaway");
        cashierTakeaway.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierTakeawayActionPerformed(evt);
            }
        });

        jLabel9.setText("Method :");

        cashierPlasticBag.setText("Plastic Bag");
        cashierPlasticBag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierPlasticBagActionPerformed(evt);
            }
        });

        jLabel10.setText("Payment Type :");

        cashierPaymentType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit Card" }));
        cashierPaymentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierPaymentTypeActionPerformed(evt);
            }
        });

        jLabel11.setText("Card Number :");

        cashierCardNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cashierCardNumberKeyReleased(evt);
            }
        });

        jLabel12.setText("Bank Name :");

        cashierBankName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "BRI", "BNI" }));
        cashierBankName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierBankNameActionPerformed(evt);
            }
        });

        jLabel14.setText("Information :");

        cashierInformation.setColumns(20);
        cashierInformation.setRows(5);
        jScrollPane3.setViewportView(cashierInformation);

        cashierPay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierPay.setText("Pay");
        cashierPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierPayActionPerformed(evt);
            }
        });

        cashierFinish.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierFinish.setText("Finish");
        cashierFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierFinishActionPerformed(evt);
            }
        });

        cashierClear.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cashierClear.setText("Clear");
        cashierClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierClearActionPerformed(evt);
            }
        });

        cashierConsole.setEditable(false);
        cashierConsole.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        jScrollPane4.setViewportView(cashierConsole);

        cashierCoupon.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Special Discount 20%", "Cashback 20% Up To Rp20.000,00", "Price Cut 50% Up To Rp50.000,00" }));
        cashierCoupon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashierCouponActionPerformed(evt);
            }
        });

        jLabel13.setText("Coupon :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cashierSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(orderAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(orderDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(orderImage, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(orderUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)
                                            .addComponent(orderMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(orderQty, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jScrollPane4))
                                .addGap(75, 75, 75)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cashierClear, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cashierPay, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cashierFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(11, 11, 11))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cashierDineIn)
                                                .addGap(18, 18, 18)
                                                .addComponent(cashierTakeaway)
                                                .addGap(18, 18, 18)
                                                .addComponent(cashierDelivery))
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel12)
                                            .addComponent(cashierPaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cashierBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11)
                                            .addComponent(cashierCardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cashierPlasticBag)
                                                    .addComponent(jLabel8))
                                                .addGap(26, 26, 26)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel13)
                                                    .addComponent(cashierCoupon, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(0, 59, Short.MAX_VALUE)))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashierSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(orderDelete)
                    .addComponent(orderAdd))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(orderMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cashierDineIn)
                                        .addComponent(cashierTakeaway)
                                        .addComponent(cashierDelivery))))
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(orderQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cashierPlasticBag)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cashierCoupon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cashierPaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addGap(4, 4, 4)
                                .addComponent(cashierBankName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(orderUpdate))))
                    .addComponent(orderImage, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cashierCardNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cashierPay)
                            .addComponent(cashierFinish)
                            .addComponent(cashierClear))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Login login = new Login();
        login.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void cashierSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierSearchActionPerformed

    }//GEN-LAST:event_cashierSearchActionPerformed

    private void cashierSearchPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cashierSearchPropertyChange

    }//GEN-LAST:event_cashierSearchPropertyChange

    private void cashierSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashierSearchKeyReleased
        loadData();
    }//GEN-LAST:event_cashierSearchKeyReleased

    private void cashierSearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashierSearchKeyTyped

    }//GEN-LAST:event_cashierSearchKeyTyped

    private void orderDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderDeleteActionPerformed
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Select menu first!", "Error Table", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Method.orderMenus.remove(row);
        loadData();
    }//GEN-LAST:event_orderDeleteActionPerformed

    private void orderAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderAddActionPerformed
        Menu menu = new Menu();
        menu.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_orderAddActionPerformed

    private void orderUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderUpdateActionPerformed
        int row = orderTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(null, "Select menu first!", "Error Table", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (!Method.isInteger(orderQty.getText())) {
            JOptionPane.showMessageDialog(null, "Quantity format must be Number!", "Error Format", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Method.orderMenus.get(row).setQty(Integer.parseInt(orderQty.getText()));
        loadData();
    }//GEN-LAST:event_orderUpdateActionPerformed

    private void cashierDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierDeliveryActionPerformed
        loadCashier();
    }//GEN-LAST:event_cashierDeliveryActionPerformed

    private void cashierTakeawayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierTakeawayActionPerformed
        loadCashier();
    }//GEN-LAST:event_cashierTakeawayActionPerformed

    private void cashierPlasticBagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierPlasticBagActionPerformed
        loadCashier();
    }//GEN-LAST:event_cashierPlasticBagActionPerformed

    private void cashierDineInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierDineInActionPerformed
        loadCashier();
    }//GEN-LAST:event_cashierDineInActionPerformed

    private void cashierPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierPayActionPerformed
        if (isCredit || Method.orderMenus.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Add Menu Order first!", "Error Cashier", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String input = JOptionPane.showInputDialog(null, "Input Payment Cash (Grand Total : " + Method.curr(grandTotal) + ")", "Payment Message", JOptionPane.PLAIN_MESSAGE);
        if (input == null)
            return;
        if (!Method.isDouble(input)) {
            JOptionPane.showMessageDialog(null, "Input Payment Cash format must be number!", "Error Payment", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (Double.parseDouble(input) <= 0) {
            JOptionPane.showMessageDialog(null, "Input Payment Cash must be more than 0!", "Error Payment", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Method.cashPay = Double.parseDouble(input);
        loadConsole();
    }//GEN-LAST:event_cashierPayActionPerformed

    private void cashierFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierFinishActionPerformed
        if (Method.orderMenus.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Add Menu Order first!", "Error Cashier", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!(!isCredit && Method.cashPay >= grandTotal) && !(isCredit && cashierBankName.getSelectedIndex() != 0 && cashierCardNumber.getText().length() == 16 && Method.isNumber(cashierCardNumber.getText()))) {
            JOptionPane.showMessageDialog(null, "Complete the Payment first!", "Error Payment", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "Are you sure finish the order?", "Confirm Message", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION) {
            db = new CafeDB();
            try {
                String sql = "INSERT INTO order_headers (employee_id, date, method, payment_type, card_number, bank_name, plastic_bag, coupon_id, information, purchase_note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement st = db.getCon().prepareStatement(sql);
                st.setInt(1, Integer.parseInt(Method.id));
                st.setDate(2, Date.valueOf(Method.date) );
                st.setString(3, method);
                st.setString(4, isCredit ? "credit" : "cash");
                st.setString(5, cashierCardNumber.getText().equals("") ? null : cashierCardNumber.getText());
                st.setString(6, cashierBankName.getSelectedItem().toString().equals("None") ? null : cashierBankName.getSelectedItem().toString());
                st.setBoolean(7, isPlasticBag);
                st.setObject(8, cashierCoupon.getSelectedIndex() == 0 ? null : cashierCoupon.getSelectedIndex());
                st.setString(9, cashierInformation.getText().equals("") ? null : cashierInformation.getText());
                st.setString(10, loadNote());
                if (st.executeUpdate() > 0) {
                    Statement s = db.getCon().createStatement();
                    ResultSet rs = s.executeQuery("SELECT id FROM order_headers ORDER BY id DESC LIMIT 1;");
                    Method.orderId = rs.next() ? rs.getInt(1) : 1;
                    sql = "INSERT INTO order_details (order_id, menu_id, quantity) VALUES (?, ?, ?)";
                    st = db.getCon().prepareStatement(sql);
                    for (OrderMenu om: Method.orderMenus) {
                        st.setInt(1, Method.orderId);
                        st.setInt(2, om.getId());
                        st.setInt(3, om.getQty());
                        st.executeUpdate();
                    }
                    st.close();
                    Method.formIndex = 0;
                    OrderNote po = new OrderNote();
                    po.setVisible(true);
                    this.setEnabled(false);
                } else {
                    System.out.println("Insert Data Failed");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Cashier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_cashierFinishActionPerformed

    private void cashierClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierClearActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are your sure clear the Cashier?", "Warning Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            Method.orderMenus = new ArrayList<>();
            loadForm();
        }
    }//GEN-LAST:event_cashierClearActionPerformed

    private void cashierPaymentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierPaymentTypeActionPerformed
        isCredit = cashierPaymentType.getSelectedItem().toString().equals("Credit Card");
        loadType();
        loadCashier();
    }//GEN-LAST:event_cashierPaymentTypeActionPerformed

    private void cashierBankNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierBankNameActionPerformed
        if (isCredit) {
            bankName = cashierBankName.getSelectedItem().toString();
            loadCashier();
        }
    }//GEN-LAST:event_cashierBankNameActionPerformed

    private void cashierCardNumberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashierCardNumberKeyReleased
        if (isCredit) {
            cardNumber = cashierCardNumber.getText();
            loadCashier();
        }
    }//GEN-LAST:event_cashierCardNumberKeyReleased

    private void orderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderTableMouseClicked
        int row = orderTable.getSelectedRow();
        name = orderTable.getValueAt(row, 2).toString();
        qty = Integer.valueOf(orderTable.getValueAt(row, 3).toString());
        image = orderTable.getValueAt(row, 8).toString();
        loadDescription();
    }//GEN-LAST:event_orderTableMouseClicked

    private void cashierCouponActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierCouponActionPerformed
        loadCashier();
    }//GEN-LAST:event_cashierCouponActionPerformed

    private void cashierOrderHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashierOrderHistoryActionPerformed
        OrderHistory oh = new OrderHistory();
        oh.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_cashierOrderHistoryActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cashier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cashier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cashier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cashier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cashier().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cashierBankName;
    private javax.swing.JLabel cashierCarbo;
    private javax.swing.JTextField cashierCardNumber;
    private javax.swing.JButton cashierClear;
    private javax.swing.JTextPane cashierConsole;
    private javax.swing.JComboBox<String> cashierCoupon;
    private javax.swing.JRadioButton cashierDelivery;
    private javax.swing.JRadioButton cashierDineIn;
    private javax.swing.JButton cashierFinish;
    private javax.swing.JTextArea cashierInformation;
    private javax.swing.ButtonGroup cashierMethod;
    private javax.swing.JLabel cashierName;
    private javax.swing.JButton cashierOrderHistory;
    private javax.swing.JButton cashierPay;
    private javax.swing.JComboBox<String> cashierPaymentType;
    private javax.swing.JCheckBox cashierPlasticBag;
    private javax.swing.JLabel cashierPosition;
    private javax.swing.JLabel cashierPosition1;
    private javax.swing.JLabel cashierPosition2;
    private javax.swing.JLabel cashierPosition3;
    private javax.swing.JLabel cashierPosition5;
    private javax.swing.JLabel cashierProtein;
    private javax.swing.JTextField cashierSearch;
    private javax.swing.JLabel cashierSubTotal;
    private javax.swing.JRadioButton cashierTakeaway;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JButton orderAdd;
    private javax.swing.JButton orderDelete;
    private javax.swing.JLabel orderImage;
    private javax.swing.JTextField orderMenu;
    private javax.swing.JTextField orderQty;
    private javax.swing.JTable orderTable;
    private javax.swing.JButton orderUpdate;
    // End of variables declaration//GEN-END:variables
}
