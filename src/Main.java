import ambiente.Ambiente;
import ambiente.Pessoa;
import ambiente.Porta;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        var ambiente = new Ambiente(15, 1, 3, 5);

        try {
            ambiente.criarAmbiente();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}