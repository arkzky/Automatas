import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
//import java.util.Arrays;

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
       this.imprimirTablaSimbolos();
       
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
                if(lineaActual.length()!=0){
                	instrucciones = lineaActual.trim().split(";");        // Es el arreglo de String que almacena cada instruccion dentro de una linea del texto.txt. trim() elimina tabs y espacios de la linea
                    for (String ins : instrucciones) {
                        lectura.add(ins.trim());                                // Traslada las instrucciones alojadas en el vector "instrucciones" al ArrayList "lectura". trim() elimina tabs y espacios entre instrucciones
                    }
                	//lectura.addAll(Arrays.asList(instrucciones));
                	//System.out.println("Linea leida: "+lineaActual);        // Cada elemento del arrayList es una linea completa del archivo de texto
            	}
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
            if (linea[0].equals("int") || linea[0].equals("String") || linea[0].equals("float") || linea[0].equals("boolean") || linea[0].equals("char"))
            {
                                auxTipo = linea[0];
                if (!l.contains("="))
                {   // SOLO ES DECLARACION
                    for (int i = 1; i < linea.length; i++)
                    {
                        agregarTablaSimbolos(linea[i], auxTipo, "");
                        //System.out.println("entro al declarar "+auxTipo);
                    }//termina for
                    //lineaAux++;
                } else  // ES DECLARACION CON ASIGNACION U OPERACION
                    {
                        if (isNumeric(linea[3]))
                        {
                            //Se esta asignando un numero
                            agregarTablaSimbolos(linea[1], auxTipo, linea[3]);
                            if (tipos.get(lexemas.indexOf(linea[1])).equals(""))
                            {
                                System.out.println("Error de variable indefinida en '" + lexemas.get(lexemas.indexOf(linea[1])) +  "'\nInstruccion: " + l + "\n");
                            }
                            //lineaAux++;
                        } else if (linea[3].equals("true") || linea[3].equals("false"))
                                {   // Se asigna un booleano
                                    agregarTablaSimbolos(linea[1], auxTipo, linea[3]);
                                    //lineaAux++;
                                } else if (linea[3].startsWith("'") && linea[3].endsWith("'"))
                                        {   // es un char
                                            // quitar comillas: String.valueOf(linea[3].charAt(1))
                                            agregarTablaSimbolos(linea[1], auxTipo, linea[3]);
                                            //lineaAux++;
                                        } else if (linea[3].startsWith(Character.toString('"')))
                                                {   // es String
                                                    agregarTablaSimbolos(linea[1], auxTipo, linea[3]);
                                                    //lineaAux++;
                                                } else if (l.contains("+") || l.contains("-") || l.contains("/") || l.contains("*"))
                                                        {
                                                            //Se esta realizando una operacion
                                                            agregarTablaSimbolos(linea[1], auxTipo, "");
                                                            agregarTablaSimbolos(linea[3], "", "");
                                                            agregarTablaSimbolos(linea[5], "", "");
                                                            //lineaAux++;
                                                            detectarError(linea[1], linea[3], linea[5], l);

                                                        }//termina else
                                                        else    // ERROR
                                                        {
                                                            System.out.println("Error lexico: " + linea[3]);
                                                        }
                    }
            }//termina if
            else{   // NO EXISTE DECLARACION (Es asignacion u operacion)
                    if (isNumeric(linea[2])) {
                        //Se esta asignando un numero
                        agregarTablaSimbolos(linea[0], "", linea[2]);

                        if (tipos.get(lexemas.indexOf(linea[0])).equals("")) {
                            System.out.println("Error de variable indefinida en '" + lexemas.get(lexemas.indexOf(linea[0])) + "'\nInstruccion: " + l + "\n");
                        }
                        //lineaAux++;
                        } else if (linea[2].equals("true") || linea[2].equals("false"))
                                {   // Se asigna un booleano
                                    agregarTablaSimbolos(linea[0], "", linea[2]);
                                    //lineaAux++;
                                } else if (linea[2].startsWith("'") && linea[2].endsWith("'"))
                                        {
                                            // es un char
                                            // quitar comillas: String.valueOf(linea[2].charAt(1))
                                            agregarTablaSimbolos(linea[0], "", linea[2]);
                                            //lineaAux++;
                                        } else if (!isNumeric(linea[2]) && linea[2].startsWith(Character.toString('"')))
                                                {   // es String
                                                    agregarTablaSimbolos(linea[0], "", linea[2]);
                                                    //lineaAux++;
                                                } else if (l.contains("+") || l.contains("-") || l.contains("/") || l.contains("*"))
                                                        {
                                                            //Se esta realizando una operacion
                                                            agregarTablaSimbolos(linea[0], "", "");
                                                            agregarTablaSimbolos(linea[2], "", "");
                                                            agregarTablaSimbolos(linea[4], "", "");
                                                            //lineaAux++;
                                                            detectarError(linea[0], linea[2], linea[4], l);

                                                        }//termina else
                                                        else    // ERROR
                                                        {
                                                            System.out.println("Error lexico: " + linea[2]);
                                                        }

            }//termina else
        }
    }//termina metodo

    private void agregarTablaSimbolos(String lexema, String tipo, String valor)
    {
        int indice;
        //System.out.println(valor);
        if(!lexemas.contains(lexema))    // Si el lexema no existe en la tabla de simbolos
        {
            lexemas.add(lexema);        // lo agrega, con su tipo y valor
            tipos.add(tipo);
            valores.add(valor);
            //System.out.println("a "+lexema+" "+tipo+" "+valor);
        }else                               // Si si existe
        {
            indice = lexemas.indexOf(lexema);   // Se obtiene el indice de ese lexema

            if(tipos.get(indice).equals(""))    // Si su tipo esta vacio en la tabla
            {
                tipos.add(indice, tipo);            //se lo agrega
                tipos.remove(indice+1);     // osea, la siguiente (el anterior valor)
            }

            if(valores.get(indice).equals("")) {  // Si su valor esta vacio en la tabla
                valores.add(indice, valor);          // se lo agrega
                valores.remove(indice+1);    // osea, la siguiente (el anterior valor)
            }
        }
    }

    private void detectarError(String resul, String variable, String variable1, String instruccion)
    {
    	//paso los 3 lexemas al metodo
    	int indice, indice2, indice3;
    	indice = lexemas.indexOf(variable);
    	indice2 = lexemas.indexOf(variable1);
    	indice3 = lexemas.indexOf(resul);

    	if(tipos.get(indice).equals(""))
    		System.out.println("Error de variable indefinida en '"+lexemas.get(indice)+"'\nInstruccion: "+instruccion+"\n");
    	if(tipos.get(indice2).equals(""))
    		System.out.println("Error de variable indefinida en '"+lexemas.get(indice2)+"'\nInstruccion: "+instruccion+"\n");
    	if(tipos.get(indice3).equals(""))
    		System.out.println("Error de variable indefinida en '"+lexemas.get(indice3)+"'\nInstruccion: "+instruccion+"\n");
    	else {
            if (!tipos.get(indice).equals(tipos.get(indice2)))
                System.out.println("Error de incompatibilidad de tipos entre la variable '" + lexemas.get(indice) + "' de tipo " + tipos.get(indice) + " y la variable '" + lexemas.get(indice2) + "' de tipo " + tipos.get(indice2) + "\nInstruccion: " + instruccion+"\n");
            if (!tipos.get(indice).equals(tipos.get(indice3)))
                System.out.println("Error de incompatibilidad de tipos entre la variable '" + lexemas.get(indice) + "' de tipo " + tipos.get(indice) + " y la variable '" + lexemas.get(indice3) + "' de tipo " + tipos.get(indice3) + "\nInstruccion: " + instruccion+"\n");
            if (!tipos.get(indice2).equals(tipos.get(indice3))) {
                System.out.println("Error de incompatibilidad de tipos entre la variable '" + lexemas.get(indice2) + "' de tipo " + tipos.get(indice2) + " y la variable '" + lexemas.get(indice3) + "' de tipo " + tipos.get(indice3) + "\nInstruccion: " + instruccion+"\n");
            }
        }
    }

    private void imprimirTablaSimbolos() {
        //CODIGO PARA IMPRIMIR TABLA DE SIMBOLOS
        int maxL = 0;

        for (String lex : lexemas) {
            if(maxL < lex.length()) {
                maxL = lex.length();
            }
        }


        if(maxL > 19)
        {
            System.out.printf("%"+(((maxL+25)/2)+9)+"s","TABLA DE SIMBOLOS");                                       //Posicion dinamica: 25 es la distancia que hay entre tipo y el final de la palabra valor. /2 para que este en medio, pero como ademas debe estar centrado + 9 que es la mitad de la longitud de "TABLA DE SIMBOLOS"
            System.out.printf("%-"+(maxL+2)+"s%-20s%s","\nLEXEMA","TIPO","VALOR\n");
            for (int i = 0; i < tipos.size(); i++)
            {
                System.out.printf("%-"+(maxL+1)+"s%-20s%s%s",lexemas.get(i), tipos.get(i), valores.get(i),"\n");
            }
        }
        else
        {
            System.out.printf("%30s","TABLA DE SIMBOLOS");
            System.out.printf("%-21s%-20s%s","\nLEXEMA","TIPO","VALOR\n");
            for (int i = 0; i < tipos.size(); i++)
            {
                System.out.printf("%-20s%-20s%s%s",lexemas.get(i), tipos.get(i), valores.get(i),"\n");
            }
        }
    }

    public static void main(String[] args)
    {
        new tablaTipos();
    }
}
