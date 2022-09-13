package montecarlo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Ventana_Xi extends javax.swing.JFrame {
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableModel aleatorios;
    DefaultTableModel calculos = new DefaultTableModel();
    String[] tituloXi = new String []{"Xi #", "Xi"};
    private int contador_ri,cc=0;
    private double media, suma = 0;
    private int[] ri_vec;
    private double[] xi_vec;
    private double[] xi_abs;
    private double[] ri;
    private boolean clicked = false;
    DecimalFormat decimas = new DecimalFormat("#0.0000");
    String mostrar ="";
    ChiCuadrada ch = new ChiCuadrada();
    
    public Ventana_Xi() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Generacion y pruebas de numeros Xi");
        
        String[] tituloRi = new String []{"Ri #", "Ri"};
        modelo.setColumnIdentifiers(tituloRi);
        tblRi.setModel(modelo);
        
        cmbDistribucion.setVisible(true);
        btnCalcular.setVisible(false);
        btnGuardarXi.setVisible(false);
    }
    
    private void guardarArchivo() {
        try {
            String nombre = "";
            JFileChooser file = new JFileChooser();
            file.showSaveDialog(this);
            File guarda = file.getSelectedFile();

            if (guarda != null) {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(guarda + ".dat"))) {
                    out.writeObject(xi_abs);
                    JOptionPane.showMessageDialog(null, "El archivo se a guardado Exitosamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Su archivo no se ha guardado",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private String[] abrirArchivo() throws ClassNotFoundException {
        
        try {
            JFileChooser file = new JFileChooser();
            file.showOpenDialog(this);
            File abre = file.getSelectedFile();

            if (abre != null) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(abre));
                int[] arch_leer = (int[]) in.readObject();
                int c = 0;

                // LEE DE ARCHIVO Y CUENTA LOS NUMEROS
                for (Integer item : arch_leer) {
                    c = c + 1;
                }
                contador_ri = c;
                
                
                int cantidad = Integer.parseInt(JOptionPane.showInputDialog("TOTAL: " + contador_ri + "\n Ingrese cantidad de numeros a usar: "));
                while (cantidad > contador_ri || cantidad < 2) {
                    JOptionPane.showMessageDialog(null, "Tiene que ser menor o igual a " + contador_ri + " ó mayor a 2", "Error", JOptionPane.ERROR_MESSAGE);
                    cantidad = Integer.parseInt(JOptionPane.showInputDialog("TOTAL: " + contador_ri + "\n Ingrese cantidad de numeros a usar: "));
                }
                
                //ORDENACION DE NUMEROS
                //Arrays.sort(arch_leer);
                
                ri_vec = new int[cantidad];
                ri = new double[cantidad];
                for (int l = 0; l < cantidad; l++) {
                    cc = cc + 1;
                    ri_vec[l] = (arch_leer[l]);
                    ri[l] = ((double) ri_vec[l] / 1000);
                    modelo.addRow(new Object[]{cc, ri[l]});
                    suma = ri[l] + suma;
                    //System.out.println("la suma es: "+ suma);
                }

                
                in.close();
                btnCalcular.setVisible(true);
                btnAgr.setEnabled(false);
                
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex + ""
                    + "\n No se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
        }
        return null;

    }
    
    //Metodos CHI CUADRADA
    private double generarMinimo(){
        double minimo = xi_vec[0];
        for (int i = 0; i < xi_vec.length; i++) {
            if (xi_vec[i] <= minimo) {
                minimo = xi_vec[i];
            }
        }
        return minimo;
    }
    
    private double generarMaximo(){
        double maximo = xi_vec[0];
        for (int i = 0; i < xi_vec.length; i++) {
            if (xi_vec[i] >= maximo) {
                maximo = xi_vec[i];
            }
        }
        return maximo;
    }
    
    private double tamañoIntervalo(){
        return (generarRango() / generarNumIntervalo());
    }
    
    private double generarRango(){
        return Double.parseDouble(decimas.format(generarMaximo() - generarMinimo()));
    }
    
    private double generarNumIntervalo(){
        double numero = 0, redondeo = 0;
        numero = (1 + 3.322 * Math.log10(xi_vec.length));
        redondeo = Math.round(numero);
        return redondeo;
    }
    
    private int frecuenciaObservada(double limInferior, double limSuperior){
        int frecuencia = 0;
        for (int i = 0; i < xi_vec.length; i++) {
            if(limInferior <= xi_vec[i] && xi_vec[i] <= limSuperior){
                frecuencia = frecuencia + 1;
            }
        }
        return frecuencia;
    }
    
    private double frecuenciaEsperada(){
        double resultado = 0;
        resultado = (xi_vec.length) / generarNumIntervalo();
        return resultado;
    }
    
    private double chi2(double frecuenciaO, double frecuenciaE){
        double chi = 0, resta = 0;
        resta = frecuenciaO - frecuenciaE;
        chi = (resta * resta) / frecuenciaE;
        return chi;
    }
    
    private double desviacionEstandar(){
        double desviacion = 0, sumatoria = 0, operacion = 0, division = 0;
        media = suma / xi_vec.length;
        for (int i = 0; i < xi_vec.length; i++) {
            operacion = Math.pow(xi_vec[i] - media, 2);
            sumatoria = sumatoria + operacion;
        }
        division = sumatoria / (xi_vec.length - 1);
        desviacion = Math.sqrt(division);
        return desviacion;
    }
    
    public static double Redondear (double numero){
        return Math.round(numero * Math.pow(10, 1))/Math.pow(10, 1);
    }
       
    private void pruebaChiCuadrada() throws Exception{
        String[] titulo = new String []{"Clases", "Lim. Inferior", "Lim. Superior", "F. Observada", "F. Esperada", "Xi^2"};
        calculos.setColumnIdentifiers(titulo);
        int contador = 0, frecuenciaObservada = 0;
        double limInferior = 0, limSuperior = 0, frecuenciaEsperada = 0, chi2 = 0, chiTotal = 0;
                
        for (int i = 0; i < generarNumIntervalo(); i++) {
            contador = contador + 1;
            if (i == 0) {
                limInferior = Double.parseDouble(decimas.format(generarMinimo()));
                limSuperior = Double.parseDouble(decimas.format(generarMinimo() + tamañoIntervalo()));
                
                frecuenciaObservada = frecuenciaObservada(limInferior, limSuperior);
                
                frecuenciaEsperada = frecuenciaEsperada();
                
                chi2 = chi2(frecuenciaObservada, frecuenciaEsperada);
                
                chiTotal = chiTotal + chi2;
                               
                calculos.addRow(new Object[]{contador, limInferior, limSuperior, frecuenciaObservada, decimas.format(frecuenciaEsperada), decimas.format(chi2)});
            }else if(i >= 1){
                limInferior = limSuperior;
                limSuperior = Double.parseDouble(decimas.format(limInferior + tamañoIntervalo()));
                
                frecuenciaObservada = frecuenciaObservada(limInferior, limSuperior);
                
                frecuenciaEsperada = frecuenciaEsperada();
                
                chi2 = chi2(frecuenciaObservada, frecuenciaEsperada);
                
                chiTotal = chiTotal + chi2;
                
                calculos.addRow(new Object[]{contador, limInferior, limSuperior, frecuenciaObservada, decimas.format(frecuenciaEsperada), decimas.format(chi2)});
            }        
        }
        tblCalculos.setModel(calculos);
        txtD.setText(" " + decimas.format(chiTotal));
        int grados = (int) (generarNumIntervalo() -1 );
        double chiTabla = ch.ObtenerChi(0.05, grados);
        txtDTabla.setText(" " + decimas.format(chiTabla));
        if((chiTotal) < chiTabla){
            txtHipotesis.setText("Como " + decimas.format(chiTotal) + " < " + chiTabla + " \nSe acepta la hipotesis nula");
            btnGuardarXi.setVisible(true);
        }else{
            txtHipotesis.setText("Como " + decimas.format(chiTotal) + " no es < " + chiTabla + " \nSe rechaza la hipotesis nula");
            btnGuardarXi.setVisible(false);
        }
    }
    
    private void distribucionTriangular(double[] numeros){
        
        xi_vec = new double[numeros.length];
        xi_abs = new double[numeros.length];
        
        aleatorios = new DefaultTableModel();
        aleatorios.setColumnIdentifiers(tituloXi);
        
        double x = 0; 

        double division = 0;
        double raiz = 0;
        int contador = 0;
        double a = 0.00;
        double b = 182.00;
        double c = 365.00;
        
        for (int i = 0; i < numeros.length; i++){
            contador = contador + 1;
            if(0 <= numeros[i] && numeros[i] <= 0.6){
                raiz = Math.sqrt(6 * numeros[i]);
                x = raiz;
                aleatorios.addRow(new Object[]{contador, decimas.format(x)});
                xi_vec[i] = Double.parseDouble(decimas.format(x));
                xi_abs[i] = xi_vec[i];
            }
            if(0.6 <= numeros[i] && numeros[i] <= 1){
                raiz = Math.sqrt(8 - ((numeros[i])/6));
                x = 4 + raiz;
                aleatorios.addRow(new Object[]{contador, decimas.format(x)});
                xi_vec[i] = Double.parseDouble(decimas.format(x));
                xi_abs[i] = Math.abs(xi_vec[i]);
            }
        }
        tblXi.setModel(aleatorios);
        JOptionPane.showMessageDialog(null, "Numeros Xi generados");
    }
       
    private void distribucionNormal(double[] numeros){
        JOptionPane.showMessageDialog(null, "Recomendable para mas de 100 Ri´s", "Advertencia",JOptionPane.WARNING_MESSAGE);
        xi_vec = new double[numeros.length];
        xi_abs = new double[numeros.length];
        aleatorios = new DefaultTableModel();
        aleatorios.setColumnIdentifiers(tituloXi);
        double sumatoria = 0, desviacionEstandar = 0, resultado = 0;
        double raiz = Math.sqrt(numeros.length / (double)12);
        double division = numeros.length / 2;
        double x = 0;
        int contador = 0;
        media = suma / (double) numeros.length;
        for (int i = 0; i < numeros.length; i++) {
            contador = contador + 1;
            resultado = (Math.abs(numeros[i] - media)) * (Math.abs(numeros[i] - media));
            sumatoria = sumatoria + resultado;
            desviacionEstandar = sumatoria/numeros.length;
            
            x = media + desviacionEstandar * ((numeros[i] - division) / raiz);
            aleatorios.addRow(new Object[]{contador, decimas.format(x)});
            xi_vec[i] = Double.parseDouble(decimas.format(x));
            xi_abs[i] = Math.abs(xi_vec[i]);
        }
        tblXi.setModel(aleatorios);
        JOptionPane.showMessageDialog(null, "Numeros Xi generados");
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRi = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblXi = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbDistribucion = new javax.swing.JComboBox<>();
        btnCalcular = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnAgr = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCalculos = new javax.swing.JTable();
        lblD = new javax.swing.JLabel();
        txtD = new javax.swing.JTextField();
        lblDtabla = new javax.swing.JLabel();
        txtDTabla = new javax.swing.JTextField();
        btnGuardarXi = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtHipotesis = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        menu_archivo = new javax.swing.JMenu();
        menu_abrir = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenu2 = new javax.swing.JMenu();

        setTitle("SIMULADORES - ITC");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(249, 249, 249));

        tblRi.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        tblRi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ri #", "Ri"
            }
        ));
        jScrollPane2.setViewportView(tblRi);

        tblXi.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        tblXi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Xi #", "Xi"
            }
        ));
        jScrollPane3.setViewportView(tblXi);

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        jLabel2.setText("Numeros Archivo");

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        jLabel3.setText("Numeros ");

        cmbDistribucion.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        cmbDistribucion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elige Una Distribucion...", "Distribucion Triangular ", "Distribucion Normal" }));
        cmbDistribucion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnCalcular.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        btnCalcular.setText("Calcular Numeros Xi");
        btnCalcular.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N

        btnAgr.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        btnAgr.setText("Agregar RI");
        btnAgr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgrActionPerformed(evt);
            }
        });

        jToggleButton1.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        jToggleButton1.setText("Reset");
        jToggleButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        jLabel5.setText("Calculos Prueba de Bondad");

        tblCalculos.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        tblCalculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblCalculos);

        lblD.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        lblD.setText("Valor de Chi^2");

        txtD.setEditable(false);

        lblDtabla.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        lblDtabla.setText("Valor de Chi^2 Tabla");

        txtDTabla.setEditable(false);

        btnGuardarXi.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        btnGuardarXi.setText("Guardar Numeros Xi");
        btnGuardarXi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarXi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarXiActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 11)); // NOI18N
        jLabel6.setText("Metodo de la Transformada Inversa");

        txtHipotesis.setColumns(20);
        txtHipotesis.setRows(5);
        jScrollPane4.setViewportView(txtHipotesis);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCalcular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbDistribucion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAgr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton1))
                    .addComponent(btnGuardarXi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(jLabel2)
                        .addGap(132, 132, 132)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(224, 224, 224)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(lblD))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtD, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(lblDtabla)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(40, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(cmbDistribucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardarXi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgr)
                            .addComponent(jToggleButton1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(lblD)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(44, 44, 44)
                                        .addComponent(lblDtabla)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jScrollPane4))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        menu_archivo.setText("Archivo");
        menu_archivo.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N

        menu_abrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        menu_abrir.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        menu_abrir.setText("Abrir");
        menu_abrir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menu_abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_abrirActionPerformed(evt);
            }
        });
        menu_archivo.add(menu_abrir);
        menu_archivo.add(jSeparator1);

        jMenuBar1.add(menu_archivo);

        jMenu2.setText("Guia");
        jMenu2.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menu_abrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_abrirActionPerformed
         try {
            abrirArchivo();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Ventana_Xi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menu_abrirActionPerformed

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
       int distribucion = cmbDistribucion.getSelectedIndex();
       switch(distribucion){
            case 1:
                distribucionTriangular(ri);
                
                break;
            case 2:
                distribucionNormal(ri);
                break;
       }
        try {
            pruebaChiCuadrada();
        } catch (Exception ex) {
            Logger.getLogger(Ventana_Xi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void btnAgrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgrActionPerformed
       int rians = Integer.parseInt( JOptionPane.showInputDialog("Ingrese cuantos valores agregara:"));
       ri_vec = new int[rians];
       int i = 0;
       while(i<rians){
       int ans = Integer.parseInt( JOptionPane.showInputDialog("Ingrese valor:"));
       cc = cc + 1;
       ri_vec[cc-1]=ans;
       i++;
       }
       
       ri = new double[rians];
                for (int l = 0; l < rians; l++) {
                    ri[l] = ((double) ri_vec[l] / 1000);
                    modelo.addRow(new Object[]{l+1, ri[l]});
                    suma = ri[l] + suma;
                    System.out.println("la suma es: "+ suma);
                }
                
       menu_abrir.setEnabled(false);
       btnCalcular.setVisible(true);
       
    }//GEN-LAST:event_btnAgrActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
       Ventana_Xi v = new Ventana_Xi();
       v.setVisible(true);
       dispose();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void btnGuardarXiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarXiActionPerformed
        guardarArchivo();
    }//GEN-LAST:event_btnGuardarXiActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana_Xi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana_Xi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana_Xi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana_Xi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana_Xi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgr;
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnGuardarXi;
    private javax.swing.JComboBox<String> cmbDistribucion;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lblD;
    private javax.swing.JLabel lblDtabla;
    private javax.swing.JMenuItem menu_abrir;
    private javax.swing.JMenu menu_archivo;
    private javax.swing.JTable tblCalculos;
    private javax.swing.JTable tblRi;
    private javax.swing.JTable tblXi;
    private javax.swing.JTextField txtD;
    private javax.swing.JTextField txtDTabla;
    private javax.swing.JTextArea txtHipotesis;
    // End of variables declaration//GEN-END:variables
}
