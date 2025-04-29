package ispp_g2.gastrostock.empleado;

import java.util.List;
import java.util.Random;
import java.util.stream.StreamSupport;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserRepository userRepository;
    private final NegocioRepository negocioRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository, AuthoritiesRepository authoritiesRepository,
        UserRepository userRepository, NegocioRepository negocioRepository) {
        this.empleadoRepository = empleadoRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.userRepository = userRepository;
        this.negocioRepository = negocioRepository;
    }

    // Crear o actualizar un empleado
    @Transactional
    public Empleado saveEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("No se puede guardar un empleado null");
        }
        userRepository.save(empleado.getUser());
        return empleadoRepository.save(empleado);
    }

    @Transactional 
    public Empleado update(Integer id, Empleado empleado) {
       Empleado toUpdate = empleadoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado")); 
       BeanUtils.copyProperties(empleado, toUpdate, "id", "user", "negocio", "tokenEmpleado");
       return empleadoRepository.save(toUpdate);
    }

    // Obtener todos los empleados
    @Transactional(readOnly = true)
    public List<Empleado> getAllEmpleados() {
        Iterable<Empleado> empleados = empleadoRepository.findAll();
        return StreamSupport.stream(empleados.spliterator(), false).toList();
    }

    // Buscar empleado por ID
    @Transactional(readOnly = true)
    public Empleado getEmpleadoById(Integer id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    // Eliminar empleado
    @Transactional
    public void deleteEmpleado(Integer id) {
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
    public List<Empleado> getEmpleadoByNegocio(Integer id) {
        return empleadoRepository.findByNegocio(id);
    }

    @Transactional(readOnly = true)
    public List<Empleado> getEmpleadoByDueno(Integer id) {
        return empleadoRepository.findByDueno(id);
    }

    @Transactional(readOnly = true)
    public Empleado getEmpleadoByUser(Integer userId) {
        return empleadoRepository.findByUserId(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Empleado getEmpleadoByTokenEmpleado(String tokenEmpleado) {
        return empleadoRepository.findByTokenEmpleado(tokenEmpleado).orElse(null);
    }

    @Transactional(readOnly = true)
    public Empleado convertirDTOEmpleado(EmpleadoDTO empleadoDTO) {
        Empleado empleado = new Empleado();
        User user = userRepository.findByUsername(empleadoDTO.getUsername())
           .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        empleado.setFirstName(empleadoDTO.getFirstName());
        empleado.setLastName(empleadoDTO.getLastName());
        empleado.setEmail(empleadoDTO.getEmail());
        empleado.setTokenEmpleado(empleadoDTO.getTokenEmpleado());
        empleado.setNumTelefono(empleadoDTO.getNumTelefono());
        empleado.setDescripcion(empleadoDTO.getDescripcion());
        empleado.setUser(user);
        empleado.setNegocio(negocioRepository.findById(empleadoDTO.getNegocio())
            .orElseThrow(() -> new ResourceNotFoundException("Negocio no encontrado")));
        return empleado;
    }

    @Transactional(readOnly = true)
    public EmpleadoDTO convertirEmpleadoDTO(Empleado empleado) {
        EmpleadoDTO empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setFirstName(empleado.getFirstName());
        empleadoDTO.setLastName(empleado.getLastName());
        empleadoDTO.setEmail(empleado.getEmail());
        empleadoDTO.setTokenEmpleado(empleado.getTokenEmpleado());
        empleadoDTO.setNumTelefono(empleado.getNumTelefono());
        empleadoDTO.setDescripcion(empleado.getDescripcion());
        empleadoDTO.setUsername(empleado.getUser().getUsername());
        empleadoDTO.setPassword(empleado.getUser().getPassword());
        empleadoDTO.setNegocio(empleado.getNegocio().getId());
        return empleadoDTO;
    }

    
    
}
