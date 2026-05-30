Algoritmo Simulacion_Linea_Espera
	
    Definir TIEMPO_SIMULACION Como Real
	
    TIEMPO_SIMULACION <- 2400
	
    // =========================
    // VARIABLES
    // =========================
	
    Definir reloj Como Real
    Definir proximaLlegada Como Real
    Definir finInspeccion Como Real
    Definir finEmpaque Como Real
	
    Definir inspectorOcupado Como Logico
    Definir empacadorOcupado Como Logico
	
    Definir piezasLlegadas Como Entero
    Definir piezasInspeccionadas Como Entero
    Definir piezasEmpacadas Como Entero
    Definir piezasPerdidas Como Entero
	
    Definir colaInspeccion Como Entero
    Definir colaEmpaque Como Entero
	
    Definir tiempoOcupadoInspector Como Real
    Definir tiempoOcupadoEmpacador Como Real
	
    Definir utilidad Como Real
	
    // =========================
    // INICIALIZACIÓN
    // =========================
	
    reloj <- 0
	
    piezasLlegadas <- 0
    piezasInspeccionadas <- 0
    piezasEmpacadas <- 0
    piezasPerdidas <- 0
	
    colaInspeccion <- 0
    colaEmpaque <- 0
	
    inspectorOcupado <- Falso
    empacadorOcupado <- Falso
	
    tiempoOcupadoInspector <- 0
    tiempoOcupadoEmpacador <- 0
	
    // Primera llegada
	
    proximaLlegada <- Aleatorio(4,10)
	
    finInspeccion <- 999999
    finEmpaque <- 999999
	
    // =========================
    // CICLO PRINCIPAL
    // =========================
	
    Mientras reloj < TIEMPO_SIMULACION Hacer
		
        // =====================
        // SIGUIENTE EVENTO
        // =====================
		
        Si proximaLlegada < finInspeccion Y proximaLlegada < finEmpaque Entonces
			
            reloj <- proximaLlegada
			
            // =====================
            // EVENTO LLEGADA
            // =====================
			
            piezasLlegadas <- piezasLlegadas + 1
			
            // Programar nueva llegada
			
            proximaLlegada <- reloj + Aleatorio(4,10)
			
            // Verificar inspector
			
            Si inspectorOcupado = Falso Entonces
				
                inspectorOcupado <- Verdadero
				
                finInspeccion <- reloj + Exponencial(5)
				
                tiempoOcupadoInspector <- tiempoOcupadoInspector + (finInspeccion - reloj)
				
            Sino
				
                colaInspeccion <- colaInspeccion + 1
				
            FinSi
			
        Sino
			
            // =====================
            // FIN INSPECCIÓN
            // =====================
			
            Si finInspeccion < finEmpaque Entonces
				
                reloj <- finInspeccion
				
                piezasInspeccionadas <- piezasInspeccionadas + 1
				
                // Verificar cola empaque
				
                Si colaEmpaque < 5 Entonces
					
                    // Empacador libre
					
                    Si empacadorOcupado = Falso Entonces
						
                        empacadorOcupado <- Verdadero
						
                        finEmpaque <- reloj + Exponencial(8)
						
                        tiempoOcupadoEmpacador <- tiempoOcupadoEmpacador + (finEmpaque - reloj)
						
                    Sino
						
                        colaEmpaque <- colaEmpaque + 1
						
                    FinSi
					
                Sino
					
                    piezasPerdidas <- piezasPerdidas + 1
					
                FinSi
				
                // Revisar cola inspección
				
                Si colaInspeccion > 0 Entonces
					
                    colaInspeccion <- colaInspeccion - 1
					
                    finInspeccion <- reloj + Exponencial(5)
					
                    tiempoOcupadoInspector <- tiempoOcupadoInspector + (finInspeccion - reloj)
					
                Sino
					
                    inspectorOcupado <- Falso
					
                    finInspeccion <- 999999
					
                FinSi
				
            Sino
				
                // =====================
                // FIN EMPAQUE
                // =====================
				
                reloj <- finEmpaque
				
                piezasEmpacadas <- piezasEmpacadas + 1
				
                Si colaEmpaque > 0 Entonces
					
                    colaEmpaque <- colaEmpaque - 1
					
                    finEmpaque <- reloj + Exponencial(8)
					
                    tiempoOcupadoEmpacador <- tiempoOcupadoEmpacador + (finEmpaque - reloj)
					
                Sino
					
                    empacadorOcupado <- Falso
					
                    finEmpaque <- 999999
					
                FinSi
				
            FinSi
			
        FinSi
		
    FinMientras
	
    // =========================
    // RESULTADOS
    // =========================
	
    utilidad <- piezasEmpacadas * 0.40
	
    Escribir "================================"
    Escribir "RESULTADOS DE SIMULACIÓN"
    Escribir "================================"
	
    Escribir "Piezas llegadas: ", piezasLlegadas
    Escribir "Piezas inspeccionadas: ", piezasInspeccionadas
    Escribir "Piezas empacadas: ", piezasEmpacadas
    Escribir "Piezas perdidas: ", piezasPerdidas
	
    Escribir "Cola inspección: ", colaInspeccion
    Escribir "Cola empaque: ", colaEmpaque
	
    Escribir "Utilidad generada: $", utilidad
	
FinAlgoritmo