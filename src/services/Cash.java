package services;

public class Cash implements Payable{
    private double balance = 100;

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
