public List<LoanAccount> getOverdueLoans(List<LoanAccount> accounts) {
   // FIX: guard against null accounts list
   if (accounts == null) {
       return new java.util.ArrayList<>();
   }

   // FIX: initialized as ArrayList to fix the NullPointerException
   List<LoanAccount> result = new java.util.ArrayList<>();
 
    for (LoanAccount account : accounts) {
        // FIX: null check for dueDate to handle restructured accounts safely
        if (account != null && account.getDueDate() != null && account.getDueDate().before(new java.util.Date())) {
            // FIX: check against 0.0 to safely handle double precision for zero balances
            if (account.getOutstandingBalance() > 0.0) {
               result.add(account);
            }
        }
    }
    return result;
}
