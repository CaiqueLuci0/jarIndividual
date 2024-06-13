package modelo;

public class LogJar {
    private String causa;
    private String mensagem;

    public LogJar(String causa, String mensagem) {
        this.causa = causa;
        this.mensagem = mensagem;
    }

    public LogJar() {}

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
