package ispp_g2.gastrostock.testNegocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.negocio.NegocioService;


class NegocioServiceTest{

    @Mock
    private NegocioRepository negocioRepository;

    @InjectMocks
    private NegocioService negocioService;

    private Negocio negocio;
    private Negocio negocio2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Crear un Dueño para asociar al Negocio
        Dueño dueño = new Dueño();
        dueño.setId(1);
        dueño.setFirstName("Juan");
        dueño.setLastName("García");
        dueño.setEmail("dueno@gmail.com");
        dueño.setNumTelefono("654321987");
        dueño.setTokenDueño("TOKEN123");
        
        // Configurar un Negocio para pruebas
        negocio = new Negocio();
        negocio.setId(1);
        negocio.setName("Restaurante La Tasca");
        negocio.setDireccion("Calle Principal 123");
        negocio.setCiudad("Sevilla");
        negocio.setPais("España");
        negocio.setCodigoPostal("41001");
        negocio.setTokenNegocio(12345);
        negocio.setDueño(dueño);
        
        // Configurar un segundo Negocio para pruebas de listas
        negocio2 = new Negocio();
        negocio2.setId(2);
        negocio2.setName("Bar El Rincón");
        negocio2.setDireccion("Avenida de la Constitución 45");
        negocio2.setCiudad("Sevilla");
        negocio2.setPais("España");
        negocio2.setCodigoPostal("41001");
        negocio2.setTokenNegocio(67890);
        negocio2.setDueño(dueño);
    }


    @Test
    void testGetById() {

        when(negocioRepository.findById(1)).thenReturn(negocio);
        
        Negocio result = negocioService.getById(1);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Restaurante La Tasca", result.getName());
        verify(negocioRepository, times(1)).findById(1);
    }
    
    @Test
    void testGetNegocios() {

        List<Negocio> negocios = Arrays.asList(negocio, negocio2);
        when(negocioRepository.findAll()).thenReturn(negocios);
        
        List<Negocio> result = negocioService.getNegocios();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Restaurante La Tasca", result.get(0).getName());
        assertEquals("Bar El Rincón", result.get(1).getName());
        verify(negocioRepository, times(1)).findAll();
    }
    
    @Test
    void testGetByName() {

        when(negocioRepository.findByName("Restaurante La Tasca")).thenReturn(negocio);
        
        Negocio result = negocioService.getByName("Restaurante La Tasca");
        
        assertNotNull(result);
        assertEquals("Restaurante La Tasca", result.getName());
        verify(negocioRepository, times(1)).findByName("Restaurante La Tasca");
    }
    
    @Test
    void testGetByDireccion() {

        when(negocioRepository.findByDireccion("Calle Principal 123")).thenReturn(negocio);
        
        Negocio result = negocioService.getByDireccion("Calle Principal 123");
        
        assertNotNull(result);
        assertEquals("Calle Principal 123", result.getDireccion());
        verify(negocioRepository, times(1)).findByDireccion("Calle Principal 123");
    }
    
    @Test
    void testGetByToken() {

        when(negocioRepository.findByTokenNegocio(12345)).thenReturn(negocio);
        
        Negocio result = negocioService.getByToken(12345);
        
        assertNotNull(result);
        assertEquals(12345, result.getTokenNegocio());
        verify(negocioRepository, times(1)).findByTokenNegocio(12345);
    }
    
    @Test
    void testGetByCiudad() {

        List<Negocio> negocios = Arrays.asList(negocio, negocio2);
        when(negocioRepository.findByCiudad("Sevilla")).thenReturn(negocios);
        
        List<Negocio> result = negocioService.getByCiudad("Sevilla");
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Sevilla", result.get(0).getCiudad());
        assertEquals("Sevilla", result.get(1).getCiudad());
        verify(negocioRepository, times(1)).findByCiudad("Sevilla");
    }
    
    @Test
    void testGetByPais() {

        List<Negocio> negocios = Arrays.asList(negocio, negocio2);
        when(negocioRepository.findByPais("España")).thenReturn(negocios);
        
        List<Negocio> result = negocioService.getByPais("España");
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("España", result.get(0).getPais());
        assertEquals("España", result.get(1).getPais());
        verify(negocioRepository, times(1)).findByPais("España");
    }
    
    @Test
    void testGetByCodigoPostal() {

        List<Negocio> negocios = Arrays.asList(negocio, negocio2);
        when(negocioRepository.findByCodigoPostal("41001")).thenReturn(negocios);
        
        List<Negocio> result = negocioService.getByCodigoPostal("41001");
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("41001", result.get(0).getCodigoPostal());
        assertEquals("41001", result.get(1).getCodigoPostal());
        verify(negocioRepository, times(1)).findByCodigoPostal("41001");
    }
    
    @Test
    void testSaveNegocio() {

        when(negocioRepository.save(negocio)).thenReturn(negocio);
        
        Negocio result = negocioService.saveNegocio(negocio);
        
        assertNotNull(result);
        assertEquals("Restaurante La Tasca", result.getName());
        verify(negocioRepository, times(1)).save(negocio);
    }
    
    @Test
    void testDeleteNegocioByToken() {

        when(negocioRepository.findByTokenNegocio(12345)).thenReturn(negocio);
        doNothing().when(negocioRepository).delete(negocio);
        
        negocioService.deleteNegocioByToken(12345);
        
        verify(negocioRepository, times(1)).findByTokenNegocio(12345);
        verify(negocioRepository, times(1)).delete(negocio);
    }
}

