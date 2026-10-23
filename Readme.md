
# Bullet Hell Game

A 2D Bullet Hell arcade game built in Java using Swing. The player navigates an arena, dodges procedural waves of incoming enemies, and shoots down hostile units while surviving as long as possible under dynamic difficulty scaling.

-------------------------------------------------------------------------------------------

## Requirements

* **Java Development Kit (JDK):** Version 17 or higher.
* **VS Code** with the **Extension Pack for Java**.

-------------------------------------------------------------------------------------------

## How to Build and Run

### Option 1: In VS Code (Easiest)
1. Open the project folder in VS Code.
2. Open **src/main/java/Programming/CBL/Main.java**.
3. Click the **Run** button located in the top-right corner, or press "F5".
   > VS Code will automatically compile all project files and launch the application.

-------------------------------------------------------------------------------------------

### Option 2: Using Terminal (Bash / Maven)
If you prefer running from the command line, open a terminal in the project root folder:

1. Compile the project:
   **bash mvn compile**
   

2. Run the game:
   **bash mvn exec:java -Dexec.mainClass="Programming.CBL.Main"**

-------------------------------------------------------------------------------------------

## Controls

* **Movement:** "W", "A", "S", "D" or "Arrow Keys"
* **Aiming:** Mouse cursor
* **Shooting:** Automatic rapid fire towards cursor
* **Menu / Return:** "ESC"
* **Restart:** "R" / "Enter" (when eliminated) or click **Try Again**