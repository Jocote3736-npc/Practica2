import java.util.ArrayList;

public class ArbolBMas {

    // Atributos
    private final int orden;
    private NodoArbolBMas raiz;

    // Métodos
    // Constructor
    public ArbolBMas(int orden) {
        this.orden = Math.max(3, orden);
        raiz = new NodoArbolBMas(true);
    }

    // Inserción de una llave en el árbol B+
    public void insertar(int clave, String dato) {
        // Se inserta primero de forma recursiva
        insertarRecursivo(this.raiz, clave, dato);

        // Se verifica si la raíz se desbordó
        if(this.raiz.getClaves().size() == orden) {
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(false);
            nuevaRaiz.getHijos().add(this.raiz);
            dividirHijo(nuevaRaiz, 0);
            this.raiz = nuevaRaiz;
        }
    }

    // Inserción recursiva de una llave
    // Permite que los nodos se desborden temporalmente y los divide al regresar de la recursión
    private void insertarRecursivo(NodoArbolBMas nodo, int llave, String dato) {
        int i = nodo.getClaves().size() - 1;

        if(nodo.esHoja()) {
            while(i >= 0 && llave < nodo.getClaves().get(i)) {
                i--;
            }
            if (i >= 0 && nodo.getClaves().get(i) == llave) {
                nodo.getDatos().set(i, dato);
                return;
            }
            nodo.getClaves().add(i + 1, llave);
            nodo.getDatos().add(i + 1, dato);
        } else {
            while(i >= 0 && llave < nodo.getClaves().get(i)) {
                i--;
            }
            i++;
            NodoArbolBMas hijo = nodo.getHijos().get(i);

            // Llamada recursiva para insertar la llave primero en el hijo correspondiente
            insertarRecursivo(hijo, llave, dato);

            // Evaluación de desbordamiento al retornar de la recursividad
            if(hijo.getClaves().size() == orden) {
                dividirHijo(nodo, i);
            }
        }
    }

    // Dividir un nodo que está lleno
    private void dividirHijo(NodoArbolBMas padre, int indice) {
        NodoArbolBMas nodoLleno = padre.getHijos().get(indice);
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(nodoLleno.esHoja());
        int mitad = orden / 2;

        if (nodoLleno.esHoja()) {
            // En nodos hoja, se mantiene la llave media en ambos lados
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad, nodoLleno.getClaves().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));

