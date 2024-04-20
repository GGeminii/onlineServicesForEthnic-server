package com.onlineServicesForEthnic.utils;

import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 用来转换点什么东西
 */
@Slf4j
public class CastUtil {

    /**
     *将Date转换为LocalDate
     */
    public static LocalDate castDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     *将Date转换为LocalDateTime
     */
    public static LocalDateTime castDateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    /**
     *将Date转换为LocalTime
     */
    public static LocalTime castDateToLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }
}
