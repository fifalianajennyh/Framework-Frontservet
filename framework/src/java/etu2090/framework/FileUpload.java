package etu2090.framework;

/**
 * FileUpload
 */
public class FileUpload {

    //Attribut
    String name;
    byte[] bytes;

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
    public FileUpload(String name,byte[] bytes)
    {
         this.setname(name);
         this.setbyte(bytes);
    }

}