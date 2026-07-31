import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;

public class Main extends JFrame {
    private Figure[] figures = new Figure[1000];
    private int kFigures = 0;

    public static final int LINE_TOOL = 1;
    public static final int OVAL_TOOL = 2;
    public static final int RECT_TOOL = 3;
    public static final int CIRCLE_TOOL = 4;
    public static final int SQUARE_TOOL = 5;
    public static final int BRUSH_TOOL = 6;
    public static final int ERASER_TOOL = 7;

    private int tool = LINE_TOOL;
    private Color currentColor = Color.BLACK;
    private int strokeWidth = 1;
    private Point beginPoint;
    private Point endPoint;
    private ArrayList<Point> brushPoints = new ArrayList<Point>();
    private boolean isDrawing = false;
    private boolean isEnglish = false;

    private Stack<DrawingState> undoStack = new Stack<DrawingState>();
    private Stack<DrawingState> redoStack = new Stack<DrawingState>();

    private JMenu fileMenu;
    private JMenu toolsMenu;
    private JMenu colorMenu;
    private JMenu strokeMenu;
    private JMenu helpMenu;
    private JMenu languageMenu;

    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;
    private JMenuItem aboutItem;
    private JMenuItem russianItem;
    private JMenuItem englishItem;

    private JButton undoButton;
    private JButton redoButton;

