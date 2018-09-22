import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.Arrays;

public class tablaTipos {

    private File escritorio;                                                    // Objeto que contiene la ruta completa, con el nombre del archivo
    private ArrayList<String> lectura, lexemas, tipos, valores;                    // Cadena que contendrá cada linea del archivo de texto
    private String lineaActual;                                                     // Es la cadena que almacenara cada linea del texto.txt

    //Constructor no nulo
    private tablaTipos() {
        // Constante que aloja la ruta del archivo a leer
        String ARCHIVO = System.getProperty("user.home") + "\\Desktop\\";
        escritorio = new File(ARCHIVO + "texto.txt");         // Agrega el nombre del archivo
        lectura = new ArrayList<>();                                    // inicializacion
        lexemas = new ArrayList<>();
        tipos = new ArrayList<>();
        valores = new ArrayList<>();
        lineaActual = "";                                               //

        this.entrada();                                                 // llamada al metodo entrada()
        this.sintactico();
       // this.detectarError();
    }

    //Lectura de datos de archivo de texto
    private void entrada() {
        BufferedReader br = null;
        FileReader fr = null;
        String[] instrucciones;

        try {
            fr = new FileReader(escritorio);      // Se especifica el archivo a leer
            br = new BufferedReader(fr);          // Se prepara el metodo de lectura de archivos

            while ((lineaActual = br.readLine()) != null)        // Asigna la linea leida a lineaActual, mientras no devuelva nulo, sigue leyendo
            {
                instrucciones = lineaActual.split(";");           // Es el arreglo de String que almacena cada instruccion dentro de una linea del texto.txt
                lectura.addAll(Arrays.asList(instrucciones));           // itera cada palabra y guarda cada instruccion que esta dentro del vector en un Arreglo de Lista
                System.out.println("Linea leida: "+lineaActual);        // Cada elemento del arrayList es una linea completa del archivo de texto
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
    private String[] separador(String linea)        //Para usarlo, utiliza ArrayList lectura.get(i); para obtener el string que deseas separar
    {
        return linea.split("[, ]+");         // regex que omite comas, espacios y punto y comas, tantas como sean
    }

    //Metodo auxiliar para detectar numeros reales
    private boolean isNumeric(String str) {
        int cont = 0;                                   // Contador que detectara los puntos analizados
        for (char c : str.toCharArray()) {
            if(c == '.' && cont < 1)                    // asi detectara a los numeros de punto flotante, pero solo admitira un punto
            {                                           // si lee otro, se ira al else y retornara falso porque el punto no es digito
                cont++;                                     // aumenta el contador para detectar cuantas veces ya analizo un punto
            } else if(!Character.isDigit(c))   // Si no es un numero
                return false;
        }
        return true;                            // Si logro analizar el string sin problemas, retorna que si es digito
    }

    private void sintactico() {
        //linea contiene strings de cada linea
        String auxTipo;
        String[] linea;
        //conversion de las lineas completas a elementos almacenados en un vector
        for (String l : lectura) {
            linea = separador(l);                    // Separa cada instruccion contenida en lectura y se lo asigna a linea
            if (linea[0].equals("int") || linea[0].equals("String") || linea[0].equals("float") || linea[0].equals("boolean") || linea[0].equals("char")) {
                //Se esta declarando una variable
                auxTipo = linea[0];
                for (int i = 1; i < linea.length; i++) {
                    agregarTablaSimbolos(linea[i], auxTipo, "");
                }//termina for
            }//termina if
            else {
                if (isNumeric(linea[2])) {
                    //Se esta asignando un valor
                    agregarTablaSimbolos(linea[0], "", linea[2]);
                } else {
                    //Se esta realizando una operacion
                    agregarTablaSimbolos(linea[0], "", "");
                    agregarTablaSimbolos(linea[2], "", "");
                    agregarTablaSimbolos(linea[4], "", "");
                    detectarError(linea[0],linea[2],linea[4]);
                }//termina else

            }//termina else
        }
    }//termina metodo

    private void agregarTablaSimbolos(String lexema, String tipo, String valor)
    {
        int indice;
        if(!lexemas.contains(lexema))    // Si el lexema no existe en la tabla de simbolos
        {
            lexemas.add(lexema);        // lo agrega, con su tipo y valor
            tipos.add(tipo);
            valores.add(valor);
        }else                               // Si si existe
        {
            indice = lexemas.indexOf(lexema);   // Se obtiene el indice de ese lexema
            if(tipos.get(indice).equals(""))    // Si su tipo esta vacio en la tabla
                tipos.add(indice, tipo);            //se lo agrega

            if(valores.get(indice).equals(""))  // Si su valor esta vacio en la tabla
                valores.add(indice,valor);          // se lo agrega
        }
       //CODIGO PARA IMPRIMIR TABLA DE SIMBOLOS
        System.out.println("\nLexema\t\tTipo\t\tValor");
        for (int i = 0; i < lexemas.size(); i++)
        {
            if (lexemas.get(i).length() > 1)
            {
                if (tipos.get(i).length() > 3)
                    System.out.println(lexemas.get(i) + "\t" + tipos.get(i) + "\t" + valores.get(i));
                else
                    System.out.println(lexemas.get(i) + "\t" + tipos.get(i) + "\t\t" + valores.get(i));
            }else if (tipos.get(i).length() > 3)
                    System.out.println(lexemas.get(i) + "\t\t" + tipos.get(i) + "\t" + valores.get(i));
                else
                    System.out.println(lexemas.get(i) + "\t\t" + tipos.get(i) + "\t\t" + valores.get(i));

                /*Para calcular la longitud de los digitos de valor (en caso de que se agregue una columna a la izquierda y sea necesario para darle formato a la salida)*/
                    //length = (int)(Math.log10(valor.get(i))+1) > 1
        }
    }

    private void detectarError(String resul, String variable, String variable1)
    {
    	//paso los 3 lexemas al metodo
    	int indice, indice2, indice3;
    	indice = lexemas.indexOf(variable);
    	indice2 = lexemas.indexOf(variable1);
    	indice3 = lexemas.indexOf(resul);
    	/*if(tipos.get(indice)!=tipos.get(indice2)||tipos.get(indice)!=tipos.get(indice3)||tipos.get(indice2)!=tipos.get(indice3)){
    		if(tipos.get(indice).equals("")||tipos.get(indice2).equals("")||tipos.get(indice3).equals("")){
    			System.out.println("Error de declaracion de variables");
    		}else
    			System.out.println("Error de imcompatibilidad de tipos");
    	}*/
    	
    	//forma 2
    	if(tipos.get(indice).equals(""))
    		System.out.println("Error de declaracion de variable en la variable '"+lexemas.get(indice)+"'");
    	if(tipos.get(indice2).equals(""))
    		System.out.println("Error de declaracion de variable en la variable '"+lexemas.get(indice2)+"'");
    	if(tipos.get(indice3).equals(""))
    		System.out.println("Error de declaracion de variable en la variable '"+lexemas.get(indice3)+"'");
    	else{
    		if(tipos.get(indice)!=tipos.get(indice2))
    			System.out.println("Error de imcompatibilidad de tipos entre la variable '"+lexemas.get(indice)+ "' y la variable '"+lexemas.get(indice2)+"'");
    		if(tipos.get(indice)!=tipos.get(indice3))
    			System.out.println("Error de imcompatibilidad de tipos entre la variable '"+lexemas.get(indice)+ "' y la variable '"+lexemas.get(indice3)+"'");
    		if(tipos.get(indice2)!=tipos.get(indice3))
    			System.out.println("Error de imcompatibilidad de tipos entre la variable '"+lexemas.get(indice2)+ "' y la variable '"+lexemas.get(indice3)+"'");
    	}
    	
		
    }

    public static void main(String[] args)
    {
        new tablaTipos();
    }
}
