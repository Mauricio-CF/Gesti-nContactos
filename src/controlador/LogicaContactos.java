package controlador;

import modelo.Persona;
import modelo.PersonaDAO;
import util.ExportadorCSV;

import java.io.IOException;
import java.util.ArrayList;

public class LogicaContactos {
    private ArrayList<Persona> lista = new ArrayList<>();

    public void agregarContacto(Persona p) throws IOException {
        lista.add(p);
        PersonaDAO.guardarTodos(lista);
    }

    public void modificarContacto(int index, Persona p) throws IOException {
        if (index >= 0 && index < lista.size()) {
            lista.set(index, p);
            PersonaDAO.guardarTodos(lista);
        }
    }

    public void eliminarContacto(int index) throws IOException {
        if (index >= 0 && index < lista.size()) {
            lista.remove(index);
            PersonaDAO.guardarTodos(lista);
        }
    }

    public ArrayList<Persona> getContactos() {
        return lista;
    }

    public void cargarDesdeArchivo() throws IOException {
        lista = PersonaDAO.cargarContactos();
    }

    public void exportarCSV(String rutaArchivo) throws IOException {
        ExportadorCSV.exportar(lista, rutaArchivo);
    }
}
