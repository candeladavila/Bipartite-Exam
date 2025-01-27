///////////////////////////////////////////////////////////////////////////////
// Student's name: [Your Name]
// Identity number:  [Your DNI or Passport Number] 
///////////////////////////////////////////////////////////////////////////////


package org.uma.ed.exam;


import org.uma.ed.datastructures.dictionary.Dictionary;
import org.uma.ed.datastructures.dictionary.JDKHashDictionary;
import org.uma.ed.datastructures.graph.DictionaryGraph;
import org.uma.ed.datastructures.graph.Graph;
import org.uma.ed.datastructures.set.JDKHashSet;
import org.uma.ed.datastructures.set.Set;
import org.uma.ed.datastructures.stack.JDKStack;
import org.uma.ed.datastructures.stack.Stack;

/**
 * This class is used to test if a given graph is bipartite using depth-first traversal. A bipartite graph is a graph
 * whose vertices can be divided into two disjoint sets such that every edge connects a vertex in the first set to one
 * in the second set.
 *
 * @param <V> The type of the vertices in the graph.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class BipartiteExam<V> {
  public enum Color { Red, Blue;
    Color opposite() {
      return (this == Red) ? Blue : Red;
    }
  }

  private record Pair<V>(V vertex, Color color) {
    static <V> Pair<V> of(V vertex, Color color) {
      return new Pair<>(vertex, color);
    }
  }

  private boolean bipartite;
  private final Dictionary<V, Color> assignedColor;

  static <V> BipartiteExam<V> of(Graph<V> graph) {
    return new BipartiteExam<>(graph);
  }

  public BipartiteExam(Graph<V> graph) {
    this.bipartite = true; //inicializamos el booleano a true
    this.assignedColor = new JDKHashDictionary<>(); //creamos un nuevo diccionario Hash (nos dan la clase en el enunciado)
    Color color; //creamos una variable "color" para llevar un registro del color que usamos

    //Condición inicial → si un grafo está vacío entonces es bipartito
    if(graph.isEmpty()){
      return;
    }

    //inicializamos la pila desde un nodo arbitrario del grafo
    Stack<Pair<V>> pila = new JDKStack<>();
    V source = graph.vertices().iterator().next();
    Pair<V> pair = new Pair<>(source, Color.Red);  //creamos una variable pareja que usaremos para ir añadiendo los pares de valores
    pila.push(pair); //añadimos el primer nodo al stack

    //Bucle while para ir iterando sobre los nodos del grafo
    while (bipartite && !pila.isEmpty()){
      //quitamos la primera pareja del stack y guardamos los valores de sus variables
      pair = pila.top();
      pila.pop();
      color = pair.color;
      source = pair.vertex;

      if(!assignedColor.isDefinedAt(source)){
        assignedColor.insert(source, color); //si no está definido en el diccionario lo añadimos

        for(V successor : graph.successors(source)){ //iteramos sobre los nodos adyacentes al nodo que estamos analizando
          if(!assignedColor.isDefinedAt(successor)){
            pila.push(Pair.of(successor, color.opposite())); //si no se encuentra en el diccionario lo añadimos al stack
          } else if(assignedColor.valueOf(successor) != color.opposite()){ //si el color de los hijos no es el opuesto al nodo fuente → false
            bipartite = false;
            return;
          }
        }
      }
      else if(color != assignedColor.valueOf(source)){ //si el nodo ya se encuentra en el diccionario y el color que le tocaría no coincide con el que tiene entonces no es bipartito
          bipartite = false;
          return;
      }
    }
  }

  public boolean isBipartite() {
    return bipartite;
  }

  public Dictionary<V, Color> assignedColor() {
    return bipartite ? assignedColor : null;
  }

  public static void main(String[] args) {
    Test.main(args);
  }
}

