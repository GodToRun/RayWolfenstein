package raycasting;

public class HealthBox extends Sprite {

	RayEngine eng;
	public HealthBox(double x, double y, int texture, boolean isHuman, RayEngine eng) {
		super(x, y, texture, isHuman);
		this.eng = eng;
	}
	@Override
	public void onCollision() {
		eng.hp += 2;
		active = false;
	}

}
