package util;

import modelo.Persona;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportadorCSV {
    public static void exportar(List<Persona> personas, String archivo) throws IOException {
        FileWriter writer = new FileWriter(archivo);
        writer.write("Nombre,Teléfono,Email,Categoría,Favorito\n");
        for (Persona p : personas) {
            writer.write(p.getNombre() + "," +
                         p.getTelefono() + "," +
                         p.getEmail() + "," +
                         p.getCategoria() + "," +
                         (p.isFavorito() ? "Sí" : "No") + "\n");
        }
        writer.close();
    }
}
