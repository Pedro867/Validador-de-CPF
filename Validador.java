
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Validador {

    private String cpfStrings[];
    private String cpfString;
    private int[] cpf;

    public Validador() {
        cpfString = "";
        cpf = new int[11];
    }

    public int setCPF(String string) {
        String regex;
        if (string.contains("-")) {// modelo 000.000.000-00
            if (validaTamanhoCPF(string.length() - 3) == 1) {
                return 1;
            } else {
                regex = "[.\\-]";
                this.cpfStrings = string.split(regex);
                // System.out.print("CPF inserido: ");
                // for (String s : this.cpfStrings) {
                //     System.out.print(s);
                // }
                // System.out.println();
            }
        } else {// modelo 00000000000
            if (validaTamanhoCPF(string.length()) == 1) {
                return 1;
            } else {
                this.cpfString = string;
                // System.out.println("CPF inserido: " + this.cpfString);
            }
        }
        return transformaStringEmInt();
    }

    public int transformaStringEmInt() {
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
        return ValidaCPF();
    }

    public int validaTamanhoCPF(int tamanhoDaString) {
        if (tamanhoDaString != 11) {
            // mensagemErro(1);
            return 1;
        }
        return 0;
    }

    public int ValidaCPF() {
        int primeirDigito = primeiroDigitoVerificador();
        int segundoDigito = segundoDigitoVerificador();
        if (primeirDigito != cpf[9] || segundoDigito != cpf[10]) {
            return 2;
            // mensagemErro(2);
        }
        // System.out.println("CPF Válido!");
        return 0;
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
        // System.out.println("Bem vindo ao sistema de validação de CPF!\n");
        // System.out.print("Digite seu CPF: ");
        // Scanner scanner = new Scanner(System.in);
        // String CPF = scanner.nextLine();
        // v.setCPF(CPF);
        Janela janela = new Janela();
        janela.setValidador(v);
    }
}

class Janela extends JFrame {

    JLabel mensagemInicial, mensagemFeedback, labelVazio[];
    JTextField campoCPF;
    JButton validar;
    JPanel mainPanel, textPanel;
    Validador validador;

    public Janela() {
        super("Validador de CPF");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setSize(300, 120);
        labelVazio = new JLabel[2];
        labelVazio[0] = new JLabel();
        labelVazio[1] = new JLabel();
        mensagemInicial = new JLabel("Insira seu CPF");
        mensagemFeedback = new JLabel("");
        campoCPF = new JTextField(10);
        validar = new JButton("Validar CPF");

        mainPanel = (JPanel) this.getContentPane();
        mainPanel.setLayout(new BorderLayout());

        // textPanel = new JPanel(new GridLayout(6,1, 0, 50));
        textPanel = new JPanel(new FlowLayout());
        textPanel.add(mensagemInicial);
        textPanel.add(campoCPF);
        //textPanel.add(labelVazio[0]);
        textPanel.add(validar);
        textPanel.add(mensagemFeedback);
        //textPanel.add(labelVazio[1]);

        mainPanel.add(textPanel, BorderLayout.CENTER);

        ButtonHandler handler = new ButtonHandler();
        validar.addActionListener(handler);

        recarregaMainPanel();
    }

    public void recarregaMainPanel() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void setValidador(Validador v) {
        this.validador = v;
    }

    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == validar) {
                int result = validador.setCPF(campoCPF.getText());
                if (result == 0) {
                    mensagemFeedback.setText("CPF válido!");
                }
                if (result == 1) {
                    mensagemFeedback.setText("Formato inválido!");
                }
                if (result == 2) {
                    mensagemFeedback.setText("CPF inválido!");
                }
                recarregaMainPanel();
            }
        }
    }
}
