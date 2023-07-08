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
    private Map<String,String> sessions;

    public Map<String, String> getSessions() {
        return sessions;
    }
    public void setSessions(Map<String, String> sessions) {
        this.sessions = sessions;
    }
    // constructors
    public ModelView(String view) {
        this.setView(view);
        this.setData(new HashMap<>());
    }

    public ModelView(String view, Map<String, Object> data,Map<String,String> session) {
        this.setView(view);
        this.setData(data);
        this.setSessions(session);
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
}