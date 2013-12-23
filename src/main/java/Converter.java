import java.io.File;
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

	private HashMap<String, ArrayList<String>> DDC2UDC;
	private HashMap<String, ArrayList<String>> UDC2DDC;

	public Converter(){
		try{
			String myJarPath = Converter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String dirPath = new File(myJarPath).getParent() + "\\";
			DDC2UDC = new HashMap<>();
			UDC2DDC = new HashMap<>();
			Scanner scan = new Scanner(new FileInputStream("D2U.txt"));
			while(scan.hasNext()){
				String str[] = scan.nextLine().split("-");
				str[0] = str[0].trim();
				DDC2UDC.put(str[0], new ArrayList<String>());
				if(str[1].contains("null")){
					DDC2UDC.get(str[0]).add("Данного номера ДКД нет.");
				}else{
					for(String udc : str[1].trim().split("\\+")){
						udc = udc.trim();
						if(udc.equals("none")){
							DDC2UDC.get(str[0]).addAll(DDC2UDC.get(parentDDC(str[0])));
						}
						else{
							DDC2UDC.get(str[0]).add(udc);
							if(!UDC2DDC.containsKey(udc))
								UDC2DDC.put(udc, new ArrayList<String>());
							UDC2DDC.get(udc).add(str[0]);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String DDC2UDC(String ddc){
		String result = "";
		if(DDC2UDC.containsKey(ddc))
			for(String str : DDC2UDC.get(ddc))
				result += str + " ";
		else
			result = "Аналога данному ДКД в УДК нет.";
		return result;
	}

	public String UDC2DDC(String udc){
		String result = "";
		while(result.equals("")){
			if(UDC2DDC.containsKey(udc))
				for(String str : UDC2DDC.get(udc))
					result += str + " ";
			else
				udc = udc.substring(0, udc.length() - 1);
			if(udc.equals(""))
				result = "Аналога данному УДК в ДКД нет";
		}
		return result;
	}

	private static String parentDDC(String ddc){
		String parent;
		if(ddc.endsWith("0")){
			parent = ddc.substring(0, 1) + "00";
		}
		else{
			parent = ddc.substring(0, 2) + "0";
		}
		return parent;
	}
}
