package controller;

import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import domain.Cancion;
import domain.PlayList;

public class GeneradorPDF {
	
	private FileOutputStream archivo;			// Archivo de salida de donde se genera el documento
	private Document documento;					// Documento PDF	
	
	public GeneradorPDF() {}

	public void generarPDF(String nombre, List<PlayList> lista) throws Exception {
		try {
			archivo = new FileOutputStream("./pdf/" + nombre + ".pdf");
			documento = new Document();
		    PdfWriter.getInstance(documento, archivo);	// Escribe el documento PDF en el archivo de salida
		} catch (Exception e) {
			e.printStackTrace();
		}
		 documento.open();
		 
		// Agregar imagen al principio
         Image imagen = Image.getInstance("./img/logoPDF.png");
         imagen.scaleAbsolute(400f, 120f);
         imagen.setAlignment(Element.ALIGN_CENTER);
         documento.add(imagen);

         // Agregar espacio entre la imagen y el siguiente contenido
         documento.add(new Paragraph("\n"));

         for (PlayList p : lista) {
             // Crear tabla con dos columnas
             PdfPTable tabla = new PdfPTable(3);

             // Configurar estilo para el encabezado de la tabla
             PdfPCell encabezadoTabla = new PdfPCell(new Paragraph("PlayList " + p.getNombre()));
             encabezadoTabla.setBackgroundColor(new BaseColor(0, 128, 0));
             encabezadoTabla.setHorizontalAlignment(Element.ALIGN_CENTER);
             encabezadoTabla.setColspan(3);
             tabla.addCell(encabezadoTabla);

             // Añadir encabezados de columna con color verde
             PdfPCell encabezadoSong = new PdfPCell(new Paragraph("Song"));
             encabezadoSong.setBackgroundColor(new BaseColor(0, 128, 0));
             encabezadoSong.setHorizontalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(encabezadoSong);

             PdfPCell encabezadoPerformer = new PdfPCell(new Paragraph("Performer"));
             encabezadoPerformer.setBackgroundColor(new BaseColor(0, 128, 0));
             encabezadoPerformer.setHorizontalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(encabezadoPerformer);
             
             PdfPCell encabezadoStyle = new PdfPCell(new Paragraph("Style"));
             encabezadoStyle.setBackgroundColor(new BaseColor(0, 128, 0));
             encabezadoStyle.setHorizontalAlignment(Element.ALIGN_CENTER);
             tabla.addCell(encabezadoStyle);

             // Añadir filas con datos de la lista
             for (Cancion c : p.getCanciones()) {
                 tabla.addCell(c.getTitulo());
                 tabla.addCell(c.getInterprete());
                 tabla.addCell(c.getEstilo());
             }

             // Añadir la tabla al documento
             documento.add(tabla);

             // Agregar espacio entre las listas
             documento.add(new Paragraph("\n"));
             documento.add(new Paragraph("\n"));
         }
	     documento.close();
	}

}
