import com.ticketvoyage.service.AuthService;
import com.ticketvoyage.service.DataService;
import com.ticketvoyage.model.User;
import com.ticketvoyage.model.Ticket;
import java.util.UUID;

public class test_profile {
    public static void main(String[] args) {
        try {
            System.out.println("Testing ProfileFrame ticket display...");

            DataService dataService = new DataService();
            AuthService authService = new AuthService(dataService);

            // Автоматический вход
            User autoUser = dataService.findUserByEmail("auto_user");
            authService.login("auto_user", "auto123");

            // Создаем тестовый билет
            Ticket testTicket = new Ticket(
                UUID.randomUUID().toString(),
                autoUser.getId(),
                "1", // route id
                "1A",
                "Тестовый пассажир",
                "test@example.com",
                "+79999999999",
                "BOOKED"
            );

            dataService.addTicket(testTicket);
            System.out.println("Added test ticket");

            // Проверяем, что билет отображается для всех пользователей
            var allTickets = dataService.getAllTickets();
            System.out.println("Total tickets visible to all users: " + allTickets.size());

            for (Ticket ticket : allTickets) {
                System.out.println("Ticket: " + ticket.getPassengerName() + " - " + ticket.getStatus());
            }

            // Проверяем билеты конкретного пользователя
            var userTickets = dataService.getUserTickets(autoUser.getId());
            System.out.println("Tickets for current user: " + userTickets.size());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
