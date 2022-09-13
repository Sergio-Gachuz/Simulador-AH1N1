/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package montecarlo;

import java.util.List;
import static java.lang.Double.max;
import static java.lang.Double.min;
import static java.lang.Math.abs;
import java.util.ArrayList;

/**
 *
 * @author Lizeth R
 */
public class Generador {
    
    private int x;
	private int a;
	private int c;
	private int m;
	private List<Double> listaGenerada;
	
	public Generador(int x,int a,int c,int m) throws Exception{
		this.x=x;
		this.a=a;
		this.c=c;
		this.m=m;
		validarConfiguracion();
	}
	
	
	public static int mcd(int a, int b) { // calcula el maximo comun divisor 
		int m1, m2, aux; 
		a = abs(a); // valor absoluto 
		b = abs(b); 
		m1 = (int) max(a, b); // mayor de dos numeros 
		m2 = (int) min(a, b); 
		aux = 0; 
		do { 
		aux = m2; 
		m2 = m1%m2; 
		m1 = aux; 
		} while(m2 != 0); 
		return m1; 
		}


	
	private void validarConfiguracion() throws Exception{
		if(c>0){
			if(x>0&&a>0&&m>0){
				if(mcd(c, m)==1){
					
				}else{
					throw new Exception("c y m no son primos relativos");
				}
			}else{
				throw new Exception("x,a,m deben ser mayores a 0");
			}
		}else{
			
		}
	}
	
	private double dameSiguienteAleatorio(){
		if(c>0){
			int valor = ((a*x)+c)%m;
			x=valor;
			return getDecimal(5,valor/Double.parseDouble(m+""));
		}else{
			int valor = ((a*x))%(m*4);
			x=valor;
			return getDecimal(5,valor/Double.parseDouble(m+""));
		}
	}
	
	public static double getDecimal(int numeroDecimales,double decimal){
		 decimal = decimal*(java.lang.Math.pow(10, numeroDecimales));
		 decimal = java.lang.Math.round(decimal);
		 decimal = decimal/java.lang.Math.pow(10, numeroDecimales);
		return decimal;  }

	
	public List<Double> generar(int hasta){
		listaGenerada= new ArrayList<Double>();
		for (int i = 0; i < hasta; i++) {
			listaGenerada.add(dameSiguienteAleatorio());
		}
		return listaGenerada;
	}



    
}
