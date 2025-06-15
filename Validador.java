import java.util.Scanner;

public class Validador {

    private String cpfStrings[];
    private String cpfString;
    private int[] cpf;

    public Validador() {
        cpfString = "";
        cpf = new int[11];
    }

    public void setCPF(String string) {
        String regex;
        if (string.contains("-")) {// modelo 000.000.000-00
            validaTamanhoCPF(string.length() - 3);
            regex = "[.\\-]";
            this.cpfStrings = string.split(regex);
            // System.out.print("CPF inserido: ");
            // for (String s : this.cpfStrings) {
            //     System.out.print(s);
            // }
            // System.out.println();
        } else {// modelo 00000000000
            validaTamanhoCPF(string.length());
            this.cpfString = string;
            // System.out.println("CPF inserido: " + this.cpfString);
        }
        transformaStringEmInt();
    }

    public void transformaStringEmInt() {
        char[] auxChar;
        String auxString = "";
        int contador = 0;
        try {
            if (cpfString.equals("")) {// formato 000.000.000-00
                for (int i = 0; i < cpfStrings.length; i++) {
                    for (int j = 0; j < cpfStrings[i].length(); j++) {
                        auxChar = cpfStrings[i].toCharArray();
                        cpf[contador] = Integer.parseInt(String.valueOf(auxChar[j]));
                        contador++;
                    }
                }
            } else {
                for (int i = 0; i < cpfString.length(); i++) {
                    auxString = String.valueOf(cpfString.toCharArray()[i]);
                    cpf[i] = Integer.parseInt(auxString);
                }
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        // for (int i : cpf) {
        //     System.out.print(i);
        // }
        ValidaCPF();
    }

    public void validaTamanhoCPF(int tamanhoDaString) {
        if (tamanhoDaString != 11) {
            mensagemErro(1);
        }
    }

    public void ValidaCPF() {
        int primeirDigito = primeiroDigitoVerificador();
        int segundoDigito = segundoDigitoVerificador();
        if (primeirDigito != cpf[9] || segundoDigito != cpf[10]){
            mensagemErro(2);
        }
        System.out.println("CPF Válido!");
    }

    public int primeiroDigitoVerificador() {
        int cont = 0;
        int[] auxVetor = new int[9];
        for (int i = 8; i >= 0; i--) {
            auxVetor[cont] = cpf[cont] * (i + 2);
            cont++;
        }
        int soma = 0;
        for (int i : auxVetor) {
            soma += i;
        }
        if (soma % 11 < 2) {
            return 0;
        }
        return (11 - (soma % 11));
    }

    public int segundoDigitoVerificador() {
        int cont = 0;
        int[] auxVetor = new int[10];
        for (int i = 9; i >= 0; i--) {
            auxVetor[cont] = cpf[cont] * (i + 2);
            cont++;
        }
        int soma = 0;
        for (int i : auxVetor) {
            soma += i;
        }
        if (soma % 11 < 2) {
            return 0;
        }
        return (11 - (soma % 11));
    }

    public static void mensagemErro(int tipoDeErro) {
        if (tipoDeErro == 1) {
            System.out.println("Formato ou tamanho do CPF inválido!");
            System.exit(0);
        }
        if (tipoDeErro == 2) {
            System.out.println("CPF inválido!");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Validador v = new Validador();
        // v.setCPF("111.222.333-44");
        // v.setCPF("17440219619");
        System.out.println("Bem vindo ao sistema de validação de CPF!\n");
        System.out.print("Digite seu CPF: ");
        Scanner scanner = new Scanner(System.in);
        String CPF = scanner.nextLine();
        v.setCPF(CPF);
    }
}