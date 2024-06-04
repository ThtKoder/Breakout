/*
 * File: MouseReporter.java
 * -----------------------------
 * Output the location of the mouse to a label on the
 * screen. Change the color of the label to red when
 * the mouse touches it.
 */
package edu.cis;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.*;
import acm.program.*;

import javax.swing.*;

public class MouseReporter extends GraphicsProgram {

	// A constant for the x value of the label

	private static final int INDENT = 300;
	
	// This variable is visible to the entire program
	// It is called an "instance" variable
	private GLabel label = new GLabel("");
	
	public void run() {	
		// this code already adds the label to the screen!
		// run it to see what it does.
		label.setFont("Courier-24");
		label.setColor(Color.BLUE);
		
		// this setLabel method takes in a "String" 
		// you can concatenate integers and commas as such:
		label.setLabel(0 + "," + 0);
		
		// add the label to the screen!
		add(label, INDENT, getHeight()/2);
	}

	public void mouseClicked(MouseEvent e){
		int mouseX = e.getX();
		int mouseY = e.getY();

		System.out.println("X:" + e.getX() + " Y:" + e.getY() + " Indent:" + INDENT + " Y:" + getHeight()/2);
		System.out.println(label.getColor() == Color.BLUE);
		if (mouseX == INDENT && mouseY == getHeight()/2 && label.getColor() == (Color.BLUE)) {
			label.setColor(Color.RED);
		}
		if (mouseX == INDENT && mouseY == getHeight()/2 && label.getColor() == (Color.RED)) {
			label.setColor(Color.BLUE);
		}
		System.out.println("mouse clicked");
	}

	public void mouseMoved(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		label.setLabel(x + "," + y);
		label.setLocation(e.getX(),e.getY());
	}

	public static void main(String[] args) {
		new MouseReporter().start();

	}

}
