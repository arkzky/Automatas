import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class tablaTipos {

    private File archivo;                                                       // Objeto que contiene la ruta completa, con el nombre del archivo
    private ArrayList<String> lectura, lexemas, tipos, valores;                    // Cadena que contendrá cada linea del archivo de texto
    private String lineaActual;                                                    // Es la cadena que almacenara cada linea del texto.txt
    private int contadorExprBool, noLinea;

    //Constructor no nulo
    private tablaTipos() {
        lectura = new ArrayList<>();                                                // inicializacion
        lexemas = new ArrayList<>();
        tipos = new ArrayList<>();
        valores = new ArrayList<>();
        lineaActual = "";                                                           //
        contadorExprBool = 0;
        noLinea = 0;

        this.crearArchivo();
        this.entrada();                                                             // llamada al metodo entrada()
        this.sintactico();
        this.optimizacion();
        this.triplo();
        this.imprimirTablaSimbolos();
    }

    // Creacion de archivo a leer si no existe
    private void crearArchivo(){
        try {
            String rutaAbsoluta = System.getProperty("user.home") + "\\Desktop\\entrada.txt";        // Constante que aloja la ruta del archivo a leer
            archivo = new File(rutaAbsoluta);                     // Agrega el nombre del archivo
            if(archivo.createNewFile()){
                System.out.println("El archivo "+rutaAbsoluta+" fue creado satisfactoriamente");

                try {
                    FileWriter escribir = new FileWriter(rutaAbsoluta, true);
                    PrintWriter imprimeLinea = new PrintWriter(escribir);
                    imprimeLinea.printf("%s" + "%n", "int x;");
                    imprimeLinea.printf("%s" + "%n", "boolean y;");
                    imprimeLinea.printf("%s" + "%n", "while ( x != y ){");
                    imprimeLinea.printf("%s" + "%n", "x = x - y;");
                    imprimeLinea.printf("%s" + "%n", "}");
                    imprimeLinea.printf("%s" + "%n", "int c = 0;");
                    imprimeLinea.printf("%s", "a =  0 + b;");
                    imprimeLinea.close();
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }else{
                System.out.println("El archivo "+rutaAbsoluta+" ya existe");
            }
            this.abrirArchivo(rutaAbsoluta);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void crearArchivo(String [] instrucciones, String nombreArchivo)
    {
        try {
            String rutaAbsoluta = System.getProperty("user.home") + "\\Desktop\\"+nombreArchivo+".txt";        // Constante que aloja la ruta del archivo a leer
            archivo = new File(rutaAbsoluta);
            if(archivo.createNewFile()) {
                System.out.println("El archivo " + rutaAbsoluta + " fue creado satisfactoriamente");
            }else{
                System.out.println("El archivo " + rutaAbsoluta + " fue actualizado satisfactoriamente");
            }
            try {
                FileWriter escribir = new FileWriter(rutaAbsoluta, false);
                PrintWriter imprimeLinea = new PrintWriter(escribir);

                for (int i = 0; i < instrucciones.length; i++){
                    if(nombreArchivo.equals("triplo"))
                    {
                        imprimeLinea.printf("%-3d%s" + "%n",(i+1) , instrucciones[i]);
                        if(i == instrucciones.length-1)
                        {
                            imprimeLinea.printf("%-3d",(i+2));
                        }
                    }else if(instrucciones[i].contains("while") || instrucciones[i].contains("}"))
                            {
                                imprimeLinea.printf("%s" + "%n", instrucciones[i]);
                            }else
                            {
                                imprimeLinea.printf("%s%s" + "%n", instrucciones[i], ";");
                            }
                }
                imprimeLinea.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
            this.abrirArchivo(rutaAbsoluta);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void abrirArchivo(String ruta) throws IOException {
        Runtime rt = Runtime.getRuntime();
        rt.exec("notepad "+ruta);
    }

    //Lectura de datos de archivo de texto
    private void entrada() {
        BufferedReader br = null;
        FileReader fr = null;
        String[] instrucciones;

        try {
            fr = new FileReader(archivo);                                        // Se especifica el archivo a leer
            br = new BufferedReader(fr);                                            // Se prepara el metodo de lectura de archivos

            while ((lineaActual = br.readLine()) != null)                           // Asigna la linea leida a lineaActual, mientras no devuelva nulo, sigue leyendo
            {
                //if(lineaActual.length()!=0){
                	instrucciones = lineaActual.trim().split(";");            // Es el arreglo de String que almacena cada instruccion dentro de una linea del texto.txt. trim() elimina tabs y espacios de la linea
                    for (String ins : instrucciones) {                              // Cada elemento del arrayList es una linea completa del archivo de texto
                        lectura.add(ins.trim());                                    // Traslada las instrucciones alojadas en el vector "instrucciones" al ArrayList "lectura". trim() elimina tabs y espacios entre instrucciones
                    }
            	//}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {                                                                 // finally sirve para indicar las instrucciones que se realizaran independientemente
            try {                                                                   //   de si existio o no una excepción antes (el anterior try catch)
                if (br != null)
                    br.close();                                                     // termina el proceso del BufferedReader
                if (fr != null)
                    fr.close();                                                     // termina el proceso del FileReader
            } catch (IOException ex) {
                ex.printStackTrace();                                               // si hubo excepcion imprimira un diagnostico de la pila
            }
        }
    }

    //Separador de palabras
    private String[] separador(String linea)                                        //Para usarlo, utiliza ArrayList lectura.get(i); para obtener el string que deseas separar
    {
        return linea.split("[, ]+");                                         // regex que omite comas, espacios y punto y comas, tantas como sean
    }

    //Converssion de Arreglo de palabras a lineas de instrucicones (;)
    private void palabrasALinea(String[] palabras, StringBuilder instruccion )
    {
        for (String p : palabras) {                             // Este for es necesario porque actualmente palabras tiene espacios vacios de más: "a =    0;"
            instruccion.append(p);                              // Y  este String builder los junta en un solo string
            if (instruccion.length() > 0) {
                instruccion.append(" ");
            }
        }
        palabras = instruccion.toString().split(" +");   // Se eliminan absolutamente todos los espacios
        instruccion.delete(0, instruccion.length());            // Se resetea el valor de instrucicones a vacio

        for (int p = 0; p < palabras.length; p++) {             // Se agregan espacios a la derecha y cuando este en la ultima palabra, agrega salto de linea
            if (instruccion.length() > 0) {
                instruccion.append(" ");
            }
            instruccion.append(palabras[p]);
            if (p == palabras.length - 1) {
                instruccion.append("\n");
            }
        }
    }

    //Metodo auxiliar para detectar numeros reales
    private boolean isNumeric(String str) {
        int cont = 0;                                                              // Contador que detectara los puntos analizados
        for (char c : str.toCharArray()) {
            if(c == '.' && cont < 1)                                               // asi detectara a los numeros de punto flotante, pero solo admitira un punto
            {                                                                      // si lee otro, se ira al else y retornara falso porque el punto no es digito
                cont++;                                                            // aumenta el contadorExprBool para detectar cuantas veces ya analizo un punto
            } else if(!Character.isDigit(c))                                       // Si no es un numero
                return false;
        }
        return true;                                                               // Si logro analizar el string sin problemas, retorna que si es digito
    }

    private boolean isInt(String str){
        for (char c : str.toCharArray()){
            if(!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isFloat(String str){
        return isNumeric(str) && str.contains(".");
    }

    private void verificador(String linea)
    {
        if( isInt( linea ) )
        {
            agregarTablaSimbolos(linea,"int", linea);
        }else if(isFloat( linea) )
        {
            agregarTablaSimbolos(linea,"float", linea);
        }else if (linea.equals("true") || linea.equals("false"))
        {
            agregarTablaSimbolos(linea, "boolean", linea);
        } else { // fue un lexema
            agregarTablaSimbolos(linea,"","");
        }
    }

    private void sintactico() {
        String auxTipo;
        String[] linea;                                                            // Linea contiene strings de cada linea
        for (String l : lectura) {                                                 // Conversion de las lineas completas a elementos almacenados en un vector
            noLinea++;
            if(l.equals("{") || l.equals("}") || l.equals(""))
            {
                continue;
            }
            linea = separador(l);                                                  // Separa cada instruccion contenida en lectura y se lo asigna a linea
            if (linea[0].equals("int") || linea[0].equals("String") || linea[0].equals("float") || linea[0].equals("boolean") || linea[0].equals("char")) //Si es palabra reservada
            {
                auxTipo = linea[0];                                                // Se almacena el tipo en una variable local para facilidad de uso
                if (!l.contains("="))                                              // Si la instruccion no contiene un "="
                {   // SOLO ES DECLARACION    Ej. int x;                           // Es una declaracion simple
                    for (int i = 1; i < linea.length; i++)
                    {
                        agregarTablaSimbolos(linea[i], auxTipo, "");         // Se almacena todos los lexemas con el tipo de datos correspondientes.
                    }//termina for
                    //lineaAux++;
                } else  // SINO, ES DECLARACION CON ASIGNACION U OPERACION  Ej. int x = 1; o int x = a + b;
                    {
                        if (isNumeric(linea[3]))                                   // Si el elemento despues de la igualdad es un numero
                        {   //Se esta asignando un numero
                            agregarTablaSimbolos(linea[1], auxTipo, linea[3]);     // Se agrega a la tabla de simbolos. Linea[1] es el lexema
                            if (tipos.get(lexemas.indexOf(linea[1])).equals(""))   // Si el tipo del lexema que se agregó es vacio, ocurre un error semantico
                            {
                                System.out.println("Error de variable indefinida en '" + lexemas.get(lexemas.indexOf(linea[1])) +  "'\nInstruccion: " + l + " en la linea "+noLinea+"\n");
                            }
                        } else if (linea[3].equals("true") || linea[3].equals("false"))         // Si el valor despues de la igualdad es "true" o "false"
                                {   // Se asigna un booleano
                                    agregarTablaSimbolos(linea[1], auxTipo, linea[3]);          // Se agrega a la tabla de simbolos
                                    //lineaAux++;
                                } else if (linea[3].startsWith("'") && linea[3].endsWith("'"))          // Si el elmento despues de la igualdad empieza y termina con comillas simples
                                        {   // es un char
                                            // quitar comillas: String.valueOf(linea[3].charAt(1))
                                            agregarTablaSimbolos(linea[1], auxTipo, linea[3]);          // Se agrega a la tabla de simbolos
                                            //lineaAux++;
                                        } else if (linea[3].startsWith(Character.toString('"')) && linea[3].endsWith(Character.toString('"'))) // Si el elemento despues de la igualdad esta entre comillas dobles
                                                {   // es String
                                                    agregarTablaSimbolos(linea[1], auxTipo, linea[3]);  // Se agrega a la tabla de simbolos
                                                    //lineaAux++;
                                                } else if (l.contains("+") || l.contains("-") || l.contains("/") || l.contains("*"))    // Si la instruccion contiene un operador aritmetico básico
                                                        {
                                                            //Se esta realizando una operacion
                                                            agregarTablaSimbolos(linea[1], auxTipo, "");                          // Se agregan los 3 elementos a la tabla de tipos. El tipo de la primer variable es conocida, porque se declaro al principio
                                                            agregarTablaSimbolos(linea[3], "", "");
                                                            agregarTablaSimbolos(linea[5], "", "");
                                                            //lineaAux++;
                                                            detectarError(linea[1], linea[3], linea[5], l);

                                                        }//termina else
                                                        else    // ERROR
                                                        {
                                                            System.out.println("Error lexico: " + linea[3]+" en la linea "+noLinea);
                                                        }
                    }
            }//termina if
            else if(linea[0].toLowerCase().equals("while"))
                { // es un while
                    // while(x==1)
                    if(!linea[2].equals("("))
                    {
                        verificador(linea[2]);
                        if(linea[3].equals("==") || linea[3].equals(">") || linea[3].equals("<") || linea[3].equals(">=") || linea[3].equals("<=") || linea[3].equals("!="))
                        {
                            verificador(linea[4]);
                            detectarError(linea[2],linea[4], l);
                        }
                        if(linea[3].equals("&&") || linea[3].equals("||"))
                        {
                            if(linea[4].equals("("))
                            {
                                String tipo = " ", lexema;
                                verificador(linea[5]);
                                verificador(linea[7]);
                                detectarError(linea[5], linea[7], l);
                                if(linea[6].equals("==") || linea[6].equals(">") || linea[6].equals("<") || linea[6].equals(">=") || linea[6].equals("<=") || linea[6].equals("!=")) {
                                    tipo = "boolean";
                                }
                                contadorExprBool++;
                                lexema = "ExprBool ("+contadorExprBool+")";
                                agregarTablaSimbolos( lexema , tipo , linea[5]+linea[6]+linea[7]);
                                detectarError(linea[2], lexema, l);
                            }
                            else
                            {
                                verificador(linea[4]);
                                detectarError( linea[2], linea[4], l );
                            }

                        }
                    }else
                    {
                        String tipo1 = " ", tipo2 = " ", lexema1, lexema2;
                        verificador(linea[3]);
                        verificador(linea[5]);
                        detectarError(linea[3], linea[5], l);
                        if(linea[4].equals("==") || linea[4].equals(">") || linea[4].equals("<") || linea[4].equals(">=") || linea[4].equals("<=") || linea[4].equals("!="))
                        {
                            tipo1 = "boolean";
                        }
                        contadorExprBool++;
                        lexema1 = "ExprBool (" + contadorExprBool + ")";
                        agregarTablaSimbolos(lexema1,tipo1,linea[3]+linea[4]+linea[5]);
                            if(linea[7].equals("&&") || linea[7].equals("||"))
                            {
                                if(linea[8].equals("("))
                                {
                                    verificador(linea[9]);
                                    verificador(linea[11]);
                                    detectarError(linea[9], linea[11], l);
                                    if(linea[10].equals("==") || linea[10].equals(">") || linea[10].equals("<") || linea[10].equals(">=") || linea[10].equals("<=") || linea[10].equals("!="))
                                    {
                                        tipo2 = "boolean";
                                    }
                                    contadorExprBool++;
                                    lexema2 = "ExprBool ("+ contadorExprBool + ")";
                                    agregarTablaSimbolos(lexema2, tipo2, linea[9]+linea[10]+linea[11]);
                                    detectarError(lexema1,lexema2,l);
                                }else{
                                    verificador(linea[8]);
                                    detectarError(lexema1, linea[8], l);
                                }
                            }

                    }
                    /* ORDEN:   0 -> while
                                1-> (
                                    2-> X o 1 o True
                                        3 -> logico ("&&" o "||")
                                                4 -> X o 1 | True | False
                                                        5 -> )
                                                4 -> (
                                                    5 -> X o 1
                                                        6 -> relacional (==, >, <, <=, >=)
                                                            7 -> X o 1
                                                                8 -> )
                                                                    9 -> )
                                        3 -> relacional (==, >, <, <=, >=)
                                                4 -> X o 1 o True o False
                                                    5 -> )

                                    2 -> (
                                        3 ->  X o 1 ->
                                                4 -> relacional (==, >, <, <=, >=)
                                                    5 -> X | 1
                                                        6 -> )
                                                            7 -> logico("&&" o "||")
                                                                8 -> X
                                                                    9 -> )
                                                                8 -> (
                                                                    9 -> X o 1
                                                                        10 -> relacional (==, >, <, <=, >=)
                                                                            11 -> X o 1
                                                                                12 -> )
                                                                                    13 -> )
                    while(x==1)
                    while(x==true)
                    while((x<7)||(y<4))
                    while((x<7)||x)
                    while(x<2)
                    while(x||(x<3))
                    while(x||True)
                    while(True ||(y<3))
                    */

                }else
                    {   // NO EXISTE DECLARACION (Es asignacion u operacion)    Ej. x = 1; o x = a + b;
                        if (isNumeric(linea[2])) {
                            //Se esta asignando un numero
                            agregarTablaSimbolos(linea[0], "", linea[2]);

                            if (tipos.get(lexemas.indexOf(linea[0])).equals("")) {
                                System.out.println("Error de variable indefinida en '" + lexemas.get(lexemas.indexOf(linea[0])) + "'\nInstruccion: " + l +" en la linea "+noLinea+"\n");
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
                                                                System.out.println("Error lexico: " + linea[2]+" en la linea "+noLinea);
                                                            }

                }//termina else
        }
    }//termina metodo

    private void agregarTablaSimbolos(String lexema, String tipo, String valor)
    {
        int indice;
        if(!lexemas.contains(lexema))                                                // Si el lexema no existe en la tabla de simbolos
        {
            lexemas.add(lexema);                                                     // lo agrega, con su tipo y valor
            tipos.add(tipo);
            valores.add(valor);
        }else                                                                        // Si existe
        {
            indice = lexemas.indexOf(lexema);                                        // Se obtiene el indice de ese lexema

            if(tipos.get(indice).equals(""))                                         // Si su tipo esta vacio en la tabla
            {
                tipos.add(indice, tipo);                                             //se lo agrega
                tipos.remove(indice+1);                                        // osea, la siguiente (el anterior valor)
            }

            if(valores.get(indice).equals("")) {                                     // Si su valor esta vacio en la tabla
                valores.add(indice, valor);                                          // se lo agrega
                valores.remove(indice+1);                                      // osea, la siguiente (el anterior valor)
            }
        }
    }

    private void detectarError(String resul, String variable, String variable1, String instruccion)         // DETECTA ERRORES CUANDO SE ASIGNAN OPERACIONES
    {
    	int indice, indice2, indice3;                                                                       //Se pasan los 3 lexemas al metodo
    	indice = lexemas.indexOf(variable);
    	indice2 = lexemas.indexOf(variable1);
    	indice3 = lexemas.indexOf(resul);

    	if(tipos.get(indice).equals(""))                                                                    // Si el tipo de las variables es vacio, ocurre un error
    		System.out.println("Error de variable indefinida en '"+lexemas.get(indice)+"'\nInstruccion: "+instruccion+" en la linea "+noLinea+"\n");
    	if(tipos.get(indice2).equals(""))
    		System.out.println("Error de variable indefinida en '"+lexemas.get(indice2)+"'\nInstruccion: "+instruccion+" en la linea "+noLinea+"\n");
    	if(tipos.get(indice3).equals(""))
    		System.out.println("Error de variable indefinida en '"+lexemas.get(indice3)+"'\nInstruccion: "+instruccion+" en la linea "+noLinea+"\n");
    	else {
            if (!tipos.get(indice).equals(tipos.get(indice2)))                                              // Si el tipo de la variable 1 es diferente al de la variable 2, ocurre un error
                System.out.println("Error de incompatibilidad de tipos entre la variable '" + lexemas.get(indice) + "' de tipo " + tipos.get(indice) + " y la variable '" + lexemas.get(indice2) + "' de tipo " + tipos.get(indice2) + "\nInstruccion: " + instruccion+" en la linea "+noLinea+"\n");
            if (!tipos.get(indice).equals(tipos.get(indice3)))
                System.out.println("Error de incompatibilidad de tipos entre la variable '" + lexemas.get(indice) + "' de tipo " + tipos.get(indice) + " y la variable '" + lexemas.get(indice3) + "' de tipo " + tipos.get(indice3) + "\nInstruccion: " + instruccion+" en la linea "+noLinea+"\n");
            if (!tipos.get(indice2).equals(tipos.get(indice3))) {
                System.out.println("Error de incompatibilidad de tipos entre la variable '" + lexemas.get(indice2) + "' de tipo " + tipos.get(indice2) + " y la variable '" + lexemas.get(indice3) + "' de tipo " + tipos.get(indice3) + "\nInstruccion: " + instruccion+" en la linea "+noLinea+"\n");
            }
        }
    }

    private void detectarError(String variable, String variable1, String instruccion)         // DETECTA ERRORES CUANDO SE EVALUA EN WHILE
    {
        int indice, indice2;                                                                       //Se pasan los 3 lexemas al metodo
        indice = lexemas.indexOf(variable);
        indice2 = lexemas.indexOf(variable1);

        if(tipos.get(indice).equals(""))                                                                    // Si el tipo de las variables es vacio, ocurre un error
            System.out.println("Error de variable indefinida en '"+lexemas.get(indice)+"'\nInstruccion: "+instruccion+" en la linea "+noLinea+"\n");
        if(tipos.get(indice2).equals(""))
            System.out.println("Error de variable indefinida en '"+lexemas.get(indice2)+"'\nInstruccion: "+instruccion+" en la linea "+noLinea+"\n");
        else {
            if (!tipos.get(indice).equals(tipos.get(indice2)))                                              // Si el tipo de la variable 1 es diferente al de la variable 2, ocurre un error
                System.out.println("Error de incompatibilidad de tipos entre el elemento '" + lexemas.get(indice) + "' de tipo " + tipos.get(indice) + " y el elemento '" + lexemas.get(indice2) + "' de tipo " + tipos.get(indice2) + "\nInstruccion: " + instruccion+" en la linea "+noLinea+"\n");
        }
    }

    private void imprimirTablaSimbolos() {                                                                  //CODIGO PARA IMPRIMIR TABLA DE SIMBOLOS
        int maxL = 0;                                                                                       // Se calcula el string mas largo dentro del ArrayList lexemas
        for (String lex : lexemas) {
            if(maxL < lex.length()) {
                maxL = lex.length();
            }
        }

        if(maxL > 19)                                                                                       // Si el string mas largo dentro de lexemas es mayor a 19
        {
            System.out.printf("%"+(((maxL+25)/2)+9)+"s","TABLA DE SIMBOLOS");                               // Se imprime dinamicamente         //Posicion dinamica: 25 es la distancia que hay entre tipo y el final de la palabra valor. /2 para que este en medio, pero como ademas debe estar centrado + 9 que es la mitad de la longitud de "TABLA DE SIMBOLOS"
            System.out.printf("%-"+(maxL+2)+"s%-20s%s","\nLEXEMA","TIPO","VALOR\n");
            for (int i = 0; i < tipos.size(); i++)
            {
                System.out.printf("%-"+(maxL+1)+"s%-20s%s%s",lexemas.get(i), tipos.get(i), valores.get(i),"\n");
            }
        }
        else
        {
            System.out.printf("%30s","TABLA DE SIMBOLOS");                                                  // Sino, se imprime a un tamaño fijo
            System.out.printf("%-21s%-20s%s","\nLEXEMA","TIPO","VALOR\n");
            for (int i = 0; i < tipos.size(); i++)
            {
                System.out.printf("%-20s%-20s%s%s",lexemas.get(i), tipos.get(i), valores.get(i),"\n");
            }
        }
    }

    private void optimizacion()
    {
        ArrayList<String> ceros = new ArrayList<>();
        ArrayList<String> unos = new ArrayList<>();
        ArrayList<String> salvar = new ArrayList<>();
        String [] palabras, instrucciones;
        StringBuilder instruccion = new StringBuilder();
        int contCiclos = 0;

        for (int i = 0; i < lexemas.size(); i++) {
            if (valores.get(i).equals("0")) {
                ceros.add(lexemas.get(i));
            }

            if (valores.get(i).equals("1")) {
                unos.add(lexemas.get(i));
            }
        }

        for (String l : lectura) {
            palabras = separador(l);

            if (palabras[0].equals("while")) {
                contCiclos++;
            }

            if (palabras[0].equals("}")) {
                contCiclos--;
            }

            if (ceros.contains(palabras[0]) && contCiclos > 0) {
                if (palabras[3].equals("+") || palabras[3].equals("-")) {
                    salvar.add(palabras[0]);
                }
            }
        }

        for (String l : lectura) {
            palabras = separador(l);

            for (int j = 0; j < palabras.length; j++) {

                if (palabras[0].equals("while")) {
                    contCiclos++;
                }

                if (palabras[0].equals("}")) {
                    contCiclos--;
                }

                if (j >= 2)                                        // Solo evalua cuando llega al indice 2
                {
                    if (ceros.contains(palabras[j]) && !(salvar.contains(palabras[j]))) {
                        if (palabras[j - 1].equals("-") || palabras[j - 1].equals("+"))  // LADO IZQUIERDO
                        {
                            palabras[j] = "";
                            palabras[j - 1] = "";

                        }

                        if (j != 4) {
                            if (palabras[j + 1].equals("-") || palabras[j + 1].equals("+"))  // LADO DERECHO
                            {
                                palabras[j] = "";
                                palabras[j + 1] = "";
                            }
                        }
                    }
                    if (unos.contains(palabras[j])) {
                        if (palabras[j - 1].equals("*") || palabras[j - 1].equals("/"))  // LADO IZQUIERDO
                        {
                            palabras[j] = "";
                            palabras[j - 1] = "";
                        }

                        if (j != 4) {
                            if (palabras[j + 1].equals("*"))  // LADO DERECHO
                            {
                                palabras[j] = "";
                                palabras[j + 1] = "";
                            }
                        }
                    }
                    if (palabras[j].equals("+") || palabras[j].equals("-")) {
                        if (palabras[j - 1].equals("0"))  // LADO IZQUIERDO
                        {
                            palabras[j] = "";
                            palabras[j - 1] = "";
                        }

                        if (palabras[j + 1].equals("0"))  // LADO DERECHO
                        {
                            palabras[j] = "";
                            palabras[j + 1] = "";
                        }
                    }

                    if (palabras[j].equals("*")) {
                        if (palabras[j - 1].equals("1"))  // LADO IZQUIERDO
                        {
                            palabras[j] = "";
                            palabras[j - 1] = "";
                        }
                    }
                    if (palabras[j].equals("*")) {
                        if (palabras[j + 1].equals("1"))  // LADO DERECHO
                        {
                            palabras[j] = "";
                            palabras[j + 1] = "";
                        }
                    }

                    if (palabras[j].equals("/")) {
                        if (palabras[j + 1].equals("1"))  // LADO DERECHO
                        {
                            palabras[j] = "";
                            palabras[j + 1] = "";
                        }
                    }
                }
            }
            this.palabrasALinea(palabras,instruccion);                     // Aqui se debe guardar estas palabras como una sola linea de codigo en un String Builder
        }
        instrucciones = instruccion.toString().split("\n");         // Finalmente, separa cada que encuentra un salto de linea, "instrucciones" tiene entonces indexadas cada una de las lineas de codigo
        this.crearArchivo(instrucciones, "optimizacion");
    }

    private void triplo()
    {
        ArrayList <String>  filasTriplo    = new ArrayList<>();
        ArrayList <String>  lineasWhile    = new ArrayList<>();
        ArrayList <String>  salidaSaltos   = new ArrayList<>();
        ArrayList <Integer> posicionSalida = new ArrayList<>();
        String [] palabras, arregloFilasTriplo;
        String JMPFalso, lineaASaltar;
        int posJMPFalso;
        int contadorTemporal = 1;
        int contador = 0;
        boolean band = false;

//      Limpieza de variables, Lectura de nuevo ahora con optimizacion
        lectura.clear();                                                    // Se borra su contenido
        this.entrada();                                                     // Se vuelve a hacer la lectura del ultimo archivo creado, es decir "Optimizacion.txt"

//      Remover declaraciones
        for (int i = 0; i < lectura.size(); i++)
        {
            if(lectura.get(i).contains("int") || lectura.get(i).contains("float") || lectura.get(i).contains("boolean") || lectura.get(i).contains("char") || lectura.get(i).contains("String")) {
                lectura.remove(i);
                i--;
            }
        }

        // Conversion a Triplo
        for (String l : lectura) {
            palabras = separador(l);

            for (int i = 0; i < palabras.length; i++) {
                // Omitir corchetes
                if(palabras[i].equals("&&"))
                    band = true;
                if ( palabras[i].equals("}")) {
                    // Agregar JMP que devuelva a la fila donde se evalua el while
                    filasTriplo.add( "JMP   " + lineasWhile.get( lineasWhile.size() - 1 ));
                    lineasWhile.remove(lineasWhile.size() - 1);
                    salidaSaltos.add( "" + ( filasTriplo.size() + 1 ));

                    // Agregar el numero de linea a los JMP de salida que quedaron pendientes
                    posJMPFalso  = posicionSalida.get( posicionSalida.size() - 1 );
                    lineaASaltar =   salidaSaltos.get(   salidaSaltos.size() - 1 );

                    JMPFalso = filasTriplo.get( posJMPFalso - 1 );

                    filasTriplo.add( posJMPFalso ,JMPFalso +" "+ lineaASaltar );
                    filasTriplo.remove(posJMPFalso - 1 );
                    posicionSalida.remove( posicionSalida.size() - 1 );
                    contador--;

                }

                // Caso de operacion aritmetica: y = a + b
                if (palabras[i].equals("+") || palabras[i].equals("-") || palabras[i].equals("/") || palabras[i].equals("*")) {
                    filasTriplo.add("T" + contadorTemporal + " " + palabras[i - 1] + " =");
                    filasTriplo.add("T" + contadorTemporal + " " + palabras[i + 1] + " " + palabras[i]);
                    filasTriplo.add(palabras[0] + " T" + contadorTemporal + " =");
                    contadorTemporal++;
                }

                // Caso asignaciones:  y = 0;
                if (palabras.length <= 3 && palabras[i].equals("=")) {
                    filasTriplo.add(palabras[i - 1] + " " + palabras[i + 1] + " " + palabras[i]);
                }

                // Caso while
                if (palabras[0].toLowerCase().equals("while")) {
                    if(palabras[i].equals("||")){  //AQUI SE EVALUA ENCONTRAR UN OR
                        if(palabras[i-1].equals(")")){ //EN ESTE CASO SE ENCUENTRA UNA OPERACION RELACIONAL
                        	lineasWhile.add(""+(filasTriplo.size()+1));
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i-4]+" =");
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i-2]+" "+palabras[i-3]);
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+7));
                            filasTriplo.add("TR FALSE "+(filasTriplo.size()+2));
                            contadorTemporal++;
                            
                        }
                        else{ //AQUI NEL, SOLO UN BOOLEAN
                        	lineasWhile.add(""+(filasTriplo.size()+1));
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i-1]+" =");
                            filasTriplo.add("T"+contadorTemporal+"   ==");
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+7));
                            filasTriplo.add("TR FALSE "+(filasTriplo.size()+2));
                            contadorTemporal++;
                            
                        }
                        if(palabras[i+1].equals("(")){ //SI ES UN RELACION A LA DERECHA
                            //filasTriplo.add("T"+contadorTemporal+" "+palabras[i+2]+" =");
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i+4]+" "+palabras[i+3]);
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+3));
                            filasTriplo.add("TR FALSE JUMP");
                            posicionSalida.add(filasTriplo.size());
                            contadorTemporal++;
                            lineasWhile.add(""+filasTriplo.size());
                        }
                        else{//ESTO ES SI ENCUENTRAS UN BOOLEAN A LA DERECHA
                        	//lineasWhile.add(""+(filasTriplo.size()+1));
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i+1]+" =");
                            filasTriplo.add("T"+contadorTemporal+"   ==");
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+3));
                            filasTriplo.add("TR FALSE JUMP");
                            posicionSalida.add(filasTriplo.size());
                            contadorTemporal++;
                            
                        }
                    }
                    if(palabras[i].equals("&&")){  //AQUI SE EVALUA ENCONTRAR UN AND
                        if(palabras[i-1].equals(")")){ //EN ESTE CASO SE ENCUENTRA UNA OPERACION RELACIONAL
                        	lineasWhile.add(""+(filasTriplo.size()+1));
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i-4]+" =");
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i-2]+" "+palabras[i-3]);
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+3));
                            filasTriplo.add("TR FALSE JUMP");
                            posicionSalida.add(filasTriplo.size());
                            contadorTemporal++;
                            
                        }
                        else{ //AQUI NEL, SOLO UN BOOLEAN
                        	lineasWhile.add(""+(filasTriplo.size()+1));
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i-1]+" =");
                            filasTriplo.add("T"+contadorTemporal+"   ==");
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+3));
                            filasTriplo.add("TR FALSE JUMP");
                            posicionSalida.add(filasTriplo.size());
                            //System.out.println(posicionSalida.get(0));
                            contadorTemporal++;
                            
                        }
                        if(palabras[i+1].equals("(")){ //SI ES UN RELACION A LA DERECHA
                        	//lineasWhile.add(""+(filasTriplo.size()+1));
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i+2]+" =");
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i+4]+" "+palabras[i+3]);
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+3));
                            filasTriplo.add("TR FALSE JUMP");
                            posicionSalida.add(filasTriplo.size());
                            contadorTemporal++;
                            
                        }
                        else{//ESTO ES SI ENCUENTRAS UN BOOLEAN A LA DERECHA
                        	//lineasWhile.add(""+(filasTriplo.size()+1));
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i+1]+" =");
                            filasTriplo.add("T"+contadorTemporal+"   ==");
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+3));
                            filasTriplo.add("TR FALSE JUMP");
                            posicionSalida.add(filasTriplo.size());
                            contadorTemporal++;
                            
                        }
                    }
                    if((palabras[i].equals("<")||palabras[i].equals(">")||palabras[i].equals("==")||palabras[i].equals("<=")||palabras[i].equals(">=")||palabras[i].equals("!="))&&palabras[i+2].equals("){")){
                            lineasWhile.add(""+(filasTriplo.size()+1));
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i-1]+" =");
                            filasTriplo.add("T"+contadorTemporal+" "+palabras[i+1]+" "+palabras[i]);
                            filasTriplo.add("TR TRUE "+(filasTriplo.size()+3));
                            filasTriplo.add("TR FALSE JUMP");
                            posicionSalida.add(filasTriplo.size());
                            contadorTemporal++;                       

                    }
                }
            }
        }
        if(band)
        {
            filasTriplo.add( posicionSalida.get(0), filasTriplo.get( posicionSalida.get(0) + 3 ) );
            filasTriplo.remove( posicionSalida.get(0) - 1);
        }
        // Impresion en archivo de texto
        arregloFilasTriplo = new String[filasTriplo.size()];
        arregloFilasTriplo = filasTriplo.toArray(arregloFilasTriplo);
        this.crearArchivo(arregloFilasTriplo,"triplo");

    }
    public static void main(String z[])                                                                     // MAIN
    {
        new tablaTipos();
    }
}