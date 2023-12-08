package services;

import exceptions.InvalidActionException;

public class BankCard implements Payable {
    private double balance = 210;

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void setBalance(double balance) {
        if (balance > this.balance) {
            try {
                throw new InvalidActionException("Impossible surgery! Card cannot be topped up!");
            } catch (InvalidActionException e) {
                System.err.println(e.getMessage());
            }
        } else {
            this.balance = balance;
        }
    }
}
