import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        Random rand = new Random();
        System.out.println("Welcome to Mafia");
        int numPlayers = -1;
        while(numPlayers < 6){
            System.out.print("Enter number of players: ");
            try {
                numPlayers = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter an Integer.");
            }
        }

        System.out.println("Choose a character: ");
        System.out.println("1. Mafia");
        System.out.println("2. Detective");
        System.out.println("3. Healer");
        System.out.println("4. Commoner");
        System.out.println("5. Assign Randomly");
        int choice;
        try {
            choice = in.nextInt();
        } catch(InputMismatchException e) {
            System.out.println("Enter an Integer.");
            choice = in.nextInt();
        }

        boolean done = false;
        while(!done) {
            done = true;
            switch (choice) {
                case 1 -> {
                    Game<Mafia> game = new UserIsMafiaGame(numPlayers);
                    game.startGame();
                }
                case 2 -> {
                    Game<Detective> game = new UserIsDetectiveGame(numPlayers);
                    game.startGame();
                }
                case 3 -> {
                    Game<Healer> game = new UserIsHealerGame(numPlayers);
                    game.startGame();
                }
                case 4 -> {
                    Game<Commoner> game = new UserIsCommonerGame(numPlayers);
                    game.startGame();
                }
                case 5 -> {
                    done = false;
                    choice = rand.nextInt(4) + 1;
                }
            }
        }
    }
}
