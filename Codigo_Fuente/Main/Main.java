package Main;
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

public class Main {

   // =====================================
   // TIEMPO TOTAL DE SIMULACIÓN
   // =====================================

   static final double TIEMPO_SIMULACION = 2400.0;

   // =====================================
   // GENERADORES INDEPENDIENTES
   // =====================================
// Para cada proceso se tiene un generador independiente de números aleatorios
   static double[] riLlegadas;
   static double[] riInspeccion;
   static double[] riEmpaque;
// Índices para cada generador
   static int indiceLlegadas = 0;
   static int indiceInspeccion = 0;
   static int indiceEmpaque = 0;

   // =====================================
   // RELOJ DE SIMULACIÓN
   // =====================================
// El reloj de simulación se actualiza al ocurrir cada evento
   static double reloj = 0;

   // =====================================
   // EVENTOS FUTUROS
   // =====================================
// Se mantiene el tiempo del próximo evento de cada tipo
   static double proximaLlegada;
   static double finInspeccion = Double.MAX_VALUE;
   static double finEmpaque = Double.MAX_VALUE;

   // =====================================
   // ESTADO DE RECURSOS
   // =====================================
// Se mantiene el estado de cada recurso (ocupado o libre)
   static boolean inspectorOcupado = false;
   static boolean empacadorOcupado = false;

   // =====================================
   // COLAS
   // =====================================
// Se mantienen colas para cada proceso, aunque en este caso solo el empaque tiene una capacidad limitada
   static Queue<Integer> colaInspeccion =
           new LinkedList<>();

   static Queue<Integer> colaEmpaque =
           new LinkedList<>();

   // =====================================
   // CONTADORES
   // =====================================
// Se mantienen contadores para las piezas que llegan, son inspeccionadas, empacadas y perdidas
   static int piezasLlegadas = 0;
   static int piezasInspeccionadas = 0;
   static int piezasEmpacadas = 0;
   static int piezasPerdidas = 0;

   // =====================================
   // ACUMULADORES
   // =====================================
// Se mantienen acumuladores para el tiempo ocupado de cada recurso, lo que permitirá calcular su utilización
   static double tiempoOcupadoInspector = 0;
   static double tiempoOcupadoEmpacador = 0;

   // =====================================
   // GENERADOR CONGRUENCIAL LINEAL
   // =====================================
// Este método genera un arreglo de números pseudoaleatorios utilizando el método congruencial lineal
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
// Se genera el arreglo de números pseudoaleatorios uniformes
       double[] ri = new double[n];

       for (int i = 0; i < n; i++) {

           x = (a * x + c) % m;

           ri[i] = (double) x / m;
       }
       return ri;
   }

   // =====================================
   // RI PARA LLEGADAS
   // =====================================
/* Este método devuelve el siguiente número pseudoaleatorio para las llegadas,
   y reinicia el índice si se alcanza el final del arreglo
*/   
public static double siguienteRiLlegadas() {
// Si se alcanza el final del arreglo, se reinicia el índice para reutilizar los números
       if (indiceLlegadas >= riLlegadas.length) {

           indiceLlegadas = 0;
       }
// Se devuelve el siguiente número pseudoaleatorio para las llegadas
       return riLlegadas[indiceLlegadas++];
   }

   // =====================================
   // RI PARA INSPECCIÓN
   // =====================================
// Este método devuelve el siguiente número pseudoaleatorio para la inspección,
   public static double siguienteRiInspeccion() {
// y reinicia el índice si se alcanza el final del arreglo
       if (indiceInspeccion >= riInspeccion.length) {

           indiceInspeccion = 0;
       }

       return riInspeccion[indiceInspeccion++];
   }

   // =====================================
   // RI PARA EMPAQUE
   // =====================================
// Este método devuelve el siguiente número pseudoaleatorio para el empaque,
   public static double siguienteRiEmpaque() {
// y reinicia el índice si se alcanza el final del arreglo
       if (indiceEmpaque >= riEmpaque.length) {

           indiceEmpaque = 0;
       }

       return riEmpaque[indiceEmpaque++];
   }

   // =====================================
   // DISTRIBUCIÓN UNIFORME
   // =====================================
