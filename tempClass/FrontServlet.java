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
import etu2090.framework.annotation.Session;
import etu2090.framework.annotation.Auth;
import etu2090.framework.annotation.scope;
import etu2090.framework.annotation.Argument;
import etu2090.framework.annotation.ApiRest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
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
import javax.servlet.http.HttpSession;

//import model.Dept;
//import model.Emp;

/**
 * Front Servlet qui gère les requêtes HTTP.
 */
public class FrontServlet extends HttpServlet {

   // private static final long serialVersionUID = 1L;
     HashMap<String, Mapping> mappingUrls=new HashMap <String, Mapping>();
     HashMap<Class<?>, Object> ClasseSingleton=new HashMap <Class<?>, Object>();
     String packages;
     Mapping mappingObject;
     String isconnected;
     String profil;


     String viewsDirectory;


     public Mapping getMappingObject() {
         return mappingObject;
     }
     public void setMappingObject(Mapping mappingObject) {
         this.mappingObject = mappingObject;
     }


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
    public void init(ServletConfig config) throws javax.servlet.ServletException{
        super.init(config);
        
        this.packages=getServletConfig().getInitParameter("modelPackage");
        this.isconnected=getServletConfig().getInitParameter("connected");
        this.profil=getServletConfig().getInitParameter("profil");
     
         try {
             this.getAllMapping(this.packages);
           this.getAllMappingSingleton();
         } catch (URISyntaxException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
             Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
       
                
    }


    
    public Method searchFonction(Object ob,String name) {
        Method[] methods=ob.getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().compareTo(name)==0) {
                return methods[i];
            }
            
        }
        return null;

    }
    @SuppressWarnings("empty-statement")
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
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
              //  out.println(paramName);
            }

            if (key.compareTo(page) == 0) {
                try {
                    PrintWriter oPrintWriter = resp.getWriter();
                    Class<?> class1 = Class.forName(packages + "." + mai.getClassName());

                    if (class1.getSimpleName().equals(mai.getClassName())) {
                        //out.print(page);
                        // out.print("cccccccccccccccccc");
                       // out.println(class1.getSimpleName());
                        // out.print("vvvvvvvvvvvv");
                        Object object =this.SingletonInstances(class1); //class1.getConstructor().newInstance();
                        Map<String, String[]> params = req.getParameterMap();

                        object = this.makaParametreDonnees(object, params, class1, req, resp); // maka an'ilay parametre
                                                                                               // avy any @ JSP
                        Method method=this.searchFonction(object, mai.getMethod());
                        this.checkAuthenf(req, resp, method);
                        //Method[] methods = class1.getDeclaredMethods();
                        // out.print("de awn kiiii");
                        //for (Method method1 : methods) {
                            if (method.getName().equals(mai.getMethod())) {
     
                              
                                Object[] arguments = this.mamenoParametreMethode(method, params, req, resp);
                                ModelView view = null;
                                // out.print("fa awn eeee");
                                if (arguments != null) {
                                    view = (ModelView) method.invoke(object, arguments);
                                    this.preapareSession(req,resp, view);
                                    this.prepareview(req, resp, view, method);
                                    this.prepareDispatch(req, resp, view);
                                } else {
                                    view = (ModelView) method.invoke(object);
                                    this.preapareSession(req,resp, view);
                                    this.prepareview(req, resp, view, method);
                                    this.prepareDispatch(req, resp, view);
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
                             // String modelString =""  view.getView();
                                RequestDispatcher dispatcher = req.getRequestDispatcher(modelString);
                                dispatcher.forward(req, resp);

                            }
                        //}
                    }
                } catch (Exception e) {
                    // out.println(e.getMessage());
                    e.printStackTrace(out);
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

    //sesssion
   //ref different de 0 de manambotr anle session izy 
    public void preapareSession(HttpServletRequest request, HttpServletResponse response,ModelView v){
        HttpSession session = request.getSession();
        if(v.getAuthenf().size()!=0){
            System.out.print("ssssssssssssssss");
             for (Map.Entry<String, Object> entry : v.getAuthenf().entrySet()) {
                 session.setAttribute(entry.getKey(), entry.getValue());
                 System.out.println(entry.getKey()+ "   "+entry.getValue());
             }
             System.out.println("PROFIL"+session.getAttribute("profil"));
            
        }

    
    
    }

    //mis auth ve le method verifier connecter ,alaina le session 
    public void checkAuthenf(HttpServletRequest request, HttpServletResponse response,Method m) throws Exception{
        //HttpSession session1 = request.getSession();
        //boolean val=(boolean)session1.getAttribute(this.isconnected);
        //System.out.print(val);
        if(m.isAnnotationPresent(Auth.class)){
         this.verifConnected(request, response);
          Auth authenf= (Auth) m.getAnnotation(Auth.class);
          String authen=authenf.value();
          System.out.println(authen);
          if(authen.compareTo("")!=0){
               HttpSession session = request.getSession(false);
               String profil=(String) session.getAttribute(this.profil);
               if(profil.compareTo(authen)!=0){
                   throw new Exception("Accès Refusé");
               }
          
          }
        }
    }

   //maka anle session ra null le izy de manao exception izy
    public void verifConnected(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession(false);
        if(session.getAttribute(this.isconnected)==null){
            throw new Exception("Vous etes pas Connecte");
        }
      
      }
      

      public void prepareview(HttpServletRequest request, HttpServletResponse response,ModelView v,Method m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        System.out.print("lalalalala");
        //  Annotation[] annotations = m.getAnnotations();
      java.lang.annotation.Annotation[] annotations=m.getAnnotations();  
      for (Annotation annotation:annotations) {
            System.out.println("ioioio"+annotation);
        }
        System.out.print("cocoucou");
        if(m.isAnnotationPresent(Session.class)){
           System.out.println("Nandalo fa session");
         
          HttpSession session = request.getSession();
         Enumeration<String> sessionNames = session.getAttributeNames();
        
         while (sessionNames.hasMoreElements()) {
            String sessionName = sessionNames.nextElement();
            System.out.println("Nom Session: "+sessionName);
            v.addSession(sessionName, session.getAttribute(sessionName ));
        }
         v.add("session", v.getSession());/* Reuperation du HashMap Session qui contient tous les session */
         //return v ;
       }
    
    }



    //JSON
    public void prepareDispatch(HttpServletRequest request, HttpServletResponse response,ModelView v) throws ServletException, IOException{
        if(v.getisJson()==true){
            for(Map.Entry<String,Object> e: v.getData().entrySet()){
                            String k=e.getKey();
                            Object o=e.getValue();
                            request.setAttribute(k, o);
            }
            String chemin="views/"+v.getView();
            System.out.println(chemin);
            RequestDispatcher dispat =request.getRequestDispatcher(chemin);
            dispat.forward(request,response);
        }else{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(v.getData());
        PrintWriter out = response.getWriter();
        out.println(json);
        }
    }
    
    public void checkVerifFonction(HttpServletRequest request, HttpServletResponse response,Map<String,String[]> valeur,Method fonction,Object ob) throws Exception{
        
        if(fonction.isAnnotationPresent(ApiRest.class)&& !fonction.getReturnType().equals(Void.TYPE)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            if(!valeur.isEmpty()){
               //  Object obj=this.makaParametreDonnees(ob, valeur, fonction, request,response);
               Object obj=this.mamenoParametreMethode(fonction, valeur, request, response)   ;
               String json = gson.toJson(obj);
                  PrintWriter out = response.getWriter();
                  out.println(json);
            }else{
                Object obj=fonction.invoke(ob);
                String json = gson.toJson(obj);
                PrintWriter out = response.getWriter();
                out.println(json);
            }
        }else{
          throw new Exception("La fonction retourne un Void ou ne Possede pas l'annotation APIrest");
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

//miverifier anle classe we annoter scope ve le izy deny valeur anle annotation singleton 
    // public void getAllMappingSingleton() throws InstantiationException,IllegalAccessException,ClassNotFoundException, URISyntaxException {
    //     for(Map.Entry<String,Mapping> entry:this.mappingUrls.entrySet())
    //     {
    //         Mapping map=entry.getValue();
    //         Class<?> classes = Class.forName(packages+ "." + map.getClassName());
    //             if (classes.isAnnotationPresent(scope.class)) {
    //                 scope annotation = classes.getAnnotation(scope.class);
    
    //                 // Vérifier la valeur de l'annotation "name"
    //                 if (annotation.name().equals("singleton")) {
    //                     Object object=classes.newInstance();
    //                     // Ajouter la classe et l'instance au HashMap
    //                     this.ClasseSingleton.put(classes, object);
    //                 }
    //             }
    //     }        
        
    //     }

         
    public void getAllMappingSingleton() throws InstantiationException,IllegalAccessException,ClassNotFoundException, URISyntaxException {
        for(Map.Entry<String,Mapping> entry:this.mappingUrls.entrySet())
        {
            Mapping map=entry.getValue();
            Class<?> classes = Class.forName(packages+ "." + map.getClassName());
                if (classes.isAnnotationPresent(scope.class)) {
                    scope annotation = classes.getAnnotation(scope.class);
                    boolean singleton=annotation.singleton();
                    // Vérifier la valeur de l'annotation "name"
                    if (singleton==true) {
                        Object object=classes.newInstance();
                        // Ajouter la classe et l'instance au HashMap
                        this.ClasseSingleton.put(classes, object);
                    }
                }
        }        
        
        }
     public Object SingletonInstances(Class<?> class1) throws InstantiationException,IllegalAccessException,IllegalArgumentException,Exception{
         for (Map.Entry<Class<?>, Object> entry : this.ClasseSingleton.entrySet()) {
             Class<?> classe = entry.getKey();
             Object instance = entry.getValue();

                     if (class1.equals(classe)) {
                        System.out.println(classe.getSimpleName()+"ok singleton");
                        System.out.println(class1.getSimpleName());
                        this.resetvaluedefault(instance);
                        return instance;
                     //   System.out.println("ooooooooooooooooo");
                    }
             }
             return class1.newInstance();
                    
     }
    
     public void resetvaluedefault(Object object) throws IllegalAccessException,IllegalArgumentException,Exception{
        Field[] fields=object.getClass().getDeclaredFields();
        for(Field field:fields)
        {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                Class<?> fieldtype=field.getType();
                Object valeurdefault=defaultvalue(fieldtype);
                field.set(object,valeurdefault);
                System.out.println("uuuuuuuuuuuuuuuu");
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

    public HashMap<Class<?>, Object> getClasseSingleton() {
        return ClasseSingleton;
    }
    public void setClasseSingleton(HashMap<Class<?>, Object> classeSingleton) {
        ClasseSingleton = classeSingleton;
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

    public Object[] mamenoParametreMethode(Method method, Map<String, String[]> params,HttpServletRequest req,HttpServletResponse resp) throws Exception{
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


    public Object makaParametreDonnees(Object object, Map<String, String[]> params, Class<?> class1,HttpServletRequest req,HttpServletResponse resp) throws Exception {
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
    
    //fonction mi reset ny valeur ny attribut ho lasa valeur par defaut daolo ny valeur an instance reetr
    private Object defaultvalue(Class<?> paramType) throws Exception {
        if (paramType == String.class) {
            return "null";
        } else if (paramType == int.class || paramType == Integer.class) {
            return 0;
        } else if (paramType == boolean.class || paramType == Boolean.class) {
            return false;
        }else if (paramType == double.class || paramType == Double.class) {
            return 0.0;
        }else {
            return null;
        }
    }

//Sprint 11
    // private void checkMethod(HttpServletRequest req, ServletConfig servletConfig) {
    //     Method method = this.getMappingTarget().getMethod();
    //     if (method.isAnnotationPresent(Auth.class)) {
    //         String sessionName = servletConfig.getInitParameter("sessionName");
    //         if (req.getSession().getAttribute(sessionName) != null) {
    //             String profileName = servletConfig.getInitParameter("sessionProfile");
    //             String profile = method.getAnnotation(Auth.class).value();
    //             if (profile.length() > 0) {
    //                 if (!req.getSession().getAttribute(profileName).equals(profile)) {
    //                     throw new RuntimeException("Length: " + profile.length());
    //                 }
    //             }

    //         } else {
    //             throw new RuntimeException("You are not allowed to access this page. Session name = " + sessionName);
    //         }
    //     }
    // }
    //         // checking user authentication
    //         this.checkMethod(req, servletConfig);



    //                 // model sessions
    //                 Map<String, String> sessions = ((ModelView) result).getSessions();
    //                 for (Map.Entry<String, String> entry : sessions.entrySet()) {
    //                     req.getSession().setAttribute(entry.getKey(), entry.getValue());
    //                 }
    
   
}