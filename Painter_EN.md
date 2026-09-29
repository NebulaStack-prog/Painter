## Part 1. Main Document.

### 1. Title and Basic Information.

• **Name:** Painter

• **Purpose:** Project No. 12. Product.

• **Project Phase:** Phase II.

• **Technology Stack:** Java (Swing library).

• **Project Status:** Fully completed.

### 2. Project Overview.

**Painter** is a desktop graphics editor developed in Java using the Swing library.

The application allows users to draw on a white canvas using various tools: lines, ellipses, rectangles, circles, squares, a brush, and an eraser.

The editor supports color selection (9 preset colors + custom colors using `JColorChooser`), line width adjustment (from 1 to 20 pixels), as well as Undo and Redo functionality.

Key features include a bilingual interface (Russian/English), saving and loading drawings as text files, and displaying the cursor coordinates in the window title.

The project is a fully functional graphics editor with a wide range of tools and a well-designed user interface.

### 3. Project Goals.

• Create a functional graphics editor capable of drawing basic shapes.

• Implement 7 drawing tools: line, ellipse, rectangle, circle, square, brush, and eraser.

• Implement a color selection system (preset + custom colors).

• Implement a line width selection system.

• Implement an Undo/Redo system using stacks.

• Implement saving and loading drawings as text files.

• Implement a bilingual interface (Russian and English).

• Implement shape deletion using the eraser (on click).

• Ensure proper handling of mouse events (press, release, and drag).

• Demonstrate a desktop application development approach using Java, Swing, and AWT.

### 4. Project Components.

The project consists of a single file containing several classes:

• **Main.java** – the main class extending `JFrame`. Contains the entry point, menus, toolbar, and the `MyPanel` inner class responsible for drawing.

• **Figure class** – the graphical shape model. Stores the type, coordinates, color, line width, and points (for the brush).

• **DrawingState class** – a state snapshot used by the Undo/Redo system. Stores an array of figures and their current count.

### 5. Usage Instructions.

* **5.1. Launch:**

• Make sure JDK (Java Development Kit) version 8 or later is installed.

• Compile and run `Main.java` (for example, using IntelliJ IDEA or from the terminal: `javac Main.java && java Main`).

* **5.2. Application Purpose:**

• Create simple raster drawings using a set of drawing tools.

• Save and load drawings for further editing.

* **5.3. Controls:**

• **Left mouse button (press):** starts drawing a shape or a brush stroke.

• **Left mouse button (drag):** draws a shape or continues the brush stroke.

• **Left mouse button (release):** finishes the drawing operation and saves the shape.

• **Left mouse button (click with the eraser):** deletes the nearest shape.

• **Ctrl+Z:** undo the last action.

• **Ctrl+Y:** redo the undone action.

• **Mouse:** control menus and buttons.

* **5.4. Interface:**

• Main window size: 600x400 pixels.

• Toolbar at the top with ◄ (Undo) and ► (Redo) buttons.

• **Menu:**

• File: Open, Save, Exit.

• Tool: Line, Ellipse, Rectangle, Circle, Square, Brush, Eraser.

• Color: Black, Red, Green, Blue, Yellow, Orange, Purple, Cyan, Gray, Choose Color.

• Width: 1, 2, 3, 4, 5, 8, 10, 15, 20 px.

• Language: Русский, English.

• Help: About.

• The window title displays the current cursor coordinates (for example, (150,200)).

* **5.5. Tools:**

• **Line** – draws a line from the point where the mouse button is pressed to the point where it is released.

• **Ellipse** – draws an ellipse within a rectangular area.

• **Rectangle** – draws a rectangle.

• **Circle** – draws a circle (diameter = min(width, height)).

• **Square** – draws a square (side length = min(width, height)).

• **Brush** – draws freehand lines following the mouse trajectory.

• **Eraser** – deletes the nearest shape on click (within a radius of 20 pixels).

## Part 2. Technical Document.

### 1. Development Goals.

• The primary goal of the development was to create a graphics editor in Java using Swing to strengthen skills in GUI development, mouse event handling, and object-oriented programming.

• The project also focused on learning how to work with menus, toolbars, and dialog windows (`JColorChooser`, `JFileChooser`), as well as implementing an Undo/Redo system using stacks.

• The tasks included implementing 7 drawing tools, working with graphics through `Graphics2D`, serializing shapes to a text file, supporting two languages, and deleting shapes using the eraser.

### 2. Technologies Used.

• **Programming Language:** Java

• **Graphics Libraries:** Swing, AWT.

• **Standard Libraries:**

* `java.util.ArrayList` – for storing brush points.

* `java.util.Stack` – for the Undo/Redo stacks.

* `java.util.Scanner` – for reading files.

* `java.io.PrintStream` – for writing files.

* `java.io.FileNotFoundException` – for handling input/output errors.

• **Swing/AWT Modules:**

* `javax.swing.*` – `JFrame`, `JPanel`, `JMenu`, `JMenuItem`, `JButton`, `JColorChooser`, `JFileChooser`, `JOptionPane`, `JDialog`.

* `java.awt.*` – `Color`, `Graphics`, `Graphics2D`, `BasicStroke`, `Point`, `BorderLayout`, `FlowLayout`, `event.*`.

### 3. Project Architecture.

• The project is implemented as a desktop application divided into three main classes:

• `Main` – the main window, menus, and application logic.

• `Figure` – the graphical shape model.

• `DrawingState` – a state snapshot used by the Undo/Redo system.

• Main thread: event-driven (Swing Event Dispatch Thread).

• The logic is divided between the following components:

* `Main` – manages menus, tools, colors, and line width.

* `MyPanel` (inner class) – handles rendering and mouse events.

* `Figure` – stores and renders individual shapes.

