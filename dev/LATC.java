package dev;
// Generated by Together


import dev.AircraftManagementDatabase;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * An interface to SAAMS:
 * Local Air Traffic Controller Screen:
 * Inputs events from LATC (a person), and displays aircraft information.
 * This class is a controller for the AircraftManagementDatabase: sending it messages to change the aircraft status information.
 * This class also registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 * @stereotype boundary/view/controller
 * @url element://model:project::SAAMS/design:view:::id15rnfcko4qme4cko4swib
 * @url element://model:project::SAAMS/design:view:::id2fh3ncko4qme4cko4swe5
 * @url element://model:project::SAAMS/design:node:::id15rnfcko4qme4cko4swib.node107
 * @url element://model:project::SAAMS/design:view:::idwwyucko4qme4cko4sgxi
 */
public class LATC extends JFrame implements Observer, ActionListener, ListSelectionListener {

  private final String LATC_NAME = "Local Air Traffic Controls";
  private final JLabel controls = new JLabel("Controls");
  private final JLabel inbounds = new JLabel("Inbound");
  private final JButton allowApproachClearance = new JButton("Allow Approach Clearance");
  private final JButton confirmPlaneLanding = new JButton("Confirm Plane Has Landed");
  private final JLabel outBound = new JLabel("Outbound");
  private final JButton allocateDepartureSlot = new JButton("Allocate Departure Slot");
  private final JButton permitTakeOff = new JButton("Permit Takeoff");
  private final JLabel planeDetails = new JLabel("Plane Details");
  private DefaultListModel<String> selectedPlane = new DefaultListModel<>();
  private final JList planeDetailsTF = new JList(selectedPlane);
  private final JLabel planesJL = new JLabel("Planes");
  private DefaultListModel<String> planesIncoming = new DefaultListModel<>();
  private final JList planesTF = new JList(planesIncoming);
  private int currentPlaneIndex;

  private List<String> statuses;
  private List<Integer> validMCodes = new ArrayList<Integer>();

  private JScrollPane planeDetailsScroll;
  private JScrollPane planesTFScroll;
  /**
   *  The Local Air Traffic Controller Screen interface has access to the AircraftManagementDatabase.
   * @supplierCardinality 1
   * @clientCardinality 1
   * @label accesses/observes
   * @directed*/

  private final AircraftManagementDatabase aMDatabase;


  public LATC(AircraftManagementDatabase aircraftManagementDatabase) {
    this.aMDatabase = aircraftManagementDatabase;
    this.currentPlaneIndex = -1;
    // if I want to mess with some JFrame components like color
    Container window = getContentPane();
    //creating the buttons, labels and the text fields for this view
    initiateGUI(window);
    createLabels();
    createButtons();
    createTextFields();
    //adding functionality
    this.aMDatabase.addObserver(this); //adds this view/controller as an observer to the changes made in the AircraftManagementDatabase
    setVisible(true);

    statuses = aMDatabase.statuses;
  }

  public void createLabels() {
    planesJL.setBounds(5, 20, 150, 20);
    add(planesJL);
    controls.setBounds(330, 40, 100, 20);
    add(controls);
    inbounds.setBounds(330, 65, 100, 20);
    add(inbounds);
    outBound.setBounds(330, 175, 100, 20);
    add(outBound);
    planeDetails.setBounds(330, 285, 100, 20);
    add(planeDetails);
  }

  public void createButtons() {
    allowApproachClearance.setBounds(270, 90, 200, 30);
    allowApproachClearance.addActionListener(this);
    add(allowApproachClearance);
    confirmPlaneLanding.setBounds(270, 125, 200, 30);
    confirmPlaneLanding.addActionListener(this);
    add(confirmPlaneLanding);
    permitTakeOff.setBounds(270, 235, 200, 30);
    permitTakeOff.addActionListener(this);
    add(permitTakeOff);
    allocateDepartureSlot.setBounds(270, 200, 200, 30);
    allocateDepartureSlot.addActionListener(this);
    add(allocateDepartureSlot);
  }

  public void createTextFields() {
	planesTF.addListSelectionListener(this);
    planesTFScroll = new JScrollPane(planesTF);
    planesTFScroll.setBounds(5, 45, 260, 410);
    add(planesTFScroll);
    planeDetailsScroll = new JScrollPane(planeDetailsTF);
    planeDetailsScroll.setBounds(270, 305, 200, 150);
    add(planeDetailsScroll);
  }


