package com.smallworld;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gson.Gson;
import com.smallworld.dto.Transaction;

public class TransactionDataFetcher {

			// path to the JSON file
			private static final String FILE_PATH = "./transactions.json";
			//all transactions stored into arrays
			Transaction[] transactions;
			Gson gson = new Gson();
	

	public TransactionDataFetcher()  {
		
	
			try {
			
					this.getTransactions();

				} catch (IOException e) {
					System.err.println("Error loading transaction data: " + e.getMessage());
				}

		}
	
	
	/**
	 * getTransactions() method.
	 */
	public void getTransactions() throws IOException {
		
		 FileReader fileReader = new FileReader(FILE_PATH);
		 transactions= gson.fromJson(fileReader, Transaction[].class);
			
	}

	/**
	 * Returns the sum of the amounts of all transactions
	 */
	public double getTotalTransactionAmount() {
		
		 double totalAmount = 0;
	     try {  
	            for (Transaction transaction : transactions) {
	                totalAmount += transaction.getAmount();
	            }
			}catch (Exception e) {
				System.err.println("" + e.getMessage());
			}
	        return totalAmount;
	}

	/**
	 * Returns the sum of the amounts of all transactions sent by the specified
	 * client
	 */
	public double getTotalTransactionAmountSentBy(String senderFullName) {
		
				double totalAmount = 0;
			try {
				 for (Transaction transaction : transactions) {
		                if (transaction.getSenderFullName().equalsIgnoreCase(senderFullName)) {
		                    totalAmount += transaction.getAmount();
		                }
		            }
				}catch (Exception e) {
					System.err.println(" " + e.getMessage());
				}
		        
        return totalAmount;
	}

	/**
	 * Returns the highest transaction amount
	 */
	public double getMaxTransactionAmount() {
		
		double maxAmount = 0;
        
		try {
			
        	for (Transaction transaction : transactions) {
				
		            if (transaction.getAmount() > maxAmount) {
		                maxAmount = transaction.getAmount();
		            }
		        
			}
		
		}catch (Exception e) {
			System.err.println("" + e.getMessage());
		}
		 
		return maxAmount;
	}

	/**
	 * Counts the number of unique clients that sent or received a transaction
	 */
	public long countUniqueClients() {
		
		 Set<String> uniqueClients = new HashSet<>();
	       
		 try {
	    
	    	   for (Transaction transaction : transactions) {
	           
	    		   uniqueClients.add(transaction.getSenderFullName());
	       
	    	   }
	       
	       }catch (Exception e) {
				System.err.println("" + e.getMessage());
			}
	     
		 return uniqueClients.size();
	}

	/**
	 * Returns whether a client (sender or beneficiary) has at least one transaction
	 * with a compliance issue that has not been solved
	 */
	public boolean hasOpenComplianceIssues(String beneficiaryFullName) {
		
			try {
			
				for (Transaction transaction : transactions) {
					
            	        if (transaction.getBeneficiaryFullName().equals(beneficiaryFullName) &&
		                    transaction.isIssueSolved()) {
		        
            	        	return true;
                        
            	        }
            	
				}
            	
			}catch (Exception e) {
				System.err.println("" + e.getMessage());
			}
	  
		return false;
	}

	/**
	 * Returns all transactions indexed by beneficiary name
	 */
	public Map<String, Object> getTransactionsByBeneficiaryName() {
		
		
		 Map<String, Object> transactionsByBeneficiary = new HashMap<>();
	    
		 try {
		 	
	      		for (Transaction transaction : transactions) {
	            
	      			String beneficiaryFullName = transaction.getBeneficiaryFullName();
	                
	      			if (!transactionsByBeneficiary.containsKey(beneficiaryFullName)) {
	                
	      				transactionsByBeneficiary.put(beneficiaryFullName, new HashMap<String, Object>());
	                
	      			}
	      			
	                Map<String, Object> transactionMap = new HashMap<>();
	                transactionMap.put("Mtn", transaction.getMtn());
	                transactionMap.put("amount", transaction.getAmount());
	                transactionMap.put("senderFullName", transaction.getSenderFullName());
	                ((Map<String, Object>) transactionsByBeneficiary.get(beneficiaryFullName)).put(""+transaction.getMtn(), transactionMap);
	            }
	      	
	      	}catch (Exception e) {
				System.err.println("" + e.getMessage());
			}
	        
	      	return transactionsByBeneficiary;
	}

