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
import java.util.ResourceBundle;


public class Ledger {
    private ArrayList<Transaction> transactions = new ArrayList<>();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    String fileInput = "./src/main/resources/transactions.csv";

    private ResourceBundle messages;
    private String transactionsPath;


    public Ledger(ResourceBundle messages, String transactionsPath) {
        this.messages = messages;
        this.transactionsPath = transactionsPath;
    }


    public void homeScreen(Scanner scanner) {
        boolean counter = true;
        while (counter) {     //loop to keep the program running until user exit the program

            System.out.println(messages.getString("homeScreen.menu"));


            String option = scanner.nextLine().trim();

            switch (option.toUpperCase()) {  // to UpperCase to manage error in typo
                case "D" -> registerDeposit(scanner);
                case "P" -> registerPayment(scanner);
                case "L" -> ledgerScreen(scanner);
                case "X" -> {
                    System.out.println(messages.getString("homeScreen.exit"));
                    counter = false;
                }
                default -> System.out.println(messages.getString("homeScreen.error"));
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


    public void saveTransactions(Transaction newTransaction) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(transactionsPath, true));

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


    public void registerDeposit(Scanner scanner) {
        System.out.println(messages.getString("register.deposit"));

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS); // This helps eliminate te error of extra decimals in time recorded in the cvs

        System.out.println(messages.getString("register.date") + localDate); //date and time data are automatically recorded
        System.out.println(messages.getString("register.time") + localTime);


        System.out.println(messages.getString("register.description"));
        String description = scanner.nextLine();

        System.out.println(messages.getString("register.vendor"));
        String vendor = scanner.nextLine();

        System.out.println(messages.getString("register.amount"));
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println(messages.getString("register.error1"));
            return;
        }


        Transaction deposit = new Transaction(localDate, localTime, description, vendor, amount);

        transactions.add(0, deposit);

        try {
            saveTransactions(deposit);
            System.out.println(messages.getString("register.message1"));
        } catch (IOException e) {
            System.out.println(messages.getString("register.error2"));
            e.printStackTrace();
        }

    }


    public void registerPayment(Scanner scanner) {
        System.out.println(messages.getString("register.payment"));

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        System.out.println(messages.getString("register.date") + localDate);
        System.out.println(messages.getString("register.time") + localTime);


        System.out.println(messages.getString("register.description"));
        String description = scanner.nextLine();

        System.out.println(messages.getString("register.vendor"));
        String vendor = scanner.nextLine();

        System.out.println(messages.getString("register.amount1"));
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount >= 0) {
            System.out.println(messages.getString("register.error3"));
            return;
        }


        Transaction payment = new Transaction(localDate, localTime, description, vendor, amount);
        transactions.add(0, payment);


        //add the new transaction to the cvs
        try {
            saveTransactions(payment);
            System.out.println(messages.getString("register.message2"));
        } catch (IOException e) {
            System.out.println(messages.getString("register.error2"));
            e.printStackTrace();
        }

    }

    //Helper method to register a new transaction *Pending


    public void ledgerScreen(Scanner scanner) {
        boolean counter = true;
        while (counter) {
            System.out.println(messages.getString("ledger.menu"));
            String option = scanner.nextLine().trim();

            switch (option.toUpperCase()) {
                case "A" -> {
                    List<Transaction> allTransactions = getAllTransactions();

                    System.out.println(messages.getString("ledger.prompt1"));
                    /*
                    System.out.println("-------------------------------All Transactions----------------------------------");
                    System.out.printf("%-12s %-8s %-20s %-10s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
                    System.out.println("-----------------------------------------------------------------------------");

                     */

                    allTransactions.forEach(System.out::println);
                    System.out.println(messages.getString("ledger.line"));

                }
                case "D" -> {
                    List<Transaction> deposits = getDeposits();
                    Collections.reverse(deposits);
                    System.out.println(messages.getString("ledger.prompt2"));
                    deposits.forEach(System.out::println);
                    System.out.println(messages.getString("ledger.line"));
                }
                case "P" -> {
                    List<Transaction> payments = getPayments();
                    Collections.reverse(payments);
                    System.out.println(messages.getString("ledger.prompt3"));
                    payments.forEach(System.out::println);
                    System.out.println(messages.getString("ledger.line"));
                }
                case "R" -> reportsScreen(scanner);
                case "H" -> {
                    System.out.println(messages.getString("ledger.exit"));
                    counter = false;
                }
                default -> System.out.println(messages.getString("ledger.error"));
            }
        }

    }

    //Helpers methods to the ledger screen


    public List<Transaction> getAllTransactions() {
    Collections.reverse(transactions);
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



        boolean counter = true;
        while (counter) {
            System.out.println(messages.getString("reports.menu"));
            int option = scanner.nextInt();
            scanner.nextLine();

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
                monthToDateTransactions.add(0,transaction);
               // Collections.reverse(monthToDateTransactions);
            }
        }

        System.out.println(messages.getString("reports.prompt1"));
        monthToDateTransactions.forEach(System.out::println);
        System.out.println(messages.getString("reports.line"));

    }

    public void yearToDate() {
        LocalDate today = LocalDate.now();

        LocalDate startOfYear = today.withDayOfYear(1);

        List<Transaction> yearToDateTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startOfYear) && !transactionDate.isAfter(today)) {
                yearToDateTransactions.add(0,transaction);

            }
        }

        System.out.println(messages.getString("reports.prompt3"));
        yearToDateTransactions.forEach(System.out::println);
        System.out.println(messages.getString("reports.line"));


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
                previousMonthTransactions.add(0,transaction);
              //  Collections.reverse(previousMonthTransactions);
            }
        }


        System.out.println(messages.getString("reports.prompt2"));
        previousMonthTransactions.forEach(System.out::println);
        System.out.println(messages.getString("reports.line"));

    }

    public void previousYear() {

        LocalDate today = LocalDate.now();

        LocalDate startOfPreviousYear = today.minusYears(1).withDayOfYear(1);
        LocalDate endOfPreviousYear = today.withDayOfYear(1).minusDays(1);

        List<Transaction> previousYearTransactions = new ArrayList<>();


        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startOfPreviousYear) && !transactionDate.isAfter(endOfPreviousYear)) {
                previousYearTransactions.add(0,transaction);
                //Collections.reverse(previousYearTransactions);
            }
        }

        System.out.println(messages.getString("reports.prompt4"));
        previousYearTransactions.forEach(System.out::println);
        System.out.println(messages.getString("reports.line"));


    }

    //Search by vendors methods
    public ArrayList<Transaction> findTransactionByVendor(String vendor) {
        ArrayList<Transaction> matchingVendors = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
                matchingVendors.add(transaction);
            }
        }
        return matchingVendors;
    }

    public void vendorsSearch(Scanner scanner) {
        System.out.println(messages.getString("vendor.prompt"));

        String vendor = scanner.nextLine().trim();

        if (!vendor.equalsIgnoreCase("X")) {
            String normalizeVendor = vendor.toLowerCase();

            ArrayList<Transaction> matchingVendors = findTransactionByVendor(normalizeVendor);
            if (!matchingVendors.isEmpty()) {
                System.out.println(messages.getString("vendor.message1"));
                Collections.reverse(matchingVendors);
                matchingVendors.forEach(System.out::println);

            } else {
                System.out.println(messages.getString(messages.getString("vendor.message2")));
            }
        } else {
            System.out.println(messages.getString("vendor.message3"));
        }
    }

}


