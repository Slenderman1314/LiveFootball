package ifp.project.livefootball.Match;

public class MatchStatistics {
    private int localScore;
    private int guestScore;
    private int localYellowCards;
    private int guestYellowCards;
    private int localRedCards;
    private int guestRedCards;

    public MatchStatistics(int localScore, int guestScore, int localYellowCards, int guestYellowCards, int localRedCards, int guestRedCards) {
        this.localScore = localScore;
        this.guestScore = guestScore;
        this.localYellowCards = localYellowCards;
        this.guestYellowCards = guestYellowCards;
        this.localRedCards = localRedCards;
        this.guestRedCards = guestRedCards;
    }

    public int getLocalScore() {
        return localScore;
    }

    public int getGuestScore() {
        return guestScore;
    }

    public int getLocalYellowCards() {
        return localYellowCards;
    }

    public int getGuestYellowCards() {
        return guestYellowCards;
    }

    public int getLocalRedCards() {
        return localRedCards;
    }

    public int getGuestRedCards() {
        return guestRedCards;
    }

}
