package game;

import raycasting.*;

public class Game {
	RayWindow win;
	RayEngine engine;
	RayRenderer renderer;
	public Game() {
		engine = new RayEngine("res/map.png");
		engine.loadTex(0, "res/redbrick_flag.png");
		engine.loadTex(1, "res/redbrick.png");
		engine.loadTex(2, "res/purplebrick.png");
		engine.loadTex(3, "res/greybrick.png");
		engine.loadTex(4, "res/bluestone.png");
		engine.loadTex(5, "res/mossy.png");
		engine.loadTex(6, "res/wood.png");
		engine.loadTex(7, "res/colorstone.png");
		engine.loadTex(9, "res/column.png");
		engine.loadTex(10, "res/guard1.png");
		engine.loadTex(11, "res/guardsd.png");
		engine.loadTex(12, "res/guard2.png");
		engine.loadTex(13, "res/guard3.png");
		engine.loadTex(14, "res/guard4.png");
		engine.loadTex(15, "res/guard_death.png");
		engine.loadTex(16, "res/shotgun.png");
		engine.loadTex(17, "res/shotgun2.png");
		engine.loadTex(18, "res/shotgun3.png");
		engine.loadTex(19, "res/shotgun4.png");
		engine.loadTex(20, "res/shotgun5.png");
		engine.loadTex(21, "res/light.png");
		engine.loadTex(22, "res/minigun1.png");
		engine.loadTex(23, "res/minigun2.png");
		engine.loadTex(24, "res/minigun3.png");
		engine.loadTex(25, "res/minigun4.png");
		engine.loadTex(26, "res/minigun5.png");
		engine.loadTex(27, "res/health_box.png");
		engine.loadTex(28, "res/table.png");
		engine.loadTex(29, "res/ammo_box.png");
		engine.loadTex(30, "res/minigun_item.png");
	}
	public static void main(String[] args) {
		new Game();
	}
}
