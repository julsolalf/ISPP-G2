package ispp_g2.gastrostock.testDueño;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.dueño.DueñoRepository;
import ispp_g2.gastrostock.dueño.DueñoService;

@ExtendWith(MockitoExtension.class)
class DueñoServiceTest {
    
    @Mock
    private DueñoRepository repo;
    
    @InjectMocks
    private DueñoService service;
    
    private Dueño DueñoP;
    
    @BeforeEach
    void setUp() {
        DueñoP = new Dueño();
        DueñoP.setId(1);
        DueñoP.setTokenDueño("testToken");
        DueñoP.setEmail("test@example.com");
    }
    
    @Test
    void testSaveDueño() {
        when(repo.save(DueñoP)).thenReturn(DueñoP);
        Dueño saved = service.saveDueño(DueñoP);
        assertNotNull(saved);
        assertEquals("testToken", saved.getTokenDueño());
    }
    
    @Test
    void testGetAllDueños() {
        when(repo.findAll()).thenReturn(List.of(DueñoP));
        List<Dueño> dueños = service.getAllDueños();
        assertFalse(dueños.isEmpty());
    }
    
    @Test
    void testGetDueñoById() {
        when(repo.findById(1)).thenReturn(Optional.of(DueñoP));
        Optional<Dueño> found = service.getDueñoById(1);
        assertTrue(found.isPresent());
        assertEquals(1, found.get().getId());
    }

    @Test
    void testGetDueñoByEmail() {
        when(repo.findByEmail("test@example.com")).thenReturn(Optional.of(DueñoP));
        Optional<Dueño> found = service.getDueñoByEmail("test@example.com");
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }
    
    @Test
    void testDeleteDueño() {
        doNothing().when(repo).deleteById(1);
        service.deleteDueño(1);
        verify(repo, times(1)).deleteById(1);
    }
    
    @Test
    void testAuthenticateDueñoSuccess() {
        when(repo.findByEmail("test@example.com")).thenReturn(Optional.of(DueñoP));
        String token = service.authenticateDueño("test@example.com", "testToken");
        assertNotNull(token);
    }

    @Test
    void testAuthenticateDueñoFailure() {
        when(repo.findByEmail("wrong@example.com")).thenReturn(Optional.empty());
        String token = service.authenticateDueño("wrong@example.com", "wrongToken");
        assertNull(token);
    }
}

