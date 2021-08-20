package pe.gob.reniec.padronn.logic.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.UsuarioExterno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class UsuarioExternoDaoTest {
    @Autowired
    UsuarioExternoDao usuarioExternoDao;

    Gson gson=new Gson();
    Gson gsonBeautiful=new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testInsert() {
        UsuarioExterno usuarioExterno = new UsuarioExterno();

        usuarioExterno.setCoUsuario("42691699");
        usuarioExterno.setCoEntidad(5);
        usuarioExterno.setApPrimer("flores");
        usuarioExterno.setApSegundo("flores");
        usuarioExterno.setPrenombres("juan perez");
        usuarioExterno.setDePassword("42691610");
        usuarioExterno.setDeEmail("test@test.com");
        usuarioExterno.setUsCreaAudi("41691600");
        usuarioExterno.setUsModiAudi("41691600");

        List<String> entidades = new ArrayList<String>();

        List<String> grupos = new ArrayList<String>();
        entidades.add("5");
        grupos.add("1");
        usuarioExterno.setEntidades(entidades);
        usuarioExterno.setGrupos(grupos);


        //usuarioExterno.setc
        //System.out.println(gsonBeautiful.toJson(usuarioExterno));

        System.out.println(usuarioExterno);
        if(usuarioExternoDao.insert(usuarioExterno))
            System.out.println("OK: Insert");
        else
            System.out.println("ERROR: Insert");
    }

    @Test
    public void testUpdate() throws Exception {
        UsuarioExterno usuarioExterno = usuarioExternoDao.getUsuarioExterno("42691610");

        usuarioExterno.setDePassword("1111");

        List<String> grupos = new ArrayList<String>();
        grupos.add("2");
        usuarioExterno.setGrupos(grupos);

        if( usuarioExternoDao.update(usuarioExterno) )
            System.out.println("OK: update");
        else
            System.out.println("ERROR: update");
    }

    @Test
    public void testGetAll() throws Exception {
        System.out.println(usuarioExternoDao.getAll(1,20));
    }

    public void testGetPermisoModulo() throws Exception {

    }

    public void testGetGrupoUsuario() throws Exception {

    }

    public void testGetUsuarioExterno() throws Exception {

    }

    @Test
    public void testGetGrupos(){
        System.out.println(usuarioExternoDao.getGrupos());
    }

    @Test
    public void testGetEntidades(){
        System.out.println(usuarioExternoDao.getEntidades());
    }

    @Test
    public void testGetGruposUsuario() {
        System.out.println(usuarioExternoDao.getGruposUsuario("41691600"));
    }

    @Test
    public void testGetAuthorities() {
        System.out.println(usuarioExternoDao.getGrupo("1"));
    }

    @Test
    public void testBusquedaUsuario() {
        List<UsuarioExterno> usuarioExternoList = usuarioExternoDao.buscarUsuarioExterno("", "", "jua", 1, 10);
        System.out.println(usuarioExternoList);
    }

    @Test
    public void testGetUsuarioAni() {
        System.out.println(usuarioExternoDao.getUsuarioAni("41691600"));
    }

}
