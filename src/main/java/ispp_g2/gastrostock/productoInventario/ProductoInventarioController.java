package ispp_g2.gastrostock.productoInventario;

import java.util.List;

import ispp_g2.gastrostock.categorias.CategoriaService;
import ispp_g2.gastrostock.dueno.Dueno;
import ispp_g2.gastrostock.dueno.DuenoService;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productosInventario")
public class ProductoInventarioController {

    private final ProductoInventarioService productoInventarioService;
	private final UserService userService;

	private final String admin = "admin";
	private final String dueno = "dueno";
	private final String empleado = "empleado";
	private final EmpleadoService empleadoService;
	private final DuenoService duenoService;
	private final CategoriaService categoriaService;
	private final NegocioService negocioService;

	@Autowired
	public ProductoInventarioController(ProductoInventarioService productoInventarioService, UserService userService, EmpleadoService empleadoService, DuenoService duenoService, CategoriaService categoriaService, NegocioService negocioService) {
		this.productoInventarioService = productoInventarioService;
		this.userService = userService;
		this.empleadoService = empleadoService;
		this.duenoService = duenoService;
		this.categoriaService = categoriaService;
		this.negocioService = negocioService;
	}

    @GetMapping
	public ResponseEntity<List<ProductoInventario>> findAll() {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productosInventario;
		switch (user.getAuthority().getAuthority()) {
			case admin -> productosInventario = productoInventarioService.getProductosInventario();
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productosInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId());
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productosInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId());
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if(productosInventario.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(productosInventario, HttpStatus.OK);
	}

