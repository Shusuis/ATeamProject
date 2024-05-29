package app_TETRIS;

public class GameTimer {
    private long startTime, currentTime, endTime;
    private int remainTime;
    private long limitTime = 1000000;

    public GameTimer() {
        this.startTime = this.currentTime = System.currentTimeMillis();
        this.endTime = startTime + limitTime;
    }

    public int getRemainTimeSec() {
        this.currentTime = System.currentTimeMillis();
        this.remainTime = (int) ((this.endTime - this.currentTime) / 1000);
        return remainTime;
    }
}
