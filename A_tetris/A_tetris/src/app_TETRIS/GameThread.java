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
    //public void nextMino(Mino nextMino){ 
      //  this.mino = nextMino;
    //}

    public void run() {

        while (true) {
            ga.moveDown(mino);
            System.out.println();
            System.out.println("残り時間： " + timer.getRemainTimeSec() + "秒");
            System.out.println("残りミノ変更回数： " + getChangeMinoCount() + "回 ");
            if (timer.getRemainTimeSec() < 30) {
                setConfused(true);
                System.out.println("　操作反転中！！" );
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
                //nextMino.initMino(); 
            } else {
                // ga.eraseLine();
                // ga.addScore();
                // ga.resetCount();
                Mino prevMino = new Mino();
                prevMino.initMino(mino);
                prevMino.convertIntoPrev();
                while (!ga.isCollison(prevMino, prevMino.getMinoX(), prevMino.getMinoY() + 1, prevMino.getMinoAngle())) {
                    ga.moveDown(prevMino);
                }
                ga.initField();
                ga.fieldAddMino(prevMino);
                ga.fieldAddMino(mino);
            }
            ga.drawField();
            System.out.println("NextMino"); 
            ga.drawNextMino(nextMino); 
            // ga.drawFieldAndMino(mino);
            if(mino.getMinoY() <= 1 && ga.isCollison(mino)){ 
                ga.drawField();
                System.out.println("GameOver");
                System.out.println(ga.getName() + "  あなたのスコア:" + ga.getScore());
                System.exit(0);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
