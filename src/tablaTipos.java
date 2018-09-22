import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class tablaTipos {

    public final String ARCHIVO;                                        // Constante que aloja la ruta del archivo a leer
    File escritorio;                                                    // Objeto que contiene la ruta completa, con el nombre del archivo
    ArrayList<String> lectura, lexemas, tipos, valores;                    // Cadena que contendrá cada linea del archivo de texto
    String lineaActual;

    //Constructor no nulo
    public tablaTipos() {
        ARCHIVO = System.getProperty("user.home") + "\\Desktop\\";      // Codigo que detecta el escritorio
        escritorio = new File(ARCHIVO + "texto.txt");         // Agrega el nombre del archivo
        lectura = new ArrayList<>();                                    // inicializacion
        lexemas = new ArrayList<>();
        tipos = new ArrayList<>();
        valores = new ArrayList<>();
        lineaActual = "";                                               //

        this.entrada();                                                 // llamada al metodo entrada()
        //this.agregarTablaSimbolos("x","int","");
        //this.agregarTablaSimbolos("y","","10.1");
        //this.agregarTablaSimbolos("x","","2");
        this.sintactico();
    }

    //Lectura de datos de archivo de texto
    public void entrada() {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(escritorio);      // Se especifica el archivo a leer
            br = new BufferedReader(fr);          // Se prepara el metodo de lectura de archivos

            while ((lineaActual = br.readLine()) != null)        // Asigna la linea leida a lineaActual, mientras no devuelva nulo, sigue leyendo
            {
                lectura.add(lineaActual);                          // Se guarda la linea leida en un Arreglo de Lista
                //System.out.println(lineaActual);                  // Cada elemento del arrayList es una linea completa del archivo de texto
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {                               // finally sirve para indicar las instrucciones que se realizaran independientemente
            try {                                 //   de si existio o no una excepción antes (el anterior try catch)
                if (br != null)
                    br.close();                       // termina el proceso del BufferedReader
                if (fr != null)
                    fr.close();                       // termina el proceso del FileReader
            } catch (IOException ex) {
                ex.printStackTrace();               // si hubo excepcion imprimira un diagnostico de la pila
            }
        }
    }

    //Separador de palabras
    public String[] separador(String linea)        //Para usarlo, utiliza ArrayList lectura.get(i); para obtener el string que deseas separar
    {
        return linea.split("[,; ]+");  // regex que omite comas, espacios y punto y comas, tantas como sean
    }

    //Metodo auxiliar para detectar numeros reales
    public boolean isNumeric(String str) {
        int cont = 0;                                   // Contador que detectara los puntos analizados
        for (char c : str.toCharArray()) {
            if(c == '.' && cont < 1)                    // asi detectara a los numeros de punto flotante, pero solo admitira un punto
            {                                           // si lee otro, se ira al else y retornara falso porque el punto no es digito
                cont++;                                     // aumenta el contador para detectar cuantas veces ya analizo un punto
                continue;                       // Se salta al siguiente char
            } else if(!Character.isDigit(c))   // Si no es un numero
                return false;
        }
        return true;                            // Si logro analizar el string sin problemas, retorna que si es digito
    }

    public void sintactico() {
        //linea contiene strings de cada linea
        String auxTipo;
        String[] linea;
        //conversion de las lineas completas a elementos almacenados en un vector
        for(int j=0; j<lectura.size(); j++){
        	linea=separador(lectura.get(j));	
        	if (linea[0].equals("int") || linea[0].equals("String") || linea[0].equals("float") || linea[0].equals("boolean") || linea[0].equals("char")) {
	            //Se esta declarando una variable
	            auxTipo = linea[0];
	            for (int i = 1; i < linea.length; i++) {
	                if (linea[i].equals(",")) {
	                    //no hace nada
	                } else {
	                    //metodo de busqueda e insercion en la tabla de tipos
	                    agregarTablaSimbolos(linea[i], auxTipo, "");
	                }
	            }//termina for
	        }//termina if
	        else {
	            //agregartablatipo(linea.[0],"", "")//no se si sera vacio o no
	            if (isNumeric(linea[2])) {
	                //Se esta asignando un valor
	                agregarTablaSimbolos(linea[0],"",linea[2]);
	            } else {
	                //Se esta realizando una operacion
	                agregarTablaSimbolos(linea[0],"","");
	                agregarTablaSimbolos(linea[2],"","");
	                agregarTablaSimbolos(linea[4],"","");
	            }//termina else

        }//termina else
        	
        	
        	
        	/*for(String a: linea){
        		System.out.println(a);
        	}*/
        }
        

    }//termina metodo

    public void agregarTablaSimbolos(String lexema, String tipo, String valor)
    {
        int indice;
        if(!lexemas.contains(lexema))    // 1er caso
        {
            lexemas.add(lexema);
            tipos.add(tipo);
            valores.add(valor);
            //System.out.println("Entre con "+lexema);
        }else
        {
            indice = lexemas.indexOf(lexema);
            if(tipos.get(indice).equals(""))
                tipos.add(indice, tipo);

            if(valores.get(indice).equals(""))
                valores.add(indice,valor);
        }

       // CODIGO PARA IMPRIMIR TABLA DE SIMBOLOS
          System.out.println("\nLexema\t\tTipo\t\tValor");
        // int max = Math.max(Math.max(lexemas.size(), tipos.size()), valores.size());    // Sirve para calcular cual de los 3 arraylist tiene mas datos y usar ese numero para el for
        //MARCA ERROR SI LA CANTIDAD DE DATOS NO ES LA MISMA PARA LAS 3 COLUMNAS, FALTA MANEJAR EXCEPCIONES
		//System.out.println("max: "+max+ "lexemas:"+lexemas.size());
        for (int i = 0; i < lexemas.size(); i++) {
            if (lexemas.get(i).length() > 1)
                if (tipos.get(i).length() > 3)
                    System.out.println(lexemas.get(i) + "\t" + tipos.get(i) + "\t" + valores.get(i));
                else
                    System.out.println(lexemas.get(i) + "\t" + tipos.get(i) + "\t\t" + valores.get(i));
            else if (tipos.get(i).length() > 3)
                System.out.println(lexemas.get(i) + "\t\t" + tipos.get(i) + "\t" + valores.get(i));
            else
                System.out.println(lexemas.get(i) + "\t\t" + tipos.get(i) + "\t\t" + valores.get(i));
        }
    }

    public static void main(String[] args) {
        new tablaTipos();
    }
}
    /*Para calcular la longitud de los digitos de valor (en caso de que se agregue una columna a la izquierda y sea necesario para darle formato a la salida)*/
        //length = (int)(Math.log10(valor.get(i))+1) > 1


    /*Codigo para trabajar con vectores:
        String[] lexema = {"x","y","z","suma"};
        String[] tipo = {"int", "int", "int", "int"};
        Integer[] valor = {80, 20, 60, 80};
        for (int i = 0; i < lexema.length; i++) {
            if(lexema[i].length() > 1)
                System.out.println(lexema[i] + "\t" + tipo[i] + "\t\t" + valor[i]);
            else
                System.out.println(lexema[i] + "\t\t" + tipo[i] + "\t\t" + valor[i]);*/