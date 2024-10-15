package com.pluralsight.ledgerAccounting;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static Scanner mainScanner = new Scanner(System.in);
    static Ledger ledger = new Ledger();
    static String fileInput = "./src/main/resources/transactions.csv";

    public static void main(String[] args) throws IOException {


        try {
            ledger.loadTransactions("./src/main/resources/transactions.csv");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        homeScreen();

    }


    private static void homeScreen() {
        boolean counter = true; //Loop control variable
        while (counter) {
            System.out.println("""
                    ===========================================
                       Welcome to the accounting application
                    ===========================================
                          Please select an option
                           D- Add Deposit
                           P- Add a Payment
                           L- Ledger book
                           X- Exit
                    ==========================================
                    """);
            String option = mainScanner.nextLine().trim();

            switch (option.toUpperCase()) {
                case "D":
                    ledger.addDeposit(mainScanner, fileInput);

                    break;
                case "P":
                    ledger.addPayment(mainScanner, fileInput);
                    break;
                case "L":
                    ledger.ledgerScreen(mainScanner);
                    break;
                case "X":
                    System.out.println("Exiting the Accounting Application");
                    counter = false;
                    break;
                default:
                    System.out.println("Invalid option. Please type 'D' 'P' 'L' 'R' or 'X'");
            }
        }
    }
}


