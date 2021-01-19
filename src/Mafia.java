import java.util.ArrayList;

public final class Mafia extends Player implements Knowledgable<Mafia>{
    Mafia() {
        this.HP = 2500;
    }
    public ArrayList<Mafia> getTeam(Game g) {       //interface Knowledgable
        ArrayList<Mafia> team = new ArrayList<Mafia>();
        for(Object p: g.getPlayersInGame()) {
            if(p instanceof Mafia) {
                team.add((Mafia) p);
            }
        }
        return team;
    }
    public void kill(Player victim, Game g){
        int targetHP;
        if(victim instanceof Detective) {
            targetHP = ((Detective) victim).reduceHP(g);
        } else if(victim instanceof Healer) {
            targetHP = ((Healer) victim).reduceHP(g);
        } else {
            targetHP = ((Commoner) victim).reduceHP(g);
        }
        int HPtoDeduct = targetHP / g.numMafia;
        int HPLeft = 0;
        for(Object p: g.getPlayersInGame()) {
            if(p instanceof Mafia) {
                HPLeft += updateHPAfterKill(HPtoDeduct);
            }
        }
        for(Object p: g.getPlayersInGame()) {
            if(HPLeft > 0 && p instanceof Mafia) {
                HPLeft = updateHPAfterKill(HPLeft);
            }
        }
    }
    public int updateHPAfterKill(int HPtoDeduct) {
        int HPLeft;
        if(this.HP < HPtoDeduct) {
            HPLeft = HPtoDeduct - this.HP;
            this.HP = 0;
        }
        else{
            HPLeft = 0;
            this.HP -= HPtoDeduct;
        }
        return HPLeft;
    }
}
