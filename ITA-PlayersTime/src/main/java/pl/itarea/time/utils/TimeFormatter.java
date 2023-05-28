package pl.itarea.time.utils;

public class TimeFormatter {

    public static String formatTime(long playTime) {
        long hours = playTime / 3600;
        long minutes = (playTime % 3600) / 60;
        long seconds = playTime % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
