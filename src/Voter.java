public interface Voter {
    Player vote(int ID, Game g);
    void votedOut(Game g);
}
