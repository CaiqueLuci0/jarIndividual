package repositorio;

import modelo.Computador;
import modelo.Processador;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class ProcessadorRepositorio {

    private final JdbcTemplate conn;

    public ProcessadorRepositorio(JdbcTemplate conn)
    {
        this.conn = conn;
    }

    public List<Processador> buscarProcessadorPorNome(String nome) {
        return conn.query("SELECT * FROM processador WHERE nome = ?", new BeanPropertyRowMapper<>(Processador.class), nome);
    };

    public Processador buscarProcessadorPorId(int id) {
        return conn.queryForObject("SELECT * FROM processador WHERE idProcessador = ?", new BeanPropertyRowMapper<>(Processador.class), id);
    };

    public Processador updateProcessador(Processador processadorNovo) {
        List<Processador> processadores = buscarProcessadorSemId(processadorNovo);

        if (processadores.isEmpty()) {
            return criarProcessador(processadorNovo);
        }
            return processadores.get(0);
    }

    public Processador criarProcessador (Processador processador) {
        conn.execute(
                "INSERT INTO processador (nome, identificador, microarquitetura, frequencia, pacotesFisicos, cpusFisicas, cpusLogicas) VALUES ('"
                + processador.getNome() + "', '"
                + processador.getIdentificador() + "', '"
                + processador.getMicroarquitetura() + "', "
                + processador.getFrequencia() + ", "
                + processador.getPacotesFisicos() + ", "
                + processador.getCpusFisicas() + ", "
                + processador.getCpusLogicas() + ")"
        );

        return buscarProcessadorSemId(processador).get(0);
    }

    public List<Processador> buscarProcessadorSemId (Processador processador) {
        return conn.query("""
                SELECT * FROM processador
                WHERE nome = ?
                AND identificador = ?
                AND microarquitetura = ?
                AND frequencia = ?
                AND pacotesFisicos = ?
                AND cpusFisicas = ?
                AND cpusLogicas = ?
                """, new BeanPropertyRowMapper<>(Processador.class),
                processador.getNome(),
                processador.getIdentificador(),
                processador.getMicroarquitetura(),
                processador.getFrequencia(),
                processador.getPacotesFisicos(),
                processador.getCpusFisicas(),
                processador.getCpusLogicas()
        );
    }

    public void deleteSemUso(int idProcessador) {
        ComputadorRepositorio computadorRepositorio = new ComputadorRepositorio(conn);
        List<Computador> computadores = computadorRepositorio.buscarComputadoresPorFkProcessador(idProcessador);

        if (computadores.isEmpty()) {
            conn.execute("DELETE FROM processador WHERE idProcessador = " + idProcessador);
        }
    }
}
