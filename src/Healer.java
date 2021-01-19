import java.util.ArrayList;

public final class Healer extends Player implements Knowledgable<Healer>, Mortal{
    Healer() {
        this.HP = 800;
    }
    public ArrayList<Healer> getTeam(Game g) {       //interface Knowledgable
        ArrayList<Healer> team = new ArrayList<Healer>();
        for(Object p: g.getPlayersInGame()) {
            if(p instanceof Healer) {
                team.add((Healer) p);
            }
        }
        return team;
    }
    public void getsKilled(Game g){         //interface Mortal
        this.isInGame = false;
        g.getPlayersInGame().remove(this);
        g.setNumPlayers(g.getNumPlayers() - 1);
        g.setNumHealer(g.getNumHealer() - 1);
        System.out.println("Player" + this.ID + " has died.");
    }
    public void heal(Player p){
        p.getsHPBoost(500);
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
}
