public final class UserIsCommonerGame extends Game<Commoner>{
    UserIsCommonerGame(int numPlayers) {
        super(numPlayers);
    }

    public void allocateUser(){
        boolean done = false;
        while(!done){
            for(Player p: playersInGame) {
                if(p instanceof Commoner && User == null) {
                    User = (Commoner) p;
                    System.out.println("You are Player" + p.ID + ".");
                    System.out.println("You are a Commoner.");
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

        mafiaTurn(m);
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
}
