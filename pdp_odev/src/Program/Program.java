package Program;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Program {
	public static void main(String[] args) throws IOException
	{
		//yorumlari tutmak icin StringBuilder tanimladim.
		StringBuilder javadoc_array = null;
		StringBuilder fonksiyon_array = null;
		StringBuilder comment_array = null;
		StringBuilder class_array = null;
		StringBuilder fonksiyon_ismi = null;
		
		//classlari ve fonksiyonlari bulmak icin regex pattern tanimlandi.
		Pattern class_ = Pattern.compile("\\b((public|private)\\s+)(class)\\s*(\\w+)"); 
		Pattern fonksiyon = Pattern.compile("\\b((public|private|protected|static|final|abstract)\\s+)*([\\w]+\\s+)\\s*(\\w+)\\s*\\((.*?)\\)\\)?");
		
		//if (args.length != 1) //okunacak dosya argumani girildi mi?
		//{
        //    System.err.println("Hatali Arguman");
        //    System.exit(1);
        //}
		
		try
	    {
	    	String fileline = "/Users/burakkozluca/eclipse-workspace/Java_odev/dist/Motor.java"; //okunacak dosya argümani
			String satir;
	    	FileReader fileReader = new FileReader(fileline); //dosya okuma
			BufferedReader buffer = new BufferedReader(fileReader); //satir satir dosyayi okumak icin buffer tanimladim.
			File file = new File("javadoc.txt");
			File file1 = new File("teksatir.txt");
			File file2 = new File("coksatir.txt");
			
			//fonksiyonda mı?,javadoc mu?, çok satirli yorum mu? satir bunların içinde mi kontrolü
			boolean function = false;
			boolean javadoc = false;
			boolean yorum = false;
			boolean boolean_class = false;
			
			int javadoc_sayisi = 0; //yorum sayilarini tutmak icin degiskenler
			int coksatir_sayisi = 0;
			int teksatir_sayisi = 0;
			
			while((satir = buffer.readLine()) != null) //dosyayi satir satir okuyan dongu
			{
				//fonksiyonlari ve classlari regexle yakalamak icin matcher
				Matcher fonksiyon_mac = fonksiyon.matcher(satir);
				Matcher class_mac = class_.matcher(satir);
				
				//satir javadoc baslangici iceriyorsa
				if(satir.contains("/**")) 
				{
					javadoc = true; //javadoc yorumunun icinde oldugumuzu takip etmek icin boolean'i true yapiyoruz.
					javadoc_array = new StringBuilder(satir.trim()); //satiri javadoc_array'e ekliyoruz.
					javadoc_array.append("\n");
					javadoc_sayisi++;
				}
				
				//fonksiyon varsa
				if(fonksiyon_mac.find())
				{
					function = true; //fonksiyon icinde oldugumuzu takip etmek icin boolean'i true yapiyoruz.
					
					//fonksiyonu parcalayarak gerekli bilgileri ayiriyoruz.
					String modifiers = fonksiyon_mac.group(1);
					String returnType = fonksiyon_mac.group(2);
					String functionName = fonksiyon_mac.group(3);
					String functionName1 = fonksiyon_mac.group(4);
					String constructor = fonksiyon_mac.group(5);
					
					//fonksiyon_array ve fonksiyon_ismi icin bellekte yer aciyoruz.
					fonksiyon_array = new StringBuilder();
					fonksiyon_ismi = new StringBuilder();
					
					if(modifiers != null) //modifiers kontrolu fonksiyonun parametresi var mi yok mu diye yapildi.
						fonksiyon_array.append(returnType + " " + functionName + functionName1 + "()"); //fonksiyon array'e eklendi.
					else
						fonksiyon_array.append(functionName + functionName1 + " (" + constructor +")");
					
					fonksiyon_ismi.append(functionName1);//fonksiyon ismi de ayrica eklendi.
                    
				}
				
				//javadoc yorum devam ediyor mu?
				if(satir.contains("*") && javadoc && !satir.contains("/**")) 
				{
					javadoc_array.append(satir.trim());//satiri array'e ekleme
                    javadoc_array.append("\n");
				}
				
				buffer.mark(10000); // mark() yöntemiyle satirin bellekteki konumunu kaydettim.
				String nextline = buffer.readLine(); //sonraki satira gecis yaptim.
				
				//javadoc fonksiyonun sonuna geldik mi? ve sonraki satirda fonksiyon var mi?
				if(satir.contains("*/") && javadoc && (nextline.contains("public") || nextline.contains("private"))) 
				{
					int parantezIndis = nextline.indexOf('(');
					int sonBoslukIndis = nextline.lastIndexOf(' ', parantezIndis - 1);
			        javadoc = false; //javadoc boolean'i false yapiyoruz.

			        String arasi = nextline.substring(sonBoslukIndis + 1, parantezIndis).trim();
			        FileWriter writer = new FileWriter(file, true); //javadoc yorumu tutmak icin file olusturyoruz.
					writer.write("Fonksiyon: "+ arasi + "\n"); //fonksiyon ismini dosyaya yazdirip
                    writer.write(javadoc_array.toString());//yorumu dosyaya ekliyorum.
                    writer.write("--------------------\n");
                    writer.close();
				}
				buffer.reset(); // mark() yöntemiyle kaydettiğim bellekteki satir konumuna geri dondum.
				
				//satir fınksiyon icinde mi? ve tek satirlik yorum var mi?
				if(function && satir.contains("//")) 
				{
					teksatir_sayisi++;
					FileWriter writer1 = new FileWriter(file1, true); //tek satir yorumlari icin dosya olusturuldu.
					writer1.write("Fonksiyon: "+ fonksiyon_ismi + "\n");
					writer1.write(satir.substring(satir.indexOf("//")) + "\n"); //yorum dosyaya eklendi.
                    writer1.write("--------------------\n");				
					writer1.close();
				}
				
				//satir fonksiyon icinde mi? ve coklu yorum satiri baslangici iceriyor mu?
				if(function && satir.contains("/*") && !satir.contains("/**")) 
				{
					yorum = true;
					int index = 0; //tek satirda biten coklu yorum kontrolu icin index tuttum.
					
					index = satir.indexOf("*/");
					if(index  == -1)
						comment_array = new StringBuilder(satir.trim()); //yorum tek satirda bitmiyorsa
					else
					{
						comment_array = new StringBuilder();
						comment_array.append(satir.substring(0, index + 2)); //yorum tek satirda bitiyorsa
					}
					comment_array.append("\n");
					coksatir_sayisi++;  
				}
				
				//fonksiyon icineysek ve coklu yorum satiri devam ediyorsa
				if(function && satir.contains("*") && yorum && !satir.contains("/*")) 
				{
					comment_array.append(satir); //yorumu ekledik.
					comment_array.append("\n");
				}
				
				//fonksiyon icindeysek ve coklu yorum satiri bittiyse
				if(function && satir.contains("*/") && yorum) 
				{
					yorum = false;
					FileWriter writer2 = new FileWriter(file2, true); //coklu yorum icin dosya olusturuldu.
					writer2.write("Fonksiyon: "+ fonksiyon_ismi + "\n");
                    writer2.write(comment_array.toString()); //yorumlar dosyaya yazdirildi.
                    writer2.write("--------------------\n");
                    writer2.close();
				}
				
				//class varsa
				if(class_mac.find()) 
				{
					String functionName = class_mac.group(4);
					
					class_array = new StringBuilder();
					class_array.append(functionName); //class ismi bulundu.
					
				}
				
				//fonksiyon bittiyse
				if(satir.contains("}"))
				{
					function = false;
					
					if(!boolean_class) 
					{
						boolean_class = true; //classı yazdiktan sonra bir daha yazmamak icin boolean tutuldu.
						System.out.println("Sınıf: " + class_array);
					}
					if(fonksiyon_ismi != null) //fonksiyon ve yorum sayilari konsol ekranina yazdirildi.
					{
						System.out.println("	Fonksiyon: " + fonksiyon_ismi);
						System.out.println("		Tek Satır Yorum Sayısı:   " + teksatir_sayisi);
						System.out.println("		Çok Satırlı Yorum Sayısı: " + coksatir_sayisi);
						System.out.println("		Javadoc Yorum Sayısı:     " + javadoc_sayisi);
					}
					//tekrar kullanilirken karmasikliga sebep olmamasi icin bazi degerler 0'landi.
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
