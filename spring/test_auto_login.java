import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.service.DataService;
import com.ticketvoyage.model.User;

public class test_auto_login {
    public static void main(String[] args) {
        try {
            System.out.println("Testing auto login functionality...");

            DataService dataService = new DataService();
            AuthService authService = new AuthService(dataService);

            // Автоматический вход под единым аккаунтом (как в Main.java)
            User autoUser = dataService.findUserByEmail("auto_user");
            if (autoUser != null) {
                System.out.println("Found auto user: " + autoUser.getName());
                User loggedInUser = authService.login("auto_user", "auto123");
                if (loggedInUser != null) {
                    System.out.println("Auto login successful!");
                    System.out.println("Current user: " + authService.getCurrentUser().getName());
                    System.out.println("User role: " + authService.getCurrentUser().getRole());
                    System.out.println("Is admin: " + authService.isAdmin());
                } else {
                    System.out.println("Auto login failed");
                }
            } else {
                System.out.println("Auto user not found");
            }

            // Проверяем, что билеты очищены
            var tickets = dataService.getAllTickets();
            System.out.println("Total tickets in database: " + tickets.size());

            // Проверяем маршруты
            var routes = dataService.getAllRoutes();
            System.out.println("Total routes in database: " + routes.size());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
