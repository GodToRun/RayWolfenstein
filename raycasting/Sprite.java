package raycasting;

public class Sprite {
	public static int numSprites = 0;
	public double x;
	public double y;
	public int texture;
	public boolean died = false;
	public boolean isHuman = false;
	public double shotTick = 0;
	public boolean active = true;
	//1D Zbuffer
	static double[] ZBuffer = new double[RayWindow.windowWidth];

	//arrays used to sort the sprites
	static int[] spriteOrder;
	static double[] spriteDistance;

	//function used to sort the sprites
	public Sprite(double x, double y, int texture, boolean isHuman) {
		this.x = x;
		this.y = y;
		this.texture = texture;
		this.isHuman = isHuman;
	}
	public void onCollision() {
		
	}

	public static Sprite sprite[] = new Sprite[500];

}