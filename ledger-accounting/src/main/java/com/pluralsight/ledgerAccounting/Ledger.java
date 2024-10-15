package com.pluralsight.ledgerAccounting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ledger {
     ArrayList<Transaction> transactions= new ArrayList<>(); // List that store transaction objects
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
     String fileInput = "./src/main/resources/transactions.csv";


     public void loadTransactions (String fileInput) throws IOException {
          BufferedReader fileReader = new BufferedReader(new FileReader(fileInput));
          String line;
          fileReader.readLine(); // skip the header

          while((line= fileReader.readLine()) != null){
               String[] transactionData = line.split("\\|");
               String date = transactionData[0];
               String time= transactionData[1];
               String description = transactionData[2];
               String vendor = transactionData[3];
               double amount = Double.parseDouble(transactionData[4]);
               transactions.add(new Transaction(date,time, description,vendor,amount));

          }
          fileReader.close();
     }

     // method to display all the transactions
     public List<Transaction> getAllTransactions (){
          return transactions;
     }
}


