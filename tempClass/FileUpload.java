package etu2090.framework;

/**
 * FileUpload
 */
public class FileUpload {

    //Attribut
    String name;
    byte[] bytes;
    String path;

    public String getpath() {
        return path;
    }
    public void setpath(String path) {
        this.path = path;
    }

    public String getname() {
        return this.name;
    }
    public void setname(String name) {
        this.name=name;
    }
    public byte[] getbyte()
    {
        return this.bytes;
    }
    public void setbyte(byte[] bytes) {
        this.bytes=bytes;
    }
    public FileUpload(){}
    public FileUpload(String name,byte[] bytes,String path)
    {
         this.setname(name);
         this.setbyte(bytes);
         this.setpath(path);
    }

}