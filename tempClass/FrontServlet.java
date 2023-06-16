/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2090.framework.servlet;
import etu2090.framework.FileUpload;
import etu2090.framework.Mapping;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import etu2090.framework.annotation.Url;
import java.lang.reflect.Field;
import etu2090.framework.ModelViews.ModelView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
//import model.Dept;
//import model.Emp;

/**
 * Front Servlet qui gère les requêtes HTTP.
 */
public class FrontServlet extends HttpServlet {

   // private static final long serialVersionUID = 1L;
     HashMap<String, Mapping> mappingUrls=new HashMap <String, Mapping>();
     String packages;
     
     String viewsDirectory;

     public String getViewsDirectory() {
         return viewsDirectory;
     }
 
     public void setViewsDirectory(String viewsDirectory) {
         this.viewsDirectory = viewsDirectory;
     }


     public String makeUpperCaseInitialLetter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
   


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
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //IOException, URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, ServletException
        PrintWriter out=resp.getWriter();
        String url=req.getRequestURI();
        String page=url.substring(url.lastIndexOf("/")+1);
        

      for(Map.Entry<String, Mapping> entry : this.mappingUrls.entrySet()) 
            {
               String key = entry.getKey();
                Mapping mai = entry.getValue();
                
                /*out.println("valeur de url    " + key + "     " + "    Nom de la classe qui a l'annotation       " + mai.getClassName() + "       " + "      methodes qui a l'annotation  " + mai.getMethod());
                out.println(page);
                out.println(key);*/
            if (key.compareTo(page)==0) {
                
                try {
                    PrintWriter oPrintWriter=resp.getWriter();
                    Class<?> class1=Class.forName(packages+"."+mai.getClassName());
                    Object object=class1.newInstance();
                   Method method=object.getClass().getMethod(mai.getMethod());

                 //  oPrintWriter.println("test");


                        //recupere les attributs de la classe
                        Field[] field = object.getClass().getDeclaredFields();

                        //les transformer en tableau de string pour la comparaison
                        String[] attributs = new String[field.length];
                        for(int j=0;j<field.length;j++)
                        {
                            attributs[j] = field[j].getName();
                            /*oPrintWriter.println("attribut classe");
                            oPrintWriter.print(attributs[j]);*/
                        }


                  // Parcourir tous les paramètres et les valeurs du formulaire
                   Enumeration<String> paramNames = req.getParameterNames();
                   while (paramNames.hasMoreElements()) {
                   String paramName = paramNames.nextElement();
                    /*  out.println(paramName);
                     out.print("trueeeeee");*/
                     
                     //Verifier si le parametre fait partie des attributs de la classe 
                     for(int j=0;j<attributs.length;j++)
                     {
                         if(attributs[j].compareTo(paramName)==0)
                         {
                             String[] paramValues = req.getParameterValues(paramName);
                             Method met= object.getClass().getMethod("set"+attributs[j], field[j].getType());
                             Object paramValue = convertParamValue(paramValues[0], field[j].getType());
                             met.invoke(object,paramValue);
                         }
                         
                     }

                   }
                    /*Map<String, String[]> params = req.getParameterMap();
                    for(Map.Entry<String, String[]> param : params.entrySet()) {
                        Field attr = object.getClass().getField(param.getKey());
                       object.getClass().getMethod("set"+this.makeUpperCaseInitialLetter(param.getKey()), attr.getType()).invoke(object, attr.getType().cast(param.getValue()[0]));
                    }*/


                ModelView view=(ModelView)method.invoke(object);
                    String modelString="views/"+view.getView();
                    Map<String, Object> data = view.getData();
                   for (Map.Entry<String,Object> dEntry: view.getData().entrySet()) {
                    String k=dEntry.getKey();
                    Object o=dEntry.getValue();
                    req.setAttribute(k,o);
                   }
                 RequestDispatcher dispatcher = req.getRequestDispatcher(modelString);
                   dispatcher.forward(req, resp);                   
                }catch(Exception e){
                    out.println(e.getMessage());
                
                }

                
                }
        
        }
            
            
     }    
         
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        try {
            try {
                processRequest(request, response);
            }catch(Exception ex){
           // } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch(Exception ex){
        //} catch (URISyntaxException | ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        try {
            processRequest(request, response);
        }catch(Exception ex){
      //  } catch (URISyntaxException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
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

    private Object convertParamValue(String paramValue, Class<?> paramType) throws Exception {
        if (paramType == String.class) {
            return paramValue;
        } else if (paramType == int.class || paramType == Integer.class) {
            return Integer.parseInt(paramValue);
        } else if (paramType == boolean.class || paramType == Boolean.class) {
            return Boolean.parseBoolean(paramValue);
        } else if (paramType == double.class || paramType == Double.class) {
            return Double.parseDouble(paramValue);
        } else if (paramType == Long.class || paramType == long.class) {
            return Long.parseLong(paramValue);
        } else if (paramType == Date.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return new java.sql.Date(formatter.parse(paramValue).getTime());
        } else if (paramType == java.sql.Timestamp.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new java.sql.Timestamp(formatter.parse(paramValue).getTime());
        } else if (paramType == java.sql.Time.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            return new java.sql.Time(formatter.parse(paramValue).getTime());
        } else if (paramType == FileUpload.class) {
            // Supposons que le paramètre "paramValue" contienne le chemin du fichier à uploader
            File file = new File("D:/fichier/test.txt");
    
            // Vérifiez si le fichier existe
            if (file.exists()) {
                try {
                    // Lisez les octets du fichier
                    byte[] fileBytes = Files.readAllBytes(Path.of(file.getAbsolutePath()));
    
                    // Récupérez le nom du fichier
                    String fileName = file.getName();
    
                    // Créez une nouvelle instance de FileUpload avec le nom et les octets du fichier
                    FileUpload fileUpload = new FileUpload();
                    fileUpload.setname(fileName);
                    fileUpload.setbyte(fileBytes);
    
                    // Retournez l'instance de FileUpload
                    return fileUpload;
                } catch (IOException e) {
                    // Gérez l'exception d'E/S si elle se produit lors de la lecture des octets du fichier
                    e.getMessage();
                    throw new Exception("Erreur lors de la lecture du fichier : " + paramValue);
                }
            } else {
                throw new FileNotFoundException("Le fichier n'existe pas : " + paramValue);
            }
        } else {
            return null;
        }
    }
    //else if (paramType.toString() == "java.sql.Date") {
      //  return java.sql.Date.valueOf(paramValue);
      
    
}