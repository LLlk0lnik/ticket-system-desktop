package com.ticketvoyage;

import com.ticketvoyage.service.DataService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.Comparator;

public class ScheduleFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final DataService dataService;
    
    public ScheduleFrame() {
        dataService = new DataService();
        setTitle("Расписание рейсов - TicketVoyage");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(249, 250, 251));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(249, 250, 251));
        
        JLabel title = new JLabel("📅 Расписание рейсов");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(17, 24, 39));
        
        JLabel subtitle = new JLabel("Актуальное расписание всех направлений");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(107, 114, 128));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(249, 250, 251));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(new Color(249, 250, 251));
        
        JLabel filterLabel = new JLabel("Направление:");
        filterLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        filterLabel.setForeground(new Color(55, 65, 81));
        
        String[] cities = {"Все направления", "Москва", "Санкт-Петербург", "Казань", "Новгород"};
        JComboBox<String> cityFilter = new JComboBox<>(cities);
        cityFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cityFilter.setPreferredSize(new Dimension(180, 35));
        
        filterPanel.add(filterLabel);
        filterPanel.add(cityFilter);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JScrollPane tableScrollPane = createScheduleTable();
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(new Color(249, 250, 251));
        
        JLabel infoLabel = new JLabel("ℹ️ Расписание обновляется ежедневно. Актуальность гарантируется.");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(107, 114, 128));
        footerPanel.add(infoLabel);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);

        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        cityFilter.addActionListener(e -> {
            String selected = (String) cityFilter.getSelectedItem();
            if (selected == null || "Все направления".equals(selected)) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                        String from = entry.getStringValue(1);
                        String to = entry.getStringValue(2);
                        return (from != null && from.contains(selected)) || (to != null && to.contains(selected));
                    }
                });
            }
        });
    }
    
    private JScrollPane createScheduleTable() {
        String[] columns = {"№", "Откуда", "Куда", "Отправление", "Прибытие", "В пути", "Дни недели", "Цена", "Мест"};

        Object[][] data = dataService != null ? dataService.getScheduleData() : new Object[0][0];

        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setGridColor(new Color(229, 231, 235));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(new Color(17, 24, 39));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(243, 244, 246));
        header.setForeground(new Color(17, 24, 39));
        header.setPreferredSize(new Dimension(0, 40));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);
        table.getColumnModel().getColumn(8).setPreferredWidth(60);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        sorter.setComparator(5, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(parseDurationMinutes(o1.toString()), parseDurationMinutes(o2.toString()));
            }
        });
        sorter.setComparator(7, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(parsePrice(o1.toString()), parsePrice(o2.toString()));
            }
        });
        sorter.setComparator(8, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                try {
                    return Integer.compare(Integer.parseInt(o1.toString().trim()), Integer.parseInt(o2.toString().trim()));
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });
        table.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        return scrollPane;
    }

    private int parseDurationMinutes(String value) {
        if (value == null) return 0;
        String text = value.replace(" ", "");
        int hIndex = text.indexOf("ч");
        int mIndex = text.indexOf("м");
        int hours = 0;
        int minutes = 0;
        try {
            if (hIndex > 0) {
                hours = Integer.parseInt(text.substring(0, hIndex));
            }
            if (mIndex > hIndex && hIndex >= 0) {
                minutes = Integer.parseInt(text.substring(hIndex + 1, mIndex));
            }
        } catch (NumberFormatException e) {
            return 0;
        }
        return hours * 60 + minutes;
    }

    private int parsePrice(String value) {
        if (value == null) return 0;
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

