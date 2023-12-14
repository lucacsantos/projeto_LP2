package gui.produto;

import application.Main;
import db.DbIntegrityException;
import gui.funcionario.FormularioFuncionarioViewController;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Funcionario;
import model.entities.Produto;
import model.services.ProdutoService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProdutoViewController implements Initializable, DataChangeListener {
    @FXML
    private Button btNovo;
    @FXML
    private TableView<Produto> table;
    @FXML
    private TableColumn<Produto,Integer> id;
    @FXML
    private TableColumn<Produto,String> nome;
    @FXML
    private TableColumn<Produto,String> lote;

    @FXML
    private TableColumn<Produto,Double> preco;
    @FXML
    private TableColumn<Produto,Integer> quantidade;
    @FXML
    private TableColumn<Produto, Produto> editar;
    @FXML
    private TableColumn<Produto, Produto> deletar;

    private ObservableList<Produto> obs;
    private ProdutoService service;

    @FXML
    public void onBtNovoAction(ActionEvent event) {
        Stage stagePai = Utils.currentStage(event);
        Produto obj = new Produto();
        criarFormulario(obj, "/gui/produto/FormularioProdutoView.fxml", stagePai);
    }

    public void setProdutoService(ProdutoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTable();
    }

    private void inicializarTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        lote.setCellValueFactory(new PropertyValueFactory<>("lote"));
        preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        Stage stage = (Stage) Main.retornaCena().getWindow();
        table.prefHeightProperty().bind(stage.heightProperty());
    }

    public void atualizarTable() {
        if (service == null) {
            throw new IllegalStateException("Service está nulo");
        }
        List<Produto> list = service.buscarTodos();
        obs = FXCollections.observableArrayList(list);
        table.setItems(obs);
        inicializarBotoesEditar();
        inicializarBotoesRemover();
    }

    private void criarFormulario(Produto obj, String absoluteNome, Stage stagePai) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteNome));
            Pane pane = loader.load();

            FormularioProdutoViewController controller = loader.getController();
            controller.setProduto(obj);
            controller.setProdutoService(new ProdutoService());
            controller.atualizarDataChangeListener(this);
            controller.atualizarDadosFormulario();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastro de Produtos");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(stagePai);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("IO Exception", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        atualizarTable();
    }

    private void inicializarBotoesEditar() {
        editar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        editar.setCellFactory(param -> new TableCell<Produto, Produto>() {
            private final Button button = new Button("editar");

            @Override
            protected void updateItem(Produto obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> criarFormulario(obj, "/gui/produto/FormularioProdutoView.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void inicializarBotoesRemover() {
        deletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deletar.setCellFactory(param -> new TableCell<Produto, Produto>() {
            private final Button button = new Button("remover");

            @Override
            protected void updateItem(Produto obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removerEntity(obj));
            }
        });
    }

    private void removerEntity(Produto obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Quer realmente deletar?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }
            try {
                service.remover(obj);
                atualizarTable();
            }
            catch (DbIntegrityException e) {
                Alerts.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}