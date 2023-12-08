import enums.ActionLetter;
import exceptions.InvalidActionException;
import model.*;
import services.BankCard;
import services.Cash;
import services.Payable;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppRunner {
    private static boolean isContinue = true;
    private final UniversalArray<Product> products = new UniversalArrayImpl<>();
    private final Payable[] payMethods = {new Cash(), new BankCard()};
    private Payable payMethod;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (isContinue) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        System.out.println("В автомате доступны:");
        showProducts(products);
        selectPaymentMethod();
        System.out.println("Монет на сумму: " + payMethod.getBalance());
        UniversalArray<Product> allowProducts = getAllowedProducts();
        chooseAction(allowProducts);
    }

    private void selectPaymentMethod() {
        System.out.println("1. У меня монеты\n2. У меня банковская карта");
        String choiceStr = fromConsole();
        try {
            validateNum(choiceStr, 1);
        } catch (NoSuchFieldException | InputMismatchException | InvalidActionException e) {
            System.out.println(e.getMessage());
            startSimulation();
        }

        int choice = Integer.parseInt(choiceStr);

        if (choice == 1) {
            payMethod = payMethods[0];
        } else if (choice == 2) {
            identifyUser();
        } else {
            startSimulation();
        }
    }

    private void identifyUser() {
        try {
            System.out.print("Enter card num: ");
            String card = fromConsole();
            validateNum(card, 16);

            System.out.print("Enter card password: ");
            String passwordStr = fromConsole();
            validateNum(passwordStr, 4);

            if (!passwordStr.equals(card.substring(0, 4))) {
                throw new InvalidActionException("Incorrect password!");
            } else {
                payMethod = payMethods[1];
            }
        } catch (NoSuchFieldException | InputMismatchException | InvalidActionException e) {
            System.out.println(e.getMessage());
            selectPaymentMethod();
        }
    }

    private void validateNum(String str, int max) throws NoSuchFieldException, InputMismatchException, InvalidActionException {
        if (str.isBlank() || str.isEmpty()) {
            throw new NoSuchFieldException("The value cannot be empty!");
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new InputMismatchException("The value does not match the expected type!");
            }
        }

        if (str.length() != max) {
            throw new InvalidActionException("The card number consists of 16 characters!");
        }
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (payMethod.getBalance() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        showAvailableOptions(products);
        String action = fromConsole();

        try {
            validateChoice(action, products);
        } catch (InvalidActionException | InputMismatchException | NoSuchFieldException e) {
            System.err.println(e.getMessage());
            startSimulation();
        }

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getActionLetter().toString().equalsIgnoreCase(action)) {
                payMethod.setBalance(payMethod.getBalance() - products.get(i).getPrice());
                System.out.println("Вы купили " + products.get(i).getName());
                break;
            } else if (action.equalsIgnoreCase("a")) {
                payMethod.setBalance(payMethod.getBalance() + 10);
                break;
            } else if (action.equalsIgnoreCase("h")) {
                isContinue = false;
                break;
            }
        }
    }

    private void validateChoice(String str, UniversalArray<Product> products) throws InvalidActionException, NoSuchFieldException, InputMismatchException {
        if (str.isBlank() || str.isEmpty()) {
            throw new NoSuchFieldException("The value cannot be empty!");
        }

        if (str.length() > 1) {
            throw new InvalidActionException("Choose one action!");
        }

        if (!Character.isAlphabetic(str.charAt(0))) {
            throw new InputMismatchException("The value does not match the expected type!");
        }

        boolean isValid = false;
        for (int i = 0; i < products.size(); i++) {
            if (str.equalsIgnoreCase(products.get(i).getActionLetter().toString())
                    || str.equalsIgnoreCase("a")
                    || str.equalsIgnoreCase("h")) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new InvalidActionException("Choose one available action!");
        }
    }

    private void showAvailableOptions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            System.out.printf(" %s - %s%n", products.get(i).getActionLetter().getValue(), products.get(i).getName());
        }
        System.out.println(" a - Пополнить баланс ");
        System.out.println(" h - Выйти ");
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            System.out.println(products.get(i).toString());
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }
}