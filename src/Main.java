import ambiente.Ambiente;
import ambiente.Pessoa;
import ambiente.Porta;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<Porta> portaList = new ArrayList<>();


    public static void main(String[] args) {
        var ambiente = new Ambiente(2, 5, 4, 5);

        try {
            ambiente.criarAmbiente();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}