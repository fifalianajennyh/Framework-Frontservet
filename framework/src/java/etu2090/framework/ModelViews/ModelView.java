/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2090.framework.ModelViews;

/**
 *
 * @author itu
 */
import java.util.HashMap;
import java.util.Map;

public class ModelView {

    private String view;
    private Map<String, Object> data;
    private Map<String,Object> session=new HashMap<>();
    private Map<String,Object> authenf=new HashMap<>();
   boolean isJson;

   public boolean getisJson()
   {
      return this.isJson;
   }
   public void setJson(boolean isJson) {
       this.isJson = isJson;
   }
    public Map<String, Object> getAuthenf() {
    return authenf;
}
public void setAuthenf(Map<String, Object> authenf) {
    this.authenf = authenf;
}
public Map<String, Object> getSession() {
    return session;
}
public void setSession(Map<String, Object> session) {
    this.session = session;
}
    // constructors
    public ModelView(String view) {
        this.setView(view);
        this.setData(new HashMap<>());
    }

    public ModelView(String view, Map<String, Object> data,Map<String,Object> session) {
        this.setView(view);
        this.setData(data);
        this.setSession(session);
    }

    // setters
    public void setView(String view) {
        this.view = view;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    // getters
    public String getView() {
        return this.view;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    // methods
    public void add(String key, Object value) {
        this.data.put(key, value);
    }
    
    public void addSession(String key, Object value) {
        this.session.put(key, value);
    }
    public void addAuthenf(String key,Object value)
    {
        this.authenf.put(key, value);
    }
}