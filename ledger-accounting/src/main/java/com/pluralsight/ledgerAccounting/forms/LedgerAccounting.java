package com.pluralsight.ledgerAccounting.forms;

import com.pluralsight.ledgerAccounting.Transaction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LedgerAccounting extends JFrame {


    private JPanel panelMain;
    private JButton accountingLedgerButton;
    private JButton addAPaymentButton;
    private JButton addADepositButton;


    public LedgerAccounting(){
        setTitle("Accounting Ledger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panelMain);
        setSize(750,550);
        setLocationRelativeTo(null);
        setVisible(true);

        ArrayList<Transaction> transactions = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String fileInput = "./src/main/resources/transactions.csv";


        addADepositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                print("Listener event");
            }
        });
        addAPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                print("Listener event");
            }
        });
        accountingLedgerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                print("Listener event");
            }
        });
    }

    private void print(String message) {
        System.out.println(message);
    }



}
