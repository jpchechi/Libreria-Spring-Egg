package com.example.demo.Controladores;
import com.example.demo.ExcepcionesLibreria.ErroresServicio;
import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.repositorios.AutorRepositorio;
import com.example.demo.repositorios.EditorialRepositorio;
import com.example.demo.repositorios.LibroRepositorio;
import com.example.demo.servicios.autorService;
import com.example.demo.servicios.editorialService;
import com.example.demo.servicios.libroService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private autorService AutorServicio;
    @Autowired
    private editorialService EditorialServicio;
    @Autowired
    private libroService LibroServicio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
//----------------------------------AUTOR---------------------------------------

    @GetMapping("/loginAutor")
    public String registroAutor() {
        return "registroAutor.html";
    }

    @PostMapping("/loginAutor")
    public String loginAutor(ModelMap modelo, @RequestParam String nombre) {
        try {
            AutorServicio.registrarAutor(nombre);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "registroAutor.html";
        }

        return "exito.html";
    }

    @GetMapping("/Autores")
    public String autores(ModelMap modelo) throws ErroresServicio {
        List<Autor> autores = AutorServicio.mostrarAutores();
        modelo.put("autores", autores);

        return "vistaAutor.html";
    }

    @GetMapping("autor_baja")
    public String bajaAutor(String id, ModelMap modelo) throws ErroresServicio {

        try {
            AutorServicio.deshabilitarAutor(id);
        } catch (ErroresServicio ex) {
            modelo.put("error", ex.getMessage());
            return "vistaAutor.html";
        }
        return "modificacionExitosa.html";
    }

    @GetMapping("autor_alta")
    public String altaAutor(String id, ModelMap modelo) throws ErroresServicio {
        System.out.println(id);
        try {
            AutorServicio.habilitarAutor(id);
        } catch (ErroresServicio ex) {
            modelo.put("error", ex.getMessage());
            return "vistaAutor.html";
        }
        return "modificacionExitosa.html";
    }

    @GetMapping("/modificar_autor")
    public String modificarAutor(String id, ModelMap modelo) throws ErroresServicio {
        Autor autor = AutorServicio.consultarAutor(id);
        modelo.put("nombre", autor.getNombre());
        modelo.put("id_autor", autor.getId());
        return "modAutor.html";
    }

    @PostMapping("/modificar_autor")
    public String modificacionAutor(ModelMap modelo, @RequestParam String id_autor, @RequestParam String nombre2) throws ErroresServicio {
        try {
            System.out.println(id_autor);
            System.out.println(nombre2);

            AutorServicio.modificarAutor(id_autor, nombre2);
        } catch (Exception e) {
            Autor autor = AutorServicio.consultarAutor(id_autor);
            modelo.put("nombre", autor.getNombre());
            modelo.put("id_autor", autor.getId());
            modelo.put("error", e.getMessage());

            System.out.println(e.getMessage());
            return "modAutor.html";
        }

        return "modificacionExitosa.html";
    }

//----------------------------------EDITORIAL---------------------------------------
    @GetMapping("/loginEditorial")
    public String registroEditorial() {
        return "registroEditorial.html";
    }

    @PostMapping("/loginEditorial")
    public String loginEditorial(ModelMap modelo, @RequestParam String nombre) {
        System.out.println("nombreeeeee: " + nombre);
        try {
            EditorialServicio.registrarEditorial(nombre);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());

            return "registroEditorial.html";
        }
        return "exito.html";
    }

    @GetMapping("/Editoriales")
    public String editoriales(ModelMap modelo) throws ErroresServicio {
        List<Editorial> editoriales = EditorialServicio.mostrarEditoriales();
        modelo.put("editoriales", editoriales);

        return "vistaEditorial.html";
    }

    @GetMapping("editorial/baja/{id}")
    public String bajaEditorial(@PathVariable("id") String id, ModelMap modelo) throws ErroresServicio {
        try {
            EditorialServicio.deshabilitarEditorial(id);
        } catch (ErroresServicio ex) {
            modelo.put("error", ex.getMessage());
            return "vistaEditorial.html";
        }
        return "modificacionExitosa.html";
    }

    @GetMapping("editorial/alta/{id}")
    public String altaEditorial(@PathVariable("id") String id, ModelMap modelo) throws ErroresServicio {
        try {
            EditorialServicio.habilitarEditorial(id);
        } catch (ErroresServicio ex) {
            modelo.put("error", ex.getMessage());
            return "vistaEditorial.html";
        }
        return "modificacionExitosa.html";
    }

    @GetMapping("/modificar_editorial")
    public String modificarEditorial(String id, ModelMap modelo) throws ErroresServicio {

        Editorial editorial = EditorialServicio.consultarEditorial(id);
        modelo.put("nombre", editorial.getNombre());
        modelo.put("id_editorial", editorial.getId());
        return "modEditorial.html";
    }

    @PostMapping("/modificar_editorial")
    public String modificacionEditorial(ModelMap modelo, @RequestParam String id_editorial, @RequestParam String nombre2) throws ErroresServicio {
        try {
            System.out.println(id_editorial);
            System.out.println(nombre2);
            EditorialServicio.modificarEditorial(id_editorial, nombre2);
            Editorial edit = EditorialServicio.consultarEditorial(id_editorial);

        } catch (Exception e) {
            Editorial edit = EditorialServicio.consultarEditorial(id_editorial);
            modelo.put("nombre", edit.getNombre());
            modelo.put("id_editorial", edit.getId());
            modelo.put("error", e.getMessage());
            return "modEditorial.html";
        }

        return "modificacionExitosa.html";
    }