* During each repaint event, the background is cleared, the temporary shape is rendered, and all saved shapes are drawn.

### 4. Project Structure.

• **Initialization:** creation of the `JFrame` window, `MyPanel` drawing panel, toolbar, and menus.

• **`Main` class fields:**

* `figures` – array of figures (up to 1000).

* `kFigures` – current number of figures.

* `tool` – currently selected tool.

* `currentColor`, `strokeWidth` – current color and line width.

* `beginPoint`, `endPoint` – starting and ending coordinates of the shape.

* `brushPoints` – list of points for the brush.

* `undoStack`, `redoStack` – Undo/Redo stacks.

* `isEnglish` – language flag.

• **`Main` methods:**

* `createToolbar()` – creates the toolbar with Undo/Redo buttons.

* `createMenus()` – creates all menus and their handlers.

* `createMenuBar()` – builds the menu bar.

* `updateLanguage()` – updates the interface text.

* `saveState()` – saves the current state for Undo.

* `undo()`, `redo()` – undo and redo actions.

* `restoreState(state)` – restores a saved state.

* `updateButtons()` – updates button availability.

• **`MyPanel` class:**

* `paint(g)` – renders the background, temporary shape, and all saved shapes.

* `mousePressed`, `mouseReleased`, `mouseDragged`, `mouseClicked` – handle mouse events.

• **`Figure` class:**

* Constructors for regular shapes and brush strokes.

* `draw(g)` – renders the shape.

* `distanceTo(p)` – calculates the distance from a point to the shape.

* `clone()` – creates a copy of the shape.

* `toString()` – serializes the shape into a string for saving.

• **`DrawingState` class:**

* Stores the array of figures and the current figure count.

### 5. Key System Components.

• **Canvas (`MyPanel`):** Extends `JPanel` and implements `MouseListener` and `MouseMotionListener`. Responsible for rendering and mouse event handling.

• **Shapes (`Figure`):** Store the type, coordinates, color, and line width. For brush strokes, the class also stores a list of points. The `distanceTo()` method is used by the eraser to find the nearest shape.

• **Undo/Redo System:** Implemented using two `Stack<DrawingState>` stacks (`undoStack` and `redoStack`). A state snapshot is saved through `saveState()` whenever a modification is made.

• **Save/Load System:** Uses a text-based format: the first line contains the number of shapes, followed by one line for each shape. Brush strokes are serialized together with their complete list of points.

• **Bilingual Interface:** Implemented using the `isEnglish` flag and the `updateLanguage()` method, which updates the text of all menu items and buttons.

• **Eraser:** On click, finds the nearest shape using `distanceTo()` within a radius of 20 pixels and removes it from the array by shifting the remaining elements.

• **Keyboard Shortcuts:** Implemented using `InputMap` and `ActionMap` for Ctrl+Z (Undo) and Ctrl+Y (Redo).

### 6. User Interface Implementation.

• The interface is fully implemented using Swing and AWT.

• Main elements: `JFrame` window, `JPanel` drawing canvas, `JMenuBar`, toolbar with buttons, and `JColorChooser`, `JFileChooser`, `JOptionPane`, and `JDialog` dialogs.

• Rendering is performed using `Graphics` and `Graphics2D` in the `paint()` method.

• `BasicStroke` is used to configure line width and stroke style.

### 7. Development Process.

Development was carried out in several stages:

• creating the basic `JFrame` window with a drawing panel;

• implementing line and shape rendering;

• adding tool, color, and line width menus;

• implementing mouse event handling for drawing;

• adding the brush tool;

• implementing the eraser tool with nearest-shape detection;

• adding the Undo/Redo system using stacks;

• implementing saving and loading to/from a text file;

• adding a bilingual interface;

• adding cursor coordinate display to the window title;

• adding Ctrl+Z and Ctrl+Y keyboard shortcuts.

### 8. Main Challenges and Solutions.

• **(1) Challenge:** Correctly rendering a temporary shape while dragging.

**Solution:** The temporary shape is created inside `paint()` based on the current `beginPoint` and `endPoint`, but is not added to the array until the mouse button is released.

• **(2) Challenge:** Implementing Undo/Redo without losing data.

**Solution:** Using `Stack<DrawingState>` stacks, where each `DrawingState` contains a complete copy of the figure array created using `clone()`.

• **(3) Challenge:** Finding the nearest shape for the eraser.

**Solution:** The `distanceTo(p)` method in the `Figure` class calculates the distance from a point to a shape depending on its type (distance to a line segment, rectangle, ellipse, or polyline).

• **(4) Challenge:** Serializing brush strokes (lists of points) to a file.

**Solution:** Using a dedicated format: first the type, color, and line width are stored, followed by the number of points and their coordinates.

• **(5) Challenge:** Updating all interface text when switching languages.

**Solution:** The `updateLanguage()` method iterates through all menu components using `getMenuComponents()` and updates their text.

• **(6) Challenge:** Handling keyboard shortcuts in Swing.

**Solution:** Using `InputMap` and `ActionMap` through `getRootPane()`.

### 9. Current Project Limitations.

• Maximum of 1000 shapes (array size limitation).

• No shape filling support (outline only).

• No ability to move already drawn shapes.

• No shape selection or grouping.

• No export to raster formats (PNG, JPEG).

• The code remains monolithic (all classes are stored in a single file).

• Settings are not persisted between application launches.

### 10. Potential Improvements and Future Development.

• Refactoring the code (splitting classes into separate files and packages).

• Adding shape filling and fill style selection.

• Implementing shape selection, movement, and scaling.

• Adding PNG/JPEG export using `ImageIO`.

• Implementing shape copy/paste functionality.

• Adding layers and layer management.

• Integrating with the system clipboard.

• Adding a palette of recently used colors.

• Implementing a text tool.
