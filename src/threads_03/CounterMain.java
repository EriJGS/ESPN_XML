/* Desarrollo III (10 febrero 2021) - Tarea 03: Threads synchronized

Desarrollar un programa que reciba a través de sus argumentos una lista de archivos de texto y cuente
cuantas líneas tiene el archivo, así como el total de lineas para todos los archivos. Crear un thread
para procesar cada archivo considerando los siguientes casos:

- Obtener el tiempo que tarda en contar las líneas utilizando Counter.java para acumular el 
  total de líneas en los archivos.
- Obtener el tiempo que tarda en contar las líneas utilizando SynchronizedCounter.java para
  acumular el total de lineas en los archivos.

 ----- Tiempos de ejecución ------
| Versión normal       | 71milis  |
| Versión sincronizada | 99milis  |
 ---------------------------------

 */

package threads_03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erica Guzmán
 */
public class CounterMain implements Runnable {
    
    // Variables que almacenan los totales por todos los archivos
    private static int threadsTerminados = 0;
    long total  = 0;
                    
    String nombre;
    int args;
    long startTime;
    Counter myCounter;
    
    public CounterMain(Counter myCounter, String nombre, int args, long startTime) {
        this.nombre = nombre;
        this.args = args;
        this.startTime = startTime;
        this.myCounter = myCounter;
    }

    @Override
    public void run() {
        try {
            File archivo = new File(nombre);
            cuantos(archivo, args);
        } catch (IOException ex) {
            Logger.getLogger(CounterMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Método para calcular cuántas líneas, caracteres y palabras hay en el archivo
    void cuantos(File archivo, int args) throws IOException {
        int totalLineas = 0;
        
        try {
            if (archivo.exists()) {
                
                FileReader fr = new FileReader(archivo);
                BufferedReader br = new BufferedReader(fr);

                while (true) {
                    String siguiente = br.readLine();                    // Leer lo que está en la siguiente línea
                    if (siguiente == null) {
                        break;                                           // Si en la línea siguiente no hay nada, sale
                    }
                    totalLineas++;                                       // Si hay algo, incrementa el total
                }
                br.close();
                fr.close();

                System.out.printf("Archivo %s \nLineas: %d\n\n", archivo, totalLineas);
            }            
        } catch (IOException e ) {
            System.err.println(e.getMessage());
        }
        threadsTerminados++;
        
        // acumular totales de cada archivo
        myCounter.add(totalLineas);
        total = myCounter.get();
        
        if (threadsTerminados == args) {
            long endTime = System.currentTimeMillis();
            System.out.printf("Lineas totales: %d\n\nTiempo de ejecución: %dmilis\n", total, (endTime - startTime));
        }
    }
    
    public static void main(String[] args) {
        if (args.length > 0) {
            Counter myCounter = new Counter();  // Instanciar clase donde acumularemos las líneas

            long startTime = System.currentTimeMillis();
            for (String arg : args) {                                                               // Para cada archivo
                Thread hilo = new Thread(new CounterMain(myCounter, arg, args.length, startTime));  // Crear un thread
                hilo.start();                                                                       // Ejecutar cada thread
            }
        } else {
            System.err.println("Faltan argumentos!");
        }
        
    }

}
