package raycasting;

public class DeathThread extends Thread {
	Sprite sprite;
	public DeathThread(Sprite sprite) {
		this.sprite = sprite;
	}
	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			sprite.texture++;
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
