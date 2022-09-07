package raycasting;

public class GunItem extends Sprite {
	RayEngine eng;
	public GunItem(double x, double y, int texture, boolean isHuman, RayEngine eng) {
		super(x, y, texture, isHuman);
		this.eng = eng;
	}
	@Override
	public void onCollision() {
		eng.gun = GunType.Minigun;
		active = false;
	}

}
