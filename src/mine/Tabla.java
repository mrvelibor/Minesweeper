package mine;

import greske.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Tabla extends JPanel {
	
	private Polje[][] polje;
	private int brMina, oznacena, otvorena, tezina, velicina;

	private Font font;
	private JTextField txt;
	
	private class Polje extends JButton {
		
		private boolean mina, otvoreno, oznaceno;
		private int x, y, okolo;
		
		protected Polje(int i, int j) {
			super();
			setBackground(Color.gray);
			setMargin(new Insets(0,0,0,0));
			setFont(font);
			
			otvoreno = oznaceno = false;
			x = i; y = j;
			mina = false;
			okolo = 0;
		}
		
		protected boolean postaviMinu() {
			if(mina) return false;
			return mina = true;
		}
		
		protected Polje[] dohvOkolne() {
			int n = 8;
			if(x == 0 || x == velicina-1) n -= 3;
			if(y == 0 || y == velicina-1) n -= 3;
			if(n == 2) n++;
			
			Polje[] okolne = new Polje[n];
			int[][] moguci = { {1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1} };
			n = 0;
			
			for(int[] i : moguci) {
				try {
					okolne[n++] = dohvPolje(x+i[0], y+i[1]);
				} catch (Greska g) { n--; }
			}
			
			return okolne;
		}
		
		protected void nadjiMine() {
			Polje[] okolne = dohvOkolne();
			for(int i = 0; i < okolne.length; i++)
				if(okolne[i].mina) okolo++; 
		}
		
		private void otv() throws Kraj {
			otvoreno = true;
			if(mina) {
				setText("X");
				throw new Kraj(false);
			}
			else {
				if(okolo == 0) for(Polje i : dohvOkolne()) i.otvori();
				postaviTekst();
			}
			if(++otvorena + brMina == velicina*velicina) throw new Kraj(true);
		}
		
		protected void postaviTekst() {
			setBackground(Color.lightGray);
			switch(okolo) {
			case 1: 	setForeground(Color.cyan); break;
			case 2: 	setForeground(Color.green); break;
			case 3: 	setForeground(Color.yellow); break;
			case 4: 	setForeground(Color.orange); break;
			case 5: 	setForeground(Color.red); break;
			case 6:		setForeground(Color.pink); break;
			case 7: 	setForeground(Color.magenta); break;
			case 8: 	setForeground(Color.black); break;
			default: 	setText(""); return;
			}
			setText("" + okolo);
		}
		
		protected void otvori() throws Kraj
		{ if(!otvoreno && !oznaceno) otv(); }
		
		protected void oznaci() {
			if(!otvoreno) setText((oznaceno ? "" : "?"));
			oznacena += (oznaceno ? -1 : 1);
			oznaceno = !oznaceno;
		}
		
	}
	
	public Tabla(int tez, int vel) {
		super();
		setLayout(new GridLayout(vel, vel));
		Dimension d = new Dimension(600, 600);
		setBounds(0, 0, 600, 600);
		setMinimumSize(d);
		setPreferredSize(d);
		
		tezina = tez; velicina = vel;
		brMina = otvorena = 0;
		font = new Font("Arial", Font.BOLD, 35-velicina);
		
		polje = new Polje[velicina][velicina];
		for(int i = 0; i < velicina; i++)
			for(int j = 0; j < velicina; j++) {
				add(polje[i][j] = new Polje(i, j));
				polje[i][j].addMouseListener(new DugmeAkcija(i, j));
		}
		
		postaviMine();
		for(int i = 0; i < velicina; i++)
			for(int j = 0; j < velicina; j++) {
				if(polje[i][j].mina) brMina++;
				else polje[i][j].nadjiMine();
		}

		txt = new JTextField("" + brMina);
		txt.setEditable(false);
	}
	
	private class DugmeAkcija implements MouseListener {
		
		private int x, y;
		private boolean mis;
		
		DugmeAkcija(int i, int j)
		{ super(); x = i; y = j; mis = false; }
		
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e)
		{ if(polje[x][y].isEnabled()) mis = true; }
		@Override
		public void mouseExited(MouseEvent e) 
		{ mis = false; }
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(mis) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					try { polje[x][y].otvori();
					} catch (Kraj g) { kraj(g.pobeda());
					}
				}
				else if(e.getButton() == MouseEvent.BUTTON3)
					oznaci(x, y);
			}
		}
	}
	
	private Polje dohvPolje(int i, int j) throws Greska {
		if(i < 0 || i >= velicina || j < 0 || j >= velicina) throw new Greska();
		return polje[i][j];
	}
	
	private void oznaci(int i, int j) {
		polje[i][j].oznaci();
		txt.setText("" + (brMina-oznacena));
	}
	
	private void postaviMine() {
		Random rand = new Random();
		for(int i = 0; i < tezina*velicina*velicina/100; i++)
			while(!polje[rand.nextInt(velicina)][rand.nextInt(velicina)].postaviMinu());
	}

	private void kraj(boolean pob) {
		for(int i = 0; i < velicina; i++)
			for(int j = 0; j < velicina; j++) {
				polje[i][j].setEnabled(false);
				if(polje[i][j].mina) polje[i][j].setBackground((pob ? Color.cyan : Color.red));
				else {
					polje[i][j].postaviTekst();
					if(!pob) polje[i][j].setBackground(Color.darkGray);
				}
		}
	}
	
	public JTextField preostaloMina()
	{ return txt; }
	
}
