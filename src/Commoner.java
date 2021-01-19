public final class Commoner extends Player implements Mortal{
    Commoner() {
        this.HP = 1000;
    }
    public void getsKilled(Game g){         //interface Mortal
        this.isInGame = false;
        g.getPlayersInGame().remove(this);
        g.setNumPlayers(g.getNumPlayers() - 1);
        g.setNumCommoner(g.getNumCommoner() - 1);
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
}
