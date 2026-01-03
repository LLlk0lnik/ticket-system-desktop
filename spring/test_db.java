import com.ticketvoyage.service.DataService;

public class test_db {
    public static void main(String[] args) {
        try {
            System.out.println("Testing DataService initialization...");
            DataService ds = new DataService();
            System.out.println("DataService created successfully");

            // Test user lookup
            var user = ds.findUserByEmail("auto_user");
            if (user != null) {
                System.out.println("Found user: " + user.getName() + " (" + user.getEmail() + ")");
            } else {
                System.out.println("User not found");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
