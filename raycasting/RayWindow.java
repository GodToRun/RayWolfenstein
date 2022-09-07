package raycasting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RayWindow extends JFrame {
	RayEngine engine;
	public static final int windowWidth = 640;
	public static final int windowHeight = 480;
	public RayWindow(RayEngine engine) {
		this.engine = engine;
		setTitle("Ray Engine");
		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		RayPanel panel = new RayPanel();
        this.add(panel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
		setVisible(true);
		addKeyListener(new KeyAdapter() { //키 이벤트
			@Override
			public void keyPressed(KeyEvent e) { //키 눌렀을때
				engine.onKey(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				engine.onKeyUp(e);
			}
		});
	}
	class RayPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if (engine.renderer != null) {
    			engine.renderer.render(g);
    		}
        }
    }
}
