package com.pluralsight.ledgerAccounting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

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

     //Method to add a new transaction (deposit) by prompting user
     public void addDeposit(Scanner scanner){
          System.out.println("Please enter the details for the new deposit");

          System.out.println("Date (yyyy-MM-dd): ");
          String date= scanner.nextLine();

          System.out.println("Time (HH:mm:ss): ");
          String time= scanner.nextLine();

          System.out.println("Description: ");
          String description = scanner.nextLine();

          System.out.println("Vendor: ");
          String vendor= scanner.nextLine();

          System.out.println("amount (positive number): ");
          double amount = scanner.nextDouble();
          scanner.nextLine();

          if (amount <= 0){
               System.out.println(" A deposit amount must be positive");
               return;
          }

          //create a new deposit and add to the list
          Transaction deposit = new Transaction(date,time,description,vendor,amount);
          transactions.add(deposit);
          System.out.println("Deposit added successfully");

     }

     //Method to add a new transaction (payment) by prompting user
     public void addPayment(Scanner scanner){
          System.out.println("Please enter the details for the new payment");

          System.out.println("Date (yyyy-MM-dd): ");
          String date= scanner.nextLine();

          System.out.println("Time (HH:mm:ss): ");
          String time= scanner.nextLine();

          System.out.println("Description: ");
          String description = scanner.nextLine();

          System.out.println("Vendor: ");
          String vendor= scanner.nextLine();

          System.out.println("amount (negative number): ");
          double amount = scanner.nextDouble();
          scanner.nextLine();

          if (amount <= 0){
               System.out.println(" A payment amount must be negative");
               return;
          }

          //create a new deposit and add to the list
          Transaction payment = new Transaction(date,time,description,vendor,amount);
          transactions.add(payment);
          System.out.println("Payment added successfully");

     }



     // method to display all the transactions
     public List<Transaction> getAllTransactions (){
          return transactions;
     }


}