/* Este método genera un número aleatorio con distribución uniforme entre min y max
   utilizando el siguiente número pseudoaleatorio para las llegadas
*/   
public static double uniforme(double min,
                                 double max) {

       double r = siguienteRiLlegadas();
// Se transforma el número pseudoaleatorio uniforme en un número con distribución uniforme entre min y max
       return min + (max - min) * r;
   }

   // =====================================
   // DISTRIBUCIÓN EXPONENCIAL INSPECCIÓN
   // =====================================
/* Este método genera un número aleatorio con distribución exponencial
   utilizando el siguiente número pseudoaleatorio para la inspección*/
   public static double exponencialInspeccion(
           double media) {

       double r = siguienteRiInspeccion();
/* Se transforma el número pseudoaleatorio uniforme
   en un número con distribución exponencial con la media especificada
*/
       return -media * Math.log(1 - r);
   }

   // =====================================
   // DISTRIBUCIÓN EXPONENCIAL EMPAQUE
   // =====================================
// Este método genera un número aleatorio con distribución exponencial
   public static double exponencialEmpaque(
           double media) {
// utilizando el siguiente número pseudoaleatorio para el empaque
       double r = siguienteRiEmpaque();
// Se transforma el número pseudoaleatorio uniforme
       return -media * Math.log(1 - r);
   }

   // =====================================
   // EVENTO LLEGADA
   // =====================================
// Este método se ejecuta cada vez que ocurre una llegada de pieza al sistema
   public static void llegada() {
// Al llegar una pieza, se incrementa el contador de piezas llegadas
       piezasLlegadas++;
// Se programa la próxima llegada generando un nuevo tiempo de llegada
       proximaLlegada =
               reloj + uniforme(4, 10);
        // Al llegar una pieza, se verifica si el inspector está ocupado
       if (!inspectorOcupado) {
        // Si el inspector no está ocupado, la pieza se inspecciona inmediatamente
           inspectorOcupado = true;
        // Se genera el tiempo de servicio para la inspección
           double tiempoServicio =
                   exponencialInspeccion(5);
        // Se acumula el tiempo ocupado del inspector
           tiempoOcupadoInspector +=
                   tiempoServicio;

           finInspeccion =
                   reloj + tiempoServicio;
       } else {
        // Si el inspector está ocupado, la pieza se agrega a la cola de inspección
           colaInspeccion.add(1);
       }
   }

   // =====================================
   // EVENTO FIN INSPECCIÓN
   // =====================================

   public static void finInspeccion() {
// Al finalizar la inspección, se incrementa el contador de piezas inspeccionadas
       piezasInspeccionadas++;
       // Después de la inspección, la pieza pasa a la fila de empaque
       if (colaEmpaque.size() < 5) {
        // Si la cola de empaque tiene espacio, se verifica si el empacador está ocupado
           if (!empacadorOcupado) {
                // Si el empacador no está ocupado, la pieza se empaca inmediatamente
               empacadorOcupado = true;
               // Se genera el tiempo de servicio para el empaque
               double tiempoEmpaque =
                       exponencialEmpaque(8);
                       // Se acumula el tiempo ocupado del empacador
               tiempoOcupadoEmpacador +=
                       tiempoEmpaque;
                       // Se programa el fin del empaque para esta pieza
               finEmpaque =
                       reloj + tiempoEmpaque;

           } else {
                // Si el empacador está ocupado, la pieza se agrega a la cola de empaque
               colaEmpaque.add(1);
           }
// Si la cola de empaque está llena, la pieza se pierde
       } else {
           piezasPerdidas++;
       }
       // Después de procesar la pieza actual, se verifica si hay piezas esperando en la cola de inspección
       if (!colaInspeccion.isEmpty()) {
           colaInspeccion.poll();

           double tiempoServicio =
                   exponencialInspeccion(5);

           tiempoOcupadoInspector +=
                   tiempoServicio;

           finInspeccion =
                   reloj + tiempoServicio;

       } else {

           inspectorOcupado = false;

           finInspeccion =
                   Double.MAX_VALUE;
       }
   }

   // =====================================
   // EVENTO FIN EMPAQUE
   // =====================================
