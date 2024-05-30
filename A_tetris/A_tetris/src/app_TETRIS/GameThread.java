package app_TETRIS;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread {

    private GameArea ga;
    private Mino mino;
    private Mino nextMino;
    private GameTimer timer;
    private boolean confused = false;
    private int changeMinoCount = 20;
    private boolean paused = false;
    private int sleepTime = 1000;

    public GameThread() {
        this.mino = new Mino();
        this.ga = new GameArea();
        this.nextMino = new Mino();
        this.timer = new GameTimer();
    }

    public GameThread(Mino mino, GameArea ga, Mino nextMino) {
        this.mino = mino;
        this.ga = ga;
        this.nextMino = nextMino;
        this.timer = new GameTimer();
    }

    public GameThread(Mino mino, GameArea ga) {
        this.mino = mino;
        this.ga = ga;
        this.nextMino = new Mino();
        this.timer = new GameTimer();
    }

    public Mino getMino() {
        return mino;
    }

    public Mino getNextMino() {
        return nextMino;
    }

    public void setMino(Mino mino) {
        this.mino = mino;
    }

    public void setNextMino(Mino nextMino) {
        this.nextMino = nextMino;
    }

    public boolean isConfused() {
        return confused;
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
    }

    public int getChangeMinoCount() {
        return changeMinoCount;
    }

    public void setChangeMinoCount(int changeMinoCount) {
        this.changeMinoCount = changeMinoCount;
    }

    public GameTimer getTimer() {
        return this.timer;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void terminateGame() {
        ga.drawField(this);
        System.out.println("GameOver");
        System.out.println(ga.getName() + "  あなたのスコア:" + ga.getScore());
        System.exit(0);
    }
    // public void nextMino(Mino nextMino){
    // this.mino = nextMino;
    // }

    public synchronized void noti() {
        notify();
    }
    

    public void run() {

        while (true) {
            if (timer.getRemainTimeSec() < 60) {
                this.sleepTime = 500;
            } else {
                this.sleepTime = 1000;
            }

            if (isPaused()) {
                try {
                    synchronized(this) {
                        wait();
                    }
                } catch (InterruptedException exception) {}
            } else {
                
            }
            

            if (ga.isCollison(mino)) {
                // if(mino.getMinoY() <= 1){

                ga.bufferFieldAddMino(mino);
                ga.eraseLine();
                // ga.addScore();
                // ga.resetCount();
                ga.initField();
                mino.initMino();
                this.mino = nextMino;
                this.nextMino = new Mino();
                // nextMino.initMino();
            } else {
                ga.moveDown(mino);
                // ga.eraseLine();
                // ga.addScore();
                // ga.resetCount();
                ga.initField();
            }
            Mino prevMino = new Mino();
            prevMino.initMino(mino);
            prevMino.convertIntoPrev();
            while (!ga.isCollison(prevMino, prevMino.getMinoX(), prevMino.getMinoY() + 1, prevMino.getMinoAngle())) {
                ga.moveDown(prevMino);
            }
            ga.fieldAddMino(prevMino);
            ga.fieldAddMino(mino);
            ga.drawField(this);
            // System.out.println("NextMino");
            // ga.drawNextMino(nextMino);
            // ga.drawFieldAndMino(mino);
            if (mino.getMinoY() <= 1 && ga.isCollison(mino)) {
                terminateGame();
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
