import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public abstract class Game <T>{
    protected int numPlayers;
    protected int numMafia;
    protected int numDetective;
    protected int numHealer;
    protected int numCommoner;
    protected int roundNumber;
    protected T User = null;
    protected ArrayList<Player> originalPlayers;
    protected ArrayList<Player> playersInGame;

    Game(int numPlayers){
        this.numPlayers = numPlayers;
        this.numMafia = numPlayers / 5;
        this.numDetective = numPlayers / 5;
        this.numHealer = Math.max(1, numPlayers / 10);
        this.numCommoner = numPlayers - (this.numMafia + this.numDetective + this.numHealer);
        this.originalPlayers = new ArrayList<Player>();
        this.playersInGame = new ArrayList<Player>();
        this.roundNumber = 1;
    }

    public final int getNumPlayers() { return numPlayers; }
    public final void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }

    public final T getUser() { return User; }
    public final void setUser(T user) { User = user; }

    public final ArrayList<Player> getPlayersInGame() { return playersInGame; }
    public final ArrayList<Player> getOriginalPlayers() { return originalPlayers; }

    public final int getNumCommoner() { return numCommoner; }
    public final void setNumCommoner(int numCommoner) { this.numCommoner = numCommoner; }

    public final int getNumDetective() { return numDetective; }
    public final void setNumDetective(int numDetective) { this.numDetective = numDetective; }

    public final int getNumHealer() { return numHealer; }
    public final void setNumHealer(int numHealer) { this.numHealer = numHealer; }

    public final int getNumMafia() { return numMafia; }
    public final void setNumMafia(int numMafia) { this.numMafia = numMafia; }

    public final int getTotalMafiaHP() {
        int totalHP = 0;
        for (Player p : playersInGame) {
            if (p instanceof Mafia) {
                totalHP += p.HP;
            }
        }
        return totalHP;
    }

    public final void startGame() {
        makePlayers();
        allocateUser();
        while(numMafia > 0 && numMafia < numPlayers - numMafia) {
            executeRound();
        }
        System.out.println("Game Over.");
        if(numMafia >= numPlayers - numMafia) {
            System.out.println("Mafia wins.");
        } else {
            System.out.println("People win.");
        }
        printPlayerDetails();
    }

    private void addToList(Player p){
        this.originalPlayers.add(p);
        this.playersInGame.add(p);
    }

    private void makePlayers(){
        for(int i = 0; i < numMafia; i++) {
            Mafia player = new Mafia();
            addToList(player);
        }
        for(int i = 0; i < numDetective; i++) {
            Detective player = new Detective();
            addToList(player);
        }
        for(int i = 0; i < numHealer; i++) {
            Healer player = new Healer();
            addToList(player);
        }
        for(int i = 0; i < numCommoner; i++) {
            Commoner player = new Commoner();
            addToList(player);
        }
    }

    public abstract void allocateUser();

    public abstract void executeRound();

    public void mafiaTurn(Mafia m) {
        try {
            Player victim = null;
            while(victim == null) {
                Player p = playersInGame.get(rand.nextInt(numPlayers));
                if(!(p instanceof Mafia)) {
                    victim = p;
                }
            }
            m.kill(victim,this);
        } catch(NullPointerException e) {
            //nothing to do
        } finally {
            System.out.println("Mafias have chosen their target.");
        }
    }

    public boolean detectiveTurn(Detective d) {
        boolean mafiaEjected = false;
        try {
            Player target = playersInGame.get(rand.nextInt(numPlayers));
            String identity = d.test(target);
            if (!identity.equals("detective")) {
                if (identity.equals("mafia")) {
                    mafiaEjected = true;
                    System.out.println("Mafia found and voted out. Player" + target.ID + " is Mafia.");
                    target.votedOut(this);
                }
            }
        } catch (NullPointerException e){

        } finally {
            System.out.println("Detectives have chosen a player to test.");
        }
        return mafiaEjected;
    }

    public void healerTurn(Healer h) {
        try {
            h.heal(playersInGame.get(rand.nextInt(numPlayers)));
        } catch(NullPointerException e) {
            //nothing to do
        } finally {
            System.out.println("Healers have chosen someone to heal.");
        }
    }

    public final Player votingRound() {
        HashMap<Player, Integer> votes = new HashMap<Player, Integer>();
        for(Player p: playersInGame) {
            if(p != User) {
                Player nominee = null;
                while(nominee == null) {
                    nominee = p.vote(playersInGame.get(rand.nextInt(numPlayers)).getID(), this);
                }
                if (votes.containsKey(nominee)) {
                    int newVotes = votes.get(nominee) + 1;
                    votes.replace(nominee, newVotes);
                } else {
                    votes.put(nominee, 1);
                }
            }
        }

        if(playersInGame.contains(User)) {
            Player nominee = userVotingTurn();
            if (votes.containsKey(nominee)) {
                int newVotes = votes.get(nominee) + 1;
                votes.replace(nominee, newVotes);
            } else {
                votes.put(nominee, 1);
            }
        }

        int maxVotes = 0;
        Player nominated = null;
        for(Player p: votes.keySet()) {
            if(votes.get(p) > maxVotes) {
                maxVotes = votes.get(p);
                nominated = p;
            }
        }
        nominated.votedOut(this);
        return nominated;
    }

    private Player userVotingTurn() {
        Scanner in = new Scanner(System.in);
        Player nominee = null;
        while(nominee == null){
            System.out.print("Select a person to vote out: ");
            int ID = in.nextInt();
            nominee = ((Player) User).vote(ID, this);
            if(nominee == null || nominee == User) {
                System.out.println("Cannot vote Player" + ID);
                nominee = null;
            }
        }
        return nominee;
    }

    public final void removeDead() {
        for(int i=0; i < playersInGame.size(); i++) {
            Player p = playersInGame.get(i);
            if(p.getHP() == 0 && !(p instanceof Mafia)) {
                if(p instanceof Detective) {
                    ((Detective) p).getsKilled(this);
                } else if(p instanceof Healer) {
                    ((Healer) p).getsKilled(this);
                } else if(p instanceof Commoner){
                    ((Commoner) p).getsKilled(this);
                }
                i--;
            }
        }
    }

    private void printPlayerDetails() {
        for(Player p: originalPlayers) {
            if(p instanceof Mafia) {
                System.out.print("Player" + p.ID + ", ");
            }
        }
        System.out.println("were Mafias.");

        for(Player p: originalPlayers) {
            if(p instanceof Detective) {
                System.out.print("Player" + p.ID + ", ");
            }
        }
        System.out.println("were Detectives.");

        for(Player p: originalPlayers) {
            if(p instanceof Healer) {
                System.out.print("Player" + p.ID + ", ");
            }
        }
        System.out.println("were Healers.");

        for(Player p: originalPlayers) {
            if(p instanceof Commoner) {
                System.out.print("Player" + p.ID + ", ");
            }
        }
        System.out.println("were Commoners.");
    }
    Random rand = new Random();
}
