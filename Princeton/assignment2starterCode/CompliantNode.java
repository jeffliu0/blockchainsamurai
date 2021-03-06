import java.util.HashSet;
import java.util.Set;
import java.util.*;
import static java.util.stream.Collectors.toSet;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private double p_graph;
    private double p_malicious;
    private double p_tXDistribution;
    private int rounds;

    private boolean[] followees;

    private Set<Transaction> pendingTransactions;
    private Set<Transaction> tmpTransactions;

    private boolean[] blackListed;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.p_graph = p_graph;
        this.p_malicious = p_malicious;
        this.p_tXDistribution = p_txDistribution;
        rounds = numRounds ;
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
        this.blackListed = new boolean[followees.length];
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {

        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        Set<Transaction> toSend = new HashSet<>(pendingTransactions);

        tmpTransactions = toSend;

        pendingTransactions.clear();
        return toSend;
    }


    public void receiveFromFollowees(Set<Candidate> candidates) {

        Set<Integer> senders = candidates.stream().map(c -> c.sender).collect(toSet());
        Set<Transaction> tx = candidates.stream().map(c -> c.tx).collect(toSet());


        System.out.println("\nWe are at round: " + (11 - rounds) + "\n");
        System.out.println("This is the size of pendingTransactions: " + tmpTransactions.size());


        HashMap<Integer, ArrayList<Transaction> > checkSize = new HashMap<Integer, ArrayList<Transaction>>();



        System.out.println("\n\nThese are the followees: " + Arrays.toString(followees));
        System.out.println(senders);


        for (int i = 0; i < followees.length; i++) {
            if (followees[i] && !senders.contains(i))
                blackListed[i] = true;
        }

        for(Candidate c : candidates)
        {
            if(!checkSize.containsKey(c.sender))
                checkSize.put(c.sender, new ArrayList<Transaction>());
            
            checkSize.get(c.sender).add(c.tx);

            if(rounds == 1 && !tmpTransactions.contains(c.tx))
                blackListed[c.sender]= true;
        }


       for (Candidate c : candidates) {


            if (!blackListed[c.sender]) 
                pendingTransactions.add(c.tx);

        }

        System.out.println("This is the blacklist: " + Arrays.toString(blackListed));
        System.out.println("This size of pendingTransactions after the fact: " + tx.size());

        rounds-=1;
    }
}