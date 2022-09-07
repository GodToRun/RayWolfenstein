package raycasting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
enum GunType {
	Shotgun,Minigun
}
public class RayEngine {
	RayRenderer renderer;
	RayWindow win;
	AnimateThread shotGunThread;
	AnimateThread miniGunThread;
	public int hp = 6;
	double posX = 1.5, posY = 1.5;  //x and y start position
	double dirX = -1, dirY = 0; //initial direction vector
	double planeX = 0, planeY = 0.66; //the 2d raycaster version of camera plane
	public GunType gun = GunType.Shotgun;
	public int ammo = 20;
	double time = 0; //time of current frame
	double oldTime = 0; //time of previous frame
	   //speed modifiers
    int[] key = new int[500];
    private long curTime = System.currentTimeMillis();
    private long lastTime = curTime;
    private long totalTime;
    private double frames;
    private double fps; //could be int or long for integer values
    public Color[][] texture = new Color[60][texWidth * texHeight];
    public static final int texWidth = 64;
    public static final int texHeight = 64;
    public static final double vMove = 0.0;
    public static final double uDiv = 1;
    public static final double vDiv = 1;
    AnimateThread currentGunThread;
    double acc = 0.0;
	public RayEngine(String mapName) {
		shotGunThread = new AnimateThread(16, 5, 60, 255);
		miniGunThread = new AnimateThread(22, 5, 60, 4);
		shotGunThread.isEnded = true;
		miniGunThread.isEnded = true;
		this.renderer = new RayRenderer(this);
		renderer.readMap(mapName);
		this.win = new RayWindow(this);
		
	}
	public void onKey(KeyEvent e) {
		key[e.getKeyCode()] = 1;
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			if (gun == GunType.Minigun) {
	    		miniGunThread = new AnimateThread(22, 5, 60, 4);
				miniGunThread.start();
			}
			else if (gun == GunType.Shotgun) {
				ammo--;
			}
    	}
	}
	public void loadTex(int texNum, String pictureName) {
		try {
			BufferedImage img = ImageIO.read(new File(pictureName));
			for (int x = 0; x < texWidth; x++) {
				for (int y = 0; y < texHeight; y++) {
					int col = img.getRGB(x, y);
					Color color = new Color((col>>16)&0xFF, (col>>8)&0xFF, (col)&0xFF);
					texture[texNum][texHeight * y + x] = color;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onKeyUp(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL && gun == GunType.Minigun) {
			miniGunThread.loopPoint = 255;
		}
		if (key[e.getKeyCode()] == 1)
			key[e.getKeyCode()] = 0;
	}
	public double getDeltaTime() {
		return 1 / fps;
	}
	public void updateEngine(Graphics g) {
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		
		totalTime += curTime - lastTime;
		if (totalTime > 100) {
			totalTime -= 100;
			fps = frames * 10;
			frames = 0;
		}
		frames++;
		if (gun == GunType.Shotgun) currentGunThread = shotGunThread;
		if (gun == GunType.Minigun) currentGunThread = miniGunThread;
		double moveSpeed = getDeltaTime() * 4.5;
		double rotSpeed = getDeltaTime() * 2.0;
		if (key[KeyEvent.VK_UP] == 1 && acc < moveSpeed) {
			acc += moveSpeed / 20;
		}
		else if (acc > 0) {
			acc -= moveSpeed / 20;
			if (acc < 0) {
				acc = 0;
			}
		}
		if (key[KeyEvent.VK_DOWN] == 1 && -acc < moveSpeed) {
			acc -= moveSpeed / 20;
		}
		else if (acc < 0) {
			acc += moveSpeed / 20;
			if (acc > 0) {
				acc = 0;
			}
		}
	    //rotate to the right
	    if (key[KeyEvent.VK_RIGHT] == 1)
	    {
	      //both camera direction and camera plane must be rotated
	      double oldDirX = dirX;
	      dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
	      dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
	      double oldPlaneX = planeX;
	      planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
	      planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
	    }
	    //rotate to the left
	    if (key[KeyEvent.VK_LEFT] == 1)
	    {
	      //both camera direction and camera plane must be rotated
	      double oldDirX = dirX;
	      dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
	      dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
	      double oldPlaneX = planeX;
	      planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
	      planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
	    }
	    if(renderer.worldMap[(int)(posX + dirX * acc)][(int)(posY)] == 0) posX += acc * dirX;
		if(renderer.worldMap[(int)(posX)][(int)(posY + dirY * acc)] == 0) posY += acc * dirY;
		
		int w = RayWindow.windowWidth;
		int h = RayWindow.windowHeight;
		for(int x = 0; x < w; x++)
	    {
	      //calculate ray position and direction
	      double cameraX = 2 * x / (double)w - 1; //x-coordinate in camera space
	      double rayDirX = dirX + planeX * cameraX;
	      double rayDirY = dirY + planeY * cameraX;

	      //which box of the map we're in
	      int mapX = (int)(posX);
	      int mapY = (int)(posY);

	      //length of ray from current position to next x or y-side
	      double sideDistX;
	      double sideDistY;

	       //length of ray from one x or y-side to next x or y-side
	      double deltaDistX = (rayDirX == 0) ? 1e30 : Math.abs(1f / rayDirX);
	      double deltaDistY = (rayDirY == 0) ? 1e30 : Math.abs(1f / rayDirY);
	      double perpWallDist;

	      //what direction to step in x or y-direction (either +1 or -1)
	      int stepX;
	      int stepY;

	      int hit = 0; //was there a wall hit?
	      int side = 0; //was a NS or a EW wall hit?

	      //calculate step and initial sideDist
	      if (rayDirX < 0)
	      {
	        stepX = -1;
	        sideDistX = (posX - mapX) * deltaDistX;
	      }
	      else
	      {
	        stepX = 1;
	        sideDistX = (mapX + 1.0 - posX) * deltaDistX;
	      }
	      if (rayDirY < 0)
	      {
	        stepY = -1;
	        sideDistY = (posY - mapY) * deltaDistY;
	      }
	      else
	      {
	        stepY = 1;
	        sideDistY = (mapY + 1.0 - posY) * deltaDistY;
	      }

	      //perform DDA
	      while (hit == 0)
	      {
	        //jump to next map square, either in x-direction, or in y-direction
	        if (sideDistX < sideDistY)
	        {
	          sideDistX += deltaDistX;
	          mapX += stepX;
	          side = 0;
	        }
	        else
	        {
	          sideDistY += deltaDistY;
	          mapY += stepY;
	          side = 1;
	        }
	        //Check if ray has hit a wall
	        if (renderer.worldMap[mapX][mapY] > 0) hit = 1;
	      } 
	      if(side == 0) perpWallDist = (sideDistX - deltaDistX);
	      else          perpWallDist = (sideDistY - deltaDistY);

	      //Calculate height of line to draw on screen
	      int lineHeight = (int)(h / perpWallDist);

	      //calculate lowest and highest pixel to fill in current stripe
	      int drawStart = -lineHeight / 2 + h / 2;
	      if(drawStart < 0)drawStart = 0;
	      int drawEnd = lineHeight / 2 + h / 2;
	      if(drawEnd >= h)drawEnd = h - 1;

	      //texturing calculations
	      int texNum = renderer.worldMap[mapX][mapY] - 1; //1 subtracted from it so that texture 0 can be used!

	      //calculate value of wallX
	      double wallX; //where exactly the wall was hit
	      if (side == 0) wallX = posY + perpWallDist * rayDirY;
	      else           wallX = posX + perpWallDist * rayDirX;
	      wallX -= Math.floor((wallX));

	      //x coordinate on the texture
	      int texX = (int)(wallX * (double)(texWidth));
	      if(side == 0 && rayDirX > 0) texX = texWidth - texX - 1;
	      if(side == 1 && rayDirY < 0) texX = texWidth - texX - 1;
	      double step = 1.0 * texHeight / lineHeight;
	      // Starting texture coordinate
	      double texPos = (drawStart - h / 2 + lineHeight / 2) * step;
	      renderer.renderVert(g, mapX, mapY, side, x, drawStart, drawEnd, texX, texPos, step, texNum);
	      Sprite.ZBuffer[x] = perpWallDist; //perpendicular distance is used
	      
	    }
		for(int i = 0; i < Sprite.numSprites; i++)
	      {
	        Sprite.spriteOrder[i] = i;
	        Sprite.spriteDistance[i] = ((posX - Sprite.sprite[i].x) * (posX - Sprite.sprite[i].x) + (posY - Sprite.sprite[i].y) * (posY - Sprite.sprite[i].y)); //sqrt not taken, unneeded
	      }
	      for(int i = 0; i < Sprite.numSprites; i++)
	      {
	    	  if (!Sprite.sprite[Sprite.spriteOrder[i]].active)
	    		  continue;
	    	  if (Sprite.spriteDistance[i] < 2) {
	    		  Sprite.sprite[Sprite.spriteOrder[i]].onCollision();
	    	  }
	        //translate sprite position to relative to camera
	        double spriteX = Sprite.sprite[Sprite.spriteOrder[i]].x - posX;
	        double spriteY = Sprite.sprite[Sprite.spriteOrder[i]].y - posY;
	      //transform sprite with the inverse camera matrix
	        // [ planeX   dirX ] -1                                       [ dirY      -dirX ]
	        // [               ]       =  1/(planeX*dirY-dirX*planeY) *   [                 ]
	        // [ planeY   dirY ]                                          [ -planeY  planeX ]

	        double invDet = 1.0 / (planeX * dirY - dirX * planeY); //required for correct matrix multiplication

	        double transformX = invDet * (dirY * spriteX - dirX * spriteY);
	        double transformY = invDet * (-planeY * spriteX + planeX * spriteY); //this is actually the depth inside the screen, that what Z is in 3D, the distance of sprite to player, matching sqrt(spriteDistance[i])

	        int spriteScreenX = (int)((w / 2) * (1 + transformX / transformY));

	        //parameters for scaling and moving the sprites
	        //int vMoveScreen = (int)(vMove / transformY);

	        //calculate height of the sprite on screen
	        int spriteHeight = (int)(Math.abs(((int)(h / (transformY))))); //using "transformY" instead of the real distance prevents fisheye
	        //calculate lowest and highest pixel to fill in current stripe
	        int drawStartY = -spriteHeight / 2 + h / 2;
	        if(drawStartY < 0) drawStartY = 0;
	        int drawEndY = spriteHeight / 2 + h / 2;
	        if(drawEndY >= h) drawEndY = h - 1;

	        //calculate width of the sprite
	        int spriteWidth = (int) (Math.abs(((int) (h / (transformY))))); // same as height of sprite, given that it's square
	        int drawStartX = -spriteWidth / 2 + spriteScreenX;
	        if(drawStartX < 0) drawStartX = 0;
	        int drawEndX = spriteWidth / 2 + spriteScreenX;
	        if(drawEndX > w) drawEndX = w;
	        boolean isSpriteShowing =  drawStartX > 0 && drawEndX < 640 && transformY > 0 &&
        			transformY < Sprite.ZBuffer[drawStartX+(drawEndX - drawStartX)/2];
	        //loop through every vertical stripe of the sprite on screen
	        Sprite spr = Sprite.sprite[Sprite.spriteOrder[i]];
	        if (isSpriteShowing && !spr.died && spr.isHuman) {
	        	spr.shotTick += getDeltaTime() * 110;
	        	if (spr.shotTick > 100) {
	        		spr.shotTick = 0;
	        		hp--;
	        	}
	        }
	        if (key[KeyEvent.VK_CONTROL] == 1) {
	        	if (gun == GunType.Shotgun) { if (shotGunThread.isEnded) { shotGunThread = new AnimateThread(16, 5, 60, 255); shotGunThread.start(); } }
	        	if (!spr.died && spr.isHuman &&
	        			drawStartX > 150 && drawEndX < 450 && isSpriteShowing) {
	        		DeathThread th = new DeathThread(spr);
	        		th.start();
	        		spr.died = true;
	        		Sprite.sprite[Sprite.numSprites] = new AmmoBox(spr.x, spr.y, 29, false, this);
	        		Sprite.numSprites++;
	        		Sprite.spriteOrder = new int[Sprite.numSprites];
	        		Sprite.spriteDistance = new double[Sprite.numSprites];
	        		if (gun == GunType.Shotgun)
	        			key[KeyEvent.VK_CONTROL] = 0;
	        	}
	        }
	        for(int stripe = drawStartX; stripe < drawEndX; stripe++)
	        {
	          int spriteTexX = (int)(256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * texWidth / spriteWidth) / 256;
	          //the conditions in the if are:
	          //1) it's in front of camera plane so you don't see things behind you
	          //2) ZBuffer, with perpendicular distance
	          if(transformY > 0 && transformY < Sprite.ZBuffer[stripe])
	          {
	        	  
	            for(int y = drawStartY; y < drawEndY; y++) //for every pixel of the current stripe
	            {
	              int d = (y) * 256 - h * 128 + spriteHeight * 128; //256 and 128 factors to avoid floats
	              int spriteTexY = ((d * texHeight) / spriteHeight) / 256;
	              Color color = texture[Sprite.sprite[Sprite.spriteOrder[i]].texture][texWidth * spriteTexY + spriteTexX]; //get current color from the texture
	              if (color != null && (color.getRed() != 0x98 || color.getGreen() != 0 || color.getBlue() != 0x88)) {
		              g.setColor(color);
		              g.drawLine(stripe, y, stripe, y); //paint pixel if it isn't black, black is the invisible color
	              }
	            }
	          }
	        }
	      }
	      for (int x = 0; x < 64; x++) {
	    	  for (int y = 0; y < 64; y++) {
	    		 Color color = texture[currentGunThread.curTex][64 * y + x];
	    		 if (color.getRed() != 0x98 || color.getGreen() != 0 || color.getBlue() != 0x88) {
		    		 g.setColor(color);
		    		 int xoff, yoff = 58;
		    		 xoff = RayWindow.windowWidth / 2 - (64*3);
		    		 g.fillRect(xoff+x*6, yoff+y*6, 6, 6);
	    		 }
	    	  }
	      }
	      //sortSprites(spriteOrder, spriteDistance, numSprites);
	}
}
