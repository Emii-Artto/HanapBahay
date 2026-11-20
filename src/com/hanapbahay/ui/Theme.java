package com.hanapbahay.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * One place for the app's colors and fonts. Owner: UI member.
 *
 * STATIC FINAL constants (Modules 3 and 4): every screen uses Theme.ACCENT, Theme.TITLE, etc.
 * Change a color here and every screen follows, so the design stays consistent.
 */
public class Theme {

	// ---- Colors ----
	public static final Color SIDEBAR = new Color(17, 37, 34);          // dark green-black menu
	public static final Color SIDEBAR_TEXT = new Color(176, 196, 190);  // menu text
	public static final Color ACCENT = new Color(15, 110, 86);          // main teal color
	public static final Color ACCENT_LIGHT = new Color(159, 225, 203);  // light teal text
	public static final Color PAGE_BG = new Color(243, 245, 244);       // light gray page
	public static final Color BORDER = new Color(222, 226, 224);        // card and field borders
	public static final Color TEXT = new Color(33, 37, 41);             // dark text
	public static final Color TEXT_MUTED = new Color(108, 117, 125);    // gray text
	public static final Color SELECTED_ROW = new Color(225, 245, 238);  // selected table row
	public static final Color DANGER = new Color(192, 57, 43);          // delete button
	public static final Color AMBER = new Color(186, 117, 23);          // "reserved" card
	public static final Color BLUE = new Color(24, 95, 165);            // "sold" card
	public static final Color CORAL = new Color(216, 90, 48);           // "inquiries" card

	// ---- Fonts ----
	public static final Font BRAND = new Font("Segoe UI", Font.BOLD, 30);   // "HanapBahay" on login
	public static final Font HERO = new Font("Segoe UI", Font.BOLD, 26);    // "Sign in", "Create account"
	public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 22);
	public static final Font HEADING = new Font("Segoe UI", Font.BOLD, 15);
	public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
	public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
	public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
	public static final Font SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);
	public static final Font BIG_NUMBER = new Font("Segoe UI", Font.BOLD, 26);

	// Border for text fields: thin gray line with 8 pixels of space inside
	public static final Border FIELD_BORDER = new CompoundBorder(new LineBorder(BORDER), new EmptyBorder(0, 8, 0, 8));

	/** Gives every table in the app the same clean look. */
	public static void styleTable(JTable table, JScrollPane scrollPane) {
		table.setFont(BODY);
		table.setRowHeight(30);
		table.setShowVerticalLines(false);
		table.setGridColor(BORDER);
		table.setSelectionBackground(SELECTED_ROW);
		table.setSelectionForeground(TEXT);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.setDefaultEditor(Object.class, null); // cells can't be typed into
		table.getTableHeader().setFont(SMALL_BOLD);
		table.getTableHeader().setBackground(PAGE_BG);
		table.getTableHeader().setForeground(TEXT_MUTED);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setBorder(new LineBorder(BORDER));
		scrollPane.getViewport().setBackground(Color.WHITE);
	}
}
