import java.util.LinkedList;
import java.util.Queue;

public class PreemptB {
    public static void main(String[] args) {
        int timer = 0;
        int processes = 100;
        int timeslice = 5;
        double arrivalrate = .05;
        double prioritymix = 0.5;
        int bursttime = 20;
        int contextswitch = 1;
        int totalswitch = 0;
        Queue<process> high = new LinkedList<>();
        Queue<process> low = new LinkedList<>();
        Queue<process> finishedhigh = new LinkedList<>();
        Queue<process> finishedlow = new LinkedList<>();
        double arrivalcounter = 0;
        boolean switching = false;

        process running = new process(0, 0, -1);
        while (high.peek() != null || low.peek() != null || processes != 0) {
            if ((arrivalcounter == Math.floor(arrivalcounter)) && !Double.isInfinite(arrivalcounter)) {
                if (processes > 0) {
                    for (double ii = arrivalcounter; ii >= 1; ii--) {
                        if (Math.random() < prioritymix) {
                            process newprocess = new process(timer, bursttime, 0);
                            high.add(newprocess); // priority of 0 is high priority
                            processes--;
                        } else {
                            process newprocess = new process(timer, bursttime, 1);
                            low.add(newprocess);  // priority of 1 is low priority
                            processes--;
                        }
                    }
                    arrivalcounter = 0;
                }
            }

            if (running.getBursttime() == 0 && timer % timeslice == 0) { // switching process
                if (running.getPriority() == 0) {
                    finishedhigh.add(high.poll());
                } else if (running.getPriority() == 1) {
                    finishedlow.add(low.poll());
                }
                if (high.peek() != null && low.peek() != null) {
                    switching = false;
                } else {
                    switching = true;
                }
                if (high.peek() != null) {
                    running = high.peek();
                    if (switching) {
                        timer = timer + contextswitch;
                        totalswitch = totalswitch + contextswitch;
                        running.setResponse(timer);
                    } else {
                        running.setResponse(timer);
                    }
                } else if (low.peek() != null) {
                    running = low.peek();
                    if (switching) {
                        totalswitch = totalswitch + contextswitch;
                        timer = timer + contextswitch;
                        if (running.getBursttime() == running.getInitialbursttime()) {  // check if was preempted and should skip resonse time
                            running.setResponse(timer);
                        }
                    } else {
                        running.setResponse(timer);
                    }
                }
            }
            if (running.getPriority() == 1 && high.peek() != null && timer % timeslice == 0) {  // for time slice preemption
                running = high.peek();
                timer = timer + contextswitch;
                totalswitch = totalswitch + contextswitch;
                running.setResponse(timer);
            }
            timer++;
            if (running.getBursttime() > 0) {  // running
                running.setBursttime(running.getBursttime() - 1);
            }
            if (running.getBursttime() == 0) {
                running.setCompletiontime(timer);
            }
            arrivalcounter = arrivalcounter + arrivalrate;
            arrivalcounter = Math.round(arrivalcounter * 100.0) / 100.0;
        }
        double highresponse = 0;
        double lowresponse = 0;
        double totalhighburstcompletion = 0;
        double totallowburstcompletion = 0;
        double numhigh = finishedhigh.size();
        double numlow = finishedlow.size();
        for (int ii = 0; ii < numhigh; ii++) {
            highresponse = highresponse + finishedhigh.peek().getResponse();
            finishedhigh.add(finishedhigh.poll());
        }
        System.out.println("Average response time for high priority in type B: " + highresponse / numhigh);
        for (int ii = 0; ii < numlow; ii++) {
            lowresponse = lowresponse + finishedlow.peek().getResponse();
            finishedlow.add(finishedlow.poll());
        }
        System.out.println("Average response time for low priority in type B: " + lowresponse / numlow);
        for (int ii = 0; ii < numhigh; ii++) {
            totalhighburstcompletion = totalhighburstcompletion + finishedhigh.poll().getburstCompletion();
        }
        System.out.println("Average burst-completion time for high priority in type B: " + totalhighburstcompletion / numhigh);
        for (int ii = 0; ii < numlow; ii++) {
            totallowburstcompletion = totallowburstcompletion + finishedlow.poll().getburstCompletion();
        }
        System.out.println("Average burst-completion time for low priority in type B: " + totallowburstcompletion / numlow);
        System.out.println("% of time doing context switching for type B: " + (totalswitch / (double) timer) * 100 + "%");
    }
}
