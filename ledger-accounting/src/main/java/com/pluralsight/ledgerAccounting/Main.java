package com.pluralsight.ledgerAccounting;



import java.io.IOException;
import java.util.Scanner;
import java.util.ResourceBundle;
import java.util.Locale;


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


        ledger.homeScreen(mainScanner);



    }

    private static void selectLanguage(){
        System.out.println(" Select your preferred language / Seleccione su idioma preferido:");
        System.out.println("1. English");
        System.out.println("2. Spanish");

        int chooseLanguage = mainScanner.nextInt();
        mainScanner.nextLine();

        switch (chooseLanguage){
            case 2:
                setLanguage("es", "ES"); //Spanish
                transactionsPath= "./src/main/resources/transacciones.csv";
                break;
            default:
                setLanguage("en", "US"); //Default to English
                transactionsPath= "./src/main/resources/transactions.csv";
        }
    }

    private static void setLanguage(String lang, String country){
        Locale locale = new Locale (lang, country);
        messages = ResourceBundle.getBundle("messages", locale);
    }

}


