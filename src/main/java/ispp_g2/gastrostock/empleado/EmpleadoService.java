package ispp_g2.gastrostock.empleado;

import java.util.List;
import java.util.stream.StreamSupport;

import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    // Crear o actualizar un empleado
    @Transactional
    public Empleado saveEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    // Obtener todos los empleados
    @Transactional(readOnly = true)
    public List<Empleado> getAllEmpleados() {
        Iterable<Empleado> empleados = empleadoRepository.findAll();
        return StreamSupport.stream(empleados.spliterator(), false).toList();
    }

    // Buscar empleado por ID
    @Transactional(readOnly = true)
    public Empleado getEmpleadoById(String id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    // Eliminar empleado
    @Transactional
    public void deleteEmpleado(String id) {
        empleadoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Empleado getEmpleadoByEmail(String email) {
        return empleadoRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Empleado> getEmpleadoByNombre(String nombre) {
        return empleadoRepository.findByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Empleado> getEmpleadoByApellido(String apellido) {
        return empleadoRepository.findByApellido(apellido);
    }

    @Transactional(readOnly = true)
    public Empleado getEmpleadoByTelefono(String telefono) {
        return empleadoRepository.findByTelefono(telefono).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Empleado> getEmpleadoByNegocio(String id) {
        return empleadoRepository.findByNegocio(id);
    }

    @Transactional(readOnly = true)
    public Empleado getEmpleadoByUser(String userId) {
        return empleadoRepository.findByUserId(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Empleado getEmpleadoByTokenEmpleado(String tokenEmpleado) {
        return empleadoRepository.findByTokenEmpleado(tokenEmpleado).orElse(null);
    }

    @Transactional
    public Empleado convertirDTOEmpleado(EmpleadoDTO empleadoDTO, Negocio negocio, User user) {
        Empleado empleado = new Empleado();
        empleado.setFirstName(empleadoDTO.getFirstName());
        empleado.setLastName(empleadoDTO.getLastName());
        empleado.setEmail(empleadoDTO.getEmail());
        empleado.setTokenEmpleado(empleadoDTO.getTokenEmpleado());
        empleado.setNumTelefono(empleadoDTO.getNumTelefono());
        empleado.setDescripcion(empleadoDTO.getDescripcion());
        empleado.setUser(user);
        empleado.setNegocio(negocio);
        return empleado;
    }
    
}
