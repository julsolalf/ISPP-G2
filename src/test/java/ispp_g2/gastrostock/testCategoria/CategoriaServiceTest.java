package ispp_g2.gastrostock.testCategoria;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.categorias.CategoriaDTO;
import ispp_g2.gastrostock.categorias.CategoriaRepository;
import ispp_g2.gastrostock.categorias.CategoriaService;
import ispp_g2.gastrostock.categorias.Pertenece;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioRepository;

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
    
    @Mock
    private NegocioRepository negocioRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Captor
    private ArgumentCaptor<Categoria> categoriaCaptor;

    private Categoria categoriaValida;
    private Categoria categoriaInvalida;
    private List<Categoria> categoriaList;
    private List<Categoria> categoriaInventarioList;
    private List<Categoria> categoriaVentaList;
    private Negocio negocio;
    private CategoriaDTO categoriaDTO;

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

        // Crear una categoría con datos inválidos
        categoriaInvalida = new Categoria();
        categoriaInvalida.setId(2);
        categoriaInvalida.setName(""); 
        categoriaInvalida.setNegocio(null);
        categoriaInvalida.setPertenece(null);

        // Lista de categorías para tests
        categoriaList = new ArrayList<>();
        categoriaList.add(categoriaValida);
        
        // Lista de categorías de inventario
        categoriaInventarioList = new ArrayList<>();
        Categoria categoriaInventario = new Categoria();
        categoriaInventario.setId(3);
        categoriaInventario.setName("Materias Primas");
        categoriaInventario.setNegocio(negocio);
        categoriaInventario.setPertenece(Pertenece.INVENTARIO);
        categoriaInventarioList.add(categoriaValida);
        categoriaInventarioList.add(categoriaInventario);
        
        // Lista de categorías de venta
        categoriaVentaList = new ArrayList<>();
        Categoria categoriaVenta = new Categoria();
        categoriaVenta.setId(4);
        categoriaVenta.setName("Menús");
        categoriaVenta.setNegocio(negocio);
        categoriaVenta.setPertenece(Pertenece.VENTA);
        categoriaVentaList.add(categoriaVenta);
        
        // DTO para tests
        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1);
        categoriaDTO.setNombre("Bebidas");
        categoriaDTO.setNegocioId(1);
        categoriaDTO.setPertenece(Pertenece.INVENTARIO);
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

    @Test
    void testGetCategoriasInventarioByNegocioId_Success() {
        when(categoriaRepository.findInventarioByNegocioId(1)).thenReturn(categoriaInventarioList);

        List<Categoria> result = categoriaService.getCategoriasInventarioByNegocioId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Pertenece.INVENTARIO, result.get(0).getPertenece());
        assertEquals(Pertenece.INVENTARIO, result.get(1).getPertenece());
        verify(categoriaRepository, times(1)).findInventarioByNegocioId(1);
    }

    @Test
    void testGetCategoriasInventarioByNegocioId_EmptyList() {
        when(categoriaRepository.findInventarioByNegocioId(2)).thenReturn(Collections.emptyList());

        List<Categoria> result = categoriaService.getCategoriasInventarioByNegocioId(2);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository, times(1)).findInventarioByNegocioId(2);
    }

    @Test
    void testGetCategoriasInventarioByNegocioId_NullId() {
        when(categoriaRepository.findInventarioByNegocioId(null)).thenReturn(Collections.emptyList());

        List<Categoria> result = categoriaService.getCategoriasInventarioByNegocioId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository, times(1)).findInventarioByNegocioId(null);
    }

    // TEST PARA getCategoriasVentaByNegocioId()

    @Test
    void testGetCategoriasVentaByNegocioId_Success() {
        when(categoriaRepository.findVentaByNegocioId(1)).thenReturn(categoriaVentaList);

        List<Categoria> result = categoriaService.getCategoriasVentaByNegocioId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Pertenece.VENTA, result.get(0).getPertenece());
        assertEquals("Menús", result.get(0).getName());
        verify(categoriaRepository, times(1)).findVentaByNegocioId(1);
    }

    @Test
    void testGetCategoriasVentaByNegocioId_EmptyList() {
        when(categoriaRepository.findVentaByNegocioId(2)).thenReturn(Collections.emptyList());

        List<Categoria> result = categoriaService.getCategoriasVentaByNegocioId(2);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository, times(1)).findVentaByNegocioId(2);
    }

    @Test
    void testGetCategoriasVentaByNegocioId_NullId() {
        when(categoriaRepository.findVentaByNegocioId(null)).thenReturn(Collections.emptyList());

        List<Categoria> result = categoriaService.getCategoriasVentaByNegocioId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoriaRepository, times(1)).findVentaByNegocioId(null);
    }

    // TEST PARA update()

    @Test
    void testUpdateCategoria_Success() {
        Categoria categoriaToUpdate = new Categoria();
        categoriaToUpdate.setId(1);
        categoriaToUpdate.setName("Bebidas Actualizadas");
        categoriaToUpdate.setNegocio(negocio);
        categoriaToUpdate.setPertenece(Pertenece.INVENTARIO);
        
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaValida));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaToUpdate);

        Categoria result = categoriaService.update(1, categoriaToUpdate);

        assertNotNull(result);
        assertEquals("Bebidas Actualizadas", result.getName());
        verify(categoriaRepository).findById(1);
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void testUpdateCategoria_NotFound() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.update(99, categoriaValida);
        });

        verify(categoriaRepository).findById(99);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void testUpdateCategoria_NullCategoria() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaValida));

        assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.update(1, null);
        });

        verify(categoriaRepository).findById(1);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    // TEST PARA convertirCategoria()

    @Test
    void testConvertirCategoria_Success() {
        when(negocioRepository.findById(1)).thenReturn(Optional.of(negocio));

        Categoria result = categoriaService.convertirCategoria(categoriaDTO);

        assertNotNull(result);
        assertEquals("Bebidas", result.getName());
        assertEquals(negocio, result.getNegocio());
        assertEquals(Pertenece.INVENTARIO, result.getPertenece());
        verify(negocioRepository).findById(1);
    }

    @Test
    void testConvertirCategoria_NegocioNotFound() {
        when(negocioRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.convertirCategoria(categoriaDTO);
        });

        verify(negocioRepository).findById(1);
    }

    @Test
    void testConvertirCategoria_NullDTO() {
        assertThrows(NullPointerException.class, () -> {
            categoriaService.convertirCategoria(null);
        });
        
        verify(negocioRepository, never()).findById(anyInt());
    }

}