//----------------------------------LIBRO---------------------------------------
    @GetMapping("/loginLibro")
    public String registroLibro(ModelMap modelo, ModelMap modelo2) throws ErroresServicio {
        List<Autor> autores = autorRepositorio.findAll();
        modelo.put("autores", autores);
        List<Editorial> editoriales = editorialRepositorio.findAll();
        modelo2.put("editoriales", editoriales);
        return "registroLibro.html";
    }

    @PostMapping("/loginLibro")
    public String loginLibro(ModelMap modelo, ModelMap modelo2, ModelMap modelo3, @RequestParam(required=false) Long isbn, @RequestParam(required=false) String titulo, @RequestParam(required=false) Integer anio, @RequestParam(required=false) Integer ejemplares, @RequestParam(required=false) Autor autor, @RequestParam(required=false) Editorial editorial) throws ErroresServicio {
//@RequestParam(required = false) String titulo

        try {
            LibroServicio.registrarLibro(isbn, titulo, anio, ejemplares, autor, editorial);
        } catch (Exception e) {

            modelo3.put("error", e.getMessage());
            modelo3.put("isbn", isbn);
            modelo3.put("titulo", titulo);
            modelo3.put("anio", anio);
            modelo3.put("ejemplares", ejemplares);
            List<Autor> autores = autorRepositorio.findAll();
            modelo.put("autores", autores);
            List<Editorial> editoriales = editorialRepositorio.findAll();
            modelo2.put("editoriales", editoriales);

            return "registroLibro.html";
        }
        return "exito.html";
    }

    @GetMapping("/Libros")
    public String libros(ModelMap modelo) throws ErroresServicio {

        List<Libro> libros = LibroServicio.mostrarLibros();
        modelo.put("libros", libros);

        return "vistaLibro.html";
    }

    @GetMapping("libro/baja/{id}")
    public String bajaLibro(@PathVariable("id") String id, ModelMap modelo) throws ErroresServicio {
        try {
            LibroServicio.deshabilitarLibro(id);
        } catch (ErroresServicio ex) {
            modelo.put("error", ex.getMessage());
            return "vistaLibro.html";
        }
        return "modificacionExitosa.html";
    }

    @GetMapping("libro/alta/{id}")
    public String altaLibro(@PathVariable("id") String id, ModelMap modelo) throws ErroresServicio {
        try {
            LibroServicio.habilitarLibro(id);
        } catch (ErroresServicio ex) {
            modelo.put("error", ex.getMessage());
            return "vistaLibro.html";
        }
        return "modificacionExitosa.html";
    }

    //---------------------------------------------------
    @GetMapping("/modificar_libro")
    public String modificarLibro(String id, ModelMap modelo) throws ErroresServicio {

        Libro libro = LibroServicio.consultarLibro(id);
        modelo.put("nombre", libro.getTitulo());
        modelo.put("id_libro", libro.getId());
        List<Autor> autores = autorRepositorio.findAll();
        modelo.put("autores", autores);
        List<Editorial> editoriales = editorialRepositorio.findAll();
        modelo.put("editoriales", editoriales);
        return "modLibro.html";
    }
//@RequestParam(required=false)
    @PostMapping("/modificar_libro")
    public String modificacionLibro(ModelMap modelo, @RequestParam(required=false) String id_libro, @RequestParam(required=false) String nombre2,
            @RequestParam(required=false) Long isbn,
            @RequestParam(required=false) Integer anio, @RequestParam(required=false) Integer ejemplares,
            @RequestParam(required=false) Autor autor, @RequestParam(required=false) Editorial editorial) throws ErroresServicio {

        try {
            System.out.println("ID:   " + id_libro);
            System.out.println("Nuevo titulo " + nombre2);
            System.out.println("ISBN " + isbn);
            System.out.println("Año " + anio);
            System.out.println("Ejemplares " + ejemplares);
            System.out.println("Autor" + autor);
            System.out.println("Editorial" + editorial);

            LibroServicio.modificarLibro(id_libro, isbn, nombre2, anio, ejemplares, autor, editorial);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("isbn", isbn);
            modelo.put("titulo", nombre2);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            List<Autor> autores = autorRepositorio.findAll();
            modelo.put("autores", autores);
            List<Editorial> editoriales = editorialRepositorio.findAll();
            modelo.put("editoriales", editoriales);
            return "registroLibro.html";
        }
        return "modificacionExitosa.html";
    }
}
