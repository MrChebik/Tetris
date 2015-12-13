import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by alex on 07.12.15.
 */
public class Main extends JPanel {

    int time_turn_going = 0;
    int n = 0;
    int col = 0;
    int col1 = 0;
    int col_plains = 0;
    int col_equal = 0;
    int random_object;
    final int SCALE = 30;
    ArrayList<Integer> objects = new ArrayList();
    int[] x_object = new int[4];
    int[] y_object = new int[4];

    static JMenuBar menu = new JMenuBar();

    Timer down = new Timer(50, e -> {
        for (int i = 0; i < 4; i++)
            if (y_object[i] > 360) {
                save_information();
                create_new_object();
                break;
            }
        repaint();
        this.down.stop();
    });

    Timer turn = new Timer(150, e -> {
        time_turn_going = 1;
        repaint();
    });

    Timer climb_down = new Timer(800, e -> {
        for (int i = 0; i < 4; i++)
            if (y_object[i] > 360) {
                save_information();
                create_new_object();
                break;
            }
        if (objects.size() > 28)
            for (int line = 0; line <= 390; line += 30) {
                col_plains = 0;
                for (int i = 0; i < objects.size(); i++)
                    if (objects.get(i) == line && i % 2 != 0)
                        col_plains++;
                if (col_plains == 10)
                    for (int i = 0; i < objects.size(); i++)
                        if (objects.get(i) == line && i % 2 != 0) {
                            objects.remove(i);
                            objects.remove(i - 1);
                            i--;
                        }
            }
        if (turn.isRunning())
            turn.stop();
        repaint();
    });

    static JFrame f = new JFrame("Tetris");

