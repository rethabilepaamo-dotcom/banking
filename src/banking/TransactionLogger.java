package banking;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// final: there is no intention for this utility class to be extended.
public final class TransactionLogger {

    private static final String FILE_NAME = "transactions.txt";
    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TransactionLogger() {
        // Prevent object creation.
    }

    public static void log(
            String accountNumber,
            String transactionType,
            Double amount,
            Double balance,
            String message
    ) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {

            String amountText = amount == null ? "N/A" : String.format("M%.2f", amount);
            String balanceText = balance == null ? "N/A" : String.format("M%.2f", balance);

            writer.printf(
                    "%s | Account: %s | Type: %s | Amount: %s | Balance: %s | Result: %s%n",
                    LocalDateTime.now().format(FORMAT),
                    accountNumber,
                    transactionType,
                    amountText,
                    balanceText,
                    message
            );

        } catch (IOException e) {
            System.out.println("Warning: transaction could not be written to file.");
        }
    }
}
