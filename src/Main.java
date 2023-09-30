import main.java.com.crud.application.Aplicacao;
import main.java.com.crud.dao.RegistroDAO;
import main.java.com.crud.model.Registro;

public class Main {
    public static void main(String[] args) {
        RegistroDAO dao = new Registro();
        Aplicacao aplicacao = new Aplicacao(dao);
        aplicacao.mostrarMenu();

        while (true) {
            int opcao = aplicacao.lerOpcao();
            aplicacao.executarOpcao(opcao);
            aplicacao.mostrarMenu();
        }
    }
}
