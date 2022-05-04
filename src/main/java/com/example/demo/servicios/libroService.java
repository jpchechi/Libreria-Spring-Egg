package com.example.demo.servicios;

import com.example.demo.ExcepcionesLibreria.ErroresServicio;
import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.repositorios.AutorRepositorio;
import com.example.demo.repositorios.EditorialRepositorio;
import com.example.demo.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class libroService {

    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private LibroRepositorio libroRepositorio;

    @Transactional(propagation = Propagation.NESTED) //esta anotacion dice que el metodo hace una modificacion a la bs, si se detecta un error no deja que nada se modifique
    public Libro registrarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Autor autor, Editorial editorial) throws ErroresServicio {
        validarLibro(isbn, titulo, anio, ejemplares);

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setAlta(true);
        libro.setEjemplares(ejemplares);

        Optional<Autor> respuesta1 = autorRepositorio.findById(autor.getId());
        if (respuesta1.isPresent()) {
            libro.setAutor(autor);
        } else {
            throw new ErroresServicio("No existe un autor con el id ingresado");
        }
        Optional<Editorial> respuesta2 = editorialRepositorio.findById(editorial.getId());
        if (respuesta2.isPresent()) {
            libro.setEditorial(editorial);
        } else {
            throw new ErroresServicio("No existe una editorial con el id ingresado");
        }

        return libroRepositorio.save(libro);

    }

    @Transactional(propagation = Propagation.NESTED)
    public Libro ficarLibro(String id, String titulo, Integer anio) throws ErroresServicio {
        if (titulo == null || titulo.isEmpty()) {
            throw new ErroresServicio("Debe ingresar un nombre valido");
        }
        if (anio == null) {
            throw new ErroresServicio("Debe ingresar un año valido");
        }

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            return libroRepositorio.save(libro);

        } else {
            throw new ErroresServicio("No se encontro un libro con el id ingresado");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public Libro deshabilitarLibro(String id) throws ErroresServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(false);
            return libroRepositorio.save(libro);

        } else {
            throw new ErroresServicio("No se encontro un libro con el id ingresado");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public Libro habilitarLibro(String id) throws ErroresServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(true);
            return libroRepositorio.save(libro);

        } else {
            throw new ErroresServicio("No se encontro un libro con el id ingresado");
        }
    }

    @Transactional(readOnly = true)//esto es porque solo va y busca datos en la bd
    public Libro consultarLibro(String id) throws ErroresServicio {

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            return libro;

        } else {
            throw new ErroresServicio("No se encontro un libro con el id ingresado");
        }
    }

    @Transactional(readOnly = true)//esto es porque solo va y busca datos en la bd
    public List<Libro> mostrarLibros() throws ErroresServicio {
        if (libroRepositorio.findAll().isEmpty()) {

            throw new ErroresServicio("No hay libros que mostrar");
        } else {
            return libroRepositorio.findAll();
        }

    }

    @Transactional(propagation = Propagation.NESTED)
    public Libro modificarLibro(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Autor autor, Editorial editorial) throws ErroresServicio {
        validarLibro(isbn, titulo, anio, ejemplares);
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            
            Optional<Autor> respuesta1 = autorRepositorio.findById(autor.getId());
            if (respuesta1.isPresent()) {
                libro.setAutor(autor);
            } else {
                throw new ErroresServicio("No existe un autor con el id ingresado");
            }
            Optional<Editorial> respuesta2 = editorialRepositorio.findById(editorial.getId());
            if (respuesta2.isPresent()) {
                libro.setEditorial(editorial);
            } else {
                throw new ErroresServicio("No existe una editorial con el id ingresado");
            }
            return libroRepositorio.save(libro);
        }
        else{
            throw new ErroresServicio("No se encontro el libro");
        }

    }


    public void validarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares) throws ErroresServicio {

        if (titulo == null || titulo.trim().isEmpty()) {

            throw new ErroresServicio("Debe ingresar un titulo valido");

        }
        if (isbn == null || isbn == 0) {
            System.out.println("error");
            throw new ErroresServicio("Debe ingresar un numero ISBN valido");
        }

        if (anio == null || anio == 1) {
            throw new ErroresServicio("Debe ingresar un año valido");
        }
        if (ejemplares == null || ejemplares == 0) {
            throw new ErroresServicio("Debe ingresar una cantidad de ejemplares validos");
        }

    }

}
