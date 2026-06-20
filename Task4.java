public class ReportDAO {
 
    private javax.sql.DataSource dataSource;
 
    public java.util.List<ReportEntry> fetchMonthlyReport(String accountId, int month, int year) throws java.sql.SQLException {
        java.util.List<ReportEntry> entries = new java.util.ArrayList<>();
        
        String query = "SELECT * FROM report_entries WHERE account_id = ? AND MONTH(entry_date) = ? AND YEAR(entry_date) = ?";

        // FIX: try-with-resources automatically closes conn and ps to prevent connection pool exhaustion
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, accountId);
            ps.setInt(2, month);
            ps.setInt(3, year);
 
            // FIX: nested try blocks handles closing rs first, then statement, then connection
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entries.add(mapRow(rs));
                }
            }
        }
        return entries;    // conn, ps, rs are never closed
    }
}
