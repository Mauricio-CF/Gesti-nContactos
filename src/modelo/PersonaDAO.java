package modelo;

import java.io.*;
import java.util.ArrayList;

public class PersonaDAO {
    private static final String ARCHIVO = "contactos.csv";

    public static void guardarTodos(ArrayList<Persona> lista) throws IOException {
        try (FileWriter fw = new FileWriter(ARCHIVO);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Persona p : lista) {
                bw.write(p.getNombre() + "," + p.getTelefono() + "," + p.getEmail() + "," +
                         p.getCategoria() + "," + p.isFavorito());
                bw.newLine();
            }
        }
    }

    public static ArrayList<Persona> cargarContactos() throws IOException {
        ArrayList<Persona> lista = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    Persona p = new Persona(
                        datos[0], datos[1], datos[2], datos[3], Boolean.parseBoolean(datos[4])
                    );
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