  public void initiateGUI(Container win) {
    setLayout(null);
    setTitle(LATC_NAME);
    setLocation(0, 0);
    setSize(500, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }


//  public String flightsLandingOrTransit() {
//    return aircraftManagementDatabase.getStatus()
//  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == allowApproachClearance){

      if (currentPlaneIndex == -1){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: You need to select an aircraft.");
      }
      else if (aMDatabase.getStatus(currentPlaneIndex) != statuses.indexOf("GROUND_CLEARANCE_GRANTED")){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: The aircraft's status is not currently valid for this operation.");
      }
      else {
        aMDatabase.setStatus(currentPlaneIndex, statuses.indexOf("LANDING"));
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: Granted approach clearance to Aircraft " + aMDatabase.getFlightCode(currentPlaneIndex));
      }
    }

    if (e.getSource() == confirmPlaneLanding){
      if (currentPlaneIndex == -1){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: You need to select an aircraft.");
      }
      else if (aMDatabase.getStatus(currentPlaneIndex) != statuses.indexOf("LANDING")){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: The aircraft's status is not currently valid for this operation.");
      }
      else {
        aMDatabase.setStatus(currentPlaneIndex, statuses.indexOf("LANDED"));
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: Aircraft " + aMDatabase.getFlightCode(currentPlaneIndex) + " landing confirmed.");
      }
    }

    if (e.getSource() == allocateDepartureSlot){
      if (currentPlaneIndex == -1){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: You need to select an aircraft.");
      }
      else if (aMDatabase.getStatus(currentPlaneIndex) != statuses.indexOf("READY_DEPART")){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: The aircraft's status is not currently valid for this operation.");
      }
      else {
        aMDatabase.setStatus(currentPlaneIndex, statuses.indexOf("AWAITING_TAXI"));
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: Aircraft " + aMDatabase.getFlightCode(currentPlaneIndex) + " assigned airslot, awaiting taxi.");
      }
    }

    if (e.getSource() == permitTakeOff){
      if (currentPlaneIndex == -1){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: You need to select an aircraft.");
      }
      else if (aMDatabase.getStatus(currentPlaneIndex) != statuses.indexOf("AWAITING_TAKEOFF")){
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: The aircraft's status is not currently valid for this operation.");
      }
      else {
        aMDatabase.setStatus(currentPlaneIndex, statuses.indexOf("DEPARTING_THROUGH_LOCAL_AIRSPACE"));
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "LATC: Aircraft " + aMDatabase.getFlightCode(currentPlaneIndex) + " granted clearance for takeoff, and they are now departing through local airspace.");
      }
    }
  }

  @Override
  public void update(Observable o, Object arg) {

    // This set of statuses are the valid statuses to be shown in this interface.
    // It covers all aircraft that are airborne within the airspace, or those that are imminently taking off.
    validMCodes.clear();
    validMCodes.addAll(aMDatabase.getWithStatus(1));
    validMCodes.addAll(aMDatabase.getWithStatus(2));
    validMCodes.addAll(aMDatabase.getWithStatus(3));
    validMCodes.addAll(aMDatabase.getWithStatus(4));
    validMCodes.addAll(aMDatabase.getWithStatus(15));
    validMCodes.addAll(aMDatabase.getWithStatus(17));
    validMCodes.addAll(aMDatabase.getWithStatus(18));

    planesIncoming.clear();
    for (int i = 0; i < validMCodes.size(); i++){
      planesIncoming.addElement(aMDatabase.getFlightCode(validMCodes.get(i)) + " - " + statuses.get(aMDatabase.getStatus(validMCodes.get(i))));
    }
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (e.getValueIsAdjusting()) {
      selectedPlane.clear();
      currentPlaneIndex = validMCodes.get(planesTF.getSelectedIndex());
      selectedPlane.addElement(planesIncoming.get(planesTF.getSelectedIndex()));
      selectedPlane.addElement("DEPARTED FROM: " + aMDatabase.getItinerary(currentPlaneIndex).getFrom());
      selectedPlane.addElement("CURRENT DESTINATION: " + aMDatabase.getItinerary(currentPlaneIndex).getNext());
      selectedPlane.addElement("FINAL DESTINATION: " + aMDatabase.getItinerary(currentPlaneIndex).getTo());
    }
  }
}