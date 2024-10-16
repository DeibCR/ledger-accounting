package com.pluralsight.ledgerAccounting;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static Scanner mainScanner = new Scanner(System.in);
    static Ledger ledger = new Ledger();


    public static void main(String[] args) throws IOException {

        try {
            ledger.loadTransactions("./src/main/resources/transactions.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ledger.homeScreen(mainScanner);

    }



}


