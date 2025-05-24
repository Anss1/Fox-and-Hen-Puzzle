import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Puzzle {

    private Map<Integer, String> bottomSide = new HashMap<>();
    private Map<Integer, String> topSide = new HashMap<>();
    private Map<Integer, String> theBoat = new HashMap<>();
    private boolean boatOnBottomSide = true;
    private boolean currentBoatPosition = true;
    private boolean gameRunning = true;

    public Puzzle() {
        initializeGame();
    }

    private void initializeGame() {
        bottomSide.put(1, "1-Fox");
        bottomSide.put(2, "2-Corn");
        bottomSide.put(3, "3-Hen");
    }

    public void startGame() {
        try (Scanner scanner = new Scanner(System.in)) {
            displayWelcomeMessage();

            if (!scanner.nextLine().equals("1")) {
                System.out.println("Goodbye!");
                return;
            }

            while (gameRunning) {
                checkWinCondition();
                checkLoseConditions();

                clearScreen();
                displayGameState();

                if (!gameRunning)
                    break;

                System.out.println("\nOptions:");
                System.out.println("1: Select Fox");
                System.out.println("2: Select Corn");
                System.out.println("3: Select Hen");
                System.out.println("9: Move boat");
                System.out.println("0: Exit game");
                System.out.print("Enter your choice: ");

                handlePlayerInput(scanner);
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void displayWelcomeMessage() {
        System.out.println("====================================");
        System.out.println("  Fox, Hen, and Corn Puzzle Game");
        System.out.println("====================================");
        System.out.println("Rules:");
        System.out.println("- Get all objects to the top side");
        System.out.println("- Don't leave fox with hen alone");
        System.out.println("- Don't leave hen with corn alone");
        System.out.println("- Boat can carry only you + 1 object");
        System.out.println("\nPress '1' to start or any other key to exit");
    }

    private void displayGameState() {
        System.out.println("\nCurrent Game State:");
        System.out.println("====================================");

        System.out.print("\t\t");
        for (String options : topSide.values()) {
            System.out.print(options + "   ");
        }
        System.out.println();
        System.out.print(getRiver(boatOnBottomSide));
        System.out.print("\t\t");
        for (String options : bottomSide.values()) {
            System.out.print(options + "   ");
        }
        System.out.println();
        if (theBoat.isEmpty()) {
            System.out.println("On the Boot: empty");
        } else {
            System.out.println("On the Boot: " + theBoat.values());
        }

    }

    private String getRiver(boolean isBottom) {
        if (isBottom) {
            return """
                                ______________________________
                                ~ ~ ~ ~~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
                                ~ ~ ~ ~ ~ ~~ ~ ~ ~ ~ ~ ~ ~ ~ ~
                                ~ ~ ~ ~ ~ ~~ ~ ~ ~ ~ ~ ~ ~~ ~
                                ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~~ ~
                    The Boat -->______________________________
                                """;
        } else {
            return """
                    The Boat -->______________________________
                                ~ ~ ~ ~~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
                                ~ ~ ~ ~ ~ ~~ ~ ~ ~ ~ ~ ~ ~ ~ ~
                                ~ ~ ~ ~ ~ ~~ ~ ~ ~ ~ ~ ~ ~~ ~
                                ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~~ ~
                                ______________________________
                                """;
        }
    }

    private void handlePlayerInput(Scanner scanner) {
        try {
            int choice = scanner.nextInt();

            if (boatOnBottomSide) {
                handleBottomSideInput(choice);
            } else {
                handleTopSideInput(choice);
            }
        } catch (Exception e) {
            scanner.nextLine(); // Clear invalid input
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void handleBottomSideInput(int choice) {
        switch (choice) {
            case 1, 2, 3 -> moveItem(choice, bottomSide);
            case 9 -> moveBoat();
            case 0 -> exitGame();
            default -> System.out.println("Invalid choice. Try again.");
        }
    }

    private void handleTopSideInput(int choice) {
        switch (choice) {
            case 1, 2, 3 -> moveItem(choice, topSide);
            case 9 -> moveBoat();
            case 0 -> exitGame();
            default -> System.out.println("Invalid choice. Try again.");
        }
    }

    private void moveItem(int itemKey, Map<Integer, String> side) {
        if (side.containsKey(itemKey)) {
            if (theBoat.size() < 1) {
                theBoat.put(itemKey, side.get(itemKey));
                side.remove(itemKey);
            } else {
                System.out.println("Boat can carry only one item at a time!");
            }
        } else if (theBoat.containsKey(itemKey)) {
            side.put(itemKey, theBoat.get(itemKey));
            theBoat.remove(itemKey);
        } else {
            System.out.println("Item not found in current location.");
        }
    }

    private void moveBoat() {
        if (theBoat.size() <= 1) {
            boatOnBottomSide = !boatOnBottomSide; // Switch sides
            System.out.println("Boat moved to " + (boatOnBottomSide ? "Bottom" : "Top") + " Side.");
        } else {
            System.out.println("You can't move the boat with more than 1 object on it!");
            return;
        }

    }

    private void checkWinCondition() {
        if (topSide.size() == 3) {
            System.out.println("\nCongratulations! You won the game!");
            gameRunning = false;
        }
    }

    private void checkLoseConditions() {
        Map<Integer, String> checkSides = currentBoatPosition ? bottomSide : topSide;

        if (currentBoatPosition != boatOnBottomSide) {
            if (checkSides.containsKey(1) && checkSides.containsKey(3) && !checkSides.containsKey(2)) {
                System.out.println("\nGame Over! The fox ate the hen!");
                gameRunning = false;
            } else if (checkSides.containsKey(2) && checkSides.containsKey(3) && !checkSides.containsKey(1)) {
                System.out.println("\nGame Over! The hen ate the corn!");
                gameRunning = false;
            }
        }
        currentBoatPosition = boatOnBottomSide;

    }

    private void exitGame() {
        System.out.println("Thanks for playing! Goodbye!");
        gameRunning = false;
    }

    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void main(String[] args) {
        new Puzzle().startGame();
    }

}
