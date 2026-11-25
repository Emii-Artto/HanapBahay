package com.hanapbahay.service;

import java.util.ArrayList;

/**
 * One finished report: a title, column names, rows of text, and a one-line summary.
 * The Reports panel shows it in a table, prints it, or exports it to CSV.
 */
public class Report {

	private String title;
	private String[] columns;
	private ArrayList<String[]> rows = new ArrayList<String[]>();
	private String summary = "";

	public Report(String title, String[] columns) {
		this.title = title;
		this.columns = columns;
	}

	public void addRow(String[] row) {
		rows.add(row);
	}

	public String getTitle() {
		return title;
	}

	public String[] getColumns() {
		return columns;
	}

	public ArrayList<String[]> getRows() {
		return rows;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
