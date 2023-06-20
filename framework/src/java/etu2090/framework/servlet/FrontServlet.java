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
import etu2090.framework.annotation.Argument;
import java.lang.reflect.Field;
import etu2090.framework.ModelViews.ModelView;
import java.io.File;
import java.util.Enumeration;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import java.text.*;
import java.util.Enumeration;
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
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception  {
        PrintWriter out = resp.getWriter();
        String url = req.getRequestURI();
        String page = url.substring(url.lastIndexOf("/") + 1);
    
        for (Map.Entry<String, Mapping> entry : this.mappingUrls.entrySet()) {
            String key = entry.getKey();
            Mapping mai = entry.getValue();
    

                  // Parcourir tous les paramètres et les valeurs du formulaire
                  Enumeration<String> paramNames = req.getParameterNames();
                  while (paramNames.hasMoreElements()) {
                  String paramName = paramNames.nextElement();
                    out.println(paramName);
                  }

            if (key.compareTo(page) == 0) {
                try {
                    PrintWriter oPrintWriter = resp.getWriter();
                    Class<?> class1 = Class.forName(packages + "." + mai.getClassName());
    
                    if (class1.getSimpleName().equals(mai.getClassName())) {
                        out.print(page);
    //                    out.print("cccccccccccccccccc");
                        out.println(class1.getSimpleName());
      //                  out.print("vvvvvvvvvvvv");
                        Object object = class1.newInstance();
                        Map<String, String[]> params = req.getParameterMap();
                        
                        object = this.makaParametreDonnees(object, params, class1); // maka an'ilay parametre avy any @ JSP
                  
                        Method[] methods = class1.getDeclaredMethods();
  //                      out.print("de awn kiiii");
                        for (Method method1 : methods) {
                            if (method1.getName().equals(mai.getMethod())) {
                                out.println(method1.getName());
                                Object[] arguments = this.mamenoParametreMethode(method1, params); // mameno parametre an'ilay fonction
                                ModelView view = null;
                                // out.print("fa awn eeee");
                                if (arguments != null) {
                                    view = (ModelView) method1.invoke(object, arguments);
                                    //out.print("mafy enao ee");
                                } else {
                                    view = (ModelView) method1.invoke(object);
                                  //  out.print("met zan ee");
                                }
    
                                out.print("View = " + view.getView());
    
                                if (view.getData() != null) {
                                    for (Map.Entry<String, Object> entry2 : view.getData().entrySet()) {
                                        String key2 = entry2.getKey();
                                        Object value = entry2.getValue();
                                        req.setAttribute(key2, value);
                                    }
                                }
    
                                String modelString = "views/" + view.getView();
                                RequestDispatcher dispatcher = req.getRequestDispatcher(modelString);
                                dispatcher.forward(req, resp);
                            }
                        }
                    }
                } catch (Exception e) {
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
        }else if (paramType == double.class || paramType == Double.class) {
            return Double.parseDouble(paramValue);
       } else if (paramType == Long.class || paramType == long.class) {
            return Long.parseLong(paramValue);
        } else if (paramType == Date.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return new java.sql.Date(formatter.parse(paramValue).getTime());
        }else if (paramType == Timestamp.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new java.sql.Timestamp(formatter.parse(paramValue).getTime());
           }else if(paramType == Time.class) {
              SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            return new java.sql.Time(formatter.parse(paramValue).getTime());
        }else {
            return null;
        }
    }


    public Object liste(Class<?> type, String[] value){
        if (type == String.class) {
            return value;
        }
        else if (type == Integer.class || type == int.class) {
            int[] liste = new int[value.length];
            for(int i=0; i<value.length; i++) {
                liste[i] = Integer.parseInt(value[i]);
            }
            return liste;
        }
        else if (type == Double.class || type == double.class) {
            double[] liste = new double[value.length];
            for(int i=0; i<value.length; i++) {
                liste[i] = Double.parseDouble(value[i]);
            }
            return liste;
        }
        else {
            return null;
        }
    }

    public Object[] mamenoParametreMethode(Method method, Map<String, String[]> params) throws Exception{
        Object[] arguments = null;
        if(params.isEmpty()==false){
            Parameter[] parameters = method.getParameters();
            if(parameters.length != 0) {
                arguments = new Object[parameters.length];
                int i = 0;
                for (Parameter parameter : parameters) {
                    for (String paramName : params.keySet()) {
                        if(paramName.equals(parameter.getAnnotation(Argument.class).value())) {
                            String[] values = params.get(paramName);
                            Object reponse = null;
                            if(values!=null && values.length == 1){
                                arguments[i] = convertParamValue(values[0],parameter.getType());
                            } 
                            
                            else if(values!=null && values.length > 1) {
                                arguments[i] = liste(parameter.getType(), values);
                            }  
                        }   
                    }
                    i++;
                }     
            }
        }
        return arguments;
    }


    public Object makaParametreDonnees(Object object, Map<String, String[]> params, Class<?> class1) throws Exception {
        // Obtient tous les champs de la classe
        Field[] fields = class1.getDeclaredFields();
        
        for (Field field : fields) {
            // Vérifie si le champ correspond à un paramètre dans le formulaire
            String fieldName = field.getName();
            if (params.containsKey(fieldName)) {
                // Obtient la valeur du paramètre du formulaire
                String[] fieldValues = params.get(fieldName);
                String fieldValue = fieldValues[0]; // Suppose qu'il y a une seule valeur pour le paramètre
                
                // Convertit la valeur du paramètre au type approprié
                Object convertedValue = convertParamValue(fieldValue, field.getType());
                
                // Définit la valeur du champ dans l'objet
                field.setAccessible(true);
                field.set(object, convertedValue);
            }
        }
        
        return object;
    }
    


    //else if (paramType.toString() == "java.sql.Date") {
      //  return java.sql.Date.valueOf(paramValue);
    
}