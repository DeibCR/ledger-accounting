package com.pluralsight.ledgerAccounting;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner mainScanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Ledger ledger = new Ledger();
        homeScreen();

        /*
        try {
            ledger.loadTransactions("./src/main/resources/transactions.csv");
            List<Transaction> allTransactions = ledger.getAllTransactions();
            allTransactions.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

         */

    }


    private static void homeScreen () {
        boolean counter = true; //Loop control variable
        while (counter) {
            System.out.println("""
                    ===========================================
                       Welcome to the accounting application
                    ===========================================
                          Please select an option
                           D- Add Deposit
                           P- Make a Payment
                           L- Ledger
                           R- Reports
                           X- Exit
                    ==========================================
                    """);
            String option = mainScanner.nextLine().trim();

            switch (option.toUpperCase()) {
                case "D":
                    break;
                case "P":
                    break;
                case "L":
                    break;
                case "R":
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
