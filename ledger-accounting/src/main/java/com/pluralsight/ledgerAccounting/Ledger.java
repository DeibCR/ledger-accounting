package com.pluralsight.ledgerAccounting;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Ledger {
    private ArrayList<Transaction> transactions = new ArrayList<>();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String fileInput = "./src/main/resources/transactions.csv";


    public void homeScreen(Scanner scanner) {
        boolean counter = true;
        while (counter) {     //loop to keep the program running until user exit the program
            System.out.println("""
                    ===========================================
                       Welcome to the accounting application
                    ===========================================
                        Please type an option to access
                           D- Add Deposit
                           P- Add a Payment
                           L- Ledger book
                           X- Exit
                    ==========================================
                    """);
            String option = scanner.nextLine().trim();

            switch (option.toUpperCase()) {  // to UpperCase to manage error in typo
                case "D":
                    registerDeposit(scanner, fileInput);

                    break;
                case "P":
                    registerPayment(scanner, fileInput);
                    break;
                case "L":
                    ledgerScreen(scanner);
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


    public void loadTransactions(String fileInput) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(fileInput));
        String line;
        fileReader.readLine();

        while ((line = fileReader.readLine()) != null) {
            String[] transactionData = line.split("\\|");
            LocalDate date = LocalDate.parse(transactionData[0], dateFormatter);
            LocalTime time = LocalTime.parse(transactionData[1], timeFormatter);
            String description = transactionData[2];
            String vendor = transactionData[3];
            double amount = Double.parseDouble(transactionData[4]);
            transactions.add(new Transaction(date, time, description, vendor, amount));

        }
        fileReader.close();
    }


    public void saveTransactions(Transaction newTransaction, String fileInput) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileInput, true));

        String line = String.format("%s|%s|%s|%s|%.2f",
                newTransaction.getDate(),
                newTransaction.getTime(),
                newTransaction.getDescription(),
                newTransaction.getVendor(),
                newTransaction.getAmount());

        fileWriter.write(line);
        fileWriter.newLine();
        fileWriter.close();

    }


    public  void registerDeposit(Scanner scanner, String fileInput) {
        System.out.println("Please enter the details for the new deposit");

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS); // This helps eliminate te error of extra decimals in time

        System.out.println("Date (using current date):  " + localDate);
        System.out.println("Time (using current time):  " + localTime);


        System.out.println("Description: ");
        String description = scanner.nextLine();

        System.out.println("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("amount (positive number): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println(" A deposit amount must be positive");
            return;
        }



        Transaction deposit = new Transaction(localDate, localTime, description, vendor, amount);
        transactions.add(deposit);

        try {
            saveTransactions(deposit, fileInput);
            System.out.println("Deposit added and saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving the transaction");
            e.printStackTrace();
        }

    }


    public void registerPayment(Scanner scanner, String fileInput) {
        System.out.println("Please enter the details for the new payment");

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        System.out.println("Date (using current date):  " + localDate);
        System.out.println("Time (using current time):  " + localTime);


        System.out.println("Description: ");
        String description = scanner.nextLine();

        System.out.println("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Amount (negative number): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount >= 0) {
            System.out.println(" A payment amount must be negative");
            return;
        }


        Transaction payment = new Transaction(localDate, localTime, description, vendor, amount);
        transactions.add(payment);


        //add the new transaction to the cvs
        try {
            saveTransactions(payment, fileInput);
            System.out.println("Payment added and saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving the transaction");
            e.printStackTrace();
        }

    }

    public void ledgerScreen(Scanner scanner) {
        boolean counter = true;
        while (counter) {
            System.out.println("""
                    ===========================================
                                   Ledger Menu
                    ===========================================
                       Please type an option to access
                           A- All transactions
                           D- All deposits
                           P- All Payments
                           R- Reports
                           H- Home
                    ==========================================
                    """);
            String option = scanner.nextLine().trim();

            switch (option.toUpperCase()) {
                case "A" -> {
                    List<Transaction> allTransactions = getAllTransactions();
                    Collections.reverse(allTransactions);
                    System.out.println("-------------------------------All Transactions----------------------------------");
                    System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
                    System.out.println("-----------------------------------------------------------------------------");
                    allTransactions.forEach(System.out::println);
                    scanner.nextLine();
                }
                case "D" -> {
                    List<Transaction> deposits = getDeposits();
                    Collections.reverse(deposits);
                    System.out.println("-------------------------------All Deposits----------------------------------");
                    System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
                    System.out.println("-----------------------------------------------------------------------------");
                    deposits.forEach(System.out::println);
                    scanner.nextLine();
                }
                case "P" -> {
                    List<Transaction> payments = getPayments();
                    Collections.reverse(payments);
                    System.out.println("-------------------------------All Payments----------------------------------");
                    System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
                    System.out.println("-----------------------------------------------------------------------------");
                    payments.forEach(System.out::println);
                    scanner.nextLine();
                }
                case "R" -> reportsScreen(scanner);
                case "H" -> {
                    System.out.println("Exiting ledger Menu");
                    counter = false;
                }
                default -> System.out.println("Invalid option. Please type 'A' 'D' 'P' 'R' or 'H'");
            }
        }

    }

    //Helpers methods to the ledger screen


    public List<Transaction> getAllTransactions() {

        return transactions;
    }


    public List<Transaction> getDeposits() {
        List<Transaction> deposits = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits.add(transaction);
            }
        }
        return deposits;

    }


    public List<Transaction> getPayments() {
        List<Transaction> payments = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction);
            }
        }
        return payments;
    }

    public void reportsScreen(Scanner scanner) {

        try {
            transactions.clear(); // Clear the current list to avoid duplicates
            loadTransactions(fileInput); // Load existing transactions from the csv
        } catch (IOException e) {
            System.out.println("Error loading transactions");
            return;
        }

        boolean counter = true;
        while (counter) {
            System.out.println("""
                    ===========================================
                                   Reports Menu
                    ===========================================
                    Please select an type the option you want to access in numeric format
                           1- Month to Date
                           2- Previous Month
                           3- Year to Date
                           4- Previous Year
                           5- Search by Vendor
                           0- Back
                    ==========================================
                    """);
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    monthToDate();
                    break;
                case 2:
                    previousMonth();
                    break;
                case 3:
                    yearToDate();
                    break;
                case 4:
                    previousYear();
                    break;
                case 5:
                    vendorsSearch(scanner);
                    break;
                case 0:
                    System.out.println("Exiting Reports Menu");
                    counter = false;
                    break;
                default:
                    System.out.println("Invalid option. Please type '1' '2' '3' '4' or '5' or '0'");
            }
        }

    }

    //helpers methods for the report screen

    public void monthToDate() {
        LocalDate today = LocalDate.now();

        LocalDate startOfMonth = today.withDayOfMonth(1);

        List<Transaction> monthToDateTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startOfMonth) && !transactionDate.isAfter(today)) {
                monthToDateTransactions.add(transaction);
            }
        }

        System.out.println("----------------------------Month to Date Report-------------------------------");
        System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------");
        for (Transaction transaction : monthToDateTransactions) {
            System.out.println(transaction);
        }

    }

    public void yearToDate() {
        LocalDate today = LocalDate.now();

        LocalDate startOfYear = today.withDayOfYear(1);

        List<Transaction> yearToDateTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startOfYear) && !transactionDate.isAfter(today)) {
                yearToDateTransactions.add(transaction);
            }
        }

        System.out.println("----------------------------Year to Date Report-------------------------------");
        System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------");
        for (Transaction transaction : yearToDateTransactions) {
            System.out.println(transaction);
        }

    }

    public void previousMonth() {
        LocalDate today = LocalDate.now();

        LocalDate startOfCurrentMonth = today.withDayOfMonth(1);

        LocalDate endOfPreviousMonth = startOfCurrentMonth.minusDays(1);

        LocalDate startOfPreviousMonth = endOfPreviousMonth.withDayOfMonth(1);

        List<Transaction> previousMonthTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startOfPreviousMonth) && !transactionDate.isAfter(endOfPreviousMonth)) {
                previousMonthTransactions.add(transaction);
            }
        }


        System.out.println("----------------------------Previous Month-------------------------------");
        System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------");
        for (Transaction transaction : previousMonthTransactions) {
            System.out.println(transaction);
        }

    }

    public void previousYear(){

        LocalDate today= LocalDate.now();

        LocalDate startOfPreviousYear= today.minusYears(1).withDayOfYear(1);
        LocalDate endOfPreviousYear = today.withDayOfYear(1).minusDays(1);

        List<Transaction> previousYearTransactions= new ArrayList<>();

        for (Transaction transaction: transactions){
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startOfPreviousYear) && !transactionDate.isAfter(endOfPreviousYear)) {
                previousYearTransactions.add(transaction);
            }
        }

        System.out.println("----------------------------Previous Year Report-------------------------------");
        System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------");

        for (Transaction transaction : previousYearTransactions) {
            System.out.println(transaction);
        }


    }

      //Search by vendors methods
    public ArrayList<Transaction> findTransactionByVendor (String vendor){
        ArrayList<Transaction> matchingVendors = new ArrayList<>();

        for (Transaction transaction: transactions){
            if (transaction.getVendor().toLowerCase().contains(vendor)){
                matchingVendors.add(transaction);
            }
        }
        return matchingVendors;
    }

    public void vendorsSearch(Scanner scanner){
        System.out.println("Enter the vendor name or type 'X' to go back : ");

        String vendor = scanner.nextLine().trim();

        if (!vendor.equalsIgnoreCase("X")){
            String normalizeVendor = vendor.toLowerCase();

            ArrayList<Transaction> matchingVendors = findTransactionByVendor(normalizeVendor);
            if (!matchingVendors.isEmpty()){
                System.out.println("Vendor found:");
                    for (Transaction transaction: matchingVendors){
                        System.out.println(transaction);

                    }
            }else {
                System.out.println("Vendor not found");
            }
        }else {
            System.out.println("Returning");
        }
    }





}