	/**
	 * Returns the identifiers of all open compliance issues
	 */
	public Set<Integer> getUnsolvedIssueIds() {
		
		Set<Integer> unsolvedIssueIds = new HashSet<>();
        
		try {
        	
            for (Transaction transaction : transactions) {
            
            	if (!transaction.isIssueSolved()) {
                    unsolvedIssueIds.add(transaction.getIssueId());
                }
            }
            
        }catch (Exception e) {
			System.err.println("" + e.getMessage());
		}
        
		return unsolvedIssueIds;
	}

	/**
	 * Returns a list of all solved issue messages
	 */
	public List<String> getAllSolvedIssueMessages() {
		
		List<String> allSolvedIssueMessages = new ArrayList<>();
       
		try {
		
			for (Transaction transaction : transactions) {
            	
                if (transaction.isIssueSolved()) {
                    String issueMessage = transaction.getIssueMessage();
                    if (issueMessage != null && !issueMessage.isEmpty()) {
                        allSolvedIssueMessages.add(issueMessage);
                    }
                }
            }
        
		}catch (Exception e) {
			System.err.println("" + e.getMessage());
		}
		return allSolvedIssueMessages;
	}

	/**
	 * Returns the 3 transactions with highest amount sorted by amount descending
	 */
	public List<Object> getTop3TransactionsByAmount() {
		   		
			List<Object> top3Transactions = new ArrayList<>();
			
			try {
	       	
				List<Transaction> sortedTransactions=new ArrayList<>(List.of(transactions));
		        // Sort transactions by amount in descending order
		        Collections.sort(sortedTransactions, Comparator.comparing(Transaction::getAmount).reversed());
		        // Get the top 3 transactions or all available transactions if less than 3
		        int count = Math.min(sortedTransactions.size(), 3);
		    
		        for (int i = 0; i < count; i++) {
	                top3Transactions.add(sortedTransactions.get(i));
	            }
			
			}catch (Exception e) {
				System.err.println("" + e.getMessage());
			}
	        return top3Transactions;
	}

	
	/**
	 * Returns the sender with the most total sent amount
	 */
	public Optional<Object> getTopSender() {
		
		Map<String, Double> senderTotalAmounts = new HashMap<>();
			
			try {
				
	            for (Transaction transaction : transactions) {
	                String sender = transaction.getSenderFullName();
	                double amount = transaction.getAmount();
	                if (senderTotalAmounts.containsKey(sender)) {
	                    senderTotalAmounts.put(sender, senderTotalAmounts.get(sender) + amount);
	                } else {
	                    senderTotalAmounts.put(sender, amount);
	                }
	            }
            
	         Optional<Map.Entry<String, Double>> topSenderEntry = senderTotalAmounts.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue));
           
	         if (topSenderEntry.isPresent()) {
	                String topSenderFullName = topSenderEntry.get().getKey();
	         
	                return Optional.of(topSenderFullName);
	         	}
	         
			}catch (Exception e) {
				System.err.println(e.getMessage());
			}
      
        return Optional.empty();
	}

	/**
	 * main method call
	 * 
	 * @param arg
	 */
	public static void main(String arg[]) {


			TransactionDataFetcher fetcher = new TransactionDataFetcher();
			System.out.println("Total Transactions Amount :	"+			fetcher.getTotalTransactionAmount());
			System.out.println("Total Amount Send By (Tom Shelb) :	"+  fetcher.getTotalTransactionAmountSentBy("Tom Shelby"));
			System.out.println("Get Max Transaction Amount :	"+		fetcher.getMaxTransactionAmount());
			System.out.println("Count Unique Clients :"+ fetcher.countUniqueClients());
			System.out.println("Has Open Compliance :"+ fetcher.hasOpenComplianceIssues("Arthur Shelby"));
			System.out.println("Transaction By BeneficiaryName :"+fetcher.getTransactionsByBeneficiaryName());
			System.out.println("Unsolved Issue Ids :"+fetcher.getUnsolvedIssueIds().toString());
			System.out.println("All Solved Issue Messages: "+ fetcher.getAllSolvedIssueMessages());
			System.out.println("Top 3 Transactions By Amount: "+ fetcher.getTop3TransactionsByAmount());
			System.out.println("Top Sender: "+ fetcher.getTopSender());
	
	}
}
