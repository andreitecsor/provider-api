package tecsor.andrei.dissertation.provider.repository;

import org.springframework.stereotype.Repository;
import tecsor.andrei.dissertation.provider.model.UserStatistics;

@Repository
public class UserStatisticsRepository {

    public UserStatistics findById(String id) {
        if (id.equals("3ac8bb5f170b7f34e5e6d74c3f0a33db186b31c7a68e5706a4f54c11693934f6")) {
            return new UserStatistics("3ac8bb5f170b7f34e5e6d74c3f0a33db186b31c7a68e5706a4f54c11693934f6",
                    6, 10, 3, 5600, 5000, 2);
        }
        return null;
    }
}
