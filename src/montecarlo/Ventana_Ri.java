
package montecarlo;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class Ventana_Ri extends javax.swing.JFrame {
    DefaultTableModel dm = new DefaultTableModel();
    DefaultTableModel ks = new DefaultTableModel();
    private double ri[];
    private int [] archivofinal;
    String semilla, numero, snumero3, a, m, c, tam;
    int longitudSemilla, multiplicadorLongitud, c1, moduloLongitud, i, primerc, cantidadNumeros,corridas;
    long X0, multiplicador, constante, modulo, x,x2,m3,tam2, conDivision;
    double estadisticoz, Za=1.96, promedio, sm, nm, suma;
    float media, varianza;
    DecimalFormat decimas = new DecimalFormat("#0.0000");
    String mostrar = "", mostrarC = "";
    
    public Ventana_Ri() {
        initComponents();
        setTitle("Generacion y pruebas de numeros Ri");
        setLocationRelativeTo(null);
        String[] titulo = new String []{"#", "Num.Gen"};
        dm.setColumnIdentifiers(titulo);
        jTable1.setModel(dm);
        btnGuardar.setVisible(false);
        rbPromedios.isSelected();
        rbIndep.isSelected();
    }

    public void calcular() {
        cantidadNumeros = Integer.parseInt(jtxtTamanoCM.getText());
        int[] archivo = new int[cantidadNumeros];
        ri = new double[cantidadNumeros];
        
        limpiar();

        try {
            semilla = jtxtSemillaCM.getText();
            longitudSemilla = semilla.length();
            X0 = Integer.parseInt(semilla);

            a = jtxtMultiplicadorCM.getText();
            multiplicadorLongitud = a.length();
            multiplicador = Integer.parseInt(a);

            m = jtxtModuloCM.getText();
            moduloLongitud = m.length();
            modulo = Integer.parseInt(m);

            /*VALIDADORES PARA VARIABLES GENERICAS*/
            if (longitudSemilla < 1111) {
                longitudSemilla = longitudSemilla + 1111;
            }
            if (multiplicador < 11) {
                multiplicador = multiplicador + 11;
            }
            if (modulo < 11) {
                modulo = modulo + 313;
            }
            while (X0 % 2 == 0) {
                X0 = X0 + 1;
            }
            while (multiplicador % 2 == 0) {
                multiplicador = multiplicador + 1;
            }
            while (modulo % 2 == 0) {
                modulo = modulo + 1;
            }
            if (multiplicador % 1 != 0) {
                multiplicador = 33;
            }

            /*VALIDADORES PARA VARIABLES GENERICAS*/
            /*OPERACIONES DE METODO*/
            constante = (8 * multiplicador) + 3;
            for (i = 0; i < cantidadNumeros; i++) {
                x = ((X0 * multiplicador) + constante) % modulo;
                X0 = x;
                archivo[i] = (int) x;
                ri[i] = x / 1000;
                dm.addRow(new Object[]{i + 1, (double) (archivo[i] / 1000.00)});
                sm = sm + x; 
            }
            suma = sm / 1000;
            /*OPERACIONES DE METODO*/
            /*MANDAR ARCHIVO A ARRAY*/
            archivofinal = archivo;
            
            /*-----------COMPROBAR NUMS REPETIDOS------------*/
            int conth = 0;
            Map<Integer, Integer> numrep = new HashMap<Integer, Integer>();

            for (Integer str : archivofinal) {
                if (numrep.containsKey(str)) {
                    numrep.put(str, numrep.get(str) + 1);
                } else {
                    numrep.put(str, 1);
                }
            }
            for (Map.Entry<Integer, Integer> entry : numrep.entrySet()) {
                if (entry.getValue() > 1) {
                    ta_numrep.setLineWrap(true);
                    ta_numrep.setText(ta_numrep.getText() + "Num: " + entry.getKey() + " se repite: " + entry.getValue() + " veces. \n");
                    if (entry.getValue() > conth) {
                        conth = entry.getValue();
                    }
                }
            }
            /*------------------------FIN DE CONTAR NUMS REP---------------------------------*/
        }catch(Exception e){
            
        }
    }
   
    public void limpiar (){
        int fila = dm.getRowCount();
        if (fila !=0){
            for(int l = 0; l< fila ; l++ ){
                dm.removeRow(0);  
            }
        }
    }
    
    private void guardarArchivo() {
        try {
            String nombre = "";
            JFileChooser file = new JFileChooser();
            file.showSaveDialog(this);
            File guarda = file.getSelectedFile();

            if (guarda != null) {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(guarda + ".dat"))) {
                    out.writeObject(archivofinal);
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
        limpiar();
        String aux = "";
        String texto = "";
        try {
            JFileChooser file = new JFileChooser();
            file.showOpenDialog(this);
            File abre = file.getSelectedFile();

            if (abre != null) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(abre));
                String[] arch_leer = (String[]) in.readObject();
                int c = 0;
                for (String item : arch_leer) {
                    System.out.println(item);
                    c = c + 1;
                    dm.addRow(new Object[]{c, item});
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex + ""
                    + "\nNo se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
        }
        return null;

    }
    
    public boolean pruebaPromedios(double confianza) {
        boolean aceptado;
        DistribucionNormal intervalo = new DistribucionNormal();
        intervalo.setIntervaloCerteza(confianza);
                
        mostrar += "Suma = " + suma + "\r\n";
        mostrar += "Media = " + suma / cantidadNumeros + "\r\n";
        double Zo = (((suma/cantidadNumeros) - 0.5) * Math.sqrt(cantidadNumeros))/Math.sqrt(1.0 / 12.0);
        mostrar += "Alfa/2=" + intervalo.getAlfaMedios() + "\r\n";
        mostrar += "Zo=" + (Zo) + "\r\n";
        if (Zo < intervalo.getLimiteSuperior()) {
            mostrar += "Se acepta";
            aceptado = true;
        } else {
            mostrar += "Se rechaza";
            aceptado = false;
        }
        mostrar += " la hipotesis debido a que Zo < " + intervalo.getLimiteSuperior() + " \r\n";
        return aceptado;
    }
    
    public boolean pruebaCorridas(double numeros[]){
        boolean aceptado;
        double aux[];
        int  z=0 ;
        double corrida[];
        double valorM,Varianza,Z0,a=0,Za=-1.96,Za1=1.96;
        aux = new double[numeros.length];
        corrida = new double[numeros.length-1];
        for (int i = 0; i < numeros.length; i++) {
            aux[i] = numeros[i];
        }
        
        for(int j=0; j< numeros.length-1;j++){
            for (int k = 1; k < aux.length-1; k++) {
                if(aux[k]>numeros[j]){
                    corrida[z]=1;
                    z++;
                }else{
                    corrida[z]=0;
                    z++;
                }
                j++;
            }
        }
       
        for (int k = 0; k < corrida.length-1; k++) {
            if(corrida[k]!=corrida[k+1]){ 
                a++;
            }
        }
        
        mostrarC += "Numero de Corridas=" + a + "\r\n";
        
        valorM = suma / cantidadNumeros;
        mostrarC += "Media=" + (valorM) + "\r\n";
        
        Varianza = (16*(numeros.length)-29)/90.0;
        mostrarC += "Varianza=" + (Varianza) + "\r\n";
        
        Z0= (a - valorM)/Math.sqrt(Varianza);
        mostrarC += "Zo=" + (Z0) + "\r\n";
        if(Za<Z0 && Z0<Za1){
           mostrarC += "Se acepta";
            aceptado = true;
        } else {
            mostrarC += "Se rechaza";
            aceptado = false;
        }
        mostrarC += " la independencia de los datos";
        return aceptado;
    }
    
    public static double getDecimal(int numeroDecimales, double decimal) {
        decimal = decimal * (java.lang.Math.pow(10, numeroDecimales));
        decimal = java.lang.Math.round(decimal);
        decimal = decimal / java.lang.Math.pow(10, numeroDecimales);
        return decimal;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtSemillaCM = new javax.swing.JTextField();
        jtxtModuloCM = new javax.swing.JTextField();
        btnCalcular = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jtxtTamanoCM = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtMultiplicadorCM = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        ta_numrep = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        rbPromedios = new javax.swing.JRadioButton();
        rbIndep = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        txtConfianza = new javax.swing.JTextField();
        btnProbar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txA = new javax.swing.JTextArea();
        btnLimpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(247, 247, 247));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel1.setText("METODO CONGRUENCIAL MIXTO");

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel2.setText("SEMILLA:");

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel5.setText("MODULO:");

        btnCalcular.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        btnCalcular.setText("GENERAR");
        btnCalcular.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jButton3.setText("ABRIR");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "X(n+1)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel6.setText("# NUMEROS");

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel4.setText("MULTIPLICADOR:");

        ta_numrep.setEditable(false);
        ta_numrep.setColumns(20);
        ta_numrep.setRows(5);
        jScrollPane2.setViewportView(ta_numrep);

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel7.setText("PRUBEAS UNIFORMIDAD E INDEPENDENCIA");

        jPanel2.setBackground(new java.awt.Color(247, 247, 247));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel8.setText("PRUEBAS:");

        rbPromedios.setBackground(new java.awt.Color(247, 247, 247));
        rbPromedios.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        rbPromedios.setSelected(true);
        rbPromedios.setText("Uniformidad");
        rbPromedios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPromediosActionPerformed(evt);
            }
        });

        rbIndep.setBackground(new java.awt.Color(247, 247, 247));
        rbIndep.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        rbIndep.setSelected(true);
        rbIndep.setText("Independencia");
        rbIndep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbIndepActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        jLabel9.setText("% confianza");

        txtConfianza.setText("95");
        txtConfianza.setEnabled(false);

        btnProbar.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        btnProbar.setText("Realizar Prueba");
        btnProbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProbarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(rbPromedios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(rbIndep)
                .addGap(28, 28, 28))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel8))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtConfianza, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(btnProbar, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPromedios)
                    .addComponent(rbIndep))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtConfianza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(btnProbar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txA.setColumns(20);
        txA.setRows(5);
        jScrollPane3.setViewportView(txA);

        btnLimpiar.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        btnLimpiar.setText("Reset");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jtxtSemillaCM, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jtxtMultiplicadorCM, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jtxtModuloCM, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jtxtTamanoCM, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(78, 78, 78)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnLimpiar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(76, 76, 76))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(186, 186, 186)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(212, 212, 212)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtSemillaCM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtMultiplicadorCM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(btnCalcular)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtModuloCM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtxtTamanoCM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(35, 35, 35)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                .addGap(39, 39, 39)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnLimpiar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProbarActionPerformed
        double confianza = Double.parseDouble(txtConfianza.getText());
        txA.setText(mostrar + "\n\n" + mostrarC);
        if (pruebaPromedios(confianza) == true && pruebaCorridas(ri) == true) {
            btnGuardar.setVisible(true);
        }else{
            btnGuardar.setVisible(false);
        }
    }//GEN-LAST:event_btnProbarActionPerformed

    private void rbIndepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbIndepActionPerformed
        rbPromedios.setSelected(false);
    }//GEN-LAST:event_rbIndepActionPerformed

    private void rbPromediosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPromediosActionPerformed
        rbIndep.setSelected(false);
    }//GEN-LAST:event_rbPromediosActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            abrirArchivo();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Ventana_Ri.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarArchivo();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        calcular();
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        Ventana_Ri vr = new Ventana_Ri();
        vr.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnLimpiarActionPerformed

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
            java.util.logging.Logger.getLogger(Ventana_Ri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana_Ri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana_Ri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana_Ri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana_Ri().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnProbar;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jtxtModuloCM;
    private javax.swing.JTextField jtxtMultiplicadorCM;
    private javax.swing.JTextField jtxtSemillaCM;
    private javax.swing.JTextField jtxtTamanoCM;
    private javax.swing.JRadioButton rbIndep;
    private javax.swing.JRadioButton rbPromedios;
    private javax.swing.JTextArea ta_numrep;
    private javax.swing.JTextArea txA;
    private javax.swing.JTextField txtConfianza;
    // End of variables declaration//GEN-END:variables
}
