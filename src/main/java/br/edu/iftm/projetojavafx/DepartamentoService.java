package br.edu.iftm.projetojavafx;

import java.util.ArrayList;
import java.util.List;

public class DepartamentoService {

    private static List<Departamento> lista = new ArrayList<>();

    public List<Departamento> buscarTodos(){
        if(lista.isEmpty()){
            lista.add(new Departamento(1, "Livros"));
            lista.add(new Departamento(2, "Computadores"));
            lista.add(new Departamento(3, "Eletrônicos"));
        }
        return lista;
    }

    public void SaveOrUpdate(Departamento obj){
        Integer id = obj.getId();
        if(id == null){
            id = lista.size()+1;
            obj.setId(id);
            lista.add(obj);
        }else{
            Integer posicao = obj.getId()-1;
            lista.add(posicao, obj);
        }
    }

}