    public Main(String title) {
        super(title);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        MyPanel panel = new MyPanel(true);
        add(panel, BorderLayout.CENTER);

        JPanel toolbar = createToolbar();
        add(toolbar, BorderLayout.NORTH);

        createMenus();
        updateLanguage();

        setJMenuBar(createMenuBar());
        setVisible(true);
        saveState();
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(Color.LIGHT_GRAY);

        undoButton = new JButton("◄");
        redoButton = new JButton("►");
        undoButton.setFont(new Font("Arial", Font.BOLD, 16));
        redoButton.setFont(new Font("Arial", Font.BOLD, 16));
        undoButton.setToolTipText("Отменить (Ctrl+Z)");
        redoButton.setToolTipText("Вернуть (Ctrl+Y)");
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });

        toolbar.add(undoButton);
        toolbar.add(redoButton);

        return toolbar;
    }

    private void saveState() {
        Figure[] copy = new Figure[kFigures];
        for (int i = 0; i < kFigures; i++) {
            copy[i] = figures[i].clone();
        }
        undoStack.push(new DrawingState(copy, kFigures));
        redoStack.clear();
        updateButtons();
    }

    private void undo() {
        if (undoStack.size() > 1) {
            redoStack.push(undoStack.pop());
            DrawingState state = undoStack.peek();
            restoreState(state);
            updateButtons();
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            DrawingState state = redoStack.pop();
            undoStack.push(state);
            restoreState(state);
            updateButtons();
        }
    }

    private void restoreState(DrawingState state) {
        kFigures = state.count;
        for (int i = 0; i < kFigures; i++) {
            figures[i] = state.figures[i].clone();
        }
        repaint();
    }

    private void updateButtons() {
        undoButton.setEnabled(undoStack.size() > 1);
        redoButton.setEnabled(!redoStack.isEmpty());
    }

    private void createMenus() {
        fileMenu = new JMenu();
        toolsMenu = new JMenu();
        colorMenu = new JMenu();
        strokeMenu = new JMenu();
        helpMenu = new JMenu();
        languageMenu = new JMenu();

        openItem = new JMenuItem();
        saveItem = new JMenuItem();
        exitItem = new JMenuItem();
        aboutItem = new JMenuItem();
        russianItem = new JMenuItem("Русский");
        englishItem = new JMenuItem("English");

        JMenuItem lineTool = new JMenuItem();
        JMenuItem ovalTool = new JMenuItem();
        JMenuItem rectTool = new JMenuItem();
        JMenuItem circleTool = new JMenuItem();
        JMenuItem squareTool = new JMenuItem();
        JMenuItem brushTool = new JMenuItem();
        JMenuItem eraserTool = new JMenuItem();

        toolsMenu.add(lineTool);
        toolsMenu.add(ovalTool);
        toolsMenu.add(rectTool);
        toolsMenu.addSeparator();
        toolsMenu.add(circleTool);
        toolsMenu.add(squareTool);
        toolsMenu.addSeparator();
        toolsMenu.add(brushTool);
        toolsMenu.add(eraserTool);

        JMenuItem blackItem = new JMenuItem();
        JMenuItem redItem = new JMenuItem();
        JMenuItem greenItem = new JMenuItem();
        JMenuItem blueItem = new JMenuItem();
        JMenuItem yellowItem = new JMenuItem();
        JMenuItem orangeItem = new JMenuItem();
        JMenuItem magentaItem = new JMenuItem();
        JMenuItem cyanItem = new JMenuItem();
        JMenuItem grayItem = new JMenuItem();
        JMenuItem chooseItem = new JMenuItem();

        colorMenu.add(blackItem);
        colorMenu.add(redItem);
        colorMenu.add(greenItem);
        colorMenu.add(blueItem);
        colorMenu.add(yellowItem);
        colorMenu.add(orangeItem);
        colorMenu.add(magentaItem);
        colorMenu.add(cyanItem);
        colorMenu.add(grayItem);
        colorMenu.addSeparator();
        colorMenu.add(chooseItem);

        JMenuItem size1 = new JMenuItem();
        JMenuItem size2 = new JMenuItem();
        JMenuItem size3 = new JMenuItem();
        JMenuItem size4 = new JMenuItem();
        JMenuItem size5 = new JMenuItem();
        JMenuItem size8 = new JMenuItem();
        JMenuItem size10 = new JMenuItem();
        JMenuItem size15 = new JMenuItem();
        JMenuItem size20 = new JMenuItem();

        strokeMenu.add(size1);
        strokeMenu.add(size2);
        strokeMenu.add(size3);
        strokeMenu.add(size4);
        strokeMenu.add(size5);
        strokeMenu.addSeparator();
        strokeMenu.add(size8);
        strokeMenu.add(size10);
        strokeMenu.add(size15);
        strokeMenu.add(size20);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        helpMenu.add(aboutItem);

        languageMenu.add(russianItem);
        languageMenu.add(englishItem);

        lineTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = LINE_TOOL;
            }
        });

        ovalTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = OVAL_TOOL;
            }
        });

        rectTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = RECT_TOOL;
            }
        });

        circleTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = CIRCLE_TOOL;
            }
        });

        squareTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = SQUARE_TOOL;
            }
        });

        brushTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = BRUSH_TOOL;
            }
        });

        eraserTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tool = ERASER_TOOL;
            }
        });

        blackItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.BLACK;
            }
        });

        redItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.RED;
            }
        });

        greenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.GREEN;
            }
        });

        blueItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.BLUE;
            }
        });

        yellowItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.YELLOW;
            }
        });

        orangeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.ORANGE;
            }
        });

        magentaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.MAGENTA;
            }
        });

        cyanItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.CYAN;
            }
        });

        grayItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.GRAY;
            }
        });

        chooseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color chosenColor = JColorChooser.showDialog(Main.this,
                        isEnglish ? "Choose color" : "Выберите цвет", currentColor);
                if (chosenColor != null) {
                    currentColor = chosenColor;
                }
            }
        });

        size1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 1;
            }
        });

        size2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 2;
            }
        });

        size3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 3;
            }
        });

        size4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 4;
            }
        });

        size5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 5;
            }
        });

        size8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 8;
            }
        });

        size10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 10;
            }
        });

        size15.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 15;
            }
        });

        size20.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strokeWidth = 20;
            }
        });

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(".");
                fileChooser.setDialogTitle(isEnglish ? "Select file to open" : "Выберите файл для открытия");
                if (fileChooser.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        Scanner fin = new Scanner(fileChooser.getSelectedFile());
                        kFigures = fin.nextInt();
                        fin.nextLine();
                        for (int i = 0; i < kFigures; i++) {
                            int type = fin.nextInt();
                            int red = fin.nextInt();
                            int green = fin.nextInt();
                            int blue = fin.nextInt();
                            int stroke = fin.nextInt();
                            Color color = new Color(red, green, blue);

                            if (type == BRUSH_TOOL) {
                                int pointCount = fin.nextInt();
                                ArrayList<Point> points = new ArrayList<Point>();
                                for (int j = 0; j < pointCount; j++) {
                                    int x = fin.nextInt();
                                    int y = fin.nextInt();
                                    points.add(new Point(x, y));
                                }
                                figures[i] = new Figure(type, points, color, stroke);
                            } else {
                                int x1 = fin.nextInt();
                                int y1 = fin.nextInt();
                                int x2 = fin.nextInt();
                                int y2 = fin.nextInt();
                                figures[i] = new Figure(type, new Point(x1, y1), new Point(x2, y2), color, stroke);
                            }
                        }
                        fin.close();
                        Main.this.repaint();
                        saveState();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(Main.this,
                                (isEnglish ? "Error opening file: " : "Ошибка открытия файла: ") + ex.getMessage(),
                                isEnglish ? "Error" : "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(".");
                fileChooser.setDialogTitle(isEnglish ? "Specify file name to save" : "Укажите имя файла для сохранения");
                if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        PrintStream fout = new PrintStream(fileChooser.getSelectedFile());
                        fout.println(kFigures);
                        for (int i = 0; i < kFigures; i++)
                            fout.println(figures[i]);
                        fout.close();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(Main.this,
                                (isEnglish ? "Error saving: " : "Ошибка сохранения: ") + ex.getMessage(),
                                isEnglish ? "Error" : "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(Main.this,
                        isEnglish ? "About" : "О программе", true);
                dialog.setBounds(100, 50, 250, 120);
                dialog.setLayout(new BorderLayout());

                JLabel label = new JLabel(isEnglish ? "Graphics Editor v1.0" : "Графический редактор v1.0",
                        SwingConstants.CENTER);
                JButton okButton = new JButton(isEnglish ? "OK" : "OK");

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        dialog.dispose();
                    }
                });

                dialog.add(label, BorderLayout.CENTER);
                dialog.add(okButton, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        });

        russianItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEnglish = false;
                updateLanguage();
            }
        });

        englishItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEnglish = true;
                updateLanguage();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("control Z"), "undo");
        getRootPane().getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("control Y"), "redo");
        getRootPane().getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
    }

    private void updateLanguage() {
        if (isEnglish) {
            setTitle("Graphics Editor");
            fileMenu.setText("File");
            toolsMenu.setText("Tool");
            colorMenu.setText("Color");
            strokeMenu.setText("Stroke");
            helpMenu.setText("Help");
            languageMenu.setText("Language");

            openItem.setText("Open...");
            saveItem.setText("Save...");
            exitItem.setText("Exit");
            aboutItem.setText("About");
            undoButton.setToolTipText("Undo (Ctrl+Z)");
            redoButton.setToolTipText("Redo (Ctrl+Y)");

            Component[] components = toolsMenu.getMenuComponents();
            if (components.length >= 8) {
                ((JMenuItem)components[0]).setText("Line");
                ((JMenuItem)components[1]).setText("Ellipse");
                ((JMenuItem)components[2]).setText("Rectangle");
                ((JMenuItem)components[4]).setText("Circle");
                ((JMenuItem)components[5]).setText("Square");
                ((JMenuItem)components[7]).setText("Brush");
                ((JMenuItem)components[8]).setText("Eraser");
            }

            components = colorMenu.getMenuComponents();
            if (components.length >= 10) {
                ((JMenuItem)components[0]).setText("Black");
                ((JMenuItem)components[1]).setText("Red");
                ((JMenuItem)components[2]).setText("Green");
                ((JMenuItem)components[3]).setText("Blue");
                ((JMenuItem)components[4]).setText("Yellow");
                ((JMenuItem)components[5]).setText("Orange");
                ((JMenuItem)components[6]).setText("Magenta");
                ((JMenuItem)components[7]).setText("Cyan");
                ((JMenuItem)components[8]).setText("Gray");
                ((JMenuItem)components[10]).setText("Choose color...");
            }

            components = strokeMenu.getMenuComponents();
            if (components.length >= 9) {
                for (int i = 0; i < 5; i++) {
                    ((JMenuItem)components[i]).setText((i+1) + " px");
                }
                ((JMenuItem)components[6]).setText("8 px");
                ((JMenuItem)components[7]).setText("10 px");
                ((JMenuItem)components[8]).setText("15 px");
                ((JMenuItem)components[9]).setText("20 px");
            }
        } else {
            setTitle("Графический редактор");
            fileMenu.setText("Файл");
            toolsMenu.setText("Инструмент");
            colorMenu.setText("Цвет");
            strokeMenu.setText("Толщина");
            helpMenu.setText("Справка");
            languageMenu.setText("Язык");

            openItem.setText("Открыть...");
            saveItem.setText("Сохранить...");
            exitItem.setText("Выход");
            aboutItem.setText("О программе");
            undoButton.setToolTipText("Отменить (Ctrl+Z)");
            redoButton.setToolTipText("Вернуть (Ctrl+Y)");

            Component[] components = toolsMenu.getMenuComponents();
            if (components.length >= 8) {
                ((JMenuItem)components[0]).setText("Прямая");
                ((JMenuItem)components[1]).setText("Эллипс");
                ((JMenuItem)components[2]).setText("Прямоугольник");
                ((JMenuItem)components[4]).setText("Круг");
                ((JMenuItem)components[5]).setText("Квадрат");
                ((JMenuItem)components[7]).setText("Кисточка");
                ((JMenuItem)components[8]).setText("Ластик");
            }

            components = colorMenu.getMenuComponents();
            if (components.length >= 10) {
                ((JMenuItem)components[0]).setText("Черный");
                ((JMenuItem)components[1]).setText("Красный");
                ((JMenuItem)components[2]).setText("Зеленый");
                ((JMenuItem)components[3]).setText("Синий");
                ((JMenuItem)components[4]).setText("Желтый");
                ((JMenuItem)components[5]).setText("Оранжевый");
                ((JMenuItem)components[6]).setText("Пурпурный");
                ((JMenuItem)components[7]).setText("Голубой");
                ((JMenuItem)components[8]).setText("Серый");
                ((JMenuItem)components[10]).setText("Выбрать цвет...");
            }

            components = strokeMenu.getMenuComponents();
            if (components.length >= 9) {
                for (int i = 0; i < 5; i++) {
                    ((JMenuItem)components[i]).setText((i+1) + " px");
                }
                ((JMenuItem)components[6]).setText("8 px");
                ((JMenuItem)components[7]).setText("10 px");
                ((JMenuItem)components[8]).setText("15 px");
                ((JMenuItem)components[9]).setText("20 px");
            }
        }
        updateButtons();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(colorMenu);
        menuBar.add(strokeMenu);
        menuBar.add(languageMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    public static void main(String[] args) {
        Main main = new Main("Графический редактор");
    }

    class MyPanel extends JPanel implements MouseListener, MouseMotionListener {
        public MyPanel(boolean isDoubleBuffered) {
            super(isDoubleBuffered);
            addMouseListener(this);
            addMouseMotionListener(this);
            setBackground(Color.WHITE);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            if (tool == BRUSH_TOOL && isDrawing && brushPoints.size() > 1) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(currentColor);
                g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < brushPoints.size() - 1; i++) {
                    Point p1 = brushPoints.get(i);
                    Point p2 = brushPoints.get(i + 1);
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            } else if (beginPoint != null && endPoint != null && tool != BRUSH_TOOL && tool != ERASER_TOOL) {
                Figure tempFigure = new Figure(tool, beginPoint, endPoint, currentColor, strokeWidth);
                tempFigure.draw(g);
            }

            for (int i = 0; i < kFigures; i++)
                figures[i].draw(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (tool == ERASER_TOOL) {
                Point clickPoint = e.getPoint();
                int indexToRemove = -1;
                int minDistance = Integer.MAX_VALUE;

                for (int i = kFigures - 1; i >= 0; i--) {
                    int dist = figures[i].distanceTo(clickPoint);
                    if (dist < minDistance) {
                        minDistance = dist;
                        if (dist < 20) {
                            indexToRemove = i;
                        }
                    }
                }

                if (indexToRemove != -1) {
                    for (int i = indexToRemove; i < kFigures - 1; i++) {
                        figures[i] = figures[i + 1];
                    }
                    kFigures--;
                    repaint();
                    saveState();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (tool == BRUSH_TOOL) {
                brushPoints.clear();
                brushPoints.add(e.getPoint());
                isDrawing = true;
            } else if (tool != ERASER_TOOL) {
                beginPoint = e.getPoint();
                endPoint = beginPoint;
            }
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (tool == BRUSH_TOOL) {
                isDrawing = false;
                if (brushPoints.size() > 1 && kFigures < figures.length) {
                    ArrayList<Point> pointsCopy = new ArrayList<Point>(brushPoints);
                    figures[kFigures] = new Figure(tool, pointsCopy, currentColor, strokeWidth);
                    kFigures++;
                    saveState();
                }
                brushPoints.clear();
            } else if (tool != ERASER_TOOL) {
                endPoint = e.getPoint();
                if (kFigures < figures.length && !beginPoint.equals(endPoint)) {
                    figures[kFigures] = new Figure(tool, beginPoint, endPoint, currentColor, strokeWidth);
                    kFigures++;
                    saveState();
                }
            }
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (tool == BRUSH_TOOL) {
                brushPoints.add(e.getPoint());
            } else if (tool != ERASER_TOOL) {
                endPoint = e.getPoint();
            }
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Main.this.setTitle((isEnglish ? "(" : "(") + e.getX() + "," + e.getY() + ")");
        }
    }
}

class Figure {
    private int type;
    private Point begin;
    private Point end;
    private ArrayList<Point> points;
    private Color color;
    private int strokeWidth;

    public Figure(int type, Point begin, Point end, Color color, int strokeWidth) {
        this.type = type;
        this.begin = begin;
        this.end = end;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.points = null;
    }

    public Figure(int type, ArrayList<Point> points, Color color, int strokeWidth) {
        this.type = type;
        this.points = new ArrayList<Point>(points);
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.begin = null;
        this.end = null;
    }

    public Figure clone() {
        if (type == Main.BRUSH_TOOL) {
            ArrayList<Point> pointsCopy = new ArrayList<Point>(points);
            return new Figure(type, pointsCopy, color, strokeWidth);
        } else {
            return new Figure(type, new Point(begin), new Point(end), color, strokeWidth);
        }
    }

    public int distanceTo(Point p) {
        if (type == Main.BRUSH_TOOL) {
            if (points != null && points.size() > 1) {
                int minDist = Integer.MAX_VALUE;
                for (int i = 0; i < points.size() - 1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i + 1);
                    int dist = (int) distanceToSegment(p1, p2, p);
                    if (dist < minDist) {
                        minDist = dist;
                    }
                }
                return minDist;
            }
            return Integer.MAX_VALUE;
        }

        int x = Math.min(begin.x, end.x);
        int y = Math.min(begin.y, end.y);
        int width = Math.abs(end.x - begin.x);
        int height = Math.abs(end.y - begin.y);

        switch (type) {
            case Main.LINE_TOOL:
                return (int) distanceToSegment(begin, end, p);
            case Main.OVAL_TOOL:
            case Main.CIRCLE_TOOL:
                return distanceToOval(p, x, y, width, height);
            case Main.RECT_TOOL:
            case Main.SQUARE_TOOL:
                return distanceToRect(p, x, y, width, height);
            default:
                return Integer.MAX_VALUE;
        }
    }

    private double distanceToSegment(Point a, Point b, Point p) {
        double dx = b.x - a.x;
        double dy = b.y - a.y;
        double len2 = dx * dx + dy * dy;

        if (len2 == 0) {
            return p.distance(a);
        }

        double t = ((p.x - a.x) * dx + (p.y - a.y) * dy) / len2;
        t = Math.max(0, Math.min(1, t));

        double projX = a.x + t * dx;
        double projY = a.y + t * dy;

        return Math.sqrt((p.x - projX) * (p.x - projX) + (p.y - projY) * (p.y - projY));
    }

    private int distanceToRect(Point p, int x, int y, int width, int height) {
        int dx = Math.max(x - p.x, Math.max(0, p.x - (x + width)));
        int dy = Math.max(y - p.y, Math.max(0, p.y - (y + height)));
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    private int distanceToOval(Point p, int x, int y, int width, int height) {
        if (width == 0 || height == 0) return Integer.MAX_VALUE;

        double cx = x + width / 2.0;
        double cy = y + height / 2.0;
        double rx = width / 2.0;
        double ry = height / 2.0;

        double dx = p.x - cx;
        double dy = p.y - cy;

        double dist = Math.sqrt((dx * dx) / (rx * rx) + (dy * dy) / (ry * ry));
        return (int) Math.abs((dist - 1) * Math.min(rx, ry));
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (type == Main.BRUSH_TOOL) {
            if (points != null && points.size() > 1) {
                for (int i = 0; i < points.size() - 1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i + 1);
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
            return;
        }

        int x = Math.min(begin.x, end.x);
        int y = Math.min(begin.y, end.y);
        int width = Math.abs(end.x - begin.x);
        int height = Math.abs(end.y - begin.y);

        switch (type) {
            case Main.LINE_TOOL:
                g2d.drawLine(begin.x, begin.y, end.x, end.y);
                break;
            case Main.OVAL_TOOL:
                g2d.drawOval(x, y, width, height);
                break;
            case Main.RECT_TOOL:
                g2d.drawRect(x, y, width, height);
                break;
            case Main.CIRCLE_TOOL:
                int circleSize = Math.min(width, height);
                g2d.drawOval(x, y, circleSize, circleSize);
                break;
            case Main.SQUARE_TOOL:
                int squareSize = Math.min(width, height);
                g2d.drawRect(x, y, squareSize, squareSize);
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ");
        sb.append(color.getRed()).append(" ");
        sb.append(color.getGreen()).append(" ");
        sb.append(color.getBlue()).append(" ");
        sb.append(strokeWidth).append(" ");

        if (type == Main.BRUSH_TOOL) {
            sb.append(points.size()).append(" ");
            for (Point p : points) {
                sb.append(p.x).append(" ").append(p.y).append(" ");
            }
        } else {
            sb.append(begin.x).append(" ").append(begin.y).append(" ");
            sb.append(end.x).append(" ").append(end.y).append(" ");
        }

        return sb.toString().trim();
    }
}

class DrawingState {
    Figure[] figures;
    int count;

    public DrawingState(Figure[] figures, int count) {
        this.figures = figures;
        this.count = count;
    }
}