// Al finalizar el empaque, se incrementa el contador de piezas empacadas
   public static void finEmpaque() {

       piezasEmpacadas++;
// Después de procesar la pieza actual, se verifica si hay piezas esperando en la cola de empaque
       if (!colaEmpaque.isEmpty()) {

           colaEmpaque.poll();
// Se genera el tiempo de servicio para el empaque de la siguiente pieza
           double tiempoEmpaque =
                   exponencialEmpaque(8);
// Se acumula el tiempo ocupado del empacador
           tiempoOcupadoEmpacador +=
                   tiempoEmpaque;
// Se programa el fin del empaque para la siguiente pieza
           finEmpaque =
                   reloj + tiempoEmpaque;

       } else {
// Si no hay piezas esperando, el empacador queda libre
           empacadorOcupado = false;
// Se programa el fin del empaque para un tiempo infinito, ya que no hay piezas por empacar
           finEmpaque =
                   Double.MAX_VALUE;
       }
   }

   // =====================================
   // MAIN
   // =====================================
/*Este es el método principal donde se inicializan los generadores,
  se ejecuta el ciclo de simulación y se muestran los resultados*/
   public static void main(String[] args) {

       Scanner pausa = new Scanner(System.in);

       System.out.println(
               "GENERADOR LLEGADAS");

       riLlegadas = generadorLGC();

       System.out.println(
               "\nGENERADOR INSPECCIÓN");

       riInspeccion = generadorLGC();

       System.out.println(
               "\nGENERADOR EMPAQUE");

       riEmpaque = generadorLGC();

       // Primera llegada al sistema
       proximaLlegada = uniforme(4, 10);

       // =====================================
       // CICLO PRINCIPAL
       // =====================================
// El ciclo principal de la simulación se ejecuta hasta que el reloj alcance el tiempo total de simulación
       while (reloj < TIEMPO_SIMULACION) {
// Se determina cuál es el siguiente evento a ocurrir comparando los tiempos de los próximos eventos
           double siguienteEvento = Math.min(
                   proximaLlegada,
                   Math.min(
                           finInspeccion,
                           finEmpaque
                   )
           );
// Se avanza el reloj al tiempo del siguiente evento
           reloj = siguienteEvento;

           if (reloj == proximaLlegada) {

               llegada();

           } else if (reloj == finInspeccion) {

               finInspeccion();

           } else if (reloj == finEmpaque) {

               finEmpaque();
           }
       }

       // =====================================
       // RESULTADOS
       // =====================================

       System.out.println(
               "\n================================");

       System.out.println(
               "RESULTADOS DE SIMULACIÓN");

       System.out.println(
               "================================");

       System.out.println(
               "Piezas llegadas: "
                       + piezasLlegadas);

       System.out.println(
               "Piezas inspeccionadas: "
                       + piezasInspeccionadas);

       System.out.println(
               "Piezas empacadas: "
                       + piezasEmpacadas);

       System.out.println(
               "Piezas perdidas: "
                       + piezasPerdidas);

       // =====================================
       // PIEZAS PENDIENTES
       // =====================================
// Se calcula el número de piezas pendientes en el sistema al finalizar la simulación
       int piezasPendientes =
               piezasLlegadas
                       - piezasEmpacadas
                       - piezasPerdidas;
// Se muestra el número de piezas pendientes en el sistema
       System.out.println(
               "Piezas pendientes: "
                       + piezasPendientes);

       System.out.println();
// Se muestra el número de piezas en cada cola al finalizar la simulación
       System.out.println(
               "Cola final inspección: "
                       + colaInspeccion.size());

       System.out.println(
               "Cola final empaque: "
                       + colaEmpaque.size());

       // =====================================
       // UTILIZACIÓN
       // =====================================
// Se calcula la utilización de cada recurso dividiendo el tiempo ocupado por el tiempo total de simulación
       double utilInspector =
               tiempoOcupadoInspector
                       / TIEMPO_SIMULACION;

       double utilEmpacador =
               tiempoOcupadoEmpacador
                       / TIEMPO_SIMULACION;

       System.out.printf(
               "\nUtilización inspector: %.2f%%\n",
               utilInspector * 100
       );

       System.out.printf(
               "Utilización empacador: %.2f%%\n",
               utilEmpacador * 100
       );

       // =====================================
       // UTILIDAD
       // =====================================
/*Se calcula la utilidad generada por las piezas empacadas
multiplicando el número de piezas empacadas por la utilidad por pieza*/
       double utilidad =
               piezasEmpacadas * 0.40;

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

       // =====================================
       // EVITAR QUE SE CIERRE
       // =====================================
/*Se muestra un mensaje para evitar que la consola se cierre inmediatamente
  después de mostrar los resultados*/
       System.out.println(
               "\nPresiona ENTER para salir...");

       pausa.nextLine();
   }
}