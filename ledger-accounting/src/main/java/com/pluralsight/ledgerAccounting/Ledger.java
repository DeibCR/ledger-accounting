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


    private ResourceBundle messages;
    private String transactionsPath;


    public Ledger(ResourceBundle messages, String transactionsPath) { //Constructor to ledger that receive parameters from resource bundle and the path to the csv file from the choose language
        this.messages = messages;
        this.transactionsPath = transactionsPath;
    }

    //method that display the Home Screen menu
    public void homeScreen(Scanner scanner) {
        boolean counter = true;
        while (counter) {     //loop to keep the program running until user exit the program

            System.out.println(messages.getString("homeScreen.menu")); //read the .properties file with the string input to display


            String option = scanner.nextLine().trim(); //Input error in typo handle

            switch (option.toUpperCase()) {  // to UpperCase to manage error in typo
                case "D" -> registerTransaction(scanner); //call to the method that register a transaction
                case "P" -> registerTransaction(scanner);
                case "L" -> ledgerScreen(scanner); //call to the method that display ledgerScreen
                case "X" -> {
                    System.out.println(messages.getString("homeScreen.exit"));
                    counter = false;
                }
                default -> System.out.println(messages.getString("homeScreen.error"));
            }
        }
    }


    public void loadTransactions(String fileInput) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(fileInput)); //File and Buffered reading to read csv file and manage the reading
        String line; //variable to use for each line read on the csv
        fileReader.readLine(); // skip the header on the csv file

        while ((line = fileReader.readLine()) != null) {
            String[] transactionData = line.split("\\|"); // split the csv file into individual variable of transaction after each '|' delimiter
            LocalDate date = LocalDate.parse(transactionData[0], dateFormatter);
            LocalTime time = LocalTime.parse(transactionData[1], timeFormatter);
            String description = transactionData[2];
            String vendor = transactionData[3];
            double amount = Double.parseDouble(transactionData[4]);
            transactions.add(new Transaction(date, time, description, vendor, amount)); // Create a new transaction object from the parsed details and add it to the list

        }
        fileReader.close(); // close the reader after reading
    }


    public void saveTransactions(Transaction newTransaction) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(transactionsPath, true)); //create a file and buffered writer that write transactions in the same file, true value in append mode secure writing new content ad the end of the file instead of overwriting the content

        String line = String.format("%s|%s|%s|%s|%.2f", //provides a format in the specific format that the file use
                newTransaction.getDate(),
                newTransaction.getTime(),
                newTransaction.getDescription(),
                newTransaction.getVendor(),
                newTransaction.getAmount());

        fileWriter.write(line);
        fileWriter.newLine();//secure that the new transaction will be written on a new line
        fileWriter.close();

    }

    public void registerTransaction(Scanner scanner) {
        System.out.println(messages.getString("register.transaction"));   //display of the prompts from a ResourceBundle call messages

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS); // This helps eliminate te error of extra decimals in time recorded in the cvs

        System.out.println(messages.getString("register.date") + localDate); //date and time data are automatically recorded and displayed at the moment of register a transaction
        System.out.println(messages.getString("register.time") + localTime);


        System.out.println(messages.getString("register.description"));
        String description = scanner.nextLine();

        System.out.println(messages.getString("register.vendor"));
        String vendor = scanner.nextLine();

        System.out.println(messages.getString("register.amount"));
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) { //loop to verify if the input is positive or negative, depends on the result a transaction object is created and added to the list , the getDeposit and getPayment methods also create list for use in other moment in the app, in both cases the transaction is saved in the same csv file using saveTransactions ()

            Transaction payment = new Transaction(localDate, localTime, description, vendor, amount);

            transactions.add(payment);
            getPayments();
            try {
                saveTransactions(payment);
                System.out.println(messages.getString("register.message2"));
            } catch (IOException e) {
                System.out.println(messages.getString("register.error2"));
                e.printStackTrace();
            }


        } else {
            Transaction deposit = new Transaction(localDate, localTime, description, vendor, amount);

            transactions.add(deposit);
            getPayments();
            try {
                saveTransactions(deposit);
                System.out.println(messages.getString("register.message2"));
            } catch (IOException e) {
                System.out.println(messages.getString("register.error2"));
                e.printStackTrace();
            }

        }
    }

    public void ledgerScreen(Scanner scanner) {
        boolean counter = true;
        while (counter) {
            System.out.println(messages.getString("ledger.menu"));
            String option = scanner.nextLine().trim();

            switch (option.toUpperCase()) {
                case "A" -> {


                    System.out.println(messages.getString("ledger.prompt1"));


                    getAllTransactions().forEach(System.out::println);
                    System.out.println(messages.getString("ledger.line"));

                }
                case "D" -> {

                    System.out.println(messages.getString("ledger.prompt2"));
                    getDeposits().forEach(System.out::println);
                    System.out.println(messages.getString("ledger.line"));
                }
                case "P" -> {

                    System.out.println(messages.getString("ledger.prompt3"));
                    getPayments().forEach(System.out::println);
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
        List<Transaction> reversedTransactions = new ArrayList<>(transactions);
        Collections.reverse(reversedTransactions);
        return reversedTransactions;
    }


    public List<Transaction> getDeposits() {
        List<Transaction> deposits = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits.add(transaction);
            }
        }
        Collections.reverse(deposits);
        return deposits;

    }


    public List<Transaction> getPayments() {
        List<Transaction> payments = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction);
            }
        }
        Collections.reverse(payments);
        return payments;
    }

    public void reportsScreen(Scanner scanner) {


        boolean counter = true;
        while (counter) {
            System.out.println(messages.getString("reports.menu"));
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> monthToDate();
                case 2 -> previousMonth();
                case 3 -> yearToDate();
                case 4 -> previousYear();
                case 5 -> vendorsSearch(scanner);
                case 0 -> {
                    System.out.println("Exiting Reports Menu");
                    counter = false;
                }
                default -> System.out.println("Invalid option. Please type '1' '2' '3' '4' or '5' or '0'");
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
        Collections.reverse(monthToDateTransactions);

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
                yearToDateTransactions.add(transaction);


            }
        }
        Collections.reverse(yearToDateTransactions);

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
                previousMonthTransactions.add(transaction);

            }
        }
        Collections.reverse(previousMonthTransactions);

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
                previousYearTransactions.add(transaction);

            }
        }
        Collections.reverse(previousYearTransactions);

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


