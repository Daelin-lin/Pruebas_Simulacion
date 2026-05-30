Algoritmo Simulacion_Linea_Espera_Dos_Empacadores
	
    Definir TIEMPO_SIMULACION Como Real
    TIEMPO_SIMULACION <- 2400
	
    Definir reloj Como Real
    Definir proximaLlegada Como Real
    Definir finInspeccion Como Real
    Definir finEmpaque1 Como Real
    Definir finEmpaque2 Como Real
	
    Definir inspectorOcupado Como Logico
    Definir empacador1Ocupado Como Logico
    Definir empacador2Ocupado Como Logico
	
    Definir piezasLlegadas Como Entero
    Definir piezasInspeccionadas Como Entero
    Definir piezasEmpacadas Como Entero
    Definir piezasPerdidas Como Entero
    Definir piezasPendientes Como Entero
	
    Definir colaInspeccion Como Entero
    Definir colaEmpaque Como Entero
	
    Definir tiempoOcupadoInspector Como Real
    Definir tiempoOcupadoEmpacador Como Real
	
    Definir utilInspector Como Real
    Definir utilEmpacador Como Real
    Definir utilidad Como Real
	
    reloj <- 0
    proximaLlegada <- Aleatorio(4,10)
    finInspeccion <- 999999
    finEmpaque1 <- 999999
    finEmpaque2 <- 999999
	
    inspectorOcupado <- Falso
    empacador1Ocupado <- Falso
    empacador2Ocupado <- Falso
	
    piezasLlegadas <- 0
    piezasInspeccionadas <- 0
    piezasEmpacadas <- 0
    piezasPerdidas <- 0
	
    colaInspeccion <- 0
    colaEmpaque <- 0
	
    tiempoOcupadoInspector <- 0
    tiempoOcupadoEmpacador <- 0
	
    Mientras reloj < TIEMPO_SIMULACION Hacer
		
        Si proximaLlegada <= finInspeccion Y proximaLlegada <= finEmpaque1 Y proximaLlegada <= finEmpaque2 Entonces
			
            reloj <- proximaLlegada
            piezasLlegadas <- piezasLlegadas + 1
            proximaLlegada <- reloj + Aleatorio(4,10)
			
            Si inspectorOcupado = Falso Entonces
                inspectorOcupado <- Verdadero
                finInspeccion <- reloj + Exponencial(5)
                tiempoOcupadoInspector <- tiempoOcupadoInspector + (finInspeccion - reloj)
            Sino
                colaInspeccion <- colaInspeccion + 1
            FinSi
			
        Sino
			
            Si finInspeccion <= finEmpaque1 Y finInspeccion <= finEmpaque2 Entonces
				
                reloj <- finInspeccion
                piezasInspeccionadas <- piezasInspeccionadas + 1
				
                Si colaEmpaque < 5 Entonces
					
                    Si empacador1Ocupado = Falso Entonces
                        empacador1Ocupado <- Verdadero
                        finEmpaque1 <- reloj + Exponencial(8)
                        tiempoOcupadoEmpacador <- tiempoOcupadoEmpacador + (finEmpaque1 - reloj)
						
                    Sino
                        Si empacador2Ocupado = Falso Entonces
                            empacador2Ocupado <- Verdadero
                            finEmpaque2 <- reloj + Exponencial(8)
                            tiempoOcupadoEmpacador <- tiempoOcupadoEmpacador + (finEmpaque2 - reloj)
                        Sino
                            colaEmpaque <- colaEmpaque + 1
                        FinSi
                    FinSi
					
                Sino
                    piezasPerdidas <- piezasPerdidas + 1
                FinSi
				
                Si colaInspeccion > 0 Entonces
                    colaInspeccion <- colaInspeccion - 1
                    finInspeccion <- reloj + Exponencial(5)
                    tiempoOcupadoInspector <- tiempoOcupadoInspector + (finInspeccion - reloj)
                Sino
                    inspectorOcupado <- Falso
                    finInspeccion <- 999999
                FinSi
				
            Sino
				
                Si finEmpaque1 <= finEmpaque2 Entonces
					
                    reloj <- finEmpaque1
                    piezasEmpacadas <- piezasEmpacadas + 1
					
                    Si colaEmpaque > 0 Entonces
                        colaEmpaque <- colaEmpaque - 1
                        finEmpaque1 <- reloj + Exponencial(8)
                        tiempoOcupadoEmpacador <- tiempoOcupadoEmpacador + (finEmpaque1 - reloj)
                    Sino
                        empacador1Ocupado <- Falso
                        finEmpaque1 <- 999999
                    FinSi
					
                Sino
					
                    reloj <- finEmpaque2
                    piezasEmpacadas <- piezasEmpacadas + 1
					
                    Si colaEmpaque > 0 Entonces
                        colaEmpaque <- colaEmpaque - 1
                        finEmpaque2 <- reloj + Exponencial(8)
                        tiempoOcupadoEmpacador <- tiempoOcupadoEmpacador + (finEmpaque2 - reloj)
                    Sino
                        empacador2Ocupado <- Falso
                        finEmpaque2 <- 999999
                    FinSi
					
                FinSi
				
            FinSi
			
        FinSi
		
    FinMientras
	
    piezasPendientes <- piezasLlegadas - piezasEmpacadas - piezasPerdidas
    utilInspector <- tiempoOcupadoInspector / TIEMPO_SIMULACION
    utilEmpacador <- tiempoOcupadoEmpacador / (TIEMPO_SIMULACION * 2)
    utilidad <- piezasEmpacadas * 0.40
	
    Escribir "RESULTADOS DE SIMULACIÓN"
    Escribir "Piezas llegadas: ", piezasLlegadas
    Escribir "Piezas inspeccionadas: ", piezasInspeccionadas
    Escribir "Piezas empacadas: ", piezasEmpacadas
    Escribir "Piezas perdidas: ", piezasPerdidas
    Escribir "Piezas pendientes: ", piezasPendientes
    Escribir "Cola inspección: ", colaInspeccion
    Escribir "Cola empaque: ", colaEmpaque
    Escribir "Utilización inspector: ", utilInspector * 100, "%"
    Escribir "Utilización empacadores: ", utilEmpacador * 100, "%"
    Escribir "Utilidad generada: $", utilidad
	
FinAlgoritmo