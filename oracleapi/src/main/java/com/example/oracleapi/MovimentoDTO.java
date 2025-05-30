package com.example.oracleapi;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MovimentoDTO {
    public int id;
    public String nomeColaborador;
    public String nomeProduto;
    public String codigoBarras;
    public int idColaborador;
    public int idProduto;
    public LocalDate data;
    public LocalDateTime horarioEntrada;
    public LocalDateTime horarioSaida;
    public Integer quantidadeEntrada;
    public Integer quantidadeSaida;
}
