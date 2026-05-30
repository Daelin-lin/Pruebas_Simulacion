# Pruebas_Simulacion
## Autores

- Herrera Martínez Aylin
- Gutiérrez Rivas Alejandro
- Vázquez Alviso Bryan Alexei

## Descripción
Proyecto desarrollado para la asignatura de Simulación, enfocado en el modelado de una línea de producción mediante la técnica de Simulación de Eventos Discretos (SED).
El sistema utiliza números pseudoaleatorios generados mediante un Generador Lineal Congruencial (LGC) para representar los tiempos entre llegadas y los tiempos de servicio de los procesos de inspección y empaque.

La simulación permite analizar el comportamiento del sistema durante un periodo de 40 horas, evaluar el desempeño de los recursos, identificar cuellos de botella y estimar la utilidad generada por la producción.

## Enunciado del problema
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
## Objetivos

### Objetivo General

Desarrollar un simulador de eventos discretos capaz de representar el funcionamiento de una línea de producción y generar métricas que apoyen la toma de decisiones.

### Objetivos Específicos

- Simular la llegada de piezas al sistema.
- Modelar el proceso de inspección mediante una distribución exponencial.
- Modelar el proceso de empaque mediante una distribución exponencial.
- Administrar las colas de espera del sistema.
- Calcular indicadores de desempeño.
- Identificar el cuello de botella del proceso.
- Analizar posibles mejoras para incrementar la utilidad semanal.

## Tecnologías Utilizadas

- Java
- Programación Orientada a Objetos
- Simulación de Eventos Discretos
- Generador Lineal Congruencial (LGC)
- OpenJDK 25
- Visual Studio Code
## Versiones del Sistema

| Versión | Ejecutable | Características                              |
|---------|------------|----------------                              |
| Main    | Main.exe   | Simulación base con 1 inspector y 1 empacador |
| Prueba_2 | Prueba_2.exe | Variante experimental utilizada para pruebas y análisis comparativos |

---
## Modelo de Simulación

| Proceso | Distribución |
|----------|-------------|
| Llegadas | Uniforme (4,10) minutos |
| Inspección | Exponencial (media = 5 min) |
| Empaque | Exponencial (media = 8 min) |
| Tiempo de simulación | 2,400 minutos (40 horas) |

---

## Requisitos

- Windows 10 o Windows 11 (64 bits)
- No requiere Java instalado (JVM embebida OpenJDK 25.0.1)
- Aproximadamente 250 MB de espacio libre en disco por versión
- Mínimo 256 MB libres en memoria RAM para la JVM
- Permisos de Lectura/escritura en la carpeta de instalación

## Instalación

1. Descargar el archivo ZIP correspondiente.
2. Extraer completamente el contenido.
3. No modificar la estructura de carpetas.
4. Ejecutar Main.exe o Prueba_2.exe.

## Uso

## Uso

Al iniciar el programa se solicitan los parámetros del Generador Lineal Congruencial para:

1. Llegadas.
2. Inspección.
3. Empaque.

Parámetros requeridos:

- a (multiplicador)
- c (incremento)
- x0 (semilla)
- m (módulo)
- cantidad de números a generar

> Solo ingresa números enteros. No uses comas ni puntos decimales.

## Parámetros Recomendados
1.
| Parámetro | Valor sugerido |
|-----------|----------------|
| a         | 57            |
| c         | 1021           |
| x0        | 7              |
| m         | 4096           |
| n         | 5000 o más     |
2.
| Parámetro | Valor sugerido |
|-----------|----------------|
| a         | 153            |
| c         | 125            |
| x0        | 100            |
| m         | 4096           |
| n         | 5000 o más     |
3.
| Parámetro | Valor sugerido |
|-----------|----------------|
| a         | 12869          |
| c         | 13849          |
| x0        | 4513           |
| m         | 4096           |
| n         | 5000 o más     |

## Modelo de Simulación

```
Llegadas → Uniforme(4,10) min entre llegadas
Inspección → Exponencial(media=5) min por pieza
Empaque → Exponencial(media=8) min por pieza
Tiempo total → 2,400 minutos (40 horas)
```

### Fórmula LGC

```
X(n+1) = (a * X(n) + c) mod m
RI(n)  = X(n) / m
```

## Resultados mostrados

- Piezas llegadas / inspeccionadas / empacadas / perdidas / pendientes
- Cola final de inspección y empaque
- Utilización del inspector y empacador(es) (%)
- Utilidad generada ($0.40 por pieza empacada)
- Cuello de botella

## Estructura del Repositorio

```
Pruebas_Simulacion/
│
├── Codigo_Fuente/
│   ├── Main/
│   │   └── Main.java
│   │
│   └── Prueba_2/
│       └── Prueba_2.java
│
├── Ejecutables/
│   ├── Main.zip
│   └── Prueba_2.zip
│
├── Manuales/
│   ├── Manual_Tecnico.docx
│   └── Manual_Usuario.docx
│
├── Diagramas/
│
└── README.md
```
## Guardar Resultados

Para guardar la salida en un archivo de texto, ejecutar desde la consola de Windows:

```cmd
cd ruta\del\programa
Main.exe > resultados.txt
```

Al finalizar, el archivo `resultados.txt` contendrá toda la salida generada por la simulación.

## Licencia

Proyecto desarrollado con fines académicos para la asignatura de Simulación.

No se autoriza su uso comercial sin consentimiento de los autores.