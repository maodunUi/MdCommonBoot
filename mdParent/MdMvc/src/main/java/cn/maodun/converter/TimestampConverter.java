package cn.maodun.converter;

import cn.maodun.exceptions.AppException;
import cn.maodun.exceptions.FastErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

/**
 * 时间戳格式转换器
 *
 * @author zhouchao
 * @date 2019-04-16
 */
public class TimestampConverter extends PropertiesEditor implements Converter<String, Timestamp> {
    @Override
    public Timestamp convert(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        } else if (StringUtils.isNumeric(value)) {
            return new Timestamp(Long.parseLong(value));
        } else {
            try {
                Date date = DateUtils.parseDate(value,
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd HH:mm:ss.S",
                        "yyyy-MM-dd",
                        "yyyyMMdd",
                        "yyyy/MM/dd HH:mm",
                        "yyyy/M/dd HH:mm",
                        "yyyy/M/d HH:mm",
                        "yyyy/MM/d HH:mm",
                        "MM/dd/yy",
                        "yyyy-MM-dd",
                        "yyyy-MM-dd HH:mm");
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(this.convert(text));
    }
}
