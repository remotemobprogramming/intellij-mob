package com.nowsprinting.intellij_mob.action.start.ui;

import com.nowsprinting.intellij_mob.MobBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

public class StartDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField timerMinutes;
    private JCheckBox startWithShare;
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
     * @param canExecute enable ok button
     * @param reason     display message
     */
    public void setPreconditionResult(boolean canExecute, @Nullable String reason) {
        buttonOK.setEnabled(canExecute);
        message.setVisible(!canExecute);
        message.setText(String.format(MobBundle.message("mob.start.error.cant_start"), reason));
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

    public int getTimerMinutes() {
        return Integer.parseInt(this.timerMinutes.getText());  // Only numerical because restrict in JTextField
    }

    public void setTimerMinutes(int timerMinutes) {
        this.timerMinutes.setText(Integer.toString(timerMinutes));
    }

    public boolean isStartWithShare() {
        return this.startWithShare.isSelected();
    }

    public void setStartWithShare(boolean startWithShare) {
        this.startWithShare.setSelected(startWithShare);
    }

    public boolean isOk() {
        return ok;
    }

    public boolean isOpenSettings() {
        return openSettings;
    }
}
