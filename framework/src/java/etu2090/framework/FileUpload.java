package etu2090.framework;

/**
 * FileUpload
 */
public class FileUpload {

    //Attribut
    String name;
    Byte[] bytes;

    public String getname() {
        return this.name;
    }
    public void setname(String name) {
        this.name=name;
    }
    public Byte[] getbyte()
    {
        return this.bytes;
    }
    public void setbyte(Byte[] bytes) {
        this.bytes=bytes;
    }
    public FileUpload(){}
    public FileUpload(String name,Byte[] bytes)
    {
         this.setname(name);
         this.setbyte(bytes);
    }

}