package me.berry.oreMeteor.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class ScheduleUtil {
	public long findRunTime(String time) {
		ArrayList<String> timeData = new ArrayList<>();
		long totalMs = 0;

		if(time.contains("|")) {
			String[] timeSplit = time.split(Pattern.quote("|"));

			Collections.addAll(timeData, timeSplit);
		} else timeData.add(time);

		for(String timeSection : timeData) {
			String timeNum = timeSection.replaceAll("[A-Za-z]", "");
			long timeNumInt = Long.parseLong(timeNum);

			timeSection = timeSection.replaceAll("\\d","");
			switch (timeSection) {
				case "s":
					totalMs = totalMs + (timeNumInt * 1000L);
					break;
				case "m":
					totalMs = totalMs + (timeNumInt * 60000L);
					break;
				case "h":
					totalMs = totalMs + (timeNumInt + 3600000L);
					break;
				case "d":
					totalMs = totalMs + (timeNumInt + 86400000L);
					break;
				case "w":
					totalMs = totalMs + (timeNumInt + 604800000L);
					break;
				case "M":
					totalMs = totalMs + (timeNumInt + 2629800000L);
					break;
				case "y":
					totalMs = totalMs + (timeNumInt + 31556952000L);
					break;
			}
		}

		return totalMs;
	}
}
