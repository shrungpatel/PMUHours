import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class HoursParser {
    static class Restaurant {
        private String name;
        private String openHours;
        private String mealSwipeHours;
        private boolean isOpen; // Is it open
        private boolean isMS; // Is it accepting meal swipes right now
        public Restaurant(String name, String openHours, String mealSwipeHours) {
            this.name = name;
            this.openHours = openHours;
            this.isOpen = checkOpen(false); // Is it open right now?
            if (mealSwipeHours.length() > 2) { // Checks to see if it accepts meal swipes
                this.mealSwipeHours = mealSwipeHours.substring(3); // Removes the "MS "
                this.isMS = checkOpen(true); // Is it accepting meal swipes right now?
            } else {
                this.isMS = false;
            }
        }
        public String[] parseHours(String schedule) {
            String[] newHours; // Split by ", "
            String[] actualHours = new String[7]; // 0 = monday...6 = sunday
            if (schedule.indexOf(", ") > 0) {
                newHours = schedule.split(", ");
            } else {
                newHours = new String[]{schedule};
            }
            for(int i = 0; i < newHours.length; i++) {
                String startDay = newHours[i].split(" ")[0];
                String endDay = " ";
                if (newHours[i].substring(0, 5).contains("-")) {
                    startDay = newHours[i].split("-")[0];
                    endDay = newHours[i].split("-")[1].split(" ")[0];
                }
                String hours = newHours[i].split(" ")[1];
                int startNum = assignDay(startDay);
                int endNum = assignDay(endDay) == -1 ? startNum: assignDay(endDay); // endNum = startNum when it only
                // accepts meal swipes on one day
                if (startNum < endNum) { // Like M -> S
                    for (int dayNum = startNum; dayNum <= endNum; dayNum++) {
                        actualHours[dayNum] = hours;
                    }
                } else { // Like S -> T
                    // Fills in S and Su
                    for (int dayNum = startNum; dayNum < 7; dayNum++) {
                        actualHours[dayNum] = hours;
                    }
                    // Fills in M and T
                    for (int dayNum = 0; dayNum <= endNum; dayNum++) {
                        actualHours[dayNum] = hours;
                    }
                }
            }
            return actualHours;
        }
        public static int assignDay(String day) {
            return switch (day) {
                case "M" -> 0;
                case "T" -> 1;
                case "W" -> 2;
                case "R" -> 3;
                case "F" -> 4;
                case "S" -> 5;
                case "Su" -> 6;
                default -> -1;
            };
        }
        public boolean checkOpen(boolean isMealSwipe) {
            String[] actualHours;
            actualHours = isMealSwipe ? this.parseHours(this.mealSwipeHours) : this.parseHours(this.openHours);
            if (actualHours[dayOfTheWeek] != null) {
                // Taking in the information
                String startTimeClock = actualHours[dayOfTheWeek].split("-")[0];
                String endTimeClock = actualHours[dayOfTheWeek].split("-")[1];
                int startTime, startMinutes = 0;
                if (startTimeClock.contains(":")) { // When it opens at 7:30 instead of 7 or 8
                    startTime = Integer.parseInt(startTimeClock.split(":")[0]);
                    startMinutes = Integer.parseInt(startTimeClock.split(":")[1].substring(0, 2));
                } else {
                    startTime = Integer.parseInt(startTimeClock.substring(0, startTimeClock.length() - 2));
                }
                int endTime = Integer.parseInt(endTimeClock.substring(0, endTimeClock.length() - 2));
                String startTimeAMPM = startTimeClock.substring(startTimeClock.length() - 2);
                String endTimeAMPM = endTimeClock.substring(endTimeClock.length() - 2);
                if (startTimeAMPM.equals("PM")) startTime += 12;
                if (endTimeAMPM.equals("PM")) endTime += 12;
                LocalTime startHour = LocalTime.of(startTime, startMinutes);
                LocalTime endHour = LocalTime.of(endTime, 0);
                if (currentTime.compareTo(startHour) > 0 && currentTime.compareTo(endHour) < 0) return true;
                else return false;
            } else return false;
        }
        /**
         * Getter methods
         * @return the field in the method name
         */
        public String getOpenHours() {
            return this.openHours;
        }
        public String getMealSwipeHours() {
            return this.mealSwipeHours;
        }
        public String getName() {
            return this.name;
        }
        public boolean getIsOpen() {
            return this.isOpen;
        }
        public boolean getIsMS() {
            return isMS;
        }
    }
    static int dayOfTheWeek = -1;
    static LocalTime currentTime = LocalTime.now(); // The current time
    public static void main(String[] args) throws IOException {
        LocalDate currentDate = LocalDate.now(); // Today's date
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek(); // Get the day of the week from the current date
        dayOfTheWeek = dayOfWeek.getValue() - 1; // 0 = Monday...6 = Sunday

        // Reads in the input and makes the restaurant objects
        BufferedReader br = new BufferedReader(new FileReader("PMU_Hours.txt"));
        String line = br.readLine();
        Restaurant[] restaurants = new Restaurant[12];
        int i = 0;
        do {
            String restaurantName = line.split(": ")[0];
            String restOfTheInfo = line.split(": ")[1];
            String openHours = restOfTheInfo.split(" / ")[0];
            String mealSwipeHours = "";
            if (restOfTheInfo.indexOf(" / ") > 0) {
                mealSwipeHours = restOfTheInfo.split(" / ")[1];
            }
            restaurants[i] = new Restaurant(restaurantName, openHours, mealSwipeHours);
            System.out.println(restaurants[i].getName() + " " + restaurants[i].getOpenHours() + " " + restaurants[i].getMealSwipeHours());
            System.out.println(restaurants[i].getIsOpen() + " " + restaurants[i].getIsMS());
            i++;
        } while ((line = br.readLine()) != null);
    }
}
