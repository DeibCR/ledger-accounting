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
import java.util.stream.Collectors;

public class Ledger {
    ArrayList<Transaction> transactions = new ArrayList<>(); // List that store transaction objects
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String fileInput = "./src/main/resources/transactions.csv";


    //method that read the transactions from the cvs file and added to the list transactions
    public void loadTransactions(String fileInput) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(fileInput));
        String line;
        fileReader.readLine(); // skip the header

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

    //method that saves each new transaction to the csv file
    public void saveTransactions(Transaction newTransaction, String fileInput) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileInput, true)); //The true value append just the new transaction to the file

        String line = String.format("%s|%s|%s|%s|%.2f", //write the transaction in the file in the same format
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
    public void addDeposit(Scanner scanner, String fileInput) {
        System.out.println("Please enter the details for the new deposit");

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

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

        //create a new deposit and add to the list

        Transaction deposit = new Transaction(localDate, localTime, description, vendor, amount);
        transactions.add(deposit);

        try {
            saveTransactions(deposit, fileInput);
            System.out.println("Deposit added and saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving the transaction: " + e.getMessage());
        }

    }

    //Method to add a new transaction (payment) by prompting user
    public void addPayment(Scanner scanner, String fileInput) {
        System.out.println("Please enter the details for the new payment");

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        System.out.println("Date (using current date):  " + localDate);
        System.out.println("Time (using current time):  " + localTime);


        System.out.println("Description: ");
        String description = scanner.nextLine();

        System.out.println("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("amount (negative number): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount >= 0) {
            System.out.println(" A payment amount must be negative");
            return;
        }

        //create a new deposit and add to the list
        Transaction payment = new Transaction(localDate, localTime, description, vendor, amount);
        transactions.add(payment);


        //add the new transaction to the cvs
        try {
            saveTransactions(payment, fileInput);
            System.out.println("Payment added and saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving the transaction: " + e.getMessage());
        }

    }

    public void ledgerScreen(Scanner scanner) {
        boolean counter = true; //Loop control variable
        while (counter) {
            System.out.println("""
                    ===========================================
                                   Ledger Menu
                    ===========================================
                          Please select an option
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
                    getDeposits().forEach(System.out::println);
                    scanner.nextLine();
                }
                case "P" -> {
                    List<Transaction> payments = getPayments();
                    Collections.reverse(payments);
                    System.out.println("-------------------------------All Payments----------------------------------");
                    System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
                    System.out.println("-----------------------------------------------------------------------------");
                    getPayments().forEach(System.out::println);
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

    // method to display all the transactions
    public List<Transaction> getAllTransactions() {

        return transactions;
    }

    //method to display all transactions that are deposits, using .stream() a sequence of elements that can processed or filter
    public List<Transaction> getDeposits() {
        return transactions.stream().filter(transaction -> transaction.getAmount() > 0).collect(Collectors.toList());
    }

    public List<Transaction> getPayments() {
        return transactions.stream().filter(transaction -> transaction.getAmount() < 0).collect(Collectors.toList());
    }


    public void reportsScreen(Scanner scanner) {

        try {
            transactions.clear(); // Clear the current list to avoid duplicates
            loadTransactions(fileInput); // Load existing transactions from the csv
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
            return; // Exit if loading fails
        }

        boolean counter = true; //Loop control variable
        while (counter) {
            System.out.println("""
                    ===========================================
                                   Reports Menu
                    ===========================================
                          Please select an option
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
                    monthToDateReport();
                    break;
                case 2:
                    previousMonthReport();
                    break;
                case 3:
                    yearToDateReport();
                    break;
                case 4:
                    break;
                case 5:
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

    public void monthToDateReport() {
        LocalDate today = LocalDate.now(); //Current date

        LocalDate startOfMonth = today.withDayOfMonth(1);// First day of the current month

        List<Transaction> monthToDateTransactions = transactions.stream().filter(transaction -> {
                    LocalDate transactionDate = transaction.getDate();
                    return (transactionDate.isEqual(startOfMonth) || transactionDate.isAfter(startOfMonth)) &&
                            (transactionDate.isEqual(today) || transactionDate.isBefore(today));
                })
                .collect(Collectors.toList());

        System.out.println("----------------------------Month to Date Report-------------------------------");
        System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------");
        monthToDateTransactions.forEach(System.out::println);


    }

    public void yearToDateReport() {
        LocalDate today = LocalDate.now(); //Current date

        LocalDate startOfYear = today.withDayOfYear(1);// First day of the current year

        List<Transaction> yearToDateTransactions = transactions.stream().filter(transaction -> {
                    LocalDate transactionDate = transaction.getDate();
                    return (transactionDate.isEqual(startOfYear) || transactionDate.isAfter(startOfYear)) &&
                            (transactionDate.isEqual(today) || transactionDate.isBefore(today));
                })
                .collect(Collectors.toList());

        System.out.println("----------------------------Year to Date Report-------------------------------");
        System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------");
        yearToDateTransactions.forEach(System.out::println);

    }

    public void previousMonthReport() {
        LocalDate today = LocalDate.now(); //Current date

        LocalDate startOfCurrentMonth = today.withDayOfMonth(1); // First day of the current month

        LocalDate endOfPreviousMonth = startOfCurrentMonth.minusDays(1); //Last day of the previous month

        LocalDate startOfPreviousMonth = endOfPreviousMonth.withDayOfMonth(1); //Fist day of previous month

        List<Transaction> previousMonthTransactions = transactions.stream()
                .filter(transaction -> { //filtering with stream and filter, all the transactions that occurs between the s
                    LocalDate transactionDate = transaction.getDate();
                    return (transactionDate.isEqual(startOfPreviousMonth) || transactionDate.isAfter(startOfPreviousMonth)) &&
                            (transactionDate.isEqual(endOfPreviousMonth) || transactionDate.isBefore(endOfPreviousMonth));
                })
                .collect(Collectors.toList());

        // Print the report
        System.out.println("----------------------------Previous Month-------------------------------");
        System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------");
        previousMonthTransactions.forEach(System.out::println);

    }


}


