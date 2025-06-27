
package Models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private Connection conexao;

    public Conexao() {
                
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //load driver  
            conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/banco?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "nova_senha");
            // conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/banco?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "Daniel03#");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Nao foi possivel efetuar uma conexao com o BD!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Nao foi possivel encontrar a classe referente! Verifique o driver!");
        }
    }

    public Connection getConexao() {
        return this.conexao;
    }

    public void closeConexao() {
        try {
            this.conexao.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
