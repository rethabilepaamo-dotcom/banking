import banking.BankAccount;
import banking.CurrentAccount;
import banking.SavingsAccount;
import banking.TransactionLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BankingSystemMain {

    private static final Scanner scanner = new Scanner(System.in);

    // Polymorphism:
    // BankAccount references can point to SavingsAccount
    // and CurrentAccount objects.
    private static final List<BankAccount> accounts = new ArrayList<>();

    // Stores the PIN for each account number
    private static final Map<String, String> accountPins = new HashMap<>();

    public static void main(String[] args) {


        BankAccount savings = new SavingsAccount(
                "S901020592",
                "Rethabile Paamo",
                2000.00
        );

        BankAccount current = new CurrentAccount(
                "C901020494",
                "Selloane Molapo",
                2500.00
        );

        accounts.add(savings);
        accounts.add(current);

        accountPins.put("S901020592", "1234");
        accountPins.put("C901020494", "5678");


        System.out.println("                            ");
        System.out.println("      CONSOLE BANKING SYSTEM");
        System.out.println("                            ");

        BankAccount loggedInAccount = login();

        // If login failed, stop the program
        if (loggedInAccount == null) {
            System.out.println("Program closed.");
            scanner.close();
            return;
        }


        boolean running = true;

        while (running) {

            displayMenu();

            try {

                int choice = readInt("Choose an option: ");

                switch (choice) {

                    case 1:
                        displayAllAccounts();
                        break;

                    case 2:
                        depositMoney();
                        break;

                    case 3:
                        withdrawMoney();
                        break;

                    case 4:
                        transferMoney();
                        break;

                    case 5:
                        calculateInterest();
                        break;

                    case 6:
                        requestLoan();
                        break;

                    case 7:
                        displayAccountDetails();
                        break;

                    case 8:
                        running = false;
                        System.out.println(
                                "Thank you for using the banking system."
                        );
                        break;

                    default:
                        System.out.println(
                                "Invalid menu choice."
                        );
                }

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }

        scanner.close();
    }



    private static BankAccount login() {

        System.out.println();
        System.out.println("========== ACCOUNT LOGIN ==========");

        String accountNumber =
                readText("Enter account number: ");

        BankAccount account;

        try {

            account = findAccount(accountNumber);

        } catch (IllegalArgumentException e) {

            System.out.println("Login failed: Account not found.");
            return null;
        }


        int attempts = 3;

        while (attempts > 0) {

            String enteredPin =
                    readText("Enter PIN: ");

            String correctPin =
                    accountPins.get(account.getAccountNumber());

            if (enteredPin.equals(correctPin)) {

                System.out.println();
                System.out.println("PIN correct.");
                System.out.println("Access granted.");
                System.out.println(
                        "Welcome, "
                                + account.getAccountHolder()
                                + "!"
                );

                return account;

            } else {

                attempts--;

                System.out.println("Incorrect PIN.");

                if (attempts > 0) {

                    System.out.println(
                            "Attempts remaining: "
                                    + attempts
                    );

                } else {

                    System.out.println(
                            "Too many incorrect PIN attempts."
                    );

                    System.out.println(
                            "Access denied."
                    );
                }
            }
        }

        return null;
    }



    private static void displayMenu() {

        System.out.println();
        System.out.println("==================================");
        System.out.println("           BANKING MENU");
        System.out.println("==================================");

        System.out.println("1. Display all accounts");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Transfer money");
        System.out.println("5. Calculate interest");
        System.out.println("6. Request bank loan");
        System.out.println("7. Display account details");
        System.out.println("8. Exit");

        System.out.println("==================================");
        System.out.println();
    }



    // DISPLAY ALL ACCOUNTS


    private static void displayAllAccounts() {

        for (BankAccount account : accounts) {

            account.displayAccountInfo();
        }
    }


    // DEPOSIT


    private static void depositMoney() {

        BankAccount account =
                findAccount(
                        readText(
                                "Enter account number: "
                        )
                );

        double amount =
                readDouble(
                        "Enter deposit amount: "
                );

        try {

            account.deposit(amount);

            System.out.printf(
                    "Deposit successful. New balance: M%.2f%n",
                    account.getBalance()
            );

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "DEPOSIT",
                    amount,
                    account.getBalance(),
                    "SUCCESS"
            );

        } catch (IllegalArgumentException e) {

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "DEPOSIT",
                    amount,
                    account.getBalance(),
                    "FAILED - " + e.getMessage()
            );

            throw e;
        }
    }



    // WITHDRAW


    private static void withdrawMoney() {

        BankAccount account =
                findAccount(
                        readText(
                                "Enter account number: "
                        )
                );

        double amount =
                readDouble(
                        "Enter withdrawal amount: "
                );

        try {

            account.withdraw(amount);

            System.out.printf(
                    "Withdrawal successful. New balance: M%.2f%n",
                    account.getBalance()
            );

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "WITHDRAWAL",
                    amount,
                    account.getBalance(),
                    "SUCCESS"
            );

        } catch (IllegalArgumentException e) {

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "WITHDRAWAL",
                    amount,
                    account.getBalance(),
                    "FAILED - " + e.getMessage()
            );

            throw e;
        }
    }



    // TRANSFER
    // ONLY SAVINGS ACCOUNT CAN RECEIVE


    private static void transferMoney() {

        BankAccount from =
                findAccount(
                        readText(
                                "Enter sender account number: "
                        )
                );

        BankAccount to =
                findAccount(
                        readText(
                                "Enter receiver account number: "
                        )
                );

        // Prevent transferring to the same account
        if (from.getAccountNumber()
                .equalsIgnoreCase(to.getAccountNumber())) {

            throw new IllegalArgumentException(
                    "Sender and receiver accounts cannot be the same."
            );
        }


        // CHECK RECEIVER TYPE


        if (!(to instanceof SavingsAccount)) {

            System.out.println();
            System.out.println(
                    "TRANSFER NOT ALLOWED."
            );

            System.out.println(
                    "The receiver must have a Savings Account."
            );

            System.out.println(
                    "Receiver account: "
                            + to.getAccountNumber()
            );

            return;
        }

        double amount =
                readDouble(
                        "Enter transfer amount: "
                );

        if (amount <= 0) {

            TransactionLogger.log(
                    from.getAccountNumber(),
                    "TRANSFER",
                    amount,
                    from.getBalance(),
                    "FAILED - Transfer amount must be greater than zero."
            );

            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero."
            );
        }

        try {

            from.withdraw(amount);

            to.deposit(amount);

            System.out.println();
            System.out.println(
                    "Transfer successful."
            );

            System.out.println(
                    "From: "
                            + from.getAccountNumber()
            );

            System.out.println(
                    "To: "
                            + to.getAccountNumber()
            );

            System.out.println(
                    "Receiver: "
                            + to.getAccountHolder()
            );

            System.out.printf(
                    "Amount: M%.2f%n",
                    amount
            );

            TransactionLogger.log(
                    from.getAccountNumber(),
                    "TRANSFER OUT",
                    amount,
                    from.getBalance(),
                    "SUCCESS to "
                            + to.getAccountNumber()
            );

            TransactionLogger.log(
                    to.getAccountNumber(),
                    "TRANSFER IN",
                    amount,
                    to.getBalance(),
                    "SUCCESS from "
                            + from.getAccountNumber()
            );

        } catch (IllegalArgumentException e) {

            TransactionLogger.log(
                    from.getAccountNumber(),
                    "TRANSFER",
                    amount,
                    from.getBalance(),
                    "FAILED - " + e.getMessage()
            );

            throw e;
        }
    }


    // INTEREST


    private static void calculateInterest() {

        BankAccount account =
                findAccount(
                        readText(
                                "Enter account number: "
                        )
                );

        double interest =
                account.calculateInterest();

        if (interest > 0) {

            System.out.printf(
                    "Interest added: M%.2f%n",
                    interest
            );

            System.out.printf(
                    "New balance: M%.2f%n",
                    account.getBalance()
            );

        } else {

            System.out.println(
                    "This account earns no interest."
            );
        }

        TransactionLogger.log(
                account.getAccountNumber(),
                "INTEREST",
                interest,
                account.getBalance(),
                interest > 0
                        ? "SUCCESS"
                        : "NO INTEREST FOR THIS ACCOUNT"
        );
    }


    // LOAN


    private static void requestLoan() {

        BankAccount account =
                findAccount(
                        readText(
                                "Enter account number: "
                        )
                );

        double amount =
                readDouble(
                        "Enter loan amount: "
                );

        try {

            account.requestLoan(amount);

            System.out.printf(
                    "Loan request approved for M%.2f.%n",
                    amount
            );

            System.out.printf(
                    "Maximum allowed: M%.2f%n",
                    account.getMaximumLoan()
            );

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "LOAN REQUEST",
                    amount,
                    account.getBalance(),
                    "APPROVED"
            );

        } catch (IllegalArgumentException e) {

            TransactionLogger.log(
                    account.getAccountNumber(),
                    "LOAN REQUEST",
                    amount,
                    account.getBalance(),
                    "REJECTED - " + e.getMessage()
            );

            throw e;
        }
    }


    // ACCOUNT DETAILS

    private static void displayAccountDetails() {

        BankAccount account =
                findAccount(
                        readText(
                                "Enter account number: "
                        )
                );

        account.displayAccountInfo();

        System.out.printf(
                "Maximum Loan   : M%.2f%n",
                account.getMaximumLoan()
        );
    }


    // FIND ACCOUNT


    private static BankAccount findAccount(
            String accountNumber) {

        for (BankAccount account : accounts) {

            if (account.getAccountNumber()
                    .equalsIgnoreCase(accountNumber)) {

                return account;
            }
        }

        throw new IllegalArgumentException(
                "Account not found."
        );
    }


    // READ TEXT


    private static String readText(String message) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }


    // READ INTEGER

    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                int value =
                        Integer.parseInt(
                                scanner.nextLine()
                        );

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a whole number."
                );
            }
        }
    }



    // READ DOUBLE

    private static double readDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Double.parseDouble(
                        scanner.nextLine()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a valid number."
                );
            }
        }
    }
}