    Main(int x, int y) {
        create_new_object();
        setLocation(x, y);
        setSize(f.getWidth(), f.getHeight());
        setLayout(null);
        JMenu file = new JMenu("File");
        menu.add(file);
        JMenuItem new_game = new JMenuItem("New Game");
        new_game.addActionListener(e -> {
            create_new_object();
            objects.clear();
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
        about.addActionListener(e -> JOptionPane.showMessageDialog(null, "Version:      0.1\nDeveloper:  MrChebik", "About", JOptionPane.PLAIN_MESSAGE));
        help.add(about);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && climb_down.isRunning()) {
                    col = 0;
                    for (int i = 0; i < objects.size() - 1; i += 2) {
                        col1 = 0;
                        for (int j = 0; j < 4; j++)
                            if (objects.get(i) == x_object[j] + 30 && objects.get(i + 1) == y_object[j])
                                col1++;
                        if (col1 != 0)
                            col++;
                    }
                    for (int i = 0; i < 4; i++)
                        if (x_object[i] + 30 > 270)
                            col++;
                    if (col == 0)
                        for (int i = 0; i < 4; i++)
                            x_object[i] += 30;
                }
                if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && climb_down.isRunning()) {
                    col = 0;
                    for (int i = 0; i < objects.size() - 1; i += 2) {
                        col1 = 0;
                        for (int j = 0; j < 4; j++)
                            if (objects.get(i) == x_object[j] - 30 && objects.get(i + 1) == y_object[j])
                                col1++;
                        if (col1 != 0)
                            col++;
                    }
                    for (int i = 0; i < 4; i++)
                        if (x_object[i] - 30 < 0)
                            col++;
                    if (col == 0)
                        for (int i = 0; i < 4; i++)
                            x_object[i] -= 30;
                }
                if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && climb_down.isRunning())
                    down.start();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP || key == KeyEvent.VK_ENTER || key == KeyEvent.VK_W) && time_turn_going == 0 && climb_down.isRunning()) {
                    if (random_object == 0)
                        if (n == 0) {
                            y_object[0] -= 90;
                            y_object[1] -= 60;
                            y_object[3] -= 30;
                            x_object[0] += 60;
                            x_object[1] += 30;
                            x_object[3] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] += 90;
                                y_object[1] += 60;
                                y_object[3] += 30;
                                x_object[0] -= 60;
                                x_object[1] -= 30;
                                x_object[3] += 30;
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else {
                            y_object[0] += 90;
                            y_object[1] += 60;
                            y_object[3] += 30;
                            x_object[0] -= 60;
                            x_object[1] -= 30;
                            x_object[3] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] -= 90;
                                y_object[1] -= 60;
                                y_object[3] -= 30;
                                x_object[0] += 60;
                                x_object[1] += 30;
                                x_object[3] -= 30;
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    if (random_object == 1)
                        if (n == 0) {
                            y_object[1] -= 30;
                            y_object[3] += 30;
                            x_object[0] += 60;
                            x_object[1] += 30;
                            x_object[3] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[1] += 30;
                                y_object[3] -= 30;
                                x_object[0] -= 60;
                                x_object[1] -= 30;
                                x_object[3] += 30;
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else if (n == 1) {
                            y_object[0] += 60;
                            y_object[1] += 30;
                            y_object[3] -= 30;
                            x_object[1] += 30;
                            x_object[3] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] -= 60;
                                y_object[1] -= 30;
                                y_object[3] += 30;
                                x_object[1] -= 30;
                                x_object[3] += 30;
                            } else {
                                n = 2;
                                turn.start();
                            }
                        } else if (n == 2) {
                            y_object[1] += 30;
                            y_object[3] -= 30;
                            x_object[0] -= 60;
                            x_object[1] -= 30;
                            x_object[3] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[1] -= 30;
                                y_object[3] += 30;
                                x_object[0] += 60;
                                x_object[1] += 30;
                                x_object[3] -= 30;
                            } else {
                                n = 3;
                                turn.start();
                            }
                        } else {
                            y_object[0] -= 60;
                            y_object[1] -= 30;
                            y_object[3] += 30;
                            x_object[1] -= 30;
                            x_object[3] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] += 60;
                                y_object[1] += 30;
                                y_object[3] -= 30;
                                x_object[1] += 30;
                                x_object[3] -= 30;
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    if (random_object == 2)
                        if (n == 0) {
                            y_object[0] += 60;
                            y_object[1] -= 30;
                            y_object[3] += 30;
                            x_object[1] += 30;
                            x_object[3] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] -= 60;
                                y_object[1] += 30;
                                y_object[3] -= 30;
                                x_object[1] -= 30;
                                x_object[3] += 30;
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else if (n == 1) {
                            y_object[1] += 30;
                            y_object[3] -= 30;
                            x_object[0] -= 60;
                            x_object[1] += 30;
                            x_object[3] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[1] -= 30;
                                y_object[3] += 30;
                                x_object[0] += 60;
                                x_object[1] -= 30;
                                x_object[3] += 30;
                            } else {
                                n = 2;
                                turn.start();
                            }
                        } else if (n == 2) {
                            y_object[0] -= 60;
                            y_object[1] += 30;
                            y_object[3] -= 30;
                            x_object[1] -= 30;
                            x_object[3] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] += 60;
                                y_object[1] -= 30;
                                y_object[3] += 30;
                                x_object[1] += 30;
                                x_object[3] -= 30;
                            } else {
                                n = 3;
                                turn.start();
                            }
                        } else {
                            y_object[1] -= 30;
                            y_object[3] += 30;
                            x_object[0] += 60;
                            x_object[1] -= 30;
                            x_object[3] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[1] += 30;
                                y_object[3] -= 30;
                                x_object[0] -= 60;
                                x_object[1] += 30;
                                x_object[3] -= 30;
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    if (random_object == 4)
                        if (n == 0) {
                            y_object[0] += 30;
                            y_object[1] += 60;
                            y_object[2] -= 30;
                            x_object[0] += 30;
                            x_object[2] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] -= 30;
                                y_object[1] -= 60;
                                y_object[2] += 30;
                                x_object[0] -= 30;
                                x_object[2] -= 30;
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else {
                            y_object[0] -= 30;
                            y_object[1] -= 60;
                            y_object[2] += 30;
                            x_object[0] -= 30;
                            x_object[2] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] += 30;
                                y_object[1] += 60;
                                y_object[2] -= 30;
                                x_object[0] += 30;
                                x_object[2] += 30;
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    if (random_object == 5)
                        if (n == 0) {
                            y_object[0] += 30;
                            y_object[1] -= 30;
                            y_object[3] += 30;
                            x_object[0] += 30;
                            x_object[1] += 30;
                            x_object[3] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] -= 30;
                                y_object[1] += 30;
                                y_object[3] -= 30;
                                x_object[0] -= 30;
                                x_object[1] -= 30;
                                x_object[3] += 30;
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else if (n == 1) {
                            y_object[0] += 30;
                            y_object[1] += 30;
                            y_object[3] -= 30;
                            x_object[0] -= 30;
                            x_object[1] += 30;
                            x_object[3] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] -= 30;
                                y_object[1] -= 30;
                                y_object[3] += 30;
                                x_object[0] += 30;
                                x_object[1] -= 30;
                                x_object[3] += 30;
                            } else {
                                n = 2;
                                turn.start();
                            }
                        } else if (n == 2) {
                            y_object[0] -= 30;
                            y_object[1] += 30;
                            y_object[3] -= 30;
                            x_object[0] -= 30;
                            x_object[1] -= 30;
                            x_object[3] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] += 30;
                                y_object[1] -= 30;
                                y_object[3] += 30;
                                x_object[0] += 30;
                                x_object[1] += 30;
                                x_object[3] -= 30;
                            } else {
                                n = 3;
                                turn.start();
                            }
                        } else {
                            y_object[0] -= 30;
                            y_object[1] -= 30;
                            y_object[3] += 30;
                            x_object[0] += 30;
                            x_object[1] -= 30;
                            x_object[3] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[0] += 30;
                                y_object[1] += 30;
                                y_object[3] -= 30;
                                x_object[0] -= 30;
                                x_object[1] += 30;
                                x_object[3] -= 30;
                            } else {
                                n = 0;
                                turn.start();
                            }
                        }
                    if (random_object == 6) {
                        if (n == 0) {
                            y_object[1] += 30;
                            y_object[3] += 30;
                            x_object[0] += 60;
                            x_object[1] += 30;
                            x_object[3] -= 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[1] -= 30;
                                y_object[3] -= 30;
                                x_object[0] -= 60;
                                x_object[1] -= 30;
                                x_object[3] += 30;
                            } else {
                                n = 1;
                                turn.start();
                            }
                        } else {
                            y_object[1] -= 30;
                            y_object[3] -= 30;
                            x_object[0] -= 60;
                            x_object[1] -= 30;
                            x_object[3] += 30;
                            check_for_obstacles();
                            if (col_equal != 0) {
                                y_object[1] += 30;
                                y_object[3] += 30;
                                x_object[0] += 60;
                                x_object[1] += 30;
                                x_object[3] -= 30;
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
        f.setSize(305, 469);
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

        for (int i = 0; i < objects.size() - 1; i += 2)
            for (int j = 0; j < 4; j++)
                if (objects.get(i) == x_object[j] && objects.get(i + 1) == y_object[j] + 30) {
                    save_information();
                    create_new_object();
                    break;
                }

        g.setColor(new Color(155, 155, 155));
        for (int i = 0; i < 4; i++)
            g.fill3DRect(x_object[i] + 1, (y_object[i] += turn.isRunning() ? 0 : 30) + 1, SCALE, SCALE, true);

        if (turn.isRunning()) {
            turn.stop();
            time_turn_going = 0;
        }

        /** Отрисовка остальных кубиков */
        for (int i = 0; i < objects.size() - 1; i += 2)
            g.fill3DRect(objects.get(i) + 1, objects.get(i + 1) + 1, SCALE, SCALE, true);
    }

    public void create_new_object() {
        n = 0;
        random_object = new java.util.Random().nextInt(7);
        if (random_object == 0) {
            y_object[0] = -30;
            y_object[1] = -30;
            y_object[2] = -30;
            y_object[3] = -30;
            x_object[0] = 90;
            x_object[1] = 120;
            x_object[2] = 150;
            x_object[3] = 180;
            n = 0;
        }
        if (random_object == 1) {
            y_object[0] = -30;
            y_object[1] = 0;
            y_object[2] = 0;
            y_object[3] = 0;
            x_object[0] = 120;
            x_object[1] = 120;
            x_object[2] = 150;
            x_object[3] = 180;
        }
        if (random_object == 2) {
            y_object[0] = -30;
            y_object[1] = 0;
            y_object[2] = 0;
            y_object[3] = 0;
            x_object[0] = 180;
            x_object[1] = 120;
            x_object[2] = 150;
            x_object[3] = 180;
        }
        if (random_object == 3) {
            y_object[0] = -30;
            y_object[1] = -30;
            y_object[2] = 0;
            y_object[3] = 0;
            x_object[0] = 120;
            x_object[1] = 150;
            x_object[2] = 120;
            x_object[3] = 150;
        }
        if (random_object == 4) {
            y_object[0] = -30;
            y_object[1] = -30;
            y_object[2] = 0;
            y_object[3] = 0;
            x_object[0] = 150;
            x_object[1] = 180;
            x_object[2] = 120;
            x_object[3] = 150;
        }
        if (random_object == 5) {
            y_object[0] = -30;
            y_object[1] = 0;
            y_object[2] = 0;
            y_object[3] = 0;
            x_object[0] = 150;
            x_object[1] = 120;
            x_object[2] = 150;
            x_object[3] = 180;
        }
        if (random_object == 6) {
            y_object[0] = -30;
            y_object[1] = -30;
            y_object[2] = 0;
            y_object[3] = 0;
            x_object[0] = 120;
            x_object[1] = 150;
            x_object[2] = 150;
            x_object[3] = 180;
        }
    }

    public void save_information() {
        for (int i = 0; i < objects.size(); i += 2)
            if (objects.get(i) >= 120 && objects.get(i) <= 180)
                if (objects.get(i + 1) >= -30 && objects.get(i + 1) <= 0) {
                    this.climb_down.stop();
                    break;
                }
        if (climb_down.isRunning())
            for (int i = 0, j = objects.size(); i < 4; i++, j += 2) {
                objects.add(j, x_object[i]);
                objects.add(j + 1, y_object[i]);
            }
    }

    public void check_for_obstacles() {
        col_equal = 0;
        for (int i = 0; i < objects.size() - 2; i += 2)
            for (int j = 0; j < 4; j++)
                if (objects.get(i) == x_object[j] && objects.get(i + 1) == y_object[j])
                    col_equal++;
        for (int i = 0; i < 4; i++)
            if (x_object[i] < 0 || x_object[i] > 270)
                col_equal++;
    }
}