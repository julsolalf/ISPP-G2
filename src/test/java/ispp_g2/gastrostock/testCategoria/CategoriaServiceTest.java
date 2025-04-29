package ispp_g2.gastrostock.testCategoria;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.categorias.CategoriaService;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.Negocio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Captor
    private ArgumentCaptor<Categoria> categoriaCaptor;

    private Categoria categoriaValida;
    private Categoria categoriaInvalida;
    private List<Categoria> categoriaList;
    private Negocio negocio;

    @BeforeEach
    void setUp() {
        // Crear un negocio
        negocio = new Negocio();
        negocio.setId(1);

        // Crear una categoría válida
        categoriaValida = new Categoria();
        categoriaValida.setId(1);
        categoriaValida.setName("Bebidas");
        categoriaValida.setNegocio(negocio);
        categoriaValida.setPertenece(Pertenece.INVENTARIO);

        // Crear una categoría con datos inválidos (por ejemplo, sin nombre, sin negocio y sin pertenece)
        categoriaInvalida = new Categoria();
        categoriaInvalida.setId(2);
        categoriaInvalida.setName(""); // nombre vacío
        categoriaInvalida.setNegocio(null); // negocio nulo
        categoriaInvalida.setPertenece(null); // pertenece nulo

        // Lista de categorías para tests
        categoriaList = new ArrayList<>();
        categoriaList.add(categoriaValida);
    }

    // TEST PARA getById()

    @Test
    void testGetById_Success() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaValida));

        Categoria result = categoriaService.getById(1);

        assertNotNull(result);
        assertEquals("Bebidas", result.getName());
        verify(categoriaRepository, times(1)).findById(1);
    }

    @Test
    void testGetById_NotFound() {
        // Dado que el repositorio devuelve vacío
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());
    
        // Entonces esperamos que getById lance ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.getById(99);
        });
    
        verify(categoriaRepository, times(1)).findById(99);
    }

    // TEST PARA getCategorias()

    @Test
    void testGetCategorias_Success() {
        when(categoriaRepository.findAll()).thenReturn(categoriaList);

        List<Categoria> result = categoriaService.getCategorias();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testGetCategorias_EmptyList() {
        when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Categoria> result = categoriaService.getCategorias();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testGetCategorias_NullList() {
        // Si el repositorio devuelve null, StreamSupport lanzará NullPointerException
        when(categoriaRepository.findAll()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            categoriaService.getCategorias();
        });
        verify(categoriaRepository, times(1)).findAll();
    }

    // TEST PARA getCategoriasByNegocioId()

    @Test
    void testGetCategoriasByNegocioId_Success() {
        when(categoriaRepository.findByNegocioId(1)).thenReturn(categoriaList);

        List<Categoria> result = categoriaService.getCategoriasByNegocioId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoriaRepository, times(1)).findByNegocioId(1);
    }

    @Test
    void testGetCategoriasByNegocioId_Empty() {
        when(categoriaRepository.findByNegocioId(1)).thenReturn(Collections.emptyList());

        List<Categoria> result = categoriaService.getCategoriasByNegocioId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository, times(1)).findByNegocioId(1);
    }

    // TEST PARA getCategoriasByName()

    @Test
    void testGetCategoriasByName_Success() {
        when(categoriaRepository.findByName("Bebidas")).thenReturn(categoriaList);

        List<Categoria> result = categoriaService.getCategoriasByName("Bebidas");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoriaRepository, times(1)).findByName("Bebidas");
    }

    @Test
    void testGetCategoriasByName_Empty() {
        when(categoriaRepository.findByName("NoExiste")).thenReturn(Collections.emptyList());

        List<Categoria> result = categoriaService.getCategoriasByName("NoExiste");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository, times(1)).findByName("NoExiste");
    }

    // TEST PARA save()

    @Test
    void testSaveCategoria_Success() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaValida);

        Categoria saved = categoriaService.save(categoriaValida);

        assertNotNull(saved);
        assertEquals("Bebidas", saved.getName());
        verify(categoriaRepository, times(1)).save(categoriaValida);
    }

    @Test
    void testSaveCategoria_InvalidData() {
        // Simulamos que guardar una categoría inválida lanza excepción
        when(categoriaRepository.save(categoriaInvalida)).thenThrow(new IllegalArgumentException("Invalid data"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.save(categoriaInvalida);
        });
        assertEquals("Invalid data", exception.getMessage());
        verify(categoriaRepository, times(1)).save(categoriaInvalida);
    }

    @Test
    void testSaveCategoria_Null() {
        // Simulamos que guardar null lanza excepción
        when(categoriaRepository.save(null)).thenThrow(new IllegalArgumentException("Categoria cannot be null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.save(null);
        });
        assertEquals("Categoria cannot be null", exception.getMessage());
        verify(categoriaRepository, times(1)).save(null);
    }

    // TEST PARA delete()

    @Test
    void testDeleteCategoria_Success() {
        doNothing().when(categoriaRepository).deleteById(1);

        categoriaService.delete(1);

        verify(categoriaRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCategoria_NotFound() {
        doThrow(new RuntimeException("Categoria not found")).when(categoriaRepository).deleteById(99);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.delete(99);
        });
        assertEquals("Categoria not found", exception.getMessage());
        verify(categoriaRepository, times(1)).deleteById(99);
    }

    @Test
    void testDeleteCategoria_NullId() {
        doThrow(new IllegalArgumentException("ID cannot be null")).when(categoriaRepository).deleteById(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.delete(null);
        });
        assertEquals("ID cannot be null", exception.getMessage());
        verify(categoriaRepository, times(1)).deleteById(null);
    }
}
