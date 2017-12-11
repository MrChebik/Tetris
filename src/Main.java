import model.Cube;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main extends JPanel {
    static JMenuBar menu = new JMenuBar();
    static JFrame f = new JFrame("Tetris");
    final int SCALE = 30;
    int time_turn_going = 0;
    int n = 0;
    int col = 0;
    int col1 = 0;
    int col_plains = 0;
    int col_equal = 0;
    int random_object;
    ArrayList<Cube> cubes;
    Cube[] currCube = new Cube[4];

    Timer turn = new Timer(150, e -> {
        time_turn_going = 1;
        repaint();
    });

    Timer climb_down = new Timer(800, e -> {
        for (int i = 0; i < 4; i++)
            if (currCube[i].getY() > 360) {
                save_information();
                create_new_object();
                break;
            }
        if (cubes.size() >= 12) {
            int index_of_del = -1;

            for (int line = 390; line >= 0; line -= 30) {
                col_plains = 0;
                for (int i = 0; i < cubes.size(); i++) {
                    if (cubes.get(i).getY() == line) {
                        col_plains++;
                    }
                }
                if (col_plains == 10) {
                    for (int i = 0; i < cubes.size(); i++) {
                        if (cubes.get(i).getY() == line) {
                            if (index_of_del == -1) {
                                index_of_del = cubes.get(i).getY();
                            }
                            cubes.remove(i);
                            i--;
                        }
                    }
                }
                if (index_of_del != -1) {
                    for (Cube cube : cubes) {
                        if (cube.getY() < index_of_del) {
                            cube.setXYOfPlus(0, 30);
                        }
                    }
                    index_of_del = -1;
                    line += 30;
                }
            }
        }
        if (turn.isRunning()) {
            turn.stop();
        }
        repaint();
    });
    Timer down = new Timer(50, e -> {
        for (int i = 0; i < 4; i++)
            if (currCube[i].getY() > 360) {
                save_information();
                create_new_object();
                break;
            }
        repaint();
        this.down.stop();
    });

    Main(int x, int y) {
        currCube[0] = new Cube(0, 0, 0);
        currCube[1] = new Cube(0, 0, 0);
        currCube[2] = new Cube(0, 0, 0);
        currCube[3] = new Cube(0, 0, 0);

        cubes = new ArrayList<>();

        create_new_object();
        setLocation(x, y);
        setSize(f.getWidth(), f.getHeight());
        setLayout(null);
        JMenu file = new JMenu("File");
        menu.add(file);
        JMenuItem new_game = new JMenuItem("New Game");
        new_game.addActionListener(e -> {
            create_new_object();
            cubes.clear();
            if (!climb_down.isRunning())
                climb_down.start();
            repaint();
        });
        file.add(new_game);
        file.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        file.add(exit);
        JMenu help = new JMenu("Help");
        menu.add(help);
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(null, "Version:      0.2\nDeveloper:  MrChebik", "About", JOptionPane.PLAIN_MESSAGE));
        help.add(about);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && climb_down.isRunning()) {
                    col = 0;
                    for (int i = 0; i < cubes.size(); i++) {
                        col1 = 0;
                        for (int j = 0; j < 4; j++) {
                            if (cubes.get(i).getX() == currCube[j].getX() + 30 && cubes.get(i).getY() == currCube[j].getY()) {
                                col1++;
                            }
                        }
                        if (col1 != 0) {
                            col++;
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        if (currCube[i].getX() + 30 > 270) {
                            col++;
                        }
                    }
                    if (col == 0) {
                        for (int i = 0; i < 4; i++) {
                            currCube[i].setX(currCube[i].getX() + 30);
                        }
                    }
                }
                if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && climb_down.isRunning()) {
                    col = 0;
                    for (int i = 0; i < cubes.size(); i++) {
                        col1 = 0;
                        for (int j = 0; j < 4; j++) {
                            if (cubes.get(i).getX() == currCube[j].getX() - 30 && cubes.get(i).getY() == currCube[j].getY()) {
                                col1++;
                            }
                        }
                        if (col1 != 0) {
                            col++;
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        if (currCube[i].getX() - 30 < 0) {
                            col++;
                        }
                    }
                    if (col == 0) {
                        for (int i = 0; i < 4; i++) {
                            currCube[i].setX(currCube[i].getX() - 30);
                        }
                    }
                }
                if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && climb_down.isRunning()) {
                    down.start();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP || key == KeyEvent.VK_ENTER || key == KeyEvent.VK_W) && time_turn_going == 0 && climb_down.isRunning()) {
                    if (random_object == 0) {
                        if (n == 0) {
                            currCube[0].setXYOfPlus(+60, -90);
                            currCube[1].setXYOfPlus(+30, -60);
                            currCube[3].setXYOfPlus(-30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(-60, +90);
                                currCube[1].setXYOfPlus(-30, +60);
                                currCube[3].setXYOfPlus(+30, +30);
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else {
                            currCube[0].setXYOfPlus(-60, +90);
                            currCube[1].setXYOfPlus(-30, +60);
                            currCube[3].setXYOfPlus(+30, +30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(60, -90);
                                currCube[1].setXYOfPlus(30, -60);
                                currCube[3].setXYOfPlus(-30, -30);
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    } if (random_object == 1) {
                        if (n == 0) {
                            currCube[0].setXYOfPlus(60, 0);
                            currCube[1].setXYOfPlus(30, -30);
                            currCube[3].setXYOfPlus(-30, 30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(-60, 0);
                                currCube[1].setXYOfPlus(-30, +30);
                                currCube[3].setXYOfPlus(+30, -30);
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else if (n == 1) {
                            currCube[0].setXYOfPlus(0, 60);
                            currCube[1].setXYOfPlus(30, 30);
                            currCube[3].setXYOfPlus(-30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(0, -60);
                                currCube[1].setXYOfPlus(-30, -30);
                                currCube[3].setXYOfPlus(+30, +30);
                            } else {
                                n = 2;
                                turn.start();
                            }
                        } else if (n == 2) {
                            currCube[0].setXYOfPlus(-60, 0);
                            currCube[1].setXYOfPlus(-30, 30);
                            currCube[3].setXYOfPlus(30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(+60, 0);
                                currCube[1].setXYOfPlus(+30, -30);
                                currCube[3].setXYOfPlus(-30, +30);

                            } else {
                                n = 3;
                                turn.start();
                            }
                        } else {
                            currCube[0].setXYOfPlus(0, -60);
                            currCube[1].setXYOfPlus(-30, -30);
                            currCube[3].setXYOfPlus(30, 30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(0, +60);
                                currCube[1].setXYOfPlus(+30, +30);
                                currCube[3].setXYOfPlus(-30, -30);
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    } if (random_object == 2) {
                        if (n == 0) {
                            currCube[0].setXYOfPlus(0, +60);
                            currCube[1].setXYOfPlus(30, -30);
                            currCube[3].setXYOfPlus(-30, +30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(0, -60);
                                currCube[1].setXYOfPlus(-30, +30);
                                currCube[3].setXYOfPlus(+30, -30);
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else if (n == 1) {
                            currCube[0].setXYOfPlus(-60, 0);
                            currCube[1].setXYOfPlus(+30, +30);
                            currCube[3].setXYOfPlus(-30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(60, 0);
                                currCube[1].setXYOfPlus(-30, -30);
                                currCube[3].setXYOfPlus(+30, +30);
                            } else {
                                n = 2;
                                turn.start();
                            }
                        } else if (n == 2) {
                            currCube[0].setXYOfPlus(0, -60);
                            currCube[1].setXYOfPlus(-30, +30);
                            currCube[3].setXYOfPlus(+30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(0, +60);
                                currCube[1].setXYOfPlus(+30, -30);
                                currCube[3].setXYOfPlus(-30, +30);
                            } else {
                                n = 3;
                                turn.start();
                            }
                        } else {
                            currCube[0].setXYOfPlus(+60, 0);
                            currCube[1].setXYOfPlus(-30, -30);
                            currCube[3].setXYOfPlus(+30, +30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(-60, 0);
                                currCube[1].setXYOfPlus(+30, +30);
                                currCube[3].setXYOfPlus(-30, -30);
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    }
                    if (random_object == 4) {
                        if (n == 0) {
                            currCube[0].setXYOfPlus(+30, +30);
                            currCube[1].setXYOfPlus(0, +60);
                            currCube[2].setXYOfPlus(+30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(-30, -30);
                                currCube[1].setXYOfPlus(0, -60);
                                currCube[2].setXYOfPlus(-30, +30);
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else {
                            currCube[0].setXYOfPlus(-30, -30);
                            currCube[1].setXYOfPlus(0, -60);
                            currCube[2].setXYOfPlus(-30, +30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(+30, +30);
                                currCube[1].setXYOfPlus(0, +60);
                                currCube[2].setXYOfPlus(+30, -30);
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    } if (random_object == 5) {
                        if (n == 0) {
                            currCube[0].setXYOfPlus(+30, +30);
                            currCube[1].setXYOfPlus(+30, -30);
                            currCube[3].setXYOfPlus(-30, +30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(-30, -30);
                                currCube[1].setXYOfPlus(-30, +30);
                                currCube[3].setXYOfPlus(+30, -30);
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else if (n == 1) {
                            currCube[0].setXYOfPlus(-30, +30);
                            currCube[1].setXYOfPlus(+30, +30);
                            currCube[3].setXYOfPlus(-30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(+30, -30);
                                currCube[1].setXYOfPlus(-30, -30);
                                currCube[3].setXYOfPlus(+30, +30);
                            } else {
                                n = 2;
                                turn.start();
                            }
                        } else if (n == 2) {
                            currCube[0].setXYOfPlus(-30, -30);
                            currCube[1].setXYOfPlus(-30, +30);
                            currCube[3].setXYOfPlus(+30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(+30, +30);
                                currCube[1].setXYOfPlus(+30, -30);
                                currCube[3].setXYOfPlus(-30, +30);
                            } else {
                                n = 3;
                                turn.start();
                            }
                        } else {
                            currCube[0].setXYOfPlus(+30, -30);
                            currCube[1].setXYOfPlus(-30, -30);
                            currCube[3].setXYOfPlus(+30, +30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(-30, +30);
                                currCube[1].setXYOfPlus(+30, +30);
                                currCube[3].setXYOfPlus(-30, -30);

                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    } if (random_object == 6) {
                        if (n == 0) {
                            currCube[0].setXYOfPlus(60, 0);
                            currCube[1].setXYOfPlus(30, 30);
                            currCube[3].setXYOfPlus(-30, 30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(-60, 0);
                                currCube[1].setXYOfPlus(-30, -30);
                                currCube[3].setXYOfPlus(+30, -30);
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else {
                            currCube[0].setXYOfPlus(-60, 0);
                            currCube[1].setXYOfPlus(-30, -30);
                            currCube[3].setXYOfPlus(+30, -30);

                            check_for_obstacles();
                            if (col_equal != 0) {
                                currCube[0].setXYOfPlus(60, 0);
                                currCube[1].setXYOfPlus(30, 30);
                                currCube[3].setXYOfPlus(-30, 30);
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    }
                }
            }
        });
        setFocusable(true);
        climb_down.start();
    }

    public static void main(String[] args) {
        f.setSize(302, 472);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLayout(null);
        f.add(new Main(0, 0));
        f.setJMenuBar(menu);
        f.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.decode("#2E1D1D"));
        g.fill3DRect(0, 0, 302, 472, true);

        for (int i = 0; i < cubes.size(); i++)
            for (int j = 0; j < 4; j++)
                if (cubes.get(i).getX() == currCube[j].getX() && cubes.get(i).getY() == currCube[j].getY() + 30) {
                    save_information();
                    create_new_object();
                    break;
                }

        for (int i = 0; i < 4; i++) {
            g.setColor(currCube[i].getOrigColor());
            currCube[i].setXYOfPlus(0, turn.isRunning() ? 0 : 30);
            g.fill3DRect(currCube[i].getX() + 1, currCube[i].getY() + 1, SCALE, SCALE, true);
        }

        if (turn.isRunning()) {
            turn.stop();
            time_turn_going = 0;
        }

        /* Отрисовка остальных кубиков */
        for (Cube cube : cubes) {
            g.setColor(cube.getOrigColor());
            g.fill3DRect(cube.getX() + 1, cube.getY() + 1, SCALE, SCALE, true);
        }
    }

    public void create_new_object() {
        n = 0;
        random_object = new java.util.Random().nextInt(7);
        if (random_object == 0) {
            currCube[0].setXY(90, -30);
            currCube[1].setXY(120, -30);
            currCube[2].setXY(150, -30);
            currCube[3].setXY(180, -30);

            n = 0;
        }
        if (random_object == 1) {
            currCube[0].setXY(120, -30);
            currCube[1].setXY(120, 0);
            currCube[2].setXY(150, 0);
            currCube[3].setXY(180, 0);
        }
        if (random_object == 2) {
            currCube[0].setXY(180, -30);
            currCube[1].setXY(120, 0);
            currCube[2].setXY(150, 0);
            currCube[3].setXY(180, 0);
        }
        if (random_object == 3) {
            currCube[0].setXY(120, -30);
            currCube[1].setXY(150, -30);
            currCube[2].setXY(120, 0);
            currCube[3].setXY(150, 0);
        }
        if (random_object == 4) {
            currCube[0].setXY(150, -30);
            currCube[1].setXY(180, -30);
            currCube[2].setXY(120, 0);
            currCube[3].setXY(150, 0);
        }
        if (random_object == 5) {
            currCube[0].setXY(150, -30);
            currCube[1].setXY(120, 0);
            currCube[2].setXY(150, 0);
            currCube[3].setXY(180, 0);
        }
        if (random_object == 6) {
            currCube[0].setXY(120, -30);
            currCube[1].setXY(150, -30);
            currCube[2].setXY(150, 0);
            currCube[3].setXY(180, 0);
        }
        int color = new java.util.Random().nextInt(5);
        currCube[0].setColor(color);
        currCube[1].setColor(color);
        currCube[2].setColor(color);
        currCube[3].setColor(color);
    }

    public void save_information() {
        for (int i = 0; i < cubes.size(); i++)
            if (cubes.get(i).getX() >= 120 && cubes.get(i).getX() <= 180)
                if (cubes.get(i).getY() >= -30 && cubes.get(i).getY() <= 0) {
                    this.climb_down.stop();
                    break;
                }
        if (climb_down.isRunning()) {
            for (int i = 0; i < 4; i++) {
                cubes.add(new Cube(currCube[i]));
            }
        }
    }

    public void check_for_obstacles() {
        col_equal = 0;
        for (int i = 0; i < cubes.size() - 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (cubes.get(i).getX() == currCube[j].getX() && cubes.get(i).getY() == currCube[j].getY()) {
                    col_equal++;
                }
            }
        }
        
        for (int i = 0; i < 4; i++) {
            if (currCube[i].getX() < 0 || currCube[i].getX() > 270) {
                col_equal++; 
            }
        }
    }
}