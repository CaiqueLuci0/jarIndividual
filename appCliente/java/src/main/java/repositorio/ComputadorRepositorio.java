package repositorio;

import com.github.britooo.looca.api.util.Conversor;
import modelo.Computador;

import java.util.List;

import modelo.Processador;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class ComputadorRepositorio {

    private final JdbcTemplate conn;

    public ComputadorRepositorio(JdbcTemplate conn) {
        this.conn = conn;
    }

    public List<Computador> autenticarComputador(String senha, String codPatrimonio) {
        DepartamentoRepositorio departamentoRepositorio = new DepartamentoRepositorio(this.conn);
        ProcessadorRepositorio processadorRepositorio = new ProcessadorRepositorio(this.conn);
        List<Computador> computadorEncontrado = conn.query("""
                SELECT
                c.idComputador,
                c.nome,
                c.codPatrimonio,
                c.gbRam as maxRam,
                c.gbDisco as maxDisco,
                C.sistemaOperacional,
                c.tempoDeAtividade,
                c.fkProcessador,
                c.fkDepartamento,
                c.fkHospital
                FROM computador c
                WHERE senha = ?
                AND codPatrimonio = ?;
                """, new BeanPropertyRowMapper<>(Computador.class), senha, codPatrimonio);

        computadorEncontrado.get(0).setDepartamento(departamentoRepositorio.buscarDepartamentoPorId(computadorEncontrado.get(0).getFkDepartamento()));
        computadorEncontrado.get(0).setProcessador(
                (
                        computadorEncontrado.get(0).getFkProcessador() != 0 ?
                        processadorRepositorio.buscarProcessadorPorId(computadorEncontrado.get(0).getFkProcessador())
                        : new Processador()
                ));
        return computadorEncontrado;
    }

    public List<Computador> buscarComputadoresPorFkProcessador(int idProcessador) {
        return conn.query("SELECT * FROM computador WHERE fkProcessador = ?", new BeanPropertyRowMapper<>(Computador.class), idProcessador);
    }

    public void updateProcessador(Computador computador) {
        conn.execute("UPDATE computador SET fkProcessador = "
                + computador.getProcessador().getIdProcessador()
                + " WHERE idComputador = "
                + computador.getIdComputador());
    }

    public void uptadeHardware(Computador computador) {
        conn.execute("UPDATE computador SET gbRam = '"
        + computador.getMaxRam()
        + "', gbDisco = '"
        + computador.getMaxDisco()
        + "', sistemaOperacional ='"
        + computador.getSistemaOperacional()
        + "' WHERE idComputador = " + computador.getIdComputador());
    }

    public void updateTempoAtividade(Computador computador) {
        conn.execute("UPDATE computador SET tempoDeAtividade = '" + computador.getTempoDeAtividade() + "' WHERE idComputador = " + computador.getIdComputador());
    }
}
