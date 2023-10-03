/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import modelo.pojo.Mensaje;
import modelo.pojo.Paciente;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author andre
 */
@Path("pacientes")
public class PacientesWS {
    
    @Context
    private UriInfo context;
    
    @GET
    @Path("prueba")
    @Produces(MediaType.APPLICATION_JSON)
    public String prueba(){
        return "Hola desde pacientes...";
    }
    
    @GET
    @Path("obtenerPacientePorIdMedico/{idMedico}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paciente> obtenerPacientePorIdMedico(@PathParam("idMedico") Integer idMedico){
        List<Paciente> paciente = null;
        SqlSession conexionDB = MyBatisUtil.getSesion();
        
        if(conexionDB != null){
            try{
                paciente = conexionDB.selectList("paciente.obtenerPorIdMedico", idMedico);
            }catch (Exception e){
               e.printStackTrace();
            } finally{
                conexionDB.close();
            }
        }
        return paciente;
    }
    
    @POST
    @Path("registrarPaciente")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje registrarPaciente(@FormParam("nombre") String nombre,
                                     @FormParam("apellidoPaterno") String apellidoPaterno,
                                     @FormParam("apellidoMaterno") String apellidoMaterno,
                                     @FormParam("fechaNacimiento") String fechaNacimiento,
                                     @FormParam("sexo") String sexo,
                                     @FormParam("peso") float peso,
                                     @FormParam("estatura") float estatura,
                                     @FormParam("tallaInicial") int tallaInicial,
                                     @FormParam("email") String email,
                                     @FormParam("telefono") String telefono,
                                     @FormParam("password") String password,
                                     @FormParam("fotografia") byte[] fotografia,
                                     @FormParam("idDomicilio") Integer idDomicilio,
                                     @FormParam("idMedico") Integer idMedico){
                                    
        
    Paciente paciente = new Paciente();
    paciente.setNombre(nombre);
    paciente.setApellidoPaterno(apellidoPaterno);
    paciente.setApellidoMaterno(apellidoMaterno);
    paciente.setFechaNacimiento(fechaNacimiento);
    paciente.setSexo(sexo);
    paciente.setPeso(peso);
    paciente.setEstatura(estatura);
    paciente.setTallaInicial(tallaInicial);
    paciente.setEmail(email);
    paciente.setTelefono(telefono);
    paciente.setPassword(password);
    paciente.setFotografia(fotografia);
    paciente.setIdDomicilio(idDomicilio);
    paciente.setIdMedico(idMedico);
    
    Mensaje msj = new Mensaje();
    SqlSession conexionDB = MyBatisUtil.getSesion();
    
    if(conexionDB != null){
        try{
            int numeroFilasAfectadas = conexionDB.insert("paciente.registrarPaciente", paciente);
            conexionDB.commit();
            if(numeroFilasAfectadas > 0){
                msj.setError(false);
                msj.setMensaje("Información del Paciente registrada con éxito");
            }else{
                msj.setError(true);
                msj.setMensaje("Lo sentimos, no se pudo registrar la información del Paciente.");
            }
        }catch(Exception e){
            msj.setError(true);
            msj.setMensaje("Error: " + e.getMessage());
        } finally{
            conexionDB.close();
        }
    } else{
        msj.setError(true);
        msj.setMensaje("Por el momento no hay conexión con la base de datos.");
    }
    
    return msj;
    }
    
    @PUT
    @Path("editar")
    @Produces(MediaType.APPLICATION_JSON)
    public Mensaje editarPaciente(@FormParam("idPaciente") Integer idPaciente,
                                  @FormParam("nombre") String nombre,
                                  @FormParam("apellidoPaterno") String apellidoPaterno,
                                  @FormParam("apellidoMaterno") String apellidoMaterno,
                                  @FormParam("fechaNacimiento") String fechaNacimiento,
                                  @FormParam("sexo") String sexo,
                                  @FormParam("peso") float peso,
                                  @FormParam("estatura") float estatura,
                                  @FormParam("tallaInicial") int tallaInicial,
                                  @FormParam("telefono") String telefono,
                                  @FormParam("password") String password,
                                  @FormParam("fotografia") byte[] fotografia,
                                  @FormParam("idDomicilio") Integer idDomicilio,
                                  @FormParam("idMedico") Integer idMedico){
        
        Mensaje msj = new Mensaje();
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("idPaciente", idPaciente);
        parametros.put("nombre", nombre);
        parametros.put("apellidoPaterno", apellidoPaterno);
        parametros.put("apellidoMaterno", apellidoMaterno);
        parametros.put("fechaNacimiento", fechaNacimiento);
        parametros.put("sexo", sexo);
        parametros.put("peso", peso);
        parametros.put("estatura", estatura);
        parametros.put("tallaInicial", tallaInicial);
        parametros.put("telefono", telefono);
        parametros.put("password", password);
        parametros.put("fotografia", fotografia);
        parametros.put("idDomicilio", idDomicilio);
        parametros.put("idMedico", idMedico);
        
        SqlSession conexionDB = MyBatisUtil.getSesion();
    
        if (conexionDB != null) {
            try {
                Paciente usuaerioExistente = conexionDB.selectOne("paciente.obtenerPacientePorId", idPaciente);
                if (usuaerioExistente != null) {
                    int numeroFilasAfectadas = conexionDB.update("paciente.editarPaciente", parametros);
                    conexionDB.commit();
                    if (numeroFilasAfectadas > 0) {
                        msj.setError(false);
                        msj.setMensaje("Paciente actualizado con éxito.");
                    } else {
                        msj.setError(true);
                        msj.setMensaje("Lo sentimos, no se pudo actualizar la información del Paciente.");
                    }
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

    @DELETE
    @Path("eliminar/{idPaciente}")
    @Produces(MediaType.TEXT_PLAIN)
    public Mensaje eliminarUsuario(@FormParam("idPaciente") Integer idPaciente){
    
    Mensaje msj = new Mensaje();
    SqlSession conexionDB = MyBatisUtil.getSesion();
    
    if(conexionDB != null){
        try{
            int numeroFilasAfectadas = conexionDB.delete("paciente.eliminarPaciente", (idPaciente));
            conexionDB.commit();
            if(numeroFilasAfectadas > 0){
                msj.setError(false);
                msj.setMensaje("Información del Paciente eliminada con éxito");
            }else{
                msj.setError(true);
                msj.setMensaje("Lo sentimos, no se pudo eliminar la información del Paciente.");
            }
        }catch(Exception e){
            msj.setError(true);
            msj.setMensaje("Error: " + e.getMessage());
        } finally{
            conexionDB.close();
        }
    } else{
        msj.setError(true);
        msj.setMensaje("Por el momento no hay conexión con la base de datos.");
    }
    
    return msj;
    }
}