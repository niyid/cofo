package com.vasworks.cofo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class PolygonSurveyor {

	public static void main(String[] args) {
		final String url = "http://192.168.43.99:8069", db = "bayelsa_gov", password = "passw0rd123";
		int userId = 1;
		
		final String applicationId = "38", polygonString = "((23 45, 23 57, 54 34, 23 45))";

		XmlRpcClient models;
		try {
			models = new XmlRpcClient() {
				{
					setConfig(new XmlRpcClientConfigImpl() {
						{
							setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
						}
					});
				}
			};

			Object val = models.execute("execute_kw", asList(db, userId, password, "lis.cofo", "import_polygon", asList2(applicationId, polygonString)));
			
			System.out.println("Return val=" + val);
			
			System.out.println("Conversion: " + degrees2Meters(-77.035974, 38.898717));
		} catch (MalformedURLException | XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static List<Object> asList(String db, int userId, String password, String entityName, String entityMethod, List<String> methodParams) {
		List<Object> params = new ArrayList<>(6);
		params.add(db);
		params.add(userId);
		params.add(password);
		params.add(entityName);
		params.add(entityMethod);
		params.add(methodParams);

		return params;
	}

	private static List<String> asList2(String applicationId, String polygonString) {
		List<String> params = new ArrayList<>(2);
		params.add(applicationId);
		params.add(polygonString);

		return params;
	}
	
	private static String degrees2Meters(double lon, double lat) {
        double x = lon * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        return "(" + x + ", " + y + ")";
	}
}
