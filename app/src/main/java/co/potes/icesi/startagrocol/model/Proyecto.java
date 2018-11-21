package co.potes.icesi.startagrocol.model;

public class Proyecto {

    public static String INVERSION_RECOMPENSA= "RECOMPENSA";
    public static String INVERSION_CREDITO = "CREDITO";


    private String imagenPrimaria;
    private String descripcion;
    private String titulo;
    private String id;
    private String Resumen;
    private String imagenSecundaria;
    private String fechaCierreProyecto;
    private String valorRecolectado;
    private String MetodoInversion;
    private String tipoProyecto;


    public Proyecto() {
    }



    public static String getInversionRecompensa() {
        return INVERSION_RECOMPENSA;
    }

    public static void setInversionRecompensa(String inversionRecompensa) {
        INVERSION_RECOMPENSA = inversionRecompensa;
    }

    public static String getInversionCredito() {
        return INVERSION_CREDITO;
    }

    public static void setInversionCredito(String inversionCredito) {
        INVERSION_CREDITO = inversionCredito;
    }

    public String getImagenPrimaria() {
        return imagenPrimaria;
    }

    public void setImagenPrimaria(String imagenPrimaria) {
        this.imagenPrimaria = imagenPrimaria;
    }

    public String getResumen() {
        return Resumen;
    }

    public void setResumen(String resumen) {
        Resumen = resumen;
    }

    public String getImagenSecundaria() {
        return imagenSecundaria;
    }

    public void setImagenSecundaria(String imagenSecundaria) {
        this.imagenSecundaria = imagenSecundaria;
    }

    public String getFechaCierreProyecto() {
        return fechaCierreProyecto;
    }

    public void setFechaCierreProyecto(String fechaCierreProyecto) {
        this.fechaCierreProyecto = fechaCierreProyecto;
    }

    public String getValorRecolectado() {
        return valorRecolectado;
    }

    public void setValorRecolectado(String valorRecolectado) {
        this.valorRecolectado = valorRecolectado;
    }

    public String getMetodoInversion() {
        return MetodoInversion;
    }

    public void setMetodoInversion(String metodoInversion) {
        MetodoInversion = metodoInversion;
    }

    public String getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(String tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
