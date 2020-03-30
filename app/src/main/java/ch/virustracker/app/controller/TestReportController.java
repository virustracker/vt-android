package ch.virustracker.app.controller;

import android.util.Base64;

import java.util.List;

import ch.virustracker.app.controller.restapi.submitreport.Report;
import ch.virustracker.app.controller.restapi.submitreport.SubmitReportTokensData;
import ch.virustracker.app.controller.restapi.submitreport.Token;
import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.advertiseevent.AdvertiseEvent;
import ch.virustracker.app.model.database.location.Location;

public class TestReportController {

    public static final int ONE_DAY_MS = 1000 * 60 * 60 * 24;
    public static final int DAYS_INFECTIOUS_BEFORE_SYMPTOMS = 1;

    public SubmitReportTokensData generateTestReport(int nDays) {
        long searchBackTime = ONE_DAY_MS * (nDays+DAYS_INFECTIOUS_BEFORE_SYMPTOMS);
        List<AdvertiseEvent> sentTokens = VtDatabase.getInstance().advertiseEventDao().selectByTimeSpan(System.currentTimeMillis() - searchBackTime, System.currentTimeMillis());
        Report report = new Report();
        report.setType("SELF_REPORT");
        report.setResult("POSITIVE");
        for (AdvertiseEvent ae : sentTokens) {
            Token token = new Token();
            token.setPreimage(Base64.encodeToString(ae.getPreImage(), Base64.NO_WRAP));
            Location loc = ae.getLocation();
            if (loc != null) {
                token.setLat(loc.getLatitude());
                token.setLong(loc.getLongitude());
            }
            report.getTokens().add(token);
        }
        SubmitReportTokensData testReport = new SubmitReportTokensData();
        testReport.setReport(report);
        return testReport;
    }

}
