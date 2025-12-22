package com.ticketvoyage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.Comparator;

@SuppressWarnings("unchecked")
public class ScheduleFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    
    public ScheduleFrame() {
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
    
    @SuppressWarnings("unchecked")
    private JScrollPane createScheduleTable() {
        String[] columns = {"№", "Откуда", "Куда", "Отправление", "Прибытие", "В пути", "Дни недели", "Цена", "Мест"};
        
        Object[][] data = {
            
            {"101", "Москва", "Санкт-Петербург", "06:30", "10:45", "4ч 15м", "Ежедневно", "1200₽", "40"},
            {"102", "Москва", "Санкт-Петербург", "08:30", "12:45", "4ч 15м", "Ежедневно", "1500₽", "40"},
            {"103", "Москва", "Санкт-Петербург", "12:00", "16:15", "4ч 15м", "Пн-Пт", "1400₽", "40"},
            {"104", "Москва", "Санкт-Петербург", "15:00", "19:30", "4ч 30м", "Ежедневно", "1800₽", "40"},
            {"105", "Москва", "Санкт-Петербург", "18:45", "23:00", "4ч 15м", "Ежедневно", "1600₽", "40"},
            {"106", "Москва", "Санкт-Петербург", "22:00", "02:15", "4ч 15м", "Ежедневно", "1200₽", "40"},
            
            
            {"201", "Санкт-Петербург", "Москва", "07:00", "11:15", "4ч 15м", "Ежедневно", "1200₽", "40"},
            {"202", "Санкт-Петербург", "Москва", "10:30", "14:45", "4ч 15м", "Ежедневно", "1500₽", "40"},
            {"203", "Санкт-Петербург", "Москва", "14:00", "18:15", "4ч 15м", "Ежедневно", "1600₽", "40"},
            {"204", "Санкт-Петербург", "Москва", "17:30", "21:45", "4ч 15м", "Пн-Сб", "1700₽", "40"},
            {"205", "Санкт-Петербург", "Москва", "20:00", "00:15", "4ч 15м", "Ежедневно", "1400₽", "40"},
            
            
            {"301", "Москва", "Казань", "08:00", "19:30", "11ч 30м", "Ежедневно", "2500₽", "40"},
            {"302", "Москва", "Казань", "14:30", "02:00", "11ч 30м", "Чт-Вс", "2300₽", "40"},
            {"303", "Москва", "Казань", "21:00", "08:30", "11ч 30м", "Ежедневно", "2200₽", "40"},
            
            
            {"401", "Казань", "Москва", "09:00", "20:30", "11ч 30м", "Ежедневно", "2500₽", "40"},
            {"402", "Казань", "Москва", "16:00", "03:30", "11ч 30м", "Пн-Пт", "2400₽", "40"},
            {"403", "Казань", "Москва", "22:30", "10:00", "11ч 30м", "Ежедневно", "2200₽", "40"},
            
            
            {"501", "Москва", "Новгород", "07:30", "10:00", "2ч 30м", "Ежедневно", "800₽", "40"},
            {"502", "Москва", "Новгород", "11:00", "13:30", "2ч 30м", "Ежедневно", "900₽", "40"},
            {"503", "Москва", "Новгород", "15:00", "17:30", "2ч 30м", "Пн-Пт", "850₽", "40"},
            {"504", "Москва", "Новгород", "19:00", "21:30", "2ч 30м", "Ежедневно", "950₽", "40"},
            
            
            {"601", "Новгород", "Москва", "08:00", "10:30", "2ч 30м", "Ежедневно", "800₽", "40"},
            {"602", "Новгород", "Москва", "12:30", "15:00", "2ч 30м", "Ежедневно", "900₽", "40"},
            {"603", "Новгород", "Москва", "16:30", "19:00", "2ч 30м", "Пн-Сб", "850₽", "40"},
            {"604", "Новгород", "Москва", "20:00", "22:30", "2ч 30м", "Ежедневно", "950₽", "40"},
            
            
            {"701", "Санкт-Петербург", "Новгород", "09:30", "11:00", "1ч 30м", "Ежедневно", "600₽", "40"},
            {"702", "Санкт-Петербург", "Новгород", "14:00", "15:30", "1ч 30м", "Ежедневно", "650₽", "40"},
            {"703", "Санкт-Петербург", "Новгород", "18:30", "20:00", "1ч 30м", "Ежедневно", "700₽", "40"},
            
            {"801", "Новгород", "Санкт-Петербург", "10:00", "11:30", "1ч 30м", "Ежедневно", "600₽", "40"},
            {"802", "Новгород", "Санкт-Петербург", "13:00", "14:30", "1ч 30м", "Пн-Пт", "650₽", "40"},
            {"803", "Новгород", "Санкт-Петербург", "17:00", "18:30", "1ч 30м", "Ежедневно", "700₽", "40"},
        };
        
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
        sorter.setComparator(5, (Comparator<Object>) (o1, o2) -> Integer.compare(parseDurationMinutes(o1.toString()), parseDurationMinutes(o2.toString())));
        sorter.setComparator(7, (Comparator<Object>) (o1, o2) -> Integer.compare(parsePrice(o1.toString()), parsePrice(o2.toString())));
        sorter.setComparator(8, (Comparator<Object>) (o1, o2) -> {
            try {
                return Integer.compare(Integer.parseInt(o1.toString().trim()), Integer.parseInt(o2.toString().trim()));
            } catch (NumberFormatException e) {
                return 0;
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

