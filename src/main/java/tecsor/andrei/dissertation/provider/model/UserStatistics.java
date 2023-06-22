package tecsor.andrei.dissertation.provider.model;

import lombok.*;

@Data
@AllArgsConstructor
public class UserStatistics {
    private String pid;
    private int gamblingPercent;
    private int overspendingScore;
    private int impulsiveBuyingScore;
    private int meanDepositSum;
    private int meanReportedIncome;
    private int noMonthsDeposited;
}