            nuevoNodo.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(mitad, nodoLleno.getDatos().size())));
            nodoLleno.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(0, mitad)));

            nuevoNodo.setSiguiente(nodoLleno.getSiguiente());
            nodoLleno.setSiguiente(nuevoNodo);

            // El padre recibe como separador la primera llave del nuevo nodo
            int llaveMedia = nuevoNodo.getClaves().getFirst();
            padre.getClaves().add(indice, llaveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        } else {
            // En nodos internos, la llave media sube y se elimina del hijo
            int claveMedia = nodoLleno.getClaves().get(mitad);
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad + 1, nodoLleno.getClaves().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));

            // Mover los hijos del nodo dividido
            nuevoNodo.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(mitad + 1, nodoLleno.getHijos().size())));
            nodoLleno.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(0, mitad + 1)));

            padre.getClaves().add(indice, claveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
    }

    // Impresión del árbol como parte de la interfaz pública del árbol
    public void imprimirArbol() {
        imprimirNodo(raiz, "", true);
    }

    // Impresión recursiva privada de los nodos a partir de uno inicial
    private void imprimirNodo(NodoArbolBMas nodo, String indentacion, boolean esUltimo) {
        if (nodo.esHoja()) {
            System.out.println(indentacion + "+- Hoja > " + nodo.getClaves() + " | Datos: " + nodo.getDatos());
        } else {
            System.out.println(indentacion + "+- Interno > " + nodo.getClaves());
        }
        indentacion += esUltimo ? "   " : "|  ";
        for (int i = 0; i < nodo.getHijos().size(); i++) {
            imprimirNodo(nodo.getHijos().get(i), indentacion, i == (nodo.getHijos().size() - 1));
        }
    }

    // Búsqueda de un dato por llave como parte de la interfaz pública del árbol
    public String buscarDato(int llave) {
        NodoArbolBMas hoja = buscarNodo(raiz, llave);
        if (hoja == null) {
            return null;
        }
        for (int i = 0; i < hoja.getClaves().size(); i++) {
            if (hoja.getClaves().get(i) == llave) {
                return hoja.getDatos().get(i);
            }
        }
        return null;
    }

    // Búsqueda de un nodo como parte de la interfaz pública del árbol
    public boolean buscar(int llave) {
        return buscarNodo(raiz, llave) != null;
    }

    // Búsqueda recursiva privada de un nodo a partir de uno inicial
    private NodoArbolBMas buscarNodo(NodoArbolBMas nodo, int llave) {
        if (nodo.esHoja()) {
            for (int clave : nodo.getClaves()) {
                if (clave == llave) {
                    return nodo;
                }
            }
            return null;
        }

        int i = 0;
        while (i < nodo.getClaves().size() && llave >= nodo.getClaves().get(i)) {
            i++;
        }
        return buscarNodo(nodo.getHijos().get(i), llave);
    }

    // Eliminación de una clave con rebalanceo automático
    public boolean eliminar(int clave) {
        if (raiz.getClaves().isEmpty() && raiz.esHoja()) {
            return false;
        }
        boolean eliminado = eliminarRecursivo(raiz, clave);

        // Si la raíz interna quedó sin claves, se promueve su único hijo restante
        if (!raiz.esHoja() && raiz.getClaves().isEmpty()) {
            raiz = raiz.getHijos().get(0);
        }
        return eliminado;
    }

    private boolean eliminarRecursivo(NodoArbolBMas nodo, int clave) {
        if (nodo.esHoja()) {
            int pos = nodo.getClaves().indexOf(clave);
            if (pos == -1) {
                return false;
            }
            nodo.getClaves().remove(pos);
            nodo.getDatos().remove(pos);
            return true;
        }

        int idxHijo = 0;
        while (idxHijo < nodo.getClaves().size() && clave >= nodo.getClaves().get(idxHijo)) {
            idxHijo++;
        }

        NodoArbolBMas hijo = nodo.getHijos().get(idxHijo);
        boolean eliminado = eliminarRecursivo(hijo, clave);
        if (!eliminado) {
            return false;
        }
        
        // Actualiza separador si se eliminó la primera clave de una hoja que aparecía en el padre
        if (hijo.esHoja() && idxHijo > 0 && !hijo.getClaves().isEmpty()) {
            nodo.getClaves().set(idxHijo - 1, hijo.getClaves().get(0));
        }

        // Verifica si el hijo quedó por debajo del mínimo permitido
        if (esInsuficiente(hijo)) {
            rebalancearHijo(nodo, idxHijo);
        }

        return true;
    }

    // Determina si un nodo distinto de la raíz tiene menos elementos que el mínimo
    private boolean esInsuficiente(NodoArbolBMas nodo) {
        if (nodo == raiz) {
            return false;
        }
        int minClaves = nodo.esHoja() ? (int) Math.ceil((orden - 1) / 2.0) : (int) Math.ceil(orden / 2.0) - 1;
        return nodo.getClaves().size() < minClaves;
    }

    // Rebalancea un hijo pidiendo prestado a un hermano o fusionando nodos
    private void rebalancearHijo(NodoArbolBMas padre, int idxHijo) {
        NodoArbolBMas hijo = padre.getHijos().get(idxHijo);
        int minClaves = hijo.esHoja() ? (int) Math.ceil((orden - 1) / 2.0) : (int) Math.ceil(orden / 2.0) - 1;

        // 1. Intentar prestar del hermano izquierdo
        if (idxHijo > 0) {
            NodoArbolBMas hermanoIzq = padre.getHijos().get(idxHijo - 1);
            if (hermanoIzq.getClaves().size() > minClaves) {
                if (hijo.esHoja()) {
                    int ultimaIdx = hermanoIzq.getClaves().size() - 1;
                    int clavePrestada = hermanoIzq.getClaves().remove(ultimaIdx);
                    String datoPrestado = hermanoIzq.getDatos().remove(ultimaIdx);

                    hijo.getClaves().add(0, clavePrestada);
                    hijo.getDatos().add(0, datoPrestado);
                    padre.getClaves().set(idxHijo - 1, clavePrestada);
                } else {
                    int claveSeparadora = padre.getClaves().get(idxHijo - 1);
                    int ultimaClave = hermanoIzq.getClaves().remove(hermanoIzq.getClaves().size() - 1);
                    NodoArbolBMas ultimoHijo = hermanoIzq.getHijos().remove(hermanoIzq.getHijos().size() - 1);

                    hijo.getClaves().add(0, claveSeparadora);
                    hijo.getHijos().add(0, ultimoHijo);
                    padre.getClaves().set(idxHijo - 1, ultimaClave);
                }
                return;
            }
        }

        // 2. Intentar prestar del hermano derecho
        if (idxHijo < padre.getHijos().size() - 1) {
            NodoArbolBMas hermanoDer = padre.getHijos().get(idxHijo + 1);
            if (hermanoDer.getClaves().size() > minClaves) {
                if (hijo.esHoja()) {
                    int clavePrestada = hermanoDer.getClaves().remove(0);
                    String datoPrestado = hermanoDer.getDatos().remove(0);

                    hijo.getClaves().add(clavePrestada);
                    hijo.getDatos().add(datoPrestado);
                    padre.getClaves().set(idxHijo, hermanoDer.getClaves().get(0));
                } else {
                    int claveSeparadora = padre.getClaves().get(idxHijo);
                    int primeraClave = hermanoDer.getClaves().remove(0);
                    NodoArbolBMas primerHijo = hermanoDer.getHijos().remove(0);

                    hijo.getClaves().add(claveSeparadora);
                    hijo.getHijos().add(primerHijo);
                    padre.getClaves().set(idxHijo, primeraClave);
                }
                return;
            }
        }

        // 3. Fusionar con hermano izquierdo o derecho
        if (idxHijo > 0) {
            fusionarNodos(padre, idxHijo - 1);
        } else {
            fusionarNodos(padre, idxHijo);
        }
    }

    // Fusiona el hijo en 'idxIzq' con su hermano derecho en 'idxIzq + 1'
    private void fusionarNodos(NodoArbolBMas padre, int idxIzq) {
        NodoArbolBMas izquierdo = padre.getHijos().get(idxIzq);
        NodoArbolBMas derecho = padre.getHijos().get(idxIzq + 1);
        int claveSeparadora = padre.getClaves().remove(idxIzq);
        padre.getHijos().remove(idxIzq + 1);

        if (izquierdo.esHoja()) {
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getDatos().addAll(derecho.getDatos());
            izquierdo.setSiguiente(derecho.getSiguiente());
        } else {
            izquierdo.getClaves().add(claveSeparadora);
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getHijos().addAll(derecho.getHijos());
        }
    }

    // Recorrido por rango (busca la clave inicial y muestra hasta n elementos enlazados)
    public void recorrerRango(int claveInicio, int n) {
        if (n <= 0) {
            System.out.println("La cantidad de elementos a recorrer debe ser mayor que 0.");
            return;
        }

        NodoArbolBMas hojaActual = buscarNodo(raiz, claveInicio);
        int idx = 0;

        // Avanza dentro de la hoja hasta la primera clave >= claveInicio
        while (hojaActual != null && idx < hojaActual.getClaves().size() && hojaActual.getClaves().get(idx) < claveInicio) {
            idx++;
            if (idx == hojaActual.getClaves().size()) {
                hojaActual = hojaActual.getSiguiente();
                idx = 0;
            }
        }

        if (hojaActual == null) {
            System.out.println("No se encontraron elementos a partir de la clave " + claveInicio + ".");
            return;
        }

        int impresos = 0;
        while (hojaActual != null && impresos < n) {
            while (idx < hojaActual.getClaves().size() && impresos < n) {
                int clave = hojaActual.getClaves().get(idx);
                String dato = hojaActual.getDatos().get(idx);
                System.out.println("  Clave: " + clave + " -> Dato: " + dato);
                idx++;
                impresos++;
            }
            hojaActual = hojaActual.getSiguiente();
            idx = 0;
        }

        if (impresos == 0) {
            System.out.println("No se encontraron elementos en el rango solicitado.");
        }
    }
}