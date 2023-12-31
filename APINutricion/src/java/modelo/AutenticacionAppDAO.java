/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.HashMap;
import modelo.pojo.Paciente;
import modelo.pojo.RespuestaLoginApp;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author andre
 */
public class AutenticacionAppDAO {
    public static RespuestaLoginApp verificarSesionApp(String email, String password) {
        RespuestaLoginApp respuesta = new RespuestaLoginApp();
        respuesta.setError(true);

        try (SqlSession conexionDB = MyBatisUtil.getSesion()) {

            if (conexionDB != null) {
                try {
                    HashMap<String, String> parametros = new HashMap<>();
                    parametros.put("email", email);
                    parametros.put("password", password);
                    Paciente paciente = conexionDB.selectOne("autenticacion.loginApp", parametros);

                    if (paciente != null) {
                        respuesta.setError(false);
                        respuesta.setContenido("Bienvenido(a) " + paciente.getNombre() + " a la App Móvil para pacientes.");
                        respuesta.setPacienteSesion(paciente);
                    } else {
                        respuesta.setContenido("Correo electronico y/o contraseña incorrectos, porfavor de verificar los datos.");
                    }
                } catch (Exception e) {
                    respuesta.setContenido("Error: " + e.getMessage());
                } finally {
                    conexionDB.close();
                }
            } else {
                respuesta.setContenido("Error: Por el momento no hay conexion a la base de datos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }
}
