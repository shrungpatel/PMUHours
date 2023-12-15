import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class HoursParser {
    static class Restaurant {
        private String name;
        private String openHours;
        private String mealSwipeHours;
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

        }
        public void fixMealSwipeHours() {

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
    }
}
