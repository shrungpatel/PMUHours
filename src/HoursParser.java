import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
public class HoursParser {
    static class Restaurant {
        private String name;
        private String openHours;
        private String mealSwipeHours;
        private boolean isOpen; // Is it open
        private boolean isMS; // Is it accepting meal swipes right now
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalTime msStartTime;
        private LocalTime msEndTime;
        public Restaurant(String name, String openHours, String mealSwipeHours) {
            this.name = name;
            this.openHours = openHours;
            this.mealSwipeHours = mealSwipeHours;
            this.fixHours();
            this.fixMealSwipeHours();
        }
        public String getOpenHours() {
            return openHours;
        }
        public String getMealSwipeHours() {
            return mealSwipeHours;
        }
        public String getName() {
            return name;
        }
        public void fixHours() {
            String[] newHours; // Split by ", "
            String[] actualHours = new String[7]; // 0 = monday...6 = sunday
            if (this.openHours.indexOf(", ") > 0) {
                newHours = this.openHours.split(", ");
            } else {
                newHours = new String[]{this.openHours};
            }
            for(String thing: newHours) {
                System.out.println(thing);
            }
            for(int i = 0; i < newHours.length; i++) {
                String startDay = newHours[i].split(" ")[0];
                String endDay = " ";
                if (newHours[i].substring(0, 5).contains("-")) {
                    startDay = newHours[i].split("-")[0];
                    endDay = newHours[i].split("-")[1].split(" ")[0];
                }
                String hours = newHours[i].split(" ")[1];
                int startNum;
                int endNum;
                switch(startDay) {
                    case "M" -> startNum = 0;
                    case "T" -> startNum = 1;
                    case "W" -> startNum = 2;
                    case "R" -> startNum = 3;
                    case "F" -> startNum = 4;
                    case "S" -> startNum = 5;
                    default -> startNum = 6; // Sunday
                }
                switch(endDay) {
                    case "M" -> endNum = 0;
                    case "T" -> endNum = 1;
                    case "W" -> endNum = 2;
                    case "R" -> endNum = 3;
                    case "F" -> endNum = 4;
                    case "S" -> endNum = 5;
                    case "Su" -> endNum = 6;
                    default -> endNum = startNum;
                }
                System.out.println(endDay);
                if (startNum < endNum) { // Like M -> S
                    for (int dayNum = startNum; dayNum <= endNum; dayNum++) {
                        actualHours[dayNum] = hours;
                    }
                } else { // Like S -> M
                    for (int dayNum = startNum; dayNum >= endNum; dayNum--) {
                        actualHours[dayNum] = hours;
                    }
                }
                for (String day: actualHours) {
                    System.out.println(day);
                }
            }
        }
        public void fixMealSwipeHours() {

        }
        public boolean getIsOpen() {
            return isOpen;
        }
        public boolean getIsMS() {
            return isMS;
        }
        public void seeIfOpen() {
        }
        public void seeIfMS() {

        }
    }
    public static void main(String[] args) throws IOException {
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
            i++;
        } while ((line = br.readLine()) != null);

        LocalTime currentTime = LocalTime.now();
        System.out.println(currentTime);
        LocalTime exampleTime = LocalTime.of(18, 35);
        System.out.println(currentTime.compareTo(exampleTime));
    }
}
