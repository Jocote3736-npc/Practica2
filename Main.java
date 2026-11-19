import java.util.Scanner;

public class Main {
    Scanner reader = new Scanner(System.in);
    private ArbolBMas arbol;

    public void pausa() {
        System.out.println("\n Presione Enter para continuar...");
        new java.util.Scanner(System.in).nextLine();
    }

    public void limpiaConsola() {
        try {
            String sistemOperativo = System.getProperty("os.name");
            if (sistemOperativo.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void bInsertar() {
        System.out.print("Digite la clave (entero): ");
        int claveInsertar = reader.nextInt();
        reader.nextLine();
        System.out.print("Digite el dato asociado: ");
        String datoInsertar = reader.nextLine();
        arbol.insertar(claveInsertar, datoInsertar);
        System.out.println("\n    Elemento insertado correctamente.");
    }

    private void bBuscar() {
        System.out.print("Digite la clave a buscar: ");
        int claveBuscar = reader.nextInt();
        String datoEncontrado = arbol.buscarDato(claveBuscar);
        if (datoEncontrado != null) {
            System.out.println("\n    Clave encontrada -> Dato: " + datoEncontrado);
        } else {
            System.out.println("\n    La clave " + claveBuscar + " no existe en el árbol.");
        }
    }

    private void bEliminar() {
        System.out.print("Digite la clave a eliminar: ");
        int claveEliminar = reader.nextInt();
        boolean eliminado = arbol.eliminar(claveEliminar);
        if (eliminado) {
            System.out.println("\n    Clave " + claveEliminar + " eliminada y árbol rebalanceado.");
        } else {
            System.out.println("\n    No se encontró la clave " + claveEliminar + ".");
        }
    }

    private void bRecorrerRango() {
        System.out.print("Digite la clave inicial: ");
        int claveInicio = reader.nextInt();
        System.out.print("Digite la cantidad de elementos a mostrar (n): ");
        int cantidad = reader.nextInt();
        System.out.println("\n    --- Resultados del recorrido por rango ---");
        arbol.recorrerRango(claveInicio, cantidad);
    }

    private void bImprimirArbol() {
        System.out.println("\n    --- Estructura actual del árbol B+ ---");
        arbol.imprimirArbol();
    }

    public void menu() {
        int opcion = -1;
        while (opcion != 0) {
            limpiaConsola();
            System.out.println("\n****** OPERACIONES DEL ARBOL B+ ******\n");
            System.out.println("    1. Insertar");
            System.out.println("    2. Buscar");
            System.out.println("    3. Eliminar");
            System.out.println("    4. Recorrer por rango");
            System.out.println("    5. Imprimir árbol");
            System.out.println("    0. Salir del programa\n");
            System.out.print("    Digite la opción: ");
            opcion = reader.nextInt();
            reader.nextLine();

            switch (opcion) {
                case 1:
                    limpiaConsola();
                    bInsertar();
                    pausa();
                    break;
                case 2:
                    limpiaConsola();
                    bBuscar();
                    pausa();
                    break;
                case 3:
                    limpiaConsola();
                    bEliminar();
                    pausa();
                    break;
                case 4:
                    limpiaConsola();
                    bRecorrerRango();
                    pausa();
                    break;
                case 5:
                    limpiaConsola();
                    bImprimirArbol();
                    pausa();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("\n *** Opción Inválida. ***");
                    pausa();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.arbol = new ArbolBMas(4);
        app.menu();
    }
}
