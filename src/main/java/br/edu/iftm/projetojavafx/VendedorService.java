package br.edu.iftm.projetojavafx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VendedorService {

    private static List<Vendedor> lista = new ArrayList<>();
    private DepartamentoService service = new DepartamentoService();

    public List<Vendedor> buscaTodos(){
        if(lista.isEmpty()){

            List<Departamento> listaDepart = service.buscarTodos();

            lista.add(new Vendedor(1,
                    "Andre",
                    "andre@gmail.com",
                    new Date(86, Calendar.APRIL, 27),
                    2500.00,
                    listaDepart.get(0)));

            lista.add(new Vendedor(2,
                    "Renata",
                    "renata@gmail.com",
                    new Date(86, Calendar.NOVEMBER, 28),
                    3500.00,
                    listaDepart.get(1)));

            lista.add(new Vendedor(3,
                    "Jose",
                    "jose@gmail.com",
                    new Date(99, Calendar.JUNE, 03),
                    1500.00,
                    listaDepart.get(2)));
        }
        return lista;
    }

    public void SaveOrUpdate(Vendedor obj){
        Integer id = obj.getId();
        if(id == null){
            id = lista.size()+1;
            obj.setId(id);
            lista.add(obj);
        }else{
            Integer posicao = obj.getId()-1;
            lista.set(posicao, obj);
        }
    }

    public void remover(Vendedor obj){
        lista.remove(obj);
    }

}
