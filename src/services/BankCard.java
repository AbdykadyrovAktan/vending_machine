package services;

public class BankCard implements Payable{
    private double balance = 210;

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
