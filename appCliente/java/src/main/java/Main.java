import Persistencia.Conexao;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.util.Conversor;
import modelo.Processador;
import org.springframework.jdbc.core.JdbcTemplate;
import repositorio.ComputadorRepositorio;
import java.time.LocalDateTime;
import java.util.*;

import modelo.Computador;
import repositorio.ProcessadorRepositorio;

public class Main {
    static Scanner leitorStr = new Scanner(System.in);
    static Conexao conexao = new Conexao();
    static JdbcTemplate conn = conexao.getConn();
    static ComputadorRepositorio repositorioComputador = new ComputadorRepositorio(conn);


    public static void main(String[] args) throws InterruptedException {

        telaInicial();
    }

    static void telaInicial() throws InterruptedException {

        System.out.println("""
                         
                         :JJJ~        !JJ?.      !JJJJJJJJJ?:     :JJJJJJJJJ7~:      !JJJJJJJJJJJ~    .?JJJJJJJJJ7.      .^7?JJJJJ?!:     .?J!       ^JJ:            \s
                         :YYYY~     .7YYYJ.      75J:.......      :YY!....:^7YY7.    ....:J57.....    :J57........      ~JY?~:..:^!?~     .J5!       ^YY:            \s
                         :YY!?Y!   .757!YJ.      75?:......       :YY!       :J57.       .J5!         :JY!.......      ~YY~               .JY7.......~YY:            \s
                         :YY^.J5! .?57 ~YJ.      7YYJJJJJJJ^      :YY!        !YJ:       .J5!         :JYYJJJJJJ?.     75?.               .JYYJJJJJJJJYY:            \s
                         :YY^ .?Y?JY!  !YJ.      75?:......       :YY!       :J57.       .J5!         :JY!.......      ~YY~               .JY7:::::::~YY:            \s
                         :YY^  .?YY!   !YJ.      75?:........     :YY!....:^!YY7.        .J5!         :J5!........      ~JY?~:...^!?^     .J5!       ^YY:            \s
                         :JJ^   .::    ~Y?.      !YJJJJJJJJJ~     :JYJJJJJJJ?~:          .?Y!         :?YJJJJJJJJ?:      .~7JJJJJJ?~:     .?Y!       ^YY:            \s
                """);
        System.out.println("""
                BEM-VINDO(A) AO NOSSO SISTEMA DE MONITORAMENTO DE COMPUTADORES!
                """);
        login();
    }


    static void login() throws InterruptedException {
        System.out.println("Login iniciado! \n");

        List computadorAutenticado;
        do {
            System.out.println("Código do patrimônio:");
            String codPatrimonio = leitorStr.next();
            System.out.println("Senha:");
            String senhaH = leitorStr.next();

            computadorAutenticado = repositorioComputador.autenticarComputador(senhaH, codPatrimonio);

            if (computadorAutenticado.size() != 1) {
                System.out.println("Código do patrimônio ou senha incorreta. \nPor favor, tente novamente. \n");
            }

        } while (computadorAutenticado.size() != 1);

        System.out.println("""
                \n
                Login realizado com sucesso!
                Estes são os dados da conta acessada:
                \n
                """);

        Computador computador = (Computador) computadorAutenticado.get(0);
        atualizarInformacoes(computador);
        System.out.println(computador);

        System.out.println("\nAGORA ESTE COMPUTADOR ESTÁ SENDO MONITORADO EM TEMPO REAL.");

        inserirLeituras(computador);
    }

