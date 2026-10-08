/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Programming.CBL;

import javax.swing.JFrame;


/**
 *
 * @author Gijs
 */
public class Main{

    public static void main(String[] args) {
        System.out.println("Hello World!");
        JFrame frame = new JFrame();
        setUp(frame);
    
    }

    private static void setUp(JFrame frame) {
        frame = new JFrame();
        // 400 width and 500 height
        frame.setSize(500, 600);
        // using no layout managers
        frame.setLayout(null);
        // making the frame visible
        frame.setVisible(true);
    }
}


