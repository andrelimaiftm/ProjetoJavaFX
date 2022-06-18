package br.edu.iftm.projetojavafx;

import br.edu.iftm.projetojavafx.util.Alerts;
import br.edu.iftm.projetojavafx.util.Utils;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListaDepartamentoController implements Initializable, AtualizaDadoListener {

    private DepartamentoService service;

    private ObservableList<Departamento> obsLista;

    @FXML
    private TableView<Departamento> tableViewDepartamento;

    @FXML
    private TableColumn<Departamento, Integer> tableColumnId;

    @FXML
    private TableColumn<Departamento, String> tableColumnNome;

    @FXML
    private TableColumn<Departamento, Departamento> tableColumnEdit;

    @FXML
    private TableColumn<Departamento, Departamento> tableColumnRemove;

    @FXML
    private Button btNovo;

    @FXML
    public void onBtNovoAction(ActionEvent event){
        //System.out.println("onBtNovoAction");
        Stage parentStage = Utils.stageCorrente(event);
        Departamento obj = new Departamento();
        criaCaixaDeDialogo(obj, "formDepartamento.fxml", parentStage);

    }

    public void setDepartamentoServico(DepartamentoService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicalizaNodes();
    }

    private void inicalizaNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        Stage stage = (Stage) Main.getScene().getWindow();
        tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView() throws IllegalAccessException {
        if(service == null){
            throw new IllegalAccessException("Servico está nulo");
        }
        List<Departamento> lista = service.buscarTodos();
        obsLista = FXCollections.observableList(lista);
        tableViewDepartamento.setItems(obsLista);
        inicializaBotaoEditar();
        inicializaBotaoRemover();

    }

    private void criaCaixaDeDialogo(Departamento obj, String nomeDaView, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeDaView));
            Pane pane = loader.load();

            FormDepartamentoController controller = loader.getController();
            controller.setDepartamento(obj);
            controller.setDepartamentoService(new DepartamentoService());
            controller.adicionaAtualizaDadosListener(this);
            controller.atualizaDadosFormulario();

            Stage caixaDialogoStage = new Stage();
            caixaDialogoStage.setTitle("Digite dados para Departamento");
            caixaDialogoStage.setScene(new Scene(pane));
            caixaDialogoStage.setResizable(false);
            caixaDialogoStage.initOwner(parentStage);
            caixaDialogoStage.initModality(Modality.WINDOW_MODAL);
            caixaDialogoStage.showAndWait();

        }catch (IOException e){
            Alerts.showAlert("IOExpetion", "Erro ao carregar a view",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }

    }

    @Override
    public void onMudancaDados() throws IllegalAccessException {
        updateTableView();
    }

    private void inicializaBotaoEditar() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
            private final Button button = new Button("editar");
            @Override
            protected void updateItem(Departamento obj, boolean vazio) {
                super.updateItem(obj, vazio);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> criaCaixaDeDialogo(
                                obj, "formDepartamento.fxml",Utils.stageCorrente(event)));
            }
        });
    }

    private void inicializaBotaoRemover() {
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
            private final Button button = new Button("remover");
            @Override
            protected void updateItem(Departamento obj, boolean vazio) {
                super.updateItem(obj, vazio);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> {
                    try {
                        removeDepartamento(obj);
                    } catch (IllegalAccessException e) {
                        Alerts.showAlert("IllegalAccessException", "Erro ao remover departamento.",
                                e.getMessage(),
                                Alert.AlertType.ERROR);
                    }
                });
            }
        });
    }

    private void removeDepartamento(Departamento obj) throws IllegalAccessException {
        Optional<ButtonType> result = Alerts.mostrarConfirmacao("Confirmação",
                "Você tem certeza que deseja remover o Departamento?");

        if(result.get() == ButtonType.OK){
            if(service == null){
                throw new IllegalStateException("Servico está nulo.");
            }

            service.remover(obj);
            updateTableView();
        }
    }
}
