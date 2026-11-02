package org.actividad1.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
      private Integer id_producto;
      private String nombre;
      private double precio;
      private int stock;

}