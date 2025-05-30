package com.example.oracleapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://127.0.0.1:5501")
@RestController
@RequestMapping("/api")
public class PatrimonioController {

    @Autowired
    private PatrimonioService patrimonioService;

    @PostMapping("/patrimonios")
    public Map<String, Object> inserir(@RequestBody PatrimonioDTO dto) throws SQLException {
        return patrimonioService.inserirPatrimonio(dto);
    }

    @GetMapping("/patrimonios")
    public List<Map<String, Object>> listar() throws SQLException {
        return patrimonioService.buscarPatrimonio();
    }

    @GetMapping("/patrimonios/{codigoBarras}")
    public List<Map<String, Object>> listarCodigoBarras(@PathVariable String codigoBarras) throws SQLException {
        return patrimonioService.buscarPatrimonioCDBarras(codigoBarras);
    }

    @DeleteMapping("/patrimonios/{codigoBarras}")
    public Map<String, Object> deletar(@PathVariable String codigoBarras) throws SQLException {
        return patrimonioService.deletarPatrimonio(codigoBarras);
    }


    @PutMapping("/patrimonios/{codigoBarras}")
    public List<Map<String, Object>> atualizar(@PathVariable int codigoBarras, @RequestBody PatrimonioDTO dto) throws SQLException {
        return patrimonioService.updatePatrimonio(codigoBarras, dto);
    }

}
