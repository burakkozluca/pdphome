package pdp_odev;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class main {
	public static void main(String[] args) throws IOException
	{
		StringBuilder javadoc_array = null;
		StringBuilder fonksiyon_array = null;
		StringBuilder comment_array = null;
		StringBuilder class_array = null;
		StringBuilder fonksiyon_ismi = null;
		
		Pattern class_ = Pattern.compile("\\b((public|private)\\s+)(class)\\s*(\\w+)"); 
		Pattern fonksiyon = Pattern.compile("\\b((public|private|protected|static|final|abstract)\\s+)*([\\w]+\\s+)\\s*(\\w+)\\s*\\((.*?)\\)\\s*(?:throws\\s+[\\w.]+(?:\\s*,\\s*[\\w.]+)*)?\\s*[\\{;]?");
        
		try 
	    {
	    	String fileline= "C:\\Users\\burak\\pdp\\pdp_odev\\src\\pdp_odev\\deneme.java";
			String satir;
	    	FileReader fileReader = new FileReader(fileline);
			BufferedReader buffer=new BufferedReader(fileReader);
			File file = new File("javadoc.txt");
			File file1 = new File("teksatir.txt");
			File file2 = new File("coksatir.txt");
			
			//fonksiyonda mı?,javadoc mu?, çok satirli yorum mu? satir bunların içinde mi kontrolü
			boolean function = false;
			boolean javadoc = false;
			boolean yorum = false;
			boolean boolean_class = false;
			
			int javadoc_sayisi = 0;
			int coksatir_sayisi = 0;
			int teksatir_sayisi = 0;
			
			while((satir = buffer.readLine()) != null) 
			{
				Matcher fonksiyon_mac = fonksiyon.matcher(satir);
				Matcher class_mac = class_.matcher(satir);
				//javadoc bulmak için fonksiyon yazacağız ama */ bittikten sonraki satir fonksiyonsa devreye girecek
				//fonksiyon bulma
				if(satir.contains("/**")) 
				{
					javadoc = true;
					javadoc_array = new StringBuilder(satir.trim());
					javadoc_array.append("\n");
					javadoc_sayisi++;
				}
				if(satir.contains("*") && javadoc && !satir.contains("/**")) 
				{
					javadoc_array.append(satir.trim());
                    javadoc_array.append("\n");
				}
				buffer.mark(10000); // mark() yöntemi, tampon bellekteki konumu kaydeder
				String nextline = buffer.readLine();
				
				//System.out.println(satir);
				if(satir.contains("*/") && javadoc && nextline.contains("public")) 
				{
					javadoc = false;
					FileWriter writer = new FileWriter(file, true);
					writer.write("Fonksiyon: "+ nextline + "\n");
                    writer.write(javadoc_array.toString());
                    writer.write("--------------------\n");
                    writer.close();
                    //javadocComment = null;
				}
				buffer.reset();
				if(fonksiyon_mac.matches()) 
				{
					function = true;
					
					String modifiers = fonksiyon_mac.group(1);
					String returnType = fonksiyon_mac.group(2);
					String functionName = fonksiyon_mac.group(3);
					String functionName1 = fonksiyon_mac.group(4);
					String constructor = fonksiyon_mac.group(5);
					
					fonksiyon_array = new StringBuilder();
					fonksiyon_ismi = new StringBuilder();
					
					if(modifiers != null)
						fonksiyon_array.append(returnType + " " + functionName + functionName1 + "()");
					else
						fonksiyon_array.append(functionName + functionName1 + " (" + constructor +")");
					
					fonksiyon_ismi.append(functionName1);
					//bunu fonksiyon dışına çıkar fonksiyonu arraye at boolean 1 olsun fonksiyon bittiyse arrayi sil boolean false olsun.  
                    
                    //System.out.println("Function found: "+ modifiers+ " " + returnType + " " + functionName);
					//System.out.println(fonksiyon_array.toString());
                    
				}
				if(function && satir.contains("//")) 
				{
					teksatir_sayisi++;
					FileWriter writer1 = new FileWriter(file1, true);
					writer1.write("Fonksiyon: "+ fonksiyon_array + "\n");
					writer1.write(satir.substring(satir.indexOf("//")) + "\n");
                    writer1.write("--------------------\n");				
					writer1.close();
				}
				if(function && satir.contains("/*") && !satir.contains("/**")) 
				{
					yorum = true;
					comment_array = new StringBuilder(satir.trim());
					comment_array.append("\n");
					coksatir_sayisi++;
				}
				if(function && satir.contains("*") && yorum && !satir.contains("/*")) 
				{
					comment_array.append(satir);
					comment_array.append("\n");
				}
				if(function && satir.contains("*/") && yorum) 
				{
					yorum = false;
					FileWriter writer2 = new FileWriter(file2, true);
					writer2.write("Fonksiyon: "+ fonksiyon_array + "\n");
                    writer2.write(comment_array.toString());
                    writer2.write("--------------------\n");
                    writer2.close();
				}
				//tekli yorum satiri bulma
				// // işaretinden sonra string var mı kontrolü yap.
				//sadece satir başındaki yorumları okuyor. satir souna doğru olan yorum satirini da okuttur.
				if(class_mac.find()) 
				{
					String functionName = class_mac.group(4);
					
					class_array = new StringBuilder();
					class_array.append(functionName);
					
				}
				if(satir.contains("}"))
				{
					function = false;
					
					if(!boolean_class) 
					{
						boolean_class = true;
						System.out.println("Sınıf: " + class_array);
					}
					if(fonksiyon_ismi != null) 
					{
						System.out.println("	Fonksiyon: " + fonksiyon_ismi);
						System.out.println("		Tek Satır Yorum Sayısı:   " + teksatir_sayisi);
						System.out.println("		Çok Satırlı Yorum Sayısı: " + coksatir_sayisi);
						System.out.println("		Javadoc Yorum Sayısı:     " + javadoc_sayisi);
					}
					fonksiyon_ismi = null;
					fonksiyon_array = null;
					javadoc_sayisi = 0;
					teksatir_sayisi = 0;
					coksatir_sayisi = 0;
				}
			}
	    }
	    catch (FileNotFoundException ex)
		{	
			System.out.println("Dosya Okunamadi!");
		}
	}
}
