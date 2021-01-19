import java.util.ArrayList;

public final class Detective extends Player implements Knowledgable<Detective>, Mortal{
    Detective() {
        this.HP = 800;
    }
    public ArrayList<Detective> getTeam(Game g) {       //interface Knowledgable
        ArrayList<Detective> team = new ArrayList<Detective>();
        for(Object p: g.getPlayersInGame()) {
            if(p instanceof Detective) {
                team.add((Detective) p);
            }
        }
        return team;
    }
    public void getsKilled(Game g) {        //interface Mortal
        this.isInGame = false;
        g.getPlayersInGame().remove(this);
        g.setNumPlayers(g.getNumPlayers() - 1);
        g.setNumDetective(g.getNumDetective() - 1);
        System.out.println("Player" + this.ID + " has died.");
    }
    public int reduceHP(Game g) {       //interface Mortal
        int targetHP = this.HP;
        if(this.HP <= g.getTotalMafiaHP()) {
            this.HP = 0;
        } else {
            this.HP -= g.getTotalMafiaHP();
        }
        return targetHP;
    }
    public String test(Player p) {
        String identity;
        if(p instanceof Detective) {
            identity = "detective";
        } else if(p instanceof Mafia) {
            identity = "mafia";
        } else {
            identity = "not mafia";
        }
        return identity;
    }
}
