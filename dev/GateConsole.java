package dev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;


/**
 * An interface to SAAMS:
 * dev.Gate Control Console:
 * Inputs events from gate staff, and displays aircraft and gate information.
 * This class is a controller for the dev.GateInfoDatabase and the dev.AircraftManagementDatabase: sends messages when aircraft dock, have finished disembarking, and are fully emarked and ready to depart.
 * This class also registers as an observer of the dev.GateInfoDatabase and the
 * dev.AircraftManagementDatabase, and is notified whenever any change occurs in those <<model>> elements.
 * See written documentation.
 *
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:view:::id1un8dcko4qme4cko4sw27
 * @url element://model:project::SAAMS/design:view:::id1jkohcko4qme4cko4svww
 * @url element://model:project::SAAMS/design:node:::id1un8dcko4qme4cko4sw27.node61
 */
public class GateConsole extends JFrame implements Observer, ActionListener {


    private final static String GATE = "dev.Gate";
    private final JLabel gateStatusLabel = new JLabel("dev.Gate Status");
    private final JLabel planeStatusLabel = new JLabel("Plane Status");
    private final JLabel flightCodeLabel = new JLabel("Flight Code");
    private final JLabel flightFromLabel = new JLabel("Flight From");
    private final JLabel flightToLabel = new JLabel("Flight To");
    private final JLabel nextStopLabel = new JLabel("Next Stop");
    private final JLabel noOfPassengersLabel = new JLabel("No. of Passengers");
    private final JLabel passengerNameLabel = new JLabel("Passenger Name");

    private final JTextField gateStatusTF = new JTextField("");
    private final JTextField planeStatusTF = new JTextField("");
    private final JTextField flightCodeTF = new JTextField("");
    private final JTextField flightFromTF = new JTextField("");
    private final JTextField flightToTF = new JTextField("");
    private final JTextField nextStopTF = new JTextField("");
    private final JTextField noOfPassengersTF = new JTextField("");
    private final JTextField passengerNameTF = new JTextField("");

    private final JButton planeDockedBtn = new JButton("Plane Docked");
    private final JButton planeUnloadedBtn = new JButton("Plane Unloaded");
    private final JButton flightReadyToDepartBtn = new JButton("Flight Ready To Depart");
    private final JButton addPassengerBtn = new JButton("Add Passenger");

    private DefaultListModel<String> passengerDefaultList = new DefaultListModel<>();
    private final JList passengerList = new JList(passengerDefaultList);
    private JScrollPane passengerListScroll;
    /**
     * The dev.GateConsole interface has access to the dev.GateInfoDatabase.
     *
     * @supplierCardinality 1
     * @clientCardinality 0..*
     * @label accesses/observes
     * @directed
     */
    GateInfoDatabase gateInfoDatabase;

    /**
     * The dev.GateConsole interface has access to the dev.AircraftManagementDatabase.
     *
     * @supplierCardinality 1
     * @clientCardinality 0..*
     * @directed
     * @label accesses/observes
     */
    private final AircraftManagementDatabase aMDatabase;

    private int mCode;

    /**
     * This gate's gateNumber
     * - for identifying this gate's information in the dev.GateInfoDatabase.
     */
    private final int gateNumber;

    //TODO : CHANGE FROM FINDFROMFLIGHTCODE TO USE THE INDEX FOR THE PLAIN

    public GateConsole(AircraftManagementDatabase aircraftManagementDatabase, GateInfoDatabase gateInfoDatabase,
                       int gateNumber) {
        super(GATE);
        this.aMDatabase = aircraftManagementDatabase;
        this.gateInfoDatabase = gateInfoDatabase;
        this.gateNumber = gateNumber - 1;
        this.aMDatabase.addObserver(this);
        this.gateInfoDatabase.addObserver(this);
        initiateGUI();
        createLabels();
        createButtons();
        createTextFields();
        setVisible(true);
    }

    public void createLabels() {
        gateStatusLabel.setBounds(180, 20, 150, 20);
        add(gateStatusLabel);
        planeStatusLabel.setBounds(180, 50, 100, 20);
        add(planeStatusLabel);
        flightCodeLabel.setBounds(180, 80, 100, 20);
        add(flightCodeLabel);
        flightFromLabel.setBounds(180, 110, 100, 20);
        add(flightFromLabel);
        flightToLabel.setBounds(180, 140, 100, 20);
        add(flightToLabel);
        nextStopLabel.setBounds(180, 170, 150, 20);
        add(nextStopLabel);
        noOfPassengersLabel.setBounds(180, 200, 150, 20);
        add(noOfPassengersLabel);
        passengerNameLabel.setBounds(180, 230, 150, 20);
        add(passengerNameLabel);
    }

    public void createButtons() {
        planeDockedBtn.addActionListener(this);
        planeDockedBtn.setBounds(220, 260, 200, 30);
        add(planeDockedBtn);
        planeUnloadedBtn.addActionListener(this);
        planeUnloadedBtn.setBounds(220, 290, 200, 30);
        add(planeUnloadedBtn);
        flightReadyToDepartBtn.addActionListener(this);
        flightReadyToDepartBtn.setBounds(220, 320, 200, 30);
        add(flightReadyToDepartBtn);
        addPassengerBtn.addActionListener(this);
        addPassengerBtn.setBounds(220, 350, 200, 30);
        add(addPassengerBtn);
    }

