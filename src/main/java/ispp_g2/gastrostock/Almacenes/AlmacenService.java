package ispp_g2.gastrostock.Almacenes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlmacenService {

    @Autowired
    private AlmacenRepository ar;

    // Obtener todos los almacenes
    public List<Almacen> obtenerTodos() {
        Iterable<Almacen> iterable = ar.findAll();
        List<Almacen> almacenes = new ArrayList<>();
        iterable.forEach(almacenes::add);
        return almacenes;
    }

    // Buscar por ID
    public Optional<Almacen> obtenerPorId(Long id) {
        return ar.findById(id);
    }

    // Crear un nuevo almacén (asegura que el ID sea null)
    @Transactional
    public Almacen crearAlmacen(Almacen almacen) {
        if (almacen.getId() != null) {
            throw new IllegalArgumentException("El nuevo almacén no debe tener un ID asignado.");
        }
        return ar.save(almacen);
    }

    // Actualizar un almacén existente
    @Transactional
    public Almacen actualizarAlmacen(Long id, Almacen almacenActualizado) {
        Almacen almacenExistente = ar.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        almacenExistente.setMetrosCuadrados(almacenActualizado.getMetrosCuadrados());
        return ar.save(almacenExistente);
    }

    // Eliminar por ID
    public void eliminar(Long id) {
        ar.deleteById(id);
    }

    // Obtener almacenes con más de X metros cuadrados
    public List<Almacen> buscarPorMetrosCuadrados(Double metrosCuadrados) {
        return ar.findByMetrosCuadradosGreaterThan(metrosCuadrados);
    }
}
