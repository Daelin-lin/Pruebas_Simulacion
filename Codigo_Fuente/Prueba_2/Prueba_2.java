package Prueba_2;
/**Autoría:     Gutiérrez Rivas Alejandro
 *              Herrera Martinez Aylin
 *              Vázquez Alviso Bryan Alexei */
/*
----------------------------------------------------
ENUNCIADO DEL PROBLEMA
10. A un sistema llegan piezas de acuerdo con una distribución uniforme de entre 4 y 10 minutos.
    Las piezas son colocadas en un almacén con capacidad infinita,
    donde esperan a ser inspeccionadas por un operario.
    El tiempo de inspección tiene una distribución exponencial con media de 5 minutos.
    Después de la inspección las piezas pasan a la fila de empaque, con capacidad para 5 piezas.
    El proceso de empaque está a cargo de un operario que tarda 8 minutos con distribución exponencial
    en empacar cada pieza. Posteriormente las piezas salen del sistema.
        a) Simule el sistema por 40 horas.
        b) Identifique dónde se encuentra el cuello de botella.
        c) Genere vistas para cada uno de los procesos por separado.
        d) Incrementar el espacio en el almacén cuesta $5/semana;
           aumentar 10% la velocidad de empaque cuesta $15/semana;
           el costo de incluir otro operario para que se reduzca el tiempo
           de empaque a 5 minutos con distribución exponencial, es de $20/semana.
           Cada unidad producida deja una utilidad de $0.40.
    Con base en esta información, determine qué mejoras podrían hacerse al sistema
    para incrementar su utilidad semanal.
----------------------------------------------------
*/
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Prueba_2 {
    // =====================================
    // VARIABLES GLOBALES
    // =====================================
   static final double TIEMPO_SIMULACION = 2400.0;
   static double[] riLlegadas;
   static double[] riInspeccion;
   static double[] riEmpaque;
   static int indiceLlegadas = 0;
   static int indiceInspeccion = 0;
   static int indiceEmpaque = 0;
   static double reloj = 0;
   static double proximaLlegada;
   static double finInspeccion = Double.MAX_VALUE;
   static double finEmpaque1 = Double.MAX_VALUE;
   static double finEmpaque2 = Double.MAX_VALUE;
   static boolean inspectorOcupado = false;
   static boolean empacador1Ocupado = false;
   static boolean empacador2Ocupado = false;
   static Queue<Integer> colaInspeccion = new LinkedList<>();
   static Queue<Integer> colaEmpaque = new LinkedList<>();
   static int piezasLlegadas = 0;
   static int piezasInspeccionadas = 0;
   static int piezasEmpacadas = 0;
   static int piezasPerdidas = 0;
   static double tiempoOcupadoInspector = 0;
   static double tiempoOcupadoEmpacador = 0;
// =====================================
    // MÉTODOS
    // =====================================
    // Método para generar números pseudoaleatorios utilizando el método LGC
   public static double[] generadorLGC() {
       Scanner sc = new Scanner(System.in);
       long a, c, x, m;
       int n;

       System.out.print("Ingresa a: ");
       a = sc.nextLong();

       System.out.print("Ingresa c: ");
       c = sc.nextLong();

       System.out.print("Ingresa x0: ");
       x = sc.nextLong();

       System.out.print("Ingresa m: ");
       m = sc.nextLong();

       System.out.print("Cantidad de números: ");
       n = sc.nextInt();
// Se crea un arreglo para almacenar los números pseudoaleatorios uniformes generados
       double[] ri = new double[n];

       for (int i = 0; i < n; i++) {
           x = (a * x + c) % m;
           ri[i] = (double) x / m;
       }

       return ri;
   }
// Método para imprimir los resultados del generador de números pseudoaleatorios
   public static void imprimirGenerador(String nombre, double[] ri, String tipo) {
       System.out.println("\n================================");
       System.out.println(nombre);
       System.out.println("================================");
/*Se identifica el tipo de evento para calcular los valores correspondientes
  utilizando las fórmulas de distribución uniforme o exponencial
*/
// Las llegadas se generan con distribución uniforme entre 4 y 10 minutos
if (tipo.equals("LLEGADAS")) {
           System.out.println("No.\tRi\t\tUniforme(4,10)");
//Se imprimen los números pseudoaleatorios y sus transformaciones correspondientes
           for (int i = 0; i < ri.length; i++) {
               double valor = 4 + (10 - 4) * ri[i];
               System.out.printf("%d\t%.5f\t\t%.5f\n",
                       i + 1,
                       ri[i],
                       valor);
           }
// Las inspecciones se generan con distribución exponencial con media 5 minutos
       } else if (tipo.equals("INSPECCION")) {
           System.out.println("No.\tRi\t\tExponencial(5)");
// Se imprimen los números pseudoaleatorios y sus transformaciones correspondientes
           for (int i = 0; i < ri.length; i++) {
               double valor = -5 * Math.log(1 - ri[i]);
               System.out.printf("%d\t%.5f\t\t%.5f\n",
                       i + 1,
                       ri[i],
                       valor);
           }
// El empaque se genera con distribución exponencial con media 8 minutos
       } else if (tipo.equals("EMPAQUE")) {
           System.out.println("No.\tRi\t\tExponencial(8)");
// Se imprimen los números pseudoaleatorios y sus transformaciones correspondientes
           for (int i = 0; i < ri.length; i++) {
               double valor = -8 * Math.log(1 - ri[i]);
               System.out.printf("%d\t%.5f\t\t%.5f\n",
                       i + 1,
                       ri[i],
                       valor);
           }
       }
   }
// Métodos para obtener el siguiente número pseudoaleatorio de cada generador,
// reiniciando el índice si se alcanza el final del arreglo
   public static double siguienteRiLlegadas() {
       if (indiceLlegadas >= riLlegadas.length) {
           indiceLlegadas = 0;
       }
       return riLlegadas[indiceLlegadas++];
   }
   public static double siguienteRiInspeccion() {
       if (indiceInspeccion >= riInspeccion.length) {
           indiceInspeccion = 0;
       }
       return riInspeccion[indiceInspeccion++];
   }
   public static double siguienteRiEmpaque() {
       if (indiceEmpaque >= riEmpaque.length) {
           indiceEmpaque = 0;
       }
       return riEmpaque[indiceEmpaque++];
   }
// Métodos para generar los tiempos de llegada,
// inspección y empaque utilizando las fórmulas de distribución correspondientes
    // La llegada se genera con distribución uniforme entre 4 y 10 minutos   
public static double uniforme(double min, double max) {
       double r = siguienteRiLlegadas();
       return min + (max - min) * r;
   }
    // La inspección se genera con distribución exponencial con media 5 minutos
   public static double exponencialInspeccion(double media) {
       double r = siguienteRiInspeccion();
       return -media * Math.log(1 - r);
   }
    // El empaque se genera con distribución exponencial con media 8 minutos
   public static double exponencialEmpaque(double media) {
       double r = siguienteRiEmpaque();
       return -media * Math.log(1 - r);
   }
// Métodos para manejar los eventos de llegada, fin de inspección y fin de empaque
   public static void llegada() {
       piezasLlegadas++;
       proximaLlegada = reloj + uniforme(4, 10);
       // Si el inspector no está ocupado, se inicia la inspección de la pieza
       if (!inspectorOcupado) {
        // Se marca el inspector como ocupado
           inspectorOcupado = true;
        // Se genera el tiempo de inspección para la pieza y se acumula el tiempo ocupado por el inspector
           double t = exponencialInspeccion(5);
           tiempoOcupadoInspector += t;
        // Se programa el fin de inspección para la pieza
           finInspeccion = reloj + t;
       } // Si el inspector está ocupado, la pieza se agrega a la cola de inspección
       else {
           colaInspeccion.add(1);
       }
   }
// Método para manejar el evento de fin de inspección
   public static void finInspeccion() {
    // Se incrementa el contador de piezas inspeccionadas
       piezasInspeccionadas++;
    // Si hay piezas en la cola de inspección, se inicia la inspección de la siguiente pieza
       if (colaEmpaque.size() < 5) {
    // Si el empacador 1 no está ocupado, se asigna la pieza al empacador 1
           if (!empacador1Ocupado) {
    // Se marca el empacador 1 como ocupado
               empacador1Ocupado = true;
    // Se genera el tiempo de empaque para la pieza y se acumula el tiempo ocupado por el empacador
               double t = exponencialEmpaque(8);
    // Se acumula el tiempo ocupado por el empacador para calcular la utilización
               tiempoOcupadoEmpacador += t;
    // Se programa el fin de empaque para el empacador 1
               finEmpaque1 = reloj + t;

           }
    // Si el empacador 1 está ocupado pero el empacador 2 no está ocupado,
    // se asigna la pieza al empacador 2
           else if (!empacador2Ocupado) {
               empacador2Ocupado = true;
               double t = exponencialEmpaque(8);
               tiempoOcupadoEmpacador += t;
               finEmpaque2 = reloj + t;
           }
    // Si ambos empacadores están ocupados,
    // la pieza se agrega a la cola de empaque 
           else {
               colaEmpaque.add(1);
           }
    // Si la cola de empaque tiene 5 piezas, la pieza se pierde
       } else {
           piezasPerdidas++;
       }
// Si hay piezas en la cola de inspección, se inicia la inspección de la siguiente pieza
       if (!colaInspeccion.isEmpty()) {
           colaInspeccion.poll();
           double t = exponencialInspeccion(5);
           tiempoOcupadoInspector += t;
           finInspeccion = reloj + t;
       } else {
    // Si no hay piezas en la cola de inspección, el inspector queda libre
           inspectorOcupado = false;
    // Se programa el fin de inspección con un valor muy alto para indicar que no hay evento programado
           finInspeccion = Double.MAX_VALUE;
       }
   }
// Métodos para manejar los eventos de fin de empaque para cada empacador
   public static void finEmpaque1() {
    // Se incrementa el contador de piezas empacadas
       piezasEmpacadas++;
       // Si hay piezas en la cola de empaque, se inicia el empaque de la siguiente pieza
       if (!colaEmpaque.isEmpty()) {
           colaEmpaque.poll();
           double t = exponencialEmpaque(8);
           tiempoOcupadoEmpacador += t;
           finEmpaque1 = reloj + t;
       } else {
        // Si no hay piezas en la cola de empaque, el empacador 1 queda libre
           empacador1Ocupado = false;
        // Se programa el fin de empaque con un valor muy alto para indicar que no hay evento programado
           finEmpaque1 = Double.MAX_VALUE;
       }
   }
// Método para manejar el evento de fin de empaque para el empacador 2
   public static void finEmpaque2() {
       piezasEmpacadas++;
       if (!colaEmpaque.isEmpty()) {
           colaEmpaque.poll();
           double t = exponencialEmpaque(8);
           tiempoOcupadoEmpacador += t;
           finEmpaque2 = reloj + t;
       } else {
           empacador2Ocupado = false;
           finEmpaque2 = Double.MAX_VALUE;
       }
   }
    // =====================================
    // MAIN
    // =====================================
   public static void main(String[] args) {
    // Se crea un objeto Scanner para pausar la consola al finalizar la simulación
       Scanner pausa = new Scanner(System.in);
    // Se generan los números pseudoaleatorios para las llegadas,
    // inspección y empaque utilizando el método LGC
       System.out.println("GENERADOR LLEGADAS");
       riLlegadas = generadorLGC();
       System.out.println("\nGENERADOR INSPECCIÓN");
       riInspeccion = generadorLGC();
       System.out.println("\nGENERADOR EMPAQUE");
       riEmpaque = generadorLGC();
    // Se imprimen los resultados de los generadores de números pseudoaleatorios
       imprimirGenerador(
               "RESULTADOS GENERADOR LLEGADAS",
               riLlegadas,
               "LLEGADAS"
       );
       imprimirGenerador(
               "RESULTADOS GENERADOR INSPECCIÓN",
               riInspeccion,
               "INSPECCION"
       );
       imprimirGenerador(
               "RESULTADOS GENERADOR EMPAQUE",
               riEmpaque,
               "EMPAQUE"
       );
        // Se inicializa el reloj y se programa la primera llegada
       proximaLlegada = uniforme(4, 10);
// Se ejecuta el ciclo de eventos hasta que se alcance el tiempo de simulación
       while (reloj < TIEMPO_SIMULACION) {
    // Se determina el siguiente evento a procesar comparando los tiempos de llegada,
           double siguienteEvento = Math.min(
                   proximaLlegada,
                   Math.min(
                           finInspeccion,
                           Math.min(finEmpaque1, finEmpaque2)
                   )
           );
    // Se avanza el reloj al tiempo del siguiente evento
           reloj = siguienteEvento;
// Se procesa el evento correspondiente según el tipo de evento identificado
           if (reloj == proximaLlegada) {
               llegada();

           } else if (reloj == finInspeccion) {
               finInspeccion();

           } else if (reloj == finEmpaque1) {
               finEmpaque1();

           } else if (reloj == finEmpaque2) {
               finEmpaque2();
           }
       }
// Se muestran los resultados de la simulación, incluyendo el número de piezas llegadas,
       System.out.println("\n================================");
       System.out.println("RESULTADOS DE SIMULACIÓN");
       System.out.println("================================");
       System.out.println("Piezas llegadas: " + piezasLlegadas);
       System.out.println("Piezas inspeccionadas: " + piezasInspeccionadas);
       System.out.println("Piezas empacadas: " + piezasEmpacadas);
       System.out.println("Piezas perdidas: " + piezasPerdidas);
       int piezasPendientes =
               piezasLlegadas - piezasEmpacadas - piezasPerdidas;
       System.out.println("Piezas pendientes: " + piezasPendientes);
       System.out.println("\nCola inspección: " + colaInspeccion.size());
       System.out.println("Cola empaque: " + colaEmpaque.size());
// Se calcula la utilización de cada recurso dividiendo el tiempo ocupado por el tiempo total de simulación
       double utilInspector =
               tiempoOcupadoInspector / TIEMPO_SIMULACION;
       double utilEmpacador =
               tiempoOcupadoEmpacador / (TIEMPO_SIMULACION * 2);
// Se muestra la utilización de cada recurso en porcentaje
       System.out.printf(
               "\nUtilización inspector: %.2f%%\n",
               utilInspector * 100
       );

       System.out.printf(
               "Utilización empacadores: %.2f%%\n",
               utilEmpacador * 100
       );
// Se calcula la utilidad generada por el proceso
// asumiendo que cada pieza empacada genera una utilidad de $0.40
       double utilidad = piezasEmpacadas * 0.40;

       System.out.printf(
               "\nUtilidad generada: $%.2f\n",
               utilidad
       );
// =====================================
       // CUELLO DE BOTELLA
       // =====================================
// Se identifica el cuello de botella comparando las utilizaciones de los recursos
       if (utilEmpacador >
               utilInspector) {

           System.out.println(
                   "\nCuello de botella: EMPAQUE");

       } else {

           System.out.println(
                   "\nCuello de botella: INSPECCIÓN");
       }
       System.out.println("\nPresiona ENTER para salir...");
       pausa.nextLine();
   }
}