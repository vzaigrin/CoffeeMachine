package machine;

import java.util.Scanner;

class Machine {
    enum State { WAIT, BUY, FILL }
    enum Fill { WATER, MILK, COFFEE, CUP }

    private int money = 550;
    private int water = 400;
    private int milk = 540;
    private int coffee = 120;
    private int cups = 9;
    private State state = State.WAIT;
    private Fill fill = Fill.WATER;

    private String printStatus() {
        return "\nThe coffee machine has:\n" +
                water + " of water\n" +
                milk +" of milk\n" +
                coffee + " of coffee beans\n" +
                cups + " of disposable cups\n" +
                money + " of money\n";
    }

    private int checkResources(int needWater, int needMilk, int needCoffe) {
        if (needWater > 0 && needWater > water) return 1;
        if (needMilk > 0 && needMilk > milk) return 2;
        if (needCoffe > 0 && needCoffe > coffee) return 3;
        if (cups < 1) return 4;
        else return 0;
    }

    private int checkCoffe(String type) {
        switch (type) {
            case "1": return checkResources(250, 0, 16);
            case "2": return checkResources(350, 75, 20);
            case "3": return checkResources(200, 100, 12);
        }
        return 0;
    }

    private void makeCoffe(String type) {
        switch (type) {
            case "1": water -= 250;
                coffee -= 16;
                money += 4;
                break;
            case "2": water -= 350;
                milk -= 75;
                coffee -= 20;
                money += 7;
                break;
            case "3": water -= 200;
                milk -= 100;
                coffee -= 12;
                money += 6;
                break;
        }
        cups--;
    }

    String action(String input) {
        String waitMessage = "\nWrite action (buy, fill, take, remaining, exit): ";
        switch (state) {
            case WAIT:
                switch (input) {
                    case "buy": state = State.BUY;
                        return "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ";
                    case "fill": state = State.FILL;
                        fill = Fill.WATER;
                        return "Write how many ml of water do you want to add: ";
                    case "take": int wasMoney = money;
                        money = 0;
                        return "I gave you " + wasMoney + "\n" + waitMessage;
                    case "remaining": return printStatus() + waitMessage;
                }
                break;
            case BUY:
                state = State.WAIT;
                if (!input.equals("back")) {
                    switch (checkCoffe(input)) {
                        case 0: makeCoffe(input);
                            return "I have enough resources, making you a coffee!\n" + waitMessage;
                        case 1: return "Sorry, not enough water!\n" + waitMessage;
                        case 2: return "Sorry, not enough milk!\n" + waitMessage;
                        case 3: return "Sorry, not enough coffee!\n" + waitMessage;
                        case 4: return "Sorry, not enough cups!\n" + waitMessage;
                    }
                }
                break;
            case FILL:
                switch (fill) {
                    case WATER: water += Integer.parseInt(input);
                        fill = Fill.MILK;
                        return "Write how many ml of milk do you want to add: ";
                    case MILK: milk += Integer.parseInt(input);
                        fill = Fill.COFFEE;
                        return "Write how many grams of coffee beans do you want to add: ";
                    case COFFEE: coffee += Integer.parseInt(input);
                        fill = Fill.CUP;
                        return "Write how many disposable cups of coffee do you want to add: ";
                    case CUP: cups += Integer.parseInt(input);
                        state = State.WAIT;
                        fill = Fill.WATER;
                        return waitMessage;
                }
                break;
        }
        return waitMessage;
    }
}


public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Machine machine = new Machine();

        System.out.print(machine.action(""));
        int exit = 0;
        while (exit != 1) {
            String input = scanner.nextLine();
            if (input.equals("exit")) exit = 1;
            else System.out.print(machine.action(input));
        }
    }
}
