package ispp_g2.gastrostock.mesa;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ispp_g2.gastrostock.exceptions.BadRequestException;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private MesaService ms;

    @GetMapping
	public ResponseEntity<List<Mesa>> findAll() {
		return new ResponseEntity<>((List<Mesa>) ms.getMesas(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Mesa> findMesa(@PathVariable("id") int id) {
		Mesa MesaToGet = ms.getById(id);
		if (MesaToGet == null)
			throw new ResourceNotFoundException("Mesa with id " + id + " not found!");
		return new ResponseEntity<Mesa>(MesaToGet, HttpStatus.OK);
	}

    @GetMapping("/numeroMesa/{numeroMesa}")
	public ResponseEntity<Mesa> findByNumeroMesa(@PathVariable("numeroMesa") int numeroMesa) {
		Mesa MesaToGet = ms.getByNumeroMesa(numeroMesa);
		if (MesaToGet == null)
			throw new ResourceNotFoundException("Mesa with numeroMesa " + numeroMesa + " not found!");
		return new ResponseEntity<Mesa>(MesaToGet, HttpStatus.OK);
	}

    @GetMapping("/numeroAsientos/{numeroAsientos}")
	public ResponseEntity<List<Mesa>> findByNumeroAsientos(@PathVariable("numeroAsientos") int numeroAsientos) {
		List<Mesa> MesasToGet = ms.getMesasByNumeroAsientos(numeroAsientos);
		if (MesasToGet == null)
			throw new ResourceNotFoundException("Mesa with numeroAsientos " + numeroAsientos + " not found!");
		return new ResponseEntity<List<Mesa>>(MesasToGet, HttpStatus.OK);
	}

    @GetMapping("/{name}")
	public ResponseEntity<Mesa> findByName(@PathVariable("name") String name) {
		Mesa MesaToGet = ms.getByName(name);
		if (MesaToGet == null)
			throw new ResourceNotFoundException("Mesa with name " + name + " not found!");
		return new ResponseEntity<Mesa>(MesaToGet, HttpStatus.OK);
	}

    @PutMapping("/{id}")
	public ResponseEntity<Void> modifyMesa(@RequestBody @Valid Mesa newMesa, BindingResult br,
			@PathVariable("id") int id) {
		Mesa MesaToUpdate = this.findMesa(id).getBody();
		if (br.hasErrors())
			throw new BadRequestException(br.getAllErrors());
		else if (newMesa.getId() == null || !newMesa.getId().equals(id))
			throw new BadRequestException("Mesa id is not consistent with resource URL:" + id);
		else {
			BeanUtils.copyProperties(newMesa, MesaToUpdate, "id");
			ms.saveMesa(MesaToUpdate);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{numeroMesa}")
	public ResponseEntity<Void> deleteMesa(@PathVariable("numeroMesa") Integer numeroMesa) {
		findByNumeroMesa(numeroMesa);
		ms.deleteMesaByNumeroMesa(numeroMesa);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
}
