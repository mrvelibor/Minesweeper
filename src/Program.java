

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import mine.*;

public class Program extends JFrame {
	
	private Tabla tabla;
	private int tezina, velicina;
	
	public Program() {
		super("Minesweeper");
		setBounds(200, 200, 630, 690);
		setMinimumSize(new Dimension(630, 690));
		setLayout(new FlowLayout());
		setResizable(false);
		
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		ButtonGroup group;
		
		//MENI
		menuBar = new JMenuBar();

		menu = new JMenu("Igra");
		menu.getAccessibleContext().setAccessibleDescription("Zapocni novu igru.");
		menuBar.add(menu);

		menuItem = new JMenuItem("Pocni");
		menuItem.addActionListener(new MeniNova());
		menu.add(menuItem);

		
		menu.addSeparator();
		submenu = new JMenu("Broj mina");
		menu.add(submenu);
		group = new ButtonGroup();

		rbMenuItem = new JRadioButtonMenuItem("Mali");
		rbMenuItem.addActionListener(new MeniTezina(12));
		group.add(rbMenuItem);
		submenu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Srednji");
		rbMenuItem.addActionListener(new MeniTezina(16));
		rbMenuItem.setSelected(true); tezina = 16;
		group.add(rbMenuItem);
		submenu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Veliki");
		rbMenuItem.addActionListener(new MeniTezina(20));
		group.add(rbMenuItem);
		submenu.add(rbMenuItem);


		submenu = new JMenu("Velicina polja");
		menu.add(submenu);
		group = new ButtonGroup();
		
		rbMenuItem = new JRadioButtonMenuItem("8x8");
		rbMenuItem.addActionListener(new MeniVelicina(8));
		group.add(rbMenuItem);
		submenu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("12x12");
		rbMenuItem.addActionListener(new MeniVelicina(12));
		rbMenuItem.setSelected(true); velicina = 12;
		group.add(rbMenuItem);
		submenu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("16x16");
		rbMenuItem.addActionListener(new MeniVelicina(16));
		group.add(rbMenuItem);
		submenu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("20x20");
		rbMenuItem.addActionListener(new MeniVelicina(20));
		group.add(rbMenuItem);
		submenu.add(rbMenuItem);
		
		
		menu = new JMenu("Pomoc");
		menuBar.add(menu);

		menuItem = new JMenuItem("Kako igrati");
		menuItem.addActionListener(new MeniPomoc('p'));
		menu.add(menuItem);

		menu.addSeparator();
		
		menuItem = new JMenuItem("O programu");
		menuItem.addActionListener(new MeniPomoc('o'));
		menu.add(menuItem);

		setJMenuBar(menuBar);
		//KRAJ MENIJA
		
		tabla = new Tabla(tezina, velicina);
		
		JLabel lab = new JLabel("Preostalo mina:");
		add(lab);
		add(tabla.preostaloMina());
		add(tabla);
		
		addWindowListener(new PrDogadjaji());
		setVisible(true);
	}
	
	private class MeniNova implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent d)
		{ nova(); }
	}
	
	private class MeniTezina implements ActionListener {
		
		private int tez;
		
		public MeniTezina(int t)
		{ tez = t; }
		
		@Override
		public void actionPerformed(ActionEvent e)
		{ tezina = tez; }
		
	}
	
	private class MeniVelicina implements ActionListener {
		
		private int vel;
		
		public MeniVelicina(int v)
		{ vel = v; }
		
		@Override
		public void actionPerformed(ActionEvent e)
		{ velicina = vel; }
		
	}
	
	private class MeniPomoc implements ActionListener {
		private char c;
		
		public MeniPomoc(char cc)
		{ super(); c = cc; }
		
		public void actionPerformed(ActionEvent d) {
			switch(c)
			{
			case 'p': 	JOptionPane.showMessageDialog(
							null,
							"Da zapocnete igru, u meniju 'Igra' odaberite broj polja i mina, a zatim kliknite na 'Pocni'.\n\n" +
							"Cilj igre je da otvorite sva polja na kojima nema mine, a da pri tom ne pogodite nijednu minu.\n" +
							"Polje otvarajte pritiskom levog tastera na misu, a polja sa minom obelezavajte desnim tasterom.\n\n" +
							"U svakom polju koje otvorite pisace broj mina koji ga okruzuje, a ako oko polja nema mina,\notvorice se sva okolna polja.",
							"Kako igrati?",
							JOptionPane.QUESTION_MESSAGE
						);
						break;
			case 'o': 	JOptionPane.showMessageDialog(
							null,
							"Minesweeper\nby Velibor Bacujkov\n2013.",
							"O programu",
							JOptionPane.INFORMATION_MESSAGE
						);
			}
		}
	}

	private class PrDogadjaji extends WindowAdapter {
		public void windowClosing(WindowEvent d)
		{ dispose(); }
	}
	
	public void nova() {
		remove(tabla.preostaloMina());
		remove(tabla);
		
		tabla = new Tabla(tezina, velicina);
		add(tabla.preostaloMina());
		add(tabla);

		Rectangle b = getBounds();
		b.height++; setBounds(b);
		b.height--; setBounds(b);
	}
	
	public static void main(String[] args)
	{ new Program(); }
	
}