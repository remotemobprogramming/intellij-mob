package com.nowsprinting.intellij_mob.action.next.ui;

import com.nowsprinting.intellij_mob.MobBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

public class NextDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField wipCommitMessage;
    private JCheckBox nextStay;
    private JButton buttonOpenSettings;
    private JLabel message;
    private boolean openSettings = false;
    private boolean ok = false;

    public NextDialog() {
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
    }

    /**
     * Set pre-condition check results
     *
     * @param canExecute enable ok button
     * @param reason     display message
     */
    public void setPreconditionResult(boolean canExecute, @Nullable String reason) {
        buttonOK.setEnabled(canExecute);
        message.setVisible(!canExecute);
        message.setText(String.format(MobBundle.message("mob.next.error.cant_do_next"), reason));
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

    public String getWipCommitMessage() {
        return wipCommitMessage.getText();
    }

    public void setWipCommitMessage(String wipCommitMessage) {
        this.wipCommitMessage.setText(wipCommitMessage);
    }

    public boolean isNextStay() {
        return nextStay.isSelected();
    }

    public void setNextStay(boolean nextStay) {
        this.nextStay.setSelected(nextStay);
    }

    public boolean isOpenSettings() {
        return openSettings;
    }

    public boolean isOk() {
        return ok;
    }
}