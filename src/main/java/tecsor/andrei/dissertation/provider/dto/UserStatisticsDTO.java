package tecsor.andrei.dissertation.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsDTO implements Serializable {
    private int size;
    private String gamblingPercent;
    private String overspendingScore;
    private String impulsiveBuyingScore;
    private String meanDepositSum;
    private String meanReportedIncome;
    private String noMonthsDeposited;
}