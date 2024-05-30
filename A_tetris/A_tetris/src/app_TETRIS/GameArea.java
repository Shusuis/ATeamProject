package app_TETRIS;

public class GameArea { // 15結合済み
    private int fieldHight = 21;
    private int fieldWidth = 15;
    private int grandHight = 30; // 広めに確保
    private int grandWidth = 20; // 広めに確保
    private int[][] field;
    private int[][] bufferField;
    private int score = 0;
    private int linecount = 0;
    private int allLinecount = 0;
    private String name;

    public GameArea() {
        // this.mino = mino;
        this.field = new int[grandHight][grandWidth];
        this.bufferField = new int[grandHight][grandWidth];
        initBufferField();
        initField();
    }

    public int getScore() {
        return this.score;
    }

    public int getcount() {
        return this.linecount;
    }

    public int resetCount() {
        this.linecount = 0;
        return this.linecount;
    }

    public int getFieldHight() {
        return this.fieldHight;
    }

    public int getFieldWidth() {
        return this.fieldWidth;
    }

    public int getGrandHight() {
        return this.grandHight;
    }

    public int getGrandWidth() {
        return this.grandWidth;
    }

    public int[][] getBufferField() {
        return this.bufferField;
    }

    public int[][] getField() {
        return this.field;
    }

