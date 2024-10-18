package com.pluralsight.ledgerAccounting;



import com.pluralsight.ledgerAccounting.forms.LedgerAccounting;

import java.io.IOException;
import java.util.Scanner;
import java.util.ResourceBundle;
import java.util.Locale;

import javax.swing.*;


public class Main {
    static Scanner mainScanner = new Scanner(System.in);
    static Ledger ledger;
    static ResourceBundle messages;
    static String transactionsPath;


    public static void main(String[] args) throws IOException {
        selectLanguage();
        ledger = new Ledger(messages,transactionsPath);

        try {
            ledger.loadTransactions(transactionsPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LedgerAccounting ledgerAccounting = new LedgerAccounting();
                ledgerAccounting.setVisible(true);
            }
        });

*/

        ledger.homeScreen(mainScanner);



    }

    private static void selectLanguage(){
        System.out.println(" Select your preferred language / Seleccione su idioma preferido:");
        System.out.println("1. English");
        System.out.println("2. Spanish");

        int chooseLanguage = mainScanner.nextInt();
        mainScanner.nextLine();

        switch (chooseLanguage){
            case 1:setLanguage("en", "US"); //Default to English
                transactionsPath= "./src/main/resources/transactionss.csv";
                break;
            case 2:
                setLanguage("es", "ES"); //Spanish
                transactionsPath= "./src/main/resources/transacciones.csv";
                break;
            default:

        }
    }

    private static void setLanguage(String lang, String country){
        Locale locale = new Locale (lang, country);
        messages = ResourceBundle.getBundle("messages", locale);
    }

}


