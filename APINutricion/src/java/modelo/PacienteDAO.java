/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.HashMap;
import java.util.List;
import modelo.pojo.Mensaje;
import modelo.pojo.Paciente;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author andre
 */
public class PacienteDAO {

    public List<Paciente> obtenerPacientePorIdMedico(Integer idMedico) {
        List<Paciente> paciente = null;
        SqlSession conexionDB = MyBatisUtil.getSesion();

        if (conexionDB != null) {
            try {
                paciente = conexionDB.selectList("paciente.obtenerPorIdMedico", idMedico);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionDB.close();
            }
        }
        return paciente;
    }

    public Mensaje registrarPaciente(Paciente paciente){
         
        Mensaje msj = new Mensaje();
        SqlSession conexionDB = MyBatisUtil.getSesion();
        
        if (conexionDB != null) {
            try {
                int numeroFilasAfectadas = conexionDB.insert("paciente.registrarPaciente", paciente);
                conexionDB.commit();
                if (numeroFilasAfectadas > 0) {
                    msj.setError(false);
                    msj.setMensaje("OK, " +numeroFilasAfectadas + ","+paciente.getNombre());
                } else {
                    msj.setError(true);
                    msj.setMensaje("Lo sentimos, no se pudo registrar la información del Paciente.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            } finally {
                conexionDB.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexión con la base de datos.");
        }

        return msj;
    }
    
    private HashMap<String, Object> toparam(Paciente paciente) {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idPaciente", paciente.getIdPaciente());
        parametros.put("nombre", paciente.getNombre());
        parametros.put("apellidoPaterno", paciente.getApellidoPaterno());
        parametros.put("apellidoMaterno", paciente.getApellidoMaterno());
        parametros.put("fechaNacimiento", paciente.getFechaNacimiento());
        parametros.put("sexo", paciente.getSexo());
        parametros.put("peso", paciente.getPeso());
        parametros.put("estatura", paciente.getEstatura());
        parametros.put("tallaInicial", paciente.getTallaInicial());
        parametros.put("email", paciente.getEmail());
        parametros.put("telefono", paciente.getTelefono());
        parametros.put("password", paciente.getPassword());
        parametros.put("fotografia", paciente.getFotografia());
        parametros.put("idDomicilio", paciente.getIdDomicilio());
        parametros.put("idMedico", paciente.getIdMedico());

        return parametros;
    }

    public Mensaje editarPaciente(Paciente paciente) {

        Mensaje response = new Mensaje();
        HashMap<String, Object> parametros = toparam(paciente);
        SqlSession conn = MyBatisUtil.getSesion();
        response.setMensaje("OK");

        if (conn != null) {
            try {
                if (paciente.getIdPaciente() == 0) {
                    response.setMensaje("ID necesario para actualizar");
                }
                Paciente found = conn.selectOne("paciente.obtenerPacientePorId", paciente.getIdPaciente());
                if (found != null) {
                    int count = conn.update("paciente.editarPaciente", parametros);
                    conn.commit();
                    if (count > 0) {
                        response.setMensaje("Paciente actualizado con éxito.");
                    } else {
                        response.setMensaje("Lo sentimos, no se pudo actualizar la información del Paciente.");
                    }
                }
            } catch (Exception e) {
                response.setError(true);
                response.setMensaje("Error: " + e.getMessage());
            } finally {
                conn.close();
            }
        } else {
            response.setMensaje("Por el momento no hay conexión con la base de datos.");
        }

        return response;
    }

    public Mensaje eliminarPaciente(Integer idPaciente) {

        Mensaje msj = new Mensaje();
        SqlSession conexionDB = MyBatisUtil.getSesion();

        if (conexionDB != null) {
            try {
                int numeroFilasAfectadas = conexionDB.delete("paciente.eliminarPaciente", (idPaciente));
                conexionDB.commit();
                if (numeroFilasAfectadas > 0) {
                    msj.setError(false);
                    msj.setMensaje("Información del Paciente eliminada con éxito");
                } else {
                    msj.setError(true);
                    msj.setMensaje("Lo sentimos, no se pudo eliminar la información del Paciente.");
                }
            } catch (Exception e) {
                msj.setError(true);
                msj.setMensaje("Error: " + e.getMessage());
            } finally {
                conexionDB.close();
            }
        } else {
            msj.setError(true);
            msj.setMensaje("Por el momento no hay conexión con la base de datos.");
        }

        return msj;
    }
    
    public static Mensaje subirFotografiaPaciente(int idPaciente, byte[] foto) {
        Mensaje msj = new Mensaje();
        msj.setError(true);
        SqlSession conexionDB = MyBatisUtil.getSesion();

        if (conexionDB != null) {
            try {
                Paciente pacienteFoto = new Paciente();
                pacienteFoto.setIdPaciente(idPaciente);
                pacienteFoto.setFotografia(foto);
                int numeroFilasAfectadas = conexionDB.update("paciente.subirFoto", pacienteFoto);
                conexionDB.commit();

                if (numeroFilasAfectadas > 0) {
                    msj.setError(false);
                    msj.setMensaje("Fotografía del Paciente guardada con éxito");
                } else {
                    msj.setError(true);
                    msj.setMensaje("Lo sentimos, no se pudo guardar la fotografía del Paciente, revisa la imagen.");
                }

            } catch (Exception e) {
                msj.setMensaje("Error: " + e.getMessage());
            } finally {
                conexionDB.close();
            }
        } else {
            msj.setMensaje("Lo sentimos no hay conexion para guardar la fotografia del paciente.");
        }
        return msj;
    }
    
    public static Paciente obtenerFotografia(int idPaciente){
        Paciente paciente = null;
        SqlSession conexionDB = MyBatisUtil.getSesion();
        
        if (conexionDB != null){
            try {
                paciente = conexionDB.selectOne("paciente.obtenerFoto", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                conexionDB.close();
            }
        }
        return paciente;
    }
}
