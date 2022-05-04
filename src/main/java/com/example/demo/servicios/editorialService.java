package com.example.demo.servicios;

import com.example.demo.ExcepcionesLibreria.ErroresServicio;
import com.example.demo.entidades.Editorial;
import com.example.demo.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class editorialService {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional(propagation = Propagation.NESTED) //esta anotacion dice que el metodo hace una modificacion a la bs, si se detecta un error no deja que nada se modifique
    public Editorial registrarEditorial(String nombre) throws ErroresServicio {
        
        validacionEditorial(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        return editorialRepositorio.save(editorial);
    }

    @Transactional(propagation = Propagation.NESTED)
    public Editorial modificarEditorial(String id, String nombre) throws ErroresServicio {
        validacionEditorial(nombre);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            return editorialRepositorio.save(editorial);

        } else {
            throw new ErroresServicio("No se encontro la editorial");
        }

    }

    @Transactional(propagation = Propagation.NESTED)
    public Editorial deshabilitarEditorial(String id) throws ErroresServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(false);
            return editorialRepositorio.save(editorial);
        } else {
            throw new ErroresServicio("No se encontro la editorial");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public Editorial habilitarEditorial(String id) throws ErroresServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(true);
            return editorialRepositorio.save(editorial);
        } else {
            throw new ErroresServicio("No se encontro la editorial");
        }
    }

    @Transactional(readOnly = true)//esto es porque solo va y busca datos en la bd
    public Editorial consultarEditorial(String id) throws ErroresServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            return editorial;
        } else {
            throw new ErroresServicio("No se encontro la editorial");
        }
    }

    @Transactional(readOnly = true)//esto es porque solo va y busca datos en la bd
    public List<Editorial> mostrarEditoriales() throws ErroresServicio {
        if (editorialRepositorio.findAll().isEmpty()) {
            throw new ErroresServicio("No hay editoriales que mostrar");
        } else {
            return editorialRepositorio.findAll();
        }

    }

    public void validacionEditorial(String nombre) throws ErroresServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErroresServicio("Debe ingresar un nombre valido");
        }

    }
}
