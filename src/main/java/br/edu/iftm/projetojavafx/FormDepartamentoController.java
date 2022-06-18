package br.edu.iftm.projetojavafx;

import br.edu.iftm.projetojavafx.Exception.ValidacaoException;
import br.edu.iftm.projetojavafx.util.Alerts;
import br.edu.iftm.projetojavafx.util.RetricoesTela;
import br.edu.iftm.projetojavafx.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class FormDepartamentoController  implements Initializable {

    private DepartamentoService service;

    private Departamento departamento;

    private List<AtualizaDadoListener> atualizaDadoListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private Label labelNomeErro;

    @FXML
    private Button btSalvar;

    @FXML
    private Button btCancelar;

    public void setDepartamento(Departamento departamento){
        this.departamento = departamento;
    }

    public void setDepartamentoService(DepartamentoService service){
        this.service = service;
    }

    @FXML
    public void onBtSalvarAction(ActionEvent event){
        //System.out.println("onBtSalvar");
        if(departamento == null){
            throw new IllegalStateException("Departamento está vazio");
        }
        try {
            departamento = getDadosFormulario();
            service.SaveOrUpdate(departamento);
            notificaAtualizaDadoListerners();
            Utils.stageCorrente(event).close();
        }catch (ValidacaoException e){
            setMensagemErros(e.getErros());
        }

    }

    @FXML
    public void onBtCancelarAction(ActionEvent event){
        Utils.stageCorrente(event).close();
    }

    public void adicionaAtualizaDadosListener(AtualizaDadoListener listener){
        atualizaDadoListeners.add(listener);
    }

    private void notificaAtualizaDadoListerners() {
        for (AtualizaDadoListener listener: atualizaDadoListeners) {
            try {
                listener.onMudancaDados();
            } catch (IllegalAccessException e) {
                Alerts.showAlert("IllegalAccessException", "Erro ao atualizar dados",
                        e.getMessage(),
                        Alert.AlertType.ERROR);
            }
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();

    }

    private void initializeNodes(){
        RetricoesTela.setTextFieldInteger(txtId);
        RetricoesTela.setTextFieldMaxLength(txtNome, 30);
    }

    public void atualizaDadosFormulario(){
        if(departamento == null){
            throw new IllegalStateException("Departamento está vazio");
        }
        txtId.setText(departamento.getId() == null ? "" : String.valueOf(departamento.getId()));
        txtNome.setText(departamento.getNome());
    }

    private Departamento getDadosFormulario() {
        Departamento obj = new Departamento();

        ValidacaoException exception = new ValidacaoException("Erro de validação");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if(txtNome.getText() == null || txtNome.getText().trim().equals("")){
            exception.addErro("nome", "Campo não pode ser vazio");
        }
        obj.setNome(txtNome.getText());

        if (exception.getErros().size() > 0){
            throw exception;
        }
        return obj;
    }

    private void setMensagemErros(Map<String, String> erros) {
        Set<String> campos = erros.keySet();

        if(campos.contains("nome")){
            labelNomeErro.setText(erros.get("nome"));
        }
    }
}
