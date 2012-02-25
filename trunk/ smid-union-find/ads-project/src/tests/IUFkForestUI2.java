package tests;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class IUFkForestUI2 {

	static class Frame extends JFrame {
		private static final long serialVersionUID = 8648061213040194821L;

		Frame() {
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			setMaximizedBounds(env.getMaximumWindowBounds());
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		}

		public static void main(String[] args) {
			JFrame.setDefaultLookAndFeelDecorated(true);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.WEST;
			c.weightx = 0;
			c.gridx = 0;
			c.gridy = 0;

			
			Frame frame = new Frame();
			
			JPanel panel = new JPanel(new GridLayout(2,1));
			
			panel.add(new IUFkForestPanel(1000,300), BorderLayout.WEST);
			panel.add(new IUFkForestPanel(1000,300), BorderLayout.WEST);

			frame.add(panel);
			frame.setVisible(true);
		}

	}

}
