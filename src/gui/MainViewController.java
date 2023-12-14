package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.cliente.ClienteViewController;
import gui.fornecedor.FornecedorViewController;
import gui.funcionario.FuncionarioViewController;
import gui.produto.ProdutoViewController;
import gui.vendas.VendaViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.*;


public class MainViewController implements Initializable {
    @FXML
    private MenuItem menuCliente;

    @FXML
    private MenuItem menuSobre;

    public void onMenuClienteAction(){
        chamarTela("/gui/cliente/ClienteView.fxml",(ClienteViewController controller)->{
            controller.setClienteService(new ClienteService());
            controller.atualizarTable();
        });
    }
    public void onMenuFornecedorAction(){
        chamarTela("/gui/fornecedor/FornecedorView.fxml",(FornecedorViewController controller)->{
            controller.setFornecedorService(new FornecedorService());
            controller.atualizarTable();
        });
    }
    public void onMenuFuncionarioAction(){
        chamarTela("/gui/funcionario/FuncionarioView.fxml",(FuncionarioViewController controller)->{
            controller.setFuncionarioService(new FuncionarioService());
            controller.atualizarTable();
        });
    }
    public void onMenuProdutoAction(){
         chamarTela("/gui/produto/ProdutoView.fxml",(ProdutoViewController controller)->{
             controller.setProdutoService(new ProdutoService());
             controller.atualizarTable();
         });
     }
    public void onMenuVendaAction(){
       chamarTela("/gui/vendas/VendaView.fxml",(VendaViewController controller)->{
           controller.setVendaService(new VendaService());
           controller.atualizarTable();
       });
   }
    public void onMenuSobreAction(){
        chamarTela("/gui/AjudaView.fxml",x->{});
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public synchronized <T> void chamarTela(String caminho, Consumer<T> consumer){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            VBox novaVBox = loader.load();
            Scene mainScene = Main.retornaCena();

            VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(novaVBox.getChildren());

            T controller = loader.getController();
            consumer.accept(controller);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
