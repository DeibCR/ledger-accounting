package com.pluralsight.ledgerAccounting;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Ledger ledger = new Ledger();

        try {
            ledger.loadTransactions("./src/main/resources/transactions.csv");
            List<Transaction> allTransactions = ledger.getAllTransactions();
            allTransactions.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
