package pdp_odev;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class main {
	public static void main(String[] args) throws IOException
	{
		ArrayList<String> fonksiyon_sayisi = new ArrayList<String>();
		Pattern fonksiyon = Pattern.compile("\\b((public|private|protected|static|final|abstract)\\s+)*([\\w]+\\s+)\\s*(\\w+)\\s*\\((.*?)\\)\\s*(?:throws\\s+[\\w.]+(?:\\s*,\\s*[\\w.]+)*)?\\s*[\\{;]?");
				
		try 
	    {
	    	String fileline= "C:\\Users\\burak\\pdp\\pdp_odev\\src\\pdp_odev\\deneme.java";
			String satir;
	    	FileReader fileReader = new FileReader(fileline);
			BufferedReader buffer=new BufferedReader(fileReader);
			File file = new File("C:\\Users\\burak\\pdp\\pdp_odev\\src\\pdp_odev\\javadoc.txt");
			File file1 = new File("C:\\Users\\burak\\pdp\\pdp_odev\\src\\pdp_odev\\teksatir.txt");
			File file2 = new File("C:\\Users\\burak\\pdp\\pdp_odev\\src\\pdp_odev\\coksatir.txt");
			
			//fonksiyonda mı?,javadoc mu?, çok satirli yorum mu? satir bunların içinde mi kontrolü
			boolean function = false;
			boolean javadoc = false;
			boolean yorum = false;
			
			while((satir = buffer.readLine()) != null) 
			{
				Matcher fonksiyon_mac = fonksiyon.matcher(satir);
				//javadoc bulmak için fonksiyon yazacağız ama */ bittikten sonraki satir fonksiyonsa devreye girecek
				//fonksiyon bulma
				if(satir.contains("/**")) 
				{
					javadoc = true;
					yorum = true;
					//System.out.println("yorum");
				}
				if((satir.contains("* @") || satir.contains("*@")) && javadoc) 
				{
					yorum = false;
					//System.out.println("burak");
				}
				//String nextline = buffer.readLine();
				//Matcher next_matcher = fonksiyon.matcher(nextline);
				if(satir.contains("*/") && javadoc) 
				{
					javadoc = false;
					//System.out.println("bruak");
				}
				//System.out.println("a:"+satir);
				//System.out.println("b:" +nextline);
				//System.out.println("a:"+satir);
				if(fonksiyon_mac.find()) 
				{
						String modifiers = fonksiyon_mac.group(1);
						String returnType = fonksiyon_mac.group(2);
						String functionName = fonksiyon_mac.group(3);
						String functionName1 = fonksiyon_mac.group(4);
						String constructor = fonksiyon_mac.group(5);
						
						if(modifiers != null)
						{
							System.out.println("Function found: " + returnType + " " + functionName + " " + functionName1);
						}
						else 
						{
							System.out.println("Function found: "+ functionName+ " " + functionName1 + " " + constructor);
						}
							
					
                    function = true;
                    
                    FileWriter writer = new FileWriter(file, true);

                    writer.write("yeni bilgi");
                    writer.close();
                    //System.out.println("Function found: "+ modifiers+ " " + returnType + " " + functionName);
				
                    
				}
				//tekli yorum satiri bulma
				// // işaretinden sonra string var mı kontrolü yap.
				//sadece satir başındaki yorumları okuyor. satir souna doğru olan yorum satirini da okuttur.
				if(function && satir.contains("//")) 
				{
					FileWriter writer1 = new FileWriter(file1, true);
					writer1.write(satir);
					writer1.close();
					//System.out.println("tek satir yorumu yazildi.");
				}
				if(function && satir.contains("/*")) 
				{
					
				}
				if(satir.contains("}"))
					function = false;
			}
	    }
	    catch (FileNotFoundException ex)
		{	
			System.out.println("Dosya Okunamadi!");
		}
	}
}
