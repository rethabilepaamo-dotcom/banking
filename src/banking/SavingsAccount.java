package banking;

// Inheritance: SavingsAccount extends BankAccount.
public class SavingsAccount extends BankAccount {

    private static final double MINIMUM_BALANCE = 100.00;
    private static final double INTEREST_RATE = 0.05;

    public SavingsAccount(String accountNumber, String accountHolder, double openingBalance) {
        super(accountNumber, accountHolder, openingBalance);
    }

    // Method overriding
    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        setBalance(getBalance() + amount);
    }

    // Savings account must keep at least M100 after withdrawal.
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }

        if (getBalance() - amount < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                    "Insufficient funds. Savings account must keep a minimum balance of M100.00."
            );
        }

        setBalance(getBalance() - amount);
    }

    // Savings earns 5% annual interest.
    @Override
    public double calculateInterest() {
        double interest = getBalance() * INTEREST_RATE;
        setBalance(getBalance() + interest);
        return interest;
    }

    // Interface implementation
    @Override
    public void requestLoan(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Loan amount must be greater than zero.");
        }

        if (amount > getMaximumLoan()) {
            throw new IllegalArgumentException(
                    String.format("Loan rejected. Maximum allowed loan is M%.2f.", getMaximumLoan())
            );
        }
    }

    @Override
    public double getMaximumLoan() {
        return getBalance() * 2;
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }
}
