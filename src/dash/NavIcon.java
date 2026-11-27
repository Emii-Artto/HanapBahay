package dash;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import javax.swing.Icon;

/**
 * Small line icons drawn with code (no image files), used in the sidebar, buttons and cards.
 * INTERFACE: NavIcon implements Swing's Icon, so any JButton or JLabel can show it with setIcon().
 * Every icon is drawn on an 18 x 18 grid and then scaled to the size asked for.
 * If no color is given, the icon uses the text color of the button or label it is on.
 */
public class NavIcon implements Icon {

	public static final String AGENT = "agent";
	public static final String PROPERTIES = "properties";
	public static final String CLIENTS = "clients";
	public static final String VIEWING = "viewing";
	public static final String DEALS = "deals";
	public static final String REPORTS = "reports";
	public static final String OVERVIEW = "overview";
	public static final String USERS = "users";
	public static final String KEY = "key";
	public static final String LOGOUT = "logout";
	public static final String SEARCH = "search";
	public static final String PLUS = "plus";
	public static final String PHOTO = "photo";
	public static final String EDIT = "edit";
	public static final String DELETE = "delete";
	public static final String BROWSE = "browse";
	public static final String CHEVRON = "chevron";

	private String type;
	private int size;
	private Color color;

	public NavIcon(String type) {
		this(type, 18, null);
	}

	public NavIcon(String type, int size, Color color) {
		this.type = type;
		this.size = size;
		this.color = color;
	}

	@Override
	public int getIconWidth() {
		return size;
	}

	@Override
	public int getIconHeight() {
		return size;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(x, y);
		g2.scale(size / 18.0, size / 18.0);
		g2.setColor(color != null ? color : c.getForeground());
		g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		if (PROPERTIES.equals(type)) {
			g2.drawPolyline(new int[] { 2, 9, 16 }, new int[] { 9, 2, 9 }, 3);
			g2.drawPolyline(new int[] { 4, 4, 14, 14 }, new int[] { 8, 16, 16, 8 }, 4);
			g2.drawRect(7, 11, 4, 5);
		} else if (AGENT.equals(type)) {
			g2.drawOval(6, 2, 6, 6);
			g2.drawArc(3, 10, 12, 12, 0, 180);
		} else if (CLIENTS.equals(type) || USERS.equals(type)) {
			g2.drawOval(3, 3, 5, 5);
			g2.drawArc(1, 10, 9, 10, 0, 180);
			g2.drawOval(10, 4, 5, 5);
			g2.drawArc(10, 11, 7, 8, 0, 180);
		} else if (VIEWING.equals(type)) {
			g2.drawRoundRect(2, 3, 14, 13, 3, 3);
			g2.drawLine(2, 7, 16, 7);
			g2.drawLine(6, 1, 6, 4);
			g2.drawLine(12, 1, 12, 4);
			g2.fillRect(5, 10, 2, 2);
			g2.fillRect(8, 10, 2, 2);
			g2.fillRect(11, 10, 2, 2);
		} else if (DEALS.equals(type)) {
			Path2D tag = new Path2D.Double();
			tag.moveTo(2, 9);
			tag.lineTo(9, 2);
			tag.lineTo(16, 2);
			tag.lineTo(16, 9);
			tag.lineTo(9, 16);
			tag.closePath();
			g2.draw(tag);
			g2.fill(new Ellipse2D.Double(11.5, 4.5, 2.2, 2.2));
		} else if (REPORTS.equals(type)) {
			g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.drawLine(4, 15, 4, 10);
			g2.drawLine(9, 15, 9, 4);
			g2.drawLine(14, 15, 14, 7);
		} else if (OVERVIEW.equals(type)) {
			g2.drawRoundRect(2, 2, 6, 6, 2, 2);
			g2.drawRoundRect(10, 2, 6, 6, 2, 2);
			g2.drawRoundRect(2, 10, 6, 6, 2, 2);
			g2.drawRoundRect(10, 10, 6, 6, 2, 2);
		} else if (KEY.equals(type)) {
			g2.drawOval(2, 6, 7, 7);
			g2.drawLine(9, 9, 16, 9);
			g2.drawLine(13, 9, 13, 12);
			g2.drawLine(16, 9, 16, 12);
		} else if (LOGOUT.equals(type)) {
			g2.drawPolyline(new int[] { 9, 3, 3, 9 }, new int[] { 2, 2, 16, 16 }, 4);
			g2.drawLine(7, 9, 16, 9);
			g2.drawPolyline(new int[] { 13, 16, 13 }, new int[] { 6, 9, 12 }, 3);
		} else if (SEARCH.equals(type)) {
			g2.drawOval(2, 2, 10, 10);
			g2.drawLine(11, 11, 16, 16);
		} else if (PLUS.equals(type)) {
			g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.drawLine(9, 3, 9, 15);
			g2.drawLine(3, 9, 15, 9);
		} else if (PHOTO.equals(type)) {
			g2.drawRoundRect(2, 3, 14, 12, 3, 3);
			g2.drawPolyline(new int[] { 2, 7, 11, 13, 16 }, new int[] { 13, 8, 12, 10, 13 }, 5);
			g2.fill(new Ellipse2D.Double(11, 5, 2.6, 2.6));
		} else if (EDIT.equals(type)) {
			Path2D pencil = new Path2D.Double();
			pencil.moveTo(3, 15);
			pencil.lineTo(4, 11);
			pencil.lineTo(12, 3);
			pencil.lineTo(15, 6);
			pencil.lineTo(7, 14);
			pencil.closePath();
			g2.draw(pencil);
		} else if (DELETE.equals(type)) {
			g2.drawLine(3, 5, 15, 5);
			g2.drawPolyline(new int[] { 7, 7, 11, 11 }, new int[] { 5, 3, 3, 5 }, 4);
			g2.drawPolyline(new int[] { 5, 6, 12, 13 }, new int[] { 5, 16, 16, 5 }, 4);
		} else if (CHEVRON.equals(type)) {
			g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.drawPolyline(new int[] { 4, 9, 14 }, new int[] { 7, 12, 7 }, 3);
		} else if (BROWSE.equals(type)) {
			g2.draw(new Ellipse2D.Double(1, 5, 16, 8));
			g2.fill(new Ellipse2D.Double(7, 7, 4, 4));
		}
		g2.dispose();
	}
}
