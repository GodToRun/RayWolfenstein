package raycasting;

public class AmmoBox extends Sprite {

	RayEngine eng;
	public AmmoBox(double x, double y, int texture, boolean isHuman, RayEngine eng) {
		super(x, y, texture, isHuman);
		this.eng = eng;
	}
	@Override
	public void onCollision() {
		eng.ammo += 2;
		active = false;
	}

}
