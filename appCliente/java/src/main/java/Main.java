import Persistencia.Conexao;
import Persistencia.ConexaoSQL;
import Registro.Leitura;
import Registro.LeituraComputador;
import log.Log;
import log.LogLevel;
import log.LogManager;
import modelo.LogJar;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import repositorio.ComputadorRepositorio;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import modelo.Computador;


public class Main {


    static Conexao conexao = new Conexao();
    static JdbcTemplate conn = conexao.getConn();

    static ConexaoSQL conexaoSQL = new ConexaoSQL();
    static JdbcTemplate connSQL = conexaoSQL.getConn();

    public static void main(String[] args) throws InterruptedException, IOException {
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

        try{
            login();
        } catch(Exception e) {
            try {
                System.out.println("Erro no sistema. Entre em contato com o nosso suporte!");
                LogJar lastLog = conn.queryForObject("SELECT * FROM logJar WHERE dtLog = (SELECT MAX(dtLog) FROM logJar);", new BeanPropertyRowMapper<>(LogJar.class));
                if (lastLog != null && lastLog.getMensagem().equals(e.getMessage())) {
                    return;
                }
                System.out.println("Cadastrando log...");
                conn.execute("INSERT INTO logJar (causa, mensagem, stacktrace, dtLog) VALUES (" +
                        "'" + e.getCause() + "', " +
                        "'" + e.getMessage() + "', " +
                        "'" + e + "', " +
                        "'" + Log.formato.format(new Date()) + "');"
                );
            } catch(Exception e2) {
                System.out.println("Erro no cadastro do log no banco de dados. Inserindo em arquivo .txt");
                LogManager.salvarLog(new Log(Exception.class, "Foi requisitada a inserção da Exception: \n\n"
                        + e + "\n\nNo entanto, a mesma gerou outra Exception durante a tentativa de inserção," +
                        " sendo ela: \n\n" + e2, LogLevel.ERROR));
            }
        }
    }


    static void login() throws InterruptedException, IOException {
        System.out.println("Login iniciado! \n");

        ComputadorRepositorio repositorioComputador = new ComputadorRepositorio(conn, connSQL);

        List computadorAutenticado;
        do {
            System.out.println(System.getenv("CODIGO_PATRIMONIO"));
            String codPatrimonio = System.getenv("CODIGO_PATRIMONIO");
            System.out.println("Senha:");
            String senhaH = System.getenv("SENHA_PC");


            computadorAutenticado = repositorioComputador.autenticarComputador(senhaH, codPatrimonio);

            if (computadorAutenticado.isEmpty()) {
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
        System.out.println(computador);

        Computador computadorLocal = new Computador();
        Leitura leituraLocal = new LeituraComputador(computadorLocal);

        System.out.println("\nAGORA ESTE COMPUTADOR ESTÁ SENDO MONITORADO EM TEMPO REAL.");

        LeituraComputador leitura = new LeituraComputador(computador);

        leitura.inserirLeitura();
    } 
}
