import ambiente.Ambiente;
public class Main {
    public static void main(String[] args) {
        var ambiente = new Ambiente(2, 4, 4, 5);
        try {
            ambiente.criarAmbiente();
        }catch (Exception e){

        }
    }
}