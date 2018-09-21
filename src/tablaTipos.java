import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class tablaTipos {

    public final String ARCHIVO = System.getProperty("user.home") + "\\Desktop\\"; // Codigo que detecta el escritorio
    File escritorio = new File(ARCHIVO + "texto.txt");                   // Agrega el nombre del archivo
    String lineaActual = "";

    //Constructor
    public tablaTipos() {

        this.entrada();
    }

    //Lectura de datos de archivo de texto
    public void entrada(){
        BufferedReader br = null;
        FileReader fr = null;

        try{
            fr = new FileReader(escritorio);
            br = new BufferedReader(fr);

            while((lineaActual = br.readLine()) != null){
                System.out.println(lineaActual);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        finally {
            try{
                if(br != null)
                    br.close();
                if(fr != null)
                    fr.close();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void tablaTipos()
    {
        ArrayList<String> lexema = new ArrayList<>();
        ArrayList<String> tipos = new ArrayList<>();
        ArrayList<Object> valor = new ArrayList<>();
        int max;

        System.out.println("Lexema\tTipo\tValor");
        lexema.add("x");
        lexema.add("y");
        lexema.add("z");
        lexema.add("suma");

        tipos.add("int");
        tipos.add("String");
        tipos.add("boolean");
        tipos.add("float");

        valor.add(80);
        valor.add("hola");
        valor.add(true);
        valor.add(80.53);


        max = Math.max( Math.max(lexema.size(),tipos.size()), valor.size());    // Sirve para calcular cual de los 3 arraylist tiene mas datos y usar ese numero para el for
        //MARCA ERROR SI LA CANTIDAD DE DATOS NO ES LA MISMA PARA LAS 3 COLUMNAS, FALTA MANEJAR EXCEPCIONES

        for (int i = 0; i < max; i++) {
            if(lexema.get(i).length() > 1)
                if(tipos.get(i).length() > 3)
                    System.out.println(lexema.get(i) + "\t" + tipos.get(i) + "\t" + valor.get(i));
                else
                    System.out.println(lexema.get(i) + "\t" + tipos.get(i) + "\t\t" + valor.get(i));
            else
            if(tipos.get(i).length() > 3)
                System.out.println(lexema.get(i) + "\t\t" + tipos.get(i) + "\t" + valor.get(i));
            else
                System.out.println(lexema.get(i) + "\t\t" + tipos.get(i) + "\t\t" + valor.get(i));
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
                System.out.println(lexema[i] + "\t\t" + tipo[i] + "\t\t" + valor[i]);
        }*/