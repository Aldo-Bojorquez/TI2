
//Panel_acceso
package proyecto_abd;


import javax.swing.*;
import java.sql.Connection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

public class Panel_Ventas extends javax.swing.JFrame{
 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DefaultTableModel modeloC = new DefaultTableModel();
    DefaultTableModel modeloPro = new DefaultTableModel();
    DefaultTableModel modeloPaq = new DefaultTableModel();
    DefaultTableModel modeloPed = new DefaultTableModel();
    DefaultTableModel modeloVen = new DefaultTableModel();
    DefaultTableModel modeloIng = new DefaultTableModel();
    ArrayList<String> registrosPro=new ArrayList<>();
    Conexion mysql=new Conexion();
    Connection cn=mysql.Conexion();
    String registrosC[]=new String[4];
    String matriz_Aux1 [][],nombreEmpleado,puestoEmpleado;
    float precioT,precioPro;
    int cantidadProducto;
    
    public Panel_Ventas() {
        initComponents();
        JLfecha.setText(fechaActual());
        String[] titulosCarrito={"Identificador","Producto","Tipo producto","¿Cuantos desea?","Precio"};
        modeloC.setColumnIdentifiers(titulosCarrito);
        JTablaCarrito.setModel(modeloC);
        String[] titulosProductos={"Id_producto","Nombre de producto","Tipo de producto","Precio"};
        modeloPro.setColumnIdentifiers(titulosProductos);
        JTablaProductos.setModel(modeloPro);
        String[] titulosIngredientes={"Id_ingrediente","Nombre de ingrediente","Tipo de producto","Precio"};
        modeloIng.setColumnIdentifiers(titulosIngredientes);
        JTablaIngredientes.setModel(modeloIng);
        String[] titulosPaquetes={"Id_producto","Nombre de producto","Tipo de producto","Precio"};
        modeloPaq.setColumnIdentifiers(titulosPaquetes);
        JTablaPaquetes.setModel(modeloPaq);
        String[] titulosPedido={"Id producto","Id paquete","Id inventario"};
        modeloPed.setColumnIdentifiers(titulosPedido);
        JTablaPedidosP.setModel(modeloPed);
        String[] titulosVenta={"Folio venta","Fecha venta","Hora pedido","Hora entrega","Id empleado","Telefono cliente","Total de venta","Tipo de Venta"};
        modeloVen.setColumnIdentifiers(titulosVenta);
        JTablaVentasP.setModel(modeloVen);
        if(jTabbedPane1.getSelectedIndex()==0){
            obtenerDatosJCB(true);
        }
    }
    
    //metodo para el limpiado de todas las tablas
    public void limpiarTabla(DefaultTableModel modeloE,JTable tablaE){
        while(modeloE.getRowCount()>0){
            modeloE.removeRow(0);
        }
        tablaE.setModel(modeloE);
    }
    //cambio
    
    //metodo para la creacion de la fecha
    public static String fechaActual(){
        Date fecha = new Date();
        SimpleDateFormat formatof = new SimpleDateFormat("YYYY-MM-dd");
        return formatof.format(fecha);
    }
    
