package es.unizar.tecnodiscap.osgi4ami.simulator.gui.panels.actuators;

import es.unizar.tecnodiscap.osgi4ami.device.Device;
import es.unizar.tecnodiscap.osgi4ami.device.actuator.Actuator;
import es.unizar.tecnodiscap.osgi4ami.device.actuator.GetStatusActuatorCluster;
import es.unizar.tecnodiscap.osgi4ami.simulator.gui.panels.UpdateDataDevicePanel;
import javax.swing.JPanel;

/**
 *
 * @author joaquin
 */
public class JShutterActuator extends JPanel implements UpdateDataDevicePanel {

    /** Creates new form JShutterActuator */
    public JShutterActuator() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblestado = new javax.swing.JLabel();
        setOpaque(false);
        lblestado.setFont(new java.awt.Font("Dialog", 1, 48));
        lblestado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblestado.setText("shutterA");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblestado, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblestado, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE).addContainerGap()));
    }

    private javax.swing.JLabel lblestado;

    @Override
    public void UpdateData(Device sender) {
        lblestado.setText((String) ((GetStatusActuatorCluster) sender).getActuatorStatus().get(Actuator.KEY_STATUS));
    }
}