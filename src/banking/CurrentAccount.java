package banking;

// Inheritance: CurrentAccount extends BankAccount.
public class CurrentAccount extends BankAccount {

    private static final double OVERDRAFT_LIMIT = 2000.00;

    public CurrentAccount(String accountNumber, String accountHolder, double openingBalance) {
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

    // Current account may use an overdraft of up to M2,000.
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }

        if (getBalance() - amount < -OVERDRAFT_LIMIT) {
            throw new IllegalArgumentException(
                    "Insufficient funds. Overdraft limit is M2,000.00."
            );
        }

        setBalance(getBalance() - amount);
    }

    // Current accounts earn no interest.
    @Override
    public double calculateInterest() {
        return 0.00;
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
        return Math.max(5000.00, getBalance() * 3);
    }

    @Override
    public String getAccountType() {
        return "Current Account";
    }
}
