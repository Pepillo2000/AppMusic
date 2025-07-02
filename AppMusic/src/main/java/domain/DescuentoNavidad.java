package domain;

import java.time.LocalDate;
import java.time.Month;

public class DescuentoNavidad implements IDescuento{

	private final double DESCUENTO = 0.8;//descuento del 20%
	
	//descuento se aplica entre las fechas 20/12 del año actual y 06/01 del año siguiente
	@Override
	public double calcDescuento(double precio) {
		LocalDate fechaActual = LocalDate.now();
		LocalDate inicio = LocalDate.of(fechaActual.getYear(), Month.DECEMBER, 20);
        LocalDate fin = LocalDate.of(fechaActual.getYear(), Month.DECEMBER, 6);
		//comprobamos que estamos en las fechas del descuento
        if (fechaActual.isAfter(inicio) && fechaActual.isBefore(fin)) {
			double nuevoPrecio = precio*DESCUENTO;
			//redondeamos a 2 cifras decimales
			return Math.round(nuevoPrecio * 100.0) / 100.0;
		}
		return precio;
		
	}

}
