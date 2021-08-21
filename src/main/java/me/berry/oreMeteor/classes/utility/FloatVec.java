package me.berry.oreMeteor.classes.utility;

import java.io.Serializable;

public class FloatVec implements Serializable {
	private static final long serialVersionUID = -8309401354959887103L;
	private final float x, y, z;

	public FloatVec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
}
