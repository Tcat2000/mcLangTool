package org.tcathebluecreper.colorpicker;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ColorPicker extends JFrame implements ActionListener {
    public static final String name = "Color Picker";
    public static final String info = name + ": Simple Color Picker";
    JFrame f;
    JButton b;
    JTextArea ta;
    public ColorPicker(){
        f=new JFrame("Color Chooser Example.");
        b=new JButton("Pad Color");
        b.setBounds(200,250,100,30);
        ta=new JTextArea();
        ta.setBounds(10,10,300,200);
        b.addActionListener(this);
        f.add(b);f.add(ta);
        f.setLayout(null);
        f.setSize(400,400);
        f.setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        Color c=JColorChooser.showDialog(this,"Choose",Color.CYAN);
        ta.setBackground(c);
    }
    public static void main(String[] args) {
        new javafx.scene.control.ColorPicker();
    }
}
