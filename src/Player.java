import java.util.Random;

public class Player implements Voter{
    private static int IDtoAssign = 1;
    protected int ID;
    protected int HP;
    protected boolean isInGame;

    Player(){
        this.ID = IDtoAssign;
        IDtoAssign++;
        this.isInGame = true;
    }

    public int getID(){ return ID; }
    public void setID(int ID){ this.ID = ID; }

    public int getHP(){ return HP; }
    public void setHP(int HP){ this.HP = HP; }

    public boolean getIsInGame(){ return isInGame; }
    public void setIsInGame(boolean status){ this.isInGame = status; }

    protected void getsHPBoost(int boost){
        this.HP += boost;
    }

    public Player vote(int ID, Game g){     //interface Voter
        Player nominee = null;
        for(Object p: g.getPlayersInGame()) {
            if(((Player) p).ID == ID) {
                nominee = (Player) p;
            }
        }
        return nominee;
    }

    public void votedOut(Game g){       //interface Voter
        this.isInGame = false;
        g.getPlayersInGame().remove(this);
        g.setNumPlayers(g.getNumPlayers() - 1);
        if(this instanceof Detective) {
            g.setNumDetective(g.getNumDetective() - 1);
        } else if(this instanceof Mafia) {
            g.setNumMafia(g.getNumMafia() - 1);
        } else if(this instanceof Commoner) {
            g.setNumCommoner(g.getNumCommoner() - 1);
        } else {
            g.setNumHealer(g.getNumHealer() - 1);
        }
    }
    Random rand = new Random();
}