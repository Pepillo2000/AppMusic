package domain;

public class DescuentoMayores implements IDescuento{
	
	private int edad;
	//edad a partir de la cual se aplicará el descuento
	private final int EDAD_DESCUENTO = 65;
	//valor del descuento
	private final double DESCUENTO = 0.5;
	
	public DescuentoMayores(int edad) {
		this.edad = edad;
	}

	@Override
	public double calcDescuento(double precio) {
		if (edad>= EDAD_DESCUENTO) {//comprobamos que es mayor de 65 años
			double nuevoPrecio = precio*DESCUENTO;
			//redondeamos a dos cifras decimales
			return Math.round(nuevoPrecio * 100.0) / 100.0;
		}
		return precio;
	}
}
