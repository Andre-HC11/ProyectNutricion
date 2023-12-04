/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import modelo.AlimentoDAO;
import modelo.pojo.Alimento;

/**
 *
 * @author andre
 */
@Path("dieta")
public class AlimentoWS {
    @Path("paciente/{idPaciente}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Alimento> obtenerPacientePorIdMedico(@PathParam("idPaciente") Integer idPaciente){
      AlimentoDAO dao = new AlimentoDAO();
      return dao.obtenerDietaPaciente(idPaciente);
    }
}