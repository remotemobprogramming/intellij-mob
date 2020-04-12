package com.nowsprinting.intellij_mob.action.start.ui;

import javax.swing.*;
import java.awt.event.*;

public class StartDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField timer;
    private JCheckBox startWithShare;
    private JCheckBox nextAtExpire;

    public StartDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setTimer(int timer) {
        this.timer.setText(Integer.toString(timer));
    }

    public void setStartWithShare(boolean startWithShare) {
        this.startWithShare.setSelected(startWithShare);
    }

    public void setNextAtExpire(boolean nextAtExpire) {
        this.nextAtExpire.setSelected(nextAtExpire);
    }
}
