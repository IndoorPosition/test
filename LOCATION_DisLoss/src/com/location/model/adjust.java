package com.location.model;

import Jama.Matrix;

public class adjust {
	
	int size_Mac1 = 0;
	int size_Mac0 = 0;
	int n = 0;
	double X0 = 0;
	double Y0 = 0;
	public double x = 0;
	public double y = 0;
	
	public adjust(Double[] s, Double[] x0, Double[] y0, String[] Mac0, String[] Mac1){
		size_Mac1 = Mac1.length;
		size_Mac0 = Mac0.length;
		Matrix B = new Matrix(size_Mac1, 2, 0.0);
		Matrix L = new Matrix(size_Mac1, 1, 0.0);
		Matrix P = new Matrix(size_Mac1, size_Mac1, 0);
		System.out.println(x+","+y);
		while (n < 4)
		{
			for(int i = 0; i<size_Mac1;i++)
			{
				for(int j = 0;j<size_Mac0;j++)
				{
					if(Mac1[i].equals(Mac0[j]))
					{
						X0 = x0[j];
						Y0 = y0[j];
						break;
					}
				}
				B.set(i, 0, (X0 - x)/s[i]);
				B.set(i, 1, (Y0 - y)/s[i]);
				L.set(i, 0, ((Math.pow(s[i], 2.0) - Math.pow(X0 - x, 2.0) - Math.pow(Y0 - y, 2)-1.44)/2*s[i]));
				P.set(i, 0, 1/s[i]);
			}
			Matrix Nbb = B.transpose().times(P).times(B);
			Matrix W = B.transpose().times(P).times(L);
			Matrix delta_X = Nbb.inverse().times(W);
			x = x+delta_X.get(0, 0);
			y = y+delta_X.get(1, 0);
			n = n+1;
			System.out.print(delta_X.get(0, 0)+","+delta_X.get(1, 0));
		}
		System.out.println(x+","+y);	
	}
	

}
