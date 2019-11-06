import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.Random;
import java.util.Vector;
import javax.swing.*;

public class GoAroundi implements KeyListener {

    private Car car = new Car();
    private Car enemy = new Car();
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private Random rand = new Random();
    private int fines = 0;
    private String brandOfCrashCar = "N/A";
    private Vector<String> carCrashes = new Vector<String>();
    
    @Override
    public void keyPressed(KeyEvent e) {
        drawMap();
        drawCities();
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            car.moveRight();
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            car.moveLeft();
        if(e.getKeyCode() == KeyEvent.VK_UP)
            car.moveUp();
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            car.moveDown();
        drawEnemy();
        drawCar();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    public void drawEnemy() {
        Graphics g = panel.getGraphics();
        
        g.setColor(Color.yellow); //chink
        g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
    }

    public GoAroundi() {
        frame.setTitle("East L.A. Lover");
        frame.setBounds(0, 0, 1200, 800);
        frame.setLayout(null);
        
        panel.setBounds(0, 0, 1000, 800);
        panel.setLayout(null);
        frame.add(panel);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        enemy.x = -125;
        
        JPanel pane = new JPanel();
        pane.setLayout(null);
        pane.setBackground(Color.lightGray);
        pane.setBounds(1000, 0, 200, 800);
        frame.add(pane);
        
        JTextArea crashList = new JTextArea();
        
        crashList.setForeground(Color.CYAN);
                
        crashList.setBounds(10, 10, 165, 380);
        crashList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                frame.requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        pane.add(crashList);
        
        JButton totalExit = new JButton();
        totalExit.setBounds(0, 520, 200, 100);
        pane.add(totalExit);
        totalExit.setText("Total Exit");
        totalExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Your total fines: -$" + fines);
                System.exit(0);
            }
        });

        JButton copyToClipboard = new JButton();
        copyToClipboard.setBounds(0, 410, 200, 100);
        pane.add(copyToClipboard);
        copyToClipboard.setText("Copy to clipboard");
        copyToClipboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(crashList.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                frame.requestFocus();
            }
        });
        
        frame.setVisible(true);
        
        frame.requestFocus();

        Thread t = new Thread() {
            public void run() {
                while(true) {
                    drawMap();
                    drawCar();
                    drawCities();
                    frame.setTitle("N/A - Fines: -$" + fines);
                    if(enemy.x > 500 && enemy.x < 510 && enemy.y > 0 && enemy.y < 20) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("right"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("left"))
                                enemy.direction = "left";
                        }
                        else {
                            if(enemy.direction.equals("right"))
                                enemy.direction = "down";
                            if(enemy.direction.equals("left"))
                                enemy.direction = "down";
                            enemy.width = 10;
                            enemy.height = 20;
                        }
                    } else if(enemy.x > 960 && enemy.x < 980 && enemy.y > 0 && enemy.y < 20) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("right"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("left"))
                                enemy.direction = "left";
                        }
                        else {
                            if(enemy.direction.equals("right"))
                                enemy.direction = "down";
                            if(enemy.direction.equals("left"))
                                enemy.direction = "down";
                            enemy.width = 10;
                            enemy.height = 20;
                        }
                    } else if(enemy.x > 950 && enemy.x < 980 && enemy.y > 400 && enemy.y < 420) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("up"))
                                enemy.direction = "up";
                            if(enemy.direction.equals("down"))
                                enemy.direction = "down";
                        }
                        else {
                            if(enemy.direction.equals("up"))
                                enemy.direction = "left";
                            if(enemy.direction.equals("down"))
                                enemy.direction = "left";
                            enemy.width = 20;
                            enemy.height = 10;
                        }
                    } else if(enemy.x > 950 && enemy.x < 980 && enemy.y > 760 && enemy.y < 780) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("up"))
                                enemy.direction = "up";
                            if(enemy.direction.equals("down"))
                                enemy.direction = "down";
                        }
                        else {
                            if(enemy.direction.equals("up"))
                                enemy.direction = "left";
                            if(enemy.direction.equals("down"))
                                enemy.direction = "left";
                            enemy.width = 20;
                            enemy.height = 10;
                        }
                    } else if(enemy.x > 500 && enemy.x < 510 && enemy.y > 400 && enemy.y < 420) {
                        int v = rand.nextInt(3);
                        if(v == 0 || v == 2 || v == 1) {
                            if(enemy.direction.equals("up")) {
                                enemy.direction = "up";
                            enemy.width = 10;
                            enemy.height = 20;
                                if(v == 2)
                                    enemy.direction = "left";
                                else
                                    enemy.direction = "right";
                            enemy.width = 20;
                            enemy.height = 10;
                            }
                            else if(enemy.direction.equals("down")) {
                                enemy.direction = "down";
                            enemy.width = 10;
                            enemy.height = 20;
                                if(v == 2)
                                    enemy.direction = "right";
                                else
                                    enemy.direction = "left";
                            enemy.width = 20;
                            enemy.height = 10;
                            }
                            else if(enemy.direction.equals("left")) {
                                enemy.direction = "left";
                            enemy.width = 20;
                            enemy.height = 10;
                                if(v == 2)
                                    enemy.direction = "down";
                                else
                                    enemy.direction = "up";
                            }
                            enemy.width = 10;
                            enemy.height = 20;
                        }
                    } else if(enemy.x > 500 && enemy.x < 510 && enemy.y > 0 && enemy.y < 20) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("up"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("down"))
                                enemy.direction = "right";
                        }
                        else {
                            if(enemy.direction.equals("up"))
                                enemy.direction = "left";
                            if(enemy.direction.equals("down"))
                                enemy.direction = "left";
                            enemy.width = 20;
                            enemy.height = 10;
                        }
                    }
                    if(enemyIsCollided()) {

                        String cl = crashList.getText();
                        if(cl.equals("") || cl == null) {
                            crashList.setText(cl + System.getProperty("line.separator") + enemy.brand 
                                    + " (-$" + enemy.fine + ")");
                        } else if(!cl.equals("")) {
                            crashList.setText(System.getProperty("line.separator") + cl 
                                    + System.getProperty("line.separator") + enemy.brand + " (-$" + enemy.fine + ")");
                        }

                        int u = rand.nextInt(4);
                        if( u == 0 ) {
                            enemy.brand = "Mercedez";
                            fines += 5000;
                            enemy.fine = 5000;
                        }
                        if( u == 1 ) {
                            enemy.brand = "Tatoyo";
                            fines += 3000;
                            enemy.fine = 3000;
                        }
                        if( u == 2 ) {
                            enemy.brand = "Jeep";
                            fines += 3800;
                            enemy.fine = 3800;
                        }
                        if( u == 3 ) {
                            enemy.brand = "Chrysler";
                            fines += 2800;
                            enemy.fine = 2800;
                        }
                        
                        createEnemyAutomobile();
                    }

                    if(enemy.x < -100 || enemy.x > 1100 || enemy.y < -100 || enemy.y > 900)
                        createEnemyAutomobile();

                    if(enemy.direction.equals("right"))
                        enemy.x += 5;
                    if(enemy.direction.equals("left"))
                        enemy.x -= 5;
                    if(enemy.direction.equals("down"))
                        enemy.y += 5;
                    if(enemy.direction.equals("up"))
                        enemy.y -= 5;

                    drawEnemy();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException interruptedException) {
                    }
                }
            }
        };
        
        t.start();
        
        frame.addKeyListener(this);
    }

    public void createEnemyAutomobile() {
        int u = rand.nextInt(4);
        if( u == 0) {
            enemy.brand = "Mercedez";
        }
        if( u == 1) {
            enemy.brand = "Tatoyo";
        }
        if( u == 2) {
            enemy.brand = "Jeep";
        }
        if( u == 3) {
            enemy.brand = "Chrysler";
        }
        int v = rand.nextInt(12);
        if(v == 0) {
            enemy.x = -20;
            enemy.y = 5;
            enemy.width = 20;
            enemy.height = 10;
            enemy.direction = "right";
        }
        if(v == 1) {
            enemy.x = 5;
            enemy.y = -20;
            enemy.width = 10;
            enemy.height = 20;
            enemy.direction = "down";
        }
        if(v == 2) {
            enemy.x = 505;
            enemy.y = -20;
            enemy.width = 10;
            enemy.height = 20;
            enemy.direction = "down";
        }
        if(v == 3) {
            enemy.x = 960;
            enemy.y = -20;
            enemy.width = 10;
            enemy.height = 20;
            enemy.direction = "down";
        }
        if(v == 4) {
            enemy.x = 1020;
            enemy.y = 5;
            enemy.width = 20;
            enemy.height = 10;
            enemy.direction = "left";
        }
        if(v == 5) {
            enemy.x = 1020;
            enemy.y = 405;
            enemy.width = 20;
            enemy.height = 10;
            enemy.direction = "left";
        }
        if(v == 6) {
            enemy.x = 1020;
            enemy.y = 795;
            enemy.width = 20;
            enemy.height = 10;
            enemy.direction = "up";
        }
        if(v == 7) {
            enemy.x = 960;
            enemy.y = 820;
            enemy.width = 10;
            enemy.height = 20;
            enemy.direction = "up";
        }
        if(v == 8) {
            enemy.x = 505;
            enemy.y = 820;
            enemy.width = 10;
            enemy.height = 20;
            enemy.direction = "up";
        }
        if(v == 9) {
            enemy.x = 5;
            enemy.y = 820;
            enemy.width = 10;
            enemy.height = 20;
            enemy.direction = "up";
        }
        if(v == 10) {
            enemy.x = -20;
            enemy.y = 795;
            enemy.width = 20;
            enemy.height = 10;
            enemy.direction = "right";
        }
        if(v == 11) {
            enemy.x = -20;
            enemy.y = 405;
            enemy.width = 20;
            enemy.height = 10;
            enemy.direction = "right";
        }
    }
    
    public void drawCities() {
        Graphics g = panel.getGraphics();
        
        g.setColor(Color.WHITE);
        
        g.setFont(new Font("Curlz MT", Font.ITALIC, 50));
        
        g.drawString("L.A.", 200, 200);
        g.drawString("EAST L.A.", 600, 200);
        g.drawString("PICO RIVERA", 600, 600);
        g.drawString("CAMPTON", 200, 600);
                
    }

    public boolean enemyIsCollided() {
        try {
            if(enemy.direction.equals("left") || enemy.direction.equals("right")) {
                if(car.direction.equals("left") || car.direction.equals("right")) {
                    if(car.direction.equals("right")) {
                        if(car.x+car.width >= enemy.x && car.x+car.width <= enemy.x + enemy.width) {
                            if(car.y >= enemy.y && car.y <= enemy.y + enemy.height) {
                                return true;
                            }
                        }
                    }
                    if(car.direction.equals("left")) {
                        if(car.x+car.width >= enemy.x && car.x+car.width <= enemy.x + enemy.width) {
                            if(car.y >= enemy.y && car.y <= enemy.y + enemy.height) {
                                return true;
                            }
                        }
                    }
                }
                if(car.direction.equals("up") || car.direction.equals("down")) {
                    if(car.direction.equals("down")) {
                        if(car.x+car.width >= enemy.x && car.x+car.width <= enemy.x + enemy.width) {
                            if(car.y >= enemy.y && car.y <= enemy.y + enemy.height) {
                                return true;
                            }
                        }
                    }
                    if(car.direction.equals("up")) {
                        if(car.x+car.width >= enemy.x && car.x+car.width <= enemy.x + enemy.width) {
                            if(car.y >= enemy.y && car.y <= enemy.y + enemy.height) {
                                return true;
                            }
                        }
                    }
                }
            }
            if(enemy.direction.equals("up") || enemy.direction.equals("down")) {
                if(car.direction.equals("left") || car.direction.equals("right")) {
                    if(car.x >= enemy.x && car.x <= enemy.x + enemy.width) {
                        if(car.y >= enemy.y && car.y <= enemy.y + enemy.height) {
                            return true;
                        }
                    }
                }
                if(car.direction.equals("up") || car.direction.equals("down")) {
                    if(car.x >= enemy.x && car.x <= enemy.x + enemy.width) {
                        if(car.y >= enemy.y && car.y <= enemy.y + enemy.height) {
                            return true;
                        }
                    }
                }
            }
        } catch(Exception e) {
            
        }
        return false;
    }
    
    public void drawCar() {
        Graphics g = panel.getGraphics();
        
        g.setColor(car.color);
        g.fillRect(car.x, car.y, car.width, car.height);
    }
    
    class Car {
        int x, y;
        int width;
        int height;
        Color color;
        String direction;
        String brand;
        int fine;
        
        Car() {
            x = 105;
            y = 5;
            width = 20;
            height = 10;
            color = Color.BLACK;
            brand = "Tatoyo";
            fine = 3000;
        }
        
        void moveDown() {
            width = 10;
            height = 20;
            y+=2;
            direction = "down";
        }

        void moveUp() {
            width = 10;
            height = 20;
            y-=2;
            direction = "up";
        }

        void moveRight() {
            width = 20;
            height = 10;
            x+=2;
            direction = "right";
        }

        void moveLeft() {
            width = 20;
            height = 10;
            x-=2;
            direction = "left";
        }
    }
    
    public void drawMap() {
        
        Graphics g = panel.getGraphics();
        
        g.setColor(new Color(0, 210, 0));
        
        g.fillRect(0, 0, 1000, 800);

        g.setColor(Color.GRAY);
        
        g.fillRect(0, 0, 1000, 20);
        g.fillRect(0, 400, 1000, 20);
        g.fillRect(0, 700, 1000, 20);
        
        g.fillRect(0, 0, 20, 800);
        g.fillRect(500, 0, 20, 800);
        g.fillRect(960, 0, 20, 800);

        g.setColor(Color.WHITE);
        for(int i=0; i<=500; i+=5)
            g.drawRect(2*i, 10, 5, 1);
        for(int i=0; i<=500; i+=5)
            g.drawRect(2*i, 410, 5, 1);
        for(int i=0; i<=500; i+=5)
            g.drawRect(2*i, 710, 5, 1);
        for(int i=0; i<=500; i+=5)
            g.drawRect(10, 2*i, 1, 5);
        for(int i=0; i<=500; i+=5)
            g.drawRect(510, 2*i, 1, 5);
        for(int i=0; i<=500; i+=5)
            g.drawRect(970, 2*i, 1, 5);
        
        g.dispose();
    }
    
    public static void main(String args[]) {
        GoAroundi goAroundi = new GoAroundi();
    }
}