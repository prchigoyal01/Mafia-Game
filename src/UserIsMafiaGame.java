import java.util.ArrayList;
import java.util.Scanner;

public final class UserIsMafiaGame extends Game<Mafia>{
    UserIsMafiaGame(int numPlayers) {
        super(numPlayers);
    }

    public void allocateUser(){
        boolean done = false;
        while(!done){
            for(Player p: playersInGame) {
                if(p instanceof Mafia && User == null) {
                    User = (Mafia) p;
                    System.out.println("You are Player" + p.ID + ".");
                    System.out.print("You are a Mafia. Other Mafias are: [");
                    ArrayList<Mafia> team = User.getTeam(this);
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
        roundNumber++;
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

        if(User.isInGame) {
            mafiaTurn(User);
        } else {
            super.mafiaTurn(m);
        }
        boolean mafiaEjected = detectiveTurn(d);
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
    public void mafiaTurn(Mafia m) {
        Scanner in = new Scanner(System.in);
        Player victim = null;
        while(victim == null) {
            System.out.print("Choose a target: ");
            int ID = in.nextInt();
            for(Player p: playersInGame) {
                if (p.ID == ID && !(p instanceof Mafia)) {
                    victim = p;
                }
            }
            if(victim == null) {
                System.out.println("Cannot kill Player" + ID);
            }
        }
        m.kill(victim,this);
    }
}
