/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2090.framework.servlet;

import etu2090.framework.Mapping;
import etu2090.framework.FileUpload;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import etu2090.framework.annotation.Url;
//import etu2090.framework.model.Dept;
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
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import java.text.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
//import model.Dept;
//import model.Emp;

/**
 * Front Servlet qui gère les requêtes HTTP.
 */
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
                 maxFileSize=1024*1024*50,      	// 50 MB
                 maxRequestSize=1024*1024*100) 
// location="D:/doc"
public class FrontServlet extends HttpServlet {

    // private static final long serialVersionUID = 1L;
    HashMap<String, Mapping> mappingUrls = new HashMap<String, Mapping>();
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
     * 
     * @param config
     * @throws javax.servlet.ServletException
     */

    @Override
    public void init(ServletConfig config) throws javax.servlet.ServletException {
        super.init(config);

        this.packages = getServletConfig().getInitParameter("modelPackage");

        try {
            this.getAllMapping(this.packages);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

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
                out.println(paramName);
            }

            if (key.compareTo(page) == 0) {
                try {
                    PrintWriter oPrintWriter = resp.getWriter();
                    Class<?> class1 = Class.forName(packages + "." + mai.getClassName());

                    if (class1.getSimpleName().equals(mai.getClassName())) {
                        out.print(page);
                        // out.print("cccccccccccccccccc");
                        out.println(class1.getSimpleName());
                        // out.print("vvvvvvvvvvvv");
                        Object object = class1.getConstructor().newInstance();
                        Map<String, String[]> params = req.getParameterMap();

                        object = this.makaParametreDonnees(object, params, class1, req, resp); // maka an'ilay parametre
                                                                                               // avy any @ JSP

                        Method[] methods = class1.getDeclaredMethods();
                        // out.print("de awn kiiii");
                        for (Method method1 : methods) {
                            if (method1.getName().equals(mai.getMethod())) {
                                out.println(method1.getName());

                                // Object[] arguments = this.mamenoParametreMethode(method1, params); // mameno
                                // parametre an'ilay fonction

                                Object[] arguments = this.mamenoParametreMethode(method1, params, req, resp);
                                ModelView view = null;
                                // out.print("fa awn eeee");
                                if (arguments != null) {
                                    view = (ModelView) method1.invoke(object, arguments);
                                    // out.print("mafy enao ee");
                                } else {
                                    view = (ModelView) method1.invoke(object);
                                    // out.print("met zan ee");
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
                    // out.println(e.getMessage());
                    e.printStackTrace(out);
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        try {
            try {
                processRequest(request, response);
            } catch (Exception ex) {
                // } catch (InstantiationException | IllegalAccessException |
                // InvocationTargetException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            // } catch (URISyntaxException | ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            // } catch (URISyntaxException | ClassNotFoundException | InstantiationException
            // | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getAllMapping(String packagename) throws URISyntaxException {
        String path = packagename.replaceAll("[.]", "/");
        URL packageURL = Thread.currentThread().getContextClassLoader().getResource(path);
        File packageDirectory = new File(packageURL.toURI());
        File[] inside = packageDirectory.listFiles();
        for (int i = 0; i < inside.length; i++) {
            String[] n = inside[i].getName().split("[.]");

            try {
                Class<?> clazz = Class.forName(packagename + "." + n[0]);
                for (int j = 0; j < clazz.getMethods().length; j++) {
                    if (clazz.getMethods()[j].isAnnotationPresent(Url.class)) {
                        Url u = clazz.getMethods()[j].getAnnotation(Url.class);
                        String cle = u.value();
                        String classN = clazz.getSimpleName();
                        String fonction = clazz.getMethods()[j].getName();
                        Mapping m = new Mapping(classN, fonction);
                        this.mappingUrls.put(cle, m);
                    }
                }
            } catch (ClassNotFoundException e) {

            }

        }
    }

    /**
     * Retourne la map des urls mappées.
     * 
     * @return
     */
    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    /**
     * Définit la map des urls mappées.
     * 
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
        } else if (paramType == Timestamp.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new java.sql.Timestamp(formatter.parse(paramValue).getTime());
        } else if (paramType == Time.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            return new java.sql.Time(formatter.parse(paramValue).getTime());
        } else {
            return null;
        }
    }

    public Object liste(Class<?> type, String[] value) {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            int[] liste = new int[value.length];
            for (int i = 0; i < value.length; i++) {
                liste[i] = Integer.parseInt(value[i]);
            }
            return liste;
        } else if (type == Double.class || type == double.class) {
            double[] liste = new double[value.length];
            for (int i = 0; i < value.length; i++) {
                liste[i] = Double.parseDouble(value[i]);
            }
            return liste;
        } else {
            return null;
        }
    }

    public Object[] mamenoParametreMethode(Method method, Map<String, String[]> params, HttpServletRequest req,
            HttpServletResponse resp) throws Exception {
        Object[] arguments = null;
        if (!params.isEmpty()) {
            Parameter[] parameters = method.getParameters();
            if (parameters.length != 0) {
                arguments = new Object[parameters.length];
                int i = 0;
                for (Parameter parameter : parameters) {
                    Argument annotation = parameter.getAnnotation(Argument.class);
                    if (annotation != null) {
                        String paramName = annotation.value();
                        String[] values = params.get(paramName);
                        if (parameter.getType() == FileUpload.class) {
                            // this.checkFileUpload(arguments[i], req, resp);
                            arguments[i] = this.preparefile(paramName, req, resp);
                          }
                        else if (values != null && values.length > 0) {
                            if (parameter.getType().isArray()) {
                                arguments[i] = liste(parameter.getType().getComponentType(), values);
                            } else {
                                    System.out.println(paramName);
                                    arguments[i] = convertParamValue(values[0], parameter.getType());
                            }
                        }
                        
                    } 
                    i++;
                }
            }
        }
        return arguments;
    }

    public Object makaParametreDonnees(Object object, Map<String, String[]> params, Class<?> class1,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        this.checkFileUpload(object, request, response);

        return object;
    }

    ////////////////////////////////////////////

    public void checkFileUpload(Object ob, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {
        Field[] attribut = ob.getClass().getDeclaredFields();
        Method[] fonction = ob.getClass().getDeclaredMethods();
        for (int i = 0; i < attribut.length; i++) {
            if (attribut[i].getType() == FileUpload.class) {
                for (int j = 0; j < fonction.length; j++) {
                    if (fonction[j].getName().compareTo("set" + attribut[i].getName()) == 0) {
                        // System.out.println("hhhhhhhhhhhhhhhh");
                        FileUpload f = this.preparefile(attribut[i].getName(), request, response);
                        fonction[j].invoke(ob, f);
                    }
                }
            }

        }
    }

    public FileUpload preparefile(String file, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {
        try {
            FileUpload fichier = new FileUpload();
            Part filePart = null;
            // System.out.println(file);
            java.util.Collection<Part> parts = request.getParts();
            for (Part p : parts) {
                if (p.getName().equals(file)) {
                    filePart = p;
                    break;
                }
            }
            String submittedFileName = filePart.getSubmittedFileName();
            fichier.setname(submittedFileName);
            // System.out.println(submittedFileName);
            InputStream fileInputStream = filePart.getInputStream();
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] fileBytes = byteOutputStream.toByteArray();

            Byte[] fileBytesWrapper = new Byte[fileBytes.length];
            for (int i = 0; i < fileBytes.length; i++) {
                fileBytesWrapper[i] = Byte.valueOf(fileBytes[i]);
            }
            fichier.setbyte(fileBytesWrapper);
            fileInputStream.close();
            byteOutputStream.close();
            System.out.println(fichier.getname());
            System.out.println(fichier.getbyte());
            return fichier;

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println(e.getMessage());
            return null;
        }

    }

    // else if (paramType.toString() == "java.sql.Date") {
    // return java.sql.Date.valueOf(paramValue);

}