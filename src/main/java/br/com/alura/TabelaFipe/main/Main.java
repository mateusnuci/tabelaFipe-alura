package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.model.DadosModelos;
import br.com.alura.TabelaFipe.service.ConsumoAPI;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private Scanner input = new Scanner(System.in);

    public void exibeMenu() {
        var menu = """
            *** OPÇÕES ***
            Carro
            Moto
            Caminhão
            
            Digite uma das opções para consulta:
            
            """;

        System.out.println(menu);
        var opcao = input.nextLine();
        String endereco;

        if (opcao.toLowerCase().contains("carr")){
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumo.obterDados(endereco);
        //System.out.println(json);
        var marcas = conversor.obterlista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);


        System.out.println("Informe o código da marca para consulta:");
        var codigoMarca = input.nextLine();

        endereco += "/"+codigoMarca+"/modelos";

        json = consumo.obterDados(endereco);

        var modelosLista = conversor.obterDados(json, DadosModelos.class);
        System.out.println("\nModelos dessa marca: ");
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);



        System.out.println("\nDigite uma parte do nome do carro para ser buscado:");
        var nomeVeiculo = input.nextLine();

        List<Dados> modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .toList();

        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);


        System.out.println("Digite por favor o código do modelo");
        var codigoModelo = input.nextInt();

        endereco += "/"+codigoModelo+"/anos";
        json = consumo.obterDados(endereco);


        List<Dados> anos = conversor.obterlista(json, Dados.class);
        List<Veiculo> veiculos=  new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados (json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veiculos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);
    }
}


//
//        System.out.println("Informe o codigo do modelo para consulta:");
//        var codigoModelo = input.nextInt();
//
//        endereco += "/"+codigoModelo+"/anos";
//        json = consumo.obterDados(endereco);
//
//
//        var modeloAnos = conversor.obterlista(json, Dados.class);
//        modeloAnos.stream()
//                .sorted(Comparator.comparing(Dados::codigo))
//                .forEach(System.out::println);
//
//
//        System.out.println("Informe o ano do modelo para consulta:");
//        var anoModelo = input.nextLine();
//        endereco += "/"+anoModelo;
//
//        json = consumo.obterDados(endereco);
//
//        var modeloDados = conversor.obterDados(json, DadosModeloAno.class);
//        ModeloAno modelo = new ModeloAno(modeloDados);
//
//        System.out.println(modelo.toString());
//
//

