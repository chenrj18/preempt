public class process {
    private int arrival;
    private int bursttime;
    private int priority;
    private int response;
    private int completiontime;
    private int initialbursttime;
    public process(int arrival, int bursttime, int priority){
        this.arrival = arrival;
        this.bursttime = bursttime;
        initialbursttime = bursttime;
        this.priority = priority;
        response = 0;
        completiontime = 0;
    }
    public void setResponse(int response) {
        this.response = response - arrival ;
    }
    public void setBursttime(int bursttime) {
        this.bursttime = bursttime;
    }
    public void setCompletiontime(int completiontime) {
        this.completiontime = completiontime;
    }
    public int getburstCompletion(){
        int comptime = completiontime - arrival;
        return comptime - getInitialbursttime() ;
    }
    public int getCompletiontime() {
        return completiontime;
    }
    public int getInitialbursttime() {
        return initialbursttime;
    }
    public int getPriority() {
        return priority;
    }
    public int getBursttime() {
        return bursttime;
    }
    public int getArrival() {
        return arrival;
    }
    public int getResponse() {
        return response;
    }
}
