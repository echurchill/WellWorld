package me.daddychurchill.WellWorld.WellTypes.Codename_B;

public class BananaTrigFunction {
	//EC: changed this to private. the getList function should be the only way to access this variable
//	private static boolean[] bools;// = null; should not be set here due to multi-threading issues associated with doing so

	public static double normalise(int x) {

		int conv = x % 360;
		if(conv >= 1)
			x = x % 360;
		return Math.toRadians(x);
	}

	public static double sinOctave(int x, int z, int octave) {
		return (Math.sin(normalise(x/octave))+Math.sin(normalise(z/octave)));
	}

	public static double cube(double x) {
		return x*x*x;
	}
	public static boolean holes(int x, int z) {
		double calc;
		calc = Math.tan(normalise(x/2))+Math.tan(normalise(z/2)-normalise(x/2))+Math.tan(normalise(z/2)*normalise(x/2))-Math.sin(normalise(x/2));
		calc = Math.abs(calc/20);
		if(calc>=1)
			calc = 1;
		else calc = 0;
		return calc==1?true:false;
	}

//EC: FindErrors still does not list this... don't know why or how to fix it... sigh
//	public static boolean[] getList() {
//		if (bools == null) {
//			bools = new boolean[16*16];
//			for(int x=-8; x<8; x++)
//				for(int z=-8; z<8; z++)
//				{
//					double spin = Math.tan(normalise(x*x*4+z*z*4));
//					spin = Math.abs(spin);
//					if(spin>=1)
//						spin = 1;
//					else
//						spin = 0;
//					bools[x+z*16] = spin==1?true:false;
//				}
//		}
//		return bools;
//	}

	public static double get(int x, int z) {
		double calc;

		calc = 
				Math.sin(normalise(x/4))
				+Math.sin(normalise(z/5))
				+Math.sin(normalise(x-z))
				+(Math.sin(normalise(z))/(Math.sin(normalise(z))+2))*2
				+Math.sin(normalise((int) (Math.sin(normalise(x)+Math.sin(normalise(z)))+Math.sin(normalise(z))))+Math.sin(normalise(z/2))
						+Math.sin(normalise(x)
								+Math.sin(normalise(z)))
								+Math.sin(normalise(z))
								+Math.sin(normalise(z))
								+Math.sin(normalise(x)))
								+Math.sin(normalise((x+z)/4))
								+Math.sin(normalise((int) (x+Math.sin(normalise(z)*Math.sin(normalise(x))))));

		calc = Math.abs(calc/6);
		if(calc>1)
			calc = 1;
		return calc;
	}
}
