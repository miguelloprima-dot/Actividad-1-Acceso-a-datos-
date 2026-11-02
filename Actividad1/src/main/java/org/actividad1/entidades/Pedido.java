package org.actividad1.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
     private Integer idPedido;
     private LocalDateTime fecha_Pedido;
     private int cantidad;
     private Integer id_producto;


}