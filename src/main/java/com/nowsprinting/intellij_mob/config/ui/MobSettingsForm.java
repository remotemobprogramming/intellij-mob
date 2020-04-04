package com.nowsprinting.intellij_mob.config.ui;

import com.nowsprinting.intellij_mob.config.MobProjectSettings;

import javax.swing.*;

public class MobSettingsForm {
    private JPanel panel1;
    private JTextField wipBranch;
    private JTextField baseBranch;
    private JTextField remoteName;
    private JCheckBox debug;
    private JTextField timer;
    private JCheckBox startWithShare;
    private JCheckBox nextAtExpire;
    private JTextField wipCommitMessage;
    private JCheckBox nextStay;

    public JPanel getPanel() {
        return panel1;
    }

    public boolean isModified(MobProjectSettings settings) {
        return false;
    }

    public void applyEditorTo(MobProjectSettings settings) {

    }

    public void resetEditorFrom(MobProjectSettings settings) {

    }


    /*

  public boolean isModified(HaxeProjectSettings settings) {
    final List<String> oldList = Arrays.asList(settings.getUserCompilerDefinitions());
    final List<String> newList = Arrays.asList(myAddDeleteListPanel.getItems());
    final boolean isEqual = oldList.size() == newList.size() && oldList.containsAll(newList);
    return !isEqual;
  }

  public void applyEditorTo(HaxeProjectSettings settings) {
    settings.setUserCompilerDefinitions(myAddDeleteListPanel.getItems());
  }

  public void resetEditorFrom(HaxeProjectSettings settings) {
    myAddDeleteListPanel.removeALlItems();
    for (String item : settings.getUserCompilerDefinitions()) {
      myAddDeleteListPanel.addItem(item);
    }
  }

  private void createUIComponents() {
    myAddDeleteListPanel = new MyAddDeleteListPanel(HaxeBundle.message("haxe.conditional.compilation.defined.macros"));
  }

  private class MyAddDeleteListPanel extends AddDeleteListPanel<String> {
    public MyAddDeleteListPanel(final String title) {
      super(title, Collections.<String>emptyList());
    }

    public void addItem(String item) {
      myListModel.addElement(item);
    }

    public void removeALlItems() {
      myListModel.removeAllElements();
    }

    public String[] getItems() {
      final Object[] itemList = getListItems();
      final String[] result = new String[itemList.length];
      for (int i = 0; i < itemList.length; i++) {
        result[i] = itemList[i].toString();
      }
      return result;
    }

    @Override
    protected String findItemToAdd() {
      final StringValueDialog dialog = new StringValueDialog(myAddDeleteListPanel, false);
      dialog.show();
      if (!dialog.isOK()) {
        return null;
      }
      final String stringValue = dialog.getStringValue();
      return stringValue != null && stringValue.isEmpty() ? null : stringValue;
    }
    * */
}
