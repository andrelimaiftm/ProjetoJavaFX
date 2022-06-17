package br.edu.iftm.projetojavafx;

import br.edu.iftm.projetojavafx.util.Alerts;
import br.edu.iftm.projetojavafx.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListaDepartamentoController implements Initializable {

    private DepartamentoService service;

    private ObservableList<Departamento> obsLista;

    @FXML
    private TableView<Departamento> tableViewDepartamento;

    @FXML
    private TableColumn<Departamento, Integer> tableColumnId;

    @FXML
    private TableColumn<Departamento, String> tableColumnNome;

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

    }

    private void criaCaixaDeDialogo(Departamento obj, String nomeDaView, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeDaView));
            Pane pane = loader.load();

            FormDepartamentoController controller = loader.getController();
            controller.setDepartamento(obj);
            controller.setDepartamentoService(new DepartamentoService());
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
}
