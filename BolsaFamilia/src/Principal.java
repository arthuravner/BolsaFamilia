import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Principal {
	
	
	private final static String PATH = new File("").getAbsolutePath();
	private final static String FILEPATH = PATH + "/docs/201707_BolsaFamiliaFolhaPagamento.csv";
	private final static String FILEPATH_INDICE = PATH + "/docs/indice.csv";
	
	public static void main (String [] args){
		
		//Cria o indice
		List<Favorecido> listaIndice = new ArrayList<Favorecido>();
		
		try{			
			
			RandomAccessFile f = new RandomAccessFile(FILEPATH, "r");			
	        
	        f.readLine();
	        String linha = f.readLine();	
	        int contador = 0;
	        
	        System.out.println("Lendo dados do arquivo original...");
	        //Popula a listaIndice
	        while(linha != null)
	        {	     
	        	String valores[] = linha.split("\t");
	        	
	        	Favorecido favorecido = new Favorecido();
	        	favorecido.setNisFavorecido(valores[7]);
	        	favorecido.setPosicaoEmBytes(f.getFilePointer());
	        	
	        	listaIndice.add(favorecido);
	        	
	            contador++;
	            if(contador%10000 == 0 && contador < 1500000)
	            {		           
	            	System.out.print(".");
	            	System.out.flush();
	            }
	            	
	            linha = f.readLine();
	        }
	        
	        System.out.println("\nLeitura completa!");
	        
	        System.out.println("Ordenando...");
	        //Ordena o indice
	        Collections.sort(listaIndice, new Comparator<Favorecido>() {
	            @Override
	            public int compare(Favorecido f1, Favorecido f2)
	            {
	                return  Long.compare(f1.getPosicaoEmBytes(), f2.getPosicaoEmBytes());
	            }
	        });
	        
	        contador = 0;
	        System.out.println("Criando arquivo de indice...");
	        //Criar arquivo de indice
	        RandomAccessFile fIndice = new RandomAccessFile(FILEPATH_INDICE, "rw");
	        for (Favorecido indice : listaIndice)
	        {
	        	fIndice.write(converteEmByte(indice));
	        	contador++;
	            if(contador%10000 == 0)
	            {	            	
	            	System.out.print(".");
	            	System.out.flush();
	            }
	        }
	        System.out.println("\nArquivo de indice criado!");
	        
	        fIndice.close();
	        
	        f.close();
	        
		}catch(Exception e)
		{
			e.printStackTrace();
		}
			
    }	
	
	private static byte[] converteEmByte(Favorecido f){
		try{
			Charset enc = Charset.forName("ISO-8859-1");
			byte b[] = (f.getNisFavorecido() + f.getPosicaoEmBytes()+"\n").getBytes(enc);
			return b;			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}
}
