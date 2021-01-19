import java.util.ArrayList;
import java.util.Scanner;

public final class UserIsDetectiveGame extends Game<Detective>{
    UserIsDetectiveGame(int numPlayers) {
        super(numPlayers);
    }

    public void allocateUser(){
        boolean done = false;
        while(!done){
            for(Player p: playersInGame) {
                if(p instanceof Detective && User == null) {
                    User = (Detective) p;
                    System.out.println("You are Player" + p.ID + ".");
                    System.out.print("You are a Detective. Other Detectives are: [");
                    ArrayList<Detective> team = User.getTeam(this);
                    for(Player other : team) {
                        if(other != p) {
                            System.out.print("Player" + other.ID + ", ");
                        }
                    }
                    System.out.println("]");
                    done = true;
                }
            }
        }
    }
    public void executeRound(){
        System.out.println("Round " + roundNumber + ":");
        System.out.print(numPlayers + " players are remaining: " );
        for(Player p: playersInGame) {
            System.out.print("Player" + p.ID + ", ");
        }
        System.out.println("are alive.");

        Mafia m = null;
        Healer h = null;
        Detective d = null;

        for(Player p: playersInGame) {
            if(p instanceof Mafia) {
                m = (Mafia) p;
            } else if(p instanceof Healer) {
                h = (Healer) p;
            } else if(p instanceof Detective) {
                d = (Detective) p;
            }
        }

        mafiaTurn(m);
        boolean mafiaEjected;
        if(User.isInGame) {
            mafiaEjected = detectiveTurn(User);
        } else {
            mafiaEjected = super.detectiveTurn(d);
        }
        healerTurn(h);
        System.out.println("----End of Actions----");
        removeDead();
        Player votedOut = null;
        if(!mafiaEjected) {
            votedOut = votingRound();
        }
        if(votedOut != null) {
            System.out.println("Player" + votedOut.ID + " has been voted out.");
        }
        System.out.println("----End of Round " + roundNumber + "----" );
        roundNumber++;
    }

    @Override
    public boolean detectiveTurn(Detective d) {
        Scanner in = new Scanner(System.in);
        boolean mafiaEjected = false;
        boolean validChoice = false;
        int choice;
        while(!validChoice) {
            System.out.print("Choose a player to test: ");
            choice = in.nextInt();
            Player target = null;
            for(Object p: playersInGame) {
                if(((Player)p).ID == choice) {
                    target = (Player) p;
                }
            }
            if(target == null) {
                System.out.println("Player" + choice + " cannot be tested.");
            } else {
                String identity = d.test(target);
                if (!identity.equals("detective")) {
                    validChoice = true;
                    if (identity.equals("mafia")) {
                        mafiaEjected = true;
                        System.out.println("Player" + target.ID + " is a mafia and has been voted out.");
                        target.votedOut(this);
                    } else {
                        System.out.println("Player" + target.ID + " is not a mafia.");
                    }
                } else {
                    System.out.println("You cannot test a Detective.");
                }
            }
        }
        return mafiaEjected;
    }
}
