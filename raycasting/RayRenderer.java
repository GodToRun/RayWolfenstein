package raycasting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RayRenderer {
	public static int mapWidth = 12;
	public static int mapHeight = 12;
	int worldMap[][];
	RayEngine engine;

	public RayRenderer(RayEngine engine) {
		this.engine = engine;
	}
	public void readMap(String mapName) {
		int spr = 0;
		BufferedImage img;
		try {
			img = ImageIO.read(new File(mapName));
			worldMap = new int[img.getWidth()][img.getHeight()];
			mapWidth = img.getWidth();
			mapHeight = img.getHeight();
			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					int col = img.getRGB(x, y);
					int r = (col>>16)&0xFF;
					int g = (col>>8)&0xFF;
					int b = (col>>0)&0xFF;
					if (r == 0 && g == 0 && b == 0) {
						worldMap[x][y] = 4;
					}
					else if (r == 127 && g == 0 && b == 0) {
						worldMap[x][y] = 2;
					}
					else if (r == 127 && g == 127 && b == 0) {
						worldMap[x][y] = 1;
					}
					else if (r == 0 && g == 0 && b == 255) {
						worldMap[x][y] = 5;
					}
					else if (r == 255 && g == 0 && b == 0) {
						Sprite.sprite[spr] = new Sprite(x, y, 10, true);
						spr++;
					}
					else if (r == 0 && g == 255 && b == 255) {
						Sprite.sprite[spr] = new Sprite(x, y, 28, false);
						spr++;
					}
					else if (r == 0 && g == 255 && b == 0) {
						Sprite.sprite[spr] = new HealthBox(x, y, 27, false, engine);
						spr++;
					}
					else if (r == 255 && g == 255 && b == 0) {
						Sprite.sprite[spr] = new GunItem(x, y, 30, false, engine);
						spr++;
					}
					else {
						worldMap[x][y] = 0;
					}
				}
			}
			Sprite.spriteDistance = new double[spr];
			Sprite.spriteOrder = new int[spr];
			Sprite.numSprites = spr;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void render(Graphics g) {
		if (engine.win != null)
			engine.win.repaint();
		engine.updateEngine(g);
	      
	}
	public void renderVert(Graphics g, int mapX, int mapY, int side, int x, int drawStart, int drawEnd,
			int texX, double texPos, double step, int texNum) {
    //draw the pixels of the stripe as a vertical line
    for (int y = drawStart; y < drawEnd; y++) {
    	int texY = (int)texPos & (RayEngine.texHeight - 1);
        texPos += step;
        Color color = engine.texture[texNum][RayEngine.texHeight * texY + texX];
        //System.out.println("X: " + texNum + ", Y: " + RayEngine.texHeight * texY + texX);
        //Color color = Color.RED;
        if (color != null) {
	        if (side == 1) {color = color.darker();}
	        g.setColor(color);
        }
        g.drawLine(x, y, x, y);
    }
    
    g.setColor(Color.DARK_GRAY);
    g.drawLine(x, 0, x, drawStart);
    g.setColor(Color.GRAY);
    g.drawLine(x, drawEnd, x, RayWindow.windowHeight);
    g.setColor(Color.WHITE);
    g.drawString("HP: " + engine.hp, 50, 50);
    g.drawString("Ammo: " + engine.ammo, 50, 80);
  }
}
