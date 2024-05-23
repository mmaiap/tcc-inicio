package com.unifacisa.tcc;

import com.unifacisa.tcc.veiculos.CarroEletrico;
import com.unifacisa.tcc.veiculos.Carro;
import com.unifacisa.tcc.veiculos.Moto;
import com.unifacisa.tcc.cliente.Cliente;
import com.unifacisa.tcc.colaborador.CARGO_FUNCIONARIO;
import com.unifacisa.tcc.colaborador.Funcionario;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class Repository {

    private Connection connection;
    @Value("${database.url}")
    private String url;
    @Value("${database.user}")
    private String user;
    @Value("${database.password}")
    private String password;



    public Repository() throws SQLException {
        this.connection = DriverManager.getConnection(this.url, this.user, this.password);
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Funcionario inserirFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionarios (matricula, cargo_funcionario, nome, " +
                "email, telefone, bonus, salario, data_admissao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, funcionario.getMatricula());
            statement.setString(2, funcionario.getCargoFuncionario().toString());
            statement.setString(3, funcionario.getNome());
            statement.setString(4, funcionario.getEmail());
            statement.setString(5, funcionario.getTelefone());
            statement.setBigDecimal(6, funcionario.getBonus());
            statement.setBigDecimal(7, funcionario.getSalario());
            statement.setDate(8, Date.valueOf(funcionario.getDataAdmissao()));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    funcionario.setMatricula(id);
                    return funcionario;
                } else {
                    throw new SQLException("Falha ao obter o ID do funcionário após a inserção.");
                }
            }
        }
    }

    public Optional<Funcionario> obterFuncionarioPorId(int matricula) throws SQLException {
        String sql = "SELECT * FROM funcionarios WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, matricula);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    CARGO_FUNCIONARIO cargoFuncionario = CARGO_FUNCIONARIO.valueOf(resultSet.getString("cargo_funcionario"));
                    String nome = resultSet.getString("nome");
                    String email = resultSet.getString("email");
                    String telefone = resultSet.getString("telefone");
                    BigDecimal bonus = resultSet.getBigDecimal("bonus");
                    BigDecimal salario = resultSet.getBigDecimal("salario");
                    LocalDate dataAdmissao = resultSet.getDate("data_admissao").toLocalDate();

                    return Optional.of(new Funcionario(matricula, cargoFuncionario, nome, email, telefone, bonus, salario, dataAdmissao));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public void atualizarFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionarios SET cargo_funcionario = ?, nome = ?, email = ?, telefone = ?, bonus = ?, salario = ?, data_admissao = ? WHERE matricula = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, funcionario.getCargoFuncionario().toString());
            statement.setString(2, funcionario.getNome());
            statement.setString(3, funcionario.getEmail());
            statement.setString(4, funcionario.getTelefone());
            statement.setBigDecimal(5, funcionario.getBonus());
            statement.setBigDecimal(6, funcionario.getSalario());
            statement.setDate(7, Date.valueOf(funcionario.getDataAdmissao()));
            statement.setInt(8, funcionario.getMatricula());
            statement.executeUpdate();
        }
    }

    public Optional<Cliente> obterClientePorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String emailCliente = resultSet.getString("email");
                    String telefone = resultSet.getString("telefone");
                    Double saldoEmCompras = resultSet.getDouble("saldoEmCompras");

                    return Optional.of(new Cliente(id, nome, emailCliente, telefone, saldoEmCompras));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public void atualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, email = ?, telefone = ?, saldo_em_compras = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getEmail());
            statement.setString(3, cliente.getTelefone());
            statement.setDouble(4, cliente.getSaldoEmCompras());
            statement.setInt(5, cliente.getId());
            statement.executeUpdate();
        }
    }

    public Cliente inserirCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, email, veiculo, telefone, saldo_em_compras) VALUES (?,?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getEmail());
            statement.setInt(3, cliente.getVeiculo().getId());
            statement.setString(4, cliente.getTelefone());
            statement.setDouble(5, cliente.getSaldoEmCompras());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    cliente.setId(id);
                    return cliente;
                } else {
                    throw new SQLException("Falha ao obter o ID do cliente após a inserção.");
                }
            }
        }
    }

    public Carro inserirCarro(Carro carro) throws SQLException {
        String sql = "INSERT INTO carros (nome, modelo, ano, preco, capacidadeTanque) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, carro.getNome());
            statement.setString(2, carro.getModelo());
            statement.setInt(3, carro.getAno());
            statement.setBigDecimal(4, carro.getPreco());
            statement.setBigDecimal(5, carro.getCapacidadeTanque());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    carro.setId(id);
                    return carro;
                } else {
                    throw new SQLException("Falha ao obter o ID do carro após a inserção.");
                }
            }
        }
    }

    public CarroEletrico inserirCarroEletrico(CarroEletrico carroEletrico) throws SQLException {
        String sql = "INSERT INTO carros_eletricos (nome, modelo, ano, preco) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, carroEletrico.getNome());
            statement.setString(2, carroEletrico.getModelo());
            statement.setInt(3, carroEletrico.getAno());
            statement.setBigDecimal(4, carroEletrico.getPreco());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    carroEletrico.setId(id);
                    return carroEletrico;
                } else {
                    throw new SQLException("Falha ao obter o ID do Carro Eletrico após a inserção.");
                }
            }
        }
    }

    public Moto inserirMoto(Moto moto) throws SQLException {
        String sql = "INSERT INTO motos (nome, modelo, ano, preco, capacidadeTanque) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, moto.getNome());
            statement.setString(2, moto.getModelo());
            statement.setInt(3, moto.getAno());
            statement.setBigDecimal(4, moto.getPreco());
            statement.setBigDecimal(5, moto.getCapacidadeTanque());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    moto.setId(id);
                    return moto;
                } else {
                    throw new SQLException("Falha ao obter o ID da moto após a inserção.");
                }
            }
        }
    }

    public Optional<Carro> obterCarroPorId(int id) throws SQLException {
        String sql = "SELECT * FROM carros WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String modelo = resultSet.getString("modelo");
                    Integer ano = resultSet.getInt("ano");
                    BigDecimal preco = resultSet.getBigDecimal("preco");
                    BigDecimal capacidadeTanque = resultSet.getBigDecimal("capacidadeTanque");

                    return Optional.of(new Carro(id,nome, modelo, ano, preco, capacidadeTanque));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public Optional<CarroEletrico> obterCarroEletricoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM carros_eletricos WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String modelo = resultSet.getString("modelo");
                    Integer ano = resultSet.getInt("ano");
                    BigDecimal preco = resultSet.getBigDecimal("preco");

                    return Optional.of(new CarroEletrico(id,nome, modelo, ano, preco));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public Optional<Moto> obterMotoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM motos WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String modelo = resultSet.getString("modelo");
                    Integer ano = resultSet.getInt("ano");
                    BigDecimal preco = resultSet.getBigDecimal("preco");
                    BigDecimal capacidadeTanque = resultSet.getBigDecimal("capacidadeTanque");

                    return Optional.of(new Moto(id,nome, modelo, ano, preco, capacidadeTanque));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

}
