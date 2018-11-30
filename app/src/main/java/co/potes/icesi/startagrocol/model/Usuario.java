package co.potes.icesi.startagrocol.model;

public class Usuario {



    public static  final String EMPRENDEDOR = "UsuarioEmprendedor";
    public  static final String INVERSOR = "UsuarioInversor";

    private String uid;
    private String nombre;
    private String email;
    private String telefono;
    private String contrasenia;
    private String tipo;
    private String urlImagen;

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Usuario(String uid, String nombre, String email, String telefono, String contrasenia) {
        this.uid = uid;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
    }

    public Usuario (){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
