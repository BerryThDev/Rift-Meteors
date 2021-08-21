package me.berry.oreMeteor.utils;

import java.util.concurrent.ThreadLocalRandom;

public class MathUtil {
	public int randBetween(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public long randBetweenLong(long min, long max) {
		return ThreadLocalRandom.current().nextLong(min, max + 1);
	}

	public static double correction(double standPoint, double blockPoint) {
		final double movementNum = 0.5;
		if(standPoint + 2 == blockPoint) {
			System.out.println("Returning");
			return standPoint;
		}
		else {
			double diff = standPoint - blockPoint;
			double diffAbs = Math.abs(diff);

			//if(diffAbs <= movementNum) return Math.round(standPoint * 100.0) / 100.0;

			if(isNeg(diff)) return standPoint + movementNum;
			else return standPoint - movementNum;
		}
	}

	public static boolean isNeg(double num) {
		return num < 0;
	}

	public float angle_triangle(int x1, int x2, int x3, int y1, int y2, int y3, int z1, int z2, int z3)  {
		int num = (x2-x1)*(x3-x1)+(y2-y1)*(y3-y1)+(z2-z1)*(z3-z1) ;

		double den = Math.sqrt(Math.pow((x2-x1),2)+
				Math.pow((y2-y1),2)+Math.pow((z2-z1),2))*
				Math.sqrt(Math.pow((x3-x1),2)+
						Math.pow((y3-y1),2)+Math.pow((z3-z1),2)) ;

		double angle = Math.acos(num / den)*(180.0/3.141592653589793238463) ;

		return (float) angle;
	}
}
