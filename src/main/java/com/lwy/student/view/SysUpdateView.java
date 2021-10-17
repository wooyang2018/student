package com.lwy.student.view;

import com.lwy.student.AppConstants;
import com.lwy.student.DAO;
import com.lwy.student.base.BaseDAO;
import com.lwy.student.dao.StudentDAO;
import com.lwy.student.model.Student;
import com.lwy.student.util.Upgrader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SysUpdateView extends JFrame {

    private JPanel jPanelCenter, jPanelSouth;
    private JButton addButton, exitButton;

    public SysUpdateView() {
        init();
    }

    private void init() {
        setTitle("提示");
        // center panel
        jPanelCenter = new JPanel();
        jPanelCenter.setLayout(new GridLayout(2, 1));
        float currentversion = Upgrader.currentversion;
        float newversion = Upgrader.newversion;
        if(newversion>currentversion)
            jPanelCenter.add(new JLabel("当前版本："+currentversion+"，最新版本："+newversion+"，建议更新"));
        else
            jPanelCenter.add(new JLabel("当前版本："+currentversion+"，已是最新版本，无需更新"));
        jPanelCenter.add(new JLabel(" "));

        // south panel
        jPanelSouth = new JPanel();
        jPanelSouth.setLayout(new GridLayout(1, 2));
        addButton = new JButton("更新");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Upgrader.autoupgrade();
            }
        });
        jPanelSouth.add(addButton);
        exitButton = new JButton("取消");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        jPanelSouth.add(exitButton);

        this.add(jPanelCenter, BorderLayout.CENTER);
        this.add(jPanelSouth, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(470, 200, 300, 100);
        setResizable(false);
        setVisible(true);
    }
}