    public void createTextFields() {
        passengerListScroll = new JScrollPane(passengerList);
        passengerListScroll.setBounds(5, 20, 160, 300);
        add(passengerListScroll);
        gateStatusTF.setEditable(false);
        gateStatusTF.setBounds(300, 20, 180, 25);
        add(gateStatusTF);
        planeStatusTF.setEditable(false);
        planeStatusTF.setBounds(300, 50, 180, 25);
        add(planeStatusTF);
        flightCodeTF.setEditable(false);
        flightCodeTF.setBounds(300, 80, 180, 25);
        add(flightCodeTF);
        flightFromTF.setEditable(false);
        flightFromTF.setBounds(300, 110, 180, 25);
        add(flightFromTF);
        flightToTF.setEditable(false);
        flightToTF.setBounds(300, 140, 180, 25);
        add(flightToTF);
        nextStopTF.setEditable(false);
        nextStopTF.setBounds(300, 170, 180, 25);
        add(nextStopTF);
        noOfPassengersTF.setEditable(false);
        noOfPassengersTF.setBounds(300, 200, 180, 25);
        add(noOfPassengersTF);
        passengerNameTF.setEditable(true);
        passengerNameTF.setBounds(300, 230, 180, 25);
        add(passengerNameTF);
    }

    public void initiateGUI() {
        setLayout(null);
        setTitle("Gate " + (gateNumber + 1));
        setBackground(Color.CYAN);
        int offset = 40 * gateNumber;
        setLocation(0, (500 + offset));
        setSize(550, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public ManagementRecord plainIndexFromFlightCode() {
        if ((flightCodeTF.getText()) != null) {
            return aMDatabase.findMrFromFlightCode(flightCodeTF.getText());
        }
        return null;
    }

    public void planeIndexForGate() {
        for (int i = 0; i < aMDatabase.maxMRs; i++) {
            if (aMDatabase.getStatus(i) >= ManagementRecord.TAXIING
                    && aMDatabase.getStatus(i) < ManagementRecord.AWAITING_TAKEOFF
                    && aMDatabase.getGate(i) == this.gateNumber) {
                mCode = i;
                displayInfoOnGate();
            }
            if (aMDatabase.getStatus(i) >= ManagementRecord.AWAITING_TAKEOFF
                    && aMDatabase.getGate(i) == this.gateNumber) {
                removeInfoOnGate();
            }
        }
    }

    public void displayInfoOnGate() {
        removeInfoOnGate();
        gateStatusTF.setText(gateInfoDatabase.statusToText(gateInfoDatabase.getStatus(gateNumber)));
        planeStatusTF.setText(aMDatabase.statusAsText(aMDatabase.getStatus(mCode)));
        flightCodeTF.setText(aMDatabase.getFlightCode(mCode));
        flightFromTF.setText(aMDatabase.getItinerary(mCode).getFrom());
        flightToTF.setText(aMDatabase.getItinerary(mCode).getTo());
        nextStopTF.setText(aMDatabase.getItinerary(mCode).getNext());
        noOfPassengersTF.setText(aMDatabase.getPassengerList(mCode).getListLength() + "");
        displayPassengers();
    }

    public void removeInfoOnGate() {
        gateStatusTF.setText("");
        planeStatusTF.setText("");
        flightCodeTF.setText("");
        flightFromTF.setText("");
        flightToTF.setText("");
        nextStopTF.setText("");
        noOfPassengersTF.setText("");
        passengerNameTF.setText("");
    }

    @Override
    public void update(Observable o, Object arg) {
        planeIndexForGate();
    }

    private void displayPassengers() {
        ManagementRecord managementRecord = plainIndexFromFlightCode();
        passengerDefaultList.removeAllElements();
        for (int i = 0; i < managementRecord.getPassengerList().getListLength(); i++) {
            passengerDefaultList.addElement(managementRecord.getPassengerList().getElement(i).getName());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == planeDockedBtn) {
            if (gateInfoDatabase.getStatus(gateNumber) == 1) {
                gateInfoDatabase.docked(gateNumber);
                aMDatabase.setStatus(mCode, ManagementRecord.UNLOADING);
                displayInfoOnGate();
            } else {
                JOptionPane.showMessageDialog(this, "Gate must be reserved through ground controller panel.");
            }
            return;
        }

        if (e.getSource() == planeUnloadedBtn) {
            if (gateInfoDatabase.getStatus(gateNumber) == 2) {
                aMDatabase.setStatus(mCode, ManagementRecord.READY_CLEAN_AND_MAINT);
                aMDatabase.getPassengerList(mCode).setListToEmpty();
                displayPassengers();
                displayInfoOnGate();
            } else {
                JOptionPane.showMessageDialog(this, "Gate must be occupied, "
                        + "press the docked button after gate has been reserved through ground controller.");
            }
            return;
        }

        if (e.getSource() == addPassengerBtn) {
            if (flightCodeTF.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "There are no planes at this gate yet, consult ground controller.");
            } else {
                if (plainIndexFromFlightCode() != null) {
                    if (passengerNameTF.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Passenger name cannot be empty.");
                    } else {
                        plainIndexFromFlightCode().addPassenger(new PassengerDetails(passengerNameTF.getText()));
                        passengerNameTF.setText("");
                        displayPassengers();
                        noOfPassengersTF.setText(aMDatabase.getPassengerList(mCode).getListLength() + "");
                    }
                }
            }
            return;
        }
        if (e.getSource() == flightReadyToDepartBtn) {
            System.out.println(mCode);
            if (plainIndexFromFlightCode() != null) {
                aMDatabase.setStatus(mCode, ManagementRecord.READY_DEPART);
            } else {
                JOptionPane.showMessageDialog(this, "Gate must be occupied and plane must be refueled.");
            }
            return;
        }
    }
}