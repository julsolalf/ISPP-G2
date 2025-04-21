package ispp_g2.gastrostock.testUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.user.Authorities;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import ispp_g2.gastrostock.user.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        Authorities authority = new Authorities();
        authority.setId(1);
        authority.setAuthority("DUENO");

        Authorities authority2 = new Authorities();
        authority2.setId(2);
        authority2.setAuthority("ADMIN");

        user1 = new User();
        user1.setId(1);
        user1.setUsername("juanito");
        user1.setPassword("password123");
        user1.setAuthority(authority);

        user2 = new User();
        user2.setId(2);
        user2.setUsername("maria");
        user2.setPassword("secure456");
        user2.setAuthority(authority2);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testFindAll_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void testFindUserById_ExistingId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        User result = userService.findUserById(1);

        assertNotNull(result);
        assertEquals("juanito", result.getUsername());
        verify(userRepository).findById(1);
    }

    @Test
    void testFindUserById_NonExistentId() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        User result = userService.findUserById(99);

        assertNull(result);
        verify(userRepository).findById(99);
    }

    @SuppressWarnings("null")
    @Test
    void testFindUserById_NullId() {
        when(userRepository.findById(null)).thenReturn(Optional.empty());

        User result = userService.findUserById(null);

        assertNull(result);
        verify(userRepository).findById(null);
    }

    @Test
    void testFindUserByUsername_ValidUsername() {
        when(userRepository.findByUsername("juanito")).thenReturn(Optional.of(user1));
    
        User result = userService.findUserByUsername("juanito");
    
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("juanito", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals("DUENO", result.getAuthority().getAuthority());
        verify(userRepository).findByUsername("juanito");
    }

    @Test
    void testFindUserByUsername_Null() {
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserByUsername(null));

        verify(userRepository).findByUsername(null);
    }

    @Test
    void testFindUserByUsernameAndPassword_ValidCredentials() {
        when(userRepository.findByUsernameAndPassword("maria", "secure456")).thenReturn(user2);

        User result = userService.findUserByUsernameAndPassword("maria", "secure456");

        assertNotNull(result);
        assertEquals("maria", result.getUsername());
        verify(userRepository).findByUsernameAndPassword("maria", "secure456");
    }

    @Test
    void testFindUserByUsernameAndPassword_BothNull() {
        when(userRepository.findByUsernameAndPassword(null, null)).thenReturn(null);

        User result = userService.findUserByUsernameAndPassword(null, null);

        assertNull(result);
        verify(userRepository).findByUsernameAndPassword(null, null);
    }

    @Test
    void testFindUserByAuthority_Valid() {
        when(userRepository.findByAuthority("DUENO")).thenReturn(user1);

        User result = userService.findUserByAuthority("DUENO");

        assertNotNull(result);
        assertEquals("juanito", result.getUsername());
        verify(userRepository).findByAuthority("DUENO");
    }

    @Test
    void testFindUserByAuthority_Null() {
        when(userRepository.findByAuthority(null)).thenReturn(null);

        User result = userService.findUserByAuthority(null);

        assertNull(result);
        verify(userRepository).findByAuthority(null);
    }

    @Test
    void testFindCurrentUser_Success() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("juanito");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByUsername("juanito")).thenReturn(Optional.of(user1));

        User result = userService.findCurrentUser();

        assertNotNull(result);
        assertEquals("juanito", result.getUsername());
        verify(userRepository).findUserByUsername("juanito");
    }

    @Test
    void testFindCurrentUser_NoAuth_ThrowsException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(ResourceNotFoundException.class, () -> userService.findCurrentUser());
    }

    @Test
    void testFindCurrentUser_UsernameNotFound_ThrowsException() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("unknown");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findCurrentUser());
    }

    @Test
    void testSaveUser_ValidUser() {
        when(userRepository.save(user1)).thenReturn(user1);

        User result = userService.saveUser(user1);

        assertNotNull(result);
        assertEquals("juanito", result.getUsername());
        verify(userRepository).save(user1);
    }

    @SuppressWarnings("null")
    @Test
    void testSaveUser_NullUser() {
        when(userRepository.save(null)).thenReturn(null);

        User result = userService.saveUser(null);

        assertNull(result);
        verify(userRepository).save(null);
    }

    @Test
    void testDeleteUser_ValidId() {
        doNothing().when(userRepository).deleteById(1);

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void testDeleteUser_NonExistentId_StillCallsRepo() {
        doThrow(new RuntimeException("No existe")).when(userRepository).deleteById(999);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(999));
        verify(userRepository).deleteById(999);
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteUser_NullId() {
        doThrow(new IllegalArgumentException("ID cannot be null")).when(userRepository).deleteById(null);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
        verify(userRepository).deleteById(null);
    }

    @Test
    void testFindAll_NullIterableHandledGracefully() {
        when(userRepository.findAll()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> userService.findAll());
    }

    @Test
    void testFindUserByUsername_EmptyString() {
        when(userRepository.findByUsername("")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> userService.findUserByUsername(""));
        
        verify(userRepository).findByUsername("");
    }
    @Test
    void testFindUserByUsernameAndPassword_EmptyUsername() {
        when(userRepository.findByUsernameAndPassword("", "secure456")).thenReturn(null);

        User result = userService.findUserByUsernameAndPassword("", "secure456");

        assertNull(result);
        verify(userRepository).findByUsernameAndPassword("", "secure456");
    }

    @Test
    void testFindUserByUsernameAndPassword_EmptyPassword() {
        when(userRepository.findByUsernameAndPassword("maria", "")).thenReturn(null);

        User result = userService.findUserByUsernameAndPassword("maria", "");

        assertNull(result);
        verify(userRepository).findByUsernameAndPassword("maria", "");
    }

    @Test
    void testFindUserByAuthority_EmptyAuthority() {
        when(userRepository.findByAuthority("")).thenReturn(null);

        User result = userService.findUserByAuthority("");

        assertNull(result);
        verify(userRepository).findByAuthority("");
    }

    @Test
    void testSaveUser_ModifiedObjectIsReturned() {
        User modified = new User();
        modified.setId(99);
        modified.setUsername("otro");
        modified.setPassword("1234");

        when(userRepository.save(any(User.class))).thenReturn(modified);

        User result = userService.saveUser(user1);

        assertNotNull(result);
        assertEquals("otro", result.getUsername());
    }

    @Test
    void testSaveUser_EmptyUser() {
        User empty = new User();
        when(userRepository.save(empty)).thenReturn(empty);

        User result = userService.saveUser(empty);

        assertNotNull(result);
        assertNull(result.getUsername());
    }

    @Test
    void testDeleteUser_MinValueId() {
        doNothing().when(userRepository).deleteById(Integer.MIN_VALUE);

        userService.deleteUser(Integer.MIN_VALUE);

        verify(userRepository).deleteById(Integer.MIN_VALUE);
    }

    @Test
    void testDeleteUser_MaxValueId() {
        doNothing().when(userRepository).deleteById(Integer.MAX_VALUE);

        userService.deleteUser(Integer.MAX_VALUE);

        verify(userRepository).deleteById(Integer.MAX_VALUE);
    }

    @Test
    void testFindUserById_MinValueId() {
        when(userRepository.findById(Integer.MIN_VALUE)).thenReturn(Optional.empty());

        User result = userService.findUserById(Integer.MIN_VALUE);

        assertNull(result);
    }

    @Test
    void testFindUserById_MaxValueId() {
        when(userRepository.findById(Integer.MAX_VALUE)).thenReturn(Optional.of(user2));

        User result = userService.findUserById(Integer.MAX_VALUE);

        assertNotNull(result);
    }

    @Test
    void testFindUserByUsernameAndPassword_WeirdChars() {
        String weirdUsername = "ñ@!#";
        String weirdPass = "🔥🐍";

        when(userRepository.findByUsernameAndPassword(weirdUsername, weirdPass)).thenReturn(null);

        User result = userService.findUserByUsernameAndPassword(weirdUsername, weirdPass);

        assertNull(result);
    }

    @Test
    void testSaveUser_NullAuthority() {
        user1.setAuthority(null);
        when(userRepository.save(user1)).thenReturn(user1);

        User result = userService.saveUser(user1);

        assertNotNull(result);
        assertNull(result.getAuthority());
    }

    @Test
    void testFindCurrentUser_AuthenticationWithNullName() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(null);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findUserByUsername(null)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findCurrentUser());
    }

    @Test
    void testFindUserByUsernameAndPassword_CaseSensitivity() {
        when(userRepository.findByUsernameAndPassword("Maria", "SECURE456")).thenReturn(null);

        User result = userService.findUserByUsernameAndPassword("Maria", "SECURE456");

        assertNull(result);
    }

    @Test
    void testFindUserByUsernameAndPassword_Whitespace() {
        when(userRepository.findByUsernameAndPassword(" maria ", " secure456 ")).thenReturn(null);

        User result = userService.findUserByUsernameAndPassword(" maria ", " secure456 ");

        assertNull(result);
    }

    @Test
    void testFindUserByAuthority_SpecialCharacters() {
        when(userRepository.findByAuthority("@@@")).thenReturn(null);

        User result = userService.findUserByAuthority("@@@");

        assertNull(result);
    }

    @Test
    void testSaveUser_ThrowsException() {
        when(userRepository.save(user1)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> userService.saveUser(user1));
    }

    @Test
    void testDeleteUser_ThrowsUnexpectedException() {
        doThrow(new IllegalStateException("Something broke")).when(userRepository).deleteById(123);

        assertThrows(IllegalStateException.class, () -> userService.deleteUser(123));
    }

    @Test
    void testFindUserByUsername_NullReturnFromRepo() {
        when(userRepository.findByUsername("juanito")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserByUsername("juanito"));

    }

}

