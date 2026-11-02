package org.actividad1;

import org.actividad1.DAO.ProductoDAO;
import org.actividad1.entidades.*;
import org.actividad1.DAO.*;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;




//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
               Scanner scanner = new Scanner(System.in);

                ProductoDAO productoDAO = new ProductoDAO();
                PedidoDAO pedidoDAO = new PedidoDAO();
                 Producto producto;
                 Integer idProducto;
              int opcion;

              do {
                  System.out.println("\n--- MENÚ GESTIÓN DE TIENDA ---");
                   System.out.println("1. Insertar producto");
                   System.out.println("2. Consultar producto por id");
                   System.out.println("3. Actualizar stock de producto");
                   System.out.println("4. Insertar pedido");
                   System.out.println("5. Listado de pedidos");
                   System.out.println("6. Salir");
                   System.out.print("Elija una opción: ");

                   opcion = scanner.nextInt();
                   scanner.nextLine();//limpiar buffer
                  switch (opcion) {
                       case 1:     //INSERTAR PRODUCTO
                           // Pedimos por consola el nombre, precio stock del producto
                           System.out.println("Introduce el nombre del producto");
                           String nombreProd = scanner.nextLine();
                        try {
                            System.out.println("Introduce el precio del producto");
                            double precioProd = scanner.nextDouble();

                            System.out.println("Introduce el stock");
                            int stock = scanner.nextInt();

                            scanner.nextLine(); //limpiamos buffer
                            //creamos e insertamos producto
                            producto = new Producto(null, nombreProd, precioProd, stock);

                            if (productoDAO.insertarProducto(producto)) {
                                System.out.println("Prodcuto insertado");
                            } else {
                                System.out.println("No se ha podido insertar producto");
                            }
                        }
                           catch (InputMismatchException e) {
                                System.out.println("¡ERROR: Has introducido letras o un formato de número inválido!");
                                scanner.nextLine(); //limpiamos buffer
                         }

                           break;
                      case 2:   //CONSULTAR PRODUCTO POR ID
                          try {
                              System.out.println("Introduce el id del producto:");
                              idProducto = scanner.nextInt();
                          } catch (InputMismatchException e) {
                              System.out.println("Error: Has introducido texto en lugar de un número.");
                              scanner.next(); //
                             break;
                          }
                          producto=null;
                          producto=productoDAO.obtenerPorId(idProducto);

                          //compñrobamos si no es null el producto
                          if(producto!=null){
                              System.out.printf("%-15s %-20s %-12s %-8s\n", "ID PRODUCTO", "NOMBRE", "PRECIO", "STOCK");
                              System.out.println("------------------------------------------------------------");


                              System.out.printf("%-15d %-20s %-12.2f %-8d\n",
                                      producto.getId_producto(),
                                      producto.getNombre(),
                                      producto.getPrecio(),
                                      producto.getStock()
                              );
                            }
                          else{
                              System.out.println("El producto no existe");
                          }

                           break;
                       case 3:    //ACTUALIZAR STOCK
                           System.out.println("--- ACTUALIZAR STOCK ---");

                           try {
                               System.out.println("Introduce el ID del producto:");
                               idProducto = scanner.nextInt();
                           } catch (InputMismatchException e) {
                               System.out.println("Error: Has introducido texto en lugar de un número.");
                               scanner.next(); //
                               break;
                           }
                          //obtenemos el producto
                            producto = productoDAO.obtenerPorId( idProducto);
                           if (producto != null) {    //comprobamos que existe
                               System.out.println("Producto actual: " + producto.getNombre() + " | Stock actual: " + producto.getStock());
                               System.out.println("Introduce el nuevo stock:");
                               int nuevoStock = scanner.nextInt();
                               scanner.nextLine(); // Limpiar el buffer


                               productoDAO.actualizarStock (idProducto, nuevoStock);
                           } else {
                               System.out.println("No se encontró ningún producto con el ID " +  idProducto);
                           }
                           break;
                       case 4:     //INSERTAR PEDIDO
                           System.out.println("Introduce el id del producto");
                            idProducto = scanner.nextInt();
                           // Lógica insertar pedido (controlando si el producto no existe)
                           producto=null;
                           producto=productoDAO.obtenerPorId(idProducto);
                           if(producto!=null) {  //comprobamos que el producto exsite

                               int stockActual=producto.getStock();

                               //introducimos cantidad
                               System.out.println("Introduce la cantidad del pedido");
                               int cant = scanner.nextInt();

                               //comprobamos que hay suficiente stcok

                               if(stockActual>=cant) {

                                   //creamos el pedido
                                   Pedido pedido = new Pedido(null, LocalDateTime.now(), cant, idProducto);

                                   //Guardamos el pedido en la base de datos

                                   pedidoDAO.insertarPedido(pedido);

                                   //actualizamos el stock
                                   productoDAO.actualizarStock(idProducto,stockActual-cant);



                               }
                               else {
                                   System.out.println("No hay suficiente STOCK");
                               }

                           }

                           else {
                               System.out.println("El producto no existe");
                           }
                               break;
                       case 5:
                          // Lógica listar pedidos con JOIN
                           System.out.printf("%-8s %-22s %-12s %-18s %-25s\n", "ID", "FECHA", "CANTIDAD", "ID DEL PRODUCTO", "NOMBRE PRODUCTO");
                           System.out.println("---------------------------------------------------------------------------------------------");

                           List<Pedido> lista = pedidoDAO.listarPedidos();
                           for (Pedido pedido : lista) {
                               // Buscamos el objeto producto para poder pintar su nombre
                               producto = productoDAO.obtenerPorId(pedido.getId_producto());

                               // Evitamos que falle el programa si por algún motivo el producto no existe en la base de datos
                               String nombreProducto = (producto != null) ? producto.getNombre() : "No encontrado";

                               // 2. Datos alineados con los mismos anchos que la cabecera
                               System.out.printf("%-8d %-22s %-12d %-18d %-25s\n",
                                       pedido.getIdPedido(),
                                       pedido.getFecha_Pedido().toString(), // Convertimos la fecha a texto para formatearla como %s
                                       pedido.getCantidad(),
                                       pedido.getId_producto(),
                                       nombreProducto
                               );
                           }

                           break;
                      case 6:
                           System.out.println("Saliendo de la aplicación...");
                           break;
                     default:
                           System.out.println("Opción no válida.");
                   }
               } while (opcion != 6);
              scanner.close();
           }
        }

