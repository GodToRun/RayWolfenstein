package raycasting;

public class AnimateThread extends Thread {
	int baseTex, animateLen, frameMs, loopPoint;
	public int curTex;
	public boolean isEnded = false;
	public AnimateThread(int baseTex, int animateLen, int frameMs, int loopPoint) {
		this.baseTex = baseTex;
		this.animateLen = animateLen;
		this.frameMs = frameMs;
		this.curTex = baseTex;
		this.loopPoint = loopPoint;
	}
	@Override
	public void run() {
		for (int i = 0; i < animateLen; i++) {
			if (i >= loopPoint) {
				curTex -= 1;
				i -= 2;
			}
			else
				curTex = baseTex+i;
			try {
				Thread.sleep(frameMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		curTex = baseTex;
		isEnded = true;
	}
}