    public static void inserirLeituras(Computador computador) throws InterruptedException {

        for (int i = 1; true; i++) {

            String queryRamCpu = "INSERT INTO leituraRamCpu (ram, cpu, dataLeitura, fkComputador, fkDepartamento, fkHospital) VALUES("
                    + computador.getPorcentagemConsumoMemoria()
                    + ", " + computador.getPorcentagemConsumoCpu()
                    + ", '" + LocalDateTime.now() + "', " + computador.getIdComputador()
                    + ", " + computador.getFkDepartamento() + ", "
                    + computador.getFkHospital() + ");";

            System.out.printf("""
                    COMANDO DE INSERÇÃO DE LEITURAS DE RAM E CPU:
                    %s \n
                    """, queryRamCpu);
            conn.execute(queryRamCpu);

            for (Janela janela : computador.getJanelas()) {
                String queryFerramenta =
                        "INSERT INTO leituraFerramenta (nomeApp, dtLeitura, caminho, fkComputador, fkDepartamento, fkHospital) VALUES( '"
                                + janela.getTitulo() + "', '"
                                + LocalDateTime.now() + "', '"
                                + janela.getComando() + "', "
                                + computador.getIdComputador() + ", "
                                + computador.getFkDepartamento() + ", "
                                + computador.getFkHospital() + ");";

                System.out.printf("""
                        COMANDO DE INSERÇÃO DE LEITURAS DE FERRAMENTAS EM USO: \n
                        %s \n
                        """, queryFerramenta);
                conn.execute(queryFerramenta);
            }

            if (i > 9) {
                String queryDisco = "INSERT INTO leituraDisco (disco, dataLeitura, fkComputador, fkDepartamento, fkHospital) VALUES ("
                        + computador.getDiscoComMaisConsumo(computador.getPorcentagemDeTodosVolumes())
                        + ", '" + LocalDateTime.now() + "', " + computador.getIdComputador()
                        + ", " + computador.getFkDepartamento() + ", " + computador.getFkDepartamento() + ");";

                System.out.printf("""
                        COMANDO DE INSERÇÃO DE LEITURAS DE FERRAMENTAS EM USO: \n
                        %s \n
                        """, queryDisco);
                conn.execute(queryDisco);

                i = 0;
            }

            Thread.sleep(3000);
        }
    }

    public static void atualizarInformacoes (Computador computador) {
        ProcessadorRepositorio processadorRepositorio = new ProcessadorRepositorio(conn);

        Processador processador = new Processador();
        processador.setFrequencia(computador.loocaProcessador.getFrequencia());
        processador.setNome(computador.loocaProcessador.getNome());
        processador.setCpusFisicas(computador.loocaProcessador.getNumeroCpusFisicas());
        processador.setIdentificador(computador.loocaProcessador.getIdentificador());
        processador.setCpusLogicas(computador.loocaProcessador.getNumeroCpusLogicas());
        processador.setMicroarquitetura(computador.loocaProcessador.getMicroarquitetura());
        processador.setPacotesFisicos(computador.loocaProcessador.getNumeroPacotesFisicos());

        if (
                computador.getProcessador().getFrequencia() != processador.getFrequencia()                  ||
                computador.getProcessador().getNome().equals(processador.getNome())                         ||
                computador.getProcessador().getCpusFisicas() != processador.getCpusFisicas()                ||
                computador.getProcessador().getIdentificador().equals(processador.getNome())                ||
                computador.getProcessador().getCpusLogicas() != processador.getCpusLogicas()                ||
                computador.getProcessador().getMicroarquitetura().equals(processador.getMicroarquitetura()) ||
                computador.getProcessador().getPacotesFisicos() != processador.getPacotesFisicos()
        ) {
            Processador processadorAntigo = computador.getProcessador();
            //UPDATE PROCESSADOR
            computador.setProcessador(processadorRepositorio.updateProcessador(processador));
            repositorioComputador.updateProcessador(computador);
            //DELETAR ANTIGO PROCESSADOR NO BANCO SE NÃO ESTIVER EM USO
            if (processadorAntigo.getIdProcessador() != 0) {
                processadorRepositorio.deleteSemUso(processadorAntigo.getIdProcessador());
            }
        }

        if (
                !computador.getMaxDisco().equals(computador.getMaiorDisco())                           ||
                !Conversor.formatarBytes(computador.memoria.getTotal()).equals(computador.getMaxRam()) ||
                !computador.getSistemaOperacional().equals(computador.sistema.getSistemaOperacional())
        ) {
            computador.setMaxRam(Conversor.formatarBytes(computador.memoria.getTotal()));
            computador.setSistemaOperacional(computador.sistema.getSistemaOperacional());
            computador.setMaxDisco(computador.getMaiorDisco());
            repositorioComputador.uptadeHardware(computador);
        }

        if (!computador.getTempoDeAtividade().equals(Conversor.formatarSegundosDecorridos(computador.sistema.getTempoDeAtividade()))) {
            computador.setTempoDeAtividade(Conversor.formatarSegundosDecorridos(computador.sistema.getTempoDeAtividade()));
            repositorioComputador.updateTempoAtividade(computador);
        }
    }
}