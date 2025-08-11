import java.io.*;
import java.util.*;

class MediaItem {
    String title;
    String genre;
    double rating;
    boolean completed;

    MediaItem(String title, String genre, double rating, boolean completed) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.completed = completed;
    }
}

public class MediaRecommendationTracker {
    static List<MediaItem> mediaList = new ArrayList<>();
    static final String FILE_NAME = "media_list.csv";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n==== Movie/Book Recommendation Tracker ====");
            System.out.println("1. Add Media");
            System.out.println("2. View All Media");
            System.out.println("3. Mark as Completed");
            System.out.println("4. Export to CSV");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addMedia();
                    break;
                case 2:
                    viewMedia();
                    break;
                case 3:
                    markCompleted();
                    break;
                case 4:
                    saveData();
                    System.out.println("Data exported to " + FILE_NAME);
                    break;
                case 5:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void addMedia() {
        System.out.print("Enter title: ");
        String title = sc.nextLine().trim();
        System.out.print("Enter genre: ");
        String genre = sc.nextLine().trim();
        System.out.print("Enter rating (0-10): ");
        double rating = getDoubleInput();

        mediaList.add(new MediaItem(title, genre, rating, false));
        saveData(); // Auto-save after adding
        System.out.println("Media added successfully!");
    }

    static void viewMedia() {
        if (mediaList.isEmpty()) {
            System.out.println("No media found.");
            return;
        }
        System.out.println("\n=== Your Media List ===");
        for (int i = 0; i < mediaList.size(); i++) {
            MediaItem m = mediaList.get(i);
            System.out.printf("%d. %s | Genre: %s | Rating: %.1f | Completed: %s%n",
                    i + 1, m.title, m.genre, m.rating, m.completed ? "Yes" : "No");
        }
    }

    static void markCompleted() {
        viewMedia();
        if (mediaList.isEmpty()) return;

        System.out.print("Enter number of media to mark as completed: ");
        int index = getIntInput() - 1;

        if (index >= 0 && index < mediaList.size()) {
            mediaList.get(index).completed = true;
            saveData(); // Auto-save after marking
            System.out.println("Marked as completed!");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    static void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    mediaList.add(new MediaItem(parts[0], parts[1],
                            Double.parseDouble(parts[2]),
                            Boolean.parseBoolean(parts[3])));
                }
            }
            System.out.println("Loaded " + mediaList.size() + " items from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    static void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (MediaItem m : mediaList) {
                bw.write(m.title + "," + m.genre + "," + m.rating + "," + m.completed);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Utility methods for safe input
    static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    static double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid decimal number: ");
            }
        }
    }
}
