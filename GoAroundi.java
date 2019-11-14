import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.*;

public class GoAroundi implements Runnable, KeyListener {

    private GasStation gasStation = new GasStation();
    
    class GasStation {
        int x, y;
        int width;
        int height;
        Color color;

        public GasStation() {
            x = 700;
            y = 300;
            width = 50;
            height = 100;
            color = Color.YELLOW;
        }
        
        public void refuel() {
            life = 100;
        }
    }
    
    private int life = 100;
    private Map map = new Map();
    private Airplane airplane_L = new Airplane();
    private Airplane airplane_R = new Airplane();
    private Airplane airplane_T = new Airplane();
    private Airplane airplane_D = new Airplane();
    private Car car = new Car();
    private Car enemy = new Car();
    private Car enemy2 = new Car();
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private Random rand = new Random();
    private ArrayList<PotHole> potholes = new ArrayList<PotHole>();
    private int fines = 0;
    private String brandOfCrashCar = "N/A";
    private Vector<String> carCrashes = new Vector<String>();
    private Image airplaneL;
    private Image airplaneR;
    private Image airplaneT;
    private Image airplaneD;
    private String which = "L";
    
    class PotHole {
        int x, y;
        int width = 15;
    }
    
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
        try {
            makeSound("tick.wav");
        } catch(Exception e1) {
            try {
                makeSound("src/tick.wav");
            } catch(Exception e2) {}
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    public void drawGasStation() {
        try {
        Graphics g = panel.getGraphics();
        g.setColor(Color.YELLOW);
        g.fillRect(gasStation.x, gasStation.y, gasStation.width, gasStation.height);
        g.setColor(Color.BLACK);
        g.drawString("Gas", gasStation.x+5, gasStation.y+15);
        g.drawString("Station", gasStation.x+5, gasStation.y+30);
        g.dispose();
        } catch(Exception e) {}
    }
    
    public void drawEnemy() {
        try {
        Graphics g = panel.getGraphics();
        g.setColor(Color.blue);
        g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
        g.dispose();
        } catch(Exception e) {}
    }
    
    public void drawEnemy2() {
        try {
        Graphics g = panel.getGraphics();
        g.setColor(Color.blue);
        g.fillRect(enemy2.x, enemy2.y, enemy2.width, enemy2.height);
        g.dispose();
        } catch(Exception e) {}
    }

    class Airplane {
        int x = -1000, y;
        String direction = "T";
    }
    
    public boolean isOutsideRangeForPothole(int x, int y) {
        if(x > 2 && x < 250 && y == 0) {
            return true;
        }
        if(x > 252 && x < 500 && y != 0) {
            return true;
        }
        if(x > 502 && x < 750 && y != 0) {
            return true;
        }
        if(x > 752 && x < 1000 && y != 0) {
            return true;
        }
        if(x > 2 && x < 250 && y != 200) {
            return true;
        }
        if(x > 252 && x < 500 && y != 200) {
            return true;
        }
        if(x > 502 && x < 750 && y != 200) {
            return true;
        }
        if(x > 752 && x < 1000 && y != 200) {
            return true;
        }
        if(x > 2 && x < 250 && y != 400) {
            return true;
        }
        if(x > 252 && x < 500 && y != 400) {
            return true;
        }
        if(x > 502 && x < 750 && y != 400) {
            return true;
        }
        if(x > 752 && x < 1000 && y != 400) {
            return true;
        }
        if(x > 2 && x < 250 && y != 600) {
            return true;
        }
        if(x > 252 && x < 500 && y != 600) {
            return true;
        }
        if(x > 502 && x < 750 && y != 600) {
            return true;
        }
        if(x > 752 && x < 1000 && y != 600) {
            return true;
        }
        return false;
    }
    
    public GoAroundi() {
        
        for(int i=0; i<10; i++) {
            int x; 
            int y;
            do {
                x = rand.nextInt(1000);
                y = rand.nextInt(800);
            } while(isOutsideRangeForPothole(x, y));
            PotHole pothole = new PotHole();
            pothole.x = x;
            pothole.y = y;
            System.out.println(x + "," + y);
System.out.println(x + "," + y);System.out.println(x + "," + y);System.out.println(x + "," + y);            potholes.add(pothole);
        }
        
        try {
            airplaneR = ImageIO.read(ClassLoader.getSystemResourceAsStream("airplaneL.png"));
            airplaneL = ImageIO.read(ClassLoader.getSystemResourceAsStream("airplaneR.png"));
            airplaneD = ImageIO.read(ClassLoader.getSystemResourceAsStream("airplaneT.png"));
            airplaneT = ImageIO.read(ClassLoader.getSystemResourceAsStream("airplaneD.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        frame.setTitle("East L.A. Lover");
        frame.setUndecorated(true);
        frame.setBounds(0, 0, 1200, 800);
        frame.setLayout(null);
        
        try {
            BufferedImage ii = ImageIO.read(ClassLoader.getSystemResourceAsStream("usedcars.jpg"));
            frame.setContentPane(new JLabel(new ImageIcon(ii)));
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        panel.setBounds(0, 0, 1000, 800);
        panel.setLayout(null);
        frame.add(panel);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        enemy.x = -125;

        enemy2.x = -125;
        
        JTextArea crashList = new JTextArea();
        
        crashList.setForeground(Color.CYAN);
        JScrollPane sp = new JScrollPane(crashList);
        sp.setBounds(1070, 10, 125, 380);
                
        crashList.setBounds(1070, 10, 125, 380);
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
        frame.add(sp);
        
        JButton totalExit = new JButton();
        totalExit.setBounds(1050, 520, 150, 40);
        frame.add(totalExit);
        totalExit.setText("Total Exit");
        totalExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Your total fines: -$" + fines);
                System.exit(0);
            }
        });

        JButton copyToClipboard = new JButton();
        copyToClipboard.setBounds(1050, 480, 150, 40);
        frame.add(copyToClipboard);
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
        
        JLabel j = new JLabel("in America");
        j.setBounds(1070, 620, 200, 30);
        j.setFont(new Font("arial", Font.BOLD, 20));
        j.setForeground(Color.red);
        frame.add(j);

        JLabel j2 = new JLabel(life + "");
        j2.setBounds(1070, 700, 100, 30);
        j2.setFont(new Font("arial", Font.BOLD, 20));
        j2.setForeground(Color.GREEN);
        frame.add(j2);
        
        frame.setVisible(true);
        
        frame.requestFocus();

        Thread t = new Thread() {
            public void run() {

                int baby = 0;

                int c = 0;
                
                while(true) {
                    
                    c++;
                    
                    j2.setText(life + "");
                    if(c%200==0)
                        life--;
                    
                    if(life == 0)
                        System.exit(0);
                    
                    drawMap();
                    drawPotholes();
                    drawCar();
                    drawCities();
                    drawAirplanes();
                    drawGasStation();
                    
                    if(gasStation.x <= car.x && gasStation.x+gasStation.width >= car.x &&
                            gasStation.y <= car.y && gasStation.y+gasStation.height >= car.y) {

                        gasStation.refuel();
                    }

                    for(int i=0; i<potholes.size(); i++) {
                        if(car.x >= potholes.get(i).x && car.x <= potholes.get(i).x+potholes.get(i).width &&
                                car.y >= potholes.get(i).y && car.y <= potholes.get(i).y+potholes.get(i).width) {
                            car.speed = 0;
                            car.timer = (int)((double)10*2.5);
                        String cl = crashList.getText();
                        crashList.setText(cl 
                                + System.getProperty("line.separator")
                                + "uGotStuckInARut");
                            potholes.remove(potholes.get(i));
                            life -= 23;
                        }
                    }
                    
                    if(car.timer == 0) {
                        baby = 0;
                        car.speed = 5;
                    }
                    
                    baby++;
                    
                    if(baby == 10) {
                        car.timer--;
                        baby = 0;
                    }
                    
                    if(potholes.size() <= 4) {

                        createAirplanes();
            potholes.clear();
        for(int i=0; i<10; i++) {
            int x; 
            int y;
            do {
                x = rand.nextInt(1000);
                y = rand.nextInt(800);
            } while(isOutsideRangeForPothole(x, y));
            PotHole pothole = new PotHole();
            pothole.x = x;
            pothole.y = y;
            System.out.println(x + "," + y);
System.out.println(x + "," + y);System.out.println(x + "," + y);System.out.println(x + "," + y);            potholes.add(pothole);
        }
                    }
                    
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
                    } else if(enemy.x > 250 && enemy.x < 260 && enemy.y > 0 && enemy.y < 20) {
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
                    } else if(enemy.x > 250 && enemy.x < 260 && enemy.y > 240 && enemy.y < 260) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("right"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("left"))
                                enemy.direction = "left";
                        }
                        else {
                            int x = rand.nextInt(2);
                            if(x == 0) {
                                if(enemy.direction.equals("right"))
                                    enemy.direction = "up";
                                if(enemy.direction.equals("left"))
                                    enemy.direction = "up";
                            } else {
                                if(enemy.direction.equals("right"))
                                    enemy.direction = "down";
                                if(enemy.direction.equals("left"))
                                    enemy.direction = "down";
                            }
                            enemy.width = 10;
                            enemy.height = 20;
                        }
                    } else if(enemy.x > 490 && enemy.x < 510 && enemy.y > 240 && enemy.y < 260) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("right"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("left"))
                                enemy.direction = "left";
                        }
                        else {
                            int x = rand.nextInt(2);
                            if(x == 0) {
                                if(enemy.direction.equals("right"))
                                    enemy.direction = "up";
                                if(enemy.direction.equals("left"))
                                    enemy.direction = "up";
                            } else {
                                if(enemy.direction.equals("right"))
                                    enemy.direction = "down";
                                if(enemy.direction.equals("left"))
                                    enemy.direction = "down";
                            }
                            enemy.width = 10;
                            enemy.height = 20;
                        }
                    } else if(enemy.x > 0 && enemy.x < 10 && enemy.y > 200 && enemy.y < 220) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("down"))
                                enemy.direction = "down";
                            if(enemy.direction.equals("up"))
                                enemy.direction = "up";
                        }
                        else {
                            if(enemy.direction.equals("down"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("up"))
                                enemy.direction = "right";
                            enemy.width = 20;
                            enemy.height = 10;
                        }
                    } else if(enemy.x > 0 && enemy.x < 10 && enemy.y > 400 && enemy.y < 420) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("down"))
                                enemy.direction = "down";
                            if(enemy.direction.equals("up"))
                                enemy.direction = "up";
                        }
                        else {
                            if(enemy.direction.equals("down"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("up"))
                                enemy.direction = "right";
                            enemy.width = 20;
                            enemy.height = 10;
                        }
                    } else if(enemy.x > 0 && enemy.x < 10 && enemy.y > 600 && enemy.y < 620) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("down"))
                                enemy.direction = "down";
                            if(enemy.direction.equals("up"))
                                enemy.direction = "up";
                        }
                        else {
                            if(enemy.direction.equals("down"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("up"))
                                enemy.direction = "right";
                            enemy.width = 20;
                            enemy.height = 10;
                        }
                    } else if(enemy.x > 0 && enemy.x < 10 && enemy.y > 750 && enemy.y < 770) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy.direction.equals("down"))
                                enemy.direction = "down";
                            if(enemy.direction.equals("up"))
                                enemy.direction = "up";
                        }
                        else {
                            if(enemy.direction.equals("down"))
                                enemy.direction = "right";
                            if(enemy.direction.equals("up"))
                                enemy.direction = "right";
                            enemy.width = 20;
                            enemy.height = 10;
                        }
                    } else if(enemy.x > 750 && enemy.x < 760 && enemy.y > 0 && enemy.y < 20) {
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
                    } else if(enemy.x > 970 && enemy.x < 990 && enemy.y > 0 && enemy.y < 20) {
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
                    } else if(enemy.x > 970 && enemy.x < 990 && enemy.y > 200 && enemy.y < 220) {
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
                    } else if(enemy.x > 970 && enemy.x < 990 && enemy.y > 400 && enemy.y < 420) {
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
                    } else if(enemy.x > 970 && enemy.x < 990 && enemy.y > 600 && enemy.y < 620) {
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
                    } else if(enemy.x > 970 && enemy.x < 990 && enemy.y > 760 && enemy.y < 780) {
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
                                if(v == 2) {
                                    enemy.direction = "left";
                                    enemy.width = 20;
                                    enemy.height = 10;
                                }
                                else {
                                    enemy.direction = "right";
                                    enemy.width = 20;
                                    enemy.height = 10;
                                }
                            }
                            else if(enemy.direction.equals("down")) {
                                enemy.direction = "down";
                                enemy.width = 10;
                                enemy.height = 20;
                                if(v == 2) {
                                    enemy.direction = "right";
                                    enemy.width = 20;
                                    enemy.height = 10;
                                }
                                else {
                                    enemy.direction = "left";
                                    enemy.width = 20;
                                    enemy.height = 10;
                                }
                            }
                            else if(enemy.direction.equals("left")) {
                                enemy.direction = "left";
                                enemy.width = 20;
                                enemy.height = 10;
                                if(v == 2) {
                                    enemy.direction = "down";
                                    enemy.width = 10;
                                    enemy.height = 20;
                                }
                                else {
                                    enemy.direction = "up";
                                    enemy.width = 10;
                                    enemy.height = 20;
                                }
                            }
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
                    if(enemy2.x > 500 && enemy2.x < 510 && enemy2.y > 0 && enemy2.y < 20) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "left";
                        }
                        else {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "down";
                            enemy2.width = 10;
                            enemy2.height = 20;
                        }
                    } else if(enemy2.x > 250 && enemy2.x < 260 && enemy2.y > 0 && enemy2.y < 20) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "left";
                        }
                        else {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "down";
                            enemy2.width = 10;
                            enemy2.height = 20;
                        }
                    } else if(enemy2.x > 250 && enemy2.x < 260 && enemy2.y > 240 && enemy2.y < 260) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "left";
                        }
                        else {
                            int x = rand.nextInt(2);
                            if(x == 0) {
                                if(enemy2.direction.equals("right"))
                                    enemy2.direction = "up";
                                if(enemy2.direction.equals("left"))
                                    enemy2.direction = "up";
                            } else {
                                if(enemy2.direction.equals("right"))
                                    enemy2.direction = "down";
                                if(enemy2.direction.equals("left"))
                                    enemy2.direction = "down";
                            }
                            enemy2.width = 10;
                            enemy2.height = 20;
                        }
                    } else if(enemy2.x > 490 && enemy2.x < 510 && enemy2.y > 240 && enemy2.y < 260) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "left";
                        }
                        else {
                            int x = rand.nextInt(2);
                            if(x == 0) {
                                if(enemy2.direction.equals("right"))
                                    enemy2.direction = "up";
                                if(enemy2.direction.equals("left"))
                                    enemy2.direction = "up";
                            } else {
                                if(enemy2.direction.equals("right"))
                                    enemy2.direction = "down";
                                if(enemy2.direction.equals("left"))
                                    enemy2.direction = "down";
                            }
                            enemy2.width = 10;
                            enemy2.height = 20;
                        }
                    } else if(enemy2.x > 0 && enemy2.x < 10 && enemy2.y > 200 && enemy2.y < 220) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "up";
                        }
                        else {
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "right";
                            enemy2.width = 20;
                            enemy2.height = 10;
                        }
                    } else if(enemy2.x > 0 && enemy2.x < 10 && enemy2.y > 400 && enemy2.y < 420) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "up";
                        }
                        else {
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "right";
                            enemy2.width = 20;
                            enemy2.height = 10;
                        }
                    } else if(enemy2.x > 0 && enemy2.x < 10 && enemy2.y > 600 && enemy2.y < 620) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "up";
                        }
                        else {
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "right";
                            enemy2.width = 20;
                            enemy2.height = 10;
                        }
                    } else if(enemy2.x > 0 && enemy2.x < 10 && enemy2.y > 750 && enemy2.y < 770) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "up";
                        }
                        else {
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "right";
                            enemy2.width = 20;
                            enemy2.height = 10;
                        }
                    } else if(enemy2.x > 750 && enemy2.x < 760 && enemy2.y > 0 && enemy2.y < 20) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "left";
                        }
                        else {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "down";
                            enemy2.width = 10;
                            enemy2.height = 20;
                        }
                    } else if(enemy2.x > 970 && enemy2.x < 990 && enemy2.y > 0 && enemy2.y < 20) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "left";
                        }
                        else {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "down";
                            enemy2.width = 10;
                            enemy2.height = 20;
                        }
                    } else if(enemy2.x > 970 && enemy2.x < 990 && enemy2.y > 200 && enemy2.y < 220) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "left";
                        }
                        else {
                            if(enemy2.direction.equals("right"))
                                enemy2.direction = "down";
                            if(enemy2.direction.equals("left"))
                                enemy2.direction = "down";
                            enemy2.width = 10;
                            enemy2.height = 20;
                        }
                    } else if(enemy2.x > 970 && enemy2.x < 990 && enemy2.y > 400 && enemy2.y < 420) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "up";
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "down";
                        }
                        else {
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "left";
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "left";
                            enemy2.width = 20;
                            enemy2.height = 10;
                        }
                    } else if(enemy2.x > 970 && enemy2.x < 990 && enemy2.y > 600 && enemy2.y < 620) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "up";
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "down";
                        }
                        else {
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "left";
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "left";
                            enemy2.width = 20;
                            enemy2.height = 10;
                        }
                    } else if(enemy2.x > 970 && enemy2.x < 990 && enemy2.y > 760 && enemy2.y < 780) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "up";
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "down";
                        }
                        else {
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "left";
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "left";
                            enemy2.width = 20;
                            enemy2.height = 10;
                        }
                    } else if(enemy2.x > 500 && enemy2.x < 510 && enemy2.y > 400 && enemy2.y < 420) {
                        int v = rand.nextInt(3);
                        if(v == 0 || v == 2 || v == 1) {
                            if(enemy2.direction.equals("up")) {
                                enemy2.direction = "up";
                                enemy2.width = 10;
                                enemy2.height = 20;
                                if(v == 2) {
                                    enemy2.direction = "left";
                                    enemy2.width = 20;
                                    enemy2.height = 10;
                                }
                                else {
                                    enemy2.direction = "right";
                                    enemy2.width = 20;
                                    enemy2.height = 10;
                                }
                            }
                            else if(enemy2.direction.equals("down")) {
                                enemy2.direction = "down";
                                enemy2.width = 10;
                                enemy2.height = 20;
                                if(v == 2) {
                                    enemy2.direction = "right";
                                    enemy2.width = 20;
                                    enemy2.height = 10;
                                }
                                else {
                                    enemy2.direction = "left";
                                    enemy2.width = 20;
                                    enemy2.height = 10;
                                }
                            }
                            else if(enemy2.direction.equals("left")) {
                                enemy2.direction = "left";
                                enemy2.width = 20;
                                enemy2.height = 10;
                                if(v == 2) {
                                    enemy2.direction = "down";
                                    enemy2.width = 10;
                                    enemy2.height = 20;
                                }
                                else {
                                    enemy2.direction = "up";
                                    enemy2.width = 10;
                                    enemy2.height = 20;
                                }
                            }
                        }
                    } else if(enemy2.x > 500 && enemy2.x < 510 && enemy2.y > 0 && enemy2.y < 20) {
                        int v = rand.nextInt(2);
                        if(v == 0) {
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "right";
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "right";
                        }
                        else {
                            if(enemy2.direction.equals("up"))
                                enemy2.direction = "left";
                            if(enemy2.direction.equals("down"))
                                enemy2.direction = "left";
                            enemy2.width = 20;
                            enemy2.height = 10;
                        }
                    }
                    if(enemyIsCollided() || enemy2IsCollided()) {

                        String cl = crashList.getText();
                        
                        if(enemyIsCollided()) {
                            if(cl.equals("") || cl == null)
                            {
                                crashList.setText(enemy.brand 
                                        + " (-$" + enemy.fine + ")");
                            }
                            else if(!cl.equals(""))
                            {
                                crashList.setText(cl 
                                        + System.getProperty("line.separator")
                                        + enemy.brand + " (-$" + enemy.fine + ")");
                            }
                        }
                        
                        if(enemy2IsCollided()) {
                            if(cl.equals("") || cl == null)
                            {
                                crashList.setText(enemy2.brand 
                                        + " (-$" + enemy2.fine + ")");
                            }
                            else if(!cl.equals(""))
                            {
                                crashList.setText(cl 
                                        + System.getProperty("line.separator")
                                        + enemy2.brand + " (-$" + enemy2.fine + ")");
                            }
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
                        
                        u = rand.nextInt(4);
                        if( u == 0 ) {
                            enemy2.brand = "Mercedez";
                            fines += 5000;
                            enemy2.fine = 5000;
                        }
                        if( u == 1 ) {
                            enemy2.brand = "Tatoyo";
                            fines += 3000;
                            enemy2.fine = 3000;
                        }
                        if( u == 2 ) {
                            enemy2.brand = "Jeep";
                            fines += 3800;
                            enemy2.fine = 3800;
                        }
                        if( u == 3 ) {
                            enemy2.brand = "Chrysler";
                            fines += 2800;
                            enemy2.fine = 2800;
                        }

                        if(enemyIsCollided())
                            createEnemyAutomobile();
                        
                        if(enemy2IsCollided())
                            createEnemy2Automobile();

                        createAirplanes();
                    }

                    if(enemy.x < -100 || enemy.x > 1100 || enemy.y < -100 || enemy.y > 900)
                        createEnemyAutomobile();

                    if(enemy2.x < -100 || enemy2.x > 1100 || enemy2.y < -100 || enemy2.y > 900)
                        createEnemy2Automobile();

                    if(enemy.direction.equals("right"))
                        enemy.x += 2;
                    if(enemy.direction.equals("left"))
                        enemy.x -= 2;
                    if(enemy.direction.equals("down"))
                        enemy.y += 2;
                    if(enemy.direction.equals("up"))
                        enemy.y -= 2;

                    if(enemy2.direction.equals("right"))
                        enemy2.x += 2;
                    if(enemy2.direction.equals("left"))
                        enemy2.x -= 2;
                    if(enemy2.direction.equals("down"))
                        enemy2.y += 2;
                    if(enemy2.direction.equals("up"))
                        enemy2.y -= 2;

                    if(which.equals("R")) {
                        airplane_R.x -= 20;
                    }

                    if(which.equals("L")) {
                        airplane_L.x += 20;
                    }

                    if(which.equals("T")) {
                        airplane_T.y += 20;
                    }

                    if(which.equals("D")) {
                        airplane_D.y -= 20;
                    }

                    drawEnemy();
                    drawEnemy2();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException interruptedException) {
                    }
                }
            }
        };
        
        t.start();
        
        frame.addKeyListener(this);
    }
    AudioInputStream audioStream;
        File audioFile;
        DataLine.Info info;
        Clip audioClip;
                AudioFormat format;
                
    private void makeSound(String file) throws Exception {
 audioFile = new File(file);
audioStream = AudioSystem.getAudioInputStream(audioFile);
 format = audioStream.getFormat();
        
 info = new DataLine.Info(Clip.class, format);
 audioClip = (Clip) AudioSystem.getLine(info);
         audioClip.open(audioStream);
audioClip.start();
        
        
    }

    public void drawAirplanes() {
        Graphics g = panel.getGraphics();
        
        if(which.equals("L"))
            g.drawImage(airplaneL, airplane_L.x, airplane_L.y, null);
        if(which.equals("R"))
            g.drawImage(airplaneR, airplane_R.x, airplane_R.y, null);
        if(which.equals("T"))
            g.drawImage(airplaneT, airplane_T.x, airplane_T.y, null);
        if(which.equals("D"))
            g.drawImage(airplaneD, airplane_D.x, airplane_D.y, null);

        g.dispose();
    }
    
    public void createEnemyAutomobile() {
        int u = rand.nextInt(4);
        if( u == 0 ) {
            enemy.brand = "Mercedez";
        }
        if( u == 1 ) {
            enemy.brand = "Tatoyo";
        }
        if( u == 2 ) {
            enemy.brand = "Jeep";
        }
        if( u == 3 ) {
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
        u = rand.nextInt(4);
        if( u == 0 ) {
            enemy2.brand = "Mercedez";
        }
        if( u == 1 ) {
            enemy2.brand = "Tatoyo";
        }
        if( u == 2 ) {
            enemy2.brand = "Jeep";
        }
        if( u == 3 ) {
            enemy2.brand = "Chrysler";
        }
    }
    
    public void createEnemy2Automobile() {
        int v = rand.nextInt(12);
        if(v == 0) {
            enemy2.x = -20;
            enemy2.y = 5;
            enemy2.width = 20;
            enemy2.height = 10;
            enemy2.direction = "right";
        }
        if(v == 1) {
            enemy2.x = 5;
            enemy2.y = -20;
            enemy2.width = 10;
            enemy2.height = 20;
            enemy2.direction = "down";
        }
        if(v == 2) {
            enemy2.x = 505;
            enemy2.y = -20;
            enemy2.width = 10;
            enemy2.height = 20;
            enemy2.direction = "down";
        }
        if(v == 3) {
            enemy2.x = 960;
            enemy2.y = -20;
            enemy2.width = 10;
            enemy2.height = 20;
            enemy2.direction = "down";
        }
        if(v == 4) {
            enemy2.x = 1020;
            enemy2.y = 5;
            enemy2.width = 20;
            enemy2.height = 10;
            enemy2.direction = "left";
        }
        if(v == 5) {
            enemy2.x = 1020;
            enemy2.y = 405;
            enemy2.width = 20;
            enemy2.height = 10;
            enemy2.direction = "left";
        }
        if(v == 6) {
            enemy2.x = 1020;
            enemy2.y = 795;
            enemy2.width = 20;
            enemy2.height = 10;
            enemy2.direction = "up";
        }
        if(v == 7) {
            enemy2.x = 960;
            enemy2.y = 820;
            enemy2.width = 10;
            enemy2.height = 20;
            enemy2.direction = "up";
        }
        if(v == 8) {
            enemy2.x = 505;
            enemy2.y = 820;
            enemy2.width = 10;
            enemy2.height = 20;
            enemy2.direction = "up";
        }
        if(v == 9) {
            enemy2.x = 5;
            enemy2.y = 820;
            enemy2.width = 10;
            enemy2.height = 20;
            enemy2.direction = "up";
        }
        if(v == 10) {
            enemy2.x = -20;
            enemy2.y = 795;
            enemy2.width = 20;
            enemy2.height = 10;
            enemy2.direction = "right";
        }
        if(v == 11) {
            enemy2.x = -20;
            enemy2.y = 405;
            enemy2.width = 20;
            enemy2.height = 10;
            enemy2.direction = "right";
        }
    }
    boolean firstTimeAirplaneFlies = true;
    public void createAirplanes() {
        if(firstTimeAirplaneFlies) {
            firstTimeAirplaneFlies = false;
            return;
        }
        int v = rand.nextInt(4);
        if(v == 0) {
            airplane_L.x = -20;
            airplane_L.y = 400;
            which = "L";
            airplane_R.x = -1000;
            airplane_T.x = -1000;
            airplane_D.x = -1000;
        }
        if(v == 1) {
            airplane_R.x = 1020;
            airplane_R.y = 400;
            which = "R";
            airplane_L.x = -1000;
            airplane_T.x = -1000;
            airplane_D.x = -1000;
        }
        if(v == 2) {
            airplane_T.x = 500;
            airplane_T.y = -20;
            which = "T";
            airplane_R.x = -1000;
            airplane_L.x = -1000;
            airplane_D.x = -1000;
        }
        if(v == 3) {
            airplane_D.x = 500;
            airplane_D.y = 800;
            which = "D";
            airplane_R.x = -1000;
            airplane_T.x = -1000;
            airplane_L.x = -1000;
        }

        try {
            makeSound("sound.wav");
        } catch(Exception e1) {
            try {
                makeSound("src/sound.wav");
            } catch(Exception e2) {}
        }
    }

    public void drawCities() {
        try {
        Graphics g = panel.getGraphics();
        
        g.setColor(Color.red);
        
        g.setFont(new Font("Curlz MT", Font.ITALIC, 29));

        g.drawString("L.A.", 70, 120);
        g.drawString("EAST L.A.", 310, 120);
        g.drawString("POMONA", 610, 120);
        g.drawString("ARTESIA", 810, 120);
        g.drawString("PICO RIVERA", 310, 300);
        g.drawString("PASADENA", 570, 290);
        g.drawString("MONTEREY", 790, 300);
        g.drawString("PARK", 820, 350);
        g.drawString("CAMPTON", 40, 300);
        g.drawString("SOUTH L.A.", 40, 500);
        g.drawString("CORONA", 310, 500);
        g.drawString("RIVERSIDE", 550, 500);
        g.drawString("CHINO", 820, 500);
        g.drawString("LONG BEACH", 40, 700);
        g.drawString("ANAHEIM", 310, 700);
        g.drawString("SAN PEDRO", 570, 700);
        g.drawString("TEMECULA", 780, 700);
 
        } catch(Exception e) {frame.dispose();}   
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
    
    public boolean enemy2IsCollided() {
        try {
            if(enemy2.direction.equals("left") || enemy2.direction.equals("right")) {
                if(car.direction.equals("left") || car.direction.equals("right")) {
                    if(car.direction.equals("right")) {
                        if(car.x+car.width >= enemy2.x && car.x+car.width <= enemy2.x + enemy2.width) {
                            if(car.y >= enemy2.y && car.y <= enemy2.y + enemy2.height) {
                                return true;
                            }
                        }
                    }
                    if(car.direction.equals("left")) {
                        if(car.x+car.width >= enemy2.x && car.x+car.width <= enemy2.x + enemy2.width) {
                            if(car.y >= enemy2.y && car.y <= enemy2.y + enemy2.height) {
                                return true;
                            }
                        }
                    }
                }
                if(car.direction.equals("up") || car.direction.equals("down")) {
                    if(car.direction.equals("down")) {
                        if(car.x+car.width >= enemy2.x && car.x+car.width <= enemy2.x + enemy2.width) {
                            if(car.y >= enemy2.y && car.y <= enemy2.y + enemy2.height) {
                                return true;
                            }
                        }
                    }
                    if(car.direction.equals("up")) {
                        if(car.x+car.width >= enemy2.x && car.x+car.width <= enemy2.x + enemy2.width) {
                            if(car.y >= enemy2.y && car.y <= enemy2.y + enemy2.height) {
                                return true;
                            }
                        }
                    }
                }
            }
            if(enemy2.direction.equals("up") || enemy2.direction.equals("down")) {
                if(car.direction.equals("left") || car.direction.equals("right")) {
                    if(car.x >= enemy2.x && car.x <= enemy2.x + enemy2.width) {
                        if(car.y >= enemy2.y && car.y <= enemy2.y + enemy2.height) {
                            return true;
                        }
                    }
                }
                if(car.direction.equals("up") || car.direction.equals("down")) {
                    if(car.x >= enemy2.x && car.x <= enemy2.x + enemy2.width) {
                        if(car.y >= enemy2.y && car.y <= enemy2.y + enemy2.height) {
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
        try {
        Graphics g = panel.getGraphics();
        g.setColor(car.color);
        g.fillRect(car.x, car.y, car.width, car.height);
        g.dispose();
        } catch(Exception e) {frame.dispose();}
    }
    
    public void drawPotholes() {
        try {
            Graphics g = panel.getGraphics();
            g.setColor(Color.green);
            for(int i=0; i<potholes.size(); i++) {
                g.fillRect(potholes.get(i).x, potholes.get(i).y, potholes.get(i).width, potholes.get(i).width);
            }
            g.dispose();
        } catch(Exception e) {frame.dispose();}
    }

    class Car {
        int x, y;
        int width;
        int height;
        Color color;
        String direction;
        String brand;
        int fine;
        int timer;
        int speed;
        
        Car() {
            speed = 5;
            timer = 0;
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
            y+=speed;
            direction = "down";
        }

        void moveUp() {
            width = 10;
            height = 20;
            y-=speed;
            direction = "up";
        }

        void moveRight() {
            width = 20;
            height = 10;
            x+=speed;
            direction = "right";
        }

        void moveLeft() {
            width = 20;
            height = 10;
            x-=speed;
            direction = "left";
        }
    }
    
    class Map {
        Color backColor = new Color(240, 231, 201);
        Color lane = Color.GRAY;
        Color line = Color.WHITE;
        
        void drawMap() {
            Graphics g = panel.getGraphics();

            try {
                g.setColor(Color.LIGHT_GRAY);

                g.fillRect(0, 0, 1000, 800);

                g.setColor(Color.GRAY);

                g.fillRect(0, 0, 1000, 20);
                g.fillRect(0, 200, 1000, 20);
                g.fillRect(0, 400, 1000, 20);
                g.fillRect(0, 600, 1000, 20);
                g.fillRect(0, 740, 1000, 20);

                g.fillRect(0, 0, 20, 800);
                g.fillRect(250, 0, 20, 800);
                g.fillRect(500, 0, 20, 800);
                g.fillRect(750, 0, 20, 800);
                g.fillRect(960, 0, 20, 800);

                g.setColor(Color.WHITE);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(2*i, 10, 5, 1);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(2*i, 410, 5, 1);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(2*i, 750, 5, 1);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(10, 2*i, 1, 5);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(510, 2*i, 1, 5);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(970, 2*i, 1, 5);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(2*i, 210, 5, 1);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(2*i, 610, 5, 1);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(10, 2*i, 1, 5);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(260, 2*i, 1, 5);
                for(int i=0; i<=500; i+=5)
                    g.drawRect(760, 2*i, 1, 5);

                g.dispose();
            } catch(Exception e) {frame.dispose();}
        }
    }
    
    public void drawMap() {
        map.drawMap();
    }
 
    public void run() {
        GoAroundi goAroundi = new GoAroundi();
        goAroundi.frame.dispose();
    }
    
    public static void main(String args[]) {
        
        Thread t = new Thread(new GoAroundi());
        t.start();
    }
}