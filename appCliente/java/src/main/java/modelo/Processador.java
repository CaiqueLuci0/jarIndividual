package modelo;

public class Processador {

    private int idProcessador;
    private String nome;
    private String identificador;
    private String microarquitetura;
    private long frequencia;
    private int pacotesFisicos;
    private int cpusFisicas;
    private int cpusLogicas;

    public Processador (
            int idProcessador,
            String nome,
            String identificador,
            String microarquitetura,
            long frequencia,
            int pacotesFisicos,
            int cpusFisicas,
            int cpusLogicas)
    {
        this.cpusFisicas = cpusFisicas;
        this.idProcessador = idProcessador;
        this.cpusLogicas = cpusLogicas;
        this.nome = nome;
        this.identificador = identificador;
        this.frequencia = frequencia;
        this.pacotesFisicos = pacotesFisicos;
        this.microarquitetura = microarquitetura;
    }

    public Processador (){}

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public void setIdProcessador(int idProcessador) {
        this.idProcessador = idProcessador;
    }

    public void setFrequencia(long frequencia) {
        this.frequencia = frequencia;
    }

    public void setMicroarquitetura(String microarquitetura) {
        this.microarquitetura = microarquitetura;
    }

    public void setCpusFisicas(int cpusFisicas) {
        this.cpusFisicas = cpusFisicas;
    }

    public void setCpusLogicas(int cpusLogicas) {
        this.cpusLogicas = cpusLogicas;
    }

    public void setPacotesFisicos(int pacotesFisicos) {
        this.pacotesFisicos = pacotesFisicos;
    }

    public String getNome() {
        return nome;
    }

    public int getIdProcessador() {
        return idProcessador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public long getFrequencia() {
        return frequencia;
    }

    public int getPacotesFisicos() {
        return pacotesFisicos;
    }

    public String getMicroarquitetura() {
        return microarquitetura;
    }

    public int getCpusFisicas() {
        return cpusFisicas;
    }

    public int getCpusLogicas() {
        return cpusLogicas;
    }

    @Override
    public String toString() {
        return "Processador{" +
                "idProcessador=" + idProcessador +
                ", nome='" + nome + '\'' +
                ", identificador='" + identificador + '\'' +
                ", microarquitetura='" + microarquitetura + '\'' +
                ", frequencia='" + frequencia + '\'' +
                ", pacotesFisicos=" + pacotesFisicos +
                ", cpusFisicas=" + cpusFisicas +
                ", cpusLogicas=" + cpusLogicas +
                '}';
    }
}
