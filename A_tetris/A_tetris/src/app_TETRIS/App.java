package app_TETRIS;

import javax.swing.JFrame;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.Scanner;

public class App extends JFrame {
    // オブジェクトをAppクラスで利用できるようにフィールド（メンバ変数）を準備
    GameArea ga;
    Mino mino;
    Mino nextMino;
    GameThread gt;
    static GameArea ga2;
    // Appコンストラクタ(Appをインスタンス化した際に1回呼び出されるメソッド)
    public App() {
        // オブジェクトをインスタンス化してAppクラスフィールド（メンバ変数）へ追加
        this.mino = new Mino();
        //this.ga = new GameArea();
        this.ga = ga2;
        
        this.nextMino = new Mino();
        // スレッド呼び出し
        //new GameThread(mino, ga).start();
        gt = new GameThread(mino, ga);
        gt.start();
        // コントローラーの呼び出し
        initControls();
    }

    // mainメソッド 1番最初に動く特別なメソッド
    public static void main(String[] args) throws Exception {
        System.out.println("Tetris");
        System.out.print("your name:");

        Scanner sc = new Scanner(System.in, "Shift-JIS");
        String name = sc.nextLine();
        
        
        // 名前入力においてのルール
        int l = name.length();
        if(0 < l && l <= 16) {
            System.out.println("Welcome, " + name + "!"); 
            //GameArea player = new GameArea();
            //player.setName(name);
            ga2 = new GameArea();
            ga2.setName(name);
            
        } else {
            System.out.println("Guest");
            //GameArea player = new GameArea();
            //player.setName("ゲスト");
            ga2 = new GameArea();
            ga2.setName("Guest");
        }
        
        System.out.println("Please hit EnterKey and start the game!");
        while ((System.in.read()) != '\n') ;
    
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new App();
                f.setVisible(true);
            }
        });
        sc.close();

        //ゲームオーバーの定義
        //固定されてるミノ（buffer）と出現したミノが重なる
        //「重なる」ことを示すコード
        //固定ミノ＝initmino

        //なぜ重なるのか？→
        // 上限として設定されているfieldAreaを超えないように置きたいから
        // →新しく出てきたミノが固定化される前に上限をbufferが超えていたらゲームオーバー

        //GameThread gt = new GameThread();
        //GameThread.mino.initMino();
        //GameThread.ga.bufferFieldAddMino();

        //if(ga.bufferFieldAddMino() > Mino.initMino()) { //ゲームオーバーの定義
            //System.out.println("Game Over");
            //System.out.println(name + "　あなたのスコア：" + "100");//A班からもらう変数
            
        //}


    }

    private void initControls() {
        InputMap im = this.getRootPane().getInputMap();
        ActionMap am = this.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        am.put("right", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gt.isConfused()) {
                    if (ga.isCollison(gt.getMino(), gt.getMino().getMinoX() + 1, gt.getMino().getMinoY(), gt.getMino().getMinoAngle()) == false) {
                        ga.moveRight(gt.getMino());
                        ga.drawFieldAndMino(gt);
                    }
                } else {
                    if (!ga.isCollison(gt.getMino(), gt.getMino().getMinoX() - 1, gt.getMino().getMinoY(), gt.getMino().getMinoAngle())) {
                        ga.moveLeft(gt.getMino());
                        ga.drawFieldAndMino(gt);
                    }
                }
                
            }
        });

        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        am.put("left", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gt.isConfused()) {
                    if (!ga.isCollison(gt.getMino(), gt.getMino().getMinoX() - 1, gt.getMino().getMinoY(), gt.getMino().getMinoAngle())) {
                            ga.moveLeft(gt.getMino());
                            ga.drawFieldAndMino(gt);
                    }
                    } else {
                        if (ga.isCollison(gt.getMino(), gt.getMino().getMinoX() + 1, gt.getMino().getMinoY(), gt.getMino().getMinoAngle()) == false) {
                            ga.moveRight(gt.getMino());
                            ga.drawFieldAndMino(gt);
                        
                    }
                }
                
            }
        });

        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        am.put("down", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gt.isConfused()) {
                    if (!ga.isCollison(gt.getMino(), gt.getMino().getMinoX(), gt.getMino().getMinoY() + 1, gt.getMino().getMinoAngle())) {
                        ga.moveDown(gt.getMino());
                        ga.drawFieldAndMino(gt);
                    }    
                } else {
                    if (!ga.isCollison(gt.getMino(), gt.getMino().getMinoX(), gt.getMino().getMinoY(),
                        (gt.getMino().getMinoAngle() + 1) % gt.getMino().getMinoAngleSize())) {
                        ga.rotation(gt.getMino());
                        ga.drawFieldAndMino(gt);
                    }
                }
                
                
            }
        });

        im.put(KeyStroke.getKeyStroke("UP"), "up");
        am.put("up", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gt.isConfused()) {
                    if (!ga.isCollison(gt.getMino(), gt.getMino().getMinoX(), gt.getMino().getMinoY(),
                        (gt.getMino().getMinoAngle() + 1) % gt.getMino().getMinoAngleSize())) {
                         ga.rotation(gt.getMino());
                         ga.drawFieldAndMino(gt);
                }
                } else {
                    if (!ga.isCollison(gt.getMino(), gt.getMino().getMinoX(), gt.getMino().getMinoY() + 1, gt.getMino().getMinoAngle())) {
                        ga.moveDown(gt.getMino());
                        ga.drawFieldAndMino(gt);
                    }  
                }

                
            }
        });

        im.put(KeyStroke.getKeyStroke("R"), "r");
        am.put("r", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (gt.getChangeMinoCount() > 0) {
                    gt.setNextMino(new Mino());
                    gt.setChangeMinoCount(gt.getChangeMinoCount() - 1);
                }
                ga.drawFieldAndMino(gt);
            }
        });

        im.put(KeyStroke.getKeyStroke("SPACE"), "s");
        am.put("s", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                while (!ga.isCollison(gt.getMino(), gt.getMino().getMinoX(), gt.getMino().getMinoY() + 1, gt.getMino().getMinoAngle())) {
                    ga.moveDown(gt.getMino());
                }
                ga.drawFieldAndMino(gt);
            }
        });

        im.put(KeyStroke.getKeyStroke("P"), "p");
        am.put("p", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (gt.isPaused()) {
                    gt.setPaused(false);
                    gt.noti();
                } else {
                    gt.setPaused(true);
                }
                
                
            }
        });
    }
}
