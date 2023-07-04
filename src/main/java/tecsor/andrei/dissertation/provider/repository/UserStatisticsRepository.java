package tecsor.andrei.dissertation.provider.repository;

import org.springframework.stereotype.Repository;
import tecsor.andrei.dissertation.provider.model.UserStatistics;

@Repository
public class UserStatisticsRepository {

    public UserStatistics findById(String id) {
        if (id.equals("5fae40f92758a3b94f441ab0fabf5c7d1c4082e8ae30043aefccad9af7e90b82")) {
            return new UserStatistics("5fae40f92758a3b94f441ab0fabf5c7d1c4082e8ae30043aefccad9af7e90b82",
                    6, 10, 3, 5600, 5000, 2);
        }
        return null;
    }
}
