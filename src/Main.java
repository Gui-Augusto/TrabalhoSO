import ambiente.Ambiente;
public class Main {
    public static void main(String[] args) {
        var ambiente = new Ambiente(2, 1, 4, 10);
        try {
            ambiente.criarAmbiente();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}