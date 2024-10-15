package com.pluralsight.ledgerAccounting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

     //method that saves each new transaction to the csv file
     public void saveTransactions (Transaction newTransaction, String fileInput) throws IOException{
          BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileInput,true)); //The true value append just the new transaction to the file

          String line= String.format("%s|%s|%s|%s|%.2f", //write the transaction in the file in the same format
                  newTransaction.getDate(),
                  newTransaction.getTime(),
                  newTransaction.getDescription(),
                  newTransaction.getVendor(),
                  newTransaction.getAmount());

          fileWriter.write(line);
          fileWriter.newLine();
          fileWriter.close();

     }

     //Method to add a new transaction (deposit) by prompting user
     public void addDeposit(Scanner scanner, String fileInput){
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

          try{
               saveTransactions(deposit,fileInput);
               System.out.println("Deposit added and saved successfully");
          } catch (IOException e){
               System.out.println("Error saving the transaction: "+ e.getMessage());
          }

     }

     //Method to add a new transaction (payment) by prompting user
     public void addPayment(Scanner scanner, String fileInput){
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

          try{
               saveTransactions(payment, this.fileInput);
               System.out.println("Payment added and saved successfully");
          } catch (IOException e){
               System.out.println("Error saving the transaction: "+ e.getMessage());
          }

     }

     public void ledgerScreen


     // method to display all the transactions
     public List<Transaction> getAllTransactions (){
          return transactions;
     }


}


