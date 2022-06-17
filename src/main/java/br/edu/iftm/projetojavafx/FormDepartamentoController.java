package br.edu.iftm.projetojavafx;

import br.edu.iftm.projetojavafx.util.RetricoesTela;
import br.edu.iftm.projetojavafx.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FormDepartamentoController  implements Initializable {

    private DepartamentoService service;

    private Departamento departamento;

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
        departamento = getDadosFormulario();
        service.SaveOrUpdate(departamento);
        Utils.stageCorrente(event).close();
    }

    @FXML
    public void onBtCancelarAction(ActionEvent event){
        Utils.stageCorrente(event).close();
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

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setNome(txtNome.getText());
        return obj;
    }
}