    public GameArea(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 描画用Field初期化
    public void initField() {
        for (int y = 0; y < getFieldHight(); y++) {
            for (int x = 0; x < getFieldWidth(); x++) {
                // if (bufferField[y][x] == 2) {bufferField[y][x] = 0;}
                field[y][x] = bufferField[y][x];
            }
        }
    }

    // 壁用BufferField初期化
    public void initBufferField() {
        for (int y = 0; y < getFieldHight(); y++) {
            for (int x = 0; x < getFieldWidth(); x++) {
                bufferField[y][x] = 0;
            }
        }
        for (int y = 0; y < getFieldHight(); y++) {
            bufferField[y][0] = bufferField[y][1] = bufferField[y][2] = bufferField[y][3] = bufferField[y][getFieldWidth()
                    - 1] = 1;
        }
        for (int x = 0; x < getFieldWidth(); x++) {
            bufferField[getFieldHight() - 1][x] = 1;
        }
    }

    // スレッドに描画
    public void drawField() {
        for (int y = 0; y < getFieldHight(); y++) {
            for (int x = 0; x < getFieldWidth() - 3; x++) {
                System.out.printf("%s", (field[y][x + 3] == 1 ? "回" : field[y][x + 3] == 2 ? "口" : "・"));
            }
            System.out.println();
        }
        System.out.println("##########################################");
        System.out.print("消したライン数：" + allLinecount + "   ");
        System.out.print("名前:" + name + "   ");
        System.out.println("スコア：" + score);
        System.out.println("##########################################");

    }

    public void drawField(GameThread gt) {
        System.out.println("\n\n\n\n\n\n\n");
        for (int y = 0; y < getFieldHight(); y++) {
            for (int x = 0; x < getFieldWidth() - 3; x++) {
                System.out.printf("%s", (field[y][x + 3] == 1 ? "回" : field[y][x + 3] == 2 ? "口" : "・"));
            }
            System.out.println();
        }
        System.out.println("####################################################");
        System.out.print("Name: " + name + "     ");
        System.out.print("Lines：" + allLinecount + "     ");
        System.out.println("Score：" + score);
        System.out.print("Time：" + gt.getTimer().getRemainTimeSec() + "s" + "       ");
        System.out.printf("Speed：%s\n", gt.getTimer().getRemainTimeSec() < 60 ? "High" : "Normal");
        System.out.println("ChangeNextMino(R key):" + gt.getChangeMinoCount() + "");
        System.out.println("NextMino:");
        drawNextMino(gt.getNextMino());
        if (gt.getTimer().getRemainTimeSec() < 30) {
            gt.setConfused(true);
            System.out.println("　!! Input Reversal !!");
            if (gt.getTimer().getRemainTimeSec() <= 0) {
                gt.terminateGame();
            }
        }
        System.out.println("####################################################");
    }

    // fieldの下にnextMinoを出力
    public void drawNextMino(Mino nextMino) {

        int[][][] m = nextMino.getMino();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                System.out.printf("%s", (m[0][y][x] == 1 ? "回" : "・"));
            }
            System.out.println();
        }

    }

    // コントローラー用再描画メソッド
    // public void drawFieldAndMino(Mino mino, Mino nextMino) {
    // if (isCollison(mino)) {
    // bufferFieldAddMino(mino);
    // eraseLine();
    // // addScore(); //操作したタイミングでしか機能しない
    // // resetCount();
    // initField();
    // mino.initMino();
    // } else {
    // // eraseLine();
    // initField();
    // fieldAddMino(mino);
    // // addScore();
    // // resetCount();
    // }
    // drawField(nextMino);
    // System.out.println();
    // // resetCount(); // 点数が加算され続ける
    // }

    public void drawFieldAndMino(GameThread gt) {
        if (isCollison(gt.getMino())) {
            bufferFieldAddMino(gt.getMino());
            eraseLine();
            // addScore(); //操作したタイミングでしか機能しない
            // resetCount();
            initField();
            gt.getMino().initMino();
            gt.setMino(gt.getNextMino());
            gt.setNextMino(new Mino());
        } else {
            // eraseLine();
            initField();
            // addScore();
            // resetCount();
        }
        Mino prevMino = new Mino();
        prevMino.initMino(gt.getMino());
        prevMino.convertIntoPrev();
        while (!isCollison(prevMino, prevMino.getMinoX(), prevMino.getMinoY() + 1, prevMino.getMinoAngle())) {
            moveDown(prevMino);
        }
        fieldAddMino(prevMino);
        fieldAddMino(gt.getMino());
        drawField(gt);
        System.out.println();
        // resetCount(); // 点数が加算され続ける
    }

    public void fieldAddMino(Mino mino) {
        for (int y = 0; y < mino.getMinoSize(); y++) {
            for (int x = 0; x < mino.getMinoSize(); x++) {
                this.field[mino.getMinoY() + y][mino.getMinoX() + x] |= mino.getMino()[mino.getMinoAngle()][y][x];
                if (this.field[mino.getMinoY() + y][mino.getMinoX() + x] > 2) {
                    this.field[mino.getMinoY() + y][mino.getMinoX() + x] = 1;
                }
            }
        }
    }

    public void bufferFieldAddMino(Mino mino) {
        for (int y = 0; y < mino.getMinoSize(); y++) {
            for (int x = 0; x < mino.getMinoSize(); x++) {
                this.bufferField[mino.getMinoY() + y][mino.getMinoX() + x] |= mino.getMino()[mino.getMinoAngle()][y][x];
            }
        }
    }

    // 当たり判定 自動落下用
    public boolean isCollison(Mino mino) {
        for (int r = 0; r < mino.getMinoSize(); r++) {
            for (int c = 0; c < mino.getMinoSize(); c++) {
                // 1カラム下の行を確認して1があるか確認
                if (this.bufferField[mino.getMinoY() + r + 1][mino.getMinoX() + c] == 1
                        && mino.getMino()[mino.getMinoAngle()][r][c] == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // 当たり判定 コントローラー用
    public boolean isCollison(Mino mino, int _x, int _y, int _angle) {
        for (int r = 0; r < mino.getMinoSize(); r++) {
            for (int c = 0; c < mino.getMinoSize(); c++) {
                if (getBufferField()[_y + r][_x + c] == 1 && mino.getMino()[_angle][r][c] >= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // ライン削除処理
    public void eraseLine() {
        boolean isFill = true;
        resetCount();

        for (int y = getFieldHight() - 2; y > 0; y--) {
            for (int x = 1; x < getFieldWidth() - 1; x++) {
                if (bufferField[y][x] == 0) {
                    isFill = false;
                }
            }
            if (isFill) {
                for (int _y = y - 1; _y > 0; _y--) {
                    for (int x = 0; x < getFieldWidth(); x++) {
                        bufferField[_y + 1][x] = bufferField[_y][x];
                    } // for end
                }
                this.linecount++;
                this.allLinecount++;
                y = getFieldHight() - 1;
            } // if end
            isFill = true;
            // addScore(); //1行ごとに処理された
            // resetCount(); // 0のまま更新されない
        } // for end
        addScore();
        // resetCount();

    }

    public void addScore() { // スコア計算を行う

        int count = getcount(); // 消えたライン数を数える
        int intMax = 21_4748_3647;
        // if(score == intMax){
        // System.out.println("max_score");
        // }else{
        switch (count) {// ライン数によって加算されるスコアの場合分け
            case 1:
                if (score - 40 > intMax - 40) {
                    score = intMax;
                } else {
                    score += 40;
                }
                break;
            case 2:
                if (score - 100 > intMax - 100) {
                    score = intMax;
                } else {
                    score += 100;
                }
                break;
            case 3:
                if (score - 300 > intMax - 300) {
                    score = intMax;
                } else {
                    score += 300;
                }
                break;
            case 4:
                if (score - 1200 > intMax - 1200) {
                    score = intMax;
                } else {
                    score += 1200;
                }
                break;
            default:
                score += 0;
                break;
        }

    }

    // }

    // コントローラー呼び出しメソッド← ↓ → 回転 ↑
    public void moveDown(Mino mino) {
        mino.addMinoY();
    }

    public void moveRight(Mino mino) {
        mino.addMinoX();
    }

    public void moveLeft(Mino mino) {
        mino.decMinoX();
    }

    public void rotation(Mino mino) {
        mino.setMinoAngle((mino.getMinoAngle() + 1) % mino.getMinoAngleSize());
    }

}
