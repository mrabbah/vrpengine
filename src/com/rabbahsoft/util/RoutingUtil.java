package com.rabbahsoft.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RoutingUtil {
	
	public final static  Double rayonTerre=20000/Math.PI; //6366.9 km
	
	public static Double distFromTo(Double lat1, Double lng1, Double lat2, Double lng2) {
		if(lat1==null || lng1==null || lat2==null || lng2==null) return 0.0; 
		Double dLat = Math.toRadians(lat2-lat1);
		Double dLng = Math.toRadians(lng2-lng1);
		Double sindLat = Math.sin(dLat / 2);
		Double sindLng = Math.sin(dLng / 2);
		Double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double dist = rayonTerre * c*1000;//en metre
		dist=round(dist, 0);
	    return dist;
	}
	
	public static double round(double value, int places) {
		//System.out.println(value);
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public static Double format(Double d) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);
        String result=format.format(d);
        return Double.parseDouble(result.replace(",", "."));
    }
	
	public static void main(String[] args) {
		//33.590341, -7.565350 hay mohammadi
		//33.570459, -7.626530 derb ghalef
		//33.5930556,-7.6163889 casablanca
		//34.0252778,-6.8361111 rabat
		System.out.println("distance entre casa rabat= "+distFromTo(33.5930556,-7.6163889,34.0252778,-6.8361111)+" m");
		System.out.println("distance entre hay mohammadi derb ghalef= "+distFromTo(33.590341,-7.565350,33.570459,-7.626530)+" m");
	}
}
