package com.example.oracleapi;
import com.example.oracleapi.PatrimonioDTO;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatrimonioService {
    @Autowired
    private DataSource dataSource;

    public Map<String, Object> inserirPatrimonio(PatrimonioDTO dto) throws SQLException {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call T09E_INSERIR_NOVO_PATRIMONIO(?, ?, ?, ?, ?, ?, ?)}")) {

            stmt.setInt(1, dto.id);
            stmt.setString(2, dto.nomePatrimonio);
            stmt.setString(3, dto.codigoBarras);
            stmt.setString(4, dto.setor);
            stmt.setString(5, dto.marca);

            stmt.setDate(6, new java.sql.Date(dto.dataFabricacao.getTime()));
            stmt.setDate(7, java.sql.Date.valueOf(dto.ultimaManutencao));

            stmt.execute();

            response.put("success", true);
            response.put("message", "Patrimônio inserido com sucesso");
        }

        return response;
    }

    public List<Map<String, Object>> buscarPatrimonio() throws SQLException {
        List<Map<String, Object>> lista = new ArrayList<>();;

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call T09E_GET(?)}")) {
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= colCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    lista.add(row);
                }
            }
            return lista;
        }
    }

    public List<Map<String, Object>> buscarPatrimonioCDBarras(String codigoBarras) throws SQLException {
        List<Map<String, Object>> lista = new ArrayList<>();;

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call T09E_GET_CODIGO_BARRAS(?, ?)}")) {
            stmt.setString(1, codigoBarras);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);

            stmt.execute();

            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= colCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    lista.add(row);
                }
            }
            return lista;
        }
    }


    public Map<String, Object> deletarPatrimonio(String codigoBarras) throws SQLException {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call T09E_DELETAR_REGISTRO(?)}")) {

            stmt.setString(1, codigoBarras);
            stmt.execute();

            response.put("success", true);
            response.put("message", "Patrimônio deletado com sucesso");
        }

        return response;
    }

    public List<Map<String, Object>> updatePatrimonio(int codigoBarras,PatrimonioDTO dto) throws SQLException {
        Map<String, Object> response = new HashMap<>();

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call T09E_ATUALIZAR_ULTIMA_MANUTENCAO(?, ?)}")) {

            stmt.setInt(1, codigoBarras);
            stmt.setDate(2, java.sql.Date.valueOf(dto.ultimaManutencao));

            stmt.execute();
            conn.commit();

            response.put("success", true);
            response.put("message", "Data de última manutenção atualizada com sucesso");
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Erro ao atualizar manutenção: " + e.getMessage());
        }

        return buscarPatrimonioCDBarras(dto.codigoBarras);
    }





}
