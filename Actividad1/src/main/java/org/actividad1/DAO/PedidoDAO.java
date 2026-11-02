package org.actividad1.DAO;

import org.actividad1.conexion.ConexionDB;
import org.actividad1.entidades.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {



    // 1. Inserción de Pedido y Reducción de Stock
    public boolean insertarPedido(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (fecha_pedido, cantidad, id_producto) VALUES (?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";

        Connection con = null;
        try {
            // Llamamos directamente a la clase estática sin crear variables locales nulas
            con = ConexionDB.obtenerConexion();
            con.setAutoCommit(false); // Iniciar transacción

            // Reducir stock del producto
            try (
                    PreparedStatement psStock = con.prepareStatement(sqlStock)) {
                psStock.setInt(1, pedido.getCantidad());
                psStock.setInt(2, pedido.getId_producto());
                psStock.setInt(3, pedido.getCantidad()); // Asegura que haya suficiente stock

                int filasAfectadas = psStock.executeUpdate();
                if (filasAfectadas == 0) {
                    con.rollback(); // Cancela si no hay suficiente stock
                    return false;
                }
            }

            // Insertar el pedido
            try (PreparedStatement psPedido = con.prepareStatement(sqlPedido)) {
                psPedido.setDate(1, java.sql.Date.valueOf(pedido.getFecha_Pedido().toLocalDate()));
                psPedido.setInt(2, pedido.getCantidad());
                psPedido.setInt(3, pedido.getId_producto());// O id_producto según lo hayas renombrado
                psPedido.executeUpdate();
            }

            con.commit(); // Confirmar cambios si ambas operaciones tienen éxito
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Cancelar cambios ante un error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 2. Listado de Pedidos utilizando JOIN
    public List<Pedido> listarPedidos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id_pedido, p.fecha_pedido, p.cantidad, p.id_producto, prod.nombre " +
                "FROM pedidos p " +
                "INNER JOIN productos prod ON p.id_producto = prod.id_producto";

        try (Connection con = ConexionDB.obtenerConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido").toLocalDate().atStartOfDay(), // Convierte java.sql.Date a LocalDateTime
                        rs.getInt("cantidad"),
                        rs.getInt("id_producto")

                );
                lista.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}