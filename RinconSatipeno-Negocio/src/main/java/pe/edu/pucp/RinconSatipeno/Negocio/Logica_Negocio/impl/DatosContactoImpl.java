package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.RinconSatipeno.Modelo.reservas.DatosContacto;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.DatosContactoLN;

import pe.edu.pucp.rinconSatipeno.dao.DatosContactoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.DatosContactoDaoImplement;

public class DatosContactoImpl
        implements DatosContactoLN {

    private final DatosContactoDao datosContactoDao =
            new DatosContactoDaoImplement();

    @Override
    public List<DatosContacto> findAll()
            throws BLException {

        try {
            return datosContactoDao.findAll();

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudieron listar los datos de contacto",
                    ex
            );
        }
    }

    @Override
    public DatosContacto findBy(Integer id)
            throws BLException {

        validarId(id);

        try {
            DatosContacto contacto =
                    datosContactoDao.findBy(id);

            if (contacto == null) {
                throw new BLException(
                        "No se encontraron los datos de contacto"
                );
            }

            return contacto;

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudieron buscar los datos de contacto",
                    ex
            );
        }
    }

    @Override
    public void insert(DatosContacto contacto)
            throws BLException {

        validarDatosContacto(contacto);

        try {
            datosContactoDao.insert(contacto);

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudieron registrar los datos de contacto",
                    ex
            );
        }
    }

    @Override
    public void update(DatosContacto contacto)
            throws BLException {

        validarDatosContacto(contacto);

        if (contacto.getIdContacto() <= 0) {
            throw new BLException(
                    "Los datos de contacto deben tener un ID válido"
            );
        }

        validarExiste(contacto.getIdContacto());

        try {
            datosContactoDao.update(contacto);

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudieron actualizar los datos de contacto",
                    ex
            );
        }
    }

    @Override
    public void delete(Integer id)
            throws BLException {

        validarId(id);
        validarExiste(id);

        try {
            datosContactoDao.delete(id);

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudieron eliminar los datos de contacto",
                    ex
            );
        }
    }

    private void validarExiste(Integer id)
            throws BLException {

        try {
            DatosContacto contacto =
                    datosContactoDao.findBy(id);

            if (contacto == null) {
                throw new BLException(
                        "Los datos de contacto no existen"
                );
            }

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo verificar la existencia de los datos de contacto",
                    ex
            );
        }
    }

    private void validarId(Integer id)
            throws BLException {

        if (id == null || id <= 0) {
            throw new BLException(
                    "El ID de contacto no es válido"
            );
        }
    }

    private void validarDatosContacto(
            DatosContacto contacto)
            throws BLException {

        if (contacto == null) {
            throw new BLException(
                    "Los datos de contacto no pueden ser nulos"
            );
        }

        if (contacto.getNombre() == null ||
                contacto.getNombre().trim().isEmpty()) {

            throw new BLException(
                    "El nombre del contacto es obligatorio"
            );
        }

        if (contacto.getTelefono() == null ||
                contacto.getTelefono().trim().isEmpty()) {

            throw new BLException(
                    "El teléfono del contacto es obligatorio"
            );
        }

        if (contacto.getCorreo() == null ||
                contacto.getCorreo().trim().isEmpty()) {

            throw new BLException(
                    "El correo del contacto es obligatorio"
            );
        }

        if (!contacto.getCorreo().contains("@")) {
            throw new BLException(
                    "El correo del contacto no es válido"
            );
        }
    }
}