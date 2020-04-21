package com.nowsprinting.intellij_mob.action.start.ui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

public class StartDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField timer;
    private JCheckBox startWithShare;
    private JCheckBox nextAtExpire;
    private JButton buttonOpenSettings;
    private JLabel message;
    private boolean openSettings = false;
    private boolean ok = false;

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

        buttonOpenSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOpenSettings();
            }
        });

        // TODO: Add control that can input only numerical value in `timer`
    }

    /**
     * Set pre-condition check results
     *
     * @param canExecute   enable ok button
     * @param errorMessage display message
     */
    public void setPreconditionResult(boolean canExecute, @Nullable String errorMessage) {
        buttonOK.setEnabled(canExecute);
        message.setVisible(!canExecute);
        message.setText(errorMessage);
    }

    private void onOpenSettings() {
        openSettings = true;
        dispose();
    }

    private void onOK() {
        // add your code here
        ok = true;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public int getTimer() {
        return Integer.parseInt(this.timer.getText());  // Only numerical because restrict in JTextField
    }

    public void setTimer(int timer) {
        this.timer.setText(Integer.toString(timer));
    }

    public boolean isStartWithShare() {
        return this.startWithShare.isSelected();
    }

    public void setStartWithShare(boolean startWithShare) {
        this.startWithShare.setSelected(startWithShare);
    }

    public boolean isNextAtExpire() {
        return this.nextAtExpire.isSelected();
    }

    public void setNextAtExpire(boolean nextAtExpire) {
        this.nextAtExpire.setSelected(nextAtExpire);
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isOpenSettings() {
        return openSettings;
    }
}
