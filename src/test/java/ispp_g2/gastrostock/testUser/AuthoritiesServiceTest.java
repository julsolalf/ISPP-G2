package ispp_g2.gastrostock.testUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.AuthoritiesService;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthoritiesServiceTest {

    @Mock
    private AuthoritiesRepository authoritiesRepository;

    @InjectMocks
    private AuthoritiesService authoritiesService;

    private Authorities auth1;
    private Authorities auth2;

    @BeforeEach
    void setUp() {
        auth1 = new Authorities();
        auth1.setId(1);
        auth1.setAuthority("ROLE_USER");
        auth2 = new Authorities();
        auth2.setId(2);
        auth2.setAuthority("ROLE_ADMIN");
    }

    @Test
    void testFindByAuthority() {
        when(authoritiesRepository.findByAuthority("ROLE_USER")).thenReturn(auth1);
        Authorities result = authoritiesService.findByAuthority("ROLE_USER");
        assertNotNull(result);
        assertEquals("ROLE_USER", result.getAuthority());
        verify(authoritiesRepository).findByAuthority("ROLE_USER");
    }

    @Test
    void testFindByAuthority_NotFound() {
        when(authoritiesRepository.findByAuthority("NON_EXISTENT")).thenReturn(null);
        Authorities result = authoritiesService.findByAuthority("NON_EXISTENT");
        assertNull(result);
        verify(authoritiesRepository).findByAuthority("NON_EXISTENT");
    }

    @Test
    void testFindByAuthority_Null() {
        when(authoritiesRepository.findByAuthority(null)).thenReturn(null);
        Authorities result = authoritiesService.findByAuthority(null);
        assertNull(result);
        verify(authoritiesRepository).findByAuthority(null);
    }

    @Test
    void testFindById_Found() {
        when(authoritiesRepository.findById(1)).thenReturn(Optional.of(auth1));
        Authorities result = authoritiesService.findById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(authoritiesRepository).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        when(authoritiesRepository.findById(999)).thenReturn(Optional.empty());
        Authorities result = authoritiesService.findById(999);
        assertNull(result);
        verify(authoritiesRepository).findById(999);
    }

    @Test
    void testFindAll_ReturnsList() {
        when(authoritiesRepository.findAll()).thenReturn(Arrays.asList(auth1, auth2));
        List<Authorities> result = authoritiesService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(authoritiesRepository).findAll();
    }

    @Test
    void testFindAll_Empty() {
        when(authoritiesRepository.findAll()).thenReturn(Collections.emptyList());
        List<Authorities> result = authoritiesService.findAll();
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(authoritiesRepository).findAll();
    }

    @Test
    void testSaveAuthorities() {
        when(authoritiesRepository.save(any(Authorities.class))).thenReturn(auth1);
        Authorities result = authoritiesService.saveAuthorities(auth1);
        assertNotNull(result);
        assertEquals("ROLE_USER", result.getAuthority());
        verify(authoritiesRepository).save(auth1);
    }

    @SuppressWarnings("null")
    @Test
    void testSaveAuthorities_NullThrows() {
        when(authoritiesRepository.save(null)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> authoritiesService.saveAuthorities(null));
        verify(authoritiesRepository).save(null);
    }

    @Test
    void testSaveAuthorities_RepositoryException() {
        when(authoritiesRepository.save(any(Authorities.class))).thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> authoritiesService.saveAuthorities(auth2));
        assertEquals("DB error", ex.getMessage());
        verify(authoritiesRepository).save(auth2);
    }

    @Test
    void testDelete() {
        doNothing().when(authoritiesRepository).deleteById(1);
        authoritiesService.delete(1);
        verify(authoritiesRepository).deleteById(1);
    }

    @SuppressWarnings("null")
    @Test
    void testDelete_NullIdThrows() {
        doThrow(IllegalArgumentException.class).when(authoritiesRepository).deleteById(null);
        assertThrows(IllegalArgumentException.class, () -> authoritiesService.delete(null));
        verify(authoritiesRepository).deleteById(null);
    }

    @Test
    void testDelete_IdNotFound_NoException() {
        doNothing().when(authoritiesRepository).deleteById(999);
        authoritiesService.delete(999);
        verify(authoritiesRepository).deleteById(999);
    }
}
