import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.sound.midi.SysexMessage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

@SuppressWarnings("serial")
public class BatailleUI extends JFrame {
	// attributs
	private ArrayList<Bateau> bateauJoueur;

	public static void main(String[] args) {
		new BatailleUI();
	}

	// contructeur
	public BatailleUI() {
		super("Bataille navale");
		bateauJoueur = new ArrayList<Bateau>();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ImageIcon icone = new ImageIcon("images/icone.jpg");
		setIconImage(icone.getImage());
		setSize(500, 500);

		JMenu menuJeu = new JMenu("Jeu");

		JMenuItem sousMenuJouer = new JMenuItem("jouer");
		sousMenuJouer.setActionCommand("J");
		sousMenuJouer.addActionListener(new menuEvent(this));

		JMenuItem sousMenuReJouer = new JMenuItem("Recommencer le Jeu");
		sousMenuReJouer.setActionCommand("RJ");
		sousMenuReJouer.addActionListener(new menuEvent(this));
		sousMenuReJouer.setEnabled(false);

		JMenuItem sousMenuQuitter = new JMenuItem("Quitter");
		sousMenuQuitter.setActionCommand("Q");
		sousMenuQuitter.addActionListener(new menuEvent(this));

		JMenuItem sousMenuReprendre = new JMenuItem("Reprendre");
		sousMenuReprendre.setActionCommand("R");
		sousMenuReprendre.addActionListener(new menuEvent(this));
		sousMenuReprendre.setEnabled(false);

		JMenuItem sousMenuPause = new JMenuItem("Pause");
		sousMenuPause.setActionCommand("P");
		sousMenuPause.addActionListener(new menuEvent(this));

		menuJeu.add(sousMenuJouer);
		menuJeu.add(sousMenuReJouer);
		menuJeu.add(sousMenuReprendre);
		menuJeu.add(sousMenuPause);
		menuJeu.add(sousMenuQuitter);

		JMenu menuOption = new JMenu("Option");

		JMenuItem sousMenuOpGraphique = new JMenuItem("Option Graphique");
		sousMenuOpGraphique.setActionCommand("OG");
		sousMenuOpGraphique.addActionListener(new menuEvent(this));

		JMenuItem sousMenuOpJeu = new JMenuItem("Option Jeu");
		sousMenuOpJeu.setActionCommand("OJ");
		sousMenuOpJeu.addActionListener(new menuEvent(this));

		menuOption.add(sousMenuOpGraphique);
		menuOption.add(sousMenuOpJeu);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuJeu);
		menuBar.add(menuOption);
		add(menuBar);
		setLayout(new FlowLayout(FlowLayout.LEFT));

		setVisible(true);

	}

	public void addBateau(Bateau bateau) {
		bateauJoueur.add(bateau);
	}

	public void setEnabledAll(Object object, boolean state) {
	    if (object instanceof Container) {
	        Container c = (Container)object;
	        Component[] components = c.getComponents();
	        for (Component component : components) {
	            setEnabledAll(component, state);
	            component.setEnabled(state);
	            component.setBackground(new Color(196,196, 245));
	        }
	    }
	    else {
	        if (object instanceof Component) {
	            Component component = (Component)object;
	            component.setEnabled(state);
	        }
	    }
	}
	private class menuEvent implements ActionListener {
		BatailleUI laBataille;

		menuEvent(BatailleUI laBataille) {

			this.laBataille = laBataille;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			String menuSelect = ((JMenuItem) e.getSource()).getActionCommand();
			switch (menuSelect) {
			case "J":
				String nom = JOptionPane.showInputDialog(null, "entrez votre pseudo", "", JOptionPane.QUESTION_MESSAGE);

				JLabel message = new JLabel("Bienvenue au jeu Bataille Navale " + (nom!=null?nom:""));
			
				JPanel panneauMsg = new JPanel();
				panneauMsg.add(message);
				laBataille.add(panneauMsg);
				JPanel panneauBateau = new JPanel();
				
				for (int i = 1; i < 5; i++) {
					Bateau bateauJoueur = new Bateau(i);
				
					laBataille.addBateau(bateauJoueur);
					panneauBateau.add(bateauJoueur);
					panneauBateau.addFocusListener(new FocusListener() {
						
						@Override
						public void focusLost(FocusEvent e) {
							System.out.println(1);
							
						}
						
						@Override
						public void focusGained(FocusEvent e) {
							System.out.println(2);
							
						}
					});
				}
				JButton bouttonPosition = new JButton("aléatoire");
				
				bouttonPosition.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						for(Bateau  b:bateauJoueur) {
							System.out.println(((JPanel)b).getWidth());
							setEnabledAll(((Object)b), false);
							
						}
						
					}
				});
				panneauBateau.add(bouttonPosition);
				laBataille.add(panneauBateau);
				JPanel panneauTerrain= new JPanel();
				panneauTerrain.setLayout(new GridLayout(10,10));
				for(int i=0 ;i<10;i++) {
					for(int j=0 ;j<10;j++) {
						Case boutton = new Case(i,j);
						
						boutton.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								System.out.println(((Case)e.getSource()).X+"   "+((Case)e.getSource()).Y);
								
							}
						});
						panneauTerrain.add(boutton);
					}
					laBataille.add(panneauTerrain);
						
				}
				SwingUtilities.updateComponentTreeUI(laBataille);
				break;

			case "Q":
				System.exit(0);
				break;
			default:
				break;
			}
		}
	}

	private class Bateau extends JPanel {
		private int nbCase;
		private int direction;
		private int posX;
		private int posY;

		public Bateau(int nbCase) {
			super();
			this.nbCase = nbCase;
			direction = (int) (Math.random() * 2);
			if (direction == 0) // direction horizontale
				setLayout(new GridLayout(this.nbCase, 1));
			else
				setLayout(new GridLayout(1, this.nbCase));
			for (int i = 0; i < this.nbCase; i++) {

				JButton partition = new JButton();
				partition.setBorderPainted(false);
				partition.setPreferredSize(new Dimension(34, 34));
				partition.setBackground(new Color(129, 129, 247));
				add(partition);
			}
		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN));
		}
		
	
		
		
		

	}
private class Case extends JButton{
	private int X;
	private int Y;

	public Case(int x, int y) {
		super();
		X = x;
		Y = y;
		setPreferredSize(new Dimension(34, 34));
	}
	
	
} 
}
