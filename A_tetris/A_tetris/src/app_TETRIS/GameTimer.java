package app_TETRIS;

public class GameTimer {
    private long startTime, currentTime, endTime;
    private long limitTime = 300000;

    public GameTimer() {
        this.startTime = this.currentTime = System.currentTimeMillis();
        this.endTime = startTime + limitTime;
    }

    public int getRemainTimeSec() {
        this.currentTime = System.currentTimeMillis();
        int remainTime = (int) ((this.endTime - this.currentTime) / 1000);
        return remainTime;
    }

    public double getRemainTimeSecDouble() {
        this.currentTime = System.currentTimeMillis();
        double remainTime = (double) ((this.endTime - this.currentTime) / 1000.0);
        remainTime = (Math.round(remainTime * 100.0)) / 100.0;
        return remainTime;
    }


}
