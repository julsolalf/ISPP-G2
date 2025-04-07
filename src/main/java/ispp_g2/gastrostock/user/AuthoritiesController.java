package ispp_g2.gastrostock.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authorities")
public class AuthoritiesController {

    private AuthoritiesService authoritiesService;

    @Autowired
    public AuthoritiesController(AuthoritiesService authoritiesService) {
        this.authoritiesService = authoritiesService;
    }

    @GetMapping
    public ResponseEntity<List<Authorities>> findAll(@RequestHeader("Authorization") String token) {
        if(authoritiesService.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(authoritiesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Authorities> findById(@PathVariable("id") Integer id) {
        Authorities authorities = authoritiesService.findById(id);
        if(authorities == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }

    @GetMapping("/authority/{authority}")
    public ResponseEntity<Authorities> findByAuthority(@PathVariable("authority") String authority) {
        Authorities authorities = authoritiesService.findByAuthority(authority);
        if(authorities == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Authorities> saveAuthorities(@RequestBody @Valid Authorities authorities) {
        if(authorities==null)
            throw new IllegalArgumentException("Authorities cannot be null");
        return new ResponseEntity<>(authoritiesService.saveAuthorities(authorities), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Authorities> update(@PathVariable("id") Integer id, @RequestBody @Valid Authorities authorities) {
        Authorities authorities1 = authoritiesService.findById(id);
        if(authorities1 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        authorities.setId(id);
        return new ResponseEntity<>(authoritiesService.saveAuthorities(authorities), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        Authorities authorities = authoritiesService.findById(id);
        if(authorities == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        authoritiesService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