	@GetMapping("/dto")
	public ResponseEntity<List<ProductoInventarioDTO>> findAllDTO() {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productosInvetario;
		switch (user.getAuthority().getAuthority()) {
			case admin -> productosInvetario = productoInventarioService.getProductosInventario();
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productosInvetario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId());
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productosInvetario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId());
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if(productosInvetario.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		List<ProductoInventarioDTO> productosInventarioDto = productosInvetario
				.stream()
				.map(productoInventarioService::convertirProductoInventarioDTO)
				.toList();

		return new ResponseEntity<>(productosInventarioDto, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductoInventario> findProductoInventario(@PathVariable String id) {
		try {
			User user = userService.findCurrentUser();
			Integer idNum = Integer.parseInt(id);
			ProductoInventario productoInventario;
			switch (user.getAuthority().getAuthority()){
				case admin -> productoInventario = productoInventarioService.getById(idNum);
				case empleado -> {
					Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
					productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
							.filter(p -> p.getId().equals(idNum))
							.findFirst()
							.orElse(null);
				}
				case dueno -> {
					Dueno currDueno = duenoService.getDuenoByUser(user.getId());
					productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
							.filter(p -> p.getId().equals(idNum))
							.findFirst()
							.orElse(null);
				}
				default -> {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
			}
			if (productoInventario == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(productoInventario, HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/dto/{id}")
	public ResponseEntity<ProductoInventarioDTO> findProductoInventarioDTO(@PathVariable Integer id) {
		User user = userService.findCurrentUser();
		ProductoInventario productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getById(id);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getId().equals(id))
						.findFirst()
						.orElse(null);
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getId().equals(id))
						.findFirst()
						.orElse(null);
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		ProductoInventarioDTO productoInventarioDTO = productoInventarioService.convertirProductoInventarioDTO(productoInventario);
		if (productoInventarioDTO == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventarioDTO, HttpStatus.OK);
	}

	@GetMapping("/negocio/{negocioId}")
	public ResponseEntity<List<ProductoInventario>> findByNegocioId(@PathVariable("negocioId") Integer negocioId) {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getProductoInventarioByNegocioId(negocioId);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getCategoria().getNegocio().getId().equals(negocioId))
						.toList();
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getCategoria().getNegocio().getId().equals(negocioId))
						.toList();
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@GetMapping("/dto/negocio/{negocioId}")
	public ResponseEntity<List<ProductoInventarioDTO>> findByNegocioIdDTO(@PathVariable("negocioId") Integer negocioId) {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getProductoInventarioByNegocioId(negocioId);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getCategoria().getNegocio().getId().equals(negocioId))
						.toList();
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getCategoria().getNegocio().getId().equals(negocioId))
						.toList();
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		List<ProductoInventarioDTO> productosInventarioDto = productoInventario
				.stream()
				.map(productoInventarioService::convertirProductoInventarioDTO)
				.toList();

		return new ResponseEntity<>(productosInventarioDto, HttpStatus.OK);
	}

    @GetMapping("/categoria/{categoria}")
	public ResponseEntity<List<ProductoInventario>> findByCategoriaName(@PathVariable("categoria") String categoriasInventario) {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getProductoInventarioByCategoriaName(categoriasInventario);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getCategoria().getName().equals(categoriasInventario))
						.toList();
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getCategoria().getName().equals(categoriasInventario))
						.toList();
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

    @GetMapping("/name/{name}")
	public ResponseEntity<ProductoInventario> findByName(@PathVariable("name") String name) {
		User user = userService.findCurrentUser();
		ProductoInventario productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getByName(name);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getName().equals(name))
						.findFirst()
						.orElse(null);
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getName().equals(name))
						.findFirst()
						.orElse(null);
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@GetMapping("/precioCompra/{precioCompra}")
	public ResponseEntity<List<ProductoInventario>> findByPrecioCompra(@PathVariable("precioCompra") Double precioCompra) {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getProductoInventarioByPrecioCompra(precioCompra);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getPrecioCompra().equals(precioCompra))
						.toList();
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getPrecioCompra().equals(precioCompra))
						.toList();
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@GetMapping("/cantidadDeseada/{cantidadDeseada}")
	public ResponseEntity<List<ProductoInventario>> findByCantidadDeseada(@PathVariable("cantidadDeseada") Integer cantidadDeseada) {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getProductoInventarioByCantidadDeseada(cantidadDeseada);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getCantidadDeseada().equals(cantidadDeseada))
						.toList();
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getCantidadDeseada().equals(cantidadDeseada))
						.toList();
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}

		if (productoInventario.isEmpty())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@GetMapping("/cantidadAviso/{cantidadAviso}")
	public ResponseEntity<List<ProductoInventario>> findByCantidadAviso(@PathVariable("cantidadAviso") Integer cantidadAviso) {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getProductoInventarioByCantidadAviso(cantidadAviso);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getCantidadAviso().equals(cantidadAviso))
						.toList();
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getCantidadAviso().equals(cantidadAviso))
						.toList();
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@GetMapping("/proveedor/{proveedorId}")
	public ResponseEntity<List<ProductoInventario>> findByProveedorId(@PathVariable("proveedorId") Integer proveedorId) {
		User user = userService.findCurrentUser();
		List<ProductoInventario> productoInventario;
		switch (user.getAuthority().getAuthority()){
			case admin -> productoInventario = productoInventarioService.getProductoInventarioByProveedorId(proveedorId);
			case empleado -> {
				Empleado currEmpleado = empleadoService.getEmpleadoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByNegocioId(currEmpleado.getNegocio().getId()).stream()
						.filter(p -> p.getProveedor().getId().equals(proveedorId))
						.toList();
			}
			case dueno -> {
				Dueno currDueno = duenoService.getDuenoByUser(user.getId());
				productoInventario = productoInventarioService.getProductoInventarioByDuenoId(currDueno.getId()).stream()
						.filter(p -> p.getProveedor().getId().equals(proveedorId))
						.toList();
			}
			default -> {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productoInventario, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProductoInventario> createProductoInventario(@RequestBody @Valid ProductoInventarioDTO newProductoInventario) {
		User user = userService.findCurrentUser();
		if (newProductoInventario==null)
			throw new IllegalArgumentException("ProductoInventario cannot be null");
		Negocio negocio = categoriaService.getById(newProductoInventario.getCategoriaId()).getNegocio();

		if(! ((user.getAuthority().getAuthority().equals(admin)) ||
				(user.getAuthority().getAuthority().equals(dueno)) && negocio.getDueno().getUser().getId().equals(user.getId()) ||
				(user.getAuthority().getAuthority().equals(empleado) && empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId())))) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		ProductoInventario productoInventario = productoInventarioService.convertirDTOProductoInventario(newProductoInventario);
		return new ResponseEntity<>(productoInventarioService.save(productoInventario), HttpStatus.CREATED);
	}

    @PutMapping("/{id}")
	public ResponseEntity<ProductoInventario> update(@RequestBody @Valid ProductoInventarioDTO newProductoInventario, @PathVariable("id") Integer id) {
		User user = userService.findCurrentUser();
		Dueno currDueno = duenoService.getDuenoByUser(user.getId());
		ProductoInventario productoInventario = productoInventarioService.getById(id);
		if (newProductoInventario == null)
			throw new IllegalArgumentException("ProductoInventario cannot be null");
		if (productoInventario == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		Negocio negocio = categoriaService.getById(newProductoInventario.getCategoriaId()).getNegocio();
		Negocio negocioToUpdate = productoInventario.getCategoria().getNegocio();

		//        Si no es admin
		if(! ((user.getAuthority().getAuthority().equals(admin)) ||
//                Ni dueno del negocio al que pertence la categoria del producto ni cambia a una categoria de otro negocio que no le pertenece
				(user.getAuthority().getAuthority().equals(dueno)  &&
						currDueno.getId().equals(negocioToUpdate.getDueno().getId()) &&
						negocioService.getByDueno(currDueno.getId()).contains(negocio)) ||
//                Ni empleado del negocio al que pertence la categoria del producto ni cambia a una categoria de otro negocio
				(user.getAuthority().getAuthority().equals(empleado) &&
						empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocioToUpdate.getId()) &&
						negocio.equals(negocioToUpdate)))) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		ProductoInventario productoInventarioToUpdate = productoInventarioService.convertirDTOProductoInventario(newProductoInventario);
		productoInventarioToUpdate.setId(id);
		return new ResponseEntity<>(productoInventarioService.save(productoInventarioToUpdate), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		try {
			User user = userService.findCurrentUser();
			Integer idNum = Integer.parseInt(id);
			ProductoInventario producto = productoInventarioService.getById(idNum);
			if (producto == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Negocio negocio = producto.getCategoria().getNegocio();

			if(!((user.getAuthority().getAuthority().equals(admin)) ||
					(user.getAuthority().getAuthority().equals(dueno) &&
							user.getId().equals(negocio.getDueno().getUser().getId())) ||
					(user.getAuthority().getAuthority().equals(empleado) &&
							empleadoService.getEmpleadoByUser(user.getId()).getNegocio().getId().equals(negocio.getId()))))  {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}

			productoInventarioService.delete(idNum);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (NumberFormatException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