    //metodo para buscar productos
    public void buscarProductos(String entrada,int f,DefaultTableModel modeloE,JTable tablaE,boolean seccion){
        String registros[]=new String [4];
        int cont_Aux;
        if(registrosPro.isEmpty()){
            cont_Aux=0;
        }else{
            cont_Aux=registrosPro.size();
        }
        String SQLs;
        //limpiar tabla
        limpiarTabla(modeloE, tablaE);
        if(seccion){
            SQLs="SELECT inventario.id_inventario,inventario.nombre,tipo_productos.tipo_producto, inventario.precio "
                + "FROM productos, tipo_productos, inventario "
                + "WHERE inventario.id_inventario = '"+entrada+"' "
                + "AND productos.id_tipo_producto = tipo_productos.id_tipo_producto "
                + "AND inventario.id_inventario = productos.id_inventario ;";
        }else{
            SQLs="SELECT inventario.id_inventario,inventario.nombre,tipo_productos.tipo_producto, inventario.precio "
                + "FROM productos, tipo_productos, inventario "
                + "WHERE productos.nombre LIKE '%"+entrada+"%' "
                + "AND productos.id_tipo_producto = tipo_productos.id_tipo_producto "
                + "AND inventario.id_inventario = productos.id_inventario ;";
        }
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            if(seccion){
                while(rs.next()){
                    matriz_Aux1[f][0]=rs.getString(1);
                    matriz_Aux1[f][1]=rs.getString(2);
                    matriz_Aux1[f][2]=rs.getString(3);
                    matriz_Aux1[f][3]=rs.getString(4);
                }
                
            }else{
                while(rs.next()){
                    registros[0]=rs.getString(1);
                    registros[1]=rs.getString(2);
                    registros[2]=rs.getString(3);
                    registros[3]=rs.getString(4);

                    registrosPro.add(cont_Aux,registros[0]+" "+registros[3]);
                    cont_Aux++;
                    modeloE.addRow(registros);
                }
                tablaE.setModel(modeloE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para mostrar todos los productos
    public void mostrarTodoProductos(){
        String registros[]=new String [4];
        int cont_Aux;
        if(registrosPro.isEmpty()){
            cont_Aux=0;
        }else{
            cont_Aux=registrosPro.size();
        }
        //limpiar tabla
        limpiarTabla(modeloPro, JTablaProductos);
        
        String SQLs="SELECT inventario.id_inventario, inventario.nombre, tipo_productos.tipo_producto, inventario.precio "
                + "FROM productos, tipo_productos, inventario "
                + "WHERE productos.id_tipo_producto = tipo_productos.id_tipo_producto "
                + "AND inventario.id_inventario = productos.id_inventario ;";
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                
                registros[0]=rs.getString(1);
                registros[1]=rs.getString(2);
                registros[2]=rs.getString(3);
                registros[3]=rs.getString(4);
                
                registrosPro.add(cont_Aux,registros[0]+" "+registros[3]);
                cont_Aux++;               
                modeloPro.addRow(registros);
            }
            JTablaProductos.setModel(modeloPro);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para buscar productos
    public void buscarIngredientes(String entrada,DefaultTableModel modeloE,JTable tablaE){
        String registros[]=new String [4];
        int cont_Aux;
        if(registrosPro.isEmpty()){
            cont_Aux=0;
        }else{
            cont_Aux=registrosPro.size();
        }
        String SQLs="SELECT inventario.id_inventario,inventario.nombre,tipo_productos.tipo_producto, inventario.precio "
                + "FROM productos, tipo_productos, inventario "
                + "WHERE productos.nombre LIKE '%"+entrada+"%' "
                + "AND productos.id_tipo_producto = tipo_productos.id_tipo_producto "
                + "AND inventario.id_inventario = productos.id_inventario ;";
        //limpiar tabla
        limpiarTabla(modeloE, tablaE);
        
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                registros[0]=rs.getString(1);
                registros[1]=rs.getString(2);
                registros[2]=rs.getString(3);
                registros[3]=rs.getString(4);

                registrosPro.add(cont_Aux,registros[0]+" "+registros[3]);
                cont_Aux++;
                
                modeloE.addRow(registros);
            }
            tablaE.setModel(modeloE);
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para mostrar todos los productos
    public void mostrarTodoIngredientes(){
        String registros[]=new String [4];
        int cont_Aux;
        if(registrosPro.isEmpty()){
            cont_Aux=0;
        }else{
            cont_Aux=registrosPro.size();
        }
        //limpiar tabla
        limpiarTabla(modeloIng, JTablaIngredientes);
        
        String SQLs="SELECT inventario.id_inventario, inventario.nombre, tipo_productos.tipo_producto, inventario.precio "
                + "FROM tipo_productos, inventario "
                + "WHERE tipo_productos.tipo_producto NOT LIKE 'Producto' "
                + "AND tipo_productos.tipo_producto NOT LIKE 'Bebida'"
                + "AND inventario.id_tipo = tipo_productos.id_tipo_producto ;";
        
        try {
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                
                registros[0]=rs.getString(1);
                registros[1]=rs.getString(2);
                registros[2]=rs.getString(3);
                registros[3]=rs.getString(4);
                
                registrosPro.add(cont_Aux,registros[0]+" "+registros[3]);
                cont_Aux++;
         
                modeloIng.addRow(registros);
            }
            JTablaIngredientes.setModel(modeloIng);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    //metodo para buscar los paquetes
    public void buscarPaquetes(int id,DefaultTableModel modeloE,JTable tablaE,boolean seccion){
        String registros[];
        if(seccion){
            limpiarTabla(modeloE, tablaE);
            registros=new String[4];
        }else{
            registros=new String[5];
        }
        
        //inicializar matriz
        String SQLs="SELECT paq_pro.id_producto,paquetes.precio_paquete FROM paquetes,paq_pro "
                + "WHERE paquetes.id_paquete='"+id+"'"
                + "AND paquetes.id_paquete=paq_pro.id_paquete ;";
        int fila=0;
        
        try {
            Statement st = cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            
            while(rs.next()){
                
                if(seccion){
                    JLprecioPaq2.setText(rs.getString(2));
                }
                fila++;
            }
            matriz_Aux1=new String[fila][4];
        } catch (SQLException ex) {
        }
        
        seleccionarDatos(id,seccion);
        if(seccion){
            for (String[] datos_matriz : matriz_Aux1) {
                registros[0] = datos_matriz[0];
                registros[1] = datos_matriz[1];
                registros[2] = datos_matriz[2];
                registros[3] = datos_matriz[3];
                
                modeloE.addRow(registros);
            }
            tablaE.setModel(modeloE);
            
            //metodo para la sumatorio de los productos de los paquetes.
            int filasT=tablaE.getRowCount();
            precioTotalPaquetes(filasT);
        }else{
            
        }
    }
    
    //metodo para pasar los datos de la tabla producto a tabla pedidos
    public void pasarProductoTablaPedido(int filaS){
        String [] registros=new String[5];
        int cont_Aux=0;
        for(int col=0;col<5;col++){
            switch (col) {
                case 3:
                    registros[col]=1+"";
                    break;
                default:
                    registros[col]=JTablaProductos.getValueAt(filaS, cont_Aux).toString();
                    cont_Aux++;
                    break;
            }
        }
        modeloC.addRow(registros);
        JTablaCarrito.setModel(modeloC);
        
        JLmensajePro.setText("Se agrego de manera correcta al carrito.");
        
        precioTotalPedido(tomarFilasCarrito());
    }

    //metodo para pasar los datos de la tabla producto a tabla pedidos
    public void pasarIngredienteTablaPedido(int filaS){
        String [] registros=new String[5];
        int cont_Aux=0;
        for(int col=0;col<5;col++){
            switch (col) {
                case 3:
                    registros[col]=1+"";
                    break;
                default:
                    registros[col]=JTablaIngredientes.getValueAt(filaS, cont_Aux).toString();
                    cont_Aux++;
                    break;
            }
        }
        modeloC.addRow(registros);
        JTablaCarrito.setModel(modeloC);
        
        JLmensajePro.setText("Se agrego de manera correcta al carrito.");
        
        precioTotalPedido(tomarFilasCarrito());
    }
    
    //metodos para los paquetes
    public void seleccionarDatos(int id,boolean seccion){
        
        ArrayList<String> id_productos=new ArrayList<>();
        ArrayList<String> id_inventario=new ArrayList<>();
                String SQLs1="SELECT paquetes.id_paquete,paq_pro.id_producto FROM paquetes,paq_pro "
                + "WHERE paquetes.id_paquete='"+id+"'"
                + "AND paquetes.id_paquete=paq_pro.id_paquete ;";
        //////////////////////////////////////////////
        try {
            Statement st = cn.createStatement();
            ResultSet rs1=st.executeQuery(SQLs1);
            
            
            while(rs1.next()){
                id_productos.add(rs1.getString(2));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error en la carga de datos de paquetes\n"+ex.getLocalizedMessage(),""
                    + "Error en la BD",JOptionPane.WARNING_MESSAGE);
        }
        ////////////////////////////////////////////////
        try {
            Statement st = cn.createStatement();
            for(int i=0;i<id_productos.size();i++){
                String SQLs2="SELECT * FROM productos WHERE id_producto = '"+id_productos.get(i)+"' ;";
                ResultSet rs2=st.executeQuery(SQLs2);
                while(rs2.next()){
                    id_inventario.add(rs2.getString(3));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error en la carga de datos de productos\n"+ex.getLocalizedMessage(),""
                    + "Error en la BD",JOptionPane.WARNING_MESSAGE);
        }
        //////////////////////////////////////////////////
        for(int i=0;i<id_inventario.size();i++){
            buscarProductos(id_inventario.get(i),i,modeloPaq,JTablaPaquetes, true);
        }
    }
    
    //metodos para sacer el precio total del paquete
    public void precioTotalPaquetes(int numFilas){
        float precioPaquete=0;
        for(int i=0;i<numFilas;i++){
            precioPaquete+=Float.parseFloat(JTablaPaquetes.getValueAt(i, 3).toString());
        }
        JTablaPaquetes.setModel(modeloPaq);
        JLprecioPaq1.setText(precioPaquete+"");
    }
    
    //metodo para sacer el precio total del pedido
    public void precioTotalPedido(int numFilas){
        if(JTablaCarrito.getRowCount()==0){
            JLprecioPed.setText("0");
        }else{
            precioT=0;
            for(int i=0;i<numFilas;i++){
                precioT+=Float.parseFloat(JTablaCarrito.getValueAt(i, 4).toString());
            }
            JLprecioPed.setText(precioT+"");
        }
    }
    
    //metodo para agregar el paquete al carrito
    public void agregarPaquete(int itemS){
        String [] registros=new String[5];
        String SQLs="SELECT paquetes.id_paquete,paquetes.nombre,tipo_productos.tipo_producto,paquetes.precio_paquete "
                + "FROM paquetes,tipo_productos "
                + "WHERE paquetes.id_paquete='"+itemS+"' "
                + "AND paquetes.id_tipo_producto=tipo_productos.id_tipo_producto;";
        
        try{
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                registros[0]=rs.getString(1);
                registros[1]=rs.getString(2);
                registros[2]=rs.getString(3);
                registros[3]="1";
                registros[4]=rs.getString(4);
                
                modeloC.addRow(registros);
            }
            JTablaCarrito.setModel(modeloC);
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "No se pudo cargar los datos de la base de datos\n"+ex.getLocalizedMessage(),
                    "Error de la BD",JOptionPane.WARNING_MESSAGE);
        }
        precioTotalPedido(tomarFilasCarrito());
    }
    
    //metodo para quitar producto del carrito
    public void quitarCarrito(int filaS){
        try{
            modeloC.removeRow(filaS);
            int numFilas=JTablaCarrito.getRowCount();
            precioTotalPedido(numFilas);
        }catch(ArrayIndexOutOfBoundsException ex){
            
        }
    }
    
    //metodo para tomar los precion de los registros.
    public void tomarPrecioArray(String id_inv){
        for(int i=0;i<registrosPro.size();i++){
            String datos[]=registrosPro.get(i).split(" ");
            if(datos[0].equalsIgnoreCase(id_inv)){
                precioPro=Float.parseFloat(datos[1]);
            }
        }
    }
    
    //metodo para obtener las filas de la tabla carrito
    public int tomarFilasCarrito(){
        int filas=JTablaCarrito.getRowCount();
        return filas;
    }
    
    //metodo para actualizar los jcb
    public void obtenerDatosJCB(boolean band){
        String SQLs="SELECT * FROM paquetes;";
        if(band){
            JCBpaquetesPe.removeAllItems();
            try {
                Statement st=cn.createStatement();
                ResultSet rs=st.executeQuery(SQLs);

                JCBpaquetesPe.addItem("Seleccione un paquete");
                while(rs.next()){
                    JCBpaquetesPe.addItem(rs.getString(2));
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }else{
            JCBpaquetesPa.removeAllItems();
            try {
                Statement st=cn.createStatement();
                ResultSet rs=st.executeQuery(SQLs);

                JCBpaquetesPa.addItem("Seleccione un paquete");
                while(rs.next()){
                    JCBpaquetesPa.addItem(rs.getString(2));
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }
    
    //metodo para sacer el tipo de producto.
    public int verTipo(String tipo_producto){
        String SQLs="SELECT * FROM tipo_productos WHERE tipo_producto LIKE '"+tipo_producto+"';";
        int id=0;
        try{
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(SQLs);
            
            while(rs.next()){
                id=Integer.parseInt(rs.getString(1));
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "No se pudo cargar los datos de la base de datos\n"+ex.getLocalizedMessage(),
                    "Error de la BD",JOptionPane.WARNING_MESSAGE);
        }
        return id;
    }
        
    //metodo para pasar los datos a pedidos
    public void agregarPedido(ArrayList<String> ing,ArrayList<String> paq,ArrayList<String> pro){
        String [] registros=new String[7];
        boolean band1=true,band2=true,band3=true;
        int cont_ing=ing.size(),cont_paq=paq.size(),cont_pro=pro.size();
        int cont1=0,cont2=0,cont3=0;
        while(band1||band2||band3){
            if(cont1<cont_ing){
                registros[0]=ing.get(cont1);
            }else{
                band1=false;
            }
            if(cont2<cont_paq){
                registros[2]=paq.get(cont2);
            }else{
                band2=false;
            }
            if(cont3<cont_pro){
                registros[4]=ing.get(cont3);
            }else{
                band3=false;
            }
        }
         
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPMpedidos = new javax.swing.JPopupMenu();
        JMIpeQuitar = new javax.swing.JMenuItem();
        JPMproductos = new javax.swing.JPopupMenu();
        JMIproAgregar = new javax.swing.JMenuItem();
        JPMpedidosPen = new javax.swing.JPopupMenu();
        JMIcompletarP = new javax.swing.JMenuItem();
        JMIcancelarP = new javax.swing.JMenuItem();
        JPMadicional = new javax.swing.JPopupMenu();
        JMIagregarI = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        JPpedidos = new javax.swing.JPanel();
        JCBpaquetesPe = new javax.swing.JComboBox<>();
        JBagregarCarrito = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTablaCarrito = new javax.swing.JTable();
        JBingresarProducto = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        JLprecioPed = new javax.swing.JLabel();
        JPproductos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JTablaProductos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        JTFbuscarPro = new javax.swing.JTextField();
        JBbuscarPro = new javax.swing.JButton();
        JLmensajePro = new javax.swing.JLabel();
        JPadicional = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        JTFbuscarIngre = new javax.swing.JTextField();
        JBbuscarIngre = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        JTablaIngredientes = new javax.swing.JTable();
        JLmensajeIng = new javax.swing.JLabel();
        JPpaquetes = new javax.swing.JPanel();
        JCBpaquetesPa = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        JTablaPaquetes = new javax.swing.JTable();
        JLmensajePaq = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JLprecioPaq2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        JLprecioPaq1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        JPventa = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        JTablaPedidosP = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        JTablaVentasP = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        JLusuario = new javax.swing.JLabel();
        JLfecha = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        JMopcionesE = new javax.swing.JMenu();
        JMIclientes = new javax.swing.JMenuItem();
        JMIconexion = new javax.swing.JMenuItem();
        JMIsalir = new javax.swing.JMenuItem();
        JMopcionesA = new javax.swing.JMenu();
        JMIpaquetes = new javax.swing.JMenuItem();
        JMalmacen = new javax.swing.JMenu();
        JMIproinv = new javax.swing.JMenuItem();
        JMItipoProducto = new javax.swing.JMenuItem();
        JMIadminEmpleados = new javax.swing.JMenuItem();
        JMIhistorial = new javax.swing.JMenuItem();
        JMayuda = new javax.swing.JMenu();
        JMItutorial = new javax.swing.JMenuItem();

        JMIpeQuitar.setText("Quitar de carrito");
        JMIpeQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIpeQuitarActionPerformed(evt);
            }
        });
        JPMpedidos.add(JMIpeQuitar);

        JMIproAgregar.setText("Agregar a carrito");
        JMIproAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIproAgregarActionPerformed(evt);
            }
        });
        JPMproductos.add(JMIproAgregar);

        JMIcompletarP.setText("Finalizar pedido");
        JPMpedidosPen.add(JMIcompletarP);

        JMIcancelarP.setText("Cancelar pedido");
        JPMpedidosPen.add(JMIcancelarP);

        JMIagregarI.setText("Agregar a carrito");
        JMIagregarI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIagregarIActionPerformed(evt);
            }
        });
        JPMadicional.add(JMIagregarI);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ventas");

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        JBagregarCarrito.setText("Agregar a carrito");
        JBagregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBagregarCarritoActionPerformed(evt);
            }
        });

        JTablaCarrito = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int rowCol){
                boolean resEditable=true;
                if(rowCol==3){
                    resEditable=true;
                }else{
                    resEditable=false;
                }
                return resEditable;
            }
        };
        JTablaCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        JTablaCarrito.setComponentPopupMenu(JPMpedidos);
        JTablaCarrito.getTableHeader().setReorderingAllowed(false);
        JTablaCarrito.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                JTablaCarritoPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(JTablaCarrito);

        JBingresarProducto.setText("Ingresar Pedido");
        JBingresarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBingresarProductoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel3.setText("$");

        JLprecioPed.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        JLprecioPed.setText("0");

        javax.swing.GroupLayout JPpedidosLayout = new javax.swing.GroupLayout(JPpedidos);
        JPpedidos.setLayout(JPpedidosLayout);
        JPpedidosLayout.setHorizontalGroup(
            JPpedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpedidosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPpedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(JPpedidosLayout.createSequentialGroup()
                        .addComponent(JCBpaquetesPe, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBagregarCarrito))
                    .addGroup(JPpedidosLayout.createSequentialGroup()
                        .addComponent(JBingresarProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLprecioPed)))
                .addContainerGap())
        );
        JPpedidosLayout.setVerticalGroup(
            JPpedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpedidosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPpedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JCBpaquetesPe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBagregarCarrito))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(JPpedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JBingresarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPpedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JLprecioPed)
                        .addComponent(jLabel3)))
                .addGap(19, 19, 19))
        );

        jTabbedPane1.addTab("Pedidos", JPpedidos);

        JTablaProductos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int rowCol){
                return false;
            }
        };
        JTablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        JTablaProductos.setComponentPopupMenu(JPMproductos);
        JTablaProductos.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(JTablaProductos);

        jLabel1.setText("Buscar producto: ");

        JBbuscarPro.setText("Buscar");
        JBbuscarPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBbuscarProActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPproductosLayout = new javax.swing.GroupLayout(JPproductos);
        JPproductos.setLayout(JPproductosLayout);
        JPproductosLayout.setHorizontalGroup(
            JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproductosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JTFbuscarPro, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLmensajePro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBbuscarPro, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        JPproductosLayout.setVerticalGroup(
            JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPproductosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(JTFbuscarPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBbuscarPro)
                    .addComponent(JLmensajePro))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Productos", JPproductos);

        jLabel6.setText("Buscar ingrediente:");

        JBbuscarIngre.setText("Buscar");
        JBbuscarIngre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBbuscarIngreActionPerformed(evt);
            }
        });

        JTablaIngredientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        JTablaIngredientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        JTablaIngredientes.setComponentPopupMenu(JPMadicional);
        JTablaIngredientes.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(JTablaIngredientes);

        javax.swing.GroupLayout JPadicionalLayout = new javax.swing.GroupLayout(JPadicional);
        JPadicional.setLayout(JPadicionalLayout);
        JPadicionalLayout.setHorizontalGroup(
            JPadicionalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPadicionalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPadicionalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(JPadicionalLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFbuscarIngre, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLmensajeIng)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBbuscarIngre, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        JPadicionalLayout.setVerticalGroup(
            JPadicionalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPadicionalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPadicionalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(JTFbuscarIngre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JBbuscarIngre)
                    .addComponent(JLmensajeIng))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ingrediente Adicional", JPadicional);

        JCBpaquetesPa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCBpaquetesPaActionPerformed(evt);
            }
        });

        JTablaPaquetes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int rowCol){
                return false;
            }
        };
        JTablaPaquetes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        JTablaPaquetes.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(JTablaPaquetes);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel4.setText("$");

        JLprecioPaq2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        JLprecioPaq2.setText("0");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel5.setText("$");

        JLprecioPaq1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        JLprecioPaq1.setText("0");

        jLabel7.setText("<- Precio total de productos");

        jLabel8.setText("Precio del paquete ->");

        javax.swing.GroupLayout JPpaquetesLayout = new javax.swing.GroupLayout(JPpaquetes);
        JPpaquetes.setLayout(JPpaquetesLayout);
        JPpaquetesLayout.setHorizontalGroup(
            JPpaquetesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpaquetesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPpaquetesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPpaquetesLayout.createSequentialGroup()
                        .addGroup(JPpaquetesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                            .addGroup(JPpaquetesLayout.createSequentialGroup()
                                .addComponent(JCBpaquetesPa, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JLmensajePaq)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPpaquetesLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JLprecioPaq1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JLprecioPaq2)
                        .addGap(20, 20, 20))))
        );
        JPpaquetesLayout.setVerticalGroup(
            JPpaquetesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpaquetesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPpaquetesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JCBpaquetesPa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLmensajePaq))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPpaquetesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(JLprecioPaq2)
                    .addComponent(jLabel5)
                    .addComponent(JLprecioPaq1)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Paquetes", JPpaquetes);

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Pedidos en procesos"));

        JTablaPedidosP= new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        JTablaPedidosP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(JTablaPedidosP);

        jScrollPane5.setBorder(javax.swing.BorderFactory.createTitledBorder("Ventas efectuadas"));

        JTablaVentasP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(JTablaVentasP);

        javax.swing.GroupLayout JPventaLayout = new javax.swing.GroupLayout(JPventa);
        JPventa.setLayout(JPventaLayout);
        JPventaLayout.setHorizontalGroup(
            JPventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPventaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        JPventaLayout.setVerticalGroup(
            JPventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPventaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Venta/Pedidos", JPventa);

        jLabel2.setText("Bienvenido: ");

        JLusuario.setText("XD\n");

        JLfecha.setText("jLabel5");

        JMopcionesE.setText("Opciones Empleados(a)");

        JMIclientes.setText("Clientes");
        JMopcionesE.add(JMIclientes);

        JMIconexion.setText("Verificar conexion");
        JMIconexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIconexionActionPerformed(evt);
            }
        });
        JMopcionesE.add(JMIconexion);

        JMIsalir.setText("Salir");
        JMIsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIsalirActionPerformed(evt);
            }
        });
        JMopcionesE.add(JMIsalir);

        jMenuBar1.add(JMopcionesE);

        JMopcionesA.setText("Opciones Adminstrativas");

        JMIpaquetes.setText("Config. Paquetes");
        JMIpaquetes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIpaquetesActionPerformed(evt);
            }
        });
        JMopcionesA.add(JMIpaquetes);

        JMalmacen.setText("Almacen");

        JMIproinv.setText("Config. Productos/Inventario");
        JMIproinv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMIproinvActionPerformed(evt);
            }
        });
        JMalmacen.add(JMIproinv);

        JMItipoProducto.setText("Config. Tipos de Productos");
        JMItipoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMItipoProductoActionPerformed(evt);
            }
        });
        JMalmacen.add(JMItipoProducto);

        JMopcionesA.add(JMalmacen);

        JMIadminEmpleados.setText("Admin. Empleados");
        JMopcionesA.add(JMIadminEmpleados);

        JMIhistorial.setText("Historial de ventas");
        JMopcionesA.add(JMIhistorial);

        jMenuBar1.add(JMopcionesA);

        JMayuda.setText("Ayuda");

        JMItutorial.setText("¿Como usar el programa?");
        JMayuda.add(JMItutorial);

        jMenuBar1.add(JMayuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JLusuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JLfecha)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JLusuario)
                    .addComponent(JLfecha))
                .addGap(8, 8, 8)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //Salir del programa
    private void JMIsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIsalirActionPerformed
        dispose();
    }//GEN-LAST:event_JMIsalirActionPerformed

    //verificar la conexion de la base de datos
    private void JMIconexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIconexionActionPerformed
         if(cn == null){
            JOptionPane.showMessageDialog(null, "Error en la base de datos.");
        }else{
            JOptionPane.showMessageDialog(null, "La base de datos esta conectada.");
        }
    }//GEN-LAST:event_JMIconexionActionPerformed

    //accion del boton agregar carrito
    private void JBagregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBagregarCarritoActionPerformed
        int itemS=JCBpaquetesPe.getSelectedIndex();

        if(itemS==0){
            JOptionPane.showMessageDialog(null, "No se ha seleccionado algun paquete.", "Selecciona paquete", JOptionPane.WARNING_MESSAGE);
        }else{
            agregarPaquete(itemS);
        }
    }//GEN-LAST:event_JBagregarCarritoActionPerformed

    //accion del boton buscar producto
    private void JBbuscarProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBbuscarProActionPerformed
        if(JTFbuscarPro.getText().isEmpty()){
            mostrarTodoProductos();
        }else{
            buscarProductos(JTFbuscarPro.getText(),0,modeloPro,JTablaProductos,false);
        }
    }//GEN-LAST:event_JBbuscarProActionPerformed

    //accion de la lista de paquetes
    private void JCBpaquetesPaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCBpaquetesPaActionPerformed
        int itemS=JCBpaquetesPa.getSelectedIndex();
        if(itemS!=0){
            buscarPaquetes(itemS,modeloPaq,JTablaPaquetes,true);
        }else{
            JLmensajePaq.setText("No se puede buscar la primera opcion(default)");
        }
    }//GEN-LAST:event_JCBpaquetesPaActionPerformed

    //accion del pop menu de la tabla de productos
    private void JMIproAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIproAgregarActionPerformed
        int filaS=JTablaProductos.getSelectedRow();
        
        if(filaS <= -1){
            JLmensajePro.setText("Selecciona un registro para agregarlo al carrito");
        }else{
            pasarProductoTablaPedido(filaS);
        }
    }//GEN-LAST:event_JMIproAgregarActionPerformed

    //accion para el menu item de in/pro
    private void JMIproinvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIproinvActionPerformed
        Panel_Inventario pi=new Panel_Inventario();
        pi.show();
    }//GEN-LAST:event_JMIproinvActionPerformed

    //metodo para cambio del valor en la cantidad
    private void JTablaCarritoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_JTablaCarritoPropertyChange
        if(evt.getPropertyName().equals("tableCellEditor")){
            int filaS=JTablaCarrito.getSelectedRow();
            int cant=Integer.parseInt(JTablaCarrito.getValueAt(filaS, 3).toString());
            if(cant<0){
                JOptionPane.showMessageDialog(null, "La cantidad no puede ser menor a 0","Error en la tabla",
                        JOptionPane.WARNING_MESSAGE);
                JTablaCarrito.setValueAt("1", filaS, 3);
            }else{
                String id_inv=JTablaCarrito.getValueAt(filaS, 0).toString();
                tomarPrecioArray(id_inv);
                JTablaCarrito.setValueAt(precioPro*cant, filaS, 4);
                precioTotalPedido(tomarFilasCarrito());
            }
        }
    }//GEN-LAST:event_JTablaCarritoPropertyChange

    //Quitar producto del carrito
    private void JMIpeQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIpeQuitarActionPerformed
        int filaS=JTablaProductos.getSelectedRow();
        
        if(filaS <= -1){
            JLmensajePro.setText("Selecciona un registro para quitar el producto del carrito");
        }else{
            int numFilas=JTablaCarrito.getRowCount();
            if(numFilas==1){
                modeloC.removeRow(0);
                JLprecioPed.setText("0");
            }else{
                quitarCarrito(filaS);
            }
        }
    }//GEN-LAST:event_JMIpeQuitarActionPerformed

    //accion para abrir la ventana de configuracion de paquetes
    private void JMIpaquetesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIpaquetesActionPerformed
        Panel_Paquetes pp = new Panel_Paquetes();
        pp.show();
    }//GEN-LAST:event_JMIpaquetesActionPerformed

    //accion para configurar los tipos de productos
    private void JMItipoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMItipoProductoActionPerformed
        
    }//GEN-LAST:event_JMItipoProductoActionPerformed

    //accion para poder actualizar los jcb de los paneles.
    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        if(jTabbedPane1.getSelectedIndex()==0){
            obtenerDatosJCB(true);
        }else if(jTabbedPane1.getSelectedIndex()==2){
            obtenerDatosJCB(false);
        }
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    //metodo para hacer el pedido
    private void JBingresarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBingresarProductoActionPerformed
        ArrayList<String> id_paquete= new ArrayList<>(),
                id_ingrediente= new ArrayList<>(),
                id_producto = new ArrayList<>();
        int cont_Paq=0,cont_Pro=0,cont_Ing=0;
        for(int i=0;i<JTablaCarrito.getRowCount();i++){
            String tipo_producto=JTablaCarrito.getValueAt(i, 2).toString();
            int tipo_id=verTipo(tipo_producto);
            
            switch (tipo_id) {
                case 3:
                    id_ingrediente.add(cont_Ing,JTablaCarrito.getValueAt(i, 0).toString());
                    cont_Ing++;
                break;
                case 6:
                    id_paquete.add(cont_Paq,JTablaCarrito.getValueAt(i, 0).toString());
                    cont_Paq++;
                break;
                default:
                    id_producto.add(cont_Pro,JTablaCarrito.getValueAt(i, 0).toString());
                    cont_Pro++;
                break;
            }
        }
        agregarPedido(id_ingrediente,id_paquete,id_producto);
    }//GEN-LAST:event_JBingresarProductoActionPerformed

    //accion para boton de buscar ingredientes
    private void JBbuscarIngreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBbuscarIngreActionPerformed
        if(JTFbuscarIngre.getText().isEmpty()){
            mostrarTodoIngredientes();
        }else{
            buscarIngredientes(JTFbuscarIngre.getText(), modeloIng, JTablaIngredientes);
        }
    }//GEN-LAST:event_JBbuscarIngreActionPerformed

    //accion para pasar los datos a pedidos
    private void JMIagregarIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMIagregarIActionPerformed
       int filaS=JTablaIngredientes.getSelectedRow();
        
        if(filaS <= -1){
            JLmensajeIng.setText("Selecciona un registro para agregarlo al carrito");
        }else{
            pasarIngredienteTablaPedido(filaS);
        }
    }//GEN-LAST:event_JMIagregarIActionPerformed

   public static void main(String args[]) {
 
        java.awt.EventQueue.invokeLater(() -> {
            new Panel_Acceso().setVisible(true);
        });
    }
    
 // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBagregarCarrito;
    private javax.swing.JButton JBbuscarIngre;
    private javax.swing.JButton JBbuscarPro;
    private javax.swing.JButton JBingresarProducto;
    private javax.swing.JComboBox<String> JCBpaquetesPa;
    private javax.swing.JComboBox<String> JCBpaquetesPe;
    private javax.swing.JLabel JLfecha;
    private javax.swing.JLabel JLmensajeIng;
    private javax.swing.JLabel JLmensajePaq;
    private javax.swing.JLabel JLmensajePro;
    private javax.swing.JLabel JLprecioPaq1;
    private javax.swing.JLabel JLprecioPaq2;
    private javax.swing.JLabel JLprecioPed;
    public static javax.swing.JLabel JLusuario;
    private javax.swing.JMenuItem JMIadminEmpleados;
    private javax.swing.JMenuItem JMIagregarI;
    private javax.swing.JMenuItem JMIcancelarP;
    private javax.swing.JMenuItem JMIclientes;
    private javax.swing.JMenuItem JMIcompletarP;
    private javax.swing.JMenuItem JMIconexion;
    private javax.swing.JMenuItem JMIhistorial;
    private javax.swing.JMenuItem JMIpaquetes;
    private javax.swing.JMenuItem JMIpeQuitar;
    private javax.swing.JMenuItem JMIproAgregar;
    private javax.swing.JMenuItem JMIproinv;
    private javax.swing.JMenuItem JMIsalir;
    private javax.swing.JMenuItem JMItipoProducto;
    private javax.swing.JMenuItem JMItutorial;
    private javax.swing.JMenu JMalmacen;
    private javax.swing.JMenu JMayuda;
    public static javax.swing.JMenu JMopcionesA;
    private javax.swing.JMenu JMopcionesE;
    private javax.swing.JPopupMenu JPMadicional;
    private javax.swing.JPopupMenu JPMpedidos;
    private javax.swing.JPopupMenu JPMpedidosPen;
    private javax.swing.JPopupMenu JPMproductos;
    private javax.swing.JPanel JPadicional;
    private javax.swing.JPanel JPpaquetes;
    private javax.swing.JPanel JPpedidos;
    private javax.swing.JPanel JPproductos;
    private javax.swing.JPanel JPventa;
    private javax.swing.JTextField JTFbuscarIngre;
    private javax.swing.JTextField JTFbuscarPro;
    private javax.swing.JTable JTablaCarrito;
    private javax.swing.JTable JTablaIngredientes;
    private javax.swing.JTable JTablaPaquetes;
    private javax.swing.JTable JTablaPedidosP;
    private javax.swing.JTable JTablaProductos;
    private javax.swing.JTable JTablaVentasP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

// </editor-fold>
}