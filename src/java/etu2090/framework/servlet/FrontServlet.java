/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2090.framework.servlet;
import etu2090.framework.Mapping;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import etu2090.framework.annotation.Url;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
//import model.Dept;
//import model.Emp;

/**
 * Front Servlet qui gère les requêtes HTTP.
 */
public class FrontServlet extends HttpServlet {

   // private static final long serialVersionUID = 1L;
     HashMap<String, Mapping> mappingUrls=new HashMap <String, Mapping>();
     String packages;
    /**
     * Initialise la servlet.
     * @param config
     * @throws javax.servlet.ServletException
     */

    @Override
    public void init(ServletConfig config) throws javax.servlet.ServletException {
        super.init(config);
        
        this.packages=getServletConfig().getInitParameter("modelPackage");
     
         try {
             this.getAllMapping(this.packages);
         } catch (URISyntaxException ex) {
             Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
       
                
    }

    @SuppressWarnings("empty-statement")
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        PrintWriter out=resp.getWriter();
                       ///fomba fiafficher na HashMap   
     //     out.println(this.packages); 
      for(Map.Entry<String, Mapping> entry : this.mappingUrls.entrySet()) 
            {
               String key = entry.getKey();
                Mapping mai = entry.getValue();
                out.println("valeur de url    " + key + "     " + "    Nom de la classe qui a l'annotation       " + mai.getClassName() + "       " + "      methodes qui a l'annotation  " + mai.getMethod());
            }    
         
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        try {
            try {
                processRequest(request, response);
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (URISyntaxException | ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (URISyntaxException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getAllMapping(String packagename) throws URISyntaxException{
        String path=packagename.replaceAll("[.]", "/");
        URL packageURL=Thread.currentThread().getContextClassLoader().getResource(path);
        File packageDirectory=new File(packageURL.toURI());
        File [] inside=packageDirectory.listFiles();
        for (int i = 0; i < inside.length; i++) {
                    String [] n=inside[i].getName().split("[.]");
                    
                 try{
                  Class<?> clazz = Class.forName(packagename+"."+n[0]);
                  for(int j=0;j<clazz.getMethods().length;j++){
                  if(clazz.getMethods()[j].isAnnotationPresent(Url.class)){
                    Url u=clazz.getMethods()[j].getAnnotation(Url.class);
                    String cle=u.value();
                    String classN=clazz.getSimpleName();
                    String fonction=clazz.getMethods()[j].getName();
                    Mapping m=new Mapping(classN,fonction);
                    this.mappingUrls.put(cle, m);
                  }
                  }
                  
                  
                  
                 }catch (ClassNotFoundException e) {
                     
            }
                
           }
        }



        
        //Class<?> classe = Class.forName("etu2090.framework.Mapping");
        //Method[] methods = classe.getDeclaredMethods();
        //for (Method method : methods) {
        //  if (method.isAnnotationPresent(Url.class)) {
        //    Url urlAnnotation = method.getAnnotation(Url.class);
        //Mapping mapping = new Mapping();
        //mapping.setClassName(urlAnnotation.value());
        //mapping.setMethod(method.getName());
        //mappingUrls.put(urlAnnotation.value(), mapping);
        //}
        //}








/**
 * Retourne la map des urls mappées.
     * @return 
 */
    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

/**
 * Définit la map des urls mappées.
     * @param mappingUrls
 */
    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }
}
