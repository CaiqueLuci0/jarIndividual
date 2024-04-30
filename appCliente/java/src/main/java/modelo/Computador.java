package modelo;



import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.janelas.JanelaGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.util.Conversor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Computador {
    private int idComputador;
    private String nome;
    private String codPatrimonio;
    private String maxRam;
    private String maxDisco;
    private String sistemaOperacional;
    private String tempoDeAtividade;
    private int fkProcessador;
    private int fkDepartamento;
    private int fkHospital;
    private Departamento departamento;
    private Processador processador;

    public Looca looca = new Looca();
    public Memoria memoria = looca.getMemoria();
    public Sistema sistema = looca.getSistema();
    public com.github.britooo.looca.api.group.processador.Processador loocaProcessador = looca.getProcessador();
    public JanelaGrupo janelaGrupo = looca.getGrupoDeJanelas();
    public List<Janela> listaJanelas = janelaGrupo.getJanelas();
    public DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
    public List<Volume> volumes = grupoDeDiscos.getVolumes();
    public List<Long> porcentagemVolumes = new ArrayList<>();

    public Computador(int idComputador, String nome, String codPatrimonio, String maxRam, String maxDisco)
    {
        this.idComputador      = idComputador;
        this.nome              = nome;
        this.codPatrimonio     = codPatrimonio;
        this.maxRam            = maxRam;
        this.maxDisco          = maxDisco;
    }

    public Computador(){
        this.departamento = new Departamento();
    }

    //GETTERS
    public String getQuantidadeMaximaMemoria(){
        return Conversor.formatarBytes(memoria.getTotal());
    }

    public String getUsoMemoria(){
        return Conversor.formatarBytes(memoria.getEmUso());
    }

    public Double getPorcentagemConsumoMemoria(){
        return memoria.getEmUso() * 100.0 / memoria.getTotal();
    }


    List<Janela> listaGuias = new ArrayList<>();
    public List<Janela> getJanelas() {
        for (Janela listaJanela : listaJanelas) {
            if (listaJanela.getTitulo().contains("Google Chrome") || listaJanela.getTitulo().contains("Edge") || listaJanela.getTitulo().contains("Firefox") || listaJanela.getTitulo().contains("Opera")) {
                listaGuias.add(listaJanela);
            }
        }
        return listaGuias;
    }

    public List<Long> getPorcentagemDeTodosVolumes(){
        for (Volume volume : volumes) {
            if(volume.getTotal() > 0){
                porcentagemVolumes.add((volume.getTotal() - volume.getDisponivel()) * 100 / volume.getTotal());
            }
        }
        return porcentagemVolumes;
    }

    // percorrer a lista de % de consumo de discos e pegar o maior número da lista
    public Double getDiscoComMaisConsumo(List<Long>porcentagemVolumes){
        Optional<Double> menorPorcentDisco = porcentagemVolumes.stream()
                .map(e -> e.doubleValue())
                .max(Comparator.naturalOrder());

        return menorPorcentDisco.get();
    }

    public String getNomeProcessador(){
        return looca.getProcessador().getNome();
    }
    public Integer getCpusFisicas(){
        return looca.getProcessador().getNumeroCpusFisicas();
    }
    public Integer getCpusLogicas(){
        return looca.getProcessador().getNumeroCpusLogicas();
    }
    public Double getPorcentagemConsumoCpu(){
        return looca.getProcessador().getUso();
    }

    public String getMaiorDisco() {
        Volume maiorVol = this.volumes.get(0);
        for (Volume volume : this.volumes) {
            if (volume.getTotal() > maiorVol.getTotal()){
                maiorVol = volume;
            }
        }
        return Conversor.formatarBytes(maiorVol.getTotal());
    }

    public int getIdComputador() {
        return this.idComputador;
    }
    public String getMaxDisco() {
        return this.maxDisco;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public String getTempoDeAtividade() {
        return tempoDeAtividade;
    }

    public Departamento getDepartamento(){
        return this.departamento;
    }

    public String getNome() {
        return nome;
    }

    public int getFkHospital() {
        return fkHospital;
    }

    public int getFkDepartamento() {
        return fkDepartamento;
    }

    public int getFkProcessador() {
        return fkProcessador;
    }

    public Processador getProcessador() {
        return processador;
    }

    public String getMaxRam() {
        return maxRam;
    }

    //SETTERS

    public void setCodPatrimonio(String codPatrimonio) {
        this.codPatrimonio = codPatrimonio;
    }

    public void setMaxDisco(String maxDisco) {
        this.maxDisco = maxDisco;
    }

    public void setMaxRam(String maxRam) {
        this.maxRam = maxRam;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setFkProcessador(int fkProcessador) {
        this.fkProcessador = fkProcessador;
    }

    public void setProcessador(Processador processador) {
        this.processador = processador;
    }

    public void setTempoDeAtividade(String tempoDeAtividade) {
        this.tempoDeAtividade = tempoDeAtividade;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
        departamento.addComputadores(this);
    }
    public void setIdComputador(int idComputador) {
        this.idComputador = idComputador;
    }

    public void setFkDepartamento(int fkDepartamento) {
        this.fkDepartamento = fkDepartamento;
    }

    public void setFkHospital(int fkHospital) {
        this.fkHospital = fkHospital;
    }

    @Override
    public String toString() {
        return
                  "========================================COMPUTADOR========================================" + "\n"
                + "Nome do computador: " + this.nome + "\n"
                + "Código do patrimônio: " + this.codPatrimonio + "\n"
                + "Sistema operacional: " + this.sistemaOperacional + "\n"
                + "CPU: " + this.processador + "\n"
                + "Capacidade máxima da RAM: " + this.maxRam + "\n"
                + "Capacidade máxima do Disco principal: " + this.maxDisco + "\n"
                + "Tempo de atividade: " + this.tempoDeAtividade + "\n"
                + "========================================DEPARTAMENTO========================================" + "\n"
                + "Identificador do departamento: " + this.departamento.getIdDepartamento() + "\n"
                + "Nome do departamento: " + this.departamento.getNome() + "\n"
                + "========================================HOSPITAL========================================" + "\n"
                + "Nome fantasia: " + this.departamento.getHospital().getNomeFantasia() + "\n"
                + "Razão social: " + this.departamento.getHospital().getRazaoSocial() + "\n"
                + "CNPJ:" + this.departamento.getHospital().getCnpj() + "\n"
                + "=============================================================================================";
    }


}
