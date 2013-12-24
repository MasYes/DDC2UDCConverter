import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Юлиан
 * Date: 23.12.13
 * Time: 1:09
 * To change this template use File | Settings | File Templates.
 */
public class Converter {

	private HashMap<String, ArrayList<String>> DDC2UDC = new HashMap<>();
	private HashMap<String, ArrayList<String>> UDC2DDC = new HashMap<>();
	private final String notDDC2UDC = "Аналога данному ДКД в УДК нет.";
	private final String notUDC2DDC = "Аналога данному УДК в ДКД нет.";
	private final String DDCNotExist = "Данного номера ДКД нет";
	private final String oo = "00";
	private final String o = "0";





	public Converter() throws Exception{
		Scanner scan = new Scanner(new FileInputStream("D2U.txt"));
		while(scan.hasNext()){
			analyzeLine(scan.nextLine());
		}
	}




	private void analyzeLine(String line){
		DDC2UDC.put(line.split("-")[0].trim(), new ArrayList<String>());
		if(line.split("-")[1].contains("null")){
			DDC2UDC.get(line.split("-")[0].trim()).add(DDCNotExist);
		}else{
			add2HashMaps(line.split("-")[0].trim(), line.split("-")[1]);
		}
	}

	private void add2HashMaps(String ddc, String udcAll){
		for(String udc : udcAll.trim().split("\\+")){
			udc = udc.trim();
			add2ArrayList(ddc, udc);
		}
	}




	private void add2ArrayList(String ddc, String udc){
		if(udc.equals("none")){
			DDC2UDC.get(ddc).addAll(DDC2UDC.get(parentDDC(ddc)));
		}
		else{
			add2ddc(ddc, udc);
		}
	}





	private void add2ddc(String ddc, String udc){
		DDC2UDC.get(ddc).add(udc);
		if(!UDC2DDC.containsKey(udc))
			UDC2DDC.put(udc, new ArrayList<String>());
		UDC2DDC.get(udc).add(ddc);
	}





	public String DDC2UDC(String ddc){
		if(DDC2UDC.containsKey(ddc))
			return D2U2String(ddc);
		else
			return notDDC2UDC;
	}





	public String UDC2DDC(String udc){
		if(udc.equals(""))
			return notUDC2DDC;
		if(UDC2DDC.containsKey(udc))
			return U2D2String(udc);
		return UDC2DDC(udc.substring(0, udc.length() - 1));
	}




	private String parentDDC(String ddc){
		if(ddc.endsWith("0")){
			return ddc.substring(0, 1) + oo;
		}
		else{
			return ddc.substring(0, 2) + o;
		}
	}




	private String D2U2String(String key){
		String res = "";
		for(String str : UDC2DDC.get(key))
			res += str + " ";
		return res;
	}




	private String U2D2String(String key){
		String res = "";
		for(String str : DDC2UDC.get(key))
			res += str + " ";
		return res;
	}

}
