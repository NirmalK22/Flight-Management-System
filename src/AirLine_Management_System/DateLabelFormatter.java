package AirLine_Management_System;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

public class DateLabelFormatter extends DefaultFormatter {
    private SimpleDateFormat dateFormatter;
    private MaskFormatter maskFormatter;
    public DateLabelFormatter() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }
}
