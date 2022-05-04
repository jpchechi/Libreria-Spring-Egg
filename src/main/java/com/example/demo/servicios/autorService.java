package com.example.demo.servicios;

import com.example.demo.ExcepcionesLibreria.ErroresServicio;
import com.example.demo.entidades.Autor;
import com.example.demo.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class autorService {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional(propagation = Propagation.NESTED) //esta anotacion dice que el metodo hace una modificacion a la bs, si se detecta un error no deja que nada se modifique
    public Autor registrarAutor(String nombre) throws ErroresServicio {

        validacionAutor(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);
        return autorRepositorio.save(autor);

    }

    @Transactional(propagation = Propagation.NESTED)
    public Autor modificarAutor(String id, String nombre) throws ErroresServicio {
        validacionAutor(nombre);
        Optional<Autor> respuesta = autorRepositorio.findById(id); //busca al autor y verifica su id
        if (respuesta.isPresent()) { //si existe un autor con ese id entra al if
            Autor autor = respuesta.get(); //trae o llama al autor que busco mas arriba por el Id

            autor.setNombre(nombre);
            return autorRepositorio.save(autor);

        } else {
            throw new ErroresServicio("No se encontro el autor");

        }

    }

    @Transactional(propagation = Propagation.NESTED)
    public Autor deshabilitarAutor(String id) throws ErroresServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id); //busca al autor y verifica su id
        if (respuesta.isPresent()) { //si existe un autor con ese id entra al if
            Autor autor = respuesta.get(); //trae o llama al autor que busco mas arriba por el Id
            autor.setAlta(false);
            return autorRepositorio.save(autor);

        } else {
            throw new ErroresServicio("No se encontro el autor");

        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public Autor habilitarAutor(String id) throws ErroresServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id); //busca al autor y verifica su id
        if (respuesta.isPresent()) { //si existe un autor con ese id entra al if
            Autor autor = respuesta.get(); //trae o llama al autor que busco mas arriba por el Id
            autor.setAlta(true);
            return autorRepositorio.save(autor);
        } else {
            throw new ErroresServicio("No se encontro el autor");

        }
    }

    @Transactional(readOnly = true)
    public Autor consultarAutor(String id) throws ErroresServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id); //busca al autor y verifica su id
        if (respuesta.isPresent()) { //si existe un autor con ese id entra al if
            Autor autor = respuesta.get(); //trae o llama al autor que busco mas arriba por el Id
            return autor;
        } else {
            throw new ErroresServicio("No se encontro el autor");

        }
    }

    @Transactional(readOnly = true)//esto es porque solo va y busca datos en la bd
    public List<Autor> mostrarAutores() throws ErroresServicio {
        
        if(autorRepositorio.findAll().isEmpty()){
            throw new ErroresServicio("No hay autores que mostrar");
        }else{
          return autorRepositorio.findAll();  
        }
      
    }

    public void validacionAutor(String nombre) throws ErroresServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErroresServicio("Debe ingresar un nombre valido");
        }
    }
}
